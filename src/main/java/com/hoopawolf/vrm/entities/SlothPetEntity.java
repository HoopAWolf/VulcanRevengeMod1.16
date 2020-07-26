package com.hoopawolf.vrm.entities;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SlothPetEntity extends CreatureEntity
{
    protected static final DataParameter<Byte> CHARGE_FLAG = EntityDataManager.createKey(SlothPetEntity.class, DataSerializers.BYTE);
    private PlayerEntity owner;
    @Nullable
    private BlockPos boundOrigin;

    public SlothPetEntity(EntityType<? extends SlothPetEntity> p_i50190_1_, World p_i50190_2_)
    {
        super(p_i50190_1_, p_i50190_2_);
        this.moveController = new SlothPetEntity.MoveHelperController(this);
    }

    public static AttributeModifierMap.MutableAttribute func_234321_m_()
    {
        return MonsterEntity.func_234295_eP_().func_233815_a_(Attributes.MAX_HEALTH, 1.0D).func_233815_a_(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void move(MoverType typeIn, Vector3d pos)
    {
        super.move(typeIn, pos);
        this.doBlockCollisions();
    }

    @Override
    public void tick()
    {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);

        if (!world.isRemote)
        {
            if (boundOrigin == null || owner == null || (int) owner.getPosX() != boundOrigin.getX() || (int) owner.getPosZ() != boundOrigin.getZ() ||
                    !(owner.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(owner.inventory.armorInventory.get(3))))
            {
                this.remove();
            }

            if (getAttackTarget() != null)
            {
                SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ()), new Vector3d(0.0F, 0.0D, 0.0F), 2, 2, 0);
                VRMPacketHandler.packetHandler.sendToDimension(this.world.func_234923_W_(), spawnParticleMessage);

                if (!getAttackTarget().isAlive())
                {
                    setAttackTarget(null);
                }
            }
        }
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new SlothPetEntity.ChargeAttackGoal());
        this.goalSelector.addGoal(8, new SlothPetEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(CHARGE_FLAG, (byte) 0);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        if (compound.contains("BoundX"))
        {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        if (this.boundOrigin != null)
        {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }
    }

    public PlayerEntity getOwner()
    {
        return this.owner;
    }

    public void setOwner(PlayerEntity ownerIn)
    {
        this.owner = ownerIn;
    }

    @Nullable
    public BlockPos getBoundOrigin()
    {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn)
    {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getChargeFlag(int mask)
    {
        int i = this.dataManager.get(CHARGE_FLAG);
        return (i & mask) != 0;
    }

    private void setChargeFlag(int mask, boolean value)
    {
        int i = this.dataManager.get(CHARGE_FLAG);
        if (value)
        {
            i = i | mask;
        } else
        {
            i = i & ~mask;
        }

        this.dataManager.set(CHARGE_FLAG, (byte) (i & 255));
    }

    public boolean isCharging()
    {
        return this.getChargeFlag(1);
    }

    public void setCharging(boolean charging)
    {
        this.setChargeFlag(1, charging);
    }

    @Override
    public float getBrightness()
    {
        return 1.0F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!world.isRemote)
        {
            if (source.getImmediateSource() != null)
            {
                if (!owner.isCreative())
                {
                    owner.attackEntityFrom(source, amount * 0.2F);
                }
                return super.attackEntityFrom(source, amount);
            }
        }

        return false;
    }

    class ChargeAttackGoal extends Goal
    {
        public ChargeAttackGoal()
        {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute()
        {
            return SlothPetEntity.this.getAttackTarget() != null && SlothPetEntity.this.rand.nextInt(3) == 0;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return SlothPetEntity.this.getMoveHelper().isUpdating() && SlothPetEntity.this.isCharging() && SlothPetEntity.this.getAttackTarget() != null && SlothPetEntity.this.getAttackTarget().isAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting()
        {
            LivingEntity livingentity = SlothPetEntity.this.getAttackTarget();
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            SlothPetEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            SlothPetEntity.this.setCharging(true);
            SlothPetEntity.this.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 1.0F, 0.5F);
        }

        @Override
        public void resetTask()
        {
            SlothPetEntity.this.setCharging(false);
        }

        @Override
        public void tick()
        {
            LivingEntity livingentity = SlothPetEntity.this.getAttackTarget();
            if (SlothPetEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox()))
            {
                SlothPetEntity.this.attackEntityAsMob(livingentity);
                SlothPetEntity.this.setCharging(false);
            } else
            {
                Vector3d vector3d = livingentity.getEyePosition(1.0F);
                SlothPetEntity.this.moveController.setMoveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            }

        }
    }

    class MoveHelperController extends MovementController
    {
        public MoveHelperController(SlothPetEntity vex)
        {
            super(vex);
        }

        @Override
        public void tick()
        {
            if (this.action == MovementController.Action.MOVE_TO)
            {
                Vector3d vector3d = new Vector3d(this.posX - SlothPetEntity.this.getPosX(), this.posY - SlothPetEntity.this.getPosY(), this.posZ - SlothPetEntity.this.getPosZ());
                double d0 = vector3d.length();
                if (d0 < SlothPetEntity.this.getBoundingBox().getAverageEdgeLength())
                {
                    this.action = MovementController.Action.WAIT;
                    SlothPetEntity.this.setMotion(SlothPetEntity.this.getMotion().scale(0.5D));
                } else
                {
                    SlothPetEntity.this.setMotion(SlothPetEntity.this.getMotion().add(vector3d.scale(this.speed * 0.05D / d0)));
                    if (SlothPetEntity.this.getAttackTarget() == null)
                    {
                        Vector3d vector3d1 = SlothPetEntity.this.getMotion();
                        SlothPetEntity.this.rotationYaw = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                    } else
                    {
                        double d2 = SlothPetEntity.this.getAttackTarget().getPosX() - SlothPetEntity.this.getPosX();
                        double d1 = SlothPetEntity.this.getAttackTarget().getPosZ() - SlothPetEntity.this.getPosZ();
                        SlothPetEntity.this.rotationYaw = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                    }
                    SlothPetEntity.this.renderYawOffset = SlothPetEntity.this.rotationYaw;
                }

            }
        }
    }

    class MoveRandomGoal extends Goal
    {
        public MoveRandomGoal()
        {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        @Override
        public boolean shouldExecute()
        {
            return !SlothPetEntity.this.getMoveHelper().isUpdating() && SlothPetEntity.this.rand.nextInt(7) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting()
        {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick()
        {
            BlockPos blockpos = SlothPetEntity.this.getBoundOrigin();
            if (blockpos == null)
            {
                blockpos = SlothPetEntity.this.func_233580_cy_();
            }

            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos1 = blockpos.add(SlothPetEntity.this.rand.nextInt(15) - 7, SlothPetEntity.this.rand.nextInt(11) - 5, SlothPetEntity.this.rand.nextInt(15) - 7);
                if (SlothPetEntity.this.world.isAirBlock(blockpos1))
                {
                    SlothPetEntity.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                    if (SlothPetEntity.this.getAttackTarget() == null)
                    {
                        SlothPetEntity.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}