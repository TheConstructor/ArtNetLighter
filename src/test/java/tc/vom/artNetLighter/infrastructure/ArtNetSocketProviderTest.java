package tc.vom.artNetLighter.infrastructure;


import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test-class for {@link ArtNetSocketProvider}.
 */
public class ArtNetSocketProviderTest {

    @Test
    public void testSocketNotNull() throws Exception {
        assertNotNull("Socket is null", ArtNetSocketProvider.getArtNetSocket());
    }

    @Test
    public void testSocketPort() throws Exception {
        assertEquals("Socket port is wrong", ArtNetSocketProvider.ART_NET_PORT, ArtNetSocketProvider.getArtNetSocket().getLocalPort());
    }

    @Test
    public void testInetAddressMatches() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        assertEquals("InetAddress of provided Socket does not match requested InetAddress", inetAddress, ArtNetSocketProvider.getArtNetSocket(inetAddress).getLocalAddress());
    }
}
