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

package tc.vom.artNetLighter.infrastructure;

import tc.vom.artNetLighter.infrastructure.packets._ArtNetPacket;

import java.util.Arrays;

/**
 * Some Art-Net helper functions; mostly to ease binary computation in Java.
 */
public class ArtNetToolkit {

    /**
     * Max 7 Bit Net
     */
    public static final int MAX_NET = 0x7F;
    /**
     * Max 4 Bit Sub-Net
     */
    public static final int MAX_SUB_NET = 0xF;
    /**
     * Max 4 Bit Universe
     */
    public static final int MAX_UNIVERSE = 0xF;
    /**
     * Max 16 Bit Port Address
     */
    public static final int MAX_PORT_ADDRESS = 0x7FFF;

    private ArtNetToolkit() {
    }

    /**
     * Extract Net from Port Address
     *
     * @param portAddress Port Address
     * @return Net
     */
    public static int getNet(final int portAddress) {
        return (portAddress & 0x7F00) >> 8;
    }


    /**
     * Alter Net of an existing 16 Bit Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @param newNet      7 Bit Net
     * @return 15 Bit Port Address
     */
    public static int setNet(int portAddress, final int newNet) {
        if ((newNet > ArtNetToolkit.MAX_NET) || (newNet < 0)) {
            throw new IllegalArgumentException("Net must be in range [0," + ArtNetToolkit.MAX_NET + "]");
        }
        portAddress = (portAddress & 0x00FF) | (newNet << 8);
        return portAddress;
    }

    /**
     * Extract Sub-Net from Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @return 4 Bit Sub-Net
     */
    public static int getSubNet(final int portAddress) {
        return (portAddress & 0x00F0) >> 4;
    }


    /**
     * Alter Sub-Net of an existing 16 Bit Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @param newSubNet   4 Bit Sub-Net
     * @return 15 Bit Port Address
     */
    public static int setSubNet(int portAddress, final int newSubNet) {
        if ((newSubNet > ArtNetToolkit.MAX_SUB_NET) || (newSubNet < 0)) {
            throw new IllegalArgumentException("Sub-Net must be in range [0," + ArtNetToolkit.MAX_SUB_NET + "]");
        }
        portAddress = (portAddress & 0x7F0F) | (newSubNet << 4);
        return portAddress;
    }

    /**
     * Extract Universe from Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @return 4 Bit Universe
     */
    public static int getUniverse(final int portAddress) {
        return (portAddress & 0x000F);
    }

    /**
     * Alter Universe of an existing 16 Bit Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @param newUniverse 4 Bit Universe
     * @return 15 Bit Port Address
     */
    public static int setUniverse(int portAddress, final int newUniverse) {
        if ((newUniverse > ArtNetToolkit.MAX_UNIVERSE) || (newUniverse < 0)) {
            throw new IllegalArgumentException("Universe must be in range [0," + ArtNetToolkit.MAX_UNIVERSE + "]");
        }
        portAddress = (portAddress & 0x7FF0) | newUniverse;
        return portAddress;
    }

    /**
     * Construct 16 Bit Port Address from Net, Sub-Net and Universe
     *
     * @param net      7 Bit Net
     * @param subNet   4 Bit Sub-Net
     * @param universe 4 Bit Universe
     * @return 16 Bit Port Address
     */
    public static int getPortId(final int net, final int subNet, final int universe) {
        return ArtNetToolkit.setNet(ArtNetToolkit.setSubNet(ArtNetToolkit.setUniverse(0, universe), subNet), net);
    }

    public static boolean isBitSet(final int input, final int bit) {
        assert bit >= 0;
        assert bit <= 7;
        return (input & (1 << bit)) == 1;
    }

    public static int setBit(final int input, final int bit) {
        assert bit >= 0;
        assert bit <= 7;
        return (input | (1 << bit));
    }

    public static int toggleBit(final int input, final int bit) {
        assert bit >= 0;
        assert bit <= 7;
        return (input ^ (1 << bit));
    }

    public static int unsetBit(final int input, final int bit) {
        assert bit >= 0;
        assert bit <= 7;
        return (input & (~(1 << bit)));
    }

    public static final int[][] BIT_MASKS = {
            // From Bit 0
            {0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff},
            // From Bit 1
            {0x00, 0x02, 0x06, 0x0e, 0x1e, 0x3e, 0x7e, 0xfe},
            // From Bit 2
            {0x00, 0x00, 0x04, 0x0c, 0x1c, 0x3c, 0x7c, 0xfc},
            // From Bit 3
            {0x00, 0x00, 0x00, 0x08, 0x18, 0x38, 0x78, 0xf8},
            // From Bit 4
            {0x00, 0x00, 0x00, 0x00, 0x10, 0x30, 0x70, 0xf0},
            // From Bit 5
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x20, 0x60, 0xe0},
            // From Bit 6
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 0xc0},
            // From Bit 7
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x80}
    };

    public static int getBits(final int input, final int lower, final int upper) {
        return input & ArtNetToolkit.generateBitRangeMask(lower, upper);
    }


    public static int setBits(final int input, final int lower, final int upper) {
        final int mask = ArtNetToolkit.generateBitRangeMask(lower, upper);
        return input | mask;
    }


    public static int setBits(final int input, final int bits, final int lower, final int upper) {
        final int mask = ArtNetToolkit.generateBitRangeMask(lower, upper);
        return (input & (~mask)) | (bits & mask);
    }

    public static int unsetBits(final int input, final int lower, final int upper) {
        final int mask = ArtNetToolkit.generateBitRangeMask(lower, upper);
        return input & (~mask);
    }

    public static int toggleBits(final int input, final int lower, final int upper) {
        final int mask = ArtNetToolkit.generateBitRangeMask(lower, upper);
        return input ^ mask;
    }

    public static int generateBitRangeMask(int lower, int upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Upper boundary has to be greater or equal to lower boundary");
        } else if ((lower < 8) && (upper < 8)) {
            return ArtNetToolkit.BIT_MASKS[lower][upper];
        } else {
            int mask = 0;
            int shift = 0;
            while (upper >= 0) {
                if (lower < 8) {
                    if (upper < 8) {
                        mask |= (ArtNetToolkit.BIT_MASKS[lower][upper] << shift);
                    } else {
                        mask |= (ArtNetToolkit.BIT_MASKS[lower][7] << shift);
                    }
                }
                lower = Math.max(0, lower - 8);
                upper -= 8;
                shift += 8;
            }
            return mask;
        }
    }

    public static void copyBytesToArray(final byte[] from, final byte[] to, final int offset) {
        if (from.length != 0) {
            System.arraycopy(from, 0, to, offset, from.length);
        }
    }

    public static void copyStringToArray(final String from, final byte[] to, final int offset, final int maxLength) {
        final byte[] strBytes = from.getBytes(_ArtNetPacket.STRING_CHARSET);
        if (strBytes.length > maxLength) {
            throw new IllegalArgumentException("String too long");
        }
        ArtNetToolkit.copyBytesToArray(strBytes, to, offset);
        Arrays.fill(to, offset + strBytes.length, offset + maxLength, (byte) 0);
    }

    public static byte[] copyBytesFromArray(final byte[] from, final int offset, final int length) {
        //return Arrays.copyOfRange(from, offset, offset + length);
        assert offset >= 0;
        assert length <= (from.length - offset);
        final byte[] result = new byte[length];
        System.arraycopy(from, offset, result, 0, length);
        return result;
    }

    public static byte[] copyBytesFromArray(final byte[] from, final int offset) {
        //return Arrays.copyOfRange(from, offset, offset + length);
        assert offset >= 0;
        if (offset == from.length) {
            return new byte[0];
        }
        return ArtNetToolkit.copyBytesFromArray(from, offset, from.length - offset);
    }

    public static String copyStringFromArray(final byte[] from, final int offset, final int length) {
        final String shortName = new String(from, offset, length, _ArtNetPacket.STRING_CHARSET);
        final int nullTerminator = shortName.indexOf(0);
        if (nullTerminator != -1) {
            return shortName.substring(0, nullTerminator);
        }
        return shortName;
    }

    public static int get4BytesHighToLow(final byte[] from, final int offset) {
        assert from.length > (offset + 3);
        return ((from[offset] & 0xff) << 24) | ((from[offset + 1] & 0xff) << 16) | ((from[offset + 2] & 0xff) << 8) | (from[offset + 3] & 0xff);
    }

    public static int get4BytesLowToHigh(final byte[] from, final int offset) {
        assert from.length > (offset + 3);
        return ((from[offset + 3] & 0xff) << 24) | ((from[offset + 2] & 0xff) << 16) | ((from[offset + 1] & 0xff) << 8) | (from[offset] & 0xff);
    }

    public static int get2BytesHighToLow(final byte[] from, final int offset) {
        assert from.length > (offset + 1);
        return ((from[offset] & 0xff) << 8) | (from[offset + 1] & 0xff);
    }

    public static int get2BytesLowToHigh(final byte[] from, final int offset) {
        assert from.length > (offset + 1);
        return ((from[offset + 1] & 0xff) << 8) | (from[offset] & 0xff);
    }

    public static void set4BytesHighToLow(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 3);
        to[offset] = (byte) ((from >> 24) & 0xff);
        to[offset + 1] = (byte) ((from >> 16) & 0xff);
        to[offset + 2] = (byte) ((from >> 8) & 0xff);
        to[offset + 3] = (byte) (from & 0xff);
    }

    public static void set4BytesLowToHigh(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 3);
        to[offset + 3] = (byte) ((from >> 24) & 0xff);
        to[offset + 2] = (byte) ((from >> 16) & 0xff);
        to[offset + 1] = (byte) ((from >> 8) & 0xff);
        to[offset] = (byte) (from & 0xff);
    }

    public static void set2BytesHighToLow(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 1);
        to[offset] = (byte) ((from >> 8) & 0xff);
        to[offset + 1] = (byte) (from & 0xff);
    }

    public static void set2BytesLowToHigh(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 1);
        to[offset + 1] = (byte) ((from >> 8) & 0xff);
        to[offset] = (byte) (from & 0xff);
    }
}
