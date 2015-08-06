package remote.phone.view;

import remote.phone.R;
import remote.phone.controller.ConnectionController;
import remote.phone.model.MenuListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends Activity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too
	 * memory intensive, it may be best to switch to a {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return new Tab1();
			} else if (position == 1) {
				return new Tab2();
			}
			return new Tab1();
		}

		@Override
		public int getCount() {
			return 2;
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		ConnectionController.getInstance().closeSocket();
	}

	@Override
	protected void onResume() {
		ConnectionController.getInstance().prepareSocket();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuListener menuListener = new MenuListener();
		menu.add("monitor").setOnMenuItemClickListener(menuListener);
		menu.add("projektor").setOnMenuItemClickListener(menuListener);
		return true;
	}

}
