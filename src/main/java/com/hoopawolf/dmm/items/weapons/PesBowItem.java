package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PesBowItem extends BowItem
{
    public PesBowItem(Properties builder)
    {
        super(builder.group(VRMItemGroup.instance).rarity(Rarity.UNCOMMON));
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate()
    {
        return null;
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count)
    {
        if (livingEntityIn.isCrouching() && livingEntityIn.ticksExisted % 50 == 0)
        {
            livingEntityIn.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 0.5F, 0.1F);
            if (!worldIn.isRemote)
            {
                for (int i = 1; i <= 180; ++i)
                {
                    double yaw = i * 360 / 180;
                    double speed = 0.3;
                    double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                    double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                    SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(livingEntityIn.getPosX(), livingEntityIn.getPosY() + 0.5F, livingEntityIn.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 1, 5, 0.0F);
                    VRMPacketHandler.packetHandler.sendToDimension(livingEntityIn.world.func_234923_W_(), spawnParticleMessage);
                }
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, true);
            if (i < 0) return;

            float f = getArrowVelocity(i);
            if (!((double) f < 0.1D))
            {
                if (!worldIn.isRemote)
                {
                    int _iteration = 1;
                    float _spread = 0.0F;

                    if (playerentity.isCrouching())
                    {
                        _iteration = 3;
                        _spread = -5.0F;
                    }

                    for (int iteration = 0; iteration < _iteration; ++iteration)
                    {
                        AbstractArrowEntity abstractarrowentity = new PesArrowEntity(worldIn, playerentity);
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.func_234612_a_(playerentity, playerentity.rotationPitch, playerentity.rotationYaw + _spread, 0.0F, f * 3.0F, (_iteration == 3) ? 3.0F : 1.0F);
                        if (f == 1.0F)
                        {
                            abstractarrowentity.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0)
                        {
                            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0)
                        {
                            abstractarrowentity.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                        {
                            abstractarrowentity.setFire(100);
                        }

                        stack.damageItem(1, playerentity, (p_220009_1_) ->
                        {
                            p_220009_1_.sendBreakAnimation(playerentity.getActiveHand());
                        });

                        worldIn.addEntity(abstractarrowentity);
                        _spread += 5.0F;
                    }

                    worldIn.playSound(null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    playerentity.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn instanceof LivingEntity && isSelected)
            {
                if (!((LivingEntity) entityIn).getActivePotionEffects().isEmpty())
                {
                    ArrayList<Effect> array = new ArrayList<>();
                    for (EffectInstance eff : ((LivingEntity) entityIn).getActivePotionEffects())
                    {
                        if (!eff.getPotion().isBeneficial())
                        {
                            array.add(eff.getPotion());
                        }
                    }

                    if (array.size() > 0)
                    {
                        for (Effect eff : array)
                        {
                            ((LivingEntity) entityIn).removePotionEffect(eff);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) return ret;

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public AbstractArrowEntity customArrow(AbstractArrowEntity arrow)
    {
        return arrow;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes1")).func_240703_c_(Style.EMPTY.setFormatting(TextFormatting.LIGHT_PURPLE)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes2")).func_240703_c_(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes3")).func_240703_c_(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:pes4")).func_240703_c_(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.GRAY)));
    }
}
