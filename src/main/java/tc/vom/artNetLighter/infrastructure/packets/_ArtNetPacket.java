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

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.get2BytesLowToHigh;
import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.set2BytesLowToHigh;

/**
 * Represents an Art-Net packet that could be send or received.
 */
public abstract class _ArtNetPacket implements ArtNetOpCodes {
    public static final int SHORT_HEADER_LENGTH = 10;
    /**
     * 8 Byte identification
     */
    public static final byte[] ART_NET_ID = {'A', 'r', 't', '-', 'N', 'e', 't', 0};
    public static final Charset STRING_CHARSET = Charset.forName("ASCII");
    public static final int SHORT_NAME_LENGTH = 18;
    public static final int LONG_NAME_LENGTH = 64;


    /**
     * 2 Byte OpCode
     */
    private final int opCode;

    public _ArtNetPacket(final int opCode) {
        this.opCode = opCode;
    }


    public _ArtNetPacket(final byte[] data) {
        if (data.length < _ArtNetPacket.SHORT_HEADER_LENGTH) {
            throw new IllegalArgumentException("Minimum size for Packet Header is " + _ArtNetPacket.SHORT_HEADER_LENGTH);
        }
        final byte[] header = new byte[_ArtNetPacket.ART_NET_ID.length];
        System.arraycopy(data, 0, header, 0, header.length);
        if (!Arrays.equals(_ArtNetPacket.ART_NET_ID, header)) {
            throw new IllegalArgumentException("Packet data must start with _ArtNetPacket.ART_NET_ID: " + Arrays.toString(_ArtNetPacket.ART_NET_ID));
        }
        this.opCode = get2BytesLowToHigh(data, 8);
    }

    /**
     * 2 Byte OpCode
     */
    public int getOpCode() {
        return this.opCode;
    }

    /**
     * Encode all stored information into an byte[]-array suitable for usage as data of an UPD-datagram.
     *
     * @return UPD-datagram data
     */
    public abstract byte[] constructPacket();

    public static byte[] constructUnversionedPacket(final int packetLength, final int opCode) {
        if (packetLength < _ArtNetPacket.SHORT_HEADER_LENGTH) {
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        }
        final byte[] result = new byte[packetLength];
        System.arraycopy(_ArtNetPacket.ART_NET_ID, 0, result, 0, _ArtNetPacket.ART_NET_ID.length);
        set2BytesLowToHigh(opCode, result, 8);
        return result;
    }

    public static _ArtNetPacket parsePacket(final byte[] pData) {
        final int opCode = get2BytesLowToHigh(pData, 8);
        switch (opCode) {
            case ArtNetOpCodes.OP_CODE_ADDRESS:
                return new ArtAddress(pData);
            case ArtNetOpCodes.OP_CODE_DIAGNOSTIC_DATA:
                return new ArtDiagData(pData);
            case ArtNetOpCodes.OP_CODE_DMX:
                return new ArtDmx(pData);
            case ArtNetOpCodes.OP_CODE_INPUT:
                return new ArtInput(pData);
            case ArtNetOpCodes.OP_CODE_IP_PROGRAM:
                return new ArtIpProg(pData);
            case ArtNetOpCodes.OP_CODE_IP_PROGRAM_REPLY:
                return new ArtIpProgReply(pData);
            case ArtNetOpCodes.OP_CODE_POLL:
                return new ArtPoll(pData);
            case ArtNetOpCodes.OP_CODE_POLL_REPLY:
                return new ArtPollReply(pData);
            case ArtNetOpCodes.OP_CODE_TIME_CODE:
                return new ArtTimeCode(pData);
            default:
                throw new IllegalArgumentException("The packet contains an unhandled OpCode");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof _ArtNetPacket)) {
            return false;
        }

        final _ArtNetPacket that = (_ArtNetPacket) o;

        //noinspection RedundantIfStatement
        if (this.opCode != that.opCode) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return this.opCode;
    }
}
