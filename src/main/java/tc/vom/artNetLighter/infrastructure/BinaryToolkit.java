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

/**
 * Some functions to ease bitwise-Operations
 */
public class BinaryToolkit {
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

    /**
     * Binary AND (&amp;) with this to get the lowest byte of a value
     */
    public static final int FIRST_BYTE_MASK = 0xff;
    /**
     * Left shift (&lt;&lt;) to get first byte to fourth postion, right shift (&gt;&gt;) to get fourth byte to first position.
     */
    public static final int SHIFT_FOURTH_BYTE = 24;
    /**
     * Left shift (&lt;&lt;) to get first byte to third postion, right shift (&gt;&gt;) to get third byte to first position.
     */
    public static final int SHIFT_THIRD_BYTE = 16;
    /**
     * Left shift (&lt;&lt;) to get first byte to second postion, right shift (&gt;&gt;) to get second byte to first position.
     */
    public static final int SHIFT_SECOND_BYTE = 8;

    public static int getUnsignedValue(final byte input) {
        return input & BinaryToolkit.FIRST_BYTE_MASK;
    }

    public static boolean isBitSet(final int input, final int bit) {
        assert bit >= 0;
        assert bit <= 7;
        return (input & (1 << bit)) != 0;
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

    public static int getBits(final int input, final int lower, final int upper) {
        return input & BinaryToolkit.generateBitRangeMask(lower, upper);
    }

    public static int setBits(final int input, final int lower, final int upper) {
        final int mask = BinaryToolkit.generateBitRangeMask(lower, upper);
        return input | mask;
    }

    /*
    Untested - should work, but is currently not needed.
    public static int setBits(final int input, final int bits, final int lower, final int upper) {
        final int mask = BinaryToolkit.generateBitRangeMask(lower, upper);
        return (input & (~mask)) | (bits & mask);
    }
    */

    public static int unsetBits(final int input, final int lower, final int upper) {
        final int mask = BinaryToolkit.generateBitRangeMask(lower, upper);
        return input & (~mask);
    }

    public static int toggleBits(final int input, final int lower, final int upper) {
        final int mask = BinaryToolkit.generateBitRangeMask(lower, upper);
        return input ^ mask;
    }

    public static int generateBitRangeMask(int lower, int upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Upper boundary has to be greater or equal to lower boundary");
        } else if ((lower < 8) && (upper < 8)) {
            return BinaryToolkit.BIT_MASKS[lower][upper];
        } else {
            int mask = 0;
            int shift = 0;
            while (upper >= 0) {
                if (lower < 8) {
                    if (upper < 8) {
                        mask |= (BinaryToolkit.BIT_MASKS[lower][upper] << shift);
                    } else {
                        mask |= (BinaryToolkit.BIT_MASKS[lower][7] << shift);
                    }
                }
                lower = Math.max(0, lower - 8);
                upper -= 8;
                shift += 8;
            }
            return mask;
        }
    }
}
