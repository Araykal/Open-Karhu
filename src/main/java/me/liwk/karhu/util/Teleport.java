/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

public class Teleport {
	public final TeleportPosition position;
	public boolean accepted = false;
	public boolean moved = false;

	public Teleport(TeleportPosition position) {
		this.position = position;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
