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

import tc.vom.artNetLighter.infrastructure.ArtNetToolkit;
import tc.vom.artNetLighter.infrastructure.ByteArrayToolkit;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetNodeReportCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetStyleCodes;

import java.util.Arrays;

/**
 * Used to hold the data of an ArtPollReply-Packet.
 */
public class ArtPollReply extends _ArtNetPacket implements ArtNetStyleCodes, ArtNetNodeReportCodes {

    public static final byte[] SPARE_BYTES = {0, 0, 0};
    public static final byte[] FILLER_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static final int NODE_REPORT_LENGTH = 64;
    public static final int MAXIMUM_NUM_PORTS = 4;
    public static final int PORT_TYPES_LENGTH = 4;
    public static final int GOOD_INPUT_LENGTH = 4;
    public static final int GOOD_OUTPUT_LENGTH = 4;
    public static final int UNIVERSES_IN_LENGTH = 4;
    public static final int UNIVERSES_OUT_LENGTH = 4;
    public static final int SPARE_LENGTH = 3;
    public static final int MAC_ADDRESS_LENGTH = 6;
    public static final int FILLER_LENGTH = 26;

    private static final int START_IP_ADDRESS = _ArtNetPacket.SHORT_HEADER_LENGTH;
    private static final int START_PORT = ArtPollReply.START_IP_ADDRESS + 4;
    private static final int START_VERSION_INFO = ArtPollReply.START_PORT + 2;
    private static final int START_NET = ArtPollReply.START_VERSION_INFO + 2;
    private static final int START_SUB_NET = ArtPollReply.START_NET + 1;
    private static final int START_OEM = ArtPollReply.START_SUB_NET + 1;
    private static final int START_UBEA = ArtPollReply.START_OEM + 2;
    private static final int START_STATUS1 = ArtPollReply.START_UBEA + 1;
    private static final int START_ESTA_MANUFACTURER = ArtPollReply.START_STATUS1 + 1;
    private static final int START_SHORT_NAME = ArtPollReply.START_ESTA_MANUFACTURER + 2;
    private static final int START_LONG_NAME = ArtPollReply.START_SHORT_NAME + _ArtNetPacket.SHORT_NAME_LENGTH;
    private static final int START_NODE_REPORT = ArtPollReply.START_LONG_NAME + _ArtNetPacket.LONG_NAME_LENGTH;
    private static final int START_NUM_PORTS = ArtPollReply.START_NODE_REPORT + ArtPollReply.NODE_REPORT_LENGTH;
    private static final int START_PORT_TYPES = ArtPollReply.START_NUM_PORTS + 2;
    private static final int START_GOOD_INPUT = ArtPollReply.START_PORT_TYPES + ArtPollReply.PORT_TYPES_LENGTH;
    private static final int START_GOOD_OUTPUT = ArtPollReply.START_GOOD_INPUT + ArtPollReply.GOOD_INPUT_LENGTH;
    private static final int START_UNIVERSES_IN = ArtPollReply.START_GOOD_OUTPUT + ArtPollReply.GOOD_OUTPUT_LENGTH;
    private static final int START_UNIVERSES_OUT = ArtPollReply.START_UNIVERSES_IN + ArtPollReply.UNIVERSES_IN_LENGTH;
    private static final int START_VIDEO = ArtPollReply.START_UNIVERSES_OUT + ArtPollReply.UNIVERSES_OUT_LENGTH;
    private static final int START_MACRO = ArtPollReply.START_VIDEO + 1;
    private static final int START_REMOTE = ArtPollReply.START_MACRO + 1;
    private static final int START_SPARE = ArtPollReply.START_REMOTE + 1;
    private static final int START_STYLE = ArtPollReply.START_SPARE + ArtPollReply.SPARE_LENGTH;
    private static final int START_MAC_ADDRESS = ArtPollReply.START_STYLE + 1;
    private static final int START_BIND_IP = ArtPollReply.START_MAC_ADDRESS + ArtPollReply.MAC_ADDRESS_LENGTH;
    private static final int START_BIND_INDEX = ArtPollReply.START_BIND_IP + 4;
    private static final int START_STATUS2 = ArtPollReply.START_BIND_INDEX + 1;
    private static final int START_FILLER = ArtPollReply.START_STATUS2 + 1;

    /**
     * {@link #bindIP} and following fields are rather "new", so this length differs greatly from {@link #PACKET_LENGTH}
     */
    public static final int MINIMUM_PACKET_LENGTH = ArtPollReply.START_BIND_IP;
    public static final int PACKET_LENGTH = ArtPollReply.START_FILLER + ArtPollReply.FILLER_LENGTH;

    /**
     * 4 Byte IP-Address
     */
    private final int ipAddress;

    /**
     * 2 Byte Port (Low-Byte first); Always 0x1936
     */
    private final int port;

    /**
     * 2 Byte Firmware revision number (High-Byte first)
     */
    private final int versionInfo;

    /**
     * 7 Bit Net Switch
     */
    private final byte net;

    /**
     * 4 Bit Sub Switch
     */
    private final byte subNet;

    /**
     * 2 Byte Oem Value (High-Byte first).
     * The Oem word describes the equipment vendor and the feature set available.
     * Bit 15 high indicates extended features available.
     */
    private final int oem;

    /**
     * 1 Byte UBEA Version.
     * This field contains the firmware version of the User Bios Extension Area (UBEA).
     * If the UBEA is not programmed, this field contains zero.
     */
    private final byte ubea;

    /**
     * 1 Byte Status1 General Status register containing bit fields as follows:
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
    private final byte status1;

    /**
     * 2 Byte ESTA manufacturer code (Lo-Byte first)
     */
    private final int estaManufacturer;

    /**
     * 18 Byte short name (null terminated)
     */
    private final String shortName;

    /**
     * 64 Byte long name (null terminated)
     */
    private final String longName;

    /**
     * 64 Byte node report (null terminated).
     * The array is a textual report of the Node’s operating status or operational errors. It is primarily intended for ‘engineering’ data rather than ‘end user’ data.
     * The field is formatted as: “#xxxx [yyyy..] zzzzz…” xxxx is a hex status code as defined in Table 3. yyyy is a decimal counter that increments every time the Node sends an ArtPollResponse. This allows the controller to monitor event changes in the Node. zzzz is an English text string defining the status. This is a fixed length field, although the string it contains can be shorter than the field.
     */
    private final String nodeReport;

    /**
     * 2 Bytes Number of Ports (High-Byte first).
     * If number of inputs is not equal to number of outputs, the largest value is taken.
     * Zero is a legal value if no input or output ports are implemented. The maximum value is 4.
     */
    private final int numPorts;

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
    private final byte[] portTypes;

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
    private final byte[] goodInput;

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
    private final byte[] goodOutput;

    /**
     * 4* 4 Bit Universe of each input channel
     */
    private final byte[] universesIn;

    /**
     * 4* 4 Bit Universe of each output channel
     */
    private final byte[] universesOut;

    /**
     * 1 Byte Video.
     * Set to 00 when video display is showing local data. Set to 01 when video is showing ethernet data.
     */
    private final byte video;

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
    private final byte macro;

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
    private final byte remote;

    /**
     * 3 Byte Spare.
     * Not used, set to zero
     */
    private final byte[] spare;

    /**
     * 1 Byte Style Code.The Style code defines the equipment style of the device.
     *
     * @see ArtNetStyleCodes
     */
    private final byte style;

    /**
     * 6 Byte Mac Address (High-Byte first)
     * Set to zero if node cannot supply this information.
     */
    private final byte[] macAddress;

    /**
     * 4 Byte Bind IP.
     * If this unit is part of a larger or modular product, this is the IP of the root device.
     */
    private final int bindIP;

    /**
     * 1 Byte Bind Index.
     * Set to zero if no binding, otherwise this number represents the order of bound devices. A lower number means closer to root device. A value of 1 means root device.
     */
    private final byte bindIndex;

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
    private final byte status2;

    /**
     * 26 Byte Filler.
     * Transmit as zero. For future expansion.
     */
    private final byte[] filler;

    public ArtPollReply(final int ipAddress, final int port, final int versionInfo, final byte net, final byte subNet, final int oem, final byte ubea, final byte status1, final int estaManufacturer, final String shortName, final String longName, final String nodeReport, final int numPorts, final byte[] portTypes, final byte[] goodInput, final byte[] goodOutput, final byte[] universesIn, final byte[] universesOut, final byte video, final byte macro, final byte remote, final byte style, final byte[] macAddress, final int bindIP, final byte bindIndex, final byte status2) {
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
        this.spare = ArtPollReply.SPARE_BYTES;
        this.style = style;
        this.macAddress = macAddress;
        this.bindIP = bindIP;
        this.bindIndex = bindIndex;
        this.status2 = status2;
        this.filler = ArtPollReply.FILLER_BYTES;
    }

    public int getIpAddress() {
        return this.ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public int getVersionInfo() {
        return this.versionInfo;
    }

    public byte getNet() {
        return this.net;
    }

    public byte getSubNet() {
        return this.subNet;
    }

    public int getOem() {
        return this.oem;
    }

    public byte getUbea() {
        return this.ubea;
    }

    public byte getStatus1() {
        return this.status1;
    }

    public int getEstaManufacturer() {
        return this.estaManufacturer;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getLongName() {
        return this.longName;
    }

    public String getNodeReport() {
        return this.nodeReport;
    }

    public int getNumPorts() {
        return this.numPorts;
    }

    public byte[] getPortTypes() {
        return this.portTypes;
    }

    public byte[] getGoodInput() {
        return this.goodInput;
    }

    public byte[] getGoodOutput() {
        return this.goodOutput;
    }

    public byte[] getUniversesIn() {
        return this.universesIn;
    }

    public byte[] getUniversesOut() {
        return this.universesOut;
    }

    public byte getVideo() {
        return this.video;
    }

    public byte getMacro() {
        return this.macro;
    }

    public byte getRemote() {
        return this.remote;
    }

    public byte[] getSpare() {
        return this.spare;
    }

    public byte getStyle() {
        return this.style;
    }

    public byte[] getMacAddress() {
        return this.macAddress;
    }

    public int getBindIP() {
        return this.bindIP;
    }

    public byte getBindIndex() {
        return this.bindIndex;
    }

    public byte getStatus2() {
        return this.status2;
    }

    public byte[] getFiller() {
        return this.filler;
    }

    @Override
    public byte[] constructPacket() {
        return ArtPollReply.constructPacket(this.ipAddress, this.port, this.versionInfo, this.net, this.subNet, this.oem, this.ubea, this.status1, this.estaManufacturer, this.shortName, this.longName, this.nodeReport, this.numPorts, this.portTypes, this.goodInput, this.goodOutput, this.universesIn, this.universesOut, this.video, this.macro, this.remote, this.spare, this.style, this.macAddress, this.bindIP, this.bindIndex, this.status2, this.filler);
    }

    public static byte[] constructPacket(final int ipAddress, final int port, final int versionInfo, final byte net, final byte subNet, final int oem, final byte ubea, final byte status1, final int estaManufacturer, String shortName, String longName, String nodeReport, final int numPorts, final byte[] portTypes, final byte[] goodInput, final byte[] goodOutput, final byte[] universesIn, final byte[] universesOut, final byte video, final byte macro, final byte remote, final byte[] spare, final byte style, final byte[] macAddress, final int bindIP, final byte bindIndex, final byte status2, final byte[] filler) {
        if (shortName == null) {
            shortName = "";
        }
        final byte[] shortNameBytes = shortName.getBytes(_ArtNetPacket.STRING_CHARSET);
        if (longName == null) {
            longName = "";
        }
        final byte[] longNameBytes = longName.getBytes(_ArtNetPacket.STRING_CHARSET);
        if (nodeReport == null) {
            nodeReport = "";
        }
        final byte[] nodeReportBytes = nodeReport.getBytes(_ArtNetPacket.STRING_CHARSET);
        return ArtPollReply.constructPacket(ipAddress, port, versionInfo, net, subNet, oem, ubea, status1, estaManufacturer, shortNameBytes, longNameBytes, nodeReportBytes, numPorts, portTypes, goodInput, goodOutput, universesIn, universesOut, video, macro, remote, spare, style, macAddress, bindIP, bindIndex, status2, filler);
    }

    public static byte[] constructPacket(final int ipAddress, final int port, final int versionInfo, final byte net, final byte subNet, final int oem, final byte ubea, final byte status1, final int estaManufacturer, final byte[] shortName, final byte[] longName, final byte[] nodeReport, final int numPorts, final byte[] portTypes, final byte[] goodInput, final byte[] goodOutput, final byte[] universesIn, final byte[] universesOut, final byte video, final byte macro, final byte remote, final byte[] spare, final byte style, final byte[] macAddress, final int bindIP, final byte bindIndex, final byte status2, final byte[] filler) {
        if ((shortName.length >= _ArtNetPacket.SHORT_NAME_LENGTH) && ((shortName.length != _ArtNetPacket.SHORT_NAME_LENGTH) || (shortName[_ArtNetPacket.SHORT_NAME_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Short Name has a maximum length of " + (_ArtNetPacket.SHORT_NAME_LENGTH - 1) + " Bytes.");
        }
        if ((longName.length >= _ArtNetPacket.LONG_NAME_LENGTH) && ((longName.length != _ArtNetPacket.LONG_NAME_LENGTH) || (longName[_ArtNetPacket.LONG_NAME_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Long Name has a maximum length of " + (_ArtNetPacket.LONG_NAME_LENGTH - 1) + " Bytes.");
        }
        if ((nodeReport.length > ArtPollReply.NODE_REPORT_LENGTH) || ((nodeReport.length == ArtPollReply.NODE_REPORT_LENGTH) && (nodeReport[ArtPollReply.NODE_REPORT_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Node Report has a maximum length of " + ArtPollReply.NODE_REPORT_LENGTH + " Bytes and is 0-terminated.");
        }
        if (numPorts > ArtPollReply.MAXIMUM_NUM_PORTS) {
            throw new IllegalArgumentException("Maximum Port-Count is " + ArtPollReply.MAXIMUM_NUM_PORTS);
        }
        if (portTypes.length > ArtPollReply.PORT_TYPES_LENGTH) {
            throw new IllegalArgumentException("portTypes has a maximum length of " + ArtPollReply.PORT_TYPES_LENGTH + " Bytes.");
        }
        if (goodInput.length > ArtPollReply.GOOD_INPUT_LENGTH) {
            throw new IllegalArgumentException("goodInput has a maximum length of " + ArtPollReply.GOOD_INPUT_LENGTH + " Bytes.");
        }
        if (goodOutput.length > ArtPollReply.GOOD_OUTPUT_LENGTH) {
            throw new IllegalArgumentException("goodOutput has a maximum length of " + ArtPollReply.GOOD_OUTPUT_LENGTH + " Bytes.");
        }
        if (universesIn.length > ArtPollReply.UNIVERSES_IN_LENGTH) {
            throw new IllegalArgumentException("universesIn has a maximum length of " + ArtPollReply.UNIVERSES_IN_LENGTH + " Bytes.");
        }
        if (universesOut.length > ArtPollReply.UNIVERSES_OUT_LENGTH) {
            throw new IllegalArgumentException("universesOut has a maximum length of " + ArtPollReply.UNIVERSES_OUT_LENGTH + " Bytes.");
        }
        if (spare.length > ArtPollReply.SPARE_LENGTH) {
            throw new IllegalArgumentException("spare has a maximum length of " + ArtPollReply.SPARE_LENGTH + " Bytes.");
        }
        if (macAddress.length > ArtPollReply.MAC_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("macAddress has a maximum length of " + ArtPollReply.MAC_ADDRESS_LENGTH + " Bytes.");
        }

        final byte[] result = _ArtNetPacket.constructUnversionedPacket(ArtPollReply.START_FILLER + filler.length, ArtNetOpCodes.OP_CODE_POLL_REPLY);

        ByteArrayToolkit.set4BytesHighToLow(ipAddress, result, ArtPollReply.START_IP_ADDRESS);
        ByteArrayToolkit.set2BytesLowToHigh(port, result, ArtPollReply.START_PORT);
        ByteArrayToolkit.set2BytesHighToLow(versionInfo, result, ArtPollReply.START_VERSION_INFO);
        result[ArtPollReply.START_NET] = (byte) (net & ArtNetToolkit.MAX_NET);
        result[ArtPollReply.START_SUB_NET] = (byte) (subNet & ArtNetToolkit.MAX_SUB_NET);
        ByteArrayToolkit.set2BytesHighToLow(oem, result, ArtPollReply.START_OEM);
        result[ArtPollReply.START_UBEA] = ubea;
        result[ArtPollReply.START_STATUS1] = status1;
        ByteArrayToolkit.set2BytesLowToHigh(estaManufacturer, result, ArtPollReply.START_ESTA_MANUFACTURER);
        ByteArrayToolkit.setBytes(shortName, result, ArtPollReply.START_SHORT_NAME);
        ByteArrayToolkit.setBytes(longName, result, ArtPollReply.START_LONG_NAME);
        ByteArrayToolkit.setBytes(nodeReport, result, ArtPollReply.START_NODE_REPORT);
        ByteArrayToolkit.set2BytesHighToLow(numPorts, result, ArtPollReply.START_NUM_PORTS);
        ByteArrayToolkit.setBytes(portTypes, result, ArtPollReply.START_PORT_TYPES);
        ByteArrayToolkit.setBytes(goodInput, result, ArtPollReply.START_GOOD_INPUT);
        ByteArrayToolkit.setBytes(goodOutput, result, ArtPollReply.START_GOOD_OUTPUT);
        ByteArrayToolkit.setBytes(universesIn, result, ArtPollReply.START_UNIVERSES_IN);
        ByteArrayToolkit.setBytes(universesOut, result, ArtPollReply.START_UNIVERSES_OUT);
        result[ArtPollReply.START_VIDEO] = video;
        result[ArtPollReply.START_MACRO] = macro;
        result[ArtPollReply.START_REMOTE] = remote;
        ByteArrayToolkit.setBytes(spare, result, ArtPollReply.START_SPARE);
        result[ArtPollReply.START_STYLE] = style;
        ByteArrayToolkit.setBytes(macAddress, result, ArtPollReply.START_MAC_ADDRESS);
        ByteArrayToolkit.set4BytesHighToLow(bindIP, result, ArtPollReply.START_BIND_IP);
        result[ArtPollReply.START_BIND_INDEX] = bindIndex;
        result[ArtPollReply.START_STATUS2] = status2;
        ByteArrayToolkit.setBytes(filler, result, ArtPollReply.START_FILLER);
        return result;
    }

    public ArtPollReply(final byte[] data) {
        super(data);
        if (data.length < ArtPollReply.MINIMUM_PACKET_LENGTH) {
            throw new IllegalArgumentException("Minimum size for ArtPollReply is " + ArtPollReply.MINIMUM_PACKET_LENGTH);
        }
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_POLL_REPLY) {
            throw new IllegalArgumentException("Provided data specifies a wrong OpCode");
        }

        this.ipAddress = ByteArrayToolkit.get4BytesHighToLow(data, ArtPollReply.START_IP_ADDRESS);
        this.port = ByteArrayToolkit.get2BytesLowToHigh(data, ArtPollReply.START_PORT);
        this.versionInfo = ByteArrayToolkit.get2BytesHighToLow(data, ArtPollReply.START_VERSION_INFO);
        this.net = (byte) (data[ArtPollReply.START_NET] & ArtNetToolkit.MAX_NET);
        this.subNet = (byte) (data[ArtPollReply.START_SUB_NET] & ArtNetToolkit.MAX_SUB_NET);
        this.oem = ByteArrayToolkit.get2BytesHighToLow(data, ArtPollReply.START_OEM);
        this.ubea = data[ArtPollReply.START_UBEA];
        this.status1 = data[ArtPollReply.START_STATUS1];
        this.estaManufacturer = ByteArrayToolkit.get2BytesLowToHigh(data, ArtPollReply.START_ESTA_MANUFACTURER);
        this.shortName = ByteArrayToolkit.getString(data, ArtPollReply.START_SHORT_NAME, _ArtNetPacket.SHORT_NAME_LENGTH);
        this.longName = ByteArrayToolkit.getString(data, ArtPollReply.START_LONG_NAME, _ArtNetPacket.LONG_NAME_LENGTH);
        this.nodeReport = ByteArrayToolkit.getString(data, ArtPollReply.START_NODE_REPORT, ArtPollReply.NODE_REPORT_LENGTH);
        this.numPorts = ByteArrayToolkit.get2BytesHighToLow(data, ArtPollReply.START_NUM_PORTS);
        this.portTypes = ByteArrayToolkit.getBytes(data, ArtPollReply.START_PORT_TYPES, ArtPollReply.PORT_TYPES_LENGTH);
        this.goodInput = ByteArrayToolkit.getBytes(data, ArtPollReply.START_GOOD_INPUT, ArtPollReply.GOOD_INPUT_LENGTH);
        this.goodOutput = ByteArrayToolkit.getBytes(data, ArtPollReply.START_GOOD_OUTPUT, ArtPollReply.GOOD_OUTPUT_LENGTH);
        this.universesIn = ByteArrayToolkit.getBytes(data, ArtPollReply.START_UNIVERSES_IN, ArtPollReply.UNIVERSES_IN_LENGTH);
        this.universesOut = ByteArrayToolkit.getBytes(data, ArtPollReply.START_UNIVERSES_OUT, ArtPollReply.UNIVERSES_OUT_LENGTH);
        this.video = data[ArtPollReply.START_VIDEO];
        this.macro = data[ArtPollReply.START_MACRO];
        this.remote = data[ArtPollReply.START_REMOTE];
        this.spare = ByteArrayToolkit.getBytes(data, ArtPollReply.START_SPARE, ArtPollReply.SPARE_LENGTH);
        this.style = data[ArtPollReply.START_STYLE];
        this.macAddress = ByteArrayToolkit.getBytes(data, ArtPollReply.START_MAC_ADDRESS, ArtPollReply.MAC_ADDRESS_LENGTH);
        if (data.length > ArtPollReply.START_BIND_INDEX) {
            this.bindIP = ByteArrayToolkit.get4BytesHighToLow(data, ArtPollReply.START_BIND_IP);
            this.bindIndex = data[ArtPollReply.START_BIND_INDEX];
        } else {
            this.bindIP = 0;
            this.bindIndex = 0;
        }
        if (data.length > ArtPollReply.START_STATUS2) {
            this.status2 = data[ArtPollReply.START_STATUS2];
        } else {
            this.status2 = 0;
        }
        if (data.length > ArtPollReply.START_FILLER) {
            this.filler = ByteArrayToolkit.getBytes(data, ArtPollReply.START_FILLER);
        } else {
            this.filler = new byte[0];
        }

        // Old specification is that high and low nibble are to be the same, but we only need the information once.
        for (int i = 0; i < 4; i++) {
            this.universesIn[i] = (byte) (this.universesIn[i] & ArtNetToolkit.MAX_UNIVERSE);
            this.universesOut[i] = (byte) (this.universesOut[i] & ArtNetToolkit.MAX_UNIVERSE);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtPollReply)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtPollReply that = (ArtPollReply) o;

        if (this.bindIP != that.bindIP) {
            return false;
        }
        if (this.bindIndex != that.bindIndex) {
            return false;
        }
        if (this.estaManufacturer != that.estaManufacturer) {
            return false;
        }
        if (this.ipAddress != that.ipAddress) {
            return false;
        }
        if (this.macro != that.macro) {
            return false;
        }
        if (this.net != that.net) {
            return false;
        }
        if (this.numPorts != that.numPorts) {
            return false;
        }
        if (this.oem != that.oem) {
            return false;
        }
        if (this.port != that.port) {
            return false;
        }
        if (this.remote != that.remote) {
            return false;
        }
        if (this.status1 != that.status1) {
            return false;
        }
        if (this.status2 != that.status2) {
            return false;
        }
        if (this.style != that.style) {
            return false;
        }
        if (this.subNet != that.subNet) {
            return false;
        }
        if (this.ubea != that.ubea) {
            return false;
        }
        if (this.versionInfo != that.versionInfo) {
            return false;
        }
        if (this.video != that.video) {
            return false;
        }
        if (!Arrays.equals(this.goodInput, that.goodInput)) {
            return false;
        }
        if (!Arrays.equals(this.goodOutput, that.goodOutput)) {
            return false;
        }
        if (!this.longName.equals(that.longName)) {
            return false;
        }
        if (!Arrays.equals(this.macAddress, that.macAddress)) {
            return false;
        }
        if (!this.nodeReport.equals(that.nodeReport)) {
            return false;
        }
        if (!Arrays.equals(this.portTypes, that.portTypes)) {
            return false;
        }
        if (!this.shortName.equals(that.shortName)) {
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
        result = (31 * result) + this.ipAddress;
        result = (31 * result) + this.port;
        result = (31 * result) + this.versionInfo;
        result = (31 * result) + this.net;
        result = (31 * result) + this.subNet;
        result = (31 * result) + this.oem;
        result = (31 * result) + this.ubea;
        result = (31 * result) + this.status1;
        result = (31 * result) + this.estaManufacturer;
        result = (31 * result) + this.shortName.hashCode();
        result = (31 * result) + this.longName.hashCode();
        result = (31 * result) + this.nodeReport.hashCode();
        result = (31 * result) + this.numPorts;
        result = (31 * result) + Arrays.hashCode(this.portTypes);
        result = (31 * result) + Arrays.hashCode(this.goodInput);
        result = (31 * result) + Arrays.hashCode(this.goodOutput);
        result = (31 * result) + Arrays.hashCode(this.universesIn);
        result = (31 * result) + Arrays.hashCode(this.universesOut);
        result = (31 * result) + this.video;
        result = (31 * result) + this.macro;
        result = (31 * result) + this.remote;
        result = (31 * result) + this.style;
        result = (31 * result) + Arrays.hashCode(this.macAddress);
        result = (31 * result) + this.bindIP;
        result = (31 * result) + this.bindIndex;
        result = (31 * result) + this.status2;
        return result;
    }
}
