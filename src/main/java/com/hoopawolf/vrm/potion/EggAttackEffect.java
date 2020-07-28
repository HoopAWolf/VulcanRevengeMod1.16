package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class EggAttackEffect extends Effect
{
    public EggAttackEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        int k = 30 >> amplifier;
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
            int charge = 1;
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 10, 5, 10, 10))
            {
                EggEntity snowballentity = new EggEntity(entityLivingBaseIn.world, entityLivingBaseIn);
                double d0 = entity.getPosYEye() - (double) 1.1F;
                double d1 = entity.getPosX() - entityLivingBaseIn.getPosX();
                double d2 = d0 - snowballentity.getPosY();
                double d3 = entity.getPosZ() - entityLivingBaseIn.getPosZ();
                float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
                snowballentity.shoot(d1, d2 + (double) f, d3, 1.6F, 12.0F);
                entityLivingBaseIn.playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (entityLivingBaseIn.getRNG().nextFloat() * 0.4F + 0.8F));
                entityLivingBaseIn.world.addEntity(snowballentity);
                --charge;

                if (charge <= 0)
                {
                    break;
                }
            }
        }
    }
}