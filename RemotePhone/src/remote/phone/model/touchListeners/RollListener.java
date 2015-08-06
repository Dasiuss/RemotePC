package remote.phone.model.touchListeners;

import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import remote.phone.controller.ConnectionController;

public class RollListener implements OnTouchListener {

	private ConnectionController con;
	private float oldY;
	private boolean moved;
	private Vibrator vibrator;
	private int vibrateTime = 20;

	public RollListener(Context context) {
		this.con = ConnectionController.getInstance();
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {// touchpad
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Write your code to perform an action on down
			moved = false;
			oldY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// Write your code to perform an action on contineus touch move
			int diff = (int) (oldY - y);
			if (diff > 15) {
				con.sendToSocket("scrollUp");
				vibrator.vibrate(vibrateTime);
				oldY = y;
				moved = true;
			} else if (diff < -15) {
				con.sendToSocket("scrollDown");
				vibrator.vibrate(vibrateTime);
				oldY = y;
				moved = true;
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
