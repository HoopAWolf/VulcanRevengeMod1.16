package com.hoopawolf.vrm.entities.ai;

import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;

public class LustAvoidEntityGoal extends AvoidEntityGoal
{

    public LustAvoidEntityGoal(CreatureEntity entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean shouldExecute()
    {
        return super.shouldExecute() && avoidTarget.isPotionActive(PotionRegistryHandler.LUST_TRIAL_EFFECT.get());
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return super.shouldContinueExecuting() && avoidTarget.isPotionActive(PotionRegistryHandler.LUST_TRIAL_EFFECT.get());
    }
}
