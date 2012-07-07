package tc.vom.artNetLighter.infrastructure.packets;

import tc.vom.artNetLighter.infrastructure.constants.ArtNetNodeReportCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetStyleCodes;

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.copyToArray;

/**
 * Created with IntelliJ IDEA.
 * User: matthias
 * Date: 06.07.12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class ArtPollReply extends ArtNetPacket implements ArtNetStyleCodes, ArtNetNodeReportCodes {

    public static final byte[] SPARE_BYTES = new byte[]{0, 0, 0};
    public static final byte[] FILLER_BYTES = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    /**
     * 4 Byte IP-Address
     */
    private int ipAddress;

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
     * 2 Byte ESTA manufacturer code (Lo-Byte first)
     */
    private int estaManufacturer;

    /**
     * 18 Byte short name (null terminated)
     */
    private String shortName;

    /**
     * 64 Byte long name (null terminated)
     */
    private String longName;

    /**
     * 64 Byte node report (null terminated).
     * The array is a textual report of the Node’s operating status or operational errors. It is primarily intended for ‘engineering’ data rather than ‘end user’ data.
     * The field is formatted as: “#xxxx [yyyy..] zzzzz…” xxxx is a hex status code as defined in Table 3. yyyy is a decimal counter that increments every time the Node sends an ArtPollResponse. This allows the controller to monitor event changes in the Node. zzzz is an English text string defining the status. This is a fixed length field, although the string it contains can be shorter than the field.
     */
    private String nodeReport;

    /**
     * 2 Bytes Number of Ports (High-Byte first).
     * If number of inputs is not equal to number of outputs, the largest value is taken.
     * Zero is a legal value if no input or output ports are implemented. The maximum value is 4.
     */
    private int numPorts;

    /**
     * 4 Bytes port type of each channel.
     * This array defines the operation and protocol of each channel. (Ether-Lynx example = 0xc0, 0xc0, 0xc0, 0xc0). The array length is fixed, independent of the number of inputs or outputs physically available on the Node.
     * <p/>
     * - 7 Set is this channel can output data from the Art-Net Network.
     * - 6 Set if this channel can input onto the Art-NetNetwork.
     * - 5-0:
     * -- 00000 = DMX512
     * -- 00001 = MIDI
     * -- 00010 = Avab
     * -- 00011 = Colortran CMX
     * -- 00100 = ADB 62.5
     * -- 00101 = Art-Net
     */
    private byte[] portTypes;

    /**
     * 4 Bytes input status of each channel.
     * This array defines input status of the node.
     * - 7 Set – Data received.
     * - 6 Set – Channel includes DMX512 test packets.
     * - 5 Set – Channel includes DMX512 SIP’s.
     * - 4 Set – Channel includes DMX512 text packets.
     * - 3 Set – Input is disabled.
     * - 2 Set – Receive errors detected.
     * - 1-0 Unused and transmitted as zero.
     */
    private byte[] goodInput;

    /**
     * 4 Bytes output status of each channel.
     * This array defines output status of the node.
     * - 7 Set – Data is being transmitted.
     * - 6 Set – Channel includes DMX512 test packets.
     * - 5 Set – Channel includes DMX512 SIP’s.
     * - 4 Set – Channel includes DMX512 text packets.
     * - 3 Set – ”utput is merging ArtNet data.
     * - 2 Set – DMX output short detected on power up
     * - 1 Set – Merge Mode is LTP.
     * - 0 Unused and transmitted as zero.
     */
    private byte[] goodOutput;

    /**
     * 4* 4 Bit Universe of each input channel
     */
    private byte[] universesIn;

    /**
     * 4* 4 Bit Universe of each output channel
     */
    private byte[] universesOut;

    /**
     * 1 Byte Video.
     * Set to 00 when video display is showing local data. Set to 01 when video is showing ethernet data.
     */
    private int video;

    /**
     * 1 Byte Macro.
     * If the Node supports macro key inputs, this byte represents the trigger values. The Node is responsible for ‘debouncing’ inputs. When the ArtPollReply is set to transmit automatically, (TalkToMe Bit 1), the ArtPollReply will be sent on both key down and key up events. However, the Controller should not assume that only one bit position has changed.
     * The Macro inputs are used for remote event triggering or cueing.
     * <p/>
     * Bit fields are active high.
     * 7 Set – Macro 8 active.
     * 6 Set – Macro 7 active.
     * 5 Set – Macro 6 active.
     * 4 Set – Macro 5 active.
     * 3 Set – Macro 4 active.
     * 2 Set – Macro 3 active.
     * 1 Set – Macro 2 active.
     * 0 Set – Macro 1 active.
     */
    private int macro;

    /**
     * 1 Byte Remote.
     * If the Node supports remote trigger inputs, this byte represents the trigger values. The Node is responsible for ‘debouncing’ inputs. When the ArtPollReply is set to transmit automatically, (TalkToMe Bit 1), the ArtPollReply will be sent on both key down and key up events. However, the Controller should not assume that only one bit position has changed.
     * The Remote inputs are used for remote event triggering or cueing.
     * <p/>
     * Bit fields are active high.
     * 7 Set – Remote 8 active.
     * 6 Set – Remote 7 active.
     * 5 Set – Remote 6 active.
     * 4 Set – Remote 5 active.
     * 3 Set – Remote 4 active.
     * 2 Set – Remote 3 active.
     * 1 Set – Remote 2 active.
     * 0 Set – Remote 1 active.
     */
    private int remote;

    /**
     * 3 Byte Spare.
     * Not used, set to zero
     */
    private byte[] spare;

    /**
     * 1 Byte Style Code.The Style code defines the equipment style of the device.
     *
     * @see ArtNetStyleCodes
     */
    private int style;

    /**
     * 6 Byte Mac Address (High-Byte first)
     * Set to zero if node cannot supply this information.
     */
    private byte[] macAddress;

    /**
     * 4 Byte Bind IP.
     * If this unit is part of a larger or modular product, this is the IP of the root device.
     */
    private int bindIP;

    /**
     * 1 Byte Bind Index.
     * Set to zero if no binding, otherwise this number represents the order of bound devices. A lower number means closer to root device. A value of 1 means root device.
     */
    private int bindIndex;

    /**
     * 1 Byte Status 2.
     * 0 Set = Product supports web browser configuration.
     * 1
     * - Clr = Node’s IP is manually configured.
     * - Set = Node’s IP is DHCP configured.
     * 2
     * - Clr = Node is not DHCP capable.
     * - Set = Node is DHCP capable.
     * 3
     * - Clr = Node supports 8 bit Port-Address (Art-Net II).
     * - Set = Node supports 15 bit Port-Address (Art-Net 3).
     */
    private int status2;

    /**
     * 26 Byte Filler.
     * Transmit as zero. For future expansion.
     */
    private byte[] filler;

    public ArtPollReply(int ipAddress, int port, int versionInfo, int net, int subNet, int oem, int ubea, int status1, int estaManufacturer, String shortName, String longName, String nodeReport, int numPorts, byte[] portTypes, byte[] goodInput, byte[] goodOutput, byte[] universesIn, byte[] universesOut, int video, int macro, int remote, int style, byte[] macAddress, int bindIP, int bindIndex, int status2) {
        this(ipAddress, port, versionInfo, net, subNet, oem, ubea, status1, estaManufacturer, shortName, longName, nodeReport, numPorts, portTypes, goodInput, goodOutput, universesIn, universesOut, video, macro, remote, SPARE_BYTES, style, macAddress, bindIP, bindIndex, status2, FILLER_BYTES);
    }

    private ArtPollReply(int ipAddress, int port, int versionInfo, int net, int subNet, int oem, int ubea, int status1, int estaManufacturer, String shortName, String longName, String nodeReport, int numPorts, byte[] portTypes, byte[] goodInput, byte[] goodOutput, byte[] universesIn, byte[] universesOut, int video, int macro, int remote, byte[] spare, int style, byte[] macAddress, int bindIP, int bindIndex, int status2, byte[] filler) {
        super(ArtNetOpCodes.OP_CODE_POLL_REPLY);
        this.ipAddress = ipAddress;
        this.port = port;
        this.versionInfo = versionInfo;
        this.net = net;
        this.subNet = subNet;
        this.oem = oem;
        this.ubea = ubea;
        this.status1 = status1;
        this.estaManufacturer = estaManufacturer;
        this.shortName = shortName;
        this.longName = longName;
        this.nodeReport = nodeReport;
        this.numPorts = numPorts;
        this.portTypes = portTypes;
        this.goodInput = goodInput;
        this.goodOutput = goodOutput;
        this.universesIn = universesIn;
        this.universesOut = universesOut;
        this.video = video;
        this.macro = macro;
        this.remote = remote;
        this.spare = spare;
        this.style = style;
        this.macAddress = macAddress;
        this.bindIP = bindIP;
        this.bindIndex = bindIndex;
        this.status2 = status2;
        this.filler = filler;
    }

    @Override
    public byte[] constructPacket() {
        return ArtNetPacket.constructPacket(0, 0);
    }

    public static byte[] constructPacket(int ipAddress, int port, int versionInfo, int net, int subNet, int oem, int ubea, int status1, int estaManufacturer, String shortName, String longName, String nodeReport, int numPorts, byte[] portTypes, byte[] goodInput, byte[] goodOutput, byte[] universesIn, byte[] universesOut, int video, int macro, int remote, byte[] spare, int style, byte[] macAddress, int bindIP, int bindIndex, int status2, byte[] filler) {
        if (shortName == null)
            shortName = "";
        final byte[] shortNameBytes = shortName.getBytes(STRING_CHARSET);
        if (longName == null)
            longName = "";
        final byte[] longNameBytes = longName.getBytes(STRING_CHARSET);
        if (nodeReport == null)
            nodeReport = "";
        final byte[] nodeReportBytes = nodeReport.getBytes(STRING_CHARSET);
        return constructPacket(ipAddress, port, versionInfo, net, subNet, oem, ubea, status1, estaManufacturer, shortNameBytes, longNameBytes, nodeReportBytes, numPorts, portTypes, goodInput, goodOutput, universesIn, universesOut, video, macro, remote, spare, style, macAddress, bindIP, bindIndex, status2, filler);
    }

    public static byte[] constructPacket(int ipAddress, int port, int versionInfo, int net, int subNet, int oem, int ubea, int status1, int estaManufacturer, byte[] shortName, byte[] longName, byte[] nodeReport, int numPorts, byte[] portTypes, byte[] goodInput, byte[] goodOutput, byte[] universesIn, byte[] universesOut, int video, int macro, int remote, byte[] spare, int style, byte[] macAddress, int bindIP, int bindIndex, int status2, byte[] filler) {
        if (shortName.length > 17 && (shortName.length != 18 || shortName[17] != 0))
            throw new IllegalArgumentException("Short Name has a maximum length of 17 Bytes.");
        if (longName.length > 63 && (longName.length != 64 || longName[63] != 0))
            throw new IllegalArgumentException("Long Name has a maximum length of 63 Bytes.");
        if (nodeReport.length > 63 && (nodeReport.length != 64 || nodeReport[63] != 0))
            throw new IllegalArgumentException("Node Report has a maximum length of 63 Bytes.");
        if (numPorts > 4)
            throw new IllegalArgumentException("Maximum Port-Count is 4");
        if (portTypes.length > 4)
            throw new IllegalArgumentException("Maximum portTypes.length is 4");
        if (goodInput.length > 4)
            throw new IllegalArgumentException("Maximum goodInput.length is 4");
        if (goodOutput.length > 4)
            throw new IllegalArgumentException("Maximum goodOutput.length is 4");
        if (universesIn.length > 4)
            throw new IllegalArgumentException("Maximum universesIn.length is 4");
        if (universesOut.length > 4)
            throw new IllegalArgumentException("Maximum universesOut.length is 4");
        if (spare.length > 3)
            throw new IllegalArgumentException("Maximum spare.length is 4");
        if (macAddress.length > 6)
            throw new IllegalArgumentException("Maximum macAddress.length is 6");
        if (filler.length > 26)
            throw new IllegalArgumentException("Maximum filler.length is 26");

        byte[] result = ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH + 2, ArtNetOpCodes.OP_CODE_POLL_REPLY);

        result[ArtNetPacket.HEADER_LENGTH] = (byte) ((ipAddress >> 24) & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 1] = (byte) ((ipAddress >> 16) & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 2] = (byte) ((ipAddress >> 8) & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 3] = (byte) (ipAddress & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 4] = (byte) (port & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 5] = (byte) ((port & 0xff00) >> 8);
        result[ArtNetPacket.HEADER_LENGTH + 6] = (byte) ((versionInfo & 0xff00) >> 8);
        result[ArtNetPacket.HEADER_LENGTH + 7] = (byte) (versionInfo & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 8] = (byte) (net & 0x7f);
        result[ArtNetPacket.HEADER_LENGTH + 9] = (byte) (subNet & 0x0f);
        result[ArtNetPacket.HEADER_LENGTH + 10] = (byte) ((oem & 0xff00) >> 8);
        result[ArtNetPacket.HEADER_LENGTH + 11] = (byte) (oem & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 12] = (byte) ubea;
        result[ArtNetPacket.HEADER_LENGTH + 13] = (byte) status1;
        result[ArtNetPacket.HEADER_LENGTH + 14] = (byte) (estaManufacturer & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 15] = (byte) ((estaManufacturer & 0xff00) >> 8);
        copyToArray(shortName, result, ArtNetPacket.HEADER_LENGTH + 16);
        copyToArray(longName, result, ArtNetPacket.HEADER_LENGTH + 34);
        copyToArray(nodeReport, result, ArtNetPacket.HEADER_LENGTH + 98);
        result[ArtNetPacket.HEADER_LENGTH + 162] = (byte) ((numPorts & 0xff00) >> 8);
        result[ArtNetPacket.HEADER_LENGTH + 163] = (byte) (numPorts & 0xff);
        copyToArray(portTypes, result, ArtNetPacket.HEADER_LENGTH + 164);
        copyToArray(goodInput, result, ArtNetPacket.HEADER_LENGTH + 168);
        copyToArray(goodOutput, result, ArtNetPacket.HEADER_LENGTH + 172);
        copyToArray(universesIn, result, ArtNetPacket.HEADER_LENGTH + 176);
        copyToArray(universesOut, result, ArtNetPacket.HEADER_LENGTH + 180);
        result[ArtNetPacket.HEADER_LENGTH + 184] = (byte) video;
        result[ArtNetPacket.HEADER_LENGTH + 185] = (byte) macro;
        result[ArtNetPacket.HEADER_LENGTH + 186] = (byte) remote;
        copyToArray(spare, result, ArtNetPacket.HEADER_LENGTH + 187);
        result[ArtNetPacket.HEADER_LENGTH + 190] = (byte) style;
        copyToArray(macAddress, result, ArtNetPacket.HEADER_LENGTH + 191);
        result[ArtNetPacket.HEADER_LENGTH + 197] = (byte) ((bindIP >> 24) & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 198] = (byte) ((bindIP >> 16) & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 199] = (byte) ((bindIP >> 8) & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 200] = (byte) (bindIP & 0xff);
        result[ArtNetPacket.HEADER_LENGTH + 201] = (byte) bindIndex;
        result[ArtNetPacket.HEADER_LENGTH + 202] = (byte) status2;
        copyToArray(filler, result, ArtNetPacket.HEADER_LENGTH + 203);
        return result;
    }
}
