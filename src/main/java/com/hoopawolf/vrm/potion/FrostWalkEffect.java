package com.hoopawolf.vrm.potion;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class FrostWalkEffect extends Effect
{
    public FrostWalkEffect(EffectType typeIn, int liquidColorIn)
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
        if (!entityLivingBaseIn.world.isRemote)
        {
            if (entityLivingBaseIn.world.getBlockState(entityLivingBaseIn.getPosition().down()).getBlock().equals(Blocks.WATER))
            {
                entityLivingBaseIn.world.setBlockState(entityLivingBaseIn.getPosition().down(), Blocks.ICE.getDefaultState());
            } else if (entityLivingBaseIn.world.getBlockState(entityLivingBaseIn.getPosition().down()).getBlock().equals(Blocks.LAVA))
            {
                entityLivingBaseIn.world.setBlockState(entityLivingBaseIn.getPosition().down(), Blocks.OBSIDIAN.getDefaultState());
            }
        }
    }
}

