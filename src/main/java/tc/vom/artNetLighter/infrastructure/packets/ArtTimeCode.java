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

import tc.vom.artNetLighter.infrastructure.constants.ArtNetOpCodes;


/**
 * Implements ArtTimeCode packet.
 */
public class ArtTimeCode extends _VersionedArtNetPacket {

    public static final int PACKET_LENGTH = _VersionedArtNetPacket.FULL_HEADER_LENGTH + 7;
    /**
     * 1 Byte Filler1
     */
    private final byte filler1;

    /**
     * 1 Byte Filler 2
     */
    private final byte filler2;

    /**
     * 1 Byte Frames.
     * Frames time. 0 – 29 depending on mode.
     */
    private final byte frames;

    /**
     * 1 Byte Seconds.
     * Seconds. 0 - 59.
     */
    private final byte seconds;

    /**
     * 1 Byte Minutes.
     * Minutes. 0 - 59.
     */
    private final byte minutes;

    /**
     * 1 Byte Hours.
     * Hours. 0 - 23.
     */
    private final byte hours;

    /**
     * 1 Byte Type.
     * 0 = Film (24fps)
     * 1 = EBU (25fps)
     * 2 = DF (29.97fps)
     * 3 = SMPTE (30fps)
     */
    private final byte type;

    public ArtTimeCode(final byte frames, final byte seconds, final byte minutes, final byte hours, final byte type) {
        this((byte) 0, (byte) 0, frames, seconds, minutes, hours, type);
    }

    private ArtTimeCode(final byte filler1, final byte filler2, final byte frames, final byte seconds, final byte minutes, final byte hours, final byte type) {
        super(ArtNetOpCodes.OP_CODE_TIME_CODE);
        this.filler1 = filler1;
        this.filler2 = filler2;
        this.frames = frames;
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.type = type;
    }

    public byte getFiller1() {
        return this.filler1;
    }

    public byte getFiller2() {
        return this.filler2;
    }

    public byte getFrames() {
        return this.frames;
    }

    public byte getSeconds() {
        return this.seconds;
    }

    public byte getMinutes() {
        return this.minutes;
    }

    public byte getHours() {
        return this.hours;
    }

    public byte getType() {
        return this.type;
    }

    @Override
    public byte[] constructPacket() {
        return ArtTimeCode.constructPacket(this.filler1, this.filler2, this.frames, this.seconds, this.minutes, this.hours, this.type);
    }

    public static byte[] constructPacket(final byte filler1, final byte filler2, final byte frames, final byte seconds, final byte minutes, final byte hours, final byte type) {
        final byte[] pData = _VersionedArtNetPacket.constructPacket(ArtTimeCode.PACKET_LENGTH, ArtNetOpCodes.OP_CODE_TIME_CODE);
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH] = filler1;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 1] = filler2;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 2] = frames;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 3] = seconds;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 4] = minutes;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 5] = hours;
        pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 6] = type;
        return pData;
    }

    public ArtTimeCode(final byte[] pData) {
        super(pData);
        if (this.getOpCode() != ArtNetOpCodes.OP_CODE_TIME_CODE) {
            throw new IllegalArgumentException("Packet received has wrong OpCode");
        }
        if (pData.length < ArtTimeCode.PACKET_LENGTH) {
            throw new IllegalArgumentException("Packet needs to be at least " + ArtTimeCode.PACKET_LENGTH + " bytes");
        }
        this.filler1 = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH];
        this.filler2 = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 1];
        this.frames = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 2];
        this.seconds = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 3];
        this.minutes = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 4];
        this.hours = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 5];
        this.type = pData[_VersionedArtNetPacket.FULL_HEADER_LENGTH + 6];
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtTimeCode)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ArtTimeCode that = (ArtTimeCode) o;

        if (this.frames != that.frames) {
            return false;
        }
        if (this.hours != that.hours) {
            return false;
        }
        if (this.minutes != that.minutes) {
            return false;
        }
        if (this.seconds != that.seconds) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (this.type != that.type) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + this.frames;
        result = (31 * result) + this.seconds;
        result = (31 * result) + this.minutes;
        result = (31 * result) + this.hours;
        result = (31 * result) + this.type;
        return result;
    }
}
