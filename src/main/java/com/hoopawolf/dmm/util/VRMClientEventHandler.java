package com.hoopawolf.dmm.util;

import com.hoopawolf.dmm.helper.EntityHelper;
import com.hoopawolf.dmm.helper.VRMEatItemDataHandler;
import com.hoopawolf.dmm.items.armors.SinsArmorItem;
import com.hoopawolf.dmm.network.VRMPacketHandler;
import com.hoopawolf.dmm.network.packets.server.SinMaskActivateMessage;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class VRMClientEventHandler
{
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
    public static void onRightClickWithItem(PlayerInteractEvent.RightClickItem event)
    {
        if (event.getEntityLiving().world.isRemote)
        {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            if (player.isCrouching() && player.inventory.armorInventory.get(3).getItem().equals(ArmorRegistryHandler.GLUTTONY_MASK_ARMOR.get()) && SinsArmorItem.isActivated(player.inventory.armorInventory.get(3)))
            {
                if (!player.getHeldItemMainhand().isEmpty() && !player.getHeldItemMainhand().isFood())
                {
                    if (consumeItem(player, event.getItemStack()))
                    {
                        event.setCanceled(true);
                    } else
                    {
                        EntityHelper.sendMessage(player, "cannoteat", TextFormatting.GRAY);
                    }
                }
            }
        }
    }

    private static boolean consumeItem(PlayerEntity playerIn, ItemStack itemStackIn)
    {
        if (VRMEatItemDataHandler.INSTANCE.data.get(itemStackIn.getTranslationKey()) != null)
        {
            playerIn.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
            playerIn.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, 1.0F);

            return true;
        }

        return false;
    }
}
