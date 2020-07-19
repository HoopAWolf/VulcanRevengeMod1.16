package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;

public class BlazeRuneTileEntity extends RuneTileEntity
{
    public BlazeRuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public BlazeRuneTileEntity()
    {
        this(TileEntityRegistryHandler.BLAZE_RUNE_TILE_ENTITY.get());
    }

    @Override
    public Item getActivationItem()
    {
        return Items.BLAZE_POWDER;
    }

    @Override
    public Vector3i getRayColor()
    {
        return new Vector3i(255, 255, 127);
    }
}