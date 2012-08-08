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

import tc.vom.artNetLighter.infrastructure.packets._ArtNetPacket;

import java.util.Arrays;

/**
 * Some functions to convert various data-types contained in byte-arrays.
 */
public class ByteArrayToolkit {

    public static void setBytes(final byte[] from, final byte[] to, final int offset) {
        if (from.length != 0) {
            System.arraycopy(from, 0, to, offset, from.length);
        }
    }

    public static void setString(final String from, final byte[] to, final int offset, final int maxLength) {
        final byte[] strBytes = from.getBytes(_ArtNetPacket.STRING_CHARSET);
        if (strBytes.length > maxLength) {
            throw new IllegalArgumentException("String too long");
        }
        ByteArrayToolkit.setBytes(strBytes, to, offset);
        Arrays.fill(to, offset + strBytes.length, offset + maxLength, (byte) 0);
    }

    public static byte[] getBytes(final byte[] from, final int offset, final int length) {
        //return Arrays.copyOfRange(from, offset, offset + length);
        assert offset >= 0;
        assert length <= (from.length - offset);
        final byte[] result = new byte[length];
        System.arraycopy(from, offset, result, 0, length);
        return result;
    }

    public static byte[] getBytes(final byte[] from, final int offset) {
        //return Arrays.copyOfRange(from, offset, offset + length);
        assert offset >= 0;
        if (offset == from.length) {
            return new byte[0];
        }
        return ByteArrayToolkit.getBytes(from, offset, from.length - offset);
    }

    public static String getString(final byte[] from, final int offset, final int length) {
        final String shortName = new String(from, offset, length, _ArtNetPacket.STRING_CHARSET);
        final int nullTerminator = shortName.indexOf(0);
        if (nullTerminator != -1) {
            return shortName.substring(0, nullTerminator);
        }
        return shortName;
    }

    public static int get4BytesHighToLow(final byte[] from, final int offset) {
        assert from.length > (offset + 3);
        return ((from[offset] & 0xff) << 24) | ((from[offset + 1] & 0xff) << 16) | ((from[offset + 2] & 0xff) << 8) | (from[offset + 3] & 0xff);
    }

    public static int get4BytesLowToHigh(final byte[] from, final int offset) {
        assert from.length > (offset + 3);
        return ((from[offset + 3] & 0xff) << 24) | ((from[offset + 2] & 0xff) << 16) | ((from[offset + 1] & 0xff) << 8) | (from[offset] & 0xff);
    }

    public static int get2BytesHighToLow(final byte[] from, final int offset) {
        assert from.length > (offset + 1);
        return ((from[offset] & 0xff) << 8) | (from[offset + 1] & 0xff);
    }

    public static int get2BytesLowToHigh(final byte[] from, final int offset) {
        assert from.length > (offset + 1);
        return ((from[offset + 1] & 0xff) << 8) | (from[offset] & 0xff);
    }

    public static void set4BytesHighToLow(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 3);
        to[offset] = (byte) ((from >> 24) & 0xff);
        to[offset + 1] = (byte) ((from >> 16) & 0xff);
        to[offset + 2] = (byte) ((from >> 8) & 0xff);
        to[offset + 3] = (byte) (from & 0xff);
    }

    public static void set4BytesLowToHigh(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 3);
        to[offset + 3] = (byte) ((from >> 24) & 0xff);
        to[offset + 2] = (byte) ((from >> 16) & 0xff);
        to[offset + 1] = (byte) ((from >> 8) & 0xff);
        to[offset] = (byte) (from & 0xff);
    }

    public static void set2BytesHighToLow(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 1);
        to[offset] = (byte) ((from >> 8) & 0xff);
        to[offset + 1] = (byte) (from & 0xff);
    }

    public static void set2BytesLowToHigh(final int from, final byte[] to, final int offset) {
        assert to.length > (offset + 1);
        to[offset + 1] = (byte) ((from >> 8) & 0xff);
        to[offset] = (byte) (from & 0xff);
    }

}
