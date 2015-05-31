package model;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPClientThread extends Thread {

	private Robot robot;

	@Override
	public void run() {
		DatagramSocket serverSocket;
		System.out.println("UDPClientThread nas³uchuje");
		try {
			serverSocket = new DatagramSocket(9564);
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

				if (command.startsWith("volumeUp") || command.startsWith("volumeDown")) {
					Commander.getInstance().command(command + ";");
				} else if (command.startsWith("scrollDown")) {
					robot.mouseWheel(1);
				} else if (command.startsWith("scrollUp")) {
					robot.mouseWheel(-1);
				} else if (command.startsWith("move;")) {
					String[] coords = command.split(";");
					Point mousePos = MouseInfo.getPointerInfo().getLocation();
					try {
						int newCoordX = mousePos.x + Integer.parseInt(coords[1]);
						int newCoordY = mousePos.y + Integer.parseInt(coords[2]);
						robot.mouseMove(newCoordX, newCoordY);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("unrecognized command" + command);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
