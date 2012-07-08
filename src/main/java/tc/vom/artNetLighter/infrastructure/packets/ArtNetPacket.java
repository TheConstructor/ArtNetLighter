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
    private static final byte[] ART_NET_ID = {'A', 'r', 't', '-', 'N', 'e', 't', 0};
    public static final Charset STRING_CHARSET = Charset.forName("ISO-8859-15");


    /**
     * 2 Byte OpCode
     */
    private final int opCode;

    public ArtNetPacket(final int opCode) {
        this.opCode = opCode;
    }

    @SuppressWarnings("WeakerAccess")
    public ArtNetPacket(final byte[] data) {
        if (data.length < ArtNetPacket.HEADER_LENGTH) {
            throw new IllegalArgumentException("Minimum size for Packet Header is " + ArtNetPacket.HEADER_LENGTH);
        }
        final byte[] header = new byte[ArtNetPacket.ART_NET_ID.length];
        System.arraycopy(data, 0, header, 0, header.length);
        if (!Arrays.equals(ArtNetPacket.ART_NET_ID, header)) {
            throw new IllegalArgumentException("Packet data must start with ArtNetPacket.ART_NET_ID");
        }
        this.opCode = (data[9] << 8) | data[8];
    }

    /**
     * 2 Byte OpCode
     */
    public int getOpCode() {
        return this.opCode;
    }

    public abstract byte[] constructPacket();

    public static byte[] constructPacket(final int bufferLength, final int opCode) {
        if (bufferLength < ArtNetPacket.HEADER_LENGTH) {
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        }
        final byte[] result = new byte[bufferLength];
        System.arraycopy(ArtNetPacket.ART_NET_ID, 0, result, 0, ArtNetPacket.ART_NET_ID.length);
        result[8] = (byte) (opCode & 0x00ff);
        result[9] = (byte) ((opCode & 0xff00) >> 8);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtNetPacket)) {
            return false;
        }

        final ArtNetPacket that = (ArtNetPacket) o;

        return this.opCode == that.opCode;

    }

    @Override
    public int hashCode() {
        return this.opCode;
    }
}
