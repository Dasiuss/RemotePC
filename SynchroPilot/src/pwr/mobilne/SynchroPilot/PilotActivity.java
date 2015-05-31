package pwr.mobilne.SynchroPilot;

import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import pwr.mobilne.SynchroPilot.model.PilotListener;
import pwr.mobilne.SynchroPilot.model.TouchListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

public class PilotActivity extends Activity {
	private OnClickListener pilotListener;
	private TouchListener touchListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pilot);
		ConnectionController.getInstance().prepareSocket(getApplicationContext());
		// startService(new Intent(this, SynchroService.class));

		pilotListener = PilotListener.getInstance();
		touchListener = TouchListener.getInstance();

		findViewById(R.id.downB).setOnClickListener(pilotListener);
		findViewById(R.id.enterB).setOnClickListener(pilotListener);
		findViewById(R.id.leftB).setOnClickListener(pilotListener);
		findViewById(R.id.mediaNextB).setOnClickListener(pilotListener);
		findViewById(R.id.mediaPlayB).setOnClickListener(pilotListener);
		findViewById(R.id.mediaPrevB).setOnClickListener(pilotListener);
		findViewById(R.id.mediaStopB).setOnClickListener(pilotListener);
		findViewById(R.id.rightB).setOnClickListener(pilotListener);
		findViewById(R.id.spaceB).setOnClickListener(pilotListener);
		findViewById(R.id.upB).setOnClickListener(pilotListener);
		findViewById(R.id.volumeDownB).setOnClickListener(pilotListener);
		findViewById(R.id.volumeMuteB).setOnClickListener(pilotListener);
		findViewById(R.id.volumeUpB).setOnClickListener(pilotListener);
		findViewById(R.id.touchpad).setOnClickListener(pilotListener);
		findViewById(R.id.volumeDownB).setOnTouchListener(touchListener);
		findViewById(R.id.volumeMuteB).setOnTouchListener(touchListener);
		findViewById(R.id.volumeUpB).setOnTouchListener(touchListener);
	}
}
