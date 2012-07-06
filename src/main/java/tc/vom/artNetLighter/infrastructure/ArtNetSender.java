package tc.vom.artNetLighter.infrastructure;

import java.net.*;

/**
 * This class is used to send out Art-Net universes.
 *
 * @author github@cconstruct.de
 * @version 0.1
 * @since 2012-07-06
 */
public class ArtNetSender {
    public static final int MAX_NET = 0x7F;
    public static final int MAX_SUB_NET = 0xF;
    public static final int MAX_UNIVERSE = 0xF;
    public static final int MAX_PORT_ADDRESS = 0x7FFF;

    private int portAddress;
    private InetAddress inetAddress;
    private DatagramSocket udpSocket;

    public ArtNetSender(int portAddress) throws SocketException {
        this(portAddress, null);
    }

    public ArtNetSender(int portAddress, InetAddress inetAddress) throws SocketException {
        this.setPortAddress(portAddress);
        this.setInetAddress(inetAddress);
    }

    public int getNet() {
        return (portAddress & 0x7F00) >> 8;
    }

    public void setNet(int newNet) {
        if (newNet > MAX_NET || newNet < 0)
            throw new IllegalArgumentException("Net must be in range [0," + MAX_NET + "]");
        portAddress = (portAddress & 0x00FF) | (newNet << 8);
    }

    public int getSubNet() {
        return (portAddress & 0x00F0) >> 4;
    }

    public void setSubNet(int newSubNet) {
        if (newSubNet > MAX_SUB_NET || newSubNet < 0)
            throw new IllegalArgumentException("Sub-Net must be in range [0," + MAX_SUB_NET + "]");
        portAddress = (portAddress & 0x7F0F) | (newSubNet << 4);
    }

    public int getUniverse() {
        return (portAddress & 0x000F);
    }

    public void setUniverse(int newUniverse) {
        if (newUniverse > MAX_UNIVERSE || newUniverse < 0)
            throw new IllegalArgumentException("Universe must be in range [0," + MAX_UNIVERSE + "]");
        portAddress = (portAddress & 0x7FF0) | newUniverse;
    }

    public int getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(int newPortAddress) {
        if (newPortAddress > MAX_PORT_ADDRESS || newPortAddress < 0)
            throw new IllegalArgumentException("Port Address must be in range [0," + MAX_PORT_ADDRESS + "]");
        this.portAddress = newPortAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) throws SocketException {
        this.udpSocket = ArtNetSocketProvider.getArtNetSocket(inetAddress);
        this.inetAddress = udpSocket.getLocalAddress();
    }

    @Override
    public String toString() {
        return "ArtNetSender{" +
                "portAddress=" + portAddress +
                '}';
    }
}
