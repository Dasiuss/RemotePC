package pwr.mobilne.SynchroPilot.model;

import pwr.mobilne.SynchroPilot.R;
import pwr.mobilne.SynchroPilot.controller.ConnectionController;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchListener implements OnTouchListener {

	private static TouchListener instance = null;
	private ConnectionController con = ConnectionController.getInstance();
	private float oldX;
	private float oldY;
	private boolean moved;

	public static TouchListener getInstance() {
		if (instance == null) instance = new TouchListener();
		return instance;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {// touchpad i rolka
		float y = event.getY();
		float x = event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Write your code to perform an action on down
			moved = false;
			oldY = y;
			oldX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			// Write your code to perform an action on contineus touch move
			int diff = (int) (y - oldY);
			if (diff != 0) moved = true;
			if (v.getId() == R.id.touchpadSurface) {
				int diffX = (int) (x - oldX);
				int diffY = (int) (y - oldY);
				if (diffX != 0 || diffY != 0) moved = true;
				con.sendToSocket("move;" + diffX + ";" + diffY + ";");
				oldX = x;
				oldY = y;
			} else if (v.getId() == R.id.roll) {
				if (diff > 5) {
					con.sendToSocket("scrollUp");
					oldY = y;
				} else if (diff < -5) {
					con.sendToSocket("scrollDown");
					oldY = y;
				}
			} else if (v.getId() == R.id.volumeDownB || v.getId() == R.id.volumeUpB || v.getId() == R.id.volumeMuteB) {
				if (diff > 5) {
					con.sendToSocket("volumeDown");
					oldY = y;
				} else if (diff < -5) {
					con.sendToSocket("volumeUp");
					oldY = y;
				}
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
