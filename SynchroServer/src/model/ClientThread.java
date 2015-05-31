package model;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
			command = command.split("\n")[0];
			command = command.split("\r")[0];
			System.out.println(command);

			if (command.equals("volumeMute") || command.equals("volumeUp") || command.equals("volumeDown")
					|| command.equals("mediaPlay") || command.equals("mediaStop") || command.equals("mediaPrev")
					|| command.equals("mediaNext") || command.startsWith("sendText^,^")) {
				Commander.getInstance().command(command + ";");
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
			} else if (command.equals("lpm")) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			} else if (command.equals("ppm")) {
				robot.mousePress(InputEvent.BUTTON3_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
			} else if (command.equals("spm")) {
				robot.mousePress(InputEvent.BUTTON2_MASK);
				robot.mouseRelease(InputEvent.BUTTON2_MASK);
			} else if (command.equals("scrollDown")) {
				robot.mouseWheel(1);
			} else if (command.equals("scrollUp")) {
				robot.mouseWheel(-1);
			} else {
				JSONObject json = (JSONObject) JSONValue.parse(command);
				if (json.containsKey("inbox")) {
					// TODO tu wywo³aj swoj¹ metodê Mateusz
				} else if (json.containsKey("contactsJson")) {
					// TODO tu wywo³aj inn¹ swoj¹ metodê Mateusz
				}
				System.out.println("nierozpoznano polecenia " + command);
			}
		}
	}
}
