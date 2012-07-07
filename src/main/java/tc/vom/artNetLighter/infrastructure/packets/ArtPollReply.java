package tc.vom.artNetLighter.infrastructure.packets;

/**
 * Created with IntelliJ IDEA.
 * User: matthias
 * Date: 06.07.12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class ArtPollReply extends ArtNetPacket {

    /**
     * Booted in debug mode (Only used in development)
     */
    public static final int NODE_REPORT_DEBUG = 0x0000;
    /**
     * Power On Tests successful
     */
    public static final int NODE_REPORT_POWER_OK = 0x0001;
    /**
     * Hardware tests failed at Power On
     */
    public static final int NODE_REPORT_POWER_FAIL = 0x0002;
    /**
     * Last UDP from Node failed due to truncated length,  Most likely caused by a collision.
     */
    public static final int NODE_REPORT_SOCKET_WR1 = 0x0003;
    /**
     * Unable to identify last UDP transmission. Check OpCode and packet length.
     */
    public static final int NODE_REPORT_PARSE_FAIL = 0x0004;
    /**
     * Unable to open Udp Socket in last transmission attempt
     */
    public static final int NODE_REPORT_UDP_FAIL = 0x0005;
    /**
     * Confirms that Short Name programming via ArtAddress, was successful.
     */
    public static final int NODE_REPORT_SH_NAME_OK = 0x0006;
    /**
     * Confirms that Long Name programming via ArtAddress, was successful.
     */
    public static final int NODE_REPORT_LO_NAME_OK = 0x0007;
    /**
     * DMX512 receive errors detected.
     */
    public static final int NODE_REPORT_DMX_ERROR = 0x0008;
    /**
     * Ran out of internal DMX transmit buffers.
     */
    public static final int NODE_REPORT_DMX_UDP_FULL = 0x0009;
    /**
     * Ran out of internal DMX Rx buffers.
     */
    public static final int NODE_REPORT_DMX_RX_FULL = 0x000a;
    /**
     * Rx Universe switches conflict.
     */
    public static final int NODE_REPORT_SWITCH_ERR = 0x000b;
    /**
     * Product configuration does not match firmware.
     */
    public static final int NODE_REPORT_CONFIG_ERR = 0x000c;
    /**
     * DMX output short detected. See GoodOutput field.
     */
    public static final int NODE_REPORT_DMX_SHORT = 0x000d;
    /**
     * Last attempt to upload new firmware failed.
     */
    public static final int NODE_REPORT_FIRMWARE_FAIL = 0x000e;
    /**
     * User changed switch settings when address locked by remote programming. User changes ignored.
     */
    public static final int NODE_REPORT_USER_FAIL = 0x000f;

    /**
     * A DMX to / from Art-Net device
     */
    public static final int STYLE_CODE_NODE = 0x00;
    /**
     * A lighting console.
     */
    public static final int STYLE_CODE_CONTROLLER = 0x01;
    /**
     * A Media Server.
     */
    public static final int STYLE_CODE__MEDIA = 0x02;
    /**
     * A network routing device.
     */
    public static final int STYLE_CODE_ROUTE = 0x03;
    /**
     * A backup device.
     */
    public static final int STYLE_CODE__BACKUP = 0x04;
    /**
     * A configuration or diagnostic tool.
     */
    public static final int STYLE_CODE_CONFIG = 0x05;
    /**
     * A visualiser.
     */
    public static final int STYLE_CODE_VISUAL = 0x06;

    /**
     * 4 Byte IP-Address
     */
    private byte[] ipAddress;

    /**
     * 2 Byte Port (Low-Byte first); Always 0x1936
     */
    private int port;

    /**
     * 2 Byte Firmware revision number (High-Byte first)
     */
    private int versionInfo;

    /**
     * 7 Bit Net Switch
     */
    private int net;

    /**
     * 4 Bit Sub Switch
     */
    private int subNet;

    /**
     * 2 Byte Oem Value (High-Byte first).
     * The Oem word describes the equipment vendor and the feature set available.
     * Bit 15 high indicates extended features available.
     */
    private int oem;

    /**
     * 1 Byte UBEA Version.
     * This field contains the firmware version of the User Bios Extension Area (UBEA).
     * If the UBEA is not programmed, this field contains zero.
     */
    private int ubea;

    /**
     * 2 Byte Status1 General Status register containing bit fields as follows:
     * 7-6 Indicator state:
     * - 00 Indicator state unknown.
     * - 01 Indicators in Locate Mode.
     * - 10 Indicators in Mute Mode.
     * - 11 Indicators in Normal Mode.
     * 5-4 Universe Address Programming Authority:
     * - 00 Universe Programming Authority unknown.
     * - 01 Set by front panel controls.
     * - 10 Programmed by network.
     * - 11 Not used.
     * 3 Not implemented, transmit as zero, receivers do not test.
     * 2
     * - 0 = Normal firmware boot (from flash). Nodes that do not support dual boot, clear this field to zero.
     * - 1 = Booted from R”M.
     * 1
     * - 0 = Not capable of Remote Device Management (RDM).
     * - 1 = Capable of Remote Device Management (RDM).
     * 0
     * - 0 = UBEA not present or corrupt
     * - 1 = UBEA present
     * -
     */
    private int status1;

    /**
     *
     */
    public ArtPollReply() {
        super(ArtNetOpCodes.OP_CODE_POLL_REPLY);
    }

    @Override
    public byte[] constructPacket() {
        return ArtNetPacket.constructPacket(0, 0);
    }

    public static byte[] constructPacket(final int talkToMe, final int priority) {
        byte[] result = ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH + 2, ArtNetOpCodes.OP_CODE_POLL_REPLY);
        result[ArtNetPacket.HEADER_LENGTH] = (byte) talkToMe;
        result[ArtNetPacket.HEADER_LENGTH + 1] = (byte) priority;
        return result;
    }
}
