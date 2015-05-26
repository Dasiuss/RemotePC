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
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SynchroService extends Service {

	Socket server;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	FileInputStream fis;
	FileOutputStream fos;
	DataInputStream dis;
	DataOutputStream out;
	
	@Override
	public IBinder onBind (Intent intent) {
		
		return null;
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int StartId) {
		// TODO connecting with server and saving it to socket
		// TODO Do shit
		
		/*
		 * Order:
		 * 1) Send file with files paths and last update date
		 * 2) Read respons which is:
		 * 		a) null - everything is awesome, no need to sending anything, shut down service
		 * 		b) send - read second message with file name/path and then send it, repeat 2)
		 * 		c) read - read second message with file name/path and then read it, repeat 2)
		 */
		return START_STICKY;
	}
	
	public void sendFile (File file) {
		try {
			fis = new FileInputStream (file);
			dis = new DataInputStream(fis);	
			out = new DataOutputStream(server.getOutputStream());
			byte[] buffor = new byte[1024];
			int count;
			while ((count = dis.read(buffor, 0, buffor.length)) > 0) {
				out.write(buffor, 0, count);
			}
			
			fis.close ();
			dis.close ();
			out.close ();
		} catch (FileNotFoundException e) {
			//TODO Log that file was not found
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void readFile (String fileName) {
		byte [] mybytearray  = new byte [65536];	//TODO size hardcoded, need to send file size
	    InputStream is;
		try {
			is = server.getInputStream();		
		    fos = new FileOutputStream (fileName);
		    BufferedOutputStream bos = new BufferedOutputStream(fos);
		    int bytesRead = is.read(mybytearray,0,mybytearray.length);
		    int current = bytesRead;
		    do {
		    	bytesRead =
		        is.read(mybytearray, current, (mybytearray.length-current));
		        if(bytesRead >= 0) current += bytesRead;
		    } while(bytesRead > -1);
	
		      bos.write(mybytearray, 0 , current);
		      bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String readResponse () {
		InputStream is;
		String message;
		try {
			is = server.getInputStream();
	        InputStreamReader isr = new InputStreamReader (is);
	        BufferedReader br = new BufferedReader (isr);
	        message = br.readLine();
	        is.close ();
	        isr.close ();
	        br.close ();
		} catch (IOException e) {
			e.printStackTrace();
			return "null";
		}
        return message;
	}
	
	public void react (String message) {
		if  (message == "null") {
			stopSelf ();
			//TODO notification progress bar stop
		}
		if (message == "send") {
			String fileName = readResponse ();
			File f = new File (fileName); //TODO path to file
			sendFile (f);
			//TODO check for another message from server
		}
		if (message == "read") {
			String fileName = readResponse ();
			readFile (fileName);
			//TODO chceck for another message from server
		}
	}

}
