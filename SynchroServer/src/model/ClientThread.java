package model;

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

			System.out.println("got client");

		} catch (IOException e) {
			System.err.println("cannot get streams");
			e.printStackTrace();
		}
	}
}
