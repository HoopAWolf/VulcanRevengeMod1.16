package com.hoopawolf.vrm.potion;

import com.hoopawolf.vrm.helper.EntityHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class BowAttackEffect extends Effect
{
    public BowAttackEffect(EffectType typeIn, int liquidColorIn)
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
                AbstractArrowEntity abstractarrowentity = ProjectileHelper.fireArrow(entityLivingBaseIn, new ItemStack(Items.ARROW), BowItem.getArrowVelocity(20));

                double d0 = entity.getPosX() - entityLivingBaseIn.getPosX();
                double d1 = entity.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
                double d2 = entity.getPosZ() - entityLivingBaseIn.getPosZ();
                double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                abstractarrowentity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - entityLivingBaseIn.world.getDifficulty().getId() * 4));
                entityLivingBaseIn.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (entityLivingBaseIn.getRNG().nextFloat() * 0.4F + 0.8F));
                entityLivingBaseIn.world.addEntity(abstractarrowentity);

                --charge;

                if (charge <= 0)
                {
                    break;
                }
            }
        }
    }
}
