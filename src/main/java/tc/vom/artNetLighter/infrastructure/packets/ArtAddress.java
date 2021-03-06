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

import java.util.Arrays;

/**
 * Implements ArtAdress packet.
 */
public class ArtAddress extends _VersionedArtNetPacket {

    public static final byte FILLER = 0;

    private static final int START_NET = _VersionedArtNetPacket.FULL_HEADER_LENGTH;
    private static final int START_FILLER = ArtAddress.START_NET + 1;
    private static final int START_SHORT_NAME = ArtAddress.START_FILLER + 1;
    private static final int START_LONG_NAME = ArtAddress.START_SHORT_NAME + _ArtNetPacket.SHORT_NAME_LENGTH;
    private static final int START_UNIVERSE_IN = ArtAddress.START_LONG_NAME + _ArtNetPacket.LONG_NAME_LENGTH;
    private static final int START_UNIVERSE_OUT = ArtAddress.START_UNIVERSE_IN + 4;
    private static final int START_SUB_NET = ArtAddress.START_UNIVERSE_OUT + 4;
    private static final int START_VIDEO = ArtAddress.START_SUB_NET + 1;
    private static final int START_COMMAND = ArtAddress.START_VIDEO + 1;

    public static final int PACKET_LENGTH = ArtAddress.START_COMMAND + 1;

    public static interface CommandConstants {

        /**
         * No Action
         */
        public final byte AcNone = 0x00;
        /**
         * The next ArtDmx packet cancels Node's merge mode
         */
        public final byte AcCancelMerge = 0x01;
        /**
         * Node front panel indicators operate normally
         */
        public final byte AcLedNormal = 0x02;
        /**
         * Node front panel indicators are muted
         */
        public final byte AcLedMute = 0x03;
        /**
         * Fast flash all indicators for locate
         */
        public final byte AcLedLocate = 0x04;
        /**
         * Reset the receive DMX flags for errors, SI's, Text & Test packets
         */
        public final byte AcResetRxFlags = 0x05;

        /**
         * Set Port 0 to merge in LTP.
         */
        public final byte AcMergeLtp0 = 0x10;
        /**
         * Set Port 1 to merge in LTP.
         */
        public final byte AcMergeLtp1 = 0x11;
        /**
         * Set Port 2 to merge in LTP.
         */
        public final byte AcMergeLtp2 = 0x12;
        /**
         * Set Port 3 to merge in LTP.
         */
        public final byte AcMergeLtp3 = 0x13;

        /**
         * Set Port 0 to merge in HTP. (Default Mode)
         */
        public final byte AcMergeHtp0 = 0x50;
        /**
         * Set Port 1 to merge in HTP. (Default Mode)
         */
        public final byte AcMergeHtp1 = 0x51;
        /**
         * Set Port 2 to merge in HTP. (Default Mode)
         */
        public final byte AcMergeHtp2 = 0x52;
        /**
         * Set Port 3 to merge in HTP. (Default Mode)
         */
        public final byte AcMergeHtp3 = 0x53;

        /**
         * Clear all data buffers associated with output port 0
         */
        public final byte AcClearOp0 = (byte) 0x90;
        /**
         * Clear all data buffers associated with output port 1
         */
        public final byte AcClearOp1 = (byte) 0x91;
        /**
         * Clear all data buffers associated with output port 2
         */
        public final byte AcClearOp2 = (byte) 0x92;
        /**
         * Clear all data buffers associated with output port 3
         */
        public final byte AcClearOp3 = (byte) 0x93;
    }

    /**
     * 1 Byte NetSwitch:
     * - Bits 14-8 of the 15 bit Port-Address are encoded into the bottom 7 bits of this field. This is used in combination with SubSwitch and SwIn[] or Sw”ut[] to produce the full universe address.
     * - This value is ignored unless bit 7 is high. i.e. to program a value 0x07, send the value as 0x87.
     * - Send 0x00 to reset this value to the physical switch setting.
     * - Use value 0x7f for no change.
     */
    private final byte net;

    /**
     * 1 Byte Filler
     */
    private final byte filler;

    /**
     * 18 Bytes Short Name.
     * The array represents a null terminated short name for the Node. The Controller uses the ArtAddress packet to program this string. Max length is 17 characters plus the null. The Node will ignore this value if the string is null.
     * This is a fixed length field, although the string it contains can be shorter than the field.
     */
    private final String shortName;

    /**
     * 64 Bytes Long Name.
     * The array represents a null terminated long name for the Node. The Controller uses the ArtAddress packet to program this string. Max length is 17 characters plus the null. The Node will ignore this value if the string is null.
     * This is a fixed length field, although the string it contains can be shorter than the field.
     */
    private final String longName;

    /**
     * 4* 4 Bit Universe of each input channel.
     * This value is ignored unless bit 7 is high. i.e. to program a value 0x07, send the value as 0x87.
     * Send 0x00 to reset this value to the physical switch setting.
     * Use value 0x7f for no change.
     */
    private final byte[] universesIn;

    /**
     * 4* 4 Bit Universe of each output channel.
     * This value is ignored unless bit 7 is high. i.e. to program a value 0x07, send the value as 0x87.
     * Send 0x00 to reset this value to the physical switch setting.
     * Use value 0x7f for no change.
     */
    private final byte[] universesOut;

    /**
     * 4 Bit Sub Switch.
     * This value is ignored unless bit 7 is high. i.e. to program a value 0x07, send the value as 0x87.
     * Send 0x00 to reset this value to the physical switch setting.
     * Use value 0x7f for no change.
     */
    private final byte subNet;

    /**
     * 1 Byte Video.
     * Reserved.
     *
     * @see ArtPollReply#video
     */
    private final byte video;

    /**
     * 1 Byte Command.
     * Node configuration commands.
     *
     * @see CommandConstants
     */
    private final byte command;

    public ArtAddress(final byte net, final String shortName, final String longName, final byte[] universesIn, final byte[] universesOut, final byte subNet, final byte video, final byte command) {
        this(net, ArtAddress.FILLER, shortName, longName, universesIn, universesOut, subNet, video, command);

    }

    private ArtAddress(final byte net, final byte filler, final String shortName, final String longName, final byte[] universesIn, final byte[] universesOut, final byte subNet, final byte video, final byte command) {
        super(_VersionedArtNetPacket.OP_CODE_ADDRESS);
        this.net = net;
        this.filler = filler;
        this.shortName = shortName;
        this.longName = longName;
        this.universesIn = universesIn;
        this.universesOut = universesOut;
        this.subNet = subNet;
        this.video = video;
        this.command = command;
    }

    public byte getNet() {
        return this.net;
    }

    public byte getFiller() {
        return this.filler;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getLongName() {
        return this.longName;
    }

    public byte[] getUniversesIn() {
        return this.universesIn;
    }

    public byte[] getUniversesOut() {
        return this.universesOut;
    }

    public byte getSubNet() {
        return this.subNet;
    }

    public byte getVideo() {
        return this.video;
    }

    public byte getCommand() {
        return this.command;
    }

    @Override
    public byte[] constructPacket() {
        return ArtAddress.constructPacket(this.net, this.filler, this.shortName, this.longName, this.universesIn, this.universesOut, this.subNet, this.video, this.command);
    }

    public static byte[] constructPacket(final byte net, final byte filler, String shortName, String longName, final byte[] universesIn, final byte[] universesOut, final byte subNet, final byte video, final byte command) {
        if (shortName == null) {
            shortName = "";
        }
        final byte[] shortNameBytes = shortName.getBytes(_ArtNetPacket.STRING_CHARSET);
        if (longName == null) {
            longName = "";
        }
        final byte[] longNameBytes = longName.getBytes(_ArtNetPacket.STRING_CHARSET);
        return ArtAddress.constructPacket(net, filler, shortNameBytes, longNameBytes, universesIn, universesOut, subNet, video, command);
    }

    public static byte[] constructPacket(final byte net, final byte filler, final byte[] shortName, final byte[] longName, final byte[] universesIn, final byte[] universesOut, final byte subNet, final byte video, final byte command) {
        if ((shortName.length >= _ArtNetPacket.SHORT_NAME_LENGTH) && ((shortName.length != _ArtNetPacket.SHORT_NAME_LENGTH) || (shortName[_ArtNetPacket.SHORT_NAME_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Short Name has a maximum length of " + (_ArtNetPacket.SHORT_NAME_LENGTH - 1) + " Bytes.");
        }
        if ((longName.length >= _ArtNetPacket.LONG_NAME_LENGTH) && ((longName.length != _ArtNetPacket.LONG_NAME_LENGTH) || (longName[_ArtNetPacket.LONG_NAME_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Long Name has a maximum length of " + (_ArtNetPacket.LONG_NAME_LENGTH - 1) + " Bytes.");
        }
        if (universesIn.length > 4) {
            throw new IllegalArgumentException("Maximum universesIn.length is 4");
        }
        if (universesOut.length > 4) {
            throw new IllegalArgumentException("Maximum universesOut.length is 4");
        }
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtAddress.PACKET_LENGTH, ArtNetOpCodes.OP_CODE_ADDRESS);
        pData[ArtAddress.START_NET] = net;
        pData[ArtAddress.START_FILLER] = filler;
        ByteArrayToolkit.setBytes(shortName, pData, ArtAddress.START_SHORT_NAME);
        ByteArrayToolkit.setBytes(longName, pData, ArtAddress.START_LONG_NAME);
        ByteArrayToolkit.setBytes(universesIn, pData, ArtAddress.START_UNIVERSE_IN);
        ByteArrayToolkit.setBytes(universesOut, pData, ArtAddress.START_UNIVERSE_OUT);
        pData[ArtAddress.START_SUB_NET] = subNet;
        pData[ArtAddress.START_VIDEO] = video;
        pData[ArtAddress.START_COMMAND] = command;
        return pData;
    }

    public ArtAddress(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_ADDRESS) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtAddress.PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtAddress.PACKET_LENGTH + " bytes");
        }
        this.net = pData[ArtAddress.START_NET];
        this.filler = pData[ArtAddress.START_FILLER];
        this.shortName = ByteArrayToolkit.getString(pData, ArtAddress.START_SHORT_NAME, _ArtNetPacket.SHORT_NAME_LENGTH);
        this.longName = ByteArrayToolkit.getString(pData, ArtAddress.START_LONG_NAME, _ArtNetPacket.LONG_NAME_LENGTH);
        this.universesIn = ByteArrayToolkit.getBytes(pData, ArtAddress.START_UNIVERSE_IN, 4);
        this.universesOut = ByteArrayToolkit.getBytes(pData, ArtAddress.START_UNIVERSE_OUT);
        this.subNet = pData[ArtAddress.START_SUB_NET];
        this.video = pData[ArtAddress.START_VIDEO];
        this.command = pData[ArtAddress.START_COMMAND];
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtAddress)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtAddress that = (ArtAddress) o;

        if (this.command != that.command) {
            return false;
        }
        if (this.net != that.net) {
            return false;
        }
        if (this.subNet != that.subNet) {
            return false;
        }
        if (this.video != that.video) {
            return false;
        }
        if ((this.longName != null) ? !this.longName.equals(that.longName) : (that.longName != null)) {
            return false;
        }
        if ((this.shortName != null) ? !this.shortName.equals(that.shortName) : (that.shortName != null)) {
            return false;
        }
        if (!Arrays.equals(this.universesIn, that.universesIn)) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (!Arrays.equals(this.universesOut, that.universesOut)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.net;
        result = (31 * result) + ((this.shortName != null) ? this.shortName.hashCode() : 0);
        result = (31 * result) + ((this.longName != null) ? this.longName.hashCode() : 0);
        result = (31 * result) + ((this.universesIn != null) ? Arrays.hashCode(this.universesIn) : 0);
        result = (31 * result) + ((this.universesOut != null) ? Arrays.hashCode(this.universesOut) : 0);
        result = (31 * result) + this.subNet;
        result = (31 * result) + this.video;
        result = (31 * result) + this.command;
        return result;
    }
}
