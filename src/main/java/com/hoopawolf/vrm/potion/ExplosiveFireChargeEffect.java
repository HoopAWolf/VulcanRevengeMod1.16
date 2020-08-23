package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;

public class ExplosiveFireChargeEffect extends Effect
{
    public ExplosiveFireChargeEffect(EffectType typeIn, int liquidColorIn)
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
            int charge = 1;
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 10, 5, 10, 10))
            {
                if (entityLivingBaseIn.canEntityBeSeen(entity))
                {
                    Vector3d vector3d = entityLivingBaseIn.getLook(1.0F);
                    double d2 = entity.getPosX() - (entityLivingBaseIn.getPosX() + vector3d.x * 4.0D);
                    double d3 = entity.getPosYHeight(0.5D) - (0.5D + entityLivingBaseIn.getPosYHeight(0.5D));
                    double d4 = entity.getPosZ() - (entityLivingBaseIn.getPosZ() + vector3d.z * 4.0D);

                    entityLivingBaseIn.world.playEvent(null, 1018, entityLivingBaseIn.getPosition(), 0);

                    FireballEntity fireballentity = new FireballEntity(entityLivingBaseIn.world, entityLivingBaseIn, d2, d3, d4);
                    fireballentity.explosionPower = 1;
                    fireballentity.setPosition(entityLivingBaseIn.getPosX() + vector3d.x * 4.0D, entityLivingBaseIn.getPosYHeight(0.5D) + 1.5D, entityLivingBaseIn.getPosZ() + vector3d.z * 4.0D);
                    entityLivingBaseIn.world.addEntity(fireballentity);
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
