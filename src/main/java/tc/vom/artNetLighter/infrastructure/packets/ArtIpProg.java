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
 * Implements ArtIpProg packet.
 */
public class ArtIpProg extends _VersionedArtNetPacket {

    public static final int SPARE_LENGTH = 8;
    public static final byte[] SPARE_BYTES = new byte[ArtIpProg.SPARE_LENGTH];
    public static final int FILLER1 = 0;
    public static final int FILLER2 = 0;

    private static final int START_FILLER1 = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_COMMAND = ArtIpProg.START_FILLER1 + 2;
    private static final int START_FILLER2 = ArtIpProg.START_COMMAND + 1;
    private static final int START_PROG_IP = ArtIpProg.START_FILLER2 + 1;
    private static final int START_PROG_SM = ArtIpProg.START_PROG_IP + 4;
    private static final int START_PROG_PORT = ArtIpProg.START_PROG_SM + 4;
    private static final int START_SPARE = ArtIpProg.START_PROG_PORT + 2;

    public static final int MINIMUM_PACKET_LENGTH = ArtIpProg.START_SPARE;

    /**
     * 2 Bytes Filler.
     * Pad length to match ArtPoll
     */
    private final int filler1;

    /**
     * 1 Byte Command.
     * Defines the how this packet is processed. If all bits are clear, this is an enquiry only.
     * 7 Set to enable any programming.
     * 6 Set to enable DHCP (if set ignore lower bits).
     * 5-4 Not used, transmit as zero
     * 3 Set to return all three parameters to default
     * 2 Program IP Address
     * 1 Program Subnet Mask
     * 0 Program Port
     */
    private final byte command;

    /**
     * 1 Byte Filler.
     * Pad data structure for word alignment
     */
    private final byte filler2;

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
     * 8 Byte Spare.
     */
    private final byte[] spare;

    public ArtIpProg(final byte command, final int progIp, final int progSm, final int progPort) {
        super(ArtNetOpCodes.OP_CODE_IP_PROGRAM);
        this.filler1 = ArtIpProg.FILLER1;
        this.command = command;
        this.filler2 = ArtIpProg.FILLER2;
        this.progIp = progIp;
        this.progSm = progSm;
        this.progPort = progPort;
        this.spare = ArtIpProg.SPARE_BYTES;
    }

    public int getFiller1() {
        return this.filler1;
    }

    public byte getCommand() {
        return this.command;
    }

    public byte getFiller2() {
        return this.filler2;
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

    public byte[] getSpare() {
        return this.spare;
    }

    @Override
    public byte[] constructPacket() {
        return ArtIpProg.constructPacket(this.filler1, this.command, this.filler2, this.progIp, this.progSm, this.progPort, this.spare);
    }

    public static byte[] constructPacket(final int filler1, final byte command, final byte filler2, final int progIp, final int progSm, final int progPort, final byte[] spare) {
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtIpProg.MINIMUM_PACKET_LENGTH + spare.length, ArtNetOpCodes.OP_CODE_IP_PROGRAM);
        ByteArrayToolkit.set2BytesHighToLow(filler1, pData, ArtIpProg.START_FILLER1);
        pData[ArtIpProg.START_COMMAND] = command;
        pData[ArtIpProg.START_FILLER2] = filler2;
        ByteArrayToolkit.set4BytesHighToLow(progIp, pData, ArtIpProg.START_PROG_IP);
        ByteArrayToolkit.set4BytesHighToLow(progSm, pData, ArtIpProg.START_PROG_SM);
        ByteArrayToolkit.set2BytesHighToLow(progPort, pData, ArtIpProg.START_PROG_PORT);
        ByteArrayToolkit.setBytes(spare, pData, ArtIpProg.START_SPARE);
        return pData;
    }

    public ArtIpProg(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_IP_PROGRAM) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtIpProg.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtIpProg.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.filler1 = ByteArrayToolkit.get2BytesHighToLow(pData, ArtIpProg.START_FILLER1);
        this.command = pData[ArtIpProg.START_COMMAND];
        this.filler2 = pData[ArtIpProg.START_FILLER2];
        this.progIp = ByteArrayToolkit.get4BytesHighToLow(pData, ArtIpProg.START_PROG_IP);
        this.progSm = ByteArrayToolkit.get4BytesHighToLow(pData, ArtIpProg.START_PROG_SM);
        this.progPort = ByteArrayToolkit.get2BytesHighToLow(pData, ArtIpProg.START_PROG_PORT);
        this.spare = ByteArrayToolkit.getBytes(pData, ArtIpProg.START_SPARE);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtIpProg)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtIpProg artIpProg = (ArtIpProg) o;

        if (this.command != artIpProg.command) {
            return false;
        }
        if (this.progIp != artIpProg.progIp) {
            return false;
        }
        if (this.progPort != artIpProg.progPort) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (this.progSm != artIpProg.progSm) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.command;
        result = (31 * result) + this.progIp;
        result = (31 * result) + this.progSm;
        result = (31 * result) + this.progPort;
        return result;
    }
}
