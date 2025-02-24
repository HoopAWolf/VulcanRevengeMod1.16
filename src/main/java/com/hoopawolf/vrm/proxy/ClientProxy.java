package com.hoopawolf.vrm.proxy;

import com.hoopawolf.vrm.util.EntityRegistryHandler;
import com.hoopawolf.vrm.util.ItemBlockRegistryHandler;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy
{
    @SubscribeEvent
    public static void onClientSetUp(final FMLClientSetupEvent event)
    {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ItemBlockRegistryHandler::registerItemModelProperties);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EntityRegistryHandler::registerEntityRenderer);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> TileEntityRegistryHandler::registerTileEntityRenderer);
    }
}