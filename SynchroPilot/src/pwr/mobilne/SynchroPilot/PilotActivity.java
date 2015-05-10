package pwr.mobilne.SynchroPilot;

import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import android.app.Activity;
import android.os.Bundle;

public class PilotActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ConnectionController.getInstance().prepareSocket(getApplicationContext());
	}
}
