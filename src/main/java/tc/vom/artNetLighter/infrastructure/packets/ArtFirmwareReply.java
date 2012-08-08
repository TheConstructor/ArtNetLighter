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

import tc.vom.artNetLighter.infrastructure.ByteArrayToolkit;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

/**
 * Implements ArtFirmwareReply packet.
 */
public class ArtFirmwareReply extends _VersionedArtNetPacket {

    public static final int SPARE_LENGTH = 21;
    public static final byte[] SPARE_BYTES = new byte[ArtFirmwareReply.SPARE_LENGTH];

    private static final int START_FILLER1 = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_FILLER2 = ArtFirmwareReply.START_FILLER1 + 1;
    private static final int START_TYPE = ArtFirmwareReply.START_FILLER2 + 1;
    private static final int START_SPARE = ArtFirmwareReply.START_TYPE + 1;

    public static final int MINIMUM_PACKET_LENGTH = ArtFirmwareReply.START_SPARE;

    public static interface TypeConstants {
        /**
         * Last packet received successfully.
         */
        public static int FirmBlockGood = 0x00;
        /**
         * All firmware received successfully.
         */
        public static int FirmAllGood = 0x01;
        /**
         * Firmware upload failed. (All error conditions)
         */
        public static int FirmFail = 0xff;
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
     * Defines the packet contents.
     *
     * @see TypeConstants
     */
    private final byte type;

    /**
     * 21 Byte Spare.
     */
    private final byte[] spare;

    public ArtFirmwareReply(final byte type) {
        this((byte) 0, (byte) 0, type, ArtFirmwareReply.SPARE_BYTES);
    }

    public ArtFirmwareReply(final byte filler1, final byte filler2, final byte type, final byte[] spare) {
        super(ArtNetOpCodes.OP_CODE_FIRMWARE_REPLY);
        this.filler1 = filler1;
        this.filler2 = filler2;
        this.type = type;
        this.spare = spare;
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

    public byte[] getSpare() {
        return this.spare;
    }

    @Override
    public byte[] constructPacket() {
        return ArtFirmwareReply.constructPacket(this.filler1, this.filler2, this.type, this.spare);
    }

    public static byte[] constructPacket(final byte filler1, final byte filler2, final byte type, final byte[] spare) {
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtFirmwareReply.MINIMUM_PACKET_LENGTH + spare.length, ArtNetOpCodes.OP_CODE_FIRMWARE_REPLY);
        pData[ArtFirmwareReply.START_FILLER1] = filler1;
        pData[ArtFirmwareReply.START_FILLER2] = filler2;
        pData[ArtFirmwareReply.START_TYPE] = type;
        ByteArrayToolkit.setBytes(spare, pData, ArtFirmwareReply.START_SPARE);
        return pData;
    }

    public ArtFirmwareReply(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_FIRMWARE_REPLY) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtFirmwareReply.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtFirmwareReply.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.filler1 = pData[ArtFirmwareReply.START_FILLER1];
        this.filler2 = pData[ArtFirmwareReply.START_FILLER2];
        this.type = pData[ArtFirmwareReply.START_TYPE];
        this.spare = ByteArrayToolkit.getBytes(pData, ArtFirmwareReply.START_SPARE);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtFirmwareReply)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtFirmwareReply that = (ArtFirmwareReply) o;

        //noinspection RedundantIfStatement
        if (this.type != that.type) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.type;
        return result;
    }
}
