package remote.phone.model;

import remote.phone.R;
import remote.phone.controller.ConnectionController;
import remote.phone.view.TouchPadActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.EditText;

public class Tab1Listener implements OnClickListener {

	private Animation animation;
	private View view;
	private ConnectionController con;
	private EditText sendText;

	public Tab1Listener() {
		con = ConnectionController.getInstance();
	}

	@Override
	public void onClick(View v) {
		v.startAnimation(animation);
		switch (v.getId()) {
		case R.id.winampB:
			con.sendToSocket("winamp");
			break;
		case R.id.bestplayerB:
			con.sendToSocket("bestPlayer");
			break;
		case R.id.mediaplayB:
			con.sendToSocket("mediaPlay");
			break;
		case R.id.volumemuteB:
			con.sendToSocket("volumeMute");
			break;
		case R.id.volumeupB:
			con.sendToSocket("volumeUp");
			break;
		case R.id.volumedownB:
			con.sendToSocket("volumeDown");
			break;
		case R.id.mediaprevB:
			con.sendToSocket("mediaPrev");
			break;
		case R.id.mediastopB:
			con.sendToSocket("mediaStop");
			break;
		case R.id.medianextB:
			con.sendToSocket("mediaNext");
			break;
		case R.id.spaceB:
			con.sendToSocket(" ");
			break;
		case R.id.enterB:
			con.sendToSocket("enter");
			break;
		case R.id.upB:
			con.sendToSocket("up");
			break;
		case R.id.downB:
			con.sendToSocket("down");
			break;
		case R.id.leftB:
			con.sendToSocket("left");
			break;
		case R.id.rightB:
			con.sendToSocket("right");
			break;
		case R.id.sendB:
			String text = sendText.getText().toString();
			sendText.setText("");
			con.sendToSocket("text;" + text + ";");
			break;
		case R.id.sleepB:
			con.sendToSocket("sleep");
			break;
		case R.id.shutdownB:
			con.sendToSocket("shutdown");
			break;
		case R.id.touchPad:
			Intent intent = new Intent(view.getContext(), TouchPadActivity.class);
			view.getContext().startActivity(intent);
			break;
		}

	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setView(View view) {
		this.view = view;
		sendText = (EditText) view.findViewById(R.id.sendText);
	}

}
