/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.manager;

import com.github.retrooper.packetevents.protocol.player.User;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import org.bukkit.entity.Player;

public final class PlayerDataManager {
	private final Map<UUID, KarhuPlayer> playerDataMap = new ConcurrentHashMap<>();
	private final Karhu karhu;

	public PlayerDataManager(Karhu karhu) {
		this.karhu = karhu;
	}

	public KarhuPlayer getPlayerData(Player player) {
		return this.playerDataMap.get(player.getUniqueId());
	}

	public KarhuPlayer getPlayerData(User user) {
		return this.playerDataMap.get(user.getUUID());
	}

	public KarhuPlayer getPlayerData(UUID uuid) {
		return this.playerDataMap.get(uuid);
	}

	public KarhuPlayer remove(UUID uuid) {
		KarhuPlayer data = this.getPlayerData(uuid);
		if (data != null) {
			data.setRemovingObject(true);
			Karhu.getInstance().getThreadManager().shutdownThread(data);
		}

		return this.playerDataMap.remove(uuid);
	}

	public KarhuPlayer add(UUID uuid, long now) {
		return this.playerDataMap.put(uuid, new KarhuPlayer(uuid, this.karhu, now));
	}

	public Map<UUID, KarhuPlayer> getPlayerDataMap() {
		return this.playerDataMap;
	}
}
