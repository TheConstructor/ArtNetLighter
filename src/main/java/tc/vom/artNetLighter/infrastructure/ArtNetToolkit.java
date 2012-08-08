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
     * Max 15 Bit Port Address
     */
    public static final int MAX_PORT_ADDRESS = 0x7FFF;

    /**
     * Max 8 Bit Address
     */
    public static final int MAX_ADDRESS = 0xff;

    private static final int NET_MASK = 0x7F00;
    private static final int ADDRESS_MASK = 0xFF;
    private static final int SUB_NET_MASK = 0x00F0;
    private static final int UNIVERSE_MASK = 0x000F;

    private ArtNetToolkit() {
    }

    /**
     * Extract Net from Port Address
     *
     * @param portAddress Port Address
     * @return Net
     */
    public static byte getNet(final int portAddress) {
        return (byte) ((portAddress & ArtNetToolkit.NET_MASK) >> ByteArrayToolkit.SHIFT_SECOND_BYTE);
    }


    /**
     * Alter Net of an existing 15 Bit Port Address
     *
     * @param portAddress 15 Bit Port Address
     * @param newNet      7 Bit Net
     * @return 15 Bit Port Address
     */
    public static int setNet(final int portAddress, final int newNet) {
        if ((newNet > ArtNetToolkit.MAX_NET) || (newNet < 0)) {
            throw new IllegalArgumentException("Net must be in range [0," + ArtNetToolkit.MAX_NET + "]");
        }
        return (portAddress & (~ArtNetToolkit.NET_MASK)) | (newNet << 8);
    }

    /**
     * Extract lower half of Port Address ("Address" in ArtTodRequest).
     * Negative return-values need to be reinterpreted as positiv values.
     *
     * @param portAddress Port Address
     * @return Address
     */
    public static byte getAddress(final int portAddress) {
        return (byte) (portAddress & ArtNetToolkit.ADDRESS_MASK);
    }

    /**
     * Alter lower half of an existing 16 Bit Port Address ("Address" in ArtTodRequest
     *
     * @param portAddress 15 Bit Port Address
     * @param newAddress  8 Bit Address
     * @return 15 Bit Port Address
     */
    public static int setAddress(final int portAddress, final byte newAddress) {
        return ArtNetToolkit.setAddress(portAddress, newAddress & ArtNetToolkit.ADDRESS_MASK);
    }


    /**
     * Alter lower half of an existing 16 Bit Port Address ("Address" in ArtTodRequest
     *
     * @param portAddress 15 Bit Port Address
     * @param newAddress  8 Bit Address
     * @return 15 Bit Port Address
     */
    public static int setAddress(final int portAddress, final int newAddress) {
        if ((newAddress > ArtNetToolkit.MAX_ADDRESS) || (newAddress < 0)) {
            throw new IllegalArgumentException("Sub-Net must be in range [0," + ArtNetToolkit.MAX_SUB_NET + "]");
        }
        return (portAddress & (~ArtNetToolkit.ADDRESS_MASK)) | newAddress;
    }

    /**
     * Extract Sub-Net from Port Address
     *
     * @param portAddress 15 Bit Port Address
     * @return 4 Bit Sub-Net
     */
    public static byte getSubNet(final int portAddress) {
        return (byte) ((portAddress & ArtNetToolkit.SUB_NET_MASK) >> 4);
    }


    /**
     * Alter Sub-Net of an existing 15 Bit Port Address
     *
     * @param portAddress 15 Bit Port Address
     * @param newSubNet   4 Bit Sub-Net
     * @return 15 Bit Port Address
     */
    public static int setSubNet(final int portAddress, final int newSubNet) {
        if ((newSubNet > ArtNetToolkit.MAX_SUB_NET) || (newSubNet < 0)) {
            throw new IllegalArgumentException("Sub-Net must be in range [0," + ArtNetToolkit.MAX_SUB_NET + "]");
        }
        return (portAddress & (~ArtNetToolkit.SUB_NET_MASK)) | (newSubNet << 4);
    }

    /**
     * Extract Universe from Port Address
     *
     * @param portAddress 15 Bit Port Address
     * @return 4 Bit Universe
     */
    public static byte getUniverse(final int portAddress) {
        return (byte) (portAddress & ArtNetToolkit.UNIVERSE_MASK);
    }

    /**
     * Alter Universe of an existing 15 Bit Port Address
     *
     * @param portAddress 15 Bit Port Address
     * @param newUniverse 4 Bit Universe
     * @return 15 Bit Port Address
     */
    public static int setUniverse(final int portAddress, final int newUniverse) {
        if ((newUniverse > ArtNetToolkit.MAX_UNIVERSE) || (newUniverse < 0)) {
            throw new IllegalArgumentException("Universe must be in range [0," + ArtNetToolkit.MAX_UNIVERSE + "]");
        }
        return (portAddress & (~ArtNetToolkit.UNIVERSE_MASK)) | newUniverse;
    }

    /**
     * Construct 15 Bit Port Address from Net, Sub-Net and Universe
     *
     * @param net      7 Bit Net
     * @param subNet   4 Bit Sub-Net
     * @param universe 4 Bit Universe
     * @return 15 Bit Port Address
     */
    public static int getPortId(final int net, final int subNet, final int universe) {
        return ArtNetToolkit.setNet(ArtNetToolkit.setSubNet(ArtNetToolkit.setUniverse(0, universe), subNet), net);
    }

    /**
     * Construct 15 Bit Port Address from Net and Address
     *
     * @param net     7 Bit Net
     * @param address 8 Bit Address
     * @return 15 Bit Port Address
     */
    public static int getPortId(final int net, final int address) {
        return ArtNetToolkit.setNet(ArtNetToolkit.setAddress(0, address), net);
    }

    /**
     * Construct 15 Bit Port Address from Net and Address
     *
     * @param net     7 Bit Net
     * @param address 8 Bit Address
     * @return 15 Bit Port Address
     */
    public static int getPortId(final int net, final byte address) {
        return ArtNetToolkit.setNet(ArtNetToolkit.setAddress(0, address), net);
    }
}
