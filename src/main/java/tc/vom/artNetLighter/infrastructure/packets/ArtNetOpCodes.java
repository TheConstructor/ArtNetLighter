package tc.vom.artNetLighter.infrastructure.packets;

/**
 * Holds all OpCode constants.
 */
public interface ArtNetOpCodes {
    /**
     * This is an ArtPoll packet, no other data is contained in this UDP
     * packet.
     */
    int OP_CODE_POLL = 0x2000;
    /**
     * This is an ArtPollReply Packet. It contains device status information.
     */
    int OP_CODE_POLL_REPLY = 0x2100;
    /**
     * Diagnostics and data logging packet.
     */
    int OP_CODE_DIAG_DATA = 0x2300;
    /**
     * Used to send text based parameter commands.
     */
    int OP_CODE_COMMAND = 0x2400;
    /**
     * This is an ArtDmx data packet. It contains zero start code DMX512 information for a single Universe.
     */
    int OP_CODE_DMX = 0x5000;
    /**
     * This is an ArtNzs data packet. It contains non-zero start code (except RDM) DMX512 information for a single Universe.
     */
    int OP_CODE_NZS = 0x5100;
    /**
     * This is an ArtAddress packet. It contains remote programming information for a Node.
     */
    int OP_CODE_ADDRESS = 0x6000;
    /**
     * This is an ArtInput packet. It contains enable – disable data for DMX inputs.
     */
    int OP_CODE_INPUT = 0x7000;
    /**
     * This is an ArtTodRequest packet. It is used to request a Table of Devices (ToD) for RDM discovery.
     */
    int OP_CODE_TOD_REQUEST = 0x8000;
    /**
     * This is an ArtTodData packet. It is used to send a Table of Devices (ToD) for RDM discovery.
     */
    int OP_CODE_TOD_DATA = 0x8100;
    /**
     * This is an ArtTodControl packet. It is used to send RDM discovery control messages.
     */
    int OP_CODE_TOD_CONTROL = 0x8200;
    /**
     * This is an ArtRdm packet. It is used to send all non discovery RDM messages.
     */
    int OP_CODE_RDM = 0x8300;
    /**
     * This is an ArtRdmSub packet. It is used to send compressed, RDM Sub-Device data.
     */
    int OP_CODE_RDM_SUB = 0x8400;
    /**
     * This is an ArtVideoSetup packet. It contains video screen setup information for nodes that implement the extended video features.
     */
    int OP_CODE_VIDEO_SETUP = 0xa010;
    /**
     * This is an ArtVideoPalette packet. It contains colour palette setup information for nodes that implement the extended video features.
     */
    int OP_CODE_VIDEO_PALETTE = 0xa020;
    /**
     * This is an ArtVideoData packet. It contains display data for nodes that implement the extended video features.
     */
    int OP_CODE_VIDEO_DATA = 0xa040;
    /**
     * This is an ArtMacMaster packet. It is used to program the Node’s MAC address, Oem device type and ESTA manufacturer code.
     * This is for factory initialisation of a Node. It is not to be used by applications.
     */
    int OP_CODE_MAC_MASTER = 0xf000;
    /**
     * This is an ArtMacSlave packet. It is returned by the node to acknowledge receipt of an ArtMacMaster packet.
     */
    int OP_CODE_MAC_SLAVE = 0xf100;
    /**
     * This is an ArtFirmwareMaster packet. It is used to upload new firmware or firmware extensions to the Node.
     */
    int OP_CODE_FIRMWARE_MASTER = 0xf200;
    /**
     * This is an ArtFirmwareReply packet. It is returned by the node to acknowledge receipt of an ArtFirmwareMaster packet or ArtFileTnMaster packet.
     */
    int OP_CODE_FIRMWARE_REPLY = 0xf300;
    /**
     * Uploads user file to node.
     */
    int OP_CODE_FILE_TN_MASTER = 0xf400;
    /**
     * Downloads user file from node.
     */
    int OP_CODE_FILE_FN_MASTER = 0xf500;
    /**
     * Node acknowledge for downloads.
     */
    int OP_CODE_FILE_FN_REPLY = 0xf600;
    /**
     * This is an ArtIpProg packet. It is used to reprogramme the IP, Mask and Port address of the Node.
     */
    int OP_CODE_IP_PROG = 0xf800;
    /**
     * This is an ArtIpProgReply packet. It is returned by the node to acknowledge receipt of an ArtIpProg packet.
     */
    int OP_CODE_IP_PROG_REPLY = 0xf900;
    /**
     * This is an ArtMedia packet. It is Unicast by a Media Server and acted upon by a Controller.
     */
    int OP_CODE_MEDIA = 0x9000;
    /**
     * This is an ArtMediaPatch packet. It is Unicast by a Controller and acted upon by a Media Server.
     */
    int OP_CODE_MEDIA_PATCH = 0x9100;
    /**
     * This is an ArtMediaControl packet. It is Unicast by a Controller and acted upon by a Media Server.
     */
    int OP_CODE_MEDIA_CONTROL = 0x9200;
    /**
     * This is an ArtMediaControlReply packet. It is Unicast by a Media Server and acted upon by a Controller.
     */
    int OP_CODE_MEDIA_CONTRL_REPLY = 0x9300;
    /**
     * This is an ArtTimeCode packet. It is used to transport time code over the network.
     */
    int OP_CODE_TIME_CODE = 0x9700;
    /**
     * Used to synchronise real time date and clock
     */
    int OP_CODE_TIME_SYNC = 0x9800;
    /**
     * Used to send trigger macros
     */
    int OP_CODE_TRIGGER = 0x9900;
    /**
     * Requests a node's file list
     */
    int OP_CODE_DIRECTORY = 0x9a00;
    /**
     * Replies to OpDirectory with file list
     */
    int OP_CODE_DIRECTORY_REPLY = 0x9b00;
}
