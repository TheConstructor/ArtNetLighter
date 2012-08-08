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

import tc.vom.artNetLighter.infrastructure.BinaryToolkit;
import tc.vom.artNetLighter.infrastructure.ByteArrayToolkit;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

import java.util.Arrays;

/**
 * Implements ArtTodData packet.
 */
public class ArtTodData extends _VersionedArtNetPacket {

    public static final int SPARE_LENGTH = 7;
    public static final byte[] SPARE_BYTES = new byte[ArtTodData.SPARE_LENGTH];

    public static final int MAXIMUM_TABLE_OF_DEVICES_LENGTH = 255;
    public static final int UID_LENGTH = 6;

    private static final int START_RDM_VERSION = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_PORT = ArtTodData.START_RDM_VERSION + 1;
    private static final int START_SPARE = ArtTodData.START_PORT + 1;
    private static final int START_NET = ArtTodData.START_SPARE + ArtTodData.SPARE_LENGTH;
    private static final int START_COMMAND_RESPONSE = ArtTodData.START_NET + 1;
    private static final int START_ADDRESS = ArtTodData.START_COMMAND_RESPONSE + 1;
    private static final int START_UID_TOTAL = ArtTodData.START_ADDRESS + 1;
    private static final int START_BLOCK_COUNT = ArtTodData.START_UID_TOTAL + 2;
    private static final int START_UID_COUNT = ArtTodData.START_BLOCK_COUNT + 1;
    private static final int START_TABLE_OF_DEVICES = ArtTodData.START_UID_COUNT + 1;
    private static final int MINIMUM_PACKET_LENGTH = ArtTodData.START_TABLE_OF_DEVICES;

    public static interface RdmVersionConstants {
        /**
         * Art-Net Devices that only support RDM DRAFT V1.0 set field to 0x00.
         */
        public static final byte RdmDraft1_0 = 0x00;
        /**
         * Devices that support RDM STANDARD V1.0 set field to 0x01.
         */
        public static final byte RdmStandard1_0 = 0x01;
    }

    public static interface CommandResponseConstants {
        /**
         * The packet contains the entire TOD or is the first packet in a sequence of packets that contains the entire TOD.
         */
        public static final byte TodFull = 0x00;

        /**
         * The TOD is not available.
         */
        public static final byte TodNak = (byte) 0xff;
    }

    /**
     * 1 Byte RdmVer.
     *
     * @see RdmVersionConstants
     */
    private final byte rdmVersion;

    /**
     * 1 Byte Port.
     * Physical Port. Range 1-4.
     */
    private final byte port;

    /**
     * 7 Bytes Spare.
     */
    private final byte[] spare;

    /**
     * 1 Byte Net.
     * The top 7 bits of the Port-Address of the Output Gateway DMX Port that generated this packet.
     */
    private final byte net;

    /**
     * 1 Byte Command Response
     *
     * @see CommandResponseConstants
     */
    private final byte commandResponse;

    /**
     * 1 Byte Address.
     * The low 8 bits of the Port-Address of the Output Gateway DMX Port that generated this packet. The high nibble is the Sub-Net switch. The low nibble corresponds to the Universe.
     */
    private final byte address;

    /**
     * 2 Bytes UidTotal (High-Byte-first)
     */
    private final int uidTotal;

    /**
     * 1 Byte BlockCount.
     * The index number of this packet. When UidTotal exceeds 200, multiple ArtTodData packets are used. BlockCount is set to zero for the first packet, and incremented for each subsequent packet containing blocks of TOD information.
     */
    private final byte blockCount;

    /**
     * 1 Byte UidCount.
     * The number of UIDs encoded in this packet. This is the length of the following array.
     */
    private final byte uidCount;

    /**
     * {@link #uidCount} * 6 Bytes TOD.
     * An array of RDM UID.
     */
    private final byte[][] tableOfDevices;

    public ArtTodData(final byte rdmVersion, final byte port, final byte net, final byte commandResponse, final byte address, final int uidTotal, final byte blockCount, final byte[][] tableOfDevices) {
        this(rdmVersion, port, ArtTodData.SPARE_BYTES, net, commandResponse, address, uidTotal, blockCount, tableOfDevices);
    }

    private ArtTodData(final byte rdmVersion, final byte port, final byte[] spare, final byte net, final byte commandResponse, final byte address, final int uidTotal, final byte blockCount, final byte[][] tableOfDevices) {
        super(ArtNetOpCodes.OP_CODE_TOD_DATA);
        this.rdmVersion = rdmVersion;
        this.port = port;
        this.spare = spare;
        this.net = net;
        this.commandResponse = commandResponse;
        this.address = address;
        this.uidTotal = uidTotal;
        this.blockCount = blockCount;
        this.uidCount = (byte) tableOfDevices.length;
        this.tableOfDevices = tableOfDevices;
    }

    public byte getRdmVersion() {
        return this.rdmVersion;
    }

    public byte getPort() {
        return this.port;
    }

    public byte[] getSpare() {
        return this.spare;
    }

    public byte getNet() {
        return this.net;
    }

    public byte getCommandResponse() {
        return this.commandResponse;
    }

    public byte getAddress() {
        return this.address;
    }

    public int getUidTotal() {
        return this.uidTotal;
    }

    public byte getBlockCount() {
        return this.blockCount;
    }

    public byte getUidCount() {
        return this.uidCount;
    }

    public byte[][] getTableOfDevices() {
        return this.tableOfDevices;
    }

    @Override
    public byte[] constructPacket() {
        return ArtTodData.constructPacket(this.rdmVersion, this.port, this.spare, this.net, this.commandResponse, this.address, this.uidTotal, this.blockCount, this.tableOfDevices);
    }

    public static byte[] constructPacket(final byte rdmVersion, final byte port, final byte[] spare, final byte net, final byte commandResponse, final byte address, final int uidTotal, final byte blockCount, final byte[][] tableOfDevices) {
        if (spare.length > ArtTodData.SPARE_LENGTH) {
            throw new IllegalArgumentException("spare has a maximum length of " + ArtTodData.SPARE_LENGTH + " Bytes.");
        }
        if (tableOfDevices.length > ArtTodData.MAXIMUM_TABLE_OF_DEVICES_LENGTH) {
            throw new IllegalArgumentException("tableOfDevices has a maximum length of " + ArtTodData.MAXIMUM_TABLE_OF_DEVICES_LENGTH + " Bytes.");
        }
        for (int i = 0; i < tableOfDevices.length; i++) {
            if (tableOfDevices[i].length != ArtTodData.UID_LENGTH) {
                throw new IllegalArgumentException("tableOfDevices seems to contain no UID at position " + i + " as inner-array-length is " + tableOfDevices[i].length + " and not " + ArtTodData.UID_LENGTH + ".");
            }
        }
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtTodData.MINIMUM_PACKET_LENGTH + (tableOfDevices.length * ArtTodData.UID_LENGTH), ArtNetOpCodes.OP_CODE_TOD_DATA);
        pData[ArtTodData.START_RDM_VERSION] = rdmVersion;
        pData[ArtTodData.START_PORT] = port;
        ByteArrayToolkit.setBytes(spare, pData, ArtTodData.START_SPARE);
        pData[ArtTodData.START_NET] = net;
        pData[ArtTodData.START_COMMAND_RESPONSE] = commandResponse;
        pData[ArtTodData.START_ADDRESS] = address;
        ByteArrayToolkit.set2BytesHighToLow(uidTotal, pData, ArtTodData.START_UID_TOTAL);
        pData[ArtTodData.START_BLOCK_COUNT] = blockCount;
        pData[ArtTodData.START_UID_COUNT] = (byte) tableOfDevices.length;
        for (int i = 0; i < tableOfDevices.length; i++) {
            ByteArrayToolkit.setBytes(tableOfDevices[i], pData, ArtTodData.START_TABLE_OF_DEVICES + (i * ArtTodData.UID_LENGTH));
        }
        return pData;
    }

    public ArtTodData(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_TOD_DATA) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtTodData.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtTodData.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.rdmVersion = pData[ArtTodData.START_RDM_VERSION];
        this.port = pData[ArtTodData.START_PORT];
        this.spare = ByteArrayToolkit.getBytes(pData, ArtTodData.START_SPARE, ArtTodData.SPARE_LENGTH);
        this.net = pData[ArtTodData.START_NET];
        this.commandResponse = pData[ArtTodData.START_COMMAND_RESPONSE];
        this.address = pData[ArtTodData.START_ADDRESS];
        this.uidTotal = ByteArrayToolkit.get2BytesHighToLow(pData, ArtTodData.START_UID_TOTAL);
        this.blockCount = pData[ArtTodData.START_BLOCK_COUNT];
        this.uidCount = pData[ArtTodData.START_UID_COUNT];
        this.tableOfDevices = new byte[BinaryToolkit.getUnsignedValue(this.uidCount)][];
        for (int i = 0; i < this.tableOfDevices.length; i++) {
            this.tableOfDevices[i] = ByteArrayToolkit.getBytes(pData, ArtTodData.START_TABLE_OF_DEVICES + (i * ArtTodData.UID_LENGTH));
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtTodData)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtTodData that = (ArtTodData) o;

        if (this.address != that.address) {
            return false;
        }
        if (this.blockCount != that.blockCount) {
            return false;
        }
        if (this.commandResponse != that.commandResponse) {
            return false;
        }
        if (this.net != that.net) {
            return false;
        }
        if (this.port != that.port) {
            return false;
        }
        if (this.rdmVersion != that.rdmVersion) {
            return false;
        }
        if (this.uidCount != that.uidCount) {
            return false;
        }
        if (this.uidTotal != that.uidTotal) {
            return false;
        }
        if (this.tableOfDevices.length != that.tableOfDevices.length) {
            return false;
        }

        for (int i = 0; i < this.tableOfDevices.length; i++) {
            if (!Arrays.equals(this.tableOfDevices[i], that.tableOfDevices[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.rdmVersion;
        result = (31 * result) + this.port;
        result = (31 * result) + this.net;
        result = (31 * result) + this.commandResponse;
        result = (31 * result) + this.address;
        result = (31 * result) + this.uidTotal;
        result = (31 * result) + this.blockCount;
        result = (31 * result) + this.uidCount;
        for (final byte[] uid : this.tableOfDevices) {
            result = (31 * result) + Arrays.hashCode(uid);
        }
        return result;
    }
}
