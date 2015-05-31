package view;

import java.util.Comparator;

import org.json.simple.JSONObject;

class JSONComparator implements Comparator<JSONObject> {

	public int compare(JSONObject a, JSONObject b) {
		String a_time = (String) a.get("date_sent");
		String b_time = (String) b.get("date_sent");
		
		long date1 = Long.parseLong(a_time);
		long date2 = Long.parseLong(b_time);
		
		return Long.compare(date1, date2);

	}

};