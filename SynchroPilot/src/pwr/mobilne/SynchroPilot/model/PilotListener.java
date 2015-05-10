package pwr.mobilne.SynchroPilot.model;

import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import android.view.View;
import android.view.View.OnClickListener;

public class PilotListener implements OnClickListener {

	private ConnectionController con;

	public PilotListener() {
		con = ConnectionController.getInstance();
	}

	@Override
	public void onClick(View v) {
		String tag;
		if ((tag = (String) v.getTag()) != null) con.sendToSocket(tag);
	}
}
