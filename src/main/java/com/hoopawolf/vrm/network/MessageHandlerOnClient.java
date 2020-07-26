package com.hoopawolf.vrm.network;

import com.hoopawolf.vrm.helper.EntityHelper;
import com.hoopawolf.vrm.network.packets.client.MessageToClient;
import com.hoopawolf.vrm.network.packets.client.PlaySoundEffectMessage;
import com.hoopawolf.vrm.network.packets.client.SendPlayerMessageMessage;
import com.hoopawolf.vrm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.vrm.ref.Reference;
import com.hoopawolf.vrm.util.ParticleRegistryHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;


public class MessageHandlerOnClient
{
    private static final BasicParticleType[] types =
            {
                    ParticleTypes.FLAME, //0
                    ParticleTypes.FIREWORK, //1
                    ParticleTypes.END_ROD, //2
                    ParticleRegistryHandler.DEATH_MARK_PARTICLE.get(), //3
                    ParticleTypes.SMOKE, //4
                    ParticleRegistryHandler.PLAGUE_PARTICLE.get(), //5
                    ParticleTypes.ITEM_SLIME, //6
                    ParticleTypes.HEART, //7
                    ParticleTypes.ANGRY_VILLAGER, //8
                    ParticleTypes.WITCH, //9
            };

    private static final SoundEvent[] sound_type =
            {
                    SoundEvents.ENTITY_GENERIC_EAT, //0
                    SoundEvents.ENTITY_PLAYER_BURP, //1
                    SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, //2
                    SoundEvents.BLOCK_NOTE_BLOCK_BANJO, //3
                    SoundEvents.ENTITY_VEX_CHARGE, //4
                    SoundEvents.BLOCK_END_PORTAL_SPAWN, //5
                    SoundEvents.ENTITY_PUFFER_FISH_BLOW_OUT, //6
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, //7
            };

    public static void onMessageReceived(final MessageToClient message, Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived != LogicalSide.CLIENT)
        {
            Reference.LOGGER.warn("MessageToClient received on wrong side:" + ctx.getDirection().getReceptionSide());
            return;
        }
        if (!message.isMessageValid())
        {
            Reference.LOGGER.warn("MessageToClient was invalid" + message.toString());
            return;
        }

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
        if (!clientWorld.isPresent())
        {
            Reference.LOGGER.warn("MessageToClient context could not provide a ClientWorld.");
            return;
        }

        ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
    }

    private static void processMessage(ClientWorld worldClient, MessageToClient message)
    {
        switch (message.getMessageType())
        {
            case 1:
            {
                SpawnParticleMessage _message = (((SpawnParticleMessage) message));
                for (int i = 0; i < _message.getIteration(); ++i)
                {
                    Vector3d targetCoordinates = _message.getTargetCoordinates();
                    Vector3d targetSpeed = _message.getTargetSpeed();
                    double spread = _message.getParticleSpread();
                    double spawnXpos = targetCoordinates.x;
                    double spawnYpos = targetCoordinates.y;
                    double spawnZpos = targetCoordinates.z;
                    double speedX = targetSpeed.x;
                    double speedY = targetSpeed.y;
                    double speedZ = targetSpeed.z;

                    worldClient.addParticle(/*_message.getPartcleType() == 2 ? new BlockParticleData(ParticleTypes.BLOCK, worldClient.getBlockState(new BlockPos(spawnXpos, spawnYpos - 1.0F, spawnZpos))) : */types[_message.getPartcleType()],
                            true,
                            MathHelper.lerp(worldClient.rand.nextDouble(), spawnXpos + spread, spawnXpos - spread),
                            spawnYpos,
                            MathHelper.lerp(worldClient.rand.nextDouble(), spawnZpos + spread, spawnZpos - spread),
                            speedX, speedY, speedZ);
                }
            }
            break;
            case 2:
            {
                PlaySoundEffectMessage _message = (((PlaySoundEffectMessage) message));

                Entity entity = worldClient.getEntityByID(_message.getEntityID());
                float pitch = _message.getPitch();
                float volume = _message.getVolume();

                entity.playSound(sound_type[_message.getSoundType()], volume, pitch);
            }
            break;
            case 3:
            {
                SendPlayerMessageMessage _message = (((SendPlayerMessageMessage) message));

                PlayerEntity entity = worldClient.getPlayerByUuid(_message.getPlayerUUID());
                String messageID = _message.getMessageID();
                int color = _message.getColor();

                EntityHelper.sendMessage(entity, messageID, TextFormatting.fromColorIndex(color));
            }
            break;
            default:
                break;
        }

    }

    public static boolean isThisProtocolAcceptedByClient(String protocolVersion)
    {
        return Reference.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }
}
