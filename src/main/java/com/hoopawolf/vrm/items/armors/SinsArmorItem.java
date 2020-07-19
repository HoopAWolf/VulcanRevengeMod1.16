package com.hoopawolf.vrm.items.armors;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class SinsArmorItem extends ArmorItem
{
    private final SINS sinHolder;
    private final int maxUse;

    public SinsArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties p_i48534_3_, SINS sinIn)
    {
        super(materialIn, slot, p_i48534_3_);
        sinHolder = sinIn;
        maxUse = sinHolder.getMaxUse();
    }

    public static SINS getSin(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("sin", 0);

        return SINS.values()[stack.getTag().getInt("sin")];
    }

    public static int getFulfilment(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("fulfil", 0);

        return stack.getTag().getInt("fulfil");
    }

    public static BlockPos getLastPos(ItemStack stack)
    {
        if (!stack.hasTag())
        {
            stack.getOrCreateTag().putInt("lastPosX", 0);
            stack.getOrCreateTag().putInt("lastPosY", 0);
            stack.getOrCreateTag().putInt("lastPosZ", 0);
        }

        return new BlockPos(stack.getTag().getInt("lastPosX"), stack.getTag().getInt("lastPosY"), stack.getTag().getInt("lastPosZ"));
    }

    public static void setFulfilment(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("fulfil", amount);
    }

    public static boolean isActivated(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putBoolean("activate", true);

        return stack.getTag().getBoolean("activate");
    }

    public static void setActivated(ItemStack stack, boolean isActiveIn)
    {
        stack.getOrCreateTag().putBoolean("activate", isActiveIn);
    }

    public static void setLastPos(ItemStack stack, BlockPos lastPos)
    {
        stack.getOrCreateTag().putInt("lastPosX", lastPos.getX());
        stack.getOrCreateTag().putInt("lastPosY", lastPos.getY());
        stack.getOrCreateTag().putInt("lastPosZ", lastPos.getZ());
    }

    public static void increaseFulfilment(ItemStack stack, int amount, int maxAmount)
    {
        setFulfilment(stack, MathHelper.clamp(getFulfilment(stack) - amount, 0, maxAmount));
    }

    public static void setSin(ItemStack stack, SINS sinIn)
    {
        stack.getOrCreateTag().putInt("sin", sinIn.getValue());
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (getSin(stack) != sinHolder)
        {
            setSin(stack, sinHolder);
        }
    }

    @Override
    public void onArmorTick(ItemStack stack, World worldIn, PlayerEntity entityIn)
    {
        if (isActivated(stack))
        {
            if (!entityIn.isCreative())
            {
                switch (getSin(stack))
                {
                    case ENVY:
                        break;
                    case LUST:
                        break;
                    case GREED:
                        break;
                    case PRIDE:
                        break;
                    case SLOTH:
                    {
                        if (!worldIn.isRemote)
                        {
                            if (entityIn.ticksExisted % 12 == 0)
                            {
                                setLastPos(stack, new BlockPos((int) entityIn.getPosX(), (int) entityIn.getPosY(), (int) entityIn.getPosZ()));
                            }

                            if (getLastPos(stack).getX() == (int) entityIn.getPosX() && getLastPos(stack).getY() == (int) entityIn.getPosY() &&
                                    getLastPos(stack).getZ() == (int) entityIn.getPosZ())
                            {
                                if (entityIn.ticksExisted % 10 == 0)
                                {
                                    if (getFulfilment(stack) > 0)
                                    {
                                        setFulfilment(stack, getFulfilment(stack) - 1);
                                    }
                                }
                            } else
                            {
                                if (entityIn.ticksExisted % 10 == 0)
                                {
                                    if (getFulfilment(stack) < maxUse)
                                    {
                                        setFulfilment(stack, getFulfilment(stack) + 1);
                                    }
                                }
                            }
                        }
                    }
                    break;
                    case WRATH:
                        break;
                    case GLUTTONY:
                    {
                        if (!worldIn.isRemote)
                        {
                            if (entityIn.ticksExisted % 10 == 0)
                            {
                                if (getFulfilment(stack) < maxUse)
                                {
                                    setFulfilment(stack, getFulfilment(stack) + 1);
                                }
                            }
                        }

                        if (!entityIn.getFoodStats().needFood())
                        {
                            entityIn.getFoodStats().setFoodLevel(19);
                        }

                        if (getDurabilityForDisplay(stack) > 0.5F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.HUNGER, 1, 3));
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        return Reference.MOD_ID + ":textures/models/armor/" + getRegistryName().getPath() + "_layer_1.png";
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (double) getFulfilment(stack) / (double) maxUse;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:sinmask") + getSin(stack).getValue()).func_240703_c_(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.DARK_GRAY)));
    }

    public enum SINS
    {
        TEMP(0, 0),
        GLUTTONY(1, 40),
        ENVY(2, 40),
        LUST(3, 40),
        GREED(4, 40),
        SLOTH(5, 40),
        WRATH(6, 40),
        PRIDE(7, 40);

        private final int value, maxUse;

        SINS(int valueIn, int maxUseIn)
        {
            this.value = valueIn;
            this.maxUse = maxUseIn;
        }

        public int getValue()
        {
            return value;
        }

        public int getMaxUse()
        {
            return maxUse;
        }
    }
}
