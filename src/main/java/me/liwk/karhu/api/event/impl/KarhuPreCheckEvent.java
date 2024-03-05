/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.impl;

import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.event.KarhuEvent;
import org.bukkit.entity.Player;

public final class KarhuPreCheckEvent extends KarhuEvent implements RawPacketInspectableEvent {
	private final Player player;
	private final CheckData check;
	private final Object packet;

	@Override
	public Object getPacket() {
		return this.packet;
	}

	public Player getPlayer() {
		return this.player;
	}

	public CheckData getCheck() {
		return this.check;
	}

	public KarhuPreCheckEvent(Player player, CheckData check, Object packet) {
		this.player = player;
		this.check = check;
		this.packet = packet;
	}
}
