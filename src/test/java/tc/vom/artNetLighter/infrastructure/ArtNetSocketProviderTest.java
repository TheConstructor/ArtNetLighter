package tc.vom.artNetLighter.infrastructure;


import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Test-class for {@link ArtNetSocketProvider}.
 */
public class ArtNetSocketProviderTest extends TestCase {

    public static TestSuite getTestSuite() {
        return new TestSuite(ArtNetSocketProvider.class);
    }

    public  static void main(String[] args) {
        TestRunner.run(getTestSuite());
    }

    public void testSocketNotNull() throws SocketException {
        assertNotNull("Socket is null", ArtNetSocketProvider.getArtNetSocket());
    }

    public void testSocketPort() throws SocketException {
        assertEquals("Socket port is wrong", ArtNetSocketProvider.ART_NET_PORT, ArtNetSocketProvider.getArtNetSocket().getLocalPort());
    }

    public void testInetAddressMatches() throws SocketException, UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        assertEquals("InetAddress of provided Socket does not match requested InetAddress", inetAddress, ArtNetSocketProvider.getArtNetSocket(inetAddress).getLocalAddress());
    }
}
