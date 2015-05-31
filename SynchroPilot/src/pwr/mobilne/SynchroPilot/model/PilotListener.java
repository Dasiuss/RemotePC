package pwr.mobilne.SynchroPilot.model;

import pwr.mobilne.SynchroPilot.R;
import pwr.mobilne.SynchroPilot.TouchPadActivity;
import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PilotListener implements OnClickListener {

	private ConnectionController con;
	private static PilotListener instance = null;
	private TextView tv = null;

	public PilotListener() {
		con = ConnectionController.getInstance();
	}

	@Override
	public void onClick(View v) {
		Object tag = v.getTag();
		if (v.getId() == R.id.touchpad) {
			Intent intent = new Intent(v.getContext(), TouchPadActivity.class);
			v.getContext().startActivity(intent);

		} else if (v.getId() == R.id.send) {
			con.sendToSocket("sendText^,^" + tv.getText());
			tv.setText("");
		} else {
			if (tag != null) con.sendToSocket((String) tag);
		}
	}

	public static PilotListener getInstance() {
		if (instance == null) instance = new PilotListener();
		return instance;
	}

	public void setTv(TextView tv) {
		this.tv = tv;
	}

}
