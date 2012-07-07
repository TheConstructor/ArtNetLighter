package tc.vom.artNetLighter.infrastructure.packets;

import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

/**
 * Used to hold the data of an ArtPoll-Packet.
 */
public class ArtPoll extends ArtNetPacket {

    public static final int TALK_TO_ME_UPDATE_ON_ANY_CHANGE = 0x1;
    public static final int TALK_TO_ME_SEND_DIAGNOSTICS_MESSAGES = 0x2;
    public static final int TALK_TO_ME_USE_UNICAST = 0x4;

    /**
     * 2 Byte Protocol Version
     */
    private int protocolVersion;
    /**
     * 1 Byte TalkToMe
     */
    private int talkToMe;

    /**
     * 1 Byte Priority
     */
    private int priority;

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
        return talkToMe;
    }

    public void setTalkToMe(int talkToMe) {
        this.talkToMe = talkToMe;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public byte[] constructPacket() {
        return ArtPoll.constructPacket(protocolVersion, talkToMe, priority);
    }

    public static byte[] constructPacket(final int talkToMe, final int priority) {
        return ArtPoll.constructPacket(talkToMe, priority, ArtNetPacket.PROTOCOL_VERSION);
    }

    public static byte[] constructPacket(final int protocolVersion, final int talkToMe, final int priority) {
        byte[] result = ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH + 4, ArtNetOpCodes.OP_CODE_POLL);
        result[ArtNetPacket.HEADER_LENGTH] = (byte) ((protocolVersion & 0xff00) >> 8);
        result[ArtNetPacket.HEADER_LENGTH + 1] = (byte) (protocolVersion & 0x00ff);
        result[ArtNetPacket.HEADER_LENGTH + 2] = (byte) talkToMe;
        result[ArtNetPacket.HEADER_LENGTH + 3] = (byte) priority;
        return result;
    }
}
