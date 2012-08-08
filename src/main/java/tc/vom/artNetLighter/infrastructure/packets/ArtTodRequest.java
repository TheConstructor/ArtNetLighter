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
import tc.vom.artNetLighter.infrastructure.ByteArrayToolkit;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

/**
 * Implements ArtTodRequest packet.
 */
public class ArtTodRequest extends _VersionedArtNetPacket {

    public static final int SPARE_LENGTH = 7;
    public static final byte[] SPARE_BYTES = new byte[ArtTodRequest.SPARE_LENGTH];
    private static final int MAXIMUM_ADDRESS_LENGTH = 32;

    private static final int START_FILLER1 = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_FILLER2 = ArtTodRequest.START_FILLER1 + 1;
    private static final int START_SPARE = ArtTodRequest.START_FILLER2 + 1;
    private static final int START_NET = ArtTodRequest.START_SPARE + ArtTodRequest.SPARE_LENGTH;
    private static final int START_COMMAND = ArtTodRequest.START_NET + 1;
    private static final int START_ADD_COUNT = ArtTodRequest.START_COMMAND + 1;
    private static final int START_ADDRESS = ArtTodRequest.START_ADD_COUNT + 1;

    public static final int MINIMUM_PACKET_LENGTH = ArtTodRequest.START_ADDRESS;

    public static interface CommandConstants {
        /**
         * Send the entire TOD.
         */
        public static final byte TodFull = 0x00;
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
     * 7 Byte Spare.
     */
    private final byte[] spare;

    /**
     * 1 Byte Net.
     */
    private final byte net;

    /**
     * 1 Byte Command.
     */
    private final byte command;

    /**
     * 1 Byte AddCount.
     * The array size of the Address field. Max value is 32.
     */
    private final byte addCount;

    /**
     * {@link #addCount} Byte Address.
     * This array defines the low byte of the Port-Address of the Output Gateway nodes that must respond to this packet. The high nibble is the Sub-Net switch. The low nibble corresponds to the Universe. This is combined with the 'Net' field above to form the 15 bit address.
     */
    private final byte[] address;

    public ArtTodRequest(final int portAddress, final byte command) {
        this(ArtNetToolkit.getNet(portAddress), command, new byte[]{(byte) portAddress});
    }

    public ArtTodRequest(final byte net, final byte command, final byte[] address) {
        this((byte) 0, (byte) 0, ArtTodRequest.SPARE_BYTES, net, command, address);
    }

    private ArtTodRequest(final byte filler1, final byte filler2, final byte[] spare, final byte net, final byte command, final byte[] address) {
        super(ArtNetOpCodes.OP_CODE_TOD_REQUEST);
        this.filler1 = filler1;
        this.filler2 = filler2;
        this.spare = spare;
        this.net = net;
        this.command = command;
        this.addCount = (byte) address.length;
        this.address = address;
    }

    public byte getFiller1() {
        return this.filler1;
    }

    public byte getFiller2() {
        return this.filler2;
    }

    public byte[] getSpare() {
        return this.spare;
    }

    public byte getNet() {
        return this.net;
    }

    public byte getCommand() {
        return this.command;
    }

    public byte getAddCount() {
        return this.addCount;
    }

    public byte[] getAddress() {
        return this.address;
    }

    @Override
    public byte[] constructPacket() {
        return ArtTodRequest.constructPacket(this.filler1, this.filler2, this.spare, this.net, this.command, this.address);
    }

    public static byte[] constructPacket(final byte filler1, final byte filler2, final byte[] spare, final byte net, final byte command, final byte[] address) {
        if (spare.length > ArtTodRequest.SPARE_LENGTH) {
            throw new IllegalArgumentException("spare has a maximum length of " + ArtTodRequest.SPARE_LENGTH + " Bytes.");
        }
        if (address.length > ArtTodRequest.MAXIMUM_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("address has a maximum length of " + ArtTodRequest.MAXIMUM_ADDRESS_LENGTH + " Bytes.");
        }
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtTodRequest.MINIMUM_PACKET_LENGTH + address.length, ArtNetOpCodes.OP_CODE_TOD_REQUEST);
        pData[ArtTodRequest.START_FILLER1] = filler1;
        pData[ArtTodRequest.START_FILLER2] = filler2;
        ByteArrayToolkit.setBytes(spare, pData, ArtTodRequest.START_SPARE);
        pData[ArtTodRequest.START_NET] = net;
        pData[ArtTodRequest.START_COMMAND] = command;
        pData[ArtTodRequest.START_ADD_COUNT] = (byte) address.length;
        ByteArrayToolkit.setBytes(address, pData, ArtTodRequest.START_ADDRESS);
        return pData;
    }

    public ArtTodRequest(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_TOD_REQUEST) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtTodRequest.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtTodRequest.MINIMUM_PACKET_LENGTH + " bytes");
        }
        this.filler1 = pData[ArtTodRequest.START_FILLER1];
        this.filler2 = pData[ArtTodRequest.START_FILLER2];
        this.spare = ByteArrayToolkit.getBytes(pData, ArtTodRequest.START_SPARE, ArtTodRequest.SPARE_LENGTH);
        this.net = pData[ArtTodRequest.START_NET];
        this.command = pData[ArtTodRequest.START_COMMAND];
        this.addCount = pData[ArtTodRequest.START_ADD_COUNT];
        this.address = ByteArrayToolkit.getBytes(pData, ArtTodRequest.START_ADDRESS);
    }
}
