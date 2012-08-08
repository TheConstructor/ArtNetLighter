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
import tc.vom.artNetLighter.infrastructure.packets._ArtNetPacket;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test for {@link ByteArrayToolkit}
 */
public class ByteArrayToolkitTest {
    @Test
    public void testGet4BytesHighToLow() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        assertEquals(0x02075a91, ByteArrayToolkit.get4BytesHighToLow(data, 0));
    }

    @Test
    public void testGet4BytesLowToHigh() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        assertEquals(0x915a0702, ByteArrayToolkit.get4BytesLowToHigh(data, 0));
    }

    @Test
    public void testGet2BytesHighToLow() {
        final byte[] data = {0x5a, (byte) 0x91};
        assertEquals(0x5a91, ByteArrayToolkit.get2BytesHighToLow(data, 0));
    }

    @Test
    public void testGet2BytesLowToHigh() {
        final byte[] data = {0x5a, (byte) 0x91};
        assertEquals(0x915a, ByteArrayToolkit.get2BytesLowToHigh(data, 0));
    }

    @Test
    public void testSet4BytesHighToLow() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        final byte[] result = new byte[4];
        ByteArrayToolkit.set4BytesHighToLow(0x02075a91, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSet4BytesLowToHigh() {
        final byte[] data = {0x02, 0x07, 0x5a, (byte) 0x91};
        final byte[] result = new byte[4];
        ByteArrayToolkit.set4BytesLowToHigh(0x915a0702, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSet2BytesHighToLow() {
        final byte[] data = {0x5a, (byte) 0x91};
        final byte[] result = new byte[2];
        ByteArrayToolkit.set2BytesHighToLow(0x5a91, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSet2BytesLowToHigh() {
        final byte[] data = {0x5a, (byte) 0x91};
        final byte[] result = new byte[2];
        ByteArrayToolkit.set2BytesLowToHigh(0x915a, result, 0);
        assertArrayEquals(data, result);
    }

    @Test
    public void testSetBytes() throws Exception {
        final byte[][] input = {
                new byte[]{0x00, 0x00},
                new byte[]{0x00, (byte) 0xff},
                new byte[]{(byte) 0xff, 0x00},
                new byte[]{(byte) 0xff, (byte) 0xff}
        };
        final byte[][][] result = {
                new byte[][]{
                        new byte[]{0x00, 0x00, 0x00, 0x00},
                        new byte[]{0x00, (byte) 0xff, 0x00, 0x00},
                        new byte[]{(byte) 0xff, 0x00, 0x00, 0x00},
                        new byte[]{(byte) 0xff, (byte) 0xff, 0x00, 0x00}
                },
                new byte[][]{
                        new byte[]{0x00, 0x00, 0x00, 0x00},
                        new byte[]{0x00, 0x00, (byte) 0xff, 0x00},
                        new byte[]{0x00, (byte) 0xff, 0x00, 0x00},
                        new byte[]{0x00, (byte) 0xff, (byte) 0xff, 0x00}
                },
                new byte[][]{
                        new byte[]{0x00, 0x00, 0x00, 0x00},
                        new byte[]{0x00, 0x00, 0x00, (byte) 0xff},
                        new byte[]{0x00, 0x00, (byte) 0xff, 0x00},
                        new byte[]{0x00, 0x00, (byte) 0xff, (byte) 0xff}
                }
        };
        for (int offset = 0; offset < 3; offset++) {
            for (int i = 0; i < input.length; i++) {
                final byte[] target = new byte[4];
                ByteArrayToolkit.setBytes(input[i], target, offset);
                assertArrayEquals("setBytes(" + Arrays.toString(input[i]) + "," + Arrays.toString(target) + "," + offset + ")", result[offset][i], target);
            }
        }
    }

    @Test
    public void testGetBytes() throws Exception {
        final byte[][][] input = {
                new byte[][]{
                        new byte[]{0x00, 0x00, 0x00, 0x00},
                        new byte[]{0x00, (byte) 0xff, 0x00, 0x00},
                        new byte[]{(byte) 0xff, 0x00, 0x00, 0x00},
                        new byte[]{(byte) 0xff, (byte) 0xff, 0x00, 0x00}
                },
                new byte[][]{
                        new byte[]{0x00, 0x00, 0x00, 0x00},
                        new byte[]{0x00, 0x00, (byte) 0xff, 0x00},
                        new byte[]{0x00, (byte) 0xff, 0x00, 0x00},
                        new byte[]{0x00, (byte) 0xff, (byte) 0xff, 0x00}
                },
                new byte[][]{
                        new byte[]{0x00, 0x00, 0x00, 0x00},
                        new byte[]{0x00, 0x00, 0x00, (byte) 0xff},
                        new byte[]{0x00, 0x00, (byte) 0xff, 0x00},
                        new byte[]{0x00, 0x00, (byte) 0xff, (byte) 0xff}
                }
        };
        final byte[][] result = {
                new byte[]{0x00, 0x00},
                new byte[]{0x00, (byte) 0xff},
                new byte[]{(byte) 0xff, 0x00},
                new byte[]{(byte) 0xff, (byte) 0xff}
        };
        for (int offset = 0; offset < 3; offset++) {
            for (int i = 0; i < input.length; i++) {
                final byte[] target = ByteArrayToolkit.getBytes(input[offset][i], offset, 2);
                assertArrayEquals("getBytes(" + Arrays.toString(input[offset][i]) + "," + offset + ",2)", result[i], target);
            }
        }
        for (int i = 0; i < input.length; i++) {
            final byte[] target = ByteArrayToolkit.getBytes(input[2][i], 2);
            assertArrayEquals("getBytes(" + Arrays.toString(input[2][i]) + ",2)", result[i], target);
        }
    }

    @Test
    public void testGetString() throws Exception {
        assertEquals("getString(_ArtNetPackage.ART_NET_ID, 0, _ArtNetPacket.ART_NET_ID.length)", "Art-Net", ByteArrayToolkit.getString(_ArtNetPacket.ART_NET_ID, 0, _ArtNetPacket.ART_NET_ID.length));
    }

    @Test
    public void testSetString() throws Exception {
        byte[] target = new byte[_ArtNetPacket.ART_NET_ID.length];
        ByteArrayToolkit.setString("Art-Net", target, 0, _ArtNetPacket.ART_NET_ID.length);
        assertArrayEquals("setString(\"Art-Net\", new byte[_ArtNetPacket.ART_NET_ID.length], 0, _ArtNetPacket.ART_NET_ID.length)", _ArtNetPacket.ART_NET_ID, target);

        try {
            target = new byte[_ArtNetPacket.ART_NET_ID.length - 2];
            ByteArrayToolkit.setString("Art-Net", target, 0, _ArtNetPacket.ART_NET_ID.length);
            fail("setString(\"Art-Net\", new byte[_ArtNetPacket.ART_NET_ID.length - 2], 0, _ArtNetPacket.ART_NET_ID.length)");
        } catch (Exception e) {
            // Expected
        }

        try {
            target = new byte[_ArtNetPacket.ART_NET_ID.length];
            ByteArrayToolkit.setString("Art-Net", target, 0, _ArtNetPacket.ART_NET_ID.length - 2);
            fail("setString(\"Art-Net\", new byte[_ArtNetPacket.ART_NET_ID.length], 0, _ArtNetPacket.ART_NET_ID.length - 2)");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testGetShorts() throws Exception {
        final byte[] input = {0x77, 0x00, 0x00, 0x00, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xff, (byte) 0xff};
        final short[] result = {0x0000, 0x00ff, (short) 0xff00, (short) 0xffff};
        assertArrayEquals("getShorts", result, ByteArrayToolkit.getShorts(input, 1));
    }

    @Test
    public void testSetShorts() throws Exception {
        final short[] input = {0x0000, 0x00ff, (short) 0xff00, (short) 0xffff};
        final byte[] result = {0x77, 0x00, 0x00, 0x00, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xff, (byte) 0xff};
        final byte[] target = new byte[result.length];
        Arrays.fill(target, (byte) 0x77);
        ByteArrayToolkit.setShorts(input, target, 1);
        assertArrayEquals("setShorts", result, target);
    }
}
