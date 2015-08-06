package connection;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {

	private Socket client;
	private Scanner in;
	private Robot robot;
	private String au3;

	public ClientThread(Socket client, String au3) {
		this.au3 = au3;
		this.client = client;
	}

	@Override
	public void run() {
		try {

			in = new Scanner(client.getInputStream(), "UTF-8");
		} catch (IOException ex) {
			System.out.println("Niemozna utworzyc wejscia i wyjscia");
			return;
		}

		try {
			robot = new Robot();
		} catch (AWTException e1) {
			System.out.println("nie uda³o siê zrobic robota");
			e1.printStackTrace();
			return;
		}

		while (!client.isClosed() && in.hasNextLine()) {
			try {
				String command = in.nextLine();
				System.out.println(command);

				if (command.equals("volumeMute") || command.equals("volumeUp") || command.equals("volumeDown")
						|| command.equals("mediaPlay") || command.equals("mediaStop") || command.equals("mediaPrev")
						|| command.equals("mediaNext") || command.equals("shutdown")) {

					Runtime.getRuntime().exec(au3 + " " + command);

				} else if (command.equals("winamp")) {
					Runtime.getRuntime().exec("C:\\Program Files (x86)\\Winamp\\winamp.exe");
				} else if (command.equals("bestPlayer")) {
					Runtime.getRuntime().exec("C:\\bestplayer.exe");
				} else if (command.equals(" ")) {
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
				} else if (command.equals("sleep")) {
					Runtime.getRuntime().exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
				} else if (command.equals("altTab")) {
					robot.keyPress(KeyEvent.VK_ALT);
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ALT);
				} else if (command.equals("refresh")) {
					robot.keyPress(KeyEvent.VK_F5);
					robot.keyRelease(KeyEvent.VK_F5);
				} else if (command.equals("back")) {
					System.out.println("bb");
					robot.keyPress(KeyEvent.VK_BACK_SPACE);
					robot.keyRelease(KeyEvent.VK_BACK_SPACE);
				} else if (command.equals("esc")) {
					robot.keyPress(KeyEvent.VK_ESCAPE);
					robot.keyRelease(KeyEvent.VK_ESCAPE);
				} else if (command.equals("monitor")) {// monitor
					robot.keyPress(KeyEvent.VK_WINDOWS);
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_WINDOWS);
					robot.delay(500);
					robot.keyPress(KeyEvent.VK_HOME);
					robot.keyRelease(KeyEvent.VK_HOME);
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				} else if (command.equals("projektor")) {// projektor
					robot.keyPress(KeyEvent.VK_WINDOWS);
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_WINDOWS);
					robot.delay(500);
					robot.keyPress(KeyEvent.VK_END);
					robot.keyRelease(KeyEvent.VK_END);
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				} else if (command.equals("LPM")) {
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				} else if (command.equals("PPM")) {
					robot.mousePress(InputEvent.BUTTON3_MASK);
					robot.mouseRelease(InputEvent.BUTTON3_MASK);
				} else if (command.equals("SPM")) {
					robot.mousePress(InputEvent.BUTTON2_MASK);
					robot.mouseRelease(InputEvent.BUTTON2_MASK);
				} else if (command.equals("scrollDown")) {
					robot.mouseWheel(1);
				} else if (command.equals("scrollUp")) {
					robot.mouseWheel(-1);
				} else if (command.contains("text;")) {
					String[] text = command.split(";");
					Runtime.getRuntime().exec(au3 + " sendText \"" + text[1] + "\"");
				} else {
					System.out.println("nierozpoznano polecenia " + command);
				}
			} catch (IOException e) {
				System.out.println("niema linii " + e.getMessage());
			}
		}
		System.out.println("end");
	}

}
