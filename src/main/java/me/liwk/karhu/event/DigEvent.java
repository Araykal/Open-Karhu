/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import org.bukkit.util.Vector;

public class DigEvent extends Event {
	private final Vector blockPos;
	private final short direction;
	private final DiggingAction digType;
	private final long now;

	public DigEvent(Vector blockPos, short direction, DiggingAction digType, long now) {
		this.blockPos = blockPos;
		this.direction = direction;
		this.digType = digType;
		this.now = now;
	}

	public Vector getBlockPos() {
		return this.blockPos;
	}

	public short getDirection() {
		return this.direction;
	}

	public DiggingAction getDigType() {
		return this.digType;
	}

	public long getNow() {
		return this.now;
	}
}
