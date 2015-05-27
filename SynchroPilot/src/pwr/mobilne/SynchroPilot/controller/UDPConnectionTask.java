package pwr.mobilne.SynchroPilot.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.os.AsyncTask;
import android.util.Log;

class UDPConnectionTask extends AsyncTask<String, Integer, Void> {

	/**
	 * 
	 */
	private final ConnectionController connectionController;

	/**
	 * @param connectionController
	 */
	UDPConnectionTask(ConnectionController connectionController) {
		this.connectionController = connectionController;
	}

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
				packet = new DatagramPacket(localPortBytes, localPortBytes.length,
						ConnectionManager.getBroadcastAddress(connectionController.context),
						this.connectionController.UDPPORT);
				socket.send(packet);

				byte[] buf = new byte[4];
				packet = new DatagramPacket(buf, buf.length);// wait for response with ipaddress of server
				socket.receive(packet);
				String data = new String(packet.getData());

				Socket socket1 = new Socket();
				socket1.connect(new InetSocketAddress(packet.getAddress().getHostAddress(),
						connectionController.PILOT_PORT), 1000);
				connectionController.setSocket(socket1, true);

				Socket socket2 = new Socket();
				socket2.connect(new InetSocketAddress(packet.getAddress().getHostAddress(),
						connectionController.SYNCHRO_PORT), 1000);
				connectionController.setSocket(socket2, false);

				Log.i("ConnectionController", "Connected to " + packet.getAddress().getHostAddress() + "thanks to udp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}