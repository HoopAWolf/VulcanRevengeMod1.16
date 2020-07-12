package com.hoopawolf.dmm.proxy;

import com.hoopawolf.dmm.helper.VRMEatItemDataHandler;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.util.StructureRegistryHandler;
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
        VRMEatItemDataHandler.INSTANCE.initJSON();
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event)
    {
        DeferredWorkQueue.runLater(StructureRegistryHandler::generateStructureWorldSpawn);
    }
}