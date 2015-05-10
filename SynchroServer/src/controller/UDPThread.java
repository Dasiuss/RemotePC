package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPThread extends Thread {

	@Override
	public void run() {
		DatagramSocket serverSocket;
		System.out.println("UDPThread nas³uchuje");
		try {
			serverSocket = new DatagramSocket(9563);
			serverSocket.setBroadcast(true);
			byte[] receiveData = new byte[1024];
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				int message = Byte.toUnsignedInt(receiveData[0]) << 8 | Byte.toUnsignedInt(receiveData[1]);
				if (message == 12345) {
					String data = "54321";
					DatagramPacket sendPacket = new DatagramPacket(data.getBytes(), data.length(),
							receivePacket.getAddress(), receivePacket.getPort());
					serverSocket.send(sendPacket);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
