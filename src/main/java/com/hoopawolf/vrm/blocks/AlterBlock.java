package com.hoopawolf.vrm.blocks;

import com.hoopawolf.vrm.blocks.tileentity.AlterTileEntity;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class AlterBlock extends Block
{
    public AlterBlock(Block.Properties properties)
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
        return TileEntityRegistryHandler.ALTER_TILE_ENTITY.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        AlterTileEntity alter = (AlterTileEntity) worldIn.getTileEntity(pos);

        if (!alter.isActivated() && PotionUtils.getPotionFromItem(player.getHeldItemMainhand()).equals(Potions.WATER))
        {
            alter.setActivated(true);
            if (!worldIn.isRemote)
            {
                if (!player.isCreative())
                {
                    player.getHeldItemMainhand().shrink(1);
                }
            }
        } else if (alter.isDone() || alter.isActivated())
        {
            dropItem(state, pos, worldIn, player);
        }
        alter.markDirty();

        return ActionResultType.SUCCESS;
    }

    public void dropItem(BlockState state, BlockPos pos, World worldIn, PlayerEntity player)
    {
        if (hasTileEntity(state))
        {
            AlterTileEntity alter = (AlterTileEntity) worldIn.getTileEntity(pos);

            if (!worldIn.isRemote)
            {
                ItemStack item = alter.getActivationItem().copy();
                player.dropItem(item, true);
            }

            alter.setActivated(false);
            alter.resetData();
            alter.markDirty();
        }
    }
}

