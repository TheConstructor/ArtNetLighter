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

import static org.junit.Assert.*;

/**
 * Test for {@link BinaryToolkit}
 */
public class BinaryToolkitTest {

    @Test
    public void test_CastIntToByte() throws Exception {
        final int[] input = {0, -65536, 65535, -1}; // same as 0x00000000, 0xffff0000, 0x0000ffff, 0xffffffff for signed int
        final byte[] result = {0, 0, -1, -1}; // same as 0x00, 0x00, 0xff, 0xff for signed byte
        for (int i = 0; i < input.length; i++) {
            assertEquals("(byte)" + input[i], result[i], (byte) input[i]);
        }
    }

    @Test
    public void test_CastByteToInt() throws Exception {
        final byte[] input = {0, -1}; // same as 0x00, 0xff for signed byte
        final int[] straigt_cast_result = {0, -1}; // same as 0x00000000, 0xffffffff for signed int
        final int[] binary_cast_result = {0, 255}; // same as 0x00000000, 0x000000ff for signed int
        for (int i = 0; i < input.length; i++) {
            assertEquals("(int)" + input[i], straigt_cast_result[i], input[i]);
        }
        for (int i = 0; i < input.length; i++) {
            assertEquals(input[i] + " & 0xff", binary_cast_result[i], input[i] & 0xff);
        }
    }

    @Test
    public void testBitMask() throws Exception {
        String lead = "";
        for (int i = 0; i < BinaryToolkit.BIT_MASKS.length; i++) {
            final StringBuilder sb = new StringBuilder(BinaryToolkit.BIT_MASKS.length);
            sb.append(lead);
            for (int j = 0; j < BinaryToolkit.BIT_MASKS[i].length; j++) {
                final String observed = Integer.toBinaryString(BinaryToolkit.BIT_MASKS[i][j]);
                if (j < i) {
                    assertEquals("Binary Mask for Bits [a,b] with b < a should be 0", "0", observed);
                } else {
                    sb.insert(0, '1');
                    final String expected = sb.toString();
                    assertEquals("Binary Mask for Bits [a,b] with a=" + i + ",b=" + j + " should be " + expected, expected, observed);
                }
            }
            lead += "0";
        }
    }

    @Test
    public void testGetBits() throws Exception {
        String lead = "";
        for (int i = 0; i <= Integer.highestOneBit(-1); i++) {
            final StringBuilder sb = new StringBuilder(32);
            sb.append(lead);
            for (int j = 0; j <= Integer.highestOneBit(-1); j++) {
                if (j < i) {
                    try {
                        BinaryToolkit.getBits(-1, i, j);
                        fail("getBits for Bits [a,b] with b < a should throw an IllegalArgumentException");
                    } catch (IllegalArgumentException e) {
                        // ok.
                    }
                } else {
                    final String observed = Integer.toBinaryString(BinaryToolkit.getBits(-1, i, j));
                    sb.insert(0, '1');
                    final String expected = sb.toString();
                    assertEquals("getBits for Bits [a,b] with a=" + i + ",b=" + j, expected, observed);
                }
            }
            lead += "0";
        }
    }

    @Test
    public void testGenerateBitRangeMask() throws Exception {
        String lead = "";
        for (int i = 0; i <= Integer.highestOneBit(-1); i++) {
            final StringBuilder sb = new StringBuilder(32);
            sb.append(lead);
            for (int j = 0; j <= Integer.highestOneBit(-1); j++) {
                if (j < i) {
                    try {
                        BinaryToolkit.generateBitRangeMask(i, j);
                        fail("generateBitRangeMask for Bits [a,b] with b < a should throw an IllegalArgumentException");
                    } catch (IllegalArgumentException e) {
                        // ok.
                    }
                } else {
                    final String observed = Integer.toBinaryString(BinaryToolkit.generateBitRangeMask(i, j));
                    sb.insert(0, '1');
                    final String expected = sb.toString();
                    assertEquals("generateBitRangeMask for Bits [a,b] with a=" + i + ",b=" + j, expected, observed);
                }
            }
            lead += "0";
        }
    }

    @Test
    public void testIsBitSet() throws Exception {
        for (int i = 0; i < 8; i++) {
            assertTrue("isBitSet(-1, " + i + ")", BinaryToolkit.isBitSet(-1, i));
        }
        for (int i = 0; i < 8; i++) {
            assertFalse("isBitSet(0, \"+i+\")", BinaryToolkit.isBitSet(0, i));
        }
        for (int j = 0; j < 8; j++) {
            final int input = 1 << j;
            for (int i = 0; i < 8; i++) {
                assertEquals("isBitSet(" + input + ", \"+i+\")", i == j, BinaryToolkit.isBitSet(input, i));
            }
        }
    }

    @Test
    public void testSetBit() throws Exception {
        final int[] input = {
                0x00,
                0xf0,
                0x0f,
                0xff
        };
        final int[][] result = {
                new int[]{
                        0x01,
                        0xf1,
                        0x0f,
                        0xff
                },
                new int[]{
                        0x02,
                        0xf2,
                        0x0f,
                        0xff
                },
                new int[]{
                        0x04,
                        0xf4,
                        0x0f,
                        0xff
                },
                new int[]{
                        0x08,
                        0xf8,
                        0x0f,
                        0xff
                },
                new int[]{
                        0x10,
                        0xf0,
                        0x1f,
                        0xff
                },
                new int[]{
                        0x20,
                        0xf0,
                        0x2f,
                        0xff
                },
                new int[]{
                        0x40,
                        0xf0,
                        0x4f,
                        0xff
                },
                new int[]{
                        0x80,
                        0xf0,
                        0x8f,
                        0xff
                }
        };
        for (int bit = 0; bit < 8; bit++) {
            for (int i = 0; i < input.length; i++) {
                assertEquals("setBit(" + input[i] + ", " + bit + ")", result[bit][i], BinaryToolkit.setBit(input[i], bit));
            }
        }
    }

    @Test
    public void testToggleBit() throws Exception {
        final int[] input = {
                0x00,
                0xf0,
                0x0f,
                0xff
        };
        final int[][] result = {
                new int[]{
                        0x01,
                        0xf1,
                        0x0e,
                        0xfe
                },
                new int[]{
                        0x02,
                        0xf2,
                        0x0d,
                        0xfd
                },
                new int[]{
                        0x04,
                        0xf4,
                        0x0b,
                        0xfb
                },
                new int[]{
                        0x08,
                        0xf8,
                        0x07,
                        0xf7
                },
                new int[]{
                        0x10,
                        0xe0,
                        0x1f,
                        0xef
                },
                new int[]{
                        0x20,
                        0xd0,
                        0x2f,
                        0xdf
                },
                new int[]{
                        0x40,
                        0xb0,
                        0x4f,
                        0xbf
                },
                new int[]{
                        0x80,
                        0x70,
                        0x8f,
                        0x7f
                }
        };
        for (int bit = 0; bit < 8; bit++) {
            for (int i = 0; i < input.length; i++) {
                assertEquals("toggleBit(" + input[i] + ", " + bit + ")", result[bit][i], BinaryToolkit.toggleBit(input[i], bit));
            }
        }
    }

    @Test
    public void testUnsetBit() throws Exception {
        final int[] input = {
                0x00,
                0xf0,
                0x0f,
                0xff
        };
        final int[][] result = {
                new int[]{
                        0x00,
                        0xf0,
                        0x0e,
                        0xfe
                },
                new int[]{
                        0x00,
                        0xf0,
                        0x0d,
                        0xfd
                },
                new int[]{
                        0x00,
                        0xf0,
                        0x0b,
                        0xfb
                },
                new int[]{
                        0x00,
                        0xf0,
                        0x07,
                        0xf7
                },
                new int[]{
                        0x00,
                        0xe0,
                        0x0f,
                        0xef
                },
                new int[]{
                        0x00,
                        0xd0,
                        0x0f,
                        0xdf
                },
                new int[]{
                        0x00,
                        0xb0,
                        0x0f,
                        0xbf
                },
                new int[]{
                        0x00,
                        0x70,
                        0x0f,
                        0x7f
                }
        };
        for (int bit = 0; bit < 8; bit++) {
            for (int i = 0; i < input.length; i++) {
                assertEquals("unsetBit(" + input[i] + ", " + bit + ")", result[bit][i], BinaryToolkit.unsetBit(input[i], bit));
            }
        }
    }

    @Test
    public void testSetBits() throws Exception {
        final int[] input = {
                0x00,
                0xf0,
                0x0f,
                0xff
        };
        final int[][] result = {
                new int[]{
                        0x03,
                        0xf3,
                        0x0f,
                        0xff
                },
                new int[]{
                        0x0e,
                        0xfe,
                        0x0f,
                        0xff
                },
                new int[]{
                        0x3c,
                        0xfc,
                        0x3f,
                        0xff
                },
                new int[]{
                        0xf8,
                        0xf8,
                        0xff,
                        0xff
                }
        };
        for (int j = 0; j < result.length; j++) {
            for (int i = 0; i < input.length; i++) {
                assertEquals("setBits(" + input[i] + ", " + j + ", " + (1 + (j * 2)) + ")", result[j][i], BinaryToolkit.setBits(input[i], j, 1 + (j * 2)));
            }
        }
    }

    @Test
    public void testUnsetBits() throws Exception {
        final int[] input = {
                0x00,
                0xf0,
                0x0f,
                0xff
        };
        final int[][] result = {
                new int[]{
                        0x00,
                        0xf0,
                        0x0c,
                        0xfc
                },
                new int[]{
                        0x00,
                        0xf0,
                        0x01,
                        0xf1
                },
                new int[]{
                        0x00,
                        0xc0,
                        0x03,
                        0xc3
                },
                new int[]{
                        0x00,
                        0x00,
                        0x07,
                        0x07
                }
        };
        for (int j = 0; j < result.length; j++) {
            for (int i = 0; i < input.length; i++) {
                assertEquals("unsetBits(" + input[i] + ", " + j + ", " + (1 + (j * 2)) + ")", result[j][i], BinaryToolkit.unsetBits(input[i], j, 1 + (j * 2)));
            }
        }
    }

    @Test
    public void testToggleBits() throws Exception {
        final int[] input = {
                0x00,
                0xf0,
                0x0f,
                0xff
        };
        final int[][] result = {
                new int[]{
                        0x03,
                        0xf3,
                        0x0c,
                        0xfc
                },
                new int[]{
                        0x0e,
                        0xfe,
                        0x01,
                        0xf1
                },
                new int[]{
                        0x3c,
                        0xcc,
                        0x33,
                        0xc3
                },
                new int[]{
                        0xf8,
                        0x08,
                        0xf7,
                        0x07
                }
        };
        for (int j = 0; j < result.length; j++) {
            for (int i = 0; i < input.length; i++) {
                assertEquals("toggleBits(" + input[i] + ", " + j + ", " + (1 + (j * 2)) + ")", result[j][i], BinaryToolkit.toggleBits(input[i], j, 1 + (j * 2)));
            }
        }
    }
}
