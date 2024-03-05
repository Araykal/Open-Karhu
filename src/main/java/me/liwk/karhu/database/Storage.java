/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.database;

import java.sql.SQLException;
import java.util.List;
import me.liwk.karhu.check.api.BanWaveX;
import me.liwk.karhu.check.api.BanX;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.check.api.ViolationX;
import me.liwk.karhu.data.KarhuPlayer;

public interface Storage {
	void init() throws SQLException;

	void addAlert(ViolationX violationX);

	void addBan(BanX banX);

	List<ViolationX> getViolations(String string, Check check, int integer3, int integer4, long long5, long long6);

	List<ViolationX> getAllViolations(String string);

	List<String> getBanwaveList();

	boolean isInBanwave(String string);

	void addToBanWave(BanWaveX banWaveX);

	void removeFromBanWave(String string);

	int getViolationAmount(String string);

	void loadActiveViolations(String string, KarhuPlayer karhuPlayer);

	void purge(String string, boolean boolean2);

	int getAllViolationsInStorage();

	List<BanX> getRecentBans();

	void checkFiles();

	void setAlerts(String string, int integer);

	boolean getAlerts(String string);
}
