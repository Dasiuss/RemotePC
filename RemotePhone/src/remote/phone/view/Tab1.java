package remote.phone.view;

import remote.phone.R;
import remote.phone.model.Tab1Listener;
import remote.phone.model.touchListeners.VolumeListener;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

public class Tab1 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		// on click settings
		Tab1Listener tab1Listener = new Tab1Listener();
		tab1Listener.setAnimation(AnimationUtils
				.loadAnimation(getActivity().getApplicationContext(), R.anim.anim_alpha));
		View view = getView();
		tab1Listener.setView(view);
		view.findViewById(R.id.winampB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.volumemuteB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.volumeupB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.volumedownB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.mediaplayB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.mediaprevB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.mediastopB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.medianextB).setOnClickListener(tab1Listener);

		view.findViewById(R.id.spaceB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.enterB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.downB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.upB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.leftB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.rightB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.bestplayerB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.touchPad).setOnClickListener(tab1Listener);

		view.findViewById(R.id.sleepB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.shutdownB).setOnClickListener(tab1Listener);
		view.findViewById(R.id.sendB).setOnClickListener(tab1Listener);

		VolumeListener volumeListener = new VolumeListener(getActivity().getApplicationContext());
		view.findViewById(R.id.volumedownB).setOnTouchListener(volumeListener);
		view.findViewById(R.id.volumeupB).setOnTouchListener(volumeListener);

	}

}