package connection;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPThread implements Runnable {

	private Robot robot;

	@Override
	public void run() {
		DatagramSocket serverSocket;
		System.out.println("UDPThread nas³uchuje");
		try {
			serverSocket = new DatagramSocket(45677);
			byte[] receiveData = new byte[1024];
			String command;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
				return;
			}
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				command = new String(receivePacket.getData());
				if (command.contains("movet1;")) {
					String[] coords = command.split(";");
					Point mousePos = MouseInfo.getPointerInfo().getLocation();
					try {
						int newCoordX = mousePos.x + Integer.parseInt(coords[1]) / 15;
						int newCoordY = mousePos.y + Integer.parseInt(coords[2]) / 25;
						/*
						 * System.out.println(newCoordX+"x"+ newCoordY); /
						 */
						robot.mouseMove(newCoordX, newCoordY);
						// */
					} catch (NumberFormatException e) {
					}
				} else if (command.contains("movet2;")) {
					String[] coords = command.split(";");
					Point mousePos = MouseInfo.getPointerInfo().getLocation();
					try {
						int newCoordX = mousePos.x + Integer.parseInt(coords[1]);
						int newCoordY = mousePos.y + Integer.parseInt(coords[2]);
						/*
						 * System.out.println(coords[1]+"x"+coords[2]+" = "+
						 * newCoordX+"x"+ newCoordY); /
						 */
						robot.mouseMove(newCoordX, newCoordY);
						// */
					} catch (NumberFormatException e) {
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
