package com.hoopawolf.dmm.client.particles;

import com.hoopawolf.dmm.helper.EntityHelper;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.server.SetPotionEffectMultipleMessage;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlagueParticle extends SpriteTexturedParticle
{
    private PlagueParticle(ClientWorld p_i51010_1_, double p_i51010_2_, double p_i51010_4_, double p_i51010_6_, double p_i51010_8_, double p_i51010_10_, double p_i51010_12_, float p_i51010_14_)
    {
        super(p_i51010_1_, p_i51010_2_, p_i51010_4_, p_i51010_6_, 0.0D, 0.0D, 0.0D);
        this.motionX += p_i51010_8_;
        this.motionZ += p_i51010_12_;
        this.motionX *= 0.9F;
        this.motionY *= -0.1F;
        this.motionZ *= 0.9F;
        this.particleRed = 0.5F;
        this.particleGreen = 0.7F;
        this.particleBlue = 0.0F;
        this.particleScale *= 0.75F * p_i51010_14_;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.maxAge = (int) ((float) this.maxAge * p_i51010_14_);
        this.maxAge = Math.max(this.maxAge, 1);
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
        return this.particleScale * MathHelper.clamp(((float) this.age + scaleFactor) / (float) this.maxAge * 32.0F, 0.0F, 0.5F);
    }

    @Override
    public void tick()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge)
        {
            this.setExpired();
        } else
        {
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY)
            {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= 0.96F;
            this.motionZ *= 0.96F;
            if (this.onGround)
            {
                this.motionX *= 0.7F;
                this.motionZ *= 0.7F;
            }

        }

        for (LivingEntity entity : EntityHelper.getEntitiesNearbyWithPos(world, getBoundingBox(), new BlockPos(this.posX, this.posY, this.posZ), LivingEntity.class, 2, 2, 2, 3))
        {
            SetPotionEffectMultipleMessage _messagePoison = new SetPotionEffectMultipleMessage(entity.getEntityId(), 300, 1, 0, 3);
            VRMPacketHandler.channel.sendToServer(_messagePoison);
        }
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
            PlagueParticle flameparticle = new PlagueParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 5);
            flameparticle.selectSpriteRandomly(this.spriteSet);
            return flameparticle;
        }
    }
}
