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

    public static final int PACKET_LENGTH = ArtNetPacket.HEADER_LENGTH + 4;

    /**
     * 2 Byte Protocol Version
     */
    private final int protocolVersion;
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
        this.protocolVersion = ArtNetPacket.PROTOCOL_VERSION;
        this.talkToMe = talkToMe;
        this.priority = priority;
    }

    /**
     * 2 Byte Protocol Version
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public int getTalkToMe() {
        return this.talkToMe;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public byte[] constructPacket() {
        return ArtPoll.constructPacket(this.protocolVersion, this.talkToMe, this.priority);
    }

    public static byte[] constructPacket(final int talkToMe, final int priority) {
        return ArtPoll.constructPacket(ArtNetPacket.PROTOCOL_VERSION, talkToMe, priority);
    }

    @SuppressWarnings("WeakerAccess")
    public static byte[] constructPacket(final int protocolVersion, final int talkToMe, final int priority) {
        final byte[] result = ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH + 4, ArtNetOpCodes.OP_CODE_POLL);
        result[ArtNetPacket.HEADER_LENGTH] = (byte) ((protocolVersion & 0xff00) >> 8);
        result[ArtNetPacket.HEADER_LENGTH + 1] = (byte) (protocolVersion & 0x00ff);
        result[ArtNetPacket.HEADER_LENGTH + 2] = (byte) talkToMe;
        result[ArtNetPacket.HEADER_LENGTH + 3] = (byte) priority;
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
        this.protocolVersion = (data[ArtNetPacket.HEADER_LENGTH] << 8) | data[ArtNetPacket.HEADER_LENGTH + 1];
        this.talkToMe = data[ArtNetPacket.HEADER_LENGTH + 2];
        this.priority = data[ArtNetPacket.HEADER_LENGTH + 3];
    }
}
