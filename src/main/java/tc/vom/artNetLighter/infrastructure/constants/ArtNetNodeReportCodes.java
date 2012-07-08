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

package tc.vom.artNetLighter.infrastructure.constants;

/**
 * Contains the Node Report Code for {@link tc.vom.artNetLighter.infrastructure.packets.ArtPollReply}
 */
@SuppressWarnings("UnusedDeclaration")
public interface ArtNetNodeReportCodes {
    /**
     * Booted in debug mode (Only used in development)
     */
    int NODE_REPORT_DEBUG = 0x0000;
    /**
     * Power On Tests successful
     */
    int NODE_REPORT_POWER_OK = 0x0001;
    /**
     * Hardware tests failed at Power On
     */
    int NODE_REPORT_POWER_FAIL = 0x0002;
    /**
     * Last UDP from Node failed due to truncated length,  Most likely caused by a collision.
     */
    int NODE_REPORT_SOCKET_WR1 = 0x0003;
    /**
     * Unable to identify last UDP transmission. Check OpCode and packet length.
     */
    int NODE_REPORT_PARSE_FAIL = 0x0004;
    /**
     * Unable to open Udp Socket in last transmission attempt
     */
    int NODE_REPORT_UDP_FAIL = 0x0005;
    /**
     * Confirms that Short Name programming via ArtAddress, was successful.
     */
    int NODE_REPORT_SH_NAME_OK = 0x0006;
    /**
     * Confirms that Long Name programming via ArtAddress, was successful.
     */
    int NODE_REPORT_LO_NAME_OK = 0x0007;
    /**
     * DMX512 receive errors detected.
     */
    int NODE_REPORT_DMX_ERROR = 0x0008;
    /**
     * Ran out of internal DMX transmit buffers.
     */
    int NODE_REPORT_DMX_UDP_FULL = 0x0009;
    /**
     * Ran out of internal DMX Rx buffers.
     */
    int NODE_REPORT_DMX_RX_FULL = 0x000a;
    /**
     * Rx Universe switches conflict.
     */
    int NODE_REPORT_SWITCH_ERR = 0x000b;
    /**
     * Product configuration does not match firmware.
     */
    int NODE_REPORT_CONFIG_ERR = 0x000c;
    /**
     * DMX output short detected. See GoodOutput field.
     */
    int NODE_REPORT_DMX_SHORT = 0x000d;
    /**
     * Last attempt to upload new firmware failed.
     */
    int NODE_REPORT_FIRMWARE_FAIL = 0x000e;
    /**
     * User changed switch settings when address locked by remote programming. User changes ignored.
     */
    int NODE_REPORT_USER_FAIL = 0x000f;
}
