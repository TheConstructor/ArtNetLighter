package tc.vom.artNetLighter.infrastructure.packets;

/**
 * Represents an Art-Net packet that could be send or received.
 */
public abstract class ArtNetPacket implements ArtNetOpCodes {

    public static final int PROTOCOL_VERSION = 14;
    public static final int HEADER_LENGTH = 10;

    /**
     * 8 Byte identification
     */
    public static final byte[] ART_NET_ID = {'A', 'r', 't', '-', 'N', 'e', 't', 0};
    /**
     * 2 Byte OpCode
     */
    private int opCode;

    public ArtNetPacket(final int opCode) {
        this.opCode = opCode;
    }

    /**
     * 2 Byte OpCode
     */
    public int getOpCode() {
        return opCode;
    }

    protected void setOpCode(final int opCode) {
        this.opCode = opCode;
    }

    public abstract byte[] constructPacket();

    protected byte[] constructPacket(final int bufferLength) {
        return ArtNetPacket.constructPacket(bufferLength, opCode);
    }

    public static byte[] constructPacket(final int bufferLength, final int opCode) {
        if (bufferLength < ArtNetPacket.HEADER_LENGTH)
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        byte[] result = new byte[bufferLength];
        System.arraycopy(ART_NET_ID, 0, result, 0, ART_NET_ID.length);
        result[8] = (byte) (opCode & 0x00ff);
        result[9] = (byte) ((opCode & 0xff00) >> 8);
        return result;
    }
}
