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
 * Used to hold the data of an ArtDmx-Packet. Approximately every 4 Seconds all DMX-Values should be retransmitted.
 */
public class ArtDmx extends _VersionedArtNetPacket {
    public static final int MAXIMUM_DATA_LENGTH = 512;

    private static final int START_SEQUENCE = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_PHYSICAL = ArtDmx.START_SEQUENCE + 1;
    private static final int START_PORT_ADDRESS = ArtDmx.START_PHYSICAL + 1;
    private static final int START_LENGTH = ArtDmx.START_PORT_ADDRESS + 2;
    private static final int START_DATA = ArtDmx.START_LENGTH + 2;

    public static final int MINIMUM_PACKET_SIZE = _VersionedArtNetPacket.FULL_HEADER_LENGTH + 6;

    /**
     * 1 Byte Sequence.
     * The sequence number is used to ensure that ArtDmx packets are used in the correct order. When Art-Net is carried over a medium such as the Internet, it is possible that ArtDmx packets will reach the receiver out of order.
     * This field is incremented in the range 0x01 to 0xff to allow the receiving node to resequence packets. The Sequence field is set to 0x00 to disable this feature.
     */
    private final byte sequence;

    /**
     * 1 Byte Physical.
     * The physical input port from which DMX512 data was input. This field is for information only. Use Universe for data routing.
     */
    private final byte physical;

    /**
     * 2 Byte Port Address.
     * Transferred Low-Byte first
     */
    private final int portAddress;

    /**
     * 2 Byte Length (High-Byte first)
     * The length of the DMX512 data array. This value should be an even number in the range 2 – 512.
     * It represents the number of DMX512 channels encoded in packet. NB: Products which convert Art-Net to DMX512 may opt to always send 512 channels.
     */
    private final int length;

    /**
     * {@link #length} Bytes DMX-data.
     */
    private final byte[] data;

    public ArtDmx(final byte sequence, final byte physical, final int portAddress, final byte[] data) {
        super(ArtNetOpCodes.OP_CODE_DMX);
        this.sequence = sequence;
        this.physical = physical;
        this.portAddress = portAddress;
        this.length = data.length;
        this.data = data;
    }

    public byte getSequence() {
        return this.sequence;
    }

    public byte getPhysical() {
        return this.physical;
    }

    public int getPortAddress() {
        return this.portAddress;
    }

    public int getLength() {
        return this.length;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getData(final int i) {
        return BinaryToolkit.getUnsignedValue(this.data[i]);
    }

    @Override
    public byte[] constructPacket() {
        return ArtDmx.constructPacket(this.sequence, this.physical, this.portAddress, this.data);
    }

    public static byte[] constructPacket(final byte sequence, final byte physical, final int portAddress, final byte[] data) {
        if (data.length > ArtDmx.MAXIMUM_DATA_LENGTH) {
            throw new IllegalArgumentException("data has a maximum length of " + ArtDmx.MAXIMUM_DATA_LENGTH + " Bytes.");
        }
        final byte[] result = _VersionedArtNetPacket.constructPacket(ArtDmx.MINIMUM_PACKET_SIZE + data.length, ArtNetOpCodes.OP_CODE_DMX);
        result[ArtDmx.START_SEQUENCE] = sequence;
        result[ArtDmx.START_PHYSICAL] = physical;
        ByteArrayToolkit.set2BytesLowToHigh(portAddress, result, ArtDmx.START_PORT_ADDRESS);
        ByteArrayToolkit.set2BytesHighToLow(data.length, result, ArtDmx.START_LENGTH);
        ByteArrayToolkit.setBytes(data, result, ArtDmx.START_DATA);
        return result;
    }

    public ArtDmx(final byte[] pData) {
        super(pData);
        if (pData.length < ArtDmx.MINIMUM_PACKET_SIZE) {
            throw new IllegalArgumentException("Minimum size for ArtDmx is " + ArtDmx.MINIMUM_PACKET_SIZE);
        }
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_DMX) {
            throw new IllegalArgumentException("Provided data specifies a wrong OpCode");
        }
        this.sequence = pData[ArtDmx.START_SEQUENCE];
        this.physical = pData[ArtDmx.START_PHYSICAL];
        this.portAddress = ByteArrayToolkit.get2BytesLowToHigh(pData, ArtDmx.START_PORT_ADDRESS);
        this.length = ByteArrayToolkit.get2BytesHighToLow(pData, ArtDmx.START_LENGTH);
        this.data = ByteArrayToolkit.getBytes(pData, ArtDmx.START_DATA, this.length);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtDmx)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtDmx artDmx = (ArtDmx) o;

        if (this.length != artDmx.length) {
            return false;
        }
        if (this.physical != artDmx.physical) {
            return false;
        }
        if (this.portAddress != artDmx.portAddress) {
            return false;
        }
        if (this.sequence != artDmx.sequence) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (!Arrays.equals(this.data, artDmx.data)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.sequence;
        result = (31 * result) + this.physical;
        result = (31 * result) + this.portAddress;
        result = (31 * result) + this.length;
        result = (31 * result) + ((this.data != null) ? Arrays.hashCode(this.data) : 0);
        return result;
    }
}
