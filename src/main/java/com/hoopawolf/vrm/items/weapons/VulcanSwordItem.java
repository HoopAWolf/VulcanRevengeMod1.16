package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.tab.VRMItemGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VulcanSwordItem extends SwordItem
{

    public VulcanSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.group(VRMItemGroup.instance));
    }

    public static int getType(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("type", 0);

        return stack.getTag().getInt("type");
    }

    private static void setType(ItemStack stack, int type)
    {
        stack.getOrCreateTag().putInt("type", type);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            ItemStack itemStack = playerIn.getHeldItem(handIn);

            setType(itemStack, getType(itemStack) + 1);

            if (getType(itemStack) > 3)
                setType(itemStack, 0);

            return ActionResult.resultPass(playerIn.getHeldItem(handIn));
        }

        return ActionResult.resultFail(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.hitEntity(stack, target, attacker);

        switch (getType(stack))
        {
            case 0:
                target.setFire(10);
                break;
            case 1:
                target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 140, 10));
                stack.damageItem(2, target, (p_220009_1_) -> p_220009_1_.sendBreakAnimation(target.getActiveHand()));
                break;

            case 2:
                stack.damageItem(5, target, (p_220009_1_) -> p_220009_1_.sendBreakAnimation(target.getActiveHand()));
                break;

            case 3:
                target.addPotionEffect(new EffectInstance(Effects.WITHER, 140, 0));
                stack.damageItem(2, target, (p_220009_1_) -> p_220009_1_.sendBreakAnimation(target.getActiveHand()));
                break;
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (isSelected && getType(stack) == 2)
            {
                if (entityIn instanceof LivingEntity)
                {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 1, 5));
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.STRENGTH, 1, 10));
                }
            }
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        return new TranslationTextComponent(this.getTranslationKey(stack) + getType(stack));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:vulcansrevenge") + getType(stack)).func_240703_c_(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:vulcansrevengeinfo")).func_240703_c_(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
    }
}
