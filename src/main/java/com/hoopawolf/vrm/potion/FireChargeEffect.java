package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.MathHelper;

public class FireChargeEffect extends Effect
{
    public FireChargeEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int k = 50 >> amplifier;
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
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 10, 5, 10, 10))
            {
                if (entityLivingBaseIn.canEntityBeSeen(entity))
                {
                    double d0 = entityLivingBaseIn.getDistanceSq(entity);
                    double d1 = entity.getPosX() - entityLivingBaseIn.getPosX();
                    double d2 = entity.getPosYHeight(0.5D) - entityLivingBaseIn.getPosYHeight(0.5D);
                    double d3 = entity.getPosZ() - entityLivingBaseIn.getPosZ();
                    float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;

                    entityLivingBaseIn.world.playEvent(null, 1018, entityLivingBaseIn.getPosition(), 0);

                    SmallFireballEntity smallfireballentity = new SmallFireballEntity(entityLivingBaseIn.world, entityLivingBaseIn, d1 + entityLivingBaseIn.getRNG().nextGaussian() * (double) f, d2, d3 + entityLivingBaseIn.getRNG().nextGaussian() * (double) f);
                    smallfireballentity.setPosition(smallfireballentity.getPosX(), entityLivingBaseIn.getPosYHeight(0.5D) + 0.5D, smallfireballentity.getPosZ());
                    entityLivingBaseIn.world.addEntity(smallfireballentity);
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
