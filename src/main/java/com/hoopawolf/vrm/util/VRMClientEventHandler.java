package com.hoopawolf.vrm.util;

import com.hoopawolf.vrm.client.gui.SinArmorFulfilmentGui;
import com.hoopawolf.vrm.config.ConfigHandler;
import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.helper.RayTracingHelper;
import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.VRMPacketHandler;
import com.hoopawolf.vrm.network.packets.server.SetAttackTargetMessage;
import com.hoopawolf.vrm.network.packets.server.SinMaskActivateMessage;
import com.hoopawolf.vrm.network.packets.server.SleepMessage;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class VRMClientEventHandler
{
    private static final SinArmorFulfilmentGui armorGui = new SinArmorFulfilmentGui();

    @SubscribeEvent
    public static void LeftClickAirWithEmpty(PlayerInteractEvent.LeftClickEmpty event)
    {
        if (event.getEntityLiving().world.isRemote)
        {
            if (event.getItemStack().isEmpty())
            {
                if (event.getEntityLiving() instanceof PlayerEntity)
                {
                    PlayerEntity player = (PlayerEntity) event.getEntityLiving();

                    if (player.isCrouching() && player.inventory.armorInventory.get(3).getItem() instanceof SinsArmorItem)
                    {
                        SinMaskActivateMessage _message = new SinMaskActivateMessage(player.getUniqueID(), !SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)));
                        VRMPacketHandler.channel.sendToServer(_message);

                        EntityHelper.sendMessage(player, (!SinsArmorItem.isActivated(player.inventory.armorInventory.get(3))) ? "sinactivate" : "sindeactivate", TextFormatting.RED);
                        player.playSound((!SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)) ? SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON : SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK), 1.0F, 0.1F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void RightClickAirWithEmpty(PlayerInteractEvent.RightClickEmpty event)
    {
        if (event.getEntityLiving().world.isRemote)
        {
            if (event.getItemStack().isEmpty())
            {
                if (event.getEntityLiving() instanceof PlayerEntity)
                {
                    PlayerEntity player = (PlayerEntity) event.getEntityLiving();

                    if (player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.SLOTH_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
                    {
                        if (player.isCrouching())
                        {
                            SleepMessage _message = new SleepMessage(player.getUniqueID());
                            VRMPacketHandler.channel.sendToServer(_message);
                        } else
                        {
                            if (SinsArmorItem.getSlothPetID(player.inventory.armorInventory.get(3)) != 0)
                            {
                                RayTracingHelper.INSTANCE.fire();
                                Entity target = RayTracingHelper.INSTANCE.getTarget();

                                if (target instanceof CreatureEntity)
                                {
                                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F);
                                    SetAttackTargetMessage _message = new SetAttackTargetMessage(SinsArmorItem.getSlothPetID(player.inventory.armorInventory.get(3)), target.getEntityId());
                                    VRMPacketHandler.channel.sendToServer(_message);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE && ConfigHandler.CLIENT.sinMaskWarning.get())
        {
            if (mc.player.inventory.armorInventory.get(3).getItem() instanceof SinsArmorItem && SinsArmorItem.isActivated(mc.player.inventory.armorInventory.get(3)))
            {
                float percentage = (float) SinsArmorItem.getFulfilment(mc.player.inventory.armorInventory.get(3)) / (float) SinsArmorItem.getSin(mc.player.inventory.armorInventory.get(3)).getMaxUse();
                if (percentage >= 0.2F)
                {
                    String fulfilment = mc.player.inventory.armorInventory.get(3).getDisplayName().getString() + I18n.format("gui.text.urge") + ((percentage >= 0.8F) ?
                            I18n.format("gui.text.urgehigh") : ((percentage >= 0.5F) ?
                            I18n.format("gui.text.urgeaverage") : I18n.format("gui.text.urgelow")));

                    armorGui.draw(mc, fulfilment, event.getMatrixStack(), ((percentage >= 0.8F) ? "FF2222" : ((percentage >= 0.5F) ? "FF7909" : "55D100")));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (event.getAction() == GLFW.GLFW_REPEAT || event.getAction() == GLFW.GLFW_PRESS)
        {
            if (mc.player != null)
            {
                switch (event.getKey())
                {
                    case GLFW.GLFW_KEY_COMMA:
                        if (ConfigHandler.CLIENT.sinMaskWarning.get() && mc.player.isCrouching() && mc.player.inventory.armorInventory.get(3).getItem() instanceof SinsArmorItem && SinsArmorItem.isActivated(mc.player.inventory.armorInventory.get(3)))
                        {
                            ConfigHandler.CLIENT.sinMaskWarningHeightOffset.set(ConfigHandler.CLIENT.sinMaskWarningHeightOffset.get() - 5);
                        }
                        break;
                    case GLFW.GLFW_KEY_PERIOD:
                        if (ConfigHandler.CLIENT.sinMaskWarning.get() && mc.player.isCrouching() && mc.player.inventory.armorInventory.get(3).getItem() instanceof SinsArmorItem && SinsArmorItem.isActivated(mc.player.inventory.armorInventory.get(3)))
                        {
                            ConfigHandler.CLIENT.sinMaskWarningHeightOffset.set(ConfigHandler.CLIENT.sinMaskWarningHeightOffset.get() + 5);
                        }
                        break;

                    case GLFW.GLFW_KEY_SEMICOLON:
                        if (ConfigHandler.CLIENT.sinMaskWarning.get() && mc.player.isCrouching() && mc.player.inventory.armorInventory.get(3).getItem() instanceof SinsArmorItem && SinsArmorItem.isActivated(mc.player.inventory.armorInventory.get(3)))
                        {
                            ConfigHandler.CLIENT.sinMaskWarningWidthOffset.set(ConfigHandler.CLIENT.sinMaskWarningWidthOffset.get() - 5);
                        }
                        break;
                    case GLFW.GLFW_KEY_APOSTROPHE:
                        if (ConfigHandler.CLIENT.sinMaskWarning.get() && mc.player.isCrouching() && mc.player.inventory.armorInventory.get(3).getItem() instanceof SinsArmorItem && SinsArmorItem.isActivated(mc.player.inventory.armorInventory.get(3)))
                        {
                            ConfigHandler.CLIENT.sinMaskWarningWidthOffset.set(ConfigHandler.CLIENT.sinMaskWarningWidthOffset.get() + 5);
                        }
                        break;
                }
            }
        }
    }
}
