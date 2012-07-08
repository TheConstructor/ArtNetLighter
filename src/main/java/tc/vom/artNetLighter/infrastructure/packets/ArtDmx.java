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


import tc.vom.artNetLighter.infrastructure.ArtNetToolkit;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

/**
 * Used to hold the data of an ArtDmx-Packet. Approximately every 4 Seconds all DMX-Values should be retransmitted.
 */
public class ArtDmx extends ArtNetPacket {
    public static final int MINIMUM_PACKET_SIZE = ArtNetPacket.FULL_HEADER_LENGTH + 6;
    /**
     * 1 Byte Sequence.
     * The sequence number is used to ensure that ArtDmx packets are used in the correct order. When Art-Net is carried over a medium such as the Internet, it is possible that ArtDmx packets will reach the receiver out of order.
     * This field is incremented in the range 0x01 to 0xff to allow the receiving node to resequence packets. The Sequence field is set to 0x00 to disable this feature.
     */
    private final int sequence;

    /**
     * 1 Byte Physical.
     * The physical input port from which DMX512 data was input. This field is for information only. Use Universe for data routing.
     */
    private final int physical;

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

    public ArtDmx(final int sequence, final int physical, final int portAddress, final byte[] data) {
        super(ArtNetOpCodes.OP_CODE_DMX);
        this.sequence = sequence;
        this.physical = physical;
        this.portAddress = portAddress;
        this.length = data.length;
        this.data = data;
    }

    public int getSequence() {
        return this.sequence;
    }

    public int getPhysical() {
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
        return (this.data[i] & 0xff);
    }

    @Override
    public byte[] constructPacket() {
        return ArtDmx.constructPacket(this.sequence, this.physical, this.portAddress, this.data);
    }

    public static byte[] constructPacket(final int sequence, final int physical, final int portAddress, final byte[] data) {
        final byte[] result = ArtNetPacket.constructPacket(ArtDmx.MINIMUM_PACKET_SIZE + data.length, ArtNetOpCodes.OP_CODE_DMX);
        result[ArtNetPacket.FULL_HEADER_LENGTH] = (byte) sequence;
        result[ArtNetPacket.FULL_HEADER_LENGTH + 1] = (byte) physical;
        ArtNetToolkit.set2BytesLowToHigh(portAddress, result, ArtNetPacket.FULL_HEADER_LENGTH + 2);
        ArtNetToolkit.set2BytesHighToLow(data.length, result, ArtNetPacket.FULL_HEADER_LENGTH + 4);
        ArtNetToolkit.copyToArray(data, result, ArtNetPacket.FULL_HEADER_LENGTH + 6);
        return result;
    }

    public ArtDmx(final byte[] pData) {
        super(pData);
        if (pData.length < ArtPoll.PACKET_LENGTH) {
            throw new IllegalArgumentException("Minimum size for ArtDmx is " + ArtDmx.MINIMUM_PACKET_SIZE);
        }
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_DMX) {
            throw new IllegalArgumentException("Provided data specifies a wrong OpCode");
        }
        this.sequence = pData[ArtNetPacket.FULL_HEADER_LENGTH];
        this.physical = pData[ArtNetPacket.FULL_HEADER_LENGTH + 1];
        this.portAddress = ArtNetToolkit.get2BytesLowToHigh(pData, ArtNetPacket.FULL_HEADER_LENGTH + 2);
        this.length = ArtNetToolkit.get2BytesHighToLow(pData, ArtNetPacket.FULL_HEADER_LENGTH + 4);
        this.data = ArtNetToolkit.copyBytesFromArray(pData, ArtNetPacket.FULL_HEADER_LENGTH + 6, this.length);
    }
}
