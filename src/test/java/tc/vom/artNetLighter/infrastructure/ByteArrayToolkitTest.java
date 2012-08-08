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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link ByteArrayToolkit}
 */
public class ByteArrayToolkitTest {
    @Test
    public void testGet4ByteHighToLow() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        assertEquals(0x02075a91, ByteArrayToolkit.get4BytesHighToLow(data, 0));
    }

    @Test
    public void testGet4ByteLowToHigh() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        assertEquals(0x915a0702, ByteArrayToolkit.get4BytesLowToHigh(data, 0));
    }

    @Test
    public void testGet2ByteHighToLow() {
        final byte[] data = {0x5a, (byte) 0x91};
        assertEquals(0x5a91, ByteArrayToolkit.get2BytesHighToLow(data, 0));
    }

    @Test
    public void testGet2ByteLowToHigh() {
        final byte[] data = {0x5a, (byte) 0x91};
        assertEquals(0x915a, ByteArrayToolkit.get2BytesLowToHigh(data, 0));
    }

    @Test
    public void testSet4ByteHighToLow() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        final byte[] result = new byte[4];
        ByteArrayToolkit.set4BytesHighToLow(0x02075a91, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSet4ByteLowToHigh() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        final byte[] result = new byte[4];
        ByteArrayToolkit.set4BytesLowToHigh(0x915a0702, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSet2ByteHighToLow() {
        final byte[] data = {0x5a, (byte) 0x91};
        final byte[] result = new byte[2];
        ByteArrayToolkit.set2BytesHighToLow(0x5a91, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSet2ByteLowToHigh() {
        final byte[] data = {0x5a, (byte) 0x91};
        final byte[] result = new byte[2];
        ByteArrayToolkit.set2BytesLowToHigh(0x915a, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSetBytes() throws Exception {

    }

    @Test
    public void testSetString() throws Exception {

    }

    @Test
    public void testGetBytes() throws Exception {

    }

    @Test
    public void testGetString() throws Exception {

    }

    @Test
    public void testGet4BytesHighToLow() throws Exception {

    }

    @Test
    public void testGet4BytesLowToHigh() throws Exception {

    }

    @Test
    public void testGet2BytesHighToLow() throws Exception {

    }

    @Test
    public void testGet2BytesLowToHigh() throws Exception {

    }

    @Test
    public void testSet4BytesHighToLow() throws Exception {

    }

    @Test
    public void testSet4BytesLowToHigh() throws Exception {

    }

    @Test
    public void testSet2BytesHighToLow() throws Exception {

    }

    @Test
    public void testSet2BytesLowToHigh() throws Exception {

    }
}
