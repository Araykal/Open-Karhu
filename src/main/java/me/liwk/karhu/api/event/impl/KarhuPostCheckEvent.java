/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.impl;

import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.event.KarhuEvent;
import org.bukkit.entity.Player;

public final class KarhuPostCheckEvent extends KarhuEvent implements RawPacketInspectableEvent {
	private final boolean failed;
	private final CheckData check;
	private final Player player;
	private final Object packet;

	@Override
	public boolean isCancellable() {
		return false;
	}

	@Override
	public Object getPacket() {
		return this.packet;
	}

	public KarhuPostCheckEvent(boolean failed, CheckData check, Player player, Object packet) {
		this.failed = failed;
		this.check = check;
		this.player = player;
		this.packet = packet;
	}

	public boolean isFailed() {
		return this.failed;
	}

	public CheckData getCheck() {
		return this.check;
	}

	public Player getPlayer() {
		return this.player;
	}
}
