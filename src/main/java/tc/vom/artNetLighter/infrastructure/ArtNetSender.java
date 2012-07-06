package tc.vom.artNetLighter.infrastructure;

/**
 * This class is used to send out Art-Net universes.
 * @author github@cconstruct.de
 * @version 0.1
 * @since 2012-07-06
 */
public class ArtNetSender {
    private int portAddress;

    public ArtNetSender(int portAddress) {
        this.portAddress = portAddress;
    }

    public int getNet(){
        return (portAddress & 0x7F00) >> 8;
    }

    public void setNet(int newNet) {
        if (newNet > 127 || newNet < 0)
            throw new IllegalArgumentException("Net must be in range [0,127]");
        portAddress = (portAddress & 0x00FF) | (newNet << 8);
    }

    public int getSubNet() {
        return (portAddress & 0x00F0) >> 4;
    }

    public void setSubNet(int newSubNet) {
        if (newSubNet > 15 || newSubNet < 0)
            throw new IllegalArgumentException("Sub-Net must be in range [0,15]");
        portAddress = (portAddress & 0x7F0F) | (newSubNet << 4);
    }

    public int getUniverse() {
        return (portAddress & 0x000F);
    }

    public void setUniverse(int newUniverse) {
        if (newUniverse > 15 || newUniverse < 0)
            throw new IllegalArgumentException("Universe must be in range [0,15]");
        portAddress = (portAddress & 0x7FF0) | newUniverse;
    }

    @Override
    public String toString() {
        return "ArtNetSender{" +
                "portAddress=" + portAddress +
                '}';
    }
}
