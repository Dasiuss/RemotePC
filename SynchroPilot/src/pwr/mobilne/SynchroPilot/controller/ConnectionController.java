package pwr.mobilne.SynchroPilot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * in Activity, call <code>ConnectionController.getInstance().prepareSocket(getApplicationContext());</code>
 * 
 */
public class ConnectionController {
	private static ConnectionController instance = null;
	public final int PORT = 9562;
	public final int UDPPORT = 9563;
	private Socket socket = null;
	private PrintWriter out;
	@SuppressWarnings("unused")
	private BufferedReader in;
	private Boolean socketReady = false;

	private Context context;

	public synchronized void sendToSocket(String msg) {
		if (socketReady) out.println(msg);
	}

	public void prepareSocket(Context context) {
		this.context = context;
		Log.i("ConnectionUDP", "preparing...");
		new UDPConnectionTask().execute("");
		// scanIPsForServer();// TODO save last positive try, to avoid rescanning after onResume();
	}

	/**
	 * 
	 */
	private void scanIPsForServer() {
		String ip = getWifiIpAddress();
		ip = ip.substring(0, ip.lastIndexOf("."));
		for (int i = 1; i < 255 && socket == null; i++) {
			new TCPConnectionTask().execute(ip + "." + i);
			Log.i("ConnectionController", "task " + i);
		}
	}

	private class UDPConnectionTask extends AsyncTask<String, Integer, Void> {

		@Override
		protected Void doInBackground(String... paramArrayOfParams) {
			DatagramPacket packet;
			Log.i("ConnectionUDP", "preparing  2...");
			try {
				DatagramSocket socket = new DatagramSocket();
				socket.setBroadcast(true);
				int message = 12345;// sends on broadcast
				byte[] localPortBytes = new byte[2];
				localPortBytes[0] = (byte) ((message >> 8) & 0xFF);
				localPortBytes[1] = (byte) ((message) & 0xFF);
				Log.i("ConnectionUDP", "Sending udp broadcast with " + 12345);
				try {
					packet = new DatagramPacket(localPortBytes, localPortBytes.length, getBroadcastAddress(), UDPPORT);
					socket.send(packet);

					byte[] buf = new byte[4];
					packet = new DatagramPacket(buf, buf.length);// wait for response with ipaddress of server
					socket.receive(packet);
					String data = new String(packet.getData());
					Log.i("ConnectionUDP", "received" + data);

					new TCPConnectionTask().execute(packet.getAddress().getHostAddress());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	/**
	 * call <code>new ConnectionTask().execute(ipAddress);</code>
	 */
	private class TCPConnectionTask extends AsyncTask<String, Integer, Socket> {

		@Override
		protected Socket doInBackground(String... params) {
			Socket socket = null;
			if (ConnectionController.getInstance().socketReady) {
				this.cancel(false);
			} else {

				String host = params[0];
				if ((socket = portIsOpen(host, PORT, 100)) == null) this.cancel(false);
			}
			return socket;
		}

		@Override
		protected void onPostExecute(Socket result) {
			ConnectionController.getInstance().setSocket(result);
		}

		public Socket portIsOpen(String ip, int port, int timeout) {
			try {
				Log.i("ConnectionController", "Connection try: " + ip);
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(ip, port), timeout);
				Log.i("ConnectionController", "Connected to " + ip);
				return socket;
			} catch (Exception ex) {
				return null;
			}
		}
	}

	protected synchronized void setSocket(Socket socket) {
		if (socket == null) return;
		this.socket = socket;
		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketReady = true;
		} catch (IOException e) {
			e.printStackTrace();
			socket = null;
		}
	}

	public void closeSocket() {
		try {
			if (socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp == null) return null;
		// TODO handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	protected String getWifiIpAddress() {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp == null) return null;
		// TODO handle null somehow
		String ipAddress = "";
		for (int k = 0; k < 4; k++)
			ipAddress += ((dhcp.ipAddress >> k * 8) & 0xFF) + ".";
		ipAddress = ipAddress.substring(0, ipAddress.length() - 1);
		Log.i("Connection Controller", ipAddress);
		return ipAddress;
	}

	public static ConnectionController getInstance() {
		if (instance == null) instance = new ConnectionController();
		return instance;
	}

}
