package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.PedestalTileEntity;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PedestalBlock extends Block
{
    public PedestalBlock(Block.Properties properties)
    {
        super(properties.notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return TileEntityRegistryHandler.PEDESTAL_TILE_ENTITY.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (hasTileEntity(state))
        {
            PedestalTileEntity rune = (PedestalTileEntity) worldIn.getTileEntity(pos);

            if (rune.getStoredItem().getItem() != Items.AIR)
            {
                dropItem(state, pos, worldIn, player);
            } else
            {
                rune.setStoredItem(player.getHeldItemMainhand().copy());
                rune.getStoredItem().setCount(1);

                if (!worldIn.isRemote)
                {
                    if (!player.isCreative())
                    {
                        player.getHeldItemMainhand().shrink(1);
                    }
                }
            }
            rune.markDirty();
        }

        return ActionResultType.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasTileEntity(state))
        {
            PedestalTileEntity rune = (PedestalTileEntity) worldIn.getTileEntity(pos);

            if (!worldIn.isRemote)
            {
                player.dropItem(rune.getStoredItem(), true);
            }

            rune.setStoredItem(new ItemStack(Items.AIR));
            rune.markDirty();
        }
    }
}
