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

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.*;

/**
 * Represents an Art-Net packet that could be send or received.
 */
public abstract class ArtNetPacket implements ArtNetOpCodes {

    public static final int PROTOCOL_VERSION = 14;
    public static final int SHORT_HEADER_LENGTH = 10;
    public static final int FULL_HEADER_LENGTH = 12;

    /**
     * 8 Byte identification
     */
    private static final byte[] ART_NET_ID = {'A', 'r', 't', '-', 'N', 'e', 't', 0};
    public static final Charset STRING_CHARSET = Charset.forName("ISO-8859-15");


    /**
     * 2 Byte OpCode
     */
    private final int opCode;

    /**
     * 2 Byte Protocol Version
     */
    private final int protocolVersion;

    public ArtNetPacket(final int opCode) {
        this(opCode, ArtNetPacket.PROTOCOL_VERSION);
    }

    public ArtNetPacket(final int opCode, final int protocolVersion) {
        this.opCode = opCode;
        this.protocolVersion = protocolVersion;
    }

    @SuppressWarnings("WeakerAccess")
    public ArtNetPacket(final byte[] data) {
        if (data.length < ArtNetPacket.SHORT_HEADER_LENGTH) {
            throw new IllegalArgumentException("Minimum size for Packet Header is " + ArtNetPacket.SHORT_HEADER_LENGTH);
        }
        final byte[] header = new byte[ArtNetPacket.ART_NET_ID.length];
        System.arraycopy(data, 0, header, 0, header.length);
        if (!Arrays.equals(ArtNetPacket.ART_NET_ID, header)) {
            throw new IllegalArgumentException("Packet data must start with ArtNetPacket.ART_NET_ID");
        }
        this.opCode = get2BytesLowToHigh(data, 8);
        if (this.opCode != ArtNetOpCodes.OP_CODE_POLL_REPLY) {
            if (data.length < ArtNetPacket.FULL_HEADER_LENGTH) {
                throw new IllegalArgumentException("Minimum size for Non-ArtPollReply-Packet Header is " + ArtNetPacket.FULL_HEADER_LENGTH);
            }
            this.protocolVersion = get2BytesHighToLow(data, 10);
        } else {
            this.protocolVersion = ArtNetPacket.PROTOCOL_VERSION;
        }
    }

    /**
     * 2 Byte OpCode
     */
    public int getOpCode() {
        return this.opCode;
    }

    /**
     * 2 Byte Protocol Version
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public abstract byte[] constructPacket();

    public static byte[] constructUnversionedPacket(final int bufferLength, final int opCode) {
        if (bufferLength < ArtNetPacket.SHORT_HEADER_LENGTH) {
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        }
        final byte[] result = new byte[bufferLength];
        System.arraycopy(ArtNetPacket.ART_NET_ID, 0, result, 0, ArtNetPacket.ART_NET_ID.length);
        set2BytesLowToHigh(opCode, result, 8);
        return result;
    }

    public static byte[] constructPacket(final int bufferLength, final int opCode) {
        return ArtNetPacket.constructPacket(bufferLength, opCode, ArtNetPacket.PROTOCOL_VERSION);
    }

    public static byte[] constructPacket(final int bufferLength, final int opCode, final int protocolVersion) {
        if (bufferLength < ArtNetPacket.FULL_HEADER_LENGTH) {
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        }
        final byte[] result = ArtNetPacket.constructUnversionedPacket(bufferLength, opCode);
        set2BytesHighToLow(protocolVersion, result, 10);
        return result;
    }

    public static ArtNetPacket parsePacket(final byte[] data) {
        final int opCode = get2BytesLowToHigh(data, 8);
        switch (opCode) {
            case ArtNetOpCodes.OP_CODE_POLL:
                return new ArtPoll(data);
            case ArtNetOpCodes.OP_CODE_POLL_REPLY:
                return new ArtPollReply(data);
            case ArtNetOpCodes.OP_CODE_DMX:
                return new ArtDmx(data);
            default:
                throw new IllegalArgumentException("The packet contains an unhandled OpCode");
        }
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
