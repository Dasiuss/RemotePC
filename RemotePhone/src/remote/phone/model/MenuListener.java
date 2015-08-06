package remote.phone.model;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import remote.phone.controller.ConnectionController;

public class MenuListener implements OnMenuItemClickListener {

	private ConnectionController con;

	public MenuListener() {
		con = ConnectionController.getInstance();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// con.sendToSocket(String.valueOf(item.getTitle()));

		if (item.getTitle().equals("projektor")) {
			con.sendToSocket("projektor");
		} else if (item.getTitle().equals("monitor")) {
			con.sendToSocket("monitor");
		}
		return true;
	}

}
