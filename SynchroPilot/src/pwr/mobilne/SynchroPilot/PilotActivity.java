package pwr.mobilne.SynchroPilot;

import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import pwr.mobilne.SynchroPilot.model.PilotListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;

public class PilotActivity extends Activity {
	private OnClickListener pilotListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ConnectionController.getInstance().prepareSocket(getApplicationContext());
		startService(new Intent(this, SynchroService.class));

		pilotListener = new PilotListener();
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
	}
}
