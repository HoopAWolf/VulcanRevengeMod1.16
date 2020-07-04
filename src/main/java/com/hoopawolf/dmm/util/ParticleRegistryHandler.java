package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.client.particles.DeathMarkParticle;
import com.hoopawolf.dmm.client.particles.PlagueParticle;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistryHandler
{
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);

    //PARTICLES
    public static final RegistryObject<BasicParticleType> DEATH_MARK_PARTICLE = PARTICLES.register("death", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> PLAGUE_PARTICLE = PARTICLES.register("plague", () -> new BasicParticleType(false));

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event)
    {
        ParticleManager particles = Minecraft.getInstance().particles;
        particles.registerFactory(DEATH_MARK_PARTICLE.get(), DeathMarkParticle.Factory::new);
        particles.registerFactory(PLAGUE_PARTICLE.get(), PlagueParticle.Factory::new);
    }
}
