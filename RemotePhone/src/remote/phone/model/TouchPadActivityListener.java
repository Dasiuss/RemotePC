package remote.phone.model;

import remote.phone.R;
import remote.phone.controller.ConnectionController;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class TouchPadActivityListener implements OnClickListener {

	private ConnectionController con;
	@SuppressWarnings("unused")
	private Activity view;

	public TouchPadActivityListener() {
		con = ConnectionController.getInstance();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enterB:
			con.sendToSocket("enter");
			break;
		case R.id.backB:
			con.sendToSocket("back");
			break;
		case R.id.refreshB:
			con.sendToSocket("refresh");
			break;
		case R.id.escB:
			con.sendToSocket("esc");
			break;
		case R.id.altTabB:
			con.sendToSocket("altTab");
			break;
		case R.id.LPM:
			con.sendToSocket("LPM");
			break;
		case R.id.PPM:
			con.sendToSocket("PPM");
			break;
		case R.id.kulka:
			con.sendToSocket("SPM");
			break;
		}
	}

	public void setView(Activity touchPadActivity) {
		this.view = touchPadActivity;
	}

}
