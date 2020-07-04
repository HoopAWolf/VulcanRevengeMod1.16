package com.hoopawolf.dmm.tab;

import com.hoopawolf.dmm.util.ItemBlockRegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VRMItemGroup extends ItemGroup
{
    public static final VRMItemGroup instance = new VRMItemGroup(ItemGroup.GROUPS.length, "vrmItemGroup");

    public VRMItemGroup(int index, String label)
    {
        super(index, label);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ItemBlockRegistryHandler.VULCAN_SWORD.get());
    }
}