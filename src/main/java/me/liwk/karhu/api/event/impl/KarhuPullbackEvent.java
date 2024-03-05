/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.impl;

import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.event.KarhuEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class KarhuPullbackEvent extends KarhuEvent {
	private final Player player;
	private final CheckData check;
	private Location to;

	public KarhuPullbackEvent(Player player, CheckData check, Location to) {
		this.player = player;
		this.check = check;
		this.to = to.clone();
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public Player getPlayer() {
		return this.player;
	}

	public CheckData getCheck() {
		return this.check;
	}

	public Location getTo() {
		return this.to;
	}
}
