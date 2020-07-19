package com.hoopawolf.vrm;

import com.hoopawolf.vrm.config.ConfigHandler;
import com.hoopawolf.vrm.proxy.ClientProxy;
import com.hoopawolf.vrm.proxy.CommonProxy;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class VulcanRevengeMod
{
    public VulcanRevengeMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(ClientProxy::onClientSetUp);
        modEventBus.addListener(CommonProxy::onCommonSetupEvent);

        ItemBlockRegistryHandler.init(modEventBus);
        ArmorRegistryHandler.init(modEventBus);
        EntityRegistryHandler.ENTITIES.register(modEventBus);
        TileEntityRegistryHandler.TILE_ENTITIES.register(modEventBus);
        ParticleRegistryHandler.PARTICLES.register(modEventBus);
        PotionRegistryHandler.init(modEventBus);
        StructureRegistryHandler.init(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
