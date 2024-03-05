/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.pending;

import org.bukkit.util.Vector;

public class VelocityPending {
	private boolean markedSent;
	private final short id;
	private final Vector velocity;
	private final boolean kohi;

	public void markSent() {
		this.markedSent = true;
	}

	public VelocityPending(short id, Vector velocity, boolean kohi) {
		this.id = id;
		this.velocity = velocity;
		this.kohi = kohi;
	}

	public boolean isMarkedSent() {
		return this.markedSent;
	}

	public short getId() {
		return this.id;
	}

	public Vector getVelocity() {
		return this.velocity;
	}

	public boolean isKohi() {
		return this.kohi;
	}
}
