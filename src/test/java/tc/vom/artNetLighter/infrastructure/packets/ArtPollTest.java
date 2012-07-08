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

import org.junit.Test;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link ArtPoll}
 */
public class ArtPollTest {

    public static final byte[] captured_data1 = {0x41, 0x72, 0x74, 0x2d, 0x4e, 0x65, 0x74, 0x00, 0x00, 0x20, 0x00, 0x0e, 0x06, 0x00};

    @Test
    public void testConstructorBytes() {
        final ArtPoll artPoll = new ArtPoll(ArtPollTest.captured_data1);
        assertEquals("OpCode", ArtNetOpCodes.OP_CODE_POLL, artPoll.getOpCode());
        assertEquals("ProtocolVersion", ArtNetPacket.PROTOCOL_VERSION, artPoll.getProtocolVersion());
        assertEquals("TalkToMe", 0x06, artPoll.getTalkToMe());
        assertEquals("Priority", 0x00, artPoll.getPriority());
    }

    @Test
    public void testConstructPacket() {
        final ArtPoll artPoll = new ArtPoll(0x06, 0x00);
        assertArrayEquals("Generated packet does not match captured package", ArtPollTest.captured_data1, artPoll.constructPacket());
    }
}
