package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.ClientThread;

public class ServerListener {
	public final int PORT = 9562;
	public final int SYNCHRO_PORT = 9561;

	public ServerSocket server = null;
	public ServerSocket synchroServer = null;
	public Socket client = null;
	public Socket synchroClient = null;

	public ServerListener() {
		new UDPThread().start();
		try {
			server = new ServerSocket(PORT);
			synchroServer = new ServerSocket(SYNCHRO_PORT);
		} catch (IOException ex) {
			System.out.println("Port zajêty");
			System.exit(-1);
		}
		while (true) {
			try {
				System.out.println("nasluchiwsanie...");
				client = server.accept();
				ClientThread w = new ClientThread(client);
				w.start();
				synchroClient = synchroServer.accept();
				Synchronizator s = new Synchronizator(synchroClient);
				s.start();
			} catch (IOException ex) {
				System.out.println("Nie mo¿na zaakceptowaæ");
			}
		}
	}

	public static void main(String[] args) {
		// Window.startWindow(args);
		new ServerListener();
	}

}
