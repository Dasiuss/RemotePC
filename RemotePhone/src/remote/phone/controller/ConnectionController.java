package remote.phone.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

public class ConnectionController {
	private static ConnectionController instance = null;

	private Socket socket = null;
	private PrintWriter out;
	private DatagramSocket UDPSocket;
	private DatagramPacket sendPacket;
	private Boolean socketReady = false;

	public final int UDPCONNECTIONPORT = 9563;
	public final int UDPPORT = 45677;

	public Context context;

	public synchronized void sendToUDPSocket(String msg) {
		try {
			sendPacket.setData(msg.getBytes("UTF-8"));
			new Thread() {
				@Override
				public void run() {
					try {
						UDPSocket.send(sendPacket);
					} catch (IOException e) {
						prepareSocket(context);
						e.printStackTrace();
					}
				}
			}.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendToSocket(String msg) {
		if (socketReady) out.println(msg);
	}

	public void prepareSocket(Context context) {
		this.context = context;
		new ConnectionTask().start();
	}

	protected void setSocket(Socket socket) {
		if (socket == null) return;
		this.socket = socket;
		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socketReady = true;
	}

	private class ConnectionTask extends Thread {

		@Override
		public void run() {
			String host = "192.168.1.155";
			int port = 3456;
			Socket socket = null;
			InetAddress IPAddress;
			try {
				socket = new Socket(host, port);
				IPAddress = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, (byte) 1, (byte) 155 });
				UDPSocket = new DatagramSocket();
				sendPacket = new DatagramPacket(new byte[1024], 1024, IPAddress, 45677);
			} catch (IOException e) {
				// Unknown host - try with finder:
				UDPServerFinder usf = new UDPServerFinder();
				IPAddress = usf.getServerIp();
				host = IPAddress.getHostAddress();

				try {
					socket = new Socket(host, port);
					UDPSocket = new DatagramSocket();
					sendPacket = new DatagramPacket(new byte[1024], 1024, IPAddress, 45677);
				} catch (IOException e1) {
					e1.printStackTrace();
					Log.wtf("ConnectionController", "Cannot connect");
				}
			}

			ConnectionController.getInstance().setSocket(socket);
		}
	}

	public void closeSocket() {
		try {
			if (socket != null) socket.close();
			if (UDPSocket != null) UDPSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ConnectionController getInstance() {
		if (instance == null) instance = new ConnectionController();
		return instance;
	}

}
