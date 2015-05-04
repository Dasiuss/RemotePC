package pwr.mobilne.SynchroPilot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * in Activity, call <code>ConnectionController.getInstance().prepareSocket(getApplicationContext());</code>
 * 
 */
public class ConnectionController {
	private static ConnectionController instance = null;

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
		String ip = getWifiIpAddress(context);
		ip = ip.substring(0, ip.lastIndexOf("."));
		for (int i = 1; i < 255; i++) {
			new ConnectionTask().execute(ip + "." + i);
		}// TODO save last positive try, to avoid rescanning after onResume();
	}

	protected void setSocket(Socket socket) {
		if (socket == null) return;
		this.socket = socket;
		try {
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		socketReady = true;
	}

	/**
	 * call <code>new ConnectionTask().execute(ipAddress);</code>
	 */
	private class ConnectionTask extends AsyncTask<String, Integer, Socket> {

		@Override
		protected Socket doInBackground(String... params) {

			String host = params[0];
			int port = 9562;
			Socket socket = null;
			if ((socket = portIsOpen(host, port, 1000)) == null) this.cancel(false);
			return socket;
		}

		@Override
		protected void onPostExecute(Socket result) {
			ConnectionController.getInstance().setSocket(result);
		}

		public Socket portIsOpen(String ip, int port, int timeout) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(ip, port), timeout);
				return socket;
			} catch (Exception ex) {
				return null;
			}
		}
	}

	public void closeSocket() {
		try {
			if (socket != null) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getWifiIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

		// Convert little-endian to big-endianif needed
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
			ipAddress = Integer.reverseBytes(ipAddress);
		}

		byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

		String ipAddressString;
		try {
			ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
		} catch (UnknownHostException ex) {
			Log.e("WIFIIP", "Unable to get host address.");
			ipAddressString = null;
		}

		return ipAddressString;
	}

	public static ConnectionController getInstance() {
		if (instance == null) instance = new ConnectionController();
		return instance;
	}

}
