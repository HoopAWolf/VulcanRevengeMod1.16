package com.hoopawolf.vrm.proxy;

import com.hoopawolf.vrm.helper.VRMEatItemDataHandler;
import com.hoopawolf.vrm.helper.VRMGreedItemDataHandler;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.util.EntityRegistryHandler;
import com.hoopawolf.vrm.util.StructureRegistryHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy
{
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event)
    {
        VRMPacketHandler.init();
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event)
    {
        DeferredWorkQueue.runLater(StructureRegistryHandler::generateStructureWorldSpawn);
        DeferredWorkQueue.runLater(VRMEatItemDataHandler.INSTANCE::initJSON);
        DeferredWorkQueue.runLater(VRMGreedItemDataHandler.INSTANCE::initJSON);
        DeferredWorkQueue.runLater(EntityRegistryHandler::registerEntityAttributes);
    }
}