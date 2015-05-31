package pwr.mobilne.SynchroPilot;

import pwr.mobilne.SynchroPilot.model.PilotListener;
import pwr.mobilne.SynchroPilot.model.TouchListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TouchPadActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_pad);

		TouchListener touchListener = TouchListener.getInstance();
		PilotListener pilotListener = PilotListener.getInstance();

		pilotListener.setTv((TextView) findViewById(R.id.sendText));

		TextView touchpad = (TextView) findViewById(R.id.touchpadSurface);
		touchpad.setOnTouchListener(touchListener);
		touchpad.setOnClickListener(pilotListener);

		Button roll = (Button) findViewById(R.id.roll);
		roll.setOnTouchListener(touchListener);
		roll.setOnClickListener(pilotListener);

		findViewById(R.id.ppm).setOnClickListener(pilotListener);
		findViewById(R.id.lpm).setOnClickListener(pilotListener);
		findViewById(R.id.enter).setOnClickListener(pilotListener);
		findViewById(R.id.send).setOnClickListener(pilotListener);
		findViewById(R.id.esc).setOnClickListener(pilotListener);

	}

	@Override
	public void onClick(View v) {

	}

}
