package remote.phone.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class ConnectionController {
	private static ConnectionController instance = null;

	private Socket socket = null;
	private PrintWriter out;
	private DatagramSocket UDPSocket;
	private InetAddress IPAddress;
	private DatagramPacket sendPacket;
	private Boolean socketReady = false;

	public synchronized void sendToUDPSocket(String msg) {
		try {
			sendPacket.setData(msg.getBytes("UTF-8"));
			new Thread() {
				@Override
				public void run() {
					try {
						UDPSocket.send(sendPacket);
					} catch (IOException e) {
						prepareSocket();
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

	public void prepareSocket() {
		try {
			UDPSocket = new DatagramSocket();
			IPAddress = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, (byte) 1, (byte) 155 });
			sendPacket = new DatagramPacket(new byte[1024], 1024, IPAddress, 45677);
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		new ConnectionTask().execute("");
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

	private class ConnectionTask extends AsyncTask<String, Integer, Socket> {

		@Override
		protected Socket doInBackground(String... params) {
			String host = "192.168.1.155";
			int port = 3456;
			Socket socket = null;
			try {
				socket = new Socket(host, port);
			} catch (IOException e) {
				this.cancel(false);
				System.out.println("Unknown host");
				// System.exit(1);
			}
			return socket;
		}

		@Override
		protected void onPostExecute(Socket result) {
			ConnectionController.getInstance().setSocket(result);
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
