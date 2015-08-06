package remote.phone.view;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import remote.phone.R;
import remote.phone.model.MenuListener;
import remote.phone.model.TouchPadActivityListener;
import remote.phone.model.touchListeners.RollListener;
import remote.phone.model.touchListeners.TouchPadListener;

public class TouchPadActivity extends Activity {
	private GestureOverlayView gov;
	private Button roll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_touchpad);
	}

	@Override
	public void onResume() {
		super.onResume();

		TouchPadActivityListener touchPadActivityListener = new TouchPadActivityListener();
		TouchPadListener touchPadListener = new TouchPadListener(getApplicationContext());

		RollListener rollListener = new RollListener(getApplicationContext());

		touchPadActivityListener.setView(this);

		gov = ((GestureOverlayView) findViewById(R.id.touchpad));
		gov.setOnTouchListener(touchPadListener);

		roll = ((Button) findViewById(R.id.kulka));
		roll.setOnTouchListener(rollListener);
		roll.setOnClickListener(touchPadActivityListener);

		findViewById(R.id.enterB).setOnClickListener(touchPadActivityListener);
		findViewById(R.id.escB).setOnClickListener(touchPadActivityListener);
		findViewById(R.id.backB).setOnClickListener(touchPadActivityListener);
		findViewById(R.id.altTabB).setOnClickListener(touchPadActivityListener);
		findViewById(R.id.refreshB).setOnClickListener(touchPadActivityListener);

		findViewById(R.id.PPM).setOnClickListener(touchPadActivityListener);
		findViewById(R.id.LPM).setOnClickListener(touchPadActivityListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuListener menuListener = new MenuListener();
		menu.add("monitor").setOnMenuItemClickListener(menuListener);
		menu.add("projektor").setOnMenuItemClickListener(menuListener);
		return true;
	}
}
