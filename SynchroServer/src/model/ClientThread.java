package model;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

	private Socket client;
	private Scanner in;
	private PrintStream out;

	public ClientThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			in = new Scanner(client.getInputStream(), "UTF-8");
			out = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("got client");
		Robot robot;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			System.out.println("nie uda³o siê zrobic robota");
			e1.printStackTrace();
			return;
		}

		while (!client.isClosed() && in.hasNextLine()) {
			String command = in.nextLine();
			System.out.println(command);

			if (command.equals("volumeMute") || command.equals("volumeUp") || command.equals("volumeDown")
					|| command.equals("mediaPlay") || command.equals("mediaStop") || command.equals("mediaPrev")
					|| command.equals("mediaNext")) {

			} else if (command.equals("space")) {
				robot.keyPress(KeyEvent.VK_SPACE);
				robot.keyRelease(KeyEvent.VK_SPACE);
			} else if (command.equals("enter")) {
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			} else if (command.equals("up")) {
				robot.keyPress(KeyEvent.VK_UP);
				robot.keyRelease(KeyEvent.VK_UP);
			} else if (command.equals("down")) {
				robot.keyPress(KeyEvent.VK_DOWN);
				robot.keyRelease(KeyEvent.VK_DOWN);
			} else if (command.equals("left")) {
				robot.keyPress(KeyEvent.VK_LEFT);
				robot.keyRelease(KeyEvent.VK_LEFT);
			} else if (command.equals("right")) {
				robot.keyPress(KeyEvent.VK_RIGHT);
				robot.keyRelease(KeyEvent.VK_RIGHT);
			} else {
				System.out.println("nierozpoznano polecenia " + command);
			}
		}
	}
}
