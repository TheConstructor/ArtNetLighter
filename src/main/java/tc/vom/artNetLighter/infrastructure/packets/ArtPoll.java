/*
 * Dieses Werk ist unter einer Creative Commons Lizenz vom Typ Namensnennung - Weitergabe unter gleichen Bedingungen 3.0 Deutschland zugänglich. Um eine Kopie dieser Lizenz einzusehen, konsultieren Sie http://creativecommons.org/licenses/by-sa/3.0/de/ oder wenden Sie sich brieflich an Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 * Autor des "ArtNetLighter" ist Matthias Vill http://vom.tc/
 *
 * --
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Germany License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/de/ or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 *
 * Author of "ArtNetLighter" is Matthias Vill http://vom.tc/
 *
 * --
 *
 * Art-Net™ Designed by and Copyright Artistic Licence Holdings Ltd
 */

package tc.vom.artNetLighter.infrastructure.packets;

import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

/**
 * Used to hold the data of an ArtPoll-Packet.
 */
public class ArtPoll extends _ArtNetPacket {

    public static final int TALK_TO_ME_UPDATE_ON_ANY_CHANGE = 0x1;
    public static final int TALK_TO_ME_SEND_DIAGNOSTICS_MESSAGES = 0x2;
    public static final int TALK_TO_ME_USE_UNICAST = 0x4;

    public static final int PACKET_LENGTH = _ArtNetPacket.FULL_HEADER_LENGTH + 2;
    /**
     * 1 Byte TalkToMe
     */
    private final byte talkToMe;

    /**
     * 1 Byte Priority
     */
    private final byte priority;

    public ArtPoll(final byte talkToMe, final byte priority) {
        super(ArtNetOpCodes.OP_CODE_POLL);
        this.talkToMe = talkToMe;
        this.priority = priority;
    }

    public byte getTalkToMe() {
        return this.talkToMe;
    }

    public byte getPriority() {
        return this.priority;
    }

    @Override
    public byte[] constructPacket() {
        return ArtPoll.constructPacket(this.talkToMe, this.priority);
    }

    public static byte[] constructPacket(final byte talkToMe, final byte priority) {
        final byte[] result = _ArtNetPacket.constructPacket(ArtPoll.PACKET_LENGTH, ArtNetOpCodes.OP_CODE_POLL);
        result[_ArtNetPacket.FULL_HEADER_LENGTH] = talkToMe;
        result[_ArtNetPacket.FULL_HEADER_LENGTH + 1] = priority;
        return result;
    }

    public ArtPoll(final byte[] data) {
        super(data);
        if (data.length < ArtPoll.PACKET_LENGTH) {
            throw new IllegalArgumentException("Minimum size for ArtPoll is " + ArtPoll.PACKET_LENGTH);
        }
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_POLL) {
            throw new IllegalArgumentException("Provided data specifies a wrong OpCode");
        }
        this.talkToMe = data[_ArtNetPacket.FULL_HEADER_LENGTH];
        this.priority = data[_ArtNetPacket.FULL_HEADER_LENGTH + 1];
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtPoll)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtPoll artPoll = (ArtPoll) o;

        if (this.priority != artPoll.priority) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (this.talkToMe != artPoll.talkToMe) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.talkToMe;
        result = (31 * result) + this.priority;
        return result;
    }
}
