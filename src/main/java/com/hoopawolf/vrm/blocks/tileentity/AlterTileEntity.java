package com.hoopawolf.vrm.blocks.tileentity;

import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import com.hoopawolf.vrm.util.TileEntityRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Objects;

class ItemStore
{
    private final Item[] storage = new Item[3];

    public ItemStore(Item first, Item second, Item third)
    {
        storage[0] = first;
        storage[1] = second;
        storage[2] = third;
    }

    public Item[] getStorage()
    {
        return storage;
    }
}

public class AlterTileEntity extends TileEntity implements ITickableTileEntity
{
    private final BlockPos[] runePos =
            {
                    new BlockPos(-2, 0, 2),
                    new BlockPos(2, 0, 2),
                    new BlockPos(0, 1, -2)
            };

    private final ItemStore[] sinItems =
            {
                    new ItemStore(Items.COOKED_BEEF, Items.COOKED_COD, Items.COOKED_RABBIT),
                    new ItemStore(Items.SLIME_BALL, Items.MAGMA_CREAM, Items.SLIME_BLOCK),
                    new ItemStore(Items.WHEAT, Items.WHEAT_SEEDS, Items.CARROT),
                    new ItemStore(Items.DIAMOND, Items.GOLD_INGOT, Items.EMERALD),
                    new ItemStore(Items.WHITE_BED, Items.BROWN_BED, Items.LIME_BED),
                    new ItemStore(Items.DIAMOND_SWORD, Items.SHIELD, Items.DIAMOND_CHESTPLATE),
                    new ItemStore(Items.BOW, Items.GOLDEN_APPLE, Items.ENDER_EYE)
            };

    private final ItemStack[] sinPotion =
            {
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.GLUTTONY_TRIAL_POTION.get()),
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.ENVY_TRIAL_POTION.get()),
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.LUST_TRIAL_POTION.get()),
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.GREED_TRIAL_POTION.get()),
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.SLOTH_TRIAL_POTION.get()),
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.WRATH_TRIAL_POTION.get()),
                    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionRegistryHandler.PRIDE_TRIAL_POTION.get())
            };

    private float timer, degree;
    private int sinType;
    private boolean activated, activatedDone;

    public AlterTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        degree = 0.0F;
        timer = 0.0F;
        sinType = 0;
    }

    public AlterTileEntity()
    {
        this(TileEntityRegistryHandler.ALTER_TILE_ENTITY.get());
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean flag)
    {
        activated = flag;
    }

    public int getSinType()
    {
        return sinType;
    }

    public void setSinType(int typeIn)
    {
        sinType = typeIn;
    }

    public boolean isDone()
    {
        return activatedDone;
    }

    public void setDone(boolean flag)
    {
        activatedDone = flag;
    }

    public float getDegree()
    {
        return degree;
    }

    public float getTimer()
    {
        return timer;
    }

    @Override
    public void tick()
    {
        if (!isDone())
        {
            ArrayList<Item> tempItems = new ArrayList<>();
            for (BlockPos pos : runePos)
            {
                if (world.getBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())).hasTileEntity() &&
                        world.getTileEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())) instanceof PedestalTileEntity)
                {
                    if (((PedestalTileEntity) Objects.requireNonNull(world.getTileEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())))).getStoredItem().getItem().equals(Items.AIR))
                    {
                        resetData();
                        break;
                    } else
                    {
                        tempItems.add(((PedestalTileEntity) Objects.requireNonNull(world.getTileEntity(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ())))).getStoredItem().getItem());
                    }
                } else
                {
                    resetData();
                    break;
                }
            }

            if (tempItems.size() == 3)
            {
                boolean flag = false;
                int type = 0;
                for (ItemStore store : sinItems)
                {
                    for (Item item : store.getStorage())
                    {
                        tempItems.remove(item);
                    }

                    if (tempItems.size() == 0)
                    {
                        flag = true;
                        setSinType(type);
                        break;
                    } else if (tempItems.size() != 3)
                    {
                        break;
                    }

                    ++type;
                }

                if (!flag)
                {
                    resetData();
                }
            }

            if (isActivated())
            {
                if (timer <= 0)
                {
                    if (!world.isRemote)
                    {
                        world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 2.0F, 0.1F);
                        ((ServerWorld) world.getWorld()).func_241113_a_(0, 6000, true, true);
                    }
                }

                timer += 0.5F;

                if (!world.isRemote)
                {
                    for (BlockPos pos : runePos)
                    {
                        Vector3d block = new Vector3d(getPos().getX() + pos.getX() + 0.5D, getPos().getY() + pos.getY() + 1.5D, getPos().getZ() + pos.getZ() + 0.5D);
                        Vector3d dir = new Vector3d(getPos().getX() + 0.5D, getPos().getY(), getPos().getZ() + 0.5).subtract(new Vector3d(block.getX(), block.getY(), block.getZ())).normalize();

                        SpawnParticleMessage particleFireworks = new SpawnParticleMessage(new Vector3d(block.getX(), block.getY(), block.getZ()), new Vector3d(dir.getX(), dir.getY(), dir.getZ()), 3, 12, 0.0F);
                        VRMPacketHandler.packetHandler.sendToDimension(world.func_234923_W_(), particleFireworks);

                        SpawnParticleMessage particleEndrod = new SpawnParticleMessage(new Vector3d(block.getX(), block.getY(), block.getZ()), new Vector3d(dir.getX(), dir.getY(), dir.getZ()), 3, 0, 0.0F);
                        VRMPacketHandler.packetHandler.sendToDimension(world.func_234923_W_(), particleEndrod);
                    }
                }

                if (getTimer() > 100)
                {
                    setDone(true);

                    if (!world.isRemote)
                    {
                        world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 2.0F, 0.1F);
                    }

                    for (BlockPos pos : runePos)
                    {
                        world.setBlockState(new BlockPos(getPos().getX() + pos.getX(), getPos().getY() + pos.getY(), getPos().getZ() + pos.getZ()), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        degree += 0.5F;
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound)
    {
        super.read(blockState, compound);
        activated = compound.getBoolean("activated");
        activatedDone = compound.getBoolean("activatedDone");
        timer = compound.getFloat("timer");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putBoolean("activated", activated);
        compound.putBoolean("activatedDone", activatedDone);
        compound.putFloat("timer", timer);
        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTag = new CompoundNBT();
        write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 3, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        read(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    public void resetData()
    {
        timer = 0;
        degree = 0.0F;
        setDone(false);
        setActivated(false);
    }

    public ItemStack getActivationItem()
    {
        return isDone() ? sinPotion[getSinType()] : PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);
    }
}

