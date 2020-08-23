package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.blocks.tileentity.*;
import com.hoopawolf.vrm.client.tileentity.*;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class TileEntityRegistryHandler
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);

    //TILE ENTITES
    public static final RegistryObject<TileEntityType<SwordStoneTileEntity>> SWORD_STONE_TILE_ENTITY = TILE_ENTITIES.register("swordstone", () ->
            TileEntityType.Builder.create(SwordStoneTileEntity::new, ItemBlockRegistryHandler.SWORD_STONE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL_TILE_ENTITY = TILE_ENTITIES.register("pedestal", () ->
            TileEntityType.Builder.create(PedestalTileEntity::new, ItemBlockRegistryHandler.PEDESTAL_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<AlterTileEntity>> ALTER_TILE_ENTITY = TILE_ENTITIES.register("alter", () ->
            TileEntityType.Builder.create(AlterTileEntity::new, ItemBlockRegistryHandler.ALTER_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<BlazeRuneTileEntity>> BLAZE_RUNE_TILE_ENTITY = TILE_ENTITIES.register("blazerune", () ->
            TileEntityType.Builder.create(BlazeRuneTileEntity::new, ItemBlockRegistryHandler.BLAZE_RUNE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<NetherRuneTileEntity>> NETHER_RUNE_TILE_ENTITY = TILE_ENTITIES.register("netherrune", () ->
            TileEntityType.Builder.create(NetherRuneTileEntity::new, ItemBlockRegistryHandler.NETHER_RUNE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<ScuteRuneTileEntity>> SCUTE_RUNE_TILE_ENTITY = TILE_ENTITIES.register("scuterune", () ->
            TileEntityType.Builder.create(ScuteRuneTileEntity::new, ItemBlockRegistryHandler.SCUTE_RUNE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<MagmaRuneTileEntity>> MAGMA_RUNE_TILE_ENTITY = TILE_ENTITIES.register("magmarune", () ->
            TileEntityType.Builder.create(MagmaRuneTileEntity::new, ItemBlockRegistryHandler.MAGMA_RUNE_BLOCK.get()).build(null));

    public static void init(IEventBus _iEventBus)
    {
        TILE_ENTITIES.register(_iEventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerTileEntityRenderer()
    {
        RenderTypeLookup.setRenderLayer(ItemBlockRegistryHandler.SWORD_STONE_BLOCK.get(), RenderType.getCutoutMipped());
        ClientRegistry.bindTileEntityRenderer(SWORD_STONE_TILE_ENTITY.get(),
                SwordStoneRenderer::new);

        RenderTypeLookup.setRenderLayer(ItemBlockRegistryHandler.PEDESTAL_BLOCK.get(), RenderType.getCutoutMipped());
        ClientRegistry.bindTileEntityRenderer(PEDESTAL_TILE_ENTITY.get(),
                PedestalRenderer::new);

        RenderTypeLookup.setRenderLayer(ItemBlockRegistryHandler.ALTER_BLOCK.get(), RenderType.getCutoutMipped());
        ClientRegistry.bindTileEntityRenderer(ALTER_TILE_ENTITY.get(),
                AlterRenderer::new);

        ClientRegistry.bindTileEntityRenderer(BLAZE_RUNE_TILE_ENTITY.get(),
                BlazeRuneRenderer::new);
        ClientRegistry.bindTileEntityRenderer(NETHER_RUNE_TILE_ENTITY.get(),
                NetherRuneRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SCUTE_RUNE_TILE_ENTITY.get(),
                ScuteRuneRenderer::new);
        ClientRegistry.bindTileEntityRenderer(MAGMA_RUNE_TILE_ENTITY.get(),
                MagmaRuneRenderer::new);
    }
}
