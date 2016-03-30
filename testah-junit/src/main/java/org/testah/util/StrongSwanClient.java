package org.testah.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketException;
import org.testah.TS;


/**
 * The Class StrongSwanClient.
 */
//https://github.com/strongswan/strongswan/blob/master/src/libcharon/plugins/vici/ruby/lib/vici.rb
public class StrongSwanClient {

    /** The socket full path. */
    private String socketFullPath = "/tmp/socketname";

    /**
     * Instantiates a new strong swan client.
     */
    public StrongSwanClient() {

    }

    /**
     * Instantiates a new strong swan client.
     *
     * @param socketFullPath the socket full path
     */
    public StrongSwanClient(final String socketFullPath) {
        this.setSocketFullPath(socketFullPath);
    }

    /**
     * The Enum Message.
     */
    public static enum Message {
        
        /** The section start. */
        SECTION_START(1), 
 /** The section end. */
 SECTION_END(2), 
 /** The list start. */
 LIST_START(4), 
 /** The list item. */
 LIST_ITEM(5), 
 /** The list end. */
 LIST_END(6);

        /** The value. */
        private final int value;

        /**
         * Instantiates a new message.
         *
         * @param value the value
         */
        Message(final int value) {
            this.value = value;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

    }

    /**
     * Send message.
     *
     * @param a6rgs the a6rgs
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void sendMessage(final String[] a6rgs) throws IOException {
        final File socketFile = new File(getSocketFullPath());
        final AFUNIXSocket sock = AFUNIXSocket.newInstance();
        try {
            sock.connect(new AFUNIXSocketAddress(socketFile));
        } catch (final AFUNIXSocketException e) {
            TS.log().error("Cannot connect to server. Have you started it?", e);
            throw e;
        }
        TS.log().debug("Connected");
        try (final InputStream is = sock.getInputStream(); final OutputStream os = sock.getOutputStream()) {

            final byte[] buf = new byte[128];
            final int read = is.read(buf);
            System.out.println("Server says: " + new String(buf, 0, read));
            System.out.println("Replying to server...");
            os.write("Hello Server".getBytes());
        }
        TS.log().debug("End of communication.");
    }

    /**
     * Gets the socket full path.
     *
     * @return the socket full path
     */
    public String getSocketFullPath() {
        return socketFullPath;
    }

    /**
     * Sets the socket full path.
     *
     * @param socketFullPath the new socket full path
     */
    public void setSocketFullPath(final String socketFullPath) {
        this.socketFullPath = socketFullPath;
    }

}
