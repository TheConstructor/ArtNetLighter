package tc.vom.artNetLighter.infrastructure.packets;

import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

import java.nio.charset.Charset;
import java.util.Arrays;

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
    public static final Charset STRING_CHARSET = Charset.forName("ISO-8859-15");


    /**
     * 2 Byte OpCode
     */
    private int opCode;

    public ArtNetPacket(final int opCode) {
        this.opCode = opCode;
    }

    public ArtNetPacket(byte[] data) {
        if (data.length < ArtNetPacket.HEADER_LENGTH) {
            throw new IllegalArgumentException("Minimum size for Packet Header is " + ArtNetPacket.HEADER_LENGTH);
        }
        final byte[] header = new byte[ArtNetPacket.ART_NET_ID.length];
        System.arraycopy(data, 0, header, 0, header.length);
        if (!Arrays.equals(ArtNetPacket.ART_NET_ID, header))
            throw new IllegalArgumentException("Packet data must start with ArtNetPacket.ART_NET_ID");
        this.opCode = (data[9] << 8) | data[8];
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

    public static byte[] constructPacket(final int bufferLength, final int opCode) {
        if (bufferLength < ArtNetPacket.HEADER_LENGTH)
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        byte[] result = new byte[bufferLength];
        System.arraycopy(ART_NET_ID, 0, result, 0, ART_NET_ID.length);
        result[8] = (byte) (opCode & 0x00ff);
        result[9] = (byte) ((opCode & 0xff00) >> 8);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtNetPacket)) return false;

        ArtNetPacket that = (ArtNetPacket) o;

        if (this.opCode != that.opCode) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return opCode;
    }
}
