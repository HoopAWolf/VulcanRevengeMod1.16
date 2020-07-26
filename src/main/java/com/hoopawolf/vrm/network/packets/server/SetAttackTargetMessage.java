package com.hoopawolf.vrm.network.packets.server;

import com.hoopawolf.vrm.ref.Reference;
import net.minecraft.network.PacketBuffer;

public class SetAttackTargetMessage extends MessageToServer
{
    private int attackerID, targetID;

    public SetAttackTargetMessage(int attackerIDIn, int targetIDIn)
    {
        messageIsValid = true;
        messageType = 4;

        attackerID = attackerIDIn;
        targetID = targetIDIn;
    }

    // for use by the message handler only.
    public SetAttackTargetMessage()
    {
        messageIsValid = false;
    }

    public static SetAttackTargetMessage decode(PacketBuffer buf)
    {
        int _attackerID, _targetID;

        try
        {
            _attackerID = buf.readInt();
            _targetID = buf.readInt();

            // these methods may also be of use for your code:
            // for Itemstacks - ByteBufUtils.readItemStack()
            // for NBT tags ByteBufUtils.readTag();
            // for Strings: ByteBufUtils.readUTF8String();
            // NB that PacketBuffer is a derived class of ByteBuf

        } catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Reference.LOGGER.warn("Exception while reading SetAttackTargetMessageToServer: " + e);
            return new SetAttackTargetMessage();
        }

        return new SetAttackTargetMessage(_attackerID, _targetID);
    }

    public int getAttackerID()
    {
        return attackerID;
    }

    public int getTargetID()
    {
        return targetID;
    }

    @Override
    public void encode(PacketBuffer buf)
    {
        if (!messageIsValid) return;
        buf.writeInt(attackerID);
        buf.writeInt(targetID);
    }

    @Override
    public String toString()
    {
        return "SetAttackTargetMessage[targetID=" + attackerID + "]";
    }
}

