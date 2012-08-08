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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test for {@link BinaryToolkit}
 */
public class BinaryToolkitTest {
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
                    assertEquals("getBits for Bits [a,b] with a=" + i + ",b=" + j + " should be " + expected, expected, observed);
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
                    assertEquals("generateBitRangeMask for Bits [a,b] with a=" + i + ",b=" + j + " should be " + expected, expected, observed);
                }
            }
            lead += "0";
        }
    }

    @Test
    public void testIsBitSet() throws Exception {

    }

    @Test
    public void testSetBit() throws Exception {

    }

    @Test
    public void testToggleBit() throws Exception {

    }

    @Test
    public void testUnsetBit() throws Exception {

    }

    @Test
    public void testSetBits() throws Exception {

    }

    @Test
    public void testUnsetBits() throws Exception {

    }

    @Test
    public void testToggleBits() throws Exception {

    }
}
