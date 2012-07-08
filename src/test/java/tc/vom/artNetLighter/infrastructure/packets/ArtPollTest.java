package tc.vom.artNetLighter.infrastructure.packets;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link ArtPoll}
 */
public class ArtPollTest {

    public static final byte[] captured_data1 = new byte[]{0x41, 0x72, 0x74, 0x2d, 0x4e, 0x65, 0x74, 0x00, 0x00, 0x20, 0x00, 0x0e, 0x06, 0x00};

    @Test
    public void testConstructorBytes() {
        ArtPoll artPoll = new ArtPoll(captured_data1);
        assertEquals("ProtocolVersion", ArtNetPacket.PROTOCOL_VERSION, artPoll.getProtocolVersion());
        assertEquals("TalkToMe", 0x06, artPoll.getTalkToMe());
        assertEquals("Priority", 0x00, artPoll.getPriority());
    }

    @Test
    public void testConstructPacket() {
        byte[] packet = ArtPoll.constructPacket(0x06, 0x00);
        assertArrayEquals("Generated packet does not match captured package", captured_data1, packet);
    }
}
