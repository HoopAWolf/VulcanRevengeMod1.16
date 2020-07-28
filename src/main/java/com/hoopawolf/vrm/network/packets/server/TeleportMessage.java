package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class TeleportMessage extends MessageToServer
{
    private BlockPos pos;
    private int hostID;

    public TeleportMessage(BlockPos posIn, int hostIDIn)
    {
        messageIsValid = true;
        messageType = 5;

        pos = posIn;
        hostID = hostIDIn;
    }

    // for use by the message handler only.
    public TeleportMessage()
    {
        messageIsValid = false;
    }

    public static TeleportMessage decode(PacketBuffer buf)
    {
        BlockPos posIn;
        int hostIDIn;

        try
        {
            posIn = buf.readBlockPos();
            hostIDIn = buf.readInt();

            // these methods may also be of use for your code:
            // for Itemstacks - ByteBufUtils.readItemStack()
            // for NBT tags ByteBufUtils.readTag();
            // for Strings: ByteBufUtils.readUTF8String();
            // NB that PacketBuffer is a derived class of ByteBuf

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading TeleportMessageToServer: " + e);
            return new TeleportMessage();
        }

        return new TeleportMessage(posIn, hostIDIn);
    }

    public BlockPos getTeleportPos()
    {
        return pos;
    }

    public int getHostID()
    {
        return hostID;
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        if (!messageIsValid) return;
        buf.writeBlockPos(pos);
        buf.writeInt(hostID);
    }

    @Override
    public String toString()
    {
        return "TeleportMessage[targetID=" + hostID + "]";
    }
}
