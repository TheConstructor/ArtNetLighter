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

import java.util.Arrays;

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.*;

/**
 * Implements ArtDiagData packet.
 */
public class ArtDiagData extends ArtNetPacket {

    public static final int MAX_DATA_LENGTH = 512;
    public static final int MINIMUM_PACKET_LENGTH = ArtNetPacket.FULL_HEADER_LENGTH + 6;

    public static final class PriorityConstants {
        /**
         * Low priority message
         */
        public static final byte DpLow = 0x10;
        /**
         * Medium priority message
         */
        public static final byte DpMed = 0x40;
        /**
         * High priority message
         */
        public static final byte DpHigh = (byte) 0x80;
        /**
         * Critical priority message
         */
        public static final byte DpCritical = (byte) 0xe0;
        /**
         * Volatile message. Messages of this type are displayed on a single line in the DMX-Workshop diagnostics display. All other types are displayed in a list box.
         */
        public static final byte DpVolatile = (byte) 0xff;

        private PriorityConstants() {

        }
    }

    /**
     * 1 Byte Filler
     */
    private final byte filler1;

    /**
     * 1 Byte Priority
     *
     * @see PriorityConstants
     */
    private final byte priority;

    /**
     * 1 Bytes filler.
     */
    private final byte filler2;

    /**
     * 1 Bytes filler.
     */
    private final byte filler3;

    /**
     * 2 Bytes Data Length (High-Byte first)
     */
    private final int length;

    /**
     * {@link #length} Bytes ASCII text, null terminated. Max length ist 512 bytes including the null terminator.
     */
    private final byte[] data;

    public ArtDiagData(final byte priority, final String data) {
        this(priority, data.getBytes(ArtNetPacket.STRING_CHARSET));
    }

    public ArtDiagData(final byte priority, final byte[] data) {
        this((byte) 0, priority, (byte) 0, (byte) 0, data);
    }

    private ArtDiagData(final byte filler1, final byte priority, final byte filler2, final byte filler3, byte[] data) {
        super(ArtNetOpCodes.OP_CODE_DIAGNOSTIC_DATA);
        if ((data.length > ArtDiagData.MAX_DATA_LENGTH) || ((data.length == ArtDiagData.MAX_DATA_LENGTH) && (data[ArtDiagData.MAX_DATA_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Data has a maximum length of " + (ArtDiagData.MAX_DATA_LENGTH) + " Bytes and is 0-terminated.");
        }
        if (data[data.length - 1] != 0) {
            final byte[] t_data = new byte[data.length + 1];
            copyBytesToArray(data, t_data, 0);
            data = t_data;
        }
        this.filler1 = filler1;
        this.priority = priority;
        this.filler2 = filler2;
        this.filler3 = filler3;
        this.length = data.length;
        this.data = data;
    }

    public byte getFiller1() {
        return this.filler1;
    }

    public byte getPriority() {
        return this.priority;
    }

    public byte getFiller2() {
        return this.filler2;
    }

    public byte getFiller3() {
        return this.filler3;
    }

    public int getLength() {
        return this.length;
    }

    public String getData() {
        return copyStringFromArray(this.data, 0, this.data.length);
    }

    public byte[] getDataBytes() {
        return this.data;
    }

    @Override
    public byte[] constructPacket() {
        return ArtDiagData.constructPacket(this.filler1, this.priority, this.filler2, this.filler3, this.data);
    }

    public static byte[] constructPacket(final byte filler1, final byte priority, final byte filler2, final byte filler3, String data) {
        if (data == null) {
            data = "";
        }
        final byte[] dataBytes = data.getBytes(ArtNetPacket.STRING_CHARSET);
        return ArtDiagData.constructPacket(filler1, priority, filler2, filler3, dataBytes);
    }

    public static byte[] constructPacket(final byte filler1, final byte priority, final byte filler2, final byte filler3, byte[] data) {
        if ((data.length > ArtDiagData.MAX_DATA_LENGTH) || ((data.length == ArtDiagData.MAX_DATA_LENGTH) && (data[ArtDiagData.MAX_DATA_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Data has a maximum length of " + (ArtDiagData.MAX_DATA_LENGTH) + " Bytes and is 0-terminated.");
        }
        if (data[data.length - 1] != 0) {
            final byte[] t_data = new byte[data.length + 1];
            copyBytesToArray(data, t_data, 0);
            data = t_data;
        }
        final byte[] pData = ArtNetPacket.constructPacket(ArtDiagData.MINIMUM_PACKET_LENGTH + data.length, ArtNetOpCodes.OP_CODE_DIAGNOSTIC_DATA);
        pData[ArtNetPacket.FULL_HEADER_LENGTH] = filler1;
        pData[ArtNetPacket.FULL_HEADER_LENGTH + 1] = priority;
        pData[ArtNetPacket.FULL_HEADER_LENGTH + 2] = filler2;
        pData[ArtNetPacket.FULL_HEADER_LENGTH + 3] = filler3;
        set2BytesHighToLow(data.length, pData, ArtNetPacket.FULL_HEADER_LENGTH + 4);
        copyBytesToArray(data, pData, ArtNetPacket.FULL_HEADER_LENGTH + 6);
        return pData;
    }

    public ArtDiagData(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_DIAGNOSTIC_DATA) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtDiagData.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtDiagData.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.filler1 = pData[ArtNetPacket.FULL_HEADER_LENGTH];
        this.priority = pData[ArtNetPacket.FULL_HEADER_LENGTH + 1];
        this.filler2 = pData[ArtNetPacket.FULL_HEADER_LENGTH + 2];
        this.filler3 = pData[ArtNetPacket.FULL_HEADER_LENGTH + 3];
        this.length = get2BytesHighToLow(pData, ArtNetPacket.FULL_HEADER_LENGTH + 4);
        this.data = copyBytesFromArray(pData, ArtNetPacket.FULL_HEADER_LENGTH + 6, this.length);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtDiagData)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtDiagData that = (ArtDiagData) o;

        if (this.length != that.length) {
            return false;
        }
        if (this.priority != that.priority) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (!Arrays.equals(this.data, that.data)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.priority;
        result = (31 * result) + this.length;
        result = (31 * result) + ((this.data != null) ? Arrays.hashCode(this.data) : 0);
        return result;
    }
}
