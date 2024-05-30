package com.shortthirdman.sharedlibs.util;

import java.net.*;
import java.util.*;

public class NetworkUtils {

    private NetworkUtils() {}

    /**
     * @return List
     * @throws SocketException
     */
    public static List<NetworkInterface> getAllAvailableInterfaces() throws SocketException {
        List<NetworkInterface> netInfList = new ArrayList<>();
        NetworkInterface intf;

        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
            intf = en.nextElement();
            netInfList.add(intf);
        }

        return netInfList;
    }

    /**
     * @return List
     * @throws SocketException
     */
    public static List<NetworkInterface> getAllInterfaces() throws SocketException {
        List<NetworkInterface> allInterfaces = new ArrayList<>();

        for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
            NetworkInterface intf = interfaces.nextElement();
            allInterfaces.add(intf);

            Enumeration<NetworkInterface> subInterfaces = intf.getSubInterfaces();

            if (subInterfaces.hasMoreElements()) {
                while (subInterfaces.hasMoreElements()) {
                    allInterfaces.add(subInterfaces.nextElement());
                }
            }
        }

        allInterfaces.sort(Comparator.comparingInt(NetworkInterface::getIndex));

        return allInterfaces;
    }

    /**
     * Get all the interfaces which are not loopback ones
     * 
     * @return Set
     */
    public static Set<NetworkInterface> getAllLocalInterfaces() {
        Set<NetworkInterface> netInfSet = new HashSet<>();
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();

                Enumeration<InetAddress> set = ni.getInetAddresses();

                if (set.hasMoreElements()) {
                    InetAddress address = set.nextElement();
                    if (!address.isLoopbackAddress()) {
                        netInfSet.add(ni);
                    }
                }
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }

        return netInfSet;
    }

    /**
     * @return NetworkInterface
     */
    public static NetworkInterface getAvailableNetworkInterface() {
        NetworkInterface result = null;

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback())
                    continue;

                if (networkInterface.isUp()) {
                    result = networkInterface;
                    break;
                }
            }
        } catch (SocketException se) {
            se.printStackTrace();
        }

        return result;
    }

    /**
     * Get broadcast address
     * @return String
     * @throws SocketException
     */
    public static String getBroadcast() throws SocketException {
        String result = null;
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum
                .hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    InetAddress bcast = interfaceAddress.getBroadcast();
                    if (bcast != null) {
                        result = bcast.toString().substring(1);
                        break;
                    }
                }
                if (result != null) break;
            }
        }
        return result;
    }

    /**
     * @param subIf the network sub-interface
     * @param subInterface true, if querying for sub-interface
     * @return String
     * @throws SocketException
     */
    public static String describeInterface(NetworkInterface subIf, boolean subInterface) throws SocketException {
        StringBuilder description = new StringBuilder();
        String padding = "";

        if (subInterface) {
            description.append("\t\tSUBINTERFACE");
            description.append("\t\t____________");
            padding = "\t\t";
        }

        description.append(String.format("%sInterface Display name: %s\n", padding, subIf.getDisplayName()));
        description.append(String.format("%sInterface Name: %s\n", padding, subIf.getName()));
        Enumeration<InetAddress> inetAddresses = subIf.getInetAddresses();

        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            description.append(String.format("%s\tInetAddress: %s\n", padding, inetAddress));
        }

        description.append(String.format("%s\t\tUp? %s\n", padding, subIf.isUp()));
        description.append(String.format("%s\t\tLoopback? %s\n", padding, subIf.isLoopback()));
        description.append(String.format("%s\t\tPointToPoint? %s\n", padding, subIf.isPointToPoint()));
        description.append(String.format("%s\t\tSupports multicast? %s\n", padding, subIf.supportsMulticast()));
        description.append(String.format("%s\t\tVirtual? %s\n", padding, subIf.isVirtual()));
        description.append(String.format("%s\t\tHardware address: %s\n", padding,
                Arrays.toString(subIf.getHardwareAddress())));
        description.append(String.format("%s\t\tMTU: %s\n", padding, subIf.getMTU()));
        description.append("\n");

        return description.toString();
    }

    /**
     * Verify if CIDR string is a valid CIDR address.
     *
     * @param network CIDR to verify
     * @throws IllegalArgumentException
     */
    public static void isCIDR(String network) throws IllegalArgumentException {
        String[] hostMask = network.split("/");
        if (hostMask.length != 2) {
            throw new IllegalArgumentException("subnetAddress is not a CIDR");
        }

        isValidInetAddress(hostMask[0]);

        // Mask must be < 32
        if (Integer.parseUnsignedInt(hostMask[1]) > 32) {
            throw new IllegalArgumentException("CIDR mask may not be larger than 32");
        }
    }

    /**
     * Verify if IP string is an IPv4 address.
     *
     * @param ipAddress IP to verify
     * @throws IllegalArgumentException
     */
    public static void isValidInetAddress(String ipAddress) throws IllegalArgumentException {

        // Opened issue #84 to track proper validation
        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new IllegalArgumentException("IP is missing or empty");
        }

        if (ipAddress.contains(":")) {
            // implement IPv6 validation
        } else {
            String[] segments = ipAddress.split("\\.");
            if (segments.length != 4) {
                throw new IllegalArgumentException("IP does not appear valid:" + ipAddress);
            }
            // it appears to be literal IP, its safe to use the getByName method
            try {
                InetAddress address = InetAddress.getByName(ipAddress);
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
