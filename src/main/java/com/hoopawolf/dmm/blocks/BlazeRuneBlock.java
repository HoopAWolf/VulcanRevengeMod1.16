package com.hoopawolf.dmm.blocks;

import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlazeRuneBlock extends RuneBlock
{

    public BlazeRuneBlock(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return TileEntityRegistryHandler.BLAZE_RUNE_TILE_ENTITY.get().create();
    }
}
