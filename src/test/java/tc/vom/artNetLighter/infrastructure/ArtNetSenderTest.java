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
        artNetSender = new ArtNetSender(0x7FFF);
    }

    @After
    public void tearDown() throws Exception {
        artNetSender = null;
    }

    @Test
    public void testNet() {
        assertEquals("Net is not 127", 127, artNetSender.getNet());
    }

    @Test
    public void testNet2() {
        artNetSender.setNet(2);
        assertEquals("Net is not 2", 2, artNetSender.getNet());
    }

    @Test
    public void testSubNet() {
        assertEquals("Sub-Net is not 15", 15, artNetSender.getSubNet());
    }

    @Test
    public void testSubNet2() {
        artNetSender.setSubNet(2);
        assertEquals("Sub-Net is not 2", 2, artNetSender.getSubNet());
    }

    @Test
    public void testUniverse() {
        assertEquals("Universe is not 15", 15, artNetSender.getUniverse());
    }

    @Test
    public void testUniverse2() {
        artNetSender.setUniverse(2);
        assertEquals("Universe is not 2", 2, artNetSender.getUniverse());
    }

    @Test
    public void testPortAddress() {
        assertEquals("Port Address is not 32767", 32767, artNetSender.getPortAddress());
    }

    @Test
    public void testPortAddress2() {
        artNetSender.setPortAddress(2);
        assertEquals("Port Address is not 2", 2, artNetSender.getUniverse());
    }

    @Test
    public void testInetAddress() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        artNetSender.setInetAddress(inetAddress);
        assertEquals("InetAddress does not match provided InetAddress", inetAddress, artNetSender.getInetAddress());
    }

    @Test
    public void testInetAddressNotNull() throws Exception {
        artNetSender.setInetAddress(null);
        assertNotNull("InetAddress is null", artNetSender.getInetAddress());
    }

    @Test
    public void testToString() {
        String result = artNetSender.toString();
        assertNotNull("toString() returns null", result);
        assertFalse("toString() return \"\"", "".equals(result));
    }
}
