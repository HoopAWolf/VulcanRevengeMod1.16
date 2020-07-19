package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;

public class NetherRuneTileEntity extends RuneTileEntity
{
    public NetherRuneTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public NetherRuneTileEntity()
    {
        this(TileEntityRegistryHandler.NETHER_RUNE_TILE_ENTITY.get());
    }

    @Override
    public Item getActivationItem()
    {
        return Items.NETHER_BRICK;
    }

    @Override
    public Vector3i getRayColor()
    {
        return new Vector3i(255, 255, 255);
    }
}