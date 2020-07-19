package com.hoopawolf.vrm.network;

import com.hoopawolf.vrm.items.armors.SinsArmorItem;
import com.hoopawolf.vrm.network.packets.server.*;
import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageHandlerOnServer
{
    static final Effect[] types =
            {
                    Effects.POISON, //0
                    Effects.SLOWNESS, //1
                    Effects.WEAKNESS, //2
                    Effects.NAUSEA, //3
            };

    public static void onMessageReceived(final MessageToServer message, Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived != LogicalSide.SERVER)
        {
            Reference.LOGGER.warn("MessageToServer received on wrong side:" + ctx.getDirection().getReceptionSide());
            return;
        }
        if (!message.isMessageValid())
        {
            Reference.LOGGER.warn("MessageToServer was invalid" + message.toString());
            return;
        }

        final ServerPlayerEntity sendingPlayer = ctx.getSender();
        if (sendingPlayer == null)
        {
            Reference.LOGGER.warn("EntityPlayerMP was null when MessageToServer was received");
        }

        ctx.enqueueWork(() -> processMessage(message, sendingPlayer));
    }


    static void processMessage(MessageToServer message, ServerPlayerEntity sendingPlayer)
    {
        switch (message.getMessageType())
        {
            case 0:
            {
                SetPotionEffectMessage _message = (SetPotionEffectMessage) message;
                Entity _entity = sendingPlayer.world.getEntityByID(_message.getEntityID());

                if (_entity != null && _entity instanceof LivingEntity)
                {
                    if (_entity.isAlive())
                    {
                        ((LivingEntity) _entity).addPotionEffect(new EffectInstance(types[_message.getPotionType()], _message.getDuration(), _message.getAmplifier()));
                    }
                }
            }
            break;
            case 1:
            {
                SetPotionEffectMultipleMessage _message = (SetPotionEffectMultipleMessage) message;
                Entity _entity = sendingPlayer.world.getEntityByID(_message.getEntityID());

                if (_entity != null && _entity instanceof LivingEntity)
                {
                    if (_entity.isAlive())
                    {
                        int max = MathHelper.clamp(_message.getEnding(), 0, types.length - 1);

                        for (int i = _message.getStarting(); i <= max; ++i)
                        {
                            ((LivingEntity) _entity).addPotionEffect(new EffectInstance(types[i], _message.getDuration(), _message.getAmplifier()));
                        }
                    }
                }
            }
            break;
            case 2:
            {
                SinMaskActivateMessage _message = (SinMaskActivateMessage) message;
                PlayerEntity _player = sendingPlayer.world.getPlayerByUuid(_message.getPlayerID());

                if (_player != null)
                {
                    if (_player.isAlive())
                    {
                        SinsArmorItem.setActivated(_player.inventory.armorInventory.get(3), _message.isActivated());
                    }
                }
            }
            break;
            case 3:
            {
                SleepMessage _message = (SleepMessage) message;
                PlayerEntity _player = sendingPlayer.world.getPlayerByUuid(_message.getPlayerID());

                if (_player != null)
                {
                    if (_player.isAlive())
                    {
                        _player.startSleeping(new BlockPos(_player.getPositionVec()));
                    }
                }
            }
            break;
            default:
                break;
        }
    }

    public static boolean isThisProtocolAcceptedByServer(String protocolVersion)
    {
        return Reference.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }
}
