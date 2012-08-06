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

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.*;

/**
 * Implements ArtIpProgReply packet.
 */
public class ArtIpProgReply extends ArtNetPacket {
    public static final byte[] FILLER_BYTES = new byte[4];
    public static final byte[] SPARE_BYTES = new byte[7];
    public static final int MINIMUM_PACKET_LENGTH = ArtNetPacket.FULL_HEADER_LENGTH + 15;
    /**
     * 4 Bytes Filler to match length of ArtIpProg
     */
    private final byte[] filler;

    /**
     * 4 Bytes ProgIp.
     * IP Address to be programmed into Node if enabled by Command Field
     */
    private final int progIp;

    /**
     * 4 Bytes ProgSm.
     * Subnet mask to be programmed into Node if enabled by Command Field
     */
    private final int progSm;

    /**
     * 2 Byte ProgPort.
     * PortAddress to be programmed into Node if enabled by Command Field
     */
    private final int progPort;

    /**
     * 1 Byte Status:
     * Bit 7: 0
     * Bit 6: DHCP enabled?
     * Bit 5-0: 0
     */
    private final byte status;

    /**
     * 7 Byte Spare
     */
    private final byte[] spare;

    public ArtIpProgReply(final int progIp, final int progSm, final int progPort, final byte status) {
        this(ArtIpProgReply.FILLER_BYTES, progIp, progSm, progPort, status, ArtIpProgReply.SPARE_BYTES);
    }

    private ArtIpProgReply(final byte[] filler, final int progIp, final int progSm, final int progPort, final byte status, final byte[] spare) {
        super(ArtNetOpCodes.OP_CODE_IP_PROGRAM_REPLY);
        this.filler = filler;
        this.progIp = progIp;
        this.progSm = progSm;
        this.progPort = progPort;
        this.status = status;
        this.spare = spare;
    }

    public byte[] getFiller() {
        return this.filler;
    }

    public int getProgIp() {
        return this.progIp;
    }

    public int getProgSm() {
        return this.progSm;
    }

    public int getProgPort() {
        return this.progPort;
    }

    public byte getStatus() {
        return this.status;
    }

    public byte[] getSpare() {
        return this.spare;
    }

    @Override
    public byte[] constructPacket() {
        return ArtIpProgReply.constructPaket(this.filler, this.progIp, this.progSm, this.progPort, this.status, this.spare);
    }

    public static byte[] constructPaket(final byte[] filler, final int progIp, final int progSm, final int progPort, final byte status, final byte[] spare) {
        final byte[] pData = ArtNetPacket.constructPacket(ArtIpProgReply.MINIMUM_PACKET_LENGTH + spare.length, ArtNetOpCodes.OP_CODE_IP_PROGRAM_REPLY);
        if (filler.length > 4) {
            throw new IllegalArgumentException("Maximum filler.length is 4");
        }
        copyBytesToArray(filler, pData, ArtNetPacket.FULL_HEADER_LENGTH);
        set4BytesHighToLow(progIp, pData, ArtNetPacket.FULL_HEADER_LENGTH + 4);
        set4BytesHighToLow(progSm, pData, ArtNetPacket.FULL_HEADER_LENGTH + 8);
        set2BytesHighToLow(progPort, pData, ArtNetPacket.FULL_HEADER_LENGTH + 16);
        pData[ArtNetPacket.FULL_HEADER_LENGTH + 18] = status;
        copyBytesToArray(spare, pData, ArtNetPacket.FULL_HEADER_LENGTH + 19);
        return pData;
    }

    public ArtIpProgReply(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_IP_PROGRAM_REPLY) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtIpProgReply.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtIpProgReply.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.filler = copyBytesFromArray(pData, ArtNetPacket.FULL_HEADER_LENGTH, 4);
        this.progIp = get4BytesHighToLow(pData, ArtNetPacket.FULL_HEADER_LENGTH + 4);
        this.progSm = get4BytesHighToLow(pData, ArtNetPacket.FULL_HEADER_LENGTH + 8);
        this.progPort = get2BytesHighToLow(pData, ArtNetPacket.FULL_HEADER_LENGTH + 16);
        this.status = pData[ArtNetPacket.FULL_HEADER_LENGTH + 18];
        this.spare = copyBytesFromArray(pData, ArtNetPacket.FULL_HEADER_LENGTH + 19);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtIpProgReply)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtIpProgReply that = (ArtIpProgReply) o;

        if (this.progIp != that.progIp) {
            return false;
        }
        if (this.progPort != that.progPort) {
            return false;
        }
        if (this.progSm != that.progSm) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (this.status != that.status) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.progIp;
        result = (31 * result) + this.progSm;
        result = (31 * result) + this.progPort;
        result = (31 * result) + this.status;
        return result;
    }
}
