package pwr.mobilne.SynchroPilot.controller;

import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.AsyncTask;

/**
 * call <code>new ConnectionTask().execute(ipAddress);</code>
 */
public class TCPConnectionTask extends AsyncTask<String, Integer, Void> {

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
	protected Void doInBackground(String... params) {
		Socket socket = null;
		String host = params[0];
		if (ConnectionController.getInstance().socketReady) {
			this.cancel(false);
		} else {

			if ((socket = portIsOpen(host, this.connectionController.PILOT_PORT, 100)) == null) this.cancel(false);
		}
		if (socket != null) {
			this.connectionController.setSocket(socket, true);

			if ((socket = portIsOpen(host, this.connectionController.SYNCHRO_PORT, 1000)) == null) return null;
			this.connectionController.setSocket(socket, false);
		}
		return null;
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