package tc.vom.artNetLighter.infrastructure;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ArtNetToolkitTest {

    @Test
    public void testBitMask() throws Exception {
        String lead = "";
        for (int i = 0; i < ArtNetToolkit.BIT_MASKS.length; i++) {
            final StringBuilder sb = new StringBuilder(ArtNetToolkit.BIT_MASKS.length);
            sb.append(lead);
            for (int j = 0; j < ArtNetToolkit.BIT_MASKS[i].length; j++) {
                final String observed = Integer.toBinaryString(ArtNetToolkit.BIT_MASKS[i][j]);
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
                        ArtNetToolkit.getBits(-1, i, j);
                        fail("getBits for Bits [a,b] with b < a should throw an IllegalArgumentException");
                    } catch (IllegalArgumentException e) {
                        // ok.
                    }
                } else {
                    final String observed = Integer.toBinaryString(ArtNetToolkit.getBits(-1, i, j));
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
                        ArtNetToolkit.generateBitRangeMask(i, j);
                        fail("generateBitRangeMask for Bits [a,b] with b < a should throw an IllegalArgumentException");
                    } catch (IllegalArgumentException e) {
                        // ok.
                    }
                } else {
                    final String observed = Integer.toBinaryString(ArtNetToolkit.generateBitRangeMask(i, j));
                    sb.insert(0, '1');
                    final String expected = sb.toString();
                    assertEquals("generateBitRangeMask for Bits [a,b] with a=" + i + ",b=" + j + " should be " + expected, expected, observed);
                }
            }
            lead += "0";
        }
    }
}
