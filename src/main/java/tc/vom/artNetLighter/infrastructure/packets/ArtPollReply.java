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

import tc.vom.artNetLighter.infrastructure.constants.ArtNetNodeReportCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetStyleCodes;

import java.util.Arrays;

import static tc.vom.artNetLighter.infrastructure.ArtNetToolkit.*;

/**
 * Used to hold the data of an ArtPollReply-Packet.
 */
public class ArtPollReply extends ArtNetPacket implements ArtNetStyleCodes, ArtNetNodeReportCodes {

    public static final byte[] SPARE_BYTES = {0, 0, 0};
    public static final byte[] FILLER_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static final int MIN_PACKET_LENGTH = ArtNetPacket.SHORT_HEADER_LENGTH + 203;
    public static final int PACKET_LENGTH = ArtNetPacket.SHORT_HEADER_LENGTH + 229;
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
        final byte[] shortNameBytes = shortName.getBytes(ArtNetPacket.STRING_CHARSET);
        if (longName == null) {
            longName = "";
        }
        final byte[] longNameBytes = longName.getBytes(ArtNetPacket.STRING_CHARSET);
        if (nodeReport == null) {
            nodeReport = "";
        }
        final byte[] nodeReportBytes = nodeReport.getBytes(ArtNetPacket.STRING_CHARSET);
        return ArtPollReply.constructPacket(ipAddress, port, versionInfo, net, subNet, oem, ubea, status1, estaManufacturer, shortNameBytes, longNameBytes, nodeReportBytes, numPorts, portTypes, goodInput, goodOutput, universesIn, universesOut, video, macro, remote, spare, style, macAddress, bindIP, bindIndex, status2, filler);
    }

    public static byte[] constructPacket(final int ipAddress, final int port, final int versionInfo, final byte net, final byte subNet, final int oem, final byte ubea, final byte status1, final int estaManufacturer, final byte[] shortName, final byte[] longName, final byte[] nodeReport, final int numPorts, final byte[] portTypes, final byte[] goodInput, final byte[] goodOutput, final byte[] universesIn, final byte[] universesOut, final byte video, final byte macro, final byte remote, final byte[] spare, final byte style, final byte[] macAddress, final int bindIP, final byte bindIndex, final byte status2, final byte[] filler) {
        if ((shortName.length >= ArtNetPacket.SHORT_NAME_LENGTH) && ((shortName.length != ArtNetPacket.SHORT_NAME_LENGTH) || (shortName[ArtNetPacket.SHORT_NAME_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Short Name has a maximum length of " + (ArtNetPacket.SHORT_NAME_LENGTH - 1) + " Bytes.");
        }
        if ((longName.length >= ArtNetPacket.LONG_NAME_LENGTH) && ((longName.length != ArtNetPacket.LONG_NAME_LENGTH) || (longName[ArtNetPacket.LONG_NAME_LENGTH - 1] != 0))) {
            throw new IllegalArgumentException("Long Name has a maximum length of " + (ArtNetPacket.LONG_NAME_LENGTH - 1) + " Bytes.");
        }
        if ((nodeReport.length > 63) && ((nodeReport.length != 64) || (nodeReport[63] != 0))) {
            throw new IllegalArgumentException("Node Report has a maximum length of 63 Bytes.");
        }
        if (numPorts > 4) {
            throw new IllegalArgumentException("Maximum Port-Count is 4");
        }
        if (portTypes.length > 4) {
            throw new IllegalArgumentException("Maximum portTypes.length is 4");
        }
        if (goodInput.length > 4) {
            throw new IllegalArgumentException("Maximum goodInput.length is 4");
        }
        if (goodOutput.length > 4) {
            throw new IllegalArgumentException("Maximum goodOutput.length is 4");
        }
        if (universesIn.length > 4) {
            throw new IllegalArgumentException("Maximum universesIn.length is 4");
        }
        if (universesOut.length > 4) {
            throw new IllegalArgumentException("Maximum universesOut.length is 4");
        }
        if (spare.length > 3) {
            throw new IllegalArgumentException("Maximum spare.length is 3");
        }
        if (macAddress.length > 6) {
            throw new IllegalArgumentException("Maximum macAddress.length is 6");
        }
        if (filler.length > 26) {
            throw new IllegalArgumentException("Maximum filler.length is 26");
        }

        final byte[] result = ArtNetPacket.constructUnversionedPacket(ArtPollReply.PACKET_LENGTH, ArtNetOpCodes.OP_CODE_POLL_REPLY);

        set4BytesHighToLow(ipAddress, result, ArtNetPacket.SHORT_HEADER_LENGTH);
        set2BytesLowToHigh(port, result, ArtNetPacket.SHORT_HEADER_LENGTH + 4);
        set2BytesHighToLow(versionInfo, result, ArtNetPacket.SHORT_HEADER_LENGTH + 6);
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 8] = (byte) (net & 0x7f);
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 9] = (byte) (subNet & 0x0f);
        set2BytesHighToLow(oem, result, ArtNetPacket.SHORT_HEADER_LENGTH + 10);
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 12] = ubea;
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 13] = status1;
        set2BytesLowToHigh(estaManufacturer, result, ArtNetPacket.SHORT_HEADER_LENGTH + 14);
        copyBytesToArray(shortName, result, ArtNetPacket.SHORT_HEADER_LENGTH + 16);
        copyBytesToArray(longName, result, ArtNetPacket.SHORT_HEADER_LENGTH + 34);
        copyBytesToArray(nodeReport, result, ArtNetPacket.SHORT_HEADER_LENGTH + 98);
        set2BytesHighToLow(numPorts, result, ArtNetPacket.SHORT_HEADER_LENGTH + 162);
        copyBytesToArray(portTypes, result, ArtNetPacket.SHORT_HEADER_LENGTH + 164);
        copyBytesToArray(goodInput, result, ArtNetPacket.SHORT_HEADER_LENGTH + 168);
        copyBytesToArray(goodOutput, result, ArtNetPacket.SHORT_HEADER_LENGTH + 172);
        copyBytesToArray(universesIn, result, ArtNetPacket.SHORT_HEADER_LENGTH + 176);
        copyBytesToArray(universesOut, result, ArtNetPacket.SHORT_HEADER_LENGTH + 180);
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 184] = video;
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 185] = macro;
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 186] = remote;
        copyBytesToArray(spare, result, ArtNetPacket.SHORT_HEADER_LENGTH + 187);
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 190] = style;
        copyBytesToArray(macAddress, result, ArtNetPacket.SHORT_HEADER_LENGTH + 191);
        set4BytesHighToLow(bindIP, result, ArtNetPacket.SHORT_HEADER_LENGTH + 197);
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 201] = bindIndex;
        result[ArtNetPacket.SHORT_HEADER_LENGTH + 202] = status2;
        copyBytesToArray(filler, result, ArtNetPacket.SHORT_HEADER_LENGTH + 203);
        return result;
    }

    public ArtPollReply(final byte[] data) {
        super(data);
        if (data.length < ArtPoll.PACKET_LENGTH) {
            throw new IllegalArgumentException("Minimum size for ArtPollReply is " + ArtPollReply.PACKET_LENGTH);
        }
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_POLL_REPLY) {
            throw new IllegalArgumentException("Provided data specifies a wrong OpCode");
        }

        this.ipAddress = get4BytesHighToLow(data, ArtNetPacket.SHORT_HEADER_LENGTH);
        this.port = get2BytesLowToHigh(data, ArtNetPacket.SHORT_HEADER_LENGTH + 4);
        this.versionInfo = get2BytesHighToLow(data, ArtNetPacket.SHORT_HEADER_LENGTH + 6);
        this.net = (byte) (data[ArtNetPacket.SHORT_HEADER_LENGTH + 8] & 0x7f);
        this.subNet = (byte) (data[ArtNetPacket.SHORT_HEADER_LENGTH + 9] & 0x0f);
        this.oem = get2BytesHighToLow(data, ArtNetPacket.SHORT_HEADER_LENGTH + 10);
        this.ubea = data[ArtNetPacket.SHORT_HEADER_LENGTH + 12];
        this.status1 = data[ArtNetPacket.SHORT_HEADER_LENGTH + 13];
        this.estaManufacturer = get2BytesLowToHigh(data, ArtNetPacket.SHORT_HEADER_LENGTH + 14);
        this.shortName = copyStringFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 16, 17);
        this.longName = copyStringFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 34, 63);
        this.nodeReport = copyStringFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 98, 63);
        this.numPorts = get2BytesHighToLow(data, ArtNetPacket.SHORT_HEADER_LENGTH + 162);
        this.portTypes = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 164, 4);
        this.goodInput = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 168, 4);
        this.goodOutput = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 172, 4);
        this.universesIn = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 176, 4);
        this.universesOut = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 180, 4);
        this.video = data[ArtNetPacket.SHORT_HEADER_LENGTH + 184];
        this.macro = data[ArtNetPacket.SHORT_HEADER_LENGTH + 185];
        this.remote = data[ArtNetPacket.SHORT_HEADER_LENGTH + 186];
        this.spare = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 187, 3);
        this.style = data[ArtNetPacket.SHORT_HEADER_LENGTH + 190];
        this.macAddress = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 191, 6);
        if (data.length > (ArtNetPacket.SHORT_HEADER_LENGTH + 201)) {
            this.bindIP = get4BytesHighToLow(data, ArtNetPacket.SHORT_HEADER_LENGTH + 197);
            this.bindIndex = data[ArtNetPacket.SHORT_HEADER_LENGTH + 201];
        } else {
            this.bindIP = 0;
            this.bindIndex = 0;
        }
        if (data.length > (ArtNetPacket.SHORT_HEADER_LENGTH + 202)) {
            this.status2 = data[ArtNetPacket.SHORT_HEADER_LENGTH + 202];
        } else {
            this.status2 = 0;
        }
        if (data.length > (ArtNetPacket.SHORT_HEADER_LENGTH + 203)) {
            this.filler = copyBytesFromArray(data, ArtNetPacket.SHORT_HEADER_LENGTH + 203);
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
