/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.manager.alert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.entity.Player;

public final class AlertsManager {
	private final Set<UUID> debugToggled = Collections.synchronizedSet(new HashSet<>());
	private final Set<UUID> miscDebugToggled = Collections.synchronizedSet(new HashSet<>());
	private final Set<UUID> alertsToggled = Collections.synchronizedSet(new HashSet<>());
	private final Set<UUID> setbackToggled = Collections.synchronizedSet(new HashSet<>());
	private final Set<UUID> mitigationToggled = Collections.synchronizedSet(new HashSet<>());
	public static final List<UUID> ADMINS = Arrays.asList(
		UUID.fromString("22a4bdba-67c3-4635-8256-0944540124f3"),
		UUID.fromString("8509d6d5-7ab0-432b-8bee-6d4835c26794"),
		UUID.fromString("fb754490-1316-4cb9-bca5-2de00212cf49"),
		UUID.fromString("3c882a25-fc17-4757-8249-ea217d13dd62"),
		UUID.fromString("4e53902f-eda1-4cbe-8306-337c8930e307")
	);

	public boolean hasAlertsToggled(UUID uuid) {
		return this.alertsToggled.contains(uuid);
	}

	public boolean hasDebugToggled(Player player) {
		return this.debugToggled.contains(player.getUniqueId());
	}

	public boolean hasMiscDebugToggled(Player player) {
		return this.miscDebugToggled.contains(player.getUniqueId());
	}

	public boolean hasSetbackToggled(Player player) {
		return this.setbackToggled.contains(player.getUniqueId());
	}

	public boolean hasMitigationToggled(Player player) {
		return this.mitigationToggled.contains(player.getUniqueId());
	}

	public void toggleAlerts(Player player) {
		if (!this.alertsToggled.remove(player.getUniqueId())) {
			this.alertsToggled.add(player.getUniqueId());
			if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
				Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getName(), 1));
			} else {
				Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getUniqueId().toString(), 1));
			}
		} else if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
			Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getName(), 0));
		} else {
			Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getUniqueId().toString(), 0));
		}
	}

	public void removeFromList(UUID uuid) {
		this.alertsToggled.remove(uuid);
	}

	public void toggleDebug(Player player) {
		if (!this.debugToggled.remove(player.getUniqueId())) {
			this.debugToggled.add(player.getUniqueId());
		}
	}

	public void toggleMiscDebug(Player player) {
		if (!this.miscDebugToggled.remove(player.getUniqueId())) {
			this.miscDebugToggled.add(player.getUniqueId());
		}
	}

	public void toggleSetback(Player player) {
		if (!this.setbackToggled.remove(player.getUniqueId())) {
			this.setbackToggled.add(player.getUniqueId());
		}
	}

	public void toggleMitigation(Player player) {
		if (!this.mitigationToggled.remove(player.getUniqueId())) {
			this.mitigationToggled.add(player.getUniqueId());
		}
	}

	public void setReceiveAlerts(Player player, boolean state) {
		if (!state) {
			this.alertsToggled.remove(player.getUniqueId());
			if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
				Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getName(), 0));
			} else {
				Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getUniqueId().toString(), 0));
			}
		} else {
			this.alertsToggled.add(player.getUniqueId());
			if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
				Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getName(), 1));
			} else {
				Tasker.taskAsync(() -> Karhu.getStorage().setAlerts(player.getUniqueId().toString(), 1));
			}
		}
	}

	public Set<UUID> getAlertsToggled() {
		return this.alertsToggled;
	}

	public Set<UUID> getDebugToggled() {
		return this.debugToggled;
	}

	public Set<UUID> getMiscDebugToggled() {
		return this.miscDebugToggled;
	}

	public Set<UUID> getSetbackToggled() {
		return this.setbackToggled;
	}

	public Set<UUID> getMitigationToggled() {
		return this.mitigationToggled;
	}
}
