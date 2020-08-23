package com.hoopawolf.vrm.potion.sin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SlothTrialEffect extends Effect
{
    private final EntityType[] spawnList = {
            EntityType.ENDERMAN
    };

    public SlothTrialEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int k = 380 >> amplifier;
        if (k > 0)
        {
            return duration % k == 0;
        } else
        {
            return true;
        }
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.world.isRemote)
        {
            teleportRandomly(entityLivingBaseIn);

            if (entityLivingBaseIn.world.rand.nextInt(100) < 20)
            {
                int rand = entityLivingBaseIn.world.rand.nextInt(1) + 1;

                while (rand != 0)
                {
                    double x = entityLivingBaseIn.getPosX() + (entityLivingBaseIn.world.rand.nextDouble() - 0.5D) * 10.0D;
                    double y = entityLivingBaseIn.getPosY() + (double) (entityLivingBaseIn.world.rand.nextInt(3) - 1);
                    double z = entityLivingBaseIn.getPosZ() + (entityLivingBaseIn.world.rand.nextDouble() - 0.5D) * 10.0D;

                    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

                    while (blockpos$mutable.getY() > 0 && !entityLivingBaseIn.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement())
                    {
                        blockpos$mutable.move(Direction.DOWN);
                    }

                    BlockState blockstate = entityLivingBaseIn.world.getBlockState(blockpos$mutable);
                    boolean flag = blockstate.getMaterial().blocksMovement();
                    boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
                    if (flag && !flag1)
                    {
                        MobEntity entity = (MobEntity) spawnList[entityLivingBaseIn.world.rand.nextInt(spawnList.length)].create(entityLivingBaseIn.world);
                        entity.setLocationAndAngles(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ(), 0.0F, 0.0F);
                        entity.setAttackTarget(entityLivingBaseIn);
                        entityLivingBaseIn.world.addEntity(entity);
                        --rand;
                    }
                }
            }
        }
    }

    protected boolean teleportRandomly(LivingEntity entityLivingBaseIn)
    {
        if (entityLivingBaseIn.isAlive())
        {
            double d0 = entityLivingBaseIn.getPosX() + (entityLivingBaseIn.world.rand.nextDouble() - 0.5D) * 10.0D;
            double d1 = entityLivingBaseIn.getPosY() + (double) (entityLivingBaseIn.world.rand.nextInt(3) - 1);
            double d2 = entityLivingBaseIn.getPosZ() + (entityLivingBaseIn.world.rand.nextDouble() - 0.5D) * 10.0D;
            return this.teleportTo(entityLivingBaseIn, d0, d1, d2);
        } else
        {
            return false;
        }
    }

    private boolean teleportTo(LivingEntity entityLivingBaseIn, double x, double y, double z)
    {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while (blockpos$mutable.getY() > 0 && !entityLivingBaseIn.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement())
        {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = entityLivingBaseIn.world.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMovement();
        boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
        if (flag && !flag1)
        {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entityLivingBaseIn, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;

            return entityLivingBaseIn.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
        } else
        {
            return false;
        }
    }
}
