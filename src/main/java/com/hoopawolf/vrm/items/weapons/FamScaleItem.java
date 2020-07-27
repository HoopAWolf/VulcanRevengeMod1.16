package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.tab.VRMItemGroup;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.block.BushBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class FamScaleItem extends Item
{
    private final int range = 10;

    public FamScaleItem(Properties properties)
    {
        super(properties.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }

    public static int getSacrificeCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("sacrifice", 0);

        return stack.getTag().getInt("sacrifice");
    }

    public static void setSacrificeCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("sacrifice", amount);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (handIn.equals(Hand.MAIN_HAND) && playerIn.isCrouching())
        {
            if (playerIn.getFoodStats().getFoodLevel() > playerIn.getHealth())
            {
                if (!worldIn.isRemote)
                {
                    setSacrificeCoolDown(playerIn.getHeldItem(handIn), 300);
                    playerIn.setHealth(playerIn.getFoodStats().getFoodLevel());
                    playerIn.getFoodStats().setFoodLevel(0);
                    playerIn.playSound(SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            } else
            {
                if (!worldIn.isRemote)
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    EntityHelper.sendCoolDownMessage(playerIn, getSacrificeCoolDown(playerIn.getHeldItem(handIn)));
                }
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn.ticksExisted % 2 == 0)
            {
                if (getSacrificeCoolDown(stack) > 0)
                {
                    setSacrificeCoolDown(stack, getSacrificeCoolDown(stack) - 1);
                }
            }

            if (entityIn.ticksExisted % 20 == 0)
            {
                if (entityIn instanceof PlayerEntity)
                {
                    PlayerEntity playerIn = (PlayerEntity) entityIn;

                    if ((isSelected || playerIn.getHeldItemOffhand().equals(stack)) && worldIn.rand.nextInt(100) < 50)
                    {
                        for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(playerIn, 5, 2, 5, 10))
                        {
                            if (entity instanceof AnimalEntity)
                            {
                                if (!entity.isChild() && !((AnimalEntity) entity).isInLove())
                                {
                                    ((AnimalEntity) entity).setInLove(600);
                                    ((AnimalEntity) entity).setGrowingAge(0);
                                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(entity.getPosX(), entity.getPosY() + 0.85F, entity.getPosZ()), new Vector3d(0.0F, 0.0D, 0.0F), 3, 7, entity.getWidth());
                                    VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), spawnParticleMessage);
                                }
                            }
                        }
                    }

                    if (playerIn.getFoodStats().needFood())
                    {
                        if (worldIn.rand.nextInt(100) < 50)
                        {
                            boolean _flag = false;
                            for (int x = -range; x < range; ++x)
                            {
                                for (int z = -range; z < range; ++z)
                                {
                                    BlockPos pos = new BlockPos(entityIn.getPosX() + x, entityIn.getPosY(), entityIn.getPosZ() + z);
                                    if (worldIn.getBlockState(pos).getBlock() instanceof BushBlock)
                                    {
                                        worldIn.destroyBlock(pos, false);
                                        increaseFood(playerIn);
                                        _flag = true;
                                        break;
                                    }
                                }

                                if (_flag)
                                {
                                    break;
                                }
                            }
                        } else
                        {
                            if ((isSelected || playerIn.getHeldItemOffhand().equals(stack)))
                            {
                                for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(entityIn, range, 1, range, 15))
                                {
                                    if (!entity.isPotionActive(PotionRegistryHandler.DAZED_EFFECT.get()))
                                    {
                                        entity.attackEntityFrom(DamageSource.STARVE, 2);
                                        increaseFood(playerIn);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void increaseFood(PlayerEntity entityIn)
    {
        entityIn.getFoodStats().setFoodLevel(entityIn.getFoodStats().getFoodLevel() + 1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:fam1")).mergeStyle(Style.EMPTY.setFormatting(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:fam2")).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:fam3")).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:fam4") + ((getSacrificeCoolDown(stack) > 0) ? " [" + (getSacrificeCoolDown(stack) / 20) + "s]" : "")).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(((getSacrificeCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
    }
}
