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
 * Implements ArtInput packet.
 */
public class ArtInput extends _VersionedArtNetPacket {
    public static final int PACKET_LENGTH = _VersionedArtNetPacket.FULL_HEADER_LENGTH + 8;
    /**
     * 1 Byte Filler1.
     */
    private final byte filler1;

    /**
     * 1 Byte Filler2.
     */
    private final byte filler2;

    /**
     * 2 Byte NumPorts. (High-Byte first)
     * If number of inputs is not equal
     * to number of outputs, the largest value is taken. The maximum value is 4.
     */
    private final int numPorts;

    /**
     * 4 Bytes Input.
     * This array defines input disable status of each channel. (Example = 0x01, 0x00, 0x01, 0x00  to disable first and third inputs)
     * <p/>
     * Bits:
     * 7-1 Not currently used
     * 0 Set to disable this input
     */
    private final byte[] input;

    public ArtInput(final int numPorts, final byte[] input) {
        this((byte) 0, (byte) 0, numPorts, input);
    }

    private ArtInput(final byte filler1, final byte filler2, final int numPorts, final byte[] input) {
        super(ArtNetOpCodes.OP_CODE_INPUT);
        this.filler1 = filler1;
        this.filler2 = filler2;
        this.numPorts = numPorts;
        this.input = input;
    }

    public byte getFiller1() {
        return this.filler1;
    }

    public byte getFiller2() {
        return this.filler2;
    }

    public int getNumPorts() {
        return this.numPorts;
    }

    public byte[] getInput() {
        return this.input;
    }

    @Override
    public byte[] constructPacket() {
        return ArtInput.constructPacket(this.filler1, this.filler2, this.numPorts, this.input);
    }

    public static byte[] constructPacket(final byte filler1, final byte filler2, final int numPorts, final byte[] input) {
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtInput.PACKET_LENGTH, ArtNetOpCodes.OP_CODE_INPUT);
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH] = filler1;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 1] = filler2;
        set2BytesHighToLow(numPorts, pData, _VersionedArtNetPacket.FULL_HEADER_LENGTH + 2);
        copyBytesToArray(input, pData, _VersionedArtNetPacket.FULL_HEADER_LENGTH + 4);
        return pData;
    }

    public ArtInput(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_INPUT) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtInput.PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtInput.PACKET_LENGTH + " bytes");
        }
        this.filler1 = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH];
        this.filler2 = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 1];
        this.numPorts = get2BytesHighToLow(pData, _VersionedArtNetPacket.FULL_HEADER_LENGTH + 2);
        this.input = copyBytesFromArray(pData, _VersionedArtNetPacket.FULL_HEADER_LENGTH + 4);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtInput)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtInput artInput = (ArtInput) o;

        if (this.numPorts != artInput.numPorts) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (!Arrays.equals(this.input, artInput.input)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.numPorts;
        result = (31 * result) + ((this.input != null) ? Arrays.hashCode(this.input) : 0);
        return result;
    }
}
