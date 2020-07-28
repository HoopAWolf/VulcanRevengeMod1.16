package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketBuffer;

import java.util.UUID;

public class SleepMessage extends MessageToServer
{
    private UUID player_ID;
    private boolean affectedByNight;

    public SleepMessage(UUID playerIDIn, boolean affectedByNightIn)
    {
        messageIsValid = true;
        messageType = 3;

        player_ID = playerIDIn;
        affectedByNight = affectedByNightIn;
    }

    // for use by the message handler only.
    public SleepMessage()
    {
        messageIsValid = false;
    }

    public static SleepMessage decode(PacketBuffer buf)
    {
        UUID _playerID;
        boolean _affectedByNight;

        try
        {
            _playerID = buf.readUniqueId();
            _affectedByNight = buf.readBoolean();

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

        return new SleepMessage(_playerID, _affectedByNight);
    }

    public UUID getPlayerID()
    {
        return player_ID;
    }

    public boolean isAffectedByNight()
    {
        return affectedByNight;
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        if (!messageIsValid) return;
        buf.writeUniqueId(player_ID);
        buf.writeBoolean(affectedByNight);
    }

    @Override
    public String toString()
    {
        return "SleepMessage[targetID=" + player_ID + "]";
    }
}

