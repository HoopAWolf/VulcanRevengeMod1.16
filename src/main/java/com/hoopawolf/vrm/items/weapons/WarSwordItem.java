package com.hoopawolf.vrm.items.weapons;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.tab.VRMItemGroup;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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

public class WarSwordItem extends SwordItem
{
    public WarSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, builder.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }

    public static int getWarCryCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("warcry", 0);

        return stack.getTag().getInt("warcry");
    }

    public static int getRageCoolDown(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("rage", 0);

        return stack.getTag().getInt("rage");
    }

    public static void setWarCryCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("warcry", amount);
    }

    public static void setRageCoolDown(ItemStack stack, int amount)
    {
        stack.getOrCreateTag().putInt("rage", amount);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if (!worldIn.isRemote)
        {
            if (!playerIn.isCrouching() && handIn.equals(Hand.MAIN_HAND))
            {
                if (getWarCryCoolDown(playerIn.getHeldItem(handIn)) <= 0)
                {
                    setWarCryCoolDown(playerIn.getHeldItem(handIn), 200);
                    playerIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 150, 3));
                    playerIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 150, 3));
                    if (playerIn.getHealth() < playerIn.getMaxHealth() * 0.3F)
                    {
                        playerIn.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 150, 3));
                    }

                    playerIn.playSound(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.BLOCKS, 5.0F, 0.1F);
                } else
                {
                    playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    EntityHelper.sendCoolDownMessage(playerIn, getWarCryCoolDown(playerIn.getHeldItem(handIn)));
                }
            } else
            {
                if (handIn.equals(Hand.MAIN_HAND))
                {
                    if (getRageCoolDown(playerIn.getHeldItem(handIn)) <= 0)
                    {
                        setRageCoolDown(playerIn.getHeldItem(handIn), 200);
                        CreatureEntity temp = null;
                        for (LivingEntity entity : EntityHelper.getEntityLivingBaseNearby(playerIn, 10, 2, 10, 15))
                        {
                            if (entity instanceof CreatureEntity)
                            {
                                if (temp == null)
                                {
                                    temp = (CreatureEntity) entity;
                                } else
                                {
                                    ((CreatureEntity) entity).setAttackTarget(temp);
                                    temp.addPotionEffect(new EffectInstance(PotionRegistryHandler.RAGE_EFFECT.get(), 300, 0));
                                    entity.addPotionEffect(new EffectInstance(PotionRegistryHandler.RAGE_EFFECT.get(), 300, 0));
                                    temp.setAttackTarget(entity);
                                    SpawnParticleMessage mob1 = new SpawnParticleMessage(new Vector3d(entity.getPosX(), entity.getPosY() + 0.5F, entity.getPosZ()), new Vector3d(0.0D, 0.0D, 0.0D), 3, 8, temp.getWidth());
                                    VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), mob1);
                                    SpawnParticleMessage mob2 = new SpawnParticleMessage(new Vector3d(temp.getPosX(), temp.getPosY() + 0.5F, temp.getPosZ()), new Vector3d(0.0D, 0.0D, 0.0D), 3, 0, temp.getWidth());
                                    VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), mob2);
                                    temp = null;
                                }
                            }
                        }

                        for (int i = 1; i <= 180; ++i)
                        {
                            double yaw = i * 360 / 180;
                            double speed = 1.5;
                            double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                            double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                            SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(playerIn.getPosX(), playerIn.getPosY() + 0.5F, playerIn.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 0, 0.0F);
                            VRMPacketHandler.packetHandler.sendToDimension(playerIn.world.func_234923_W_(), spawnParticleMessage);
                        }

                        playerIn.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.BLOCKS, 5.0F, 0.1F);
                    } else
                    {
                        playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 5.0F, 0.1F);
                        EntityHelper.sendCoolDownMessage(playerIn, getRageCoolDown(playerIn.getHeldItem(handIn)));
                    }
                }
            }
        }

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        super.hitEntity(stack, target, attacker);

        if (attacker.world.rand.nextInt(100) < 30 || attacker.getHealth() < attacker.getMaxHealth() * 0.3F)
        {
            target.setFire(10);
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn.ticksExisted % 2 == 0)
            {
                if (getRageCoolDown(stack) > 0)
                {
                    setRageCoolDown(stack, getRageCoolDown(stack) - 1);
                }

                if (getWarCryCoolDown(stack) > 0)
                {
                    setWarCryCoolDown(stack, getWarCryCoolDown(stack) - 1);
                }
            }

            if (entityIn instanceof LivingEntity && isSelected)
            {
                if (((LivingEntity) entityIn).getHealth() < ((LivingEntity) entityIn).getMaxHealth() * 0.3F)
                {
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.STRENGTH, 1, 3));
                    ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1, 3));
                }

                if (entityIn.getFireTimer() > 0)
                {
                    entityIn.extinguish();
                }
            }
        }
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
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:war1")).mergeStyle(Style.EMPTY.applyFormatting(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:war2") + ((getWarCryCoolDown(stack) > 0) ? " [" + (getWarCryCoolDown(stack) / 20) + "s]" : "")).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(((getWarCryCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:war3") + ((getRageCoolDown(stack) > 0) ? " [" + (getRageCoolDown(stack) / 20) + "s]" : "")).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(((getRageCoolDown(stack) > 0) ? TextFormatting.DARK_GRAY : TextFormatting.GRAY))));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:war4")).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
    }
}
