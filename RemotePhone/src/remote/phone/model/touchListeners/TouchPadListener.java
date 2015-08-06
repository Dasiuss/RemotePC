package remote.phone.model.touchListeners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import remote.phone.controller.ConnectionController;

public class TouchPadListener implements OnTouchListener {

	private ConnectionController con;
	private int oldX, oldY;
	private Vibrator vibrator;
	private int vibrateTime = 20;
	private boolean moved;

	public TouchPadListener(Context context) {
		this.con = ConnectionController.getInstance();
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		con.prepareSocket();

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {// touchpad
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Write your code to perform an action on down
			oldX = x;
			oldY = y;
			moved = false;
			break;
		case MotionEvent.ACTION_MOVE:
			// Write your code to perform an action on contineus touch move
			int diffX = (x - oldX);
			int diffY = (y - oldY);
			if (diffX != 0 || diffY != 0) {
				con.sendToUDPSocket("movet2;" + diffX + ";" + diffY + ";");
				oldX = x;
				oldY = y;
				moved = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			// Write your code to perform an action on touch up
			if (!moved) {
				con.sendToSocket("LPM");
				vibrator.vibrate(vibrateTime);
			}
			break;
		}
		return true;
	}
}
