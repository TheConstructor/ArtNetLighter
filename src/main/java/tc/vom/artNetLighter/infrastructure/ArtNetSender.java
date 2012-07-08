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
