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

import static tc.vom.artNetLighter.infrastructure.ByteArrayToolkit.*;

/**
 * Implements ArtFirmwareMaster packet.
 */
public class ArtFirmwareMaster extends _VersionedArtNetPacket {

    public static final int MAX_DATA_LENGTH = 512;
    public static final int SPARE_LENGTH = 20;

    private static final int START_FILLER1 = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_FILLER2 = ArtFirmwareMaster.START_FILLER1 + 1;
    private static final int START_TYPE = ArtFirmwareMaster.START_FILLER2 + 1;
    private static final int START_BLOCK_ID = ArtFirmwareMaster.START_TYPE + 1;
    private static final int START_FIRMWARE_LENGTH = ArtFirmwareMaster.START_BLOCK_ID + 1;
    private static final int START_SPARE = ArtFirmwareMaster.START_FIRMWARE_LENGTH + 4;
    private static final int START_DATA = ArtFirmwareMaster.START_SPARE + ArtFirmwareMaster.SPARE_LENGTH;

    public static final int MINIMUM_PACKET_LENGTH = ArtFirmwareMaster.START_DATA;
    public static final byte[] SPARE_BYTES = new byte[ArtFirmwareMaster.SPARE_LENGTH];

    public static interface TypeConstants {
        /**
         * The first packet of a firmware upload.
         */
        public static final byte FirmFirst = 0x00;
        /**
         * A consecutive continuation packet of a firmware upload.
         */
        public static final byte FirmCont = 0x01;
        /**
         * The last packet of a firmware upload.
         */
        public static final byte FirmLast = 0x02;
        /**
         * The first packet of a UBEA upload.
         */
        public static final byte UbeaFirst = 0x03;
        /**
         * A consecutive continuation packet of a UBEA upload.
         */
        public static final byte UbeaCont = 0x04;
        /**
         * The last packet of a UBEA upload.
         */
        public static final byte UbeaLast = 0x05;
    }

    /**
     * 1 Byte Filler1.
     */
    private final byte filler1;

    /**
     * 1 Byte Filler2.
     */
    private final byte filler2;

    /**
     * 1 Byte Type.
     */
    private final byte type;

    /**
     * 1 Byte BlockId.
     */
    private final byte blockId;

    /**
     * 4 Byte FirmwareLength. (High-Byte first)
     * The total number of words (Int16) in the firmware upload plus the firmware header size. Eg a 32K word upload plus 530 words of header information == 0x00008212. This value is also the file size (in words) of the file to be uploaded.
     */
    private final int firmwareLength;

    /**
     * 20 Byte Spare.
     */
    private final byte[] spare;

    /**
     * 1024 Byte Data (512 x Int16).
     * This array contains the firmware or UBEA data block.
     * The order is hi byte first. The interpretation of this data is manufacturer specific.
     */
    private final short[] data;

    public ArtFirmwareMaster(final byte type, final byte blockId, final int firmwareLength, final short[] data) {
        this((byte) 0, (byte) 0, type, blockId, firmwareLength, ArtFirmwareMaster.SPARE_BYTES, data);
    }

    private ArtFirmwareMaster(final byte filler1, final byte filler2, final byte type, final byte blockId, final int firmwareLength, final byte[] spare, final short[] data) {
        super(ArtNetOpCodes.OP_CODE_FIRMWARE_MASTER);
        this.filler1 = filler1;
        this.filler2 = filler2;
        this.type = type;
        this.blockId = blockId;
        this.firmwareLength = firmwareLength;
        this.spare = spare;
        this.data = data;
    }

    public byte getFiller1() {
        return this.filler1;
    }

    public byte getFiller2() {
        return this.filler2;
    }

    public byte getType() {
        return this.type;
    }

    public byte getBlockId() {
        return this.blockId;
    }

    public int getFirmwareLength() {
        return this.firmwareLength;
    }

    public byte[] getSpare() {
        return this.spare;
    }

    public short[] getData() {
        return this.data;
    }

    @Override
    public byte[] constructPacket() {
        return ArtFirmwareMaster.constructPacket(this.filler1, this.filler2, this.type, this.blockId, this.firmwareLength, this.spare, this.data);
    }

    public static byte[] constructPacket(final byte filler1, final byte filler2, final byte type, final byte blockId, final int firmwareLength, final byte[] spare, final short[] data) {
        if (spare.length > ArtFirmwareMaster.SPARE_LENGTH) {
            throw new IllegalArgumentException("Spare has a maximum length of " + ArtFirmwareMaster.SPARE_LENGTH + " Bytes.");
        }
        if (data.length > ArtFirmwareMaster.MAX_DATA_LENGTH) {
            throw new IllegalArgumentException("Data has a maximum length of " + ArtFirmwareMaster.MAX_DATA_LENGTH + " Shorts.");
        }
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtFirmwareMaster.MINIMUM_PACKET_LENGTH + (data.length * 2), ArtNetOpCodes.OP_CODE_FIRMWARE_MASTER);
        pData[ArtFirmwareMaster.START_FILLER1] = filler1;
        pData[ArtFirmwareMaster.START_FILLER2] = filler2;
        pData[ArtFirmwareMaster.START_TYPE] = type;
        pData[ArtFirmwareMaster.START_BLOCK_ID] = blockId;
        set4BytesHighToLow(firmwareLength, pData, ArtFirmwareMaster.START_FIRMWARE_LENGTH);
        setBytes(spare, pData, ArtFirmwareMaster.START_SPARE);
        setShorts(data, pData, ArtFirmwareMaster.START_DATA);
        return pData;
    }

    public ArtFirmwareMaster(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_FIRMWARE_MASTER) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtFirmwareMaster.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtFirmwareMaster.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.filler1 = pData[ArtFirmwareMaster.START_FILLER1];
        this.filler2 = pData[ArtFirmwareMaster.START_FILLER2];
        this.type = pData[ArtFirmwareMaster.START_TYPE];
        this.blockId = pData[ArtFirmwareMaster.START_BLOCK_ID];
        this.firmwareLength = get4BytesHighToLow(pData, ArtFirmwareMaster.START_FIRMWARE_LENGTH);
        this.spare = getBytes(pData, ArtFirmwareMaster.START_SPARE, ArtFirmwareMaster.SPARE_LENGTH);
        this.data = getShorts(pData, ArtFirmwareMaster.START_DATA);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtFirmwareMaster)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtFirmwareMaster that = (ArtFirmwareMaster) o;

        if (this.blockId != that.blockId) {
            return false;
        }
        if (this.firmwareLength != that.firmwareLength) {
            return false;
        }
        if (this.type != that.type) {
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
        result = (31 * result) + this.type;
        result = (31 * result) + this.blockId;
        result = (31 * result) + this.firmwareLength;
        result = (31 * result) + ((this.data != null) ? Arrays.hashCode(this.data) : 0);
        return result;
    }
}
