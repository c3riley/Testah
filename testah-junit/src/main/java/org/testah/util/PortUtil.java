package org.testah.util;

import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

public class PortUtil
{
    /**
     * Find a free port for use with e.g. Wiremock servers.
     * @return port number
     */
    public static int getFreePort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int portNumber = serverSocket.getLocalPort();
            serverSocket.close();
            return portNumber;
        } catch (Exception e) {
            return ThreadLocalRandom.current().nextInt(3000, 65535);
        }
    }
}
