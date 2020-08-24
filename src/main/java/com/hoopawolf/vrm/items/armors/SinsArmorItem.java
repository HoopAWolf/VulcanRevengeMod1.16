package com.hoopawolf.vrm.items.armors;

import com.hoopawolf.vrm.entities.SlothPetEntity;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.EntityRegistryHandler;
import com.hoopawolf.vrm.util.PotionRegistryHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
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

    public static int getSlothPetID(ItemStack stack)
    {
        if (!stack.hasTag())
            stack.getOrCreateTag().putInt("slothpet", 0);

        return stack.getTag().getInt("slothpet");
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

    public static void setSlothPetID(ItemStack stack, int id)
    {
        stack.getOrCreateTag().putInt("slothpet", id);
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

    public static void decreaseFulfilment(ItemStack stack, int amount, int maxAmount)
    {
        setFulfilment(stack, MathHelper.clamp(getFulfilment(stack) + amount, 0, maxAmount));
    }

    public static void setSin(ItemStack stack, SINS sinIn)
    {
        stack.getOrCreateTag().putInt("sin", sinIn.getValue());
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (getSin(stack) != sinHolder)
            {
                setSin(stack, sinHolder);

                if (sinHolder.equals(SINS.ENVY))
                {
                    setFulfilment(stack, maxUse);
                }
            }
        }
    }

    @Override
    public void onArmorTick(ItemStack stack, World worldIn, PlayerEntity entityIn)
    {
        if (isActivated(stack))
        {
            switch (getSin(stack))
            {
                case ENVY:
                    if (!worldIn.isRemote)
                    {
                        if (entityIn.ticksExisted % 10 == 0)
                        {
                            if (getFulfilment(stack) < maxUse)
                            {
                                decreaseFulfilment(stack, 1, maxUse);
                            }
                        }

                        if (getDurabilityForDisplay(stack) > 0.99F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.POISON, 50, 0));
                        }
                    }
                    break;
                case LUST:
                    if (!worldIn.isRemote)
                    {
                        if (entityIn.ticksExisted % 10 == 0)
                        {
                            if (getFulfilment(stack) < maxUse)
                            {
                                decreaseFulfilment(stack, 1, maxUse);
                            }
                        }

                        if (getDurabilityForDisplay(stack) > 0.8F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 50, 0));
                        }
                    }
                    break;
                case GREED:
                {
                    if (!worldIn.isRemote)
                    {
                        if (entityIn.ticksExisted % 10 == 0)
                        {
                            int totalItems = 0;
                            for (ItemStack itemstack : entityIn.inventory.mainInventory)
                            {
                                totalItems += itemstack.getCount();
                            }

                            setFulfilment(stack, (int) (100.0F - (((float) totalItems / (float) (entityIn.inventory.mainInventory.size() * 64)) * 100.0F)));
                        }

                        if (getDurabilityForDisplay(stack) > 0.9F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, 2));
                            entityIn.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 10, 2));
                        } else if (getDurabilityForDisplay(stack) > 0.7F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, 1));
                            entityIn.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 10, 1));
                        } else if (getDurabilityForDisplay(stack) > 0.5F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, 0));
                            entityIn.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 10, 0));
                        } else if (getDurabilityForDisplay(stack) < 0.1F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.SPEED, 10, 2));
                            entityIn.addPotionEffect(new EffectInstance(Effects.HASTE, 10, 2));
                        } else if (getDurabilityForDisplay(stack) < 0.3F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.SPEED, 10, 1));
                            entityIn.addPotionEffect(new EffectInstance(Effects.HASTE, 10, 1));
                        } else if (getDurabilityForDisplay(stack) < 0.5F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.SPEED, 10, 0));
                            entityIn.addPotionEffect(new EffectInstance(Effects.HASTE, 10, 0));
                        }
                    }
                }
                break;
                case PRIDE:
                    if (!worldIn.isRemote)
                    {
                        if (entityIn.ticksExisted % 10 == 0)
                        {
                            if (getFulfilment(stack) > 0)
                            {
                                increaseFulfilment(stack, 1, maxUse);
                            }
                        }

                        if (entityIn.ticksExisted % 5 == 0)
                        {
                            for (Entity entity : EntityHelper.getEntitiesNearby(entityIn, Entity.class, 5, 5, 5, 20))
                            {
                                if (entity instanceof CreatureEntity)
                                {
                                    if (!((CreatureEntity) entity).isPotionActive(Effects.WEAKNESS))
                                    {
                                        ((CreatureEntity) entity).addPotionEffect(new EffectInstance(Effects.WEAKNESS, 400, 0));

                                        if (entity.world.rand.nextInt(100) < 50)
                                        {
                                            ((CreatureEntity) entity).addPotionEffect(new EffectInstance(PotionRegistryHandler.FEAR_EFFECT.get(), 200, 0));
                                            PlaySoundEffectMessage playFearSoundMessage = new PlaySoundEffectMessage(entity.getEntityId(), 9, 1.0F, 1.0F);
                                            VRMPacketHandler.packetHandler.sendToDimension(entity.world.func_234923_W_(), playFearSoundMessage);
                                        }
                                    }
                                } else if (entity instanceof ProjectileEntity)
                                {
                                    if (!entity.velocityChanged)
                                    {
                                        if (entity.world.rand.nextInt(100) < 50)
                                        {
                                            if (((ProjectileEntity) entity).func_234616_v_() != null)
                                            {
                                                if (!(((ProjectileEntity) entity).func_234616_v_() instanceof PlayerEntity && ((ProjectileEntity) entity).func_234616_v_().getUniqueID().equals(entityIn.getUniqueID())))
                                                {
                                                    entity.setMotion(entity.getMotion().inverse().scale(1.2D));
                                                    ((ProjectileEntity) entity).setShooter(entityIn);
                                                    entity.velocityChanged = true;
                                                }
                                            } else
                                            {
                                                entity.setMotion(entity.getMotion().inverse().scale(1.2D));
                                                ((ProjectileEntity) entity).setShooter(entityIn);
                                                entity.velocityChanged = true;
                                            }
                                        } else
                                        {
                                            entity.velocityChanged = true;
                                        }
                                    }
                                }
                            }
                        }

                        if (getDurabilityForDisplay(stack) > 0.90F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(PotionRegistryHandler.FEAR_EFFECT.get(), 10, 0));
                        }
                    }
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
                                    increaseFulfilment(stack, 1, maxUse);
                                }
                            }

                            if (getSlothPetID(stack) == 0)
                            {
                                SlothPetEntity slothpetentity = EntityRegistryHandler.SLOTH_PET_ENTITY.get().create(entityIn.world);
                                slothpetentity.setLocationAndAngles(entityIn.getPosX(), entityIn.getPosY() + 2, entityIn.getPosZ(), entityIn.rotationYaw, entityIn.rotationPitch);
                                slothpetentity.setOwner(entityIn);
                                slothpetentity.setBoundOrigin(new BlockPos((int) entityIn.getPosX(), (int) entityIn.getPosY(), (int) entityIn.getPosZ()));
                                slothpetentity.setGlowing(true);
                                entityIn.world.addEntity(slothpetentity);

                                setSlothPetID(stack, slothpetentity.getEntityId());
                            } else
                            {
                                if (entityIn.world.getEntityByID(getSlothPetID(stack)) == null ||
                                        !(entityIn.world.getEntityByID(getSlothPetID(stack)) instanceof SlothPetEntity))
                                {
                                    setSlothPetID(stack, 0);
                                }
                            }

                        } else
                        {
                            if (entityIn.ticksExisted % 10 == 0)
                            {
                                if (getFulfilment(stack) < maxUse)
                                {
                                    decreaseFulfilment(stack, 2, maxUse);
                                }
                            }
                        }

                        if (getDurabilityForDisplay(stack) > 0.90F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(PotionRegistryHandler.DAZED_EFFECT.get(), 10, 0));
                            entityIn.startSleeping(new BlockPos(entityIn.getPositionVec()));
                        }
                    }
                }
                break;
                case WRATH:
                    if (!worldIn.isRemote)
                    {
                        if (entityIn.ticksExisted % 10 == 0)
                        {
                            if (getFulfilment(stack) < maxUse)
                            {
                                decreaseFulfilment(stack, 1, maxUse);
                            }
                        }

                        if (getDurabilityForDisplay(stack) < 0.2F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 4));
                            entityIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 10, 4));
                            entityIn.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 10, 4));
                        } else if (getDurabilityForDisplay(stack) < 0.3F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 3));
                            entityIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 10, 3));
                        } else if (getDurabilityForDisplay(stack) < 0.5F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 2));
                            entityIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 10, 2));

                        } else if (getDurabilityForDisplay(stack) < 0.7F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 1));
                            entityIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 10, 1));
                        } else if (getDurabilityForDisplay(stack) < 0.85F)
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 0));
                            entityIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 10, 0));
                        } else
                        {
                            entityIn.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 10, 0));
                        }
                    }
                    break;
                case GLUTTONY:
                {
                    if (!worldIn.isRemote)
                    {
                        if (entityIn.ticksExisted % 10 == 0)
                        {
                            if (getFulfilment(stack) < maxUse)
                            {
                                decreaseFulfilment(stack, 1, maxUse);
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

                        if (getDurabilityForDisplay(stack) > 0.90F)
                        {
                            if (entityIn.getFoodStats().getFoodLevel() > 1)
                            {
                                entityIn.getFoodStats().setFoodLevel(1);
                                entityIn.getFoodStats().addExhaustion(40);
                            }
                        }
                    }
                }
                break;
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
        tooltip.add(new TranslationTextComponent(I18n.format("tooltip.vrm:sinmask") + getSin(stack).getValue()).mergeStyle(Style.EMPTY.setItalic(true).setFormatting(TextFormatting.DARK_GRAY)));
    }

    public enum SINS
    {
        TEMP(0, 0),
        GLUTTONY(1, 40),
        ENVY(2, 40),
        LUST(3, 40),
        GREED(4, 100),
        SLOTH(5, 40),
        WRATH(6, 50),
        PRIDE(7, 50);

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
