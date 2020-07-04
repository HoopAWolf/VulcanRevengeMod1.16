package com.hoopawolf.dmm.network;

import com.hoopawolf.dmm.network.packets.client.MessageToClient;
import com.hoopawolf.dmm.network.packets.client.SpawnParticleMessage;
import com.hoopawolf.dmm.ref.Reference;
import com.hoopawolf.dmm.util.ParticleRegistryHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;


public class MessageHandlerOnClient
{
    static final BasicParticleType[] types =
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
                            true, MathHelper.lerp(worldClient.rand.nextDouble(), spawnXpos + spread,
                                    spawnXpos - spread), spawnYpos,
                            MathHelper.lerp(worldClient.rand.nextDouble(), spawnZpos + spread, spawnZpos - spread),
                            speedX, speedY, speedZ);
                }
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
