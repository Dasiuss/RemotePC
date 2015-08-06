package remote.phone.model.touchListeners;

import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import remote.phone.controller.ConnectionController;

public class VolumeListener implements OnTouchListener {

	private ConnectionController con;
	private float oldY;
	private boolean moved = false;
	private Vibrator vibrator;
	private int vibrateTime = 20;

	public VolumeListener(Context context) {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		this.con = ConnectionController.getInstance();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {// touchpad
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Write your code to perform an action on down
			oldY = y;
			moved = false;
			break;
		case MotionEvent.ACTION_MOVE:
			// Write your code to perform an action on contineus touch move
			int diff = (int) (oldY - y);
			if (diff > 15) {
				con.sendToSocket("volumeUp");
				moved = true;
				vibrator.vibrate(vibrateTime);
				oldY = y;
			} else if (diff < -15) {
				con.sendToSocket("volumeDown");
				moved = true;
				vibrator.vibrate(vibrateTime);
				oldY = y;
			}
			break;
		case MotionEvent.ACTION_UP:
			// Write your code to perform an action on touch up
			if (!moved) v.performClick();
			break;
		}
		return true;
	}
}
