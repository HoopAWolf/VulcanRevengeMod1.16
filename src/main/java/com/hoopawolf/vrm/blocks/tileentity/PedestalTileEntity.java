package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class PedestalTileEntity extends TileEntity
{
    private float degree;
    private ItemStack storedItem;

    public PedestalTileEntity()
    {
        this(TileEntityRegistryHandler.PEDESTAL_TILE_ENTITY.get());
    }

    public PedestalTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;

        if (storedItem == null)
        {
            storedItem = new ItemStack(Items.AIR);
        }
    }

    public float getDegree()
    {
        return degree += 0.5F;
    }

    public ItemStack getStoredItem()
    {
        return storedItem;
    }

    public void setStoredItem(ItemStack stack)
    {
        storedItem = stack;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound)
    {
        super.read(blockState, compound);

        CompoundNBT compoundnbt = compound.getCompound("Item");
        if (!compoundnbt.isEmpty())
        {
            ItemStack itemstack = ItemStack.read(compoundnbt);
            if (itemstack.isEmpty())
            {
                storedItem = new ItemStack(Items.AIR);
            } else
            {
                storedItem = itemstack;
            }
        } else
        {
            storedItem = new ItemStack(Items.AIR);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);

        if (storedItem != null)
        {
            compound.put("Item", storedItem.write(new CompoundNBT()));
        }

        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTag = new CompoundNBT();
        write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        CompoundNBT tag = pkt.getNbtCompound();
        read(getBlockState(), tag);
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }
}