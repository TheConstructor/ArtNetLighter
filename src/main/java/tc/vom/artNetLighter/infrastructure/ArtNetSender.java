package tc.vom.artNetLighter.infrastructure;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * This class is used to send out Art-Net universes.
 *
 * @author github@cconstruct.de
 * @version 0.1
 * @since 2012-07-06
 */
public class ArtNetSender {

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
        return ArtNetToolkit.getNet(portAddress);
    }

    public void setNet(int newNet) {
        portAddress = ArtNetToolkit.setNet(portAddress, newNet);
    }

    public int getSubNet() {
        return ArtNetToolkit.getSubNet(portAddress);
    }

    public void setSubNet(int newSubNet) {
        portAddress = ArtNetToolkit.setSubNet(portAddress, newSubNet);
    }

    public int getUniverse() {
        return ArtNetToolkit.getUniverse(portAddress);
    }

    public void setUniverse(int newUniverse) {
        portAddress = ArtNetToolkit.setUniverse(portAddress, newUniverse);
    }

    public int getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(int newPortAddress) {
        if (newPortAddress > ArtNetToolkit.MAX_PORT_ADDRESS || newPortAddress < 0)
            throw new IllegalArgumentException("Port Address must be in range [0," + ArtNetToolkit.MAX_PORT_ADDRESS + "]");
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
