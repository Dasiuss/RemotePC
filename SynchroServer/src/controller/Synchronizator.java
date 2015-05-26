package controller;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Synchronizator {
	Socket application;
	FileOutputStream fos;

	public Synchronizator (Socket app) {
		this.application = app;
	}

	public void receiveFile () {
		byte [] mybytearray = new byte [65536]; // TODO size hardcoded, need to send file size
		InputStream is;
		try {
			is = application.getInputStream ();
			fos = new FileOutputStream ("C:/updates.txt"); // TODO file path
			BufferedOutputStream bos = new BufferedOutputStream (fos);
			int bytesRead = is.read (mybytearray, 0, mybytearray.length);
			int current = bytesRead;
			do {
				bytesRead = is.read (mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);

			bos.write (mybytearray, 0, current);
			bos.flush ();
			bos.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}

	public void compare () {
		/*
		 * TODO 1) Compare received file with list of existing files 
		 * 		2) a) No differences: sendResponse ("null") 
		 * 		   b) lack of file: sendResponse ("send"), then sendResponse (fileName), receiveFile (); 
		 * 		   c) newer file: sentResponse ("read"), then sendResponse (fileName), sendFile (file); 
		 * 		3) Repeat 2) until no differences noticed
		 */
	}

	public void sendResponse (String message) { // send "null" "send" or "read" to app
		try {
			OutputStream os = application.getOutputStream ();
			OutputStreamWriter osw = new OutputStreamWriter (os);
			BufferedWriter bw = new BufferedWriter (osw);
			bw.write (message);
			os.close ();
			osw.close ();
			bw.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}

	public void sendFile (File file) {
		try {
			FileInputStream fis = new FileInputStream (file);
			DataInputStream dis = new DataInputStream (fis);
			DataOutputStream out = new DataOutputStream (application.getOutputStream ());
			byte [] buffor = new byte [1024];
			int count;
			while ((count = dis.read (buffor, 0, buffor.length)) > 0) {
				out.write (buffor, 0, count);
			}

			fis.close ();
			dis.close ();
			out.close ();
		} catch (FileNotFoundException e) {
			// TODO Log that file was not found
			e.printStackTrace ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}

	}
}