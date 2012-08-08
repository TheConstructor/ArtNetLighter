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
}
