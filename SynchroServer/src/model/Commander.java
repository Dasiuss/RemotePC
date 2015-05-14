package model;

import java.io.IOException;
import java.io.PrintWriter;

public class Commander {
	public Process cmd;
	private static Commander instance = null;
	private PrintWriter pWriter;

	public Commander() {
		try {
			cmd = Runtime.getRuntime().exec("commander.exe");
		} catch (IOException e) {
			System.out.println("file commander.exe not found!");
			e.printStackTrace();
			return;
		}
		pWriter = new PrintWriter(cmd.getOutputStream(), true);

	}

	public static void main(String[] args) {
		Commander comm = new Commander();
		comm.command("volumeMute");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		comm.cmd.destroy();
	}

	public void command(String command) {
		pWriter.println(command);
	}

	public static Commander getInstance() {
		if (instance == null) instance = new Commander();
		return instance;
	}
}
