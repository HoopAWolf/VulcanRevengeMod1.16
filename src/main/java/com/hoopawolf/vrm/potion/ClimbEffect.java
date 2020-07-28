package com.hoopawolf.vrm.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ClimbEffect extends Effect
{
    public ClimbEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return true;
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.collidedHorizontally)
        {
            entityLivingBaseIn.setMotion(entityLivingBaseIn.getMotion().getX(), (entityLivingBaseIn.isCrouching()) ? -0.15 : 0.25, entityLivingBaseIn.getMotion().getZ());
            entityLivingBaseIn.velocityChanged = true;
        }

        if (!entityLivingBaseIn.world.isRemote)
        {
            entityLivingBaseIn.fallDistance = 0;
        }
    }
}

