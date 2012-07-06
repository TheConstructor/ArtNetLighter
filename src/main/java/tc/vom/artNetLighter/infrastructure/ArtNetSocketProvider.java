package tc.vom.artNetLighter.infrastructure;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.WeakHashMap;

/**
 * This class is used to send to manage the bound {@link java.net.DatagramSocket}s so we don't get {@link java.net.BindException} when having multiple {@link ArtNetSender}.
 *
 * @author github@cconstruct.de
 * @version 0.1
 * @since 2012-07-06
 */
public class ArtNetSocketProvider {
    public static final int ART_NET_PORT = 0x1936;

    private static WeakHashMap<InetAddress, DatagramSocket> map = new WeakHashMap<InetAddress, DatagramSocket>();

    public static DatagramSocket getArtNetSocket() throws SocketException {
        return ArtNetSocketProvider.getArtNetSocket(null);
    }

    public static DatagramSocket getArtNetSocket(InetAddress inetAddress) throws SocketException {
        if (inetAddress == null)
            inetAddress = ArtNetSocketProvider.findHostAddress();
        DatagramSocket result = map.get(inetAddress);
        if (result == null) {
            result = new DatagramSocket(ART_NET_PORT, inetAddress);
            map.put(inetAddress, result);
        }
        return result;
    }

    public static InetAddress findHostAddress() throws SocketException {
        InetAddress hostAddress = null;
        InetAddress fallbackAddress = null;
        InetAddress lastAddress = null;

        final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        selection:
        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (!networkInterface.isUp())
                continue;
            final Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                final InetAddress inetAddress = addressEnumeration.nextElement();
                final byte[] address = inetAddress.getAddress();
                if (address.length != 4)
                    continue;
                switch (address[0]) {
                    case 127:
                        continue;
                    case 10:
                        fallbackAddress = inetAddress;
                        break;
                    case 2:
                        hostAddress = inetAddress;
                        break selection;
                    default:
                        lastAddress = inetAddress;
                        break;
                }
            }
        }

        if (hostAddress == null) {
            if (fallbackAddress != null)
                hostAddress = fallbackAddress;
            else
                hostAddress = lastAddress;
        }
        return hostAddress;
    }
}
