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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test Class for {@link _ArtNetPacket}
 */
public class ArtNetPacketTest {
    private _ArtNetPacket artNetPacket = null;

    @Before
    public void setUp() throws Exception {
        this.artNetPacket = new _ArtNetPacket(ArtNetOpCodes.OP_CODE_POLL) {
            @Override
            public byte[] constructPacket() {
                return _ArtNetPacket.constructUnversionedPacket(_ArtNetPacket.SHORT_HEADER_LENGTH, ArtNetOpCodes.OP_CODE_POLL);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        this.artNetPacket = null;
    }

    @Test
    public void testGetOpCode() throws Exception {
        assertEquals("Op Code was set to OP_POLL", ArtNetOpCodes.OP_CODE_POLL, this.artNetPacket.getOpCode());
    }

    @Test
    public void testConstructPacket1() throws Exception {
        assertArrayEquals("constructUnversionedPacket should do the same as the static call", this.artNetPacket.constructPacket(), _ArtNetPacket.constructUnversionedPacket(_ArtNetPacket.SHORT_HEADER_LENGTH, ArtNetOpCodes.OP_CODE_POLL));
    }

    @Test
    public void testConstructPacket2() throws Exception {
        assertArrayEquals("constructUnversionedPacket(HEADER_LENGTH, OP_POLL) should generate a valid OP_POLL header", new byte[]{'A', 'r', 't', '-', 'N', 'e', 't', 0, ArtNetOpCodes.OP_CODE_POLL & 0xff, (ArtNetOpCodes.OP_CODE_POLL >> 8) & 0xff}, _ArtNetPacket.constructUnversionedPacket(_ArtNetPacket.SHORT_HEADER_LENGTH, ArtNetOpCodes.OP_CODE_POLL));
    }
}
