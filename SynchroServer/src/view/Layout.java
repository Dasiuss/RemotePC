package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.ClientThread;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import controller.ServerListener;

public class Layout {

	private JFrame frame;
	private static JTextArea msgArea = new JTextArea();
	private static Map<String, String> contacts = new HashMap<>();
	private static Map<String, ArrayList<JSONObject>> listofTextPerUser = new HashMap<>();
	private static JSONObject SMS = new JSONObject();
	ClientThread ct ;
	ServerListener sl;
	
	public Layout(){
		sl = ServerListener.getInstance();
		ct = new ClientThread(sl.client);
		ct.getContacts();
		ct.getSMS();
		initialize();
	}
	
	/**
	 * Launch the application.
	 * 
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	public static void getLayout() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Layout window = new Layout();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void genListofTextPerUser(JSONObject item) {
		SMS = item;
		JSONObject[] inbox = getInbox(item);
		JSONObject[] sended = getSended(item);
		for (Entry<String, String> e : contacts.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
			ArrayList<JSONObject> tempList = new ArrayList<>();
			for (int i = 0; i < inbox.length; i++) {
				if (e.getKey().equals(inbox[i].get("address"))) {
					tempList.add(inbox[i]);
				}
			}
			for (int i = 0; i < sended.length; i++) {
				if (e.getKey().equals(sended[i].get("address"))) {
					tempList.add(sended[i]);
				}
			}
			Collections.sort(tempList, new JSONComparator());
			listofTextPerUser.put(e.getValue(), tempList);
		}
	}

 

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton settings_b1 = new JButton("Close");
		settings_b1.setBounds(10, 143, 89, 23);
		frame.getContentPane().add(settings_b1);

		JButton btnMessages = new JButton("Messages");
		btnMessages.setBounds(10, 188, 89, 23);
		frame.getContentPane().add(btnMessages);

		JButton btnSettings = new JButton("Settings");
		btnSettings.setBounds(10, 234, 89, 23);
		frame.getContentPane().add(btnSettings);

		JButton btnSend = new JButton("Send");
		btnSend.setBounds(396, 414, 89, 23);
		frame.getContentPane().add(btnSend);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(127, 316, 519, 67);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(scrollPane_1);

		JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(127, 51, 519, 254);
		frame.getContentPane().add(scrollPane);

		scrollPane.setViewportView(msgArea);
		msgArea.setTabSize(4);
		msgArea.setLineWrap(true);
		msgArea.setEditable(false);

		String[] data = getContactsList();

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(656, 51, 118, 254);
		frame.getContentPane().add(scrollPane_2);
		JList list = new JList(data);
		scrollPane_2.setViewportView(list);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
					msgArea.setText("");
					changeContact((String) list.getSelectedValue());
				}
			}
		});

		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = textArea.getText();
				String date = Calendar.getInstance().getTimeInMillis() + "";
				JSONObject json = new JSONObject();
				
				json.put("body", msg);
				json.put("date_sent", date);
				String address = getByVal((String) list.getSelectedValue());
				String person = (String) list.getSelectedValue();
				json.put("address", address);
				ArrayList<JSONObject> temp = listofTextPerUser.get(person);
				temp.add(json);
				Collections.sort(temp, new JSONComparator());
				listofTextPerUser.put(person, temp);
				sendSMS(address, msg);
				addSentMSG(json, person);
			}
		});

	}

	private String getByVal(String val) {
		for (Entry<String, String> e : contacts.entrySet()) {
			if (e.getValue().equals(val))
				return e.getKey();
		}
		return null;
	}

	private void changeContact(String contact) {
		List<JSONObject> texts = listofTextPerUser.get(contact);
		for (JSONObject l : texts) {
			if (l.get("sent").equals("true")) {
				//add to sent section
				addSentMSG(l, contact);
			} else {
				//add to recived section
				addRecivedMSG(l);
			}
		}
	}

	private void addSentMSG(JSONObject item, String contact) {
		String msg = "___________________________\n";
		long temp = Long.parseLong((String) item.get("date_sent"));
		Date date = new Date(temp);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateToStr = format.format(date);

		msg += dateToStr + "\n";
		msg += item.get("body") + "\n";
		msg += "___________________________\n";
		msgArea.append(msg);
	}

	private void addRecivedMSG(JSONObject item) {
		String msg = "___________________________\n";
		long temp = Long.parseLong((String) item.get("date_sent"));
		Date date = new Date(temp);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateToStr = format.format(date);

		msg += dateToStr + "\n";
		msg += item.get("body") + "\n";
		msg += "___________________________\n";
		msgArea.append(msg);
	}

	private static void getContacts(JSONObject item) {
		contacts = (Map<String, String>) item.clone();
	}

	private static String[] getContactsList() {
		String[] res = new String[contacts.size()];
		int i = 0;
		for (Map.Entry<String, String> e : contacts.entrySet()) {
			res[i++] = e.getValue();
		}
		return res;
	}

	private static JSONObject[] getInbox(JSONObject json) {
		JSONArray inboxArray = (JSONArray) json.get("inbox");
		//teraz mam tablice smsow
		//chce wyciagnaæ pojedyncze smsy i w³o¿yæ je do tablicy jsonow
		JSONObject[] arrayofJSON = new JSONObject[inboxArray.size()];

		for (int i = 0; i < inboxArray.size(); i++) {
			arrayofJSON[i] = (JSONObject) JSONValue.parse((String) inboxArray.get(i));
			arrayofJSON[i].put("sent", "false");
		}
		return arrayofJSON;
	}

	private static JSONObject[] getSended(JSONObject json) {

		JSONArray inboxArray = (JSONArray) json.get("sent");
		//teraz mam tablice smsow
		//chce wyciagnaæ pojedyncze smsy i w³o¿yæ je do tablicy jsonow

		JSONObject[] arrayofJSON = new JSONObject[inboxArray.size()];

		for (int i = 0; i < inboxArray.size(); i++) {
			arrayofJSON[i] = (JSONObject) JSONValue.parse((String) inboxArray.get(i));
			arrayofJSON[i].put("sent", "true");
		}
		return arrayofJSON;

	}
	
	public void sendSMS(String number, String message){
		ct.sendSMS(number, message);
	}

	public static void setSMS(JSONObject item) {
		genListofTextPerUser(item);
	}

	public static void setContacts(JSONObject item) {
		contacts = null;
		getContacts(item);
	}

}
