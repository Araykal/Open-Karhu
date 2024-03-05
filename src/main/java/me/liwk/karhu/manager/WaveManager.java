/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.manager;

import java.util.ArrayList;
import java.util.UUID;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.api.BanWaveX;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WaveManager {
	private final ArrayList<String> playersToBan = new ArrayList<>();
	private boolean runningBanwave = false;
	private int bans = 0;

	public void startBanwave() {
		Tasker.taskAsync(() -> {
			int max = this.playersToBan.size();
			if (max > 0) {
				if (!this.runningBanwave) {
					this.importFromDb();
				}

				this.runningBanwave = true;
				++this.bans;
				String pre = this.playersToBan.get(0);
				String player = pre.contains("-") ? this.findName(pre) : pre;
				Bukkit.broadcastMessage(Karhu.getInstance().getConfigManager().getBanwaveCaught().replaceAll("%player%", player));
				this.removeFromWave(pre);
				String punish = Karhu.getInstance().getConfigManager().getBanwavePunish().replaceAll("%player%", player);
				if (!Karhu.getInstance().getConfigManager().isBungeeCommand()) {
					Tasker.run(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), punish));
				}

				Tasker.runTaskLaterAsync(this::startBanwave, 20L);
			} else {
				this.completeBanWave();
			}
		});
	}

	public void completeBanWave() {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Karhu.getInstance().getPlug(), () -> {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Karhu.getInstance().getConfigManager().getBanwaveComplete().replaceAll("%bans%", String.valueOf(this.bans)));
			Bukkit.broadcastMessage("");
			this.bans = 0;
			this.runningBanwave = false;
		}, 20L);
	}

	public void addToWave(String uuid, String check) {
		if (!this.playersToBan.contains(uuid)) {
			this.playersToBan.add(uuid);
			BanWaveX bwRequest = new BanWaveX(uuid, check, 1, System.currentTimeMillis());
			Karhu.getStorage().addToBanWave(bwRequest);
		}
	}

	public void removeFromWave(String uuid) {
		this.playersToBan.remove(uuid);
		Karhu.getStorage().removeFromBanWave(uuid);
	}

	public void importFromDb() {
		this.playersToBan.clear();
		this.playersToBan.addAll(Karhu.storage.getBanwaveList());
	}

	private String findName(String arg) {
		Player target = Bukkit.getPlayer(UUID.fromString(arg));
		return target != null ? target.getName() : Bukkit.getOfflinePlayer(UUID.fromString(arg)).getName();
	}

	public ArrayList<String> getPlayersToBan() {
		return this.playersToBan;
	}

	public boolean isRunningBanwave() {
		return this.runningBanwave;
	}

	public int getBans() {
		return this.bans;
	}
}
