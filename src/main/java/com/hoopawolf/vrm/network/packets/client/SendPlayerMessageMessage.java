package com.hoopawolf.vrm.network.packets.client;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketBuffer;

import java.util.UUID;

public class SendPlayerMessageMessage extends MessageToClient
{
    private int color;
    private String message;
    private UUID playerUUID;

    public SendPlayerMessageMessage(UUID playerUUIDIn, String messageIn, int colorIn)
    {
        messageIsValid = true;
        messageType = 3;
        message = messageIn;
        color = colorIn;
        playerUUID = playerUUIDIn;
    }

    // for use by the message handler only.
    public SendPlayerMessageMessage()
    {
        messageIsValid = false;
    }

    public static SendPlayerMessageMessage decode(PacketBuffer buf)
    {
        String messageIn;
        int colorIn;
        UUID playerUUIDIn;

        try
        {
            messageIn = buf.readString();
            colorIn = buf.readInt();
            playerUUIDIn = buf.readUniqueId();

            // these methods may also be of use for your code:
            // for Itemstacks - ByteBufUtils.readItemStack()
            // for NBT tags ByteBufUtils.readTag();
            // for Strings: ByteBufUtils.readUTF8String();
            // NB that PacketBuffer is a derived class of ByteBuf

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SendPlayerMessageMessageToClient: " + e);
            return new SendPlayerMessageMessage();
        }

        return new SendPlayerMessageMessage(playerUUIDIn, messageIn, colorIn);
    }

    public String getMessageID()
    {
        return message;
    }

    public UUID getPlayerUUID()
    {
        return playerUUID;
    }

    public int getColor()
    {
        return color;
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        if (!messageIsValid) return;
        buf.writeString(message);
        buf.writeInt(color);
        buf.writeUniqueId(playerUUID);
    }

    @Override
    public String toString()
    {
        return "SendPlayerMessageMessageToClient[MessageID=" + message + "]";
    }
}
