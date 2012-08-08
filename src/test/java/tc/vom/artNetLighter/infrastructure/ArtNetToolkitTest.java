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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link ArtNetToolkit}
 */
public class ArtNetToolkitTest {
    @Test
    public void testGetNet() throws Exception {
        final int[] input = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        final int[] result = {
                0x00,
                0x7f,
                0x00,
                0x7f,
                0x00,
                0x7f,
                0x00,
                0x7f
        };
        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals("getNet(" + input[i] + ")", result[i], ArtNetToolkit.getNet(input[i]));
        }
    }

    @Test
    public void testSetNet() throws Exception {
        final int[] input = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        final int[] inputNet = {
                0x00,
                0x3f,
                0x7f
        };
        final int[][] result = {
                {
                        0x0000,
                        0x0000,
                        0x00f0,
                        0x00f0,
                        0x000f,
                        0x000f,
                        0x00ff,
                        0x00ff,
                },
                {
                        0x3f00,
                        0x3f00,
                        0x3ff0,
                        0x3ff0,
                        0x3f0f,
                        0x3f0f,
                        0x3fff,
                        0x3fff,
                },
                {
                        0x7f00,
                        0x7f00,
                        0x7ff0,
                        0x7ff0,
                        0x7f0f,
                        0x7f0f,
                        0x7fff,
                        0x7fff,
                }
        };
        for (int j = 0; j < inputNet.length; j++) {
            for (int i = 0; i < input.length; i++) {
                Assert.assertEquals("setNet(" + input[i] + "," + inputNet[j] + ")", result[j][i], ArtNetToolkit.setNet(input[i], inputNet[j]));
            }
        }
    }

    @Test
    public void testGetSubNet() throws Exception {
        final int[] input = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        final int[] result = {
                0x00,
                0x00,
                0x0f,
                0x0f,
                0x00,
                0x00,
                0x0f,
                0x0f
        };
        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals("getSubNet(" + input[i] + ")", result[i], ArtNetToolkit.getSubNet(input[i]));
        }
    }

    @Test
    public void testSetSubNet() throws Exception {
        final int[] input = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        final int[] inputSubNet = {
                0x00,
                0x07,
                0x0f
        };
        final int[][] result = {
                {
                        0x0000,
                        0x7f00,
                        0x0000,
                        0x7f00,
                        0x000f,
                        0x7f0f,
                        0x000f,
                        0x7f0f,
                },
                {
                        0x0070,
                        0x7f70,
                        0x0070,
                        0x7f70,
                        0x007f,
                        0x7f7f,
                        0x007f,
                        0x7f7f,
                },
                {
                        0x00f0,
                        0x7ff0,
                        0x00f0,
                        0x7ff0,
                        0x00ff,
                        0x7fff,
                        0x00ff,
                        0x7fff,
                }
        };
        for (int j = 0; j < inputSubNet.length; j++) {
            for (int i = 0; i < input.length; i++) {
                Assert.assertEquals("setSubNet(" + input[i] + "," + inputSubNet[j] + ")", result[j][i], ArtNetToolkit.setSubNet(input[i], inputSubNet[j]));
            }
        }
    }

    @Test
    public void testGetUniverse() throws Exception {
        final int[] input = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        final int[] result = {
                0x00,
                0x00,
                0x00,
                0x00,
                0x0f,
                0x0f,
                0x0f,
                0x0f
        };
        for (int i = 0; i < input.length; i++) {
            Assert.assertEquals("getUniverse(" + input[i] + ")", result[i], ArtNetToolkit.getUniverse(input[i]));
        }
    }

    @Test
    public void testSetUniverse() throws Exception {
        final int[] input = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        final int[] inputUniverse = {
                0x00,
                0x07,
                0x0f
        };
        final int[][] result = {
                {
                        0x0000,
                        0x7f00,
                        0x00f0,
                        0x7ff0,
                        0x0000,
                        0x7f00,
                        0x00f0,
                        0x7ff0,
                },
                {
                        0x0007,
                        0x7f07,
                        0x00f7,
                        0x7ff7,
                        0x0007,
                        0x7f07,
                        0x00f7,
                        0x7ff7,
                },
                {
                        0x000f,
                        0x7f0f,
                        0x00ff,
                        0x7fff,
                        0x000f,
                        0x7f0f,
                        0x00ff,
                        0x7fff,
                }
        };
        for (int j = 0; j < inputUniverse.length; j++) {
            for (int i = 0; i < input.length; i++) {
                Assert.assertEquals("setUniverse(" + input[i] + "," + inputUniverse[j] + ")", result[j][i], ArtNetToolkit.setUniverse(input[i], inputUniverse[j]));
            }
        }
    }

    @Test
    public void testGetPortId() throws Exception {
        final int[] inputUniverse = {0x00, 0x0f};
        final int[] inputSubNet = {0x00, 0x0f};
        final int[] inputNet = {0x00, 0x7f};
        final int[] result = {
                0x0000,
                0x7f00,
                0x00f0,
                0x7ff0,
                0x000f,
                0x7f0f,
                0x00ff,
                0x7fff,
        };
        for (int i = 0; i < inputUniverse.length; i++) {
            for (int j = 0; j < inputSubNet.length; j++) {
                for (int k = 0; k < inputNet.length; k++) {
                    Assert.assertEquals("getPortId(" + inputNet[k] + ", " + inputSubNet[j] + ", " + inputUniverse[i] + ")", result[(k + (2 * j) + (4 * i))], ArtNetToolkit.getPortId(inputNet[k], inputSubNet[j], inputUniverse[i]));
                }
            }
        }
    }
}
