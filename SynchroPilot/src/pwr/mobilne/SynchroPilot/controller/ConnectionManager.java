package pwr.mobilne.SynchroPilot.controller;

import java.io.IOException;
import java.net.InetAddress;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ConnectionManager {

	public static InetAddress getBroadcastAddress(Context context) throws IOException {
		DhcpInfo dhcp = getDhcpInfo(context);

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public static String getWifiIpAddress(Context context) {
		DhcpInfo dhcp = getDhcpInfo(context);
		String ipAddress = "";
		for (int k = 0; k < 4; k++)
			ipAddress += ((dhcp.ipAddress >> k * 8) & 0xFF) + ".";
		ipAddress = ipAddress.substring(0, ipAddress.length() - 1);
		Log.i("Connection Controller", ipAddress);
		return ipAddress;
	}

	/**
	 * @return
	 */
	public static DhcpInfo getDhcpInfo(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp == null) return null;
		// TODO handle null somehow
		return dhcp;
	}

}
