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
 * Contains the Art-Net Style Codes used by {@link tc.vom.artNetLighter.infrastructure.packets.ArtPollReply}
 */
@SuppressWarnings("UnusedDeclaration")
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
