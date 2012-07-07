package tc.vom.artNetLighter.infrastructure.constants;

/**
 * Contains the Art-Net Style Codes used by {@link tc.vom.artNetLighter.infrastructure.packets.ArtPollReply}
 */
public interface ArtNetStyleCodes {
    /**
     * A DMX to / from Art-Net device
     */
    int STYLE_CODE_NODE = 0x00;
    /**
     * A lighting console.
     */
    int STYLE_CODE_CONTROLLER = 0x01;
    /**
     * A Media Server.
     */
    int STYLE_CODE__MEDIA = 0x02;
    /**
     * A network routing device.
     */
    int STYLE_CODE_ROUTE = 0x03;
    /**
     * A backup device.
     */
    int STYLE_CODE__BACKUP = 0x04;
    /**
     * A configuration or diagnostic tool.
     */
    int STYLE_CODE_CONFIG = 0x05;
    /**
     * A visualiser.
     */
    int STYLE_CODE_VISUAL = 0x06;
}
