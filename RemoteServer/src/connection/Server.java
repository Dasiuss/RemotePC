package connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public ServerSocket server = null;
	public Socket client = null;
	private String au3;

	public Server() {
		int port = 3456;
		try {
			server = new ServerSocket(port);
			new Thread(new UDPThread()).start();
			extractAu3Helper();
		} catch (IOException ex) {
			System.out.println("Port zajêty");
			System.exit(-1);
		}
		while (true) {
			try {
				System.out.println("nasluchiwanie...");
				client = server.accept();
				ClientThread w = new ClientThread(client, au3);
				w.start();
			} catch (IOException ex) {
				System.out.println("Nie mo¿na zaakceptowaæ");
			}
		}

	}

	public static void main(String[] args) {
		new Server();
	}

	/**
	 * odpala program au3 do którego potem mo¿na wysy³a komendy
	 * 
	 * @throws IOException
	 */
	private void extractAu3Helper() throws IOException {
		InputStream src = ClientThread.class.getResource("/RemotePhoneKeySimulator.exe").openStream();
		File exeTempFile = File.createTempFile("RemotePhoneKeySimulator", ".exe");
		FileOutputStream out = new FileOutputStream(exeTempFile);
		byte[] temp = new byte[32768];
		int rc;
		while ((rc = src.read(temp)) > 0)
			out.write(temp, 0, rc);
		src.close();
		out.close();
		exeTempFile.deleteOnExit();
		au3 = exeTempFile.toString();

		System.out.println("au3 ready!");
	}

	public String getAu3() {
		return au3;
	}
}