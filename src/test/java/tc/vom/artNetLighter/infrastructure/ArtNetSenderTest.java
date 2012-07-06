package tc.vom.artNetLighter.infrastructure;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Test-class for {@link ArtNetSender}.
 */
public class ArtNetSenderTest extends TestCase {

    public static TestSuite getTestSuite() {
        return new TestSuite(ArtNetSenderTest.class);
    }

    public  static void main(String[] args) {
        TestRunner.run(getTestSuite());
    }

    private ArtNetSender artNetSender = null;

    protected void setUp() throws Exception {
        super.setUp();
        artNetSender = new ArtNetSender(0x7FFF);
    }

    protected void tearDown() throws Exception {
        artNetSender = null;
        super.tearDown();
    }

    public void testNet() {
        assertEquals("Net is not 127", 127, artNetSender.getNet());
    }

    public void testNet2() {
        artNetSender.setNet(2);
        assertEquals("Net is not 2", 2, artNetSender.getNet());
    }

    public void testSubNet() {
        assertEquals("Sub-Net is not 15", 15, artNetSender.getSubNet());
    }

    public void testSubNet2() {
        artNetSender.setSubNet(2);
        assertEquals("Sub-Net is not 2", 2, artNetSender.getSubNet());
    }

    public void testUniverse() {
        assertEquals("Universe is not 15", 15, artNetSender.getUniverse());
    }

    public void testUniverse2() {
        artNetSender.setUniverse(2);
        assertEquals("Universe is not 2", 2, artNetSender.getUniverse());
    }

    public void testPortAddress() {
        assertEquals("Port Address is not 32767", 32767, artNetSender.getPortAddress());
    }

    public void testPortAddress2() {
        artNetSender.setPortAddress(2);
        assertEquals("Port Address is not 2", 2, artNetSender.getUniverse());
    }

    public void testInetAddress() throws UnknownHostException, SocketException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        artNetSender.setInetAddress(inetAddress);
        assertEquals("InetAddress does not match provided InetAddress", inetAddress, artNetSender.getInetAddress());
    }

    public void testInetAddressNotNull() throws UnknownHostException, SocketException {
        artNetSender.setInetAddress(null);
        assertNotNull("InetAddress is null", artNetSender.getInetAddress());
    }

    public void testToString() {
        String result = artNetSender.toString();
        assertNotNull("toString() returns null", result);
        assertFalse("toString() return \"\"", "".equals(result));
    }
}
