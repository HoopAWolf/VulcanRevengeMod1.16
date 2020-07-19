package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.data.EatItemData;
import com.hoopawolf.vrm.entities.ai.AnimalAttackGoal;
import com.hoopawolf.vrm.entities.ai.DazedGoal;
import com.hoopawolf.vrm.entities.projectiles.PesArrowEntity;
import com.hoopawolf.vrm.helper.VRMEatItemDataHandler;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.items.weapons.DeathSwordItem;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class VRMServerEventHandler
{
    @SubscribeEvent
    public static void DeathEvent(LivingDeathEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity && event.getSource().getTrueSource() instanceof LivingEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get()) &&
                        DeathSwordItem.getDeathCoolDown(player.getHeldItemMainhand()) <= 0)
                {
                    event.setCanceled(true);
                    attacker.attackEntityFrom(new DamageSource("death"), attacker.getMaxHealth() * 0.5F);
                    player.setHealth(player.getMaxHealth() * 0.5F);
                    player.getFoodStats().setFoodLevel(20);
                    DeathSwordItem.setDeathCoolDown(player.getHeldItemMainhand(), 600);
                    player.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 5.0F, 0.1F);

                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = i * 360 / 180;
                        double speed = 0.3;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(player.getPosX(), player.getPosY() + 0.5F, player.getPosZ()), new Vector3d(xSpeed, 0.0D, zSpeed), 3, 4, 0.0F);
                        VRMPacketHandler.packetHandler.sendToDimension(player.world.func_234923_W_(), spawnParticleMessage);
                    }
                }
            }

            if (event.getEntity() instanceof LivingEntity)
            {
                LivingEntity target = (LivingEntity) event.getEntity();

                if (target.isPotionActive(PotionRegistryHandler.PLAGUE_EFFECT.get()) || event.getSource().getImmediateSource() instanceof PesArrowEntity)
                {
                    target.playSound(SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, 0.5F, 0.1F);
                    for (int i = 1; i <= 180; ++i)
                    {
                        double yaw = i * 360 / 180;
                        double speed = 0.7;
                        double xSpeed = speed * Math.cos(Math.toRadians(yaw));
                        double zSpeed = speed * Math.sin(Math.toRadians(yaw));

                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(target.getPosX(), target.getPosY() + 0.5F, target.getPosZ()), new Vector3d(xSpeed, 0.9D, zSpeed), 3, 5, 0.0F);
                        VRMPacketHandler.packetHandler.sendToDimension(target.world.func_234923_W_(), spawnParticleMessage);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof CreatureEntity)
            {
                if (((CreatureEntity) event.getEntity()).isPotionActive(PotionRegistryHandler.DAZED_EFFECT.get()))
                {
                    ((CreatureEntity) event.getEntity()).removePotionEffect(PotionRegistryHandler.DAZED_EFFECT.get());
                }
            }

            if (event.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.DEATH_SWORD.get()) &&
                        DeathSwordItem.getVoodooID(player.getHeldItemMainhand()) != 0 &&
                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())) != null &&
                        player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).isAlive())
                {
                    event.setCanceled(true);
                    player.world.getEntityByID(DeathSwordItem.getVoodooID(player.getHeldItemMainhand())).attackEntityFrom(new DamageSource("reaper"), event.getAmount());
                    player.playSound(SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }

                if (player.world.rand.nextInt(100) < 40 && (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.FAM_SCALE.get()) || player.getHeldItemOffhand().getItem().equals(ItemBlockRegistryHandler.FAM_SCALE.get())))
                {
                    if (event.getSource().getTrueSource() instanceof CreatureEntity)
                    {
                        ((CreatureEntity) event.getSource().getTrueSource()).addPotionEffect(new EffectInstance(new EffectInstance(PotionRegistryHandler.DAZED_EFFECT.get(), 100)));
                        SpawnParticleMessage spawnParticleMessage = new SpawnParticleMessage(new Vector3d(event.getSource().getTrueSource().getPosX(), event.getSource().getTrueSource().getPosY() + 0.5F, event.getSource().getTrueSource().getPosZ()), new Vector3d(0.0F, 0.0D, 0.0F), 3, 9, event.getSource().getTrueSource().getWidth());
                        VRMPacketHandler.packetHandler.sendToDimension(event.getSource().getTrueSource().world.func_234923_W_(), spawnParticleMessage);
                    }

                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BANJO, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }

                if (player.getHealth() < player.getMaxHealth() * 0.3F && player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.WAR_SWORD.get()) &&
                        event.getSource().getTrueSource() instanceof LivingEntity)
                {
                    event.getSource().getTrueSource().setFire(10);
                    player.playSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 5.0F, 0.1F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void ApplyPotionEvent(PotionEvent.PotionApplicableEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (player.getHeldItemMainhand().getItem().equals(ItemBlockRegistryHandler.PES_BOW.get()))
                {
                    if (!event.getPotionEffect().getPotion().isBeneficial())
                    {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void CheckSleeping(SleepingTimeCheckEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
                {
                    event.setResult(Event.Result.ALLOW);
                }

                if (player.getSleepTimer() >= 100)
                {
                    if (event.getEntity().world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
                    {
                        long i = event.getEntity().world.getDayTime() + 24000L;
                        if (event.getEntity().world.isDaytime())
                        {
                            ((ServerWorld) event.getEntity().world).func_241114_a_((i - i % 24000L) - 11000L);
                        } else
                        {
                            ((ServerWorld) event.getEntity().world).func_241114_a_((i - i % 24000L));
                        }
                    }

                    player.wakeUp();
                }
            }
        }
    }

    @SubscribeEvent
    public static void CheckSleepingLocation(SleepingLocationCheckEvent event)
    {
        if (!event.getEntity().world.isRemote)
        {
            if (event.getEntity() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
                {
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();

        World world = entity.world;

        if (!world.isRemote)
        {
            if (entity instanceof CreatureEntity)
            {
                ((CreatureEntity) entity).goalSelector.addGoal(0, new DazedGoal(((CreatureEntity) entity)));
            }

            if (entity instanceof CowEntity || entity instanceof RabbitEntity || entity instanceof SheepEntity || entity instanceof AbstractHorseEntity || entity instanceof PigEntity || entity instanceof ChickenEntity)
            {
                ((AnimalEntity) entity).goalSelector.addGoal(1, new AnimalAttackGoal(((AnimalEntity) entity), 1.0D, true, 2, 1));
            }
        }
    }

    @SubscribeEvent
    public static void onEatFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (!event.getEntityLiving().world.isRemote)
        {
            if (event.getItem().isFood() && event.getEntityLiving() instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) event.getEntityLiving();

                if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
                {
                    SinsArmorItem.increaseFulfilment(player.inventory.armorInventory.get(3), event.getItem().getItem().getFood().getHealing(), SinsArmorItem.getSin(player.inventory.armorInventory.get(3)).getMaxUse());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickWithItem(PlayerInteractEvent.RightClickItem event)
    {
        if (!event.getEntityLiving().world.isRemote)
        {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            if (player.isCrouching() && player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
            {
                if (!player.getHeldItemMainhand().isEmpty() && !player.getHeldItemMainhand().isFood())
                {
                    if (consumeItem(player, event.getItemStack()))
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private static boolean consumeItem(PlayerEntity playerIn, ItemStack itemStackIn)
    {
        if (VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey()) != null)
        {
            EatItemData data = VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey());

            if (itemStackIn.isDamageable())
            {
                itemStackIn.damageItem((int) (itemStackIn.getMaxDamage() * 0.3F), playerIn, (p_220287_1_) ->
                {
                    p_220287_1_.sendBreakAnimation(Hand.MAIN_HAND);
                });
            } else
            {
                itemStackIn.shrink(1);
                playerIn.sendBreakAnimation(Hand.MAIN_HAND);
            }

            playerIn.getFoodStats().setFoodLevel(MathHelper.clamp(playerIn.getFoodStats().getFoodLevel() + data.getFoodAmount(), 0, 20));
            SinsArmorItem.increaseFulfilment(playerIn.inventory.armorInventory.get(3), data.getFoodAmount(), SinsArmorItem.getSin(playerIn.inventory.armorInventory.get(3)).getMaxUse());

            for (String effects : data.getListOfEffects())
            {
                for (Map.Entry<ResourceLocation, Effect> list : ForgeRegistries.POTIONS.getEntries())
                {
                    if (list.getKey().toString().equals(effects))
                    {
                        playerIn.addPotionEffect(new EffectInstance(list.getValue(), (list.getValue().equals(Effects.INSTANT_DAMAGE) || list.getValue().equals(Effects.INSTANT_HEALTH)) ? 1 : data.getDuration(), data.getAmplifier()));
                        break;
                    }
                }
            }

            return true;
        }

        return false;
    }
}