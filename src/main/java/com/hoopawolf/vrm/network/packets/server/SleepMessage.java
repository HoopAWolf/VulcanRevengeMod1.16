package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketBuffer;

import java.util.UUID;

public class SleepMessage extends MessageToServer
{
    private UUID player_ID;

    public SleepMessage(UUID playerIDIn)
    {
        messageIsValid = true;
        messageType = 3;

        player_ID = playerIDIn;
    }

    // for use by the message handler only.
    public SleepMessage()
    {
        messageIsValid = false;
    }

    public static SleepMessage decode(PacketBuffer buf)
    {
        UUID _playerID;

        try
        {
            _playerID = buf.readUniqueId();

            // these methods may also be of use for your code:
            // for Itemstacks - ByteBufUtils.readItemStack()
            // for NBT tags ByteBufUtils.readTag();
            // for Strings: ByteBufUtils.readUTF8String();
            // NB that PacketBuffer is a derived class of ByteBuf

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SleepMessageToServer: " + e);
            return new SleepMessage();
        }

        return new SleepMessage(_playerID);
    }

    public UUID getPlayerID()
    {
        return player_ID;
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        if (!messageIsValid) return;
        buf.writeUniqueId(player_ID);
    }

    @Override
    public String toString()
    {
        return "SleepMessage[targetID=" + player_ID + "]";
    }
}

