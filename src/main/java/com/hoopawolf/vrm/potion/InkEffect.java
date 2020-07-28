package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;

public class InkEffect extends Effect
{
    public InkEffect(EffectType typeIn, int liquidColorIn)
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
                if (entityLivingBaseIn.canEntityBeSeen(entity) && !entity.isPotionActive(Effects.BLINDNESS))
                {
                    entity.addPotionEffect(new EffectInstance(PotionRegistryHandler.FEAR_EFFECT.get(), 200, 0));
                    entity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 400, 0));

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(entity.getPosX(), entity.getPosY() + 1.2F, entity.getPosZ()), new Vector3d(0.0F, -0.15D, 0.0F), 9, 10, entity.getWidth());
                    VRMPacketHandler.packetHandler.sendToDimension(entity.world.func_234923_W_(), spawnParticleMessage);

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
