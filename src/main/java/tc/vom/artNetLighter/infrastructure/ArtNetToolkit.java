package tc.vom.artNetLighter.infrastructure;

/**
 * Some Art-Net helper functions.
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
    public static int getNet(int portAddress) {
        return (portAddress & 0x7F00) >> 8;
    }


    /**
     * Alter Net of an existing 16 Bit Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @param newNet      7 Bit Net
     * @return 15 Bit Port Address
     */
    public static int setNet(int portAddress, int newNet) {
        if (newNet > MAX_NET || newNet < 0)
            throw new IllegalArgumentException("Net must be in range [0," + MAX_NET + "]");
        portAddress = (portAddress & 0x00FF) | (newNet << 8);
        return portAddress;
    }

    /**
     * Extract Sub-Net from Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @return 4 Bit Sub-Net
     */
    public static int getSubNet(int portAddress) {
        return (portAddress & 0x00F0) >> 4;
    }


    /**
     * Alter Sub-Net of an existing 16 Bit Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @param newSubNet   4 Bit Sub-Net
     * @return 15 Bit Port Address
     */
    public static int setSubNet(int portAddress, int newSubNet) {
        if (newSubNet > MAX_SUB_NET || newSubNet < 0)
            throw new IllegalArgumentException("Sub-Net must be in range [0," + MAX_SUB_NET + "]");
        portAddress = (portAddress & 0x7F0F) | (newSubNet << 4);
        return portAddress;
    }

    /**
     * Extract Universe from Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @return 4 Bit Universe
     */
    public static int getUniverse(int portAddress) {
        return (portAddress & 0x000F);
    }

    /**
     * Alter Universe of an existing 16 Bit Port Address
     *
     * @param portAddress 16 Bit Port Address
     * @param newUniverse 4 Bit Universe
     * @return 15 Bit Port Address
     */
    public static int setUniverse(int portAddress, int newUniverse) {
        if (newUniverse > MAX_UNIVERSE || newUniverse < 0)
            throw new IllegalArgumentException("Universe must be in range [0," + MAX_UNIVERSE + "]");
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
    public static int getPortId(int net, int subNet, int universe) {
        return setNet(setSubNet(setUniverse(0, universe), subNet), net);
    }

    public static boolean isBitSet(int input, int bit) {
        return (input & (1 << bit)) == 1;
    }

    public static int setBit(int input, int bit) {
        return (input | (1 << bit));
    }

    public static int toggleBit(int input, int bit) {
        return (input ^ (1 << bit));
    }

    public static int unsetBit(int input, int bit) {
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

    public static int getBits(int input, int lower, int upper) {
        return input & generateBitRangeMask(lower, upper);
    }


    public static int setBits(int input, int lower, int upper) {
        final int mask = generateBitRangeMask(lower, upper);
        return input | mask;
    }


    public static int setBits(int input, int bits, int lower, int upper) {
        final int mask = generateBitRangeMask(lower, upper);
        return (input & (~mask)) | (bits & mask);
    }

    public static int unsetBits(int input, int lower, int upper) {
        final int mask = generateBitRangeMask(lower, upper);
        return input & (~mask);
    }

    public static int toggleBits(int input, int lower, int upper) {
        final int mask = generateBitRangeMask(lower, upper);
        return input ^ mask;
    }

    public static int generateBitRangeMask(int lower, int upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Upper boundary has to be greater or equal to lower boundary");
        } else if (lower < 8 && upper < 8) {
            return BIT_MASKS[lower][upper];
        } else {
            int mask = 0;
            int shift = 0;
            while (upper >= 0) {
                if (lower < 8) {
                    if (upper < 8) {
                        mask |= (BIT_MASKS[lower][upper] << shift);
                    } else {
                        mask |= (BIT_MASKS[lower][7] << shift);
                    }
                }
                lower = Math.max(0, lower - 8);
                upper -= 8;
                shift += 8;
            }
            return mask;
        }
    }

    public static void copyToArray(byte[] src, byte[] dest, int offset) {
        System.arraycopy(src, 0, dest, offset, src.length);
    }

    public static <T> void copyToArray(T[] src, T[] dest, int offset) {
        System.arraycopy(src, 0, dest, offset, src.length);
    }
}
