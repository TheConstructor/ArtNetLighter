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

package tc.vom.artNetLighter.infrastructure.packets;

import static tc.vom.artNetLighter.infrastructure.ByteArrayToolkit.get2BytesHighToLow;
import static tc.vom.artNetLighter.infrastructure.ByteArrayToolkit.set2BytesHighToLow;

/**
 * Represents an Art-Net packet that could be send or received.
 */
public abstract class _VersionedArtNetPacket extends _ArtNetPacket {

    public static final int PROTOCOL_VERSION = 14;
    public static final int FULL_HEADER_LENGTH = 12;

    /**
     * 2 Byte Protocol Version
     */
    private final int protocolVersion;

    public _VersionedArtNetPacket(final int opCode) {
        this(opCode, _VersionedArtNetPacket.PROTOCOL_VERSION);
    }

    public _VersionedArtNetPacket(final int opCode, final int protocolVersion) {
        super(opCode);
        this.protocolVersion = protocolVersion;
    }

    @SuppressWarnings("WeakerAccess")
    public _VersionedArtNetPacket(final byte[] data) {
        super(data);
        if (data.length < _VersionedArtNetPacket.FULL_HEADER_LENGTH) {
            throw new IllegalArgumentException("Minimum size for Non-ArtPollReply-Packet Header is " + _VersionedArtNetPacket.FULL_HEADER_LENGTH);
        }
        this.protocolVersion = get2BytesHighToLow(data, 10);
    }

    /**
     * 2 Byte Protocol Version
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public static byte[] constructPacket(final int packetLength, final int opCode) {
        return _VersionedArtNetPacket.constructPacket(packetLength, opCode, _VersionedArtNetPacket.PROTOCOL_VERSION);
    }

    public static byte[] constructPacket(final int packetLength, final int opCode, final int protocolVersion) {
        if (packetLength < _VersionedArtNetPacket.FULL_HEADER_LENGTH) {
            throw new IllegalArgumentException("Header alone needs 10 Bytes");
        }
        final byte[] result = _ArtNetPacket.constructUnversionedPacket(packetLength, opCode);
        set2BytesHighToLow(protocolVersion, result, 10);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof _VersionedArtNetPacket)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final _VersionedArtNetPacket that = (_VersionedArtNetPacket) o;

        //noinspection RedundantIfStatement
        if (this.protocolVersion != that.protocolVersion) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.protocolVersion;
        return result;
    }
}
