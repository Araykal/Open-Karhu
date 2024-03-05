/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class VehicleEvent extends Event {
	private final double x;
	private final double y;
	private final double z;

	public VehicleEvent(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
}
