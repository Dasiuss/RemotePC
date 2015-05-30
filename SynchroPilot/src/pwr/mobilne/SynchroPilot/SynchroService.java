package pwr.mobilne.SynchroPilot;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.AsynchronousCloseException;

import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class SynchroService extends Service {

	ConnectionController con;
	Socket server;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	FileOutputStream fos;
	BufferedReader in;
	PrintWriter out;

	Notification.Builder notificationBuilder;
	Notification notification;
	NotificationManager notificationManager;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int StartId) {
		con = ConnectionController.getInstance();
		con.prepareSocket(getApplicationContext());
		while (con.isSocketReady() == false) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		server = con.getSynchroSocket();
		Log.i("Serivce connection", "Service connected");
		try {
			out = new PrintWriter(server.getOutputStream(),true);  
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			Log.d ("Service started", "Streams creation complete");
			main();
		}catch (IOException e) {
			e.printStackTrace ();
		}

		/*
		 * Order: 1) Send file with files paths and last update date 2) Read respons which is: a) null - everything is
		 * awesome, no need to sending anything, shut down service b) send - read second message with file name/path and
		 * then send it, repeat 2) c) read - read second message with file name/path and then read it, repeat 2)
		 */
		return START_STICKY;
	}

	public void main() {
		Task t = new Task ();
		t.execute (in);
		//System.out.println (readResponse ());
		// try {
		notificationProgressBar();
		// wait (5000);
		updatePrograes(50);
		// wait(5000);
		updatePrograes(100);
		// } catch (InterruptedException e) {e.printStackTrace ();}
		/*
		 * sendFile (new File ("ListOfFiles"));//TODO file while (true) react (readResponse ());
		 */

	}

	public void sendFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			DataOutputStream out = new DataOutputStream(server.getOutputStream());
			byte[] buffor = new byte[1024];
			int count;
			while ((count = dis.read(buffor, 0, buffor.length)) > 0) {
				out.write(buffor, 0, count);
			}

			fis.close();
			dis.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Log that file was not found
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readFile(String fileName) {
		byte[] mybytearray = new byte[65536]; // TODO size hardcoded, need to send file size
		InputStream is;
		try {
			is = server.getInputStream();
			fos = new FileOutputStream(fileName);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray, 0, mybytearray.length);
			int current = bytesRead;
			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0) current += bytesRead;
			} while (bytesRead > -1);

			bos.write(mybytearray, 0, current);
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readResponse() {
		String message = "null";
		try {
			while (in.ready () == false) {
				Thread.sleep (1000);
			}
			message = in.readLine();
			Log.d ("Servce readResponse", "Message received");
		} catch (IOException e) {
			Log.e ("No kurwa error", "IOException");
			e.printStackTrace ();
			return message;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return message;
	}

	public void react(String message) throws IOException {
		if (message == "null") {
			in.close ();
			out.close ();
			updatePrograes(100);
			stopSelf();
		}
		if (message == "send") {
			String fileName = readResponse();
			File f = new File(fileName); // TODO path to file
			sendFile(f);
		}
		if (message == "read") {
			String fileName = readResponse();
			readFile(fileName);
		}
	}

	public void updatePrograes(int progress) {
		if (progress < 100) {
			notificationBuilder.setProgress(100, progress, false);

			// Send the notification:
			notification = notificationBuilder.build();
			notificationManager.notify(100, notification);
		} else {
			notificationBuilder.setContentText("Synchronizing complete");
			notificationBuilder.setProgress(0, 0, false);
			// Send the notification:
			notification = notificationBuilder.build();
			notificationManager.notify(100, notification);
		}
	}

	public void notificationProgressBar() {
		Integer notificationID = 100;

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Set notification information:
		notificationBuilder = new Notification.Builder(getApplicationContext());
		notificationBuilder.setContentTitle("Synchronizing").setContentText("Synchronizing in progress")
				.setProgress(100, 0, false);

		// Send the notification:
		notification = notificationBuilder.build();
		notificationManager.notify(notificationID, notification);
	}

	// ----------------------JUST FOR TESTS-------------
	public void sendResponse(String message) { // send "null" "send" or "read" to app
		out.println(message);
	}

	
	class Task extends AsyncTask <BufferedReader, Void, Void> {

		@Override
		protected Void doInBackground (BufferedReader... params) {
			BufferedReader in = params [0];
			Log.d ("ASYNCTASK", "doInBackground started");
				String message = "null";
				try {
					while (in.ready () == false) {
						Thread.sleep (1000);
					}
					message = in.readLine();
					Log.d ("Servce readResponse", "Message received");
				} catch (IOException e) {
					Log.e ("No kurwa error", "IOException");
					e.printStackTrace ();
					
				} catch (InterruptedException e) {
					Log.e ("No kurwa error", "InterruptedException");
					e.printStackTrace();
				}
		        System.out.println (message);
			
			return null;
		}
		
	}
}
