package com.hoopawolf.dmm.network;

import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.network.packets.server.SetPotionEffectMessage;
import com.hoopawolf.dmm.network.packets.server.SetPotionEffectMultipleMessage;
import com.hoopawolf.dmm.ref.Reference;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class VRMPacketHandler
{
    public static final VRMPacketHandler packetHandler = new VRMPacketHandler();
    public static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Reference.MOD_ID, "vrm_main_channel"))
            .clientAcceptedVersions(MessageHandlerOnClient::isThisProtocolAcceptedByClient)
            .serverAcceptedVersions(MessageHandlerOnServer::isThisProtocolAcceptedByServer)
            .networkProtocolVersion(() -> Reference.MESSAGE_PROTOCOL_VERSION)
            .simpleChannel();

    public static void init()
    {
        int id = 0;
        //SERVER
        channel.messageBuilder(SetPotionEffectMessage.class, id++).encoder(SetPotionEffectMessage::encode).decoder(SetPotionEffectMessage::decode).consumer(MessageHandlerOnServer::onMessageReceived).add();
        channel.messageBuilder(SetPotionEffectMultipleMessage.class, id++).encoder(SetPotionEffectMultipleMessage::encode).decoder(SetPotionEffectMultipleMessage::decode).consumer(MessageHandlerOnServer::onMessageReceived).add();

        //CLIENT
        channel.messageBuilder(SpawnParticleMessage.class, id++).encoder(SpawnParticleMessage::encode).decoder(SpawnParticleMessage::decode).consumer(MessageHandlerOnClient::onMessageReceived).add();
    }

    public void send(PacketTarget target, Object message)
    {
        channel.send(target, message);
    }

    public void sendToPlayer(ServerPlayerEntity player, Object message)
    {
        this.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public void sendToDimension(RegistryKey<World> dimension, Object message)
    {
        this.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
    }

    public void sendToNearbyPlayers(double x, double y, double z, double radius, RegistryKey<World> dimension, Object message)
    {
        this.sendToNearbyPlayers(new TargetPoint(x, y, z, radius, dimension), message);
    }

    public void sendToNearbyPlayers(TargetPoint point, Object message)
    {
        this.send(PacketDistributor.NEAR.with(() -> point), message);
    }

    public void sendToAllPlayers(Object message)
    {
        this.send(PacketDistributor.ALL.noArg(), message);
    }

    public void sendToChunk(Chunk chunk, Object message)
    {
        this.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
    }
}
