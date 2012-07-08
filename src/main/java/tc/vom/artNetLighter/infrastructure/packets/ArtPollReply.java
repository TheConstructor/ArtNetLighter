package tc.vom.artNetLighter.infrastructure.packets;

import tc.vom.artNetLighter.infrastructure.constants.ArtNetNodeReportCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetStyleCodes;

import java.util.Arrays;

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.*;

/**
 * Used to hold the data of an ArtPollReply-Packet.
 */
public class ArtPollReply extends ArtNetPacket implements ArtNetStyleCodes, ArtNetNodeReportCodes {

    public static final byte[] SPARE_BYTES = new byte[]{0, 0, 0};
    public static final byte[] FILLER_BYTES = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static final int PACKET_LENGTH = ArtNetPacket.HEADER_LENGTH + 203;
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
     * - 3 Set – Output is merging ArtNet data.
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
        return ArtPollReply.constructPacket(ipAddress, port, versionInfo, net, subNet, oem, ubea, status1, estaManufacturer, shortName, longName, nodeReport, numPorts, portTypes, goodInput, goodOutput, universesIn, universesOut, video, macro, remote, spare, style, macAddress, bindIP, bindIndex, status2, filler);
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public int getVersionInfo() {
        return versionInfo;
    }

    public int getNet() {
        return net;
    }

    public int getSubNet() {
        return subNet;
    }

    public int getOem() {
        return oem;
    }

    public int getUbea() {
        return ubea;
    }

    public int getStatus1() {
        return status1;
    }

    public int getEstaManufacturer() {
        return estaManufacturer;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getNodeReport() {
        return nodeReport;
    }

    public int getNumPorts() {
        return numPorts;
    }

    public byte[] getPortTypes() {
        return portTypes;
    }

    public byte[] getGoodInput() {
        return goodInput;
    }

    public byte[] getGoodOutput() {
        return goodOutput;
    }

    public byte[] getUniversesIn() {
        return universesIn;
    }

    public byte[] getUniversesOut() {
        return universesOut;
    }

    public int getVideo() {
        return video;
    }

    public int getMacro() {
        return macro;
    }

    public int getRemote() {
        return remote;
    }

    public byte[] getSpare() {
        return spare;
    }

    public int getStyle() {
        return style;
    }

    public byte[] getMacAddress() {
        return macAddress;
    }

    public int getBindIP() {
        return bindIP;
    }

    public int getBindIndex() {
        return bindIndex;
    }

    public int getStatus2() {
        return status2;
    }

    public byte[] getFiller() {
        return filler;
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

        byte[] result = ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH + 229, ArtNetOpCodes.OP_CODE_POLL_REPLY);

        set4BytesHighToLow(ipAddress, result, ArtNetPacket.HEADER_LENGTH);
        set2BytesLowToHigh(port, result, ArtNetPacket.HEADER_LENGTH + 4);
        set2BytesHighToLow(versionInfo, result, ArtNetPacket.HEADER_LENGTH + 6);
        result[ArtNetPacket.HEADER_LENGTH + 8] = (byte) (net & 0x7f);
        result[ArtNetPacket.HEADER_LENGTH + 9] = (byte) (subNet & 0x0f);
        set2BytesHighToLow(oem, result, ArtNetPacket.HEADER_LENGTH + 10);
        result[ArtNetPacket.HEADER_LENGTH + 12] = (byte) ubea;
        result[ArtNetPacket.HEADER_LENGTH + 13] = (byte) status1;
        set2BytesLowToHigh(estaManufacturer, result, ArtNetPacket.HEADER_LENGTH + 14);
        copyToArray(shortName, result, ArtNetPacket.HEADER_LENGTH + 16);
        copyToArray(longName, result, ArtNetPacket.HEADER_LENGTH + 34);
        copyToArray(nodeReport, result, ArtNetPacket.HEADER_LENGTH + 98);
        set2BytesHighToLow(numPorts, result, ArtNetPacket.HEADER_LENGTH + 162);
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
        set4BytesHighToLow(bindIP, result, ArtNetPacket.HEADER_LENGTH + 197);
        result[ArtNetPacket.HEADER_LENGTH + 201] = (byte) bindIndex;
        result[ArtNetPacket.HEADER_LENGTH + 202] = (byte) status2;
        copyToArray(filler, result, ArtNetPacket.HEADER_LENGTH + 203);
        return result;
    }

    public ArtPollReply(byte[] data) {
        super(data);
        if (data.length < ArtPoll.PACKET_LENGTH) {
            throw new IllegalArgumentException("Minimum size for ArtPollReply is " + ArtPollReply.PACKET_LENGTH);
        }
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_POLL_REPLY) {
            throw new IllegalArgumentException("Provided data specifies a wrong OpCode");
        }

        this.ipAddress = get4BytesHighToLow(data, ArtNetPacket.HEADER_LENGTH);
        this.port = get2BytesLowToHigh(data, ArtNetPacket.HEADER_LENGTH + 4);
        this.versionInfo = get2BytesHighToLow(data, ArtNetPacket.HEADER_LENGTH + 6);
        this.net = data[ArtNetPacket.HEADER_LENGTH + 8] & 0x7f;
        this.subNet = data[ArtNetPacket.HEADER_LENGTH + 9] & 0x0f;
        this.oem = get2BytesHighToLow(data, ArtNetPacket.HEADER_LENGTH + 10);
        this.ubea = data[ArtNetPacket.HEADER_LENGTH + 12];
        this.status1 = data[ArtNetPacket.HEADER_LENGTH + 13];
        this.estaManufacturer = get2BytesLowToHigh(data, ArtNetPacket.HEADER_LENGTH + 14);
        this.shortName = copyStringFromArray(data, ArtNetPacket.HEADER_LENGTH + 16, 17);
        this.longName = copyStringFromArray(data, ArtNetPacket.HEADER_LENGTH + 34, 63);
        this.nodeReport = copyStringFromArray(data, ArtNetPacket.HEADER_LENGTH + 98, 63);
        this.numPorts = get2BytesHighToLow(data, ArtNetPacket.HEADER_LENGTH + 162);
        this.portTypes = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 164, 4);
        this.goodInput = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 168, 4);
        this.goodOutput = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 172, 4);
        this.universesIn = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 176, 4);
        this.universesOut = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 180, 4);
        this.video = data[ArtNetPacket.HEADER_LENGTH + 184];
        this.macro = data[ArtNetPacket.HEADER_LENGTH + 185];
        this.remote = data[ArtNetPacket.HEADER_LENGTH + 186];
        this.spare = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 187, 3);
        this.style = data[ArtNetPacket.HEADER_LENGTH + 190];
        this.macAddress = copyBytesFromArray(data, ArtNetPacket.HEADER_LENGTH + 191, 6);
        if (data.length > ArtNetPacket.HEADER_LENGTH + 201) {
            this.bindIP = get4BytesHighToLow(data, ArtNetPacket.HEADER_LENGTH + 197);
            this.bindIndex = data[ArtNetPacket.HEADER_LENGTH + 201];
        } else {
            this.bindIP = 0;
            this.bindIndex = 0;
        }
        if (data.length > ArtNetPacket.HEADER_LENGTH + 202) {
            this.status2 = data[ArtNetPacket.HEADER_LENGTH + 202];
        } else {
            this.status2 = 0;
        }
        if (data.length > ArtNetPacket.HEADER_LENGTH + 203) {
            this.filler = Arrays.copyOfRange(data, ArtNetPacket.HEADER_LENGTH + 203, data.length);
        } else {
            this.filler = new byte[0];
        }

        // Old specification is that high and low nibble are to be the same, but we only need the information once.
        for (int i = 0; i < 4; i++) {
            this.universesIn[i] = (byte) (this.universesIn[i] & 0xf);
            this.universesOut[i] = (byte) (this.universesOut[i] & 0xf);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtPollReply)) return false;
        if (!super.equals(o)) return false;

        ArtPollReply that = (ArtPollReply) o;

        if (bindIP != that.bindIP) return false;
        if (bindIndex != that.bindIndex) return false;
        if (estaManufacturer != that.estaManufacturer) return false;
        if (ipAddress != that.ipAddress) return false;
        if (macro != that.macro) return false;
        if (net != that.net) return false;
        if (numPorts != that.numPorts) return false;
        if (oem != that.oem) return false;
        if (port != that.port) return false;
        if (remote != that.remote) return false;
        if (status1 != that.status1) return false;
        if (status2 != that.status2) return false;
        if (style != that.style) return false;
        if (subNet != that.subNet) return false;
        if (ubea != that.ubea) return false;
        if (versionInfo != that.versionInfo) return false;
        if (video != that.video) return false;
        if (!Arrays.equals(goodInput, that.goodInput)) return false;
        if (!Arrays.equals(goodOutput, that.goodOutput)) return false;
        if (!longName.equals(that.longName)) return false;
        if (!Arrays.equals(macAddress, that.macAddress)) return false;
        if (!nodeReport.equals(that.nodeReport)) return false;
        if (!Arrays.equals(portTypes, that.portTypes)) return false;
        if (!shortName.equals(that.shortName)) return false;
        if (!Arrays.equals(universesIn, that.universesIn)) return false;
        if (!Arrays.equals(universesOut, that.universesOut)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ipAddress;
        result = 31 * result + port;
        result = 31 * result + versionInfo;
        result = 31 * result + net;
        result = 31 * result + subNet;
        result = 31 * result + oem;
        result = 31 * result + ubea;
        result = 31 * result + status1;
        result = 31 * result + estaManufacturer;
        result = 31 * result + shortName.hashCode();
        result = 31 * result + longName.hashCode();
        result = 31 * result + nodeReport.hashCode();
        result = 31 * result + numPorts;
        result = 31 * result + Arrays.hashCode(portTypes);
        result = 31 * result + Arrays.hashCode(goodInput);
        result = 31 * result + Arrays.hashCode(goodOutput);
        result = 31 * result + Arrays.hashCode(universesIn);
        result = 31 * result + Arrays.hashCode(universesOut);
        result = 31 * result + video;
        result = 31 * result + macro;
        result = 31 * result + remote;
        result = 31 * result + style;
        result = 31 * result + Arrays.hashCode(macAddress);
        result = 31 * result + bindIP;
        result = 31 * result + bindIndex;
        result = 31 * result + status2;
        return result;
    }
}
