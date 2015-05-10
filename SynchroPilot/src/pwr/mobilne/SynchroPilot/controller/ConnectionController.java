package pwr.mobilne.SynchroPilot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;
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
	Boolean socketReady = false;
	public String lastFoundIp = "";

	Context context;

	public synchronized void sendToSocket(String msg) {
		if (socketReady) out.println(msg);
	}

	public void prepareSocket(Context context) {
		this.context = context;
		Log.i("ConnectionUDP", "preparing...");
		if (!lastFoundIp.equals("")) new TCPConnectionTask(this).execute(lastFoundIp);
		new UDPConnectionTask(this).execute("");
		scanIPsForServer();
	}

	/**
	 * 
	 */
	private void scanIPsForServer() {
		String ip = ConnectionManager.getWifiIpAddress(context);
		ip = ip.substring(0, ip.lastIndexOf("."));
		for (int i = 1; i < 255 && socket == null; i++) {
			new TCPConnectionTask(this).execute(ip + "." + i);
			Log.i("ConnectionController", "task " + i);
		}
	}

	protected synchronized void setSocket(Socket socket) {
		if (socket == null) return;
		this.socket = socket;
		lastFoundIp = socket.getInetAddress().getHostAddress();
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

	public static ConnectionController getInstance() {
		if (instance == null) instance = new ConnectionController();
		return instance;
	}

}
