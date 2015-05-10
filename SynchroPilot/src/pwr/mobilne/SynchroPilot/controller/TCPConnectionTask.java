package pwr.mobilne.SynchroPilot.controller;

import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

/**
 * call <code>new ConnectionTask().execute(ipAddress);</code>
 */
public class TCPConnectionTask extends AsyncTask<String, Integer, Socket> {

	/**
	 * 
	 */
	private final ConnectionController connectionController;

	/**
	 * @param connectionController
	 */
	TCPConnectionTask(ConnectionController connectionController) {
		this.connectionController = connectionController;
	}

	@Override
	protected Socket doInBackground(String... params) {
		Socket socket = null;
		if (ConnectionController.getInstance().socketReady) {
			this.cancel(false);
		} else {

			String host = params[0];
			if ((socket = portIsOpen(host, this.connectionController.PORT, 100)) == null) this.cancel(false);
		}
		return socket;
	}

	@Override
	protected void onPostExecute(Socket result) {
		this.connectionController.setSocket(result);
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