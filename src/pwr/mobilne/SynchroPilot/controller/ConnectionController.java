package pwr.mobilne.SynchroPilot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;

public class ConnectionController {
	private static ConnectionController instance = null;

	private Socket socket = null;
	private PrintWriter out;
	@SuppressWarnings("unused")
	private BufferedReader in;
	private Boolean socketReady = false;

	public synchronized void sendToSocket(String msg) {
		if (socketReady) out.println(msg);
	}

	public void prepareSocket() {
		new ConnectionTask().execute("");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ConnectionController getInstance() {
		if (instance == null) instance = new ConnectionController();
		return instance;
	}

}
