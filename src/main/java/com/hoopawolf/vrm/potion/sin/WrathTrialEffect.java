package com.hoopawolf.vrm.potion.sin;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;

public class WrathTrialEffect extends Effect
{
    public WrathTrialEffect(EffectType typeIn, int liquidColorIn)
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
            for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityLivingBaseIn, 20, 2, 20, 30))
            {
                if (!entity.isPotionActive(PotionRegistryHandler.RAGE_EFFECT.get()))
                {
                    entity.addPotionEffect(new EffectInstance(PotionRegistryHandler.RAGE_EFFECT.get(), 200, 0));

                    if (entity instanceof CreatureEntity)
                    {
                        ((CreatureEntity) entity).setAttackTarget(entityLivingBaseIn);
                    }

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(entity.getPosX(), entity.getPosY() + 1.2F, entity.getPosZ()), new Vector3d(0.0F, -0.15D, 0.0F), 9, 8, entity.getWidth());
                    VRMPacketHandler.packetHandler.sendToDimension(entity.world.func_234923_W_(), spawnParticleMessage);
                }
            }
        }
    }
}