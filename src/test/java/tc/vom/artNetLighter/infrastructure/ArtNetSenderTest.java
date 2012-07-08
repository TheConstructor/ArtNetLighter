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

package tc.vom.artNetLighter.infrastructure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

/**
 * Test-class for {@link ArtNetSender}.
 */
public class ArtNetSenderTest {

    private ArtNetSender artNetSender = null;

    @Before
    public void setUp() throws Exception {
        this.artNetSender = new ArtNetSender(0);
    }

    @After
    public void tearDown() throws Exception {
        this.artNetSender = null;
    }

    @Test
    public void testPortConstructor1() throws Exception {
        this.artNetSender = new ArtNetSender(ArtNetToolkit.MAX_PORT_ADDRESS);
        assertEquals("Net", ArtNetToolkit.MAX_NET, this.artNetSender.getNet());
        assertEquals("Sub-Net", ArtNetToolkit.MAX_SUB_NET, this.artNetSender.getSubNet());
        assertEquals("Universe", ArtNetToolkit.MAX_UNIVERSE, this.artNetSender.getUniverse());
        assertEquals("Port Address", ArtNetToolkit.MAX_UNIVERSE, this.artNetSender.getPortAddress());
    }

    @Test
    public void testPortConstructor2() throws Exception {
        this.artNetSender = new ArtNetSender(0);
        assertEquals("Net is not 0", 0, this.artNetSender.getNet());
        assertEquals("Sub-Net is not 0", 0, this.artNetSender.getSubNet());
        assertEquals("Universe is not 0", 0, this.artNetSender.getUniverse());
        assertEquals("Port Address is not 0", 0, this.artNetSender.getPortAddress());
    }

    @Test
    public void testPortConstructor3() throws Exception {
        InetAddress inetAddress = ArtNetSocketProvider.findHostAddress();
        this.artNetSender = new ArtNetSender(0, inetAddress);
        assertEquals("InetAddress", inetAddress, this.artNetSender.getInetAddress());
        assertEquals("Net is not 0", 0, this.artNetSender.getNet());
        assertEquals("Sub-Net is not 0", 0, this.artNetSender.getSubNet());
        assertEquals("Universe is not 0", 0, this.artNetSender.getUniverse());
        assertEquals("Port Address is not 0", 0, this.artNetSender.getPortAddress());
    }

    @Test
    public void testNet2() {
        this.artNetSender.setNet(2);
        assertEquals("Net is not 2", 2, this.artNetSender.getNet());
        this.artNetSender.setNet(127);
        assertEquals("Net is not 127", 127, this.artNetSender.getNet());
    }

    @Test
    public void testSubNet2() {
        this.artNetSender.setSubNet(2);
        assertEquals("Sub-Net is not 2", 2, this.artNetSender.getSubNet());
        this.artNetSender.setSubNet(15);
        assertEquals("Sub-Net is not 15", 15, this.artNetSender.getSubNet());
    }

    @Test
    public void testUniverse2() {
        this.artNetSender.setUniverse(2);
        assertEquals("Universe is not 2", 2, this.artNetSender.getUniverse());
        this.artNetSender.setUniverse(15);
        assertEquals("Universe is not 15", 15, this.artNetSender.getUniverse());
    }

    @Test
    public void testPortAddress2() {
        this.artNetSender.setPortAddress(2);
        assertEquals("Port Address is not 2", 2, this.artNetSender.getPortAddress());
        this.artNetSender.setPortAddress(32767);
        assertEquals("Port Address is not 32767", 2, this.artNetSender.getPortAddress());
    }

    @Test
    public void testInetAddress() throws Exception {
        final InetAddress inetAddress = InetAddress.getLocalHost();
        this.artNetSender.setInetAddress(inetAddress);
        assertEquals("InetAddress does not match provided InetAddress", inetAddress, this.artNetSender.getInetAddress());
    }

    @Test
    public void testInetAddressNotNull() throws Exception {
        this.artNetSender.setInetAddress(null);
        assertNotNull("InetAddress is null", this.artNetSender.getInetAddress());
    }

    @Test
    public void testToString() {
        final String result = this.artNetSender.toString();
        assertNotNull("toString() returns null", result);
        assertFalse("toString() return \"\"", "".equals(result));
    }
}
