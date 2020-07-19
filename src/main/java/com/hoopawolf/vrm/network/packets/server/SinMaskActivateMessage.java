package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketBuffer;

import java.util.UUID;

public class SinMaskActivateMessage extends MessageToServer
{
    private UUID player_ID;
    private boolean isActivated;

    public SinMaskActivateMessage(UUID playerIDIn, boolean isActivatedIn)
    {
        messageIsValid = true;
        messageType = 2;

        player_ID = playerIDIn;
        isActivated = isActivatedIn;
    }

    // for use by the message handler only.
    public SinMaskActivateMessage()
    {
        messageIsValid = false;
    }

    public static SinMaskActivateMessage decode(PacketBuffer buf)
    {
        UUID _playerID;
        boolean _isActivated;

        try
        {
            _playerID = buf.readUniqueId();
            _isActivated = buf.readBoolean();

            // these methods may also be of use for your code:
            // for Itemstacks - ByteBufUtils.readItemStack()
            // for NBT tags ByteBufUtils.readTag();
            // for Strings: ByteBufUtils.readUTF8String();
            // NB that PacketBuffer is a derived class of ByteBuf

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SinMaskActivateMessageToServer: " + e);
            return new SinMaskActivateMessage();
        }

        return new SinMaskActivateMessage(_playerID, _isActivated);
    }

    public UUID getPlayerID()
    {
        return player_ID;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        if (!messageIsValid) return;
        buf.writeUniqueId(player_ID);
        buf.writeBoolean(isActivated);
    }

    @Override
    public String toString()
    {
        return "SinMaskActivateMessage[targetID=" + player_ID + "]";
    }
}
