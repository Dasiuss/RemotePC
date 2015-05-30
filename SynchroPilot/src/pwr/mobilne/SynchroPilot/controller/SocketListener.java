package pwr.mobilne.SynchroPilot.controller;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

@SuppressWarnings("unchecked")
class SocketListener extends AsyncTask<BufferedReader, Void, Void> {

	public static final String INBOX = "content://sms/inbox";
	public static final String SENT = "content://sms/sent";
	public static final String DRAFT = "content://sms/draft";

	/**
	 * 
	 */
	private final ConnectionController connectionController;

	/**
	 * @param connectionController
	 */
	SocketListener(ConnectionController connectionController) {
		this.connectionController = connectionController;
	}

	@Override
	protected Void doInBackground(BufferedReader... paramArrayOfParams) {
		BufferedReader in = paramArrayOfParams[0];
		while (true) {
			try {
				while (in.ready()) {
					String read = in.readLine();
					JSONObject json = (JSONObject) JSONValue.parse(read);

					if (json.containsKey("sendSMS")) {
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage((String) json.get("number"), null, (String) json.get("message"), null, null);

					} else if (json.containsKey("getSMS")) {

						JSONObject smsListJson = new JSONObject();

						smsListJson.put("inbox", getSmsList(INBOX));
						smsListJson.put("sent", getSmsList(SENT));
						smsListJson.put("draft", getSmsList(DRAFT));
						connectionController.sendToSocket(smsListJson.toJSONString());

					} else if (json.containsKey("getContacts")) {
						connectionController.sendToSocket(getContactList().toJSONString());
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONArray getSmsList(String uri) {
		JSONArray smsList = new JSONArray();

		Cursor cursor = connectionController.context.getContentResolver().query(Uri.parse(uri), null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				JSONObject sms = new JSONObject();
				for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
					sms.put(cursor.getColumnName(idx), cursor.getString(idx));
				}
				smsList.add(sms);
			} while (cursor.moveToNext());
		}
		return smsList;
	}

	public JSONObject getContactList() {
		Cursor phones = connectionController.context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		JSONObject json = new JSONObject();
		while (phones.moveToNext()) {
			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			json.put(phoneNumber, name);
		}
		phones.close();
		return json;
	}

}