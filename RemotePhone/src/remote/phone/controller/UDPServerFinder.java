package remote.phone.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class UDPServerFinder {

	/**
	 * 
	 */
	private final ConnectionController connectionController;

	UDPServerFinder() {
		this.connectionController = ConnectionController.getInstance();
	}

	public InetAddress getServerIp() {
		DatagramPacket packet;
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			int message = 12345;// sends on broadcast
			byte[] localPortBytes = new byte[2];
			localPortBytes[0] = (byte) ((message >> 8) & 0xFF);
			localPortBytes[1] = (byte) ((message) & 0xFF);
			try {
				packet = new DatagramPacket(localPortBytes, localPortBytes.length,
						ConnectionHelper.getBroadcastAddress(connectionController.context),
						connectionController.UDPCONNECTIONPORT);
				socket.send(packet);

				byte[] buf = new byte[4];
				packet = new DatagramPacket(buf, buf.length);// wait for response with ipaddress of server
				socket.receive(packet);
				return packet.getAddress();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}