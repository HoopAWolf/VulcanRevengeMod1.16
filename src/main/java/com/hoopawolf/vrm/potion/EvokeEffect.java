package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EvokeEffect extends Effect
{
    public EvokeEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int k = 80 >> amplifier;
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
            int charge = 3;
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 8, 8, 8, 10))
            {
                if (entityLivingBaseIn.canEntityBeSeen(entity))
                {
                    entityLivingBaseIn.world.addEntity(new EvokerFangsEntity(entityLivingBaseIn.world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, 3, entityLivingBaseIn));
                    --charge;
                }

                if (charge <= 0)
                {
                    break;
                }
            }
        }
    }
}
