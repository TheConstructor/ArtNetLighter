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
public class ArtPoll extends ArtNetPacket {

    public static final int TALK_TO_ME_UPDATE_ON_ANY_CHANGE = 0x1;
    public static final int TALK_TO_ME_SEND_DIAGNOSTICS_MESSAGES = 0x2;
    public static final int TALK_TO_ME_USE_UNICAST = 0x4;

    public static final int PACKET_LENGTH = ArtNetPacket.FULL_HEADER_LENGTH + 2;
    /**
     * 1 Byte TalkToMe
     */
    private final int talkToMe;

    /**
     * 1 Byte Priority
     */
    private final int priority;

    /**
     * @param talkToMe
     */
    public ArtPoll(final int talkToMe, final int priority) {
        super(ArtNetOpCodes.OP_CODE_POLL);
        this.talkToMe = talkToMe;
        this.priority = priority;
    }

    public int getTalkToMe() {
        return this.talkToMe;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public byte[] constructPacket() {
        return ArtPoll.constructPacket(this.talkToMe, this.priority);
    }

    public static byte[] constructPacket(final int talkToMe, final int priority) {
        final byte[] result = ArtNetPacket.constructPacket(ArtPoll.PACKET_LENGTH, ArtNetOpCodes.OP_CODE_POLL);
        result[ArtNetPacket.FULL_HEADER_LENGTH] = (byte) talkToMe;
        result[ArtNetPacket.FULL_HEADER_LENGTH + 1] = (byte) priority;
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
        this.talkToMe = data[ArtNetPacket.FULL_HEADER_LENGTH];
        this.priority = data[ArtNetPacket.FULL_HEADER_LENGTH + 1];
    }
}
