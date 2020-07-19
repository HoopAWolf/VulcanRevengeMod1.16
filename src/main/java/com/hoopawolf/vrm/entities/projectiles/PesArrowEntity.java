package com.hoopawolf.vrm.entities.projectiles;

import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.EntityRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class PesArrowEntity extends AbstractArrowEntity
{
    public PesArrowEntity(EntityType<? extends AbstractArrowEntity> p_i50172_1_, World p_i50172_2_)
    {
        super(p_i50172_1_, p_i50172_2_);
        this.pickupStatus = PickupStatus.DISALLOWED;
    }

    public PesArrowEntity(World worldIn, double x, double y, double z)
    {
        super(EntityRegistryHandler.PES_ARROW_ENTITY.get(), x, y, z, worldIn);
    }

    public PesArrowEntity(World worldIn, LivingEntity shooter)
    {
        super(EntityRegistryHandler.PES_ARROW_ENTITY.get(), shooter, worldIn);
        this.pickupStatus = PickupStatus.DISALLOWED;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!this.world.isRemote)
        {
            if (this.inGround && this.timeInGround != 0)
            {
                this.world.setEntityState(this, (byte) 0);
                SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ()), new Vector3d(0.0D, 0.0D, 0.0D), 3, 5, 0.0F);
                VRMPacketHandler.packetHandler.sendToDimension(this.world.func_234923_W_(), spawnParticleMessage);
            }

            Vector3d vec3d = this.getMotion();
            double d3 = vec3d.x;
            double d4 = vec3d.y;
            double d0 = vec3d.z;

            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(this.getPosX() + d3, this.getPosY() + d4, this.getPosZ() + d0), new Vector3d(0.0D, 0.0D, 0.0D), 3, 5, 0.0F);
            VRMPacketHandler.packetHandler.sendToDimension(this.world.func_234923_W_(), spawnParticleMessage);
        }
    }

    @Override
    protected void func_225516_i_()
    {
        if (this.timeInGround >= 1)
        {
            this.remove();
        }
    }

    @Override
    public float getBrightness()
    {
        return 15;
    }

    @Override
    protected void arrowHit(LivingEntity living)
    {
        super.arrowHit(living);
        if (!this.world.isRemote)
        {
            living.addPotionEffect(new EffectInstance(PotionRegistryHandler.PLAGUE_EFFECT.get(), 1000, 1));
        }
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}

