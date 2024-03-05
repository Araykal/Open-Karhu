/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.impl;

import me.liwk.karhu.api.event.KarhuEvent;
import org.bukkit.entity.Player;

public final class KarhuPlayerUnregisterEvent extends KarhuEvent {
	private final Player player;

	@Override
	public boolean isCancellable() {
		return false;
	}

	public KarhuPlayerUnregisterEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}
}
