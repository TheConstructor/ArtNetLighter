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
        final InetAddress inetAddress = InetAddress.getLocalHost();
        assertEquals("InetAddress of provided Socket does not match requested InetAddress", inetAddress, ArtNetSocketProvider.getArtNetSocket(inetAddress).getLocalAddress());
    }
}
