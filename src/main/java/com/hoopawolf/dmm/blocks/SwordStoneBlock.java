package com.hoopawolf.dmm.blocks;

import com.hoopawolf.dmm.blocks.tileentity.SwordStoneTileEntity;
import com.hoopawolf.dmm.util.TileEntityRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SwordStoneBlock extends Block
{

    public SwordStoneBlock(Block.Properties properties)
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
        return TileEntityRegistryHandler.SWORD_STONE_TILE_ENTITY.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        SwordStoneTileEntity swordStone = (SwordStoneTileEntity) worldIn.getTileEntity(pos);

        if (!swordStone.isActivated())
        {
            swordStone.setActivated(true);
            swordStone.setUUID(player.getUniqueID());
            swordStone.markDirty();
        } else if (swordStone.isDone())
        {
            dropItem(state, pos, worldIn, player);
        }

        return ActionResultType.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasTileEntity(state))
        {
            SwordStoneTileEntity swordStone = (SwordStoneTileEntity) worldIn.getTileEntity(pos);

            player.dropItem(new ItemStack(swordStone.getActivationItem()), true);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}
