package com.hoopawolf.vrm.network.packets.client;

import net.minecraft.network.PacketBuffer;

public abstract class MessageToClient
{
    protected boolean messageIsValid;
    protected int messageType;

    public boolean isMessageValid()
    {
        return messageIsValid;
    }

    public int getMessageType()
    {
        return messageType;
    }

    /**
     * Called by the network code.
     * Used to write the contents of your message member variables into the ByteBuf, ready for transmission over the network.
     *
     * @param buf
     */
    public abstract void encode(PacketBuffer buf);

    public abstract String toString();
}
