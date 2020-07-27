package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.util.ArmorRegistryHandler;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class LustSinTemptGoal extends Goal
{
    private static final EntityPredicate ENTITY_PREDICATE = (new EntityPredicate()).setDistance(20.0D).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setLineOfSiteRequired();
    protected final CreatureEntity creature;
    private final double speed;
    protected PlayerEntity closestPlayer;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    private int delayTemptCounter;

    public LustSinTemptGoal(CreatureEntity creatureIn, double speedIn)
    {
        this.creature = creatureIn;
        this.speed = speedIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.delayTemptCounter > 0)
        {
            --this.delayTemptCounter;
            return false;
        } else
        {
            this.closestPlayer = this.creature.world.getClosestPlayer(ENTITY_PREDICATE, this.creature);
            if (this.closestPlayer == null)
            {
                return false;
            } else
            {
                return closestPlayer.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.LUST_MASK_ARMOR.get()) && SinsArmorItem.isActivated(closestPlayer.inventory.armorInventory.get(3));
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (this.creature.getDistanceSq(this.closestPlayer) < 36.0D)
        {
            if (this.closestPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
            {
                return false;
            }

            if (Math.abs((double) this.closestPlayer.rotationPitch - this.pitch) > 5.0D || Math.abs((double) this.closestPlayer.rotationYaw - this.yaw) > 5.0D)
            {
                return false;
            }
        } else
        {
            this.targetX = this.closestPlayer.getPosX();
            this.targetY = this.closestPlayer.getPosY();
            this.targetZ = this.closestPlayer.getPosZ();
        }

        this.pitch = this.closestPlayer.rotationPitch;
        this.yaw = this.closestPlayer.rotationYaw;

        return this.shouldExecute();
    }

    @Override
    public void startExecuting()
    {
        this.targetX = this.closestPlayer.getPosX();
        this.targetY = this.closestPlayer.getPosY();
        this.targetZ = this.closestPlayer.getPosZ();
    }

    @Override
    public void resetTask()
    {
        this.closestPlayer = null;
        this.creature.getNavigator().clearPath();
        this.delayTemptCounter = 100;
    }

    @Override
    public void tick()
    {
        this.creature.getLookController().setLookPositionWithEntity(this.closestPlayer, (float) (this.creature.getHorizontalFaceSpeed() + 20), (float) this.creature.getVerticalFaceSpeed());

        if (this.creature.ticksExisted % 3 == 0)
        {
            SinsArmorItem.increaseFulfilment(closestPlayer.inventory.armorInventory.get(3), 1, SinsArmorItem.getSin(closestPlayer.inventory.armorInventory.get(3)).getMaxUse());
        }

        if (this.creature.getDistanceSq(this.closestPlayer) < 6.25D)
        {
            this.creature.getNavigator().clearPath();
        } else
        {
            this.creature.getNavigator().tryMoveToEntityLiving(this.closestPlayer, this.speed);
        }

    }
}