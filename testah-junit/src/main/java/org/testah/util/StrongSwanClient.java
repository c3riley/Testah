package org.testah.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketException;
import org.testah.TS;

//https://github.com/strongswan/strongswan/blob/master/src/libcharon/plugins/vici/ruby/lib/vici.rb
public class StrongSwanClient {

	private String socketFullPath = "/tmp/socketname";

	public StrongSwanClient() {

	}

	public StrongSwanClient(final String socketFullPath) {
		this.setSocketFullPath(socketFullPath);
	}

	public static enum Message {
		SECTION_START(1), SECTION_END(2), LIST_START(4), LIST_ITEM(5), LIST_END(6);

		private final int value;

		Message(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

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
		try (final InputStream is = sock.getInputStream()) {
			try (final OutputStream os = sock.getOutputStream()) {
				final byte[] buf = new byte[128];
				final int read = is.read(buf);
				System.out.println("Server says: " + new String(buf, 0, read));
				System.out.println("Replying to server...");
				os.write("Hello Server".getBytes());
				os.flush();
				os.close();
			}
		} finally {
			sock.close();
		}
		TS.log().debug("End of communication.");
	}

	public String getSocketFullPath() {
		return socketFullPath;
	}

	public void setSocketFullPath(final String socketFullPath) {
		this.socketFullPath = socketFullPath;
	}

}
