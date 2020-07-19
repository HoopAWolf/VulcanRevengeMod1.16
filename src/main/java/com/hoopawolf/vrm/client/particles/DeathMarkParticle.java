package com.hoopawolf.vrm.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeathMarkParticle extends SpriteTexturedParticle
{
    private DeathMarkParticle(ClientWorld p_i51030_1_, double p_i51030_2_, double p_i51030_4_, double p_i51030_6_)
    {
        super(p_i51030_1_, p_i51030_2_, p_i51030_4_, p_i51030_6_, 0.0D, 0.0D, 0.0D);
        this.particleGravity = 0.0F;
        this.maxAge = 1;
        this.canCollide = false;
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getScale(float scaleFactor)
    {
        return 0.5F;
    }

    @Override
    public int getBrightnessForRender(float partialTick)
    {
        return 15;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite p_i50823_1_)
        {
            this.spriteSet = p_i50823_1_;
        }

        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            DeathMarkParticle flameparticle = new DeathMarkParticle(worldIn, x, y, z);
            flameparticle.selectSpriteRandomly(this.spriteSet);
            return flameparticle;
        }
    }
}
