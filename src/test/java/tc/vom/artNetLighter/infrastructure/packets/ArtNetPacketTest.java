package tc.vom.artNetLighter.infrastructure.packets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test Class for {@link ArtNetPacket}
 */
public class ArtNetPacketTest {
    private ArtNetPacket artNetPacket = null;

    @Before
    public void setUp() throws Exception {
        artNetPacket = new ArtNetPacket(ArtNetOpCodes.OP_CODE_POLL) {
            @Override
            public byte[] constructPacket() {
                return ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH, ArtNetOpCodes.OP_CODE_POLL);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        artNetPacket = null;
    }

    @Test
    public void testGetOpCode() throws Exception {
        assertEquals("Op Code was set to OP_POLL", ArtNetOpCodes.OP_CODE_POLL, artNetPacket.getOpCode());
    }

    @Test
    public void testConstructPacket1() throws Exception {
        assertArrayEquals("constructPacket should do the same as the static call", artNetPacket.constructPacket(), ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH, ArtNetOpCodes.OP_CODE_POLL));
    }

    @Test
    public void testConstructPacket2() throws Exception {
        assertArrayEquals("constructPacket(HEADER_LENGTH, OP_POLL) should generate a valid OP_POLL header", new byte[]{'A', 'r', 't', '-', 'N', 'e', 't', 0, ArtNetOpCodes.OP_CODE_POLL & 0xff, (ArtNetOpCodes.OP_CODE_POLL >> 8) & 0xff}, ArtNetPacket.constructPacket(ArtNetPacket.HEADER_LENGTH, ArtNetOpCodes.OP_CODE_POLL));
    }
}
