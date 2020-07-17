package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.blocks.*;
import com.hoopawolf.dmm.items.RuneItem;
import com.hoopawolf.dmm.items.weapons.*;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ItemBlockRegistryHandler
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);

    //BLOCKS
    public static final RegistryObject<Block> SWORD_STONE_BLOCK = BLOCKS.register("swordstone", () -> new SwordStoneBlock(Block.Properties.create(Material.ANVIL).hardnessAndResistance(100)));
    public static final RegistryObject<Block> BLAZE_RUNE_BLOCK = BLOCKS.register("blazerune", () -> new BlazeRuneBlock(Block.Properties.create(Material.ANVIL).hardnessAndResistance(100)));
    public static final RegistryObject<Block> NETHER_RUNE_BLOCK = BLOCKS.register("netherrune", () -> new NetherRuneBlock(Block.Properties.create(Material.ANVIL).hardnessAndResistance(100)));
    public static final RegistryObject<Block> SCUTE_RUNE_BLOCK = BLOCKS.register("scuterune", () -> new ScuteRuneBlock(Block.Properties.create(Material.ANVIL).hardnessAndResistance(100)));
    public static final RegistryObject<Block> MAGMA_RUNE_BLOCK = BLOCKS.register("magmarune", () -> new MagmaRuneBlock(Block.Properties.create(Material.ANVIL).hardnessAndResistance(100)));

    //ITEMS
    public static final RegistryObject<Item> VULCAN_SWORD = ITEMS.register("vulcansrevenge", () -> new VulcanSwordItem(ItemTier.DIAMOND, 3, -2.5f, new Item.Properties().maxDamage(500)));
    public static final RegistryObject<Item> DEATH_SWORD = ITEMS.register("death", () -> new DeathSwordItem(ItemTier.DIAMOND, 3, -1.5f, new Item.Properties().maxDamage(1000)));
    public static final RegistryObject<Item> WAR_SWORD = ITEMS.register("war", () -> new WarSwordItem(ItemTier.DIAMOND, 3, -0.5f, new Item.Properties().maxDamage(1000)));
    public static final RegistryObject<Item> PES_BOW = ITEMS.register("pes", () -> new PesBowItem(new Item.Properties().maxStackSize(1).maxDamage(1000)));
    public static final RegistryObject<Item> FAM_SCALE = ITEMS.register("fam", () -> new FamScaleItem(new Item.Properties().maxStackSize(1).maxDamage(1000)));
    public static final RegistryObject<Item> RUNE_ITEM = ITEMS.register("rune", () -> new RuneItem(new Item.Properties().maxStackSize(1).group(VRMItemGroup.instance)));
    public static final RegistryObject<Item> ENDER_CLUSTER_ITEM = ITEMS.register("endercluster", () -> new Item(new Item.Properties().group(VRMItemGroup.instance)));
    public static final RegistryObject<Item> ENDERDLE_ITEM = ITEMS.register("enderdle", () -> new Item(new Item.Properties().group(VRMItemGroup.instance)));

    public static final RegistryObject<BlockItem> SWORD_STONE = ITEMS.register("swordstone",
            () -> new BlockItem(SWORD_STONE_BLOCK.get(), new Item.Properties().group(VRMItemGroup.instance)));
    public static final RegistryObject<BlockItem> BLAZE_RUNE = ITEMS.register("blazerune",
            () -> new BlockItem(BLAZE_RUNE_BLOCK.get(), new Item.Properties().group(VRMItemGroup.instance)));
    public static final RegistryObject<BlockItem> NETHER_RUNE = ITEMS.register("netherrune",
            () -> new BlockItem(NETHER_RUNE_BLOCK.get(), new Item.Properties().group(VRMItemGroup.instance)));
    public static final RegistryObject<BlockItem> SCUTE_RUNE = ITEMS.register("scuterune",
            () -> new BlockItem(SCUTE_RUNE_BLOCK.get(), new Item.Properties().group(VRMItemGroup.instance)));
    public static final RegistryObject<BlockItem> MAGMA_RUNE = ITEMS.register("magmarune",
            () -> new BlockItem(MAGMA_RUNE_BLOCK.get(), new Item.Properties().group(VRMItemGroup.instance)));


    public static void init(IEventBus _iEventBus)
    {
        BLOCKS.register(_iEventBus);
        ITEMS.register(_iEventBus);
    }

    public static void registerItemModelProperties()
    {
        ItemModelsProperties.func_239418_a_(PES_BOW.get(), new ResourceLocation("pull"),
                (p_239427_0_, p_239427_1_, p_239427_2_) ->
                {

                    if (p_239427_2_ == null)
                    {
                        return 0.0F;
                    } else
                    {
                        return !(p_239427_2_.getActiveItemStack().getItem() instanceof PesBowItem) ? 0.0F : (float) (p_239427_0_.getUseDuration() - p_239427_2_.getItemInUseCount()) / 20.0F;

                    }
                });

        ItemModelsProperties.func_239418_a_(PES_BOW.get(), new ResourceLocation("pulling"),
                (p_210309_0_, p_210309_1_, p_210309_2_) ->
                {
                    return p_210309_2_ != null && p_210309_2_.isHandActive() && p_210309_2_.getActiveItemStack() == p_210309_0_ ? 1.0F : 0.0F;
                });

        ItemModelsProperties.func_239418_a_(VULCAN_SWORD.get(), new ResourceLocation("type"),
                (p_210310_0_, p_210310_1_, p_210310_2_) -> VulcanSwordItem.getType(p_210310_0_));
    }
}