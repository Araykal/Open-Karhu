/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

import org.bukkit.util.Vector;

public class PositionEvent extends Event {
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;

	public PositionEvent(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Vector getPositionVector() {
		return new Vector(this.x, this.y, this.z);
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}
}
