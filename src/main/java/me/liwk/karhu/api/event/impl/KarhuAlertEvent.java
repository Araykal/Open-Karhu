/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.impl;

import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.data.DebugData;
import me.liwk.karhu.api.event.KarhuEvent;
import org.bukkit.entity.Player;

public final class KarhuAlertEvent extends KarhuEvent {
	private final Player player;
	private final CheckData check;
	private final DebugData debug;
	private final int violations;

	public Player getPlayer() {
		return this.player;
	}

	public CheckData getCheck() {
		return this.check;
	}

	public DebugData getDebug() {
		return this.debug;
	}

	public int getViolations() {
		return this.violations;
	}

	public KarhuAlertEvent(Player player, CheckData check, DebugData debug, int violations) {
		this.player = player;
		this.check = check;
		this.debug = debug;
		this.violations = violations;
	}
}
