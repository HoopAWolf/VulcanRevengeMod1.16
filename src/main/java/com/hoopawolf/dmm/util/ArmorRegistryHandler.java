package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.items.armors.SinsArmorItem;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArmorRegistryHandler
{
    public static final DeferredRegister<Item> ARMOR = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    //ITEMS
    public static final RegistryObject<ArmorItem> GLUTTONY_MASK_ARMOR = ARMOR.register("gluttonymask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.GLUTTONY));
    public static final RegistryObject<ArmorItem> ENVY_MASK_ARMOR = ARMOR.register("envymask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.ENVY));
    public static final RegistryObject<ArmorItem> LUST_MASK_ARMOR = ARMOR.register("lustmask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.LUST));
    public static final RegistryObject<ArmorItem> GREED_MASK_ARMOR = ARMOR.register("greedmask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.GREED));
    public static final RegistryObject<ArmorItem> SLOTH_MASK_ARMOR = ARMOR.register("slothmask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.SLOTH));
    public static final RegistryObject<ArmorItem> WRATH_MASK_ARMOR = ARMOR.register("wrathmask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.WRATH));
    public static final RegistryObject<ArmorItem> PRIDE_MASK_ARMOR = ARMOR.register("pridemask", () -> new SinsArmorItem(ArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(VRMItemGroup.instance), SinsArmorItem.SINS.PRIDE));

    public static void init(IEventBus _iEventBus)
    {
        ARMOR.register(_iEventBus);
    }
}
