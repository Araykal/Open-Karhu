/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

import me.liwk.karhu.api.KarhuAPI;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.data.Category;
import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.data.DebugData;
import me.liwk.karhu.api.data.SubCategory;
import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.impl.KarhuAlertEvent;
import me.liwk.karhu.api.event.impl.KarhuBanEvent;
import me.liwk.karhu.api.event.impl.KarhuInitEvent;
import me.liwk.karhu.api.event.impl.KarhuPlayerRegistrationEvent;
import me.liwk.karhu.api.event.impl.KarhuPlayerUnregisterEvent;
import me.liwk.karhu.api.event.impl.KarhuPostCheckEvent;
import me.liwk.karhu.api.event.impl.KarhuPreCheckEvent;
import me.liwk.karhu.api.event.impl.KarhuPullbackEvent;
import me.liwk.karhu.check.api.Check;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class APICaller {
	private static CheckData checkData = new CheckData();

	public static void callInit(long loadMs) {
		KarhuAPI.getEventRegistry().fireEvent(new KarhuInitEvent(loadMs));
	}

	public static void callRegister(Player player) {
		KarhuAPI.getEventRegistry().fireEvent(new KarhuPlayerRegistrationEvent(player));
	}

	public static void callUnregister(Player player) {
		KarhuAPI.getEventRegistry().fireEvent(new KarhuPlayerUnregisterEvent(player));
	}

	public static boolean callAlert(Player player, CheckInfo chk, Check check, String debug, BaseComponent hover, int violations, int maxvl, long ping) {
		boolean sheesh = false;

		try {
			sheesh = KarhuAPI.getEventRegistry().fireEvent(new KarhuAlertEvent(player, toCheckData(check), new DebugData(debug, hover, ping, maxvl), violations));
		} catch (Exception var11) {
		}

		return sheesh;
	}

	public static boolean callBan(Player player, CheckInfo chk, Check check) {
		boolean sheesh = false;

		try {
			sheesh = KarhuAPI.getEventRegistry().fireEvent(new KarhuBanEvent(player, toCheckData(check)));
		} catch (Exception var5) {
		}

		return sheesh;
	}

	public static void callPostCheck(Player player, CheckInfo chk, Check check, Object packet) {
		try {
			KarhuAPI.getEventRegistry().fireEvent(new KarhuPostCheckEvent(check.isDidFail(), toCheckData(check), player, packet));
		} catch (Exception var5) {
		}
	}

	public static boolean callPreCheck(CheckInfo chk, Check check, Player player, Object packet) {
		boolean sheesh = false;

		try {
			sheesh = KarhuAPI.getEventRegistry().fireEvent(new KarhuPreCheckEvent(player, toCheckData(check), packet));
		} catch (Exception var6) {
		}

		return sheesh;
	}

	public static void callEvent(KarhuEvent event) {
		KarhuAPI.getEventRegistry().fireEvent(event);
	}

	public static boolean callPullback(Player player, CheckInfo chk, Check check, Location to) {
		return KarhuAPI.getEventRegistry().fireEvent(new KarhuPullbackEvent(player, toCheckData(chk), to));
	}

	public static boolean callCancellableEvent(KarhuEvent event) {
		return KarhuAPI.getEventRegistry().fireEvent(event);
	}

	public static CheckData toCheckData(Check check) {
		checkData.setName(check.getName());
		checkData.setDesc(check.getDesc());
		checkData.setCategory(Category.valueOf(check.getCategory().name()));
		checkData.setSubCategory(SubCategory.valueOf(check.getSubCategory().name()));
		checkData.setExperimental(check.isExperimental());
		checkData.setSilent(check.isSilent());
		checkData.setCredits(check.getCredits());
		return checkData;
	}

	public static CheckData toCheckData(CheckInfo info) {
		checkData.setName(info.name());
		checkData.setDesc(info.desc());
		checkData.setCategory(Category.valueOf(info.category().name()));
		checkData.setSubCategory(SubCategory.valueOf(info.subCategory().name()));
		checkData.setExperimental(info.experimental());
		checkData.setSilent(info.silent());
		checkData.setCredits(info.credits());
		return checkData;
	}
}
