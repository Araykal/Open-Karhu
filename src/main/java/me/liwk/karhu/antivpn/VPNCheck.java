/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.antivpn;

import java.net.InetAddress;
import java.util.HashMap;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.KarhuLogger;
import me.liwk.karhu.util.json.JsonReader;

public class VPNCheck {
	private static final HashMap<String, Boolean> cachedIPs = new HashMap<>();

	public static boolean checkAddress(InetAddress inetAddress) {
		String ip = inetAddress.getHostAddress();
		String address = inetAddress.getHostName();
		if (cachedIPs.containsKey(ip)) {
			return cachedIPs.get(ip);
		} else {
			try {
				checkVPN(ip);
			} catch (Exception var4) {
				KarhuLogger.critical("ip check services down? message: " + var4.getMessage());
				var4.printStackTrace();
			}

			return cachedIPs.containsKey(address) && cachedIPs.get(address);
		}
	}

	private static void checkVPN(String address) throws Exception {
		String[] dataFromIP = JsonReader.getData(address);
		if (dataFromIP[0] != null && dataFromIP[2] != null) {
			boolean proxy = Boolean.parseBoolean(dataFromIP[0]);
			boolean risk = Boolean.parseBoolean(dataFromIP[2]);
			if (proxy && Karhu.getInstance().getConfigManager().isProxycheck()) {
				cachedIPs.put(address, true);
			} else if (risk && Karhu.getInstance().getConfigManager().isMaliciouscheck()) {
				cachedIPs.put(address, true);
			} else {
				cachedIPs.put(address, false);
			}
		}
	}
}
