/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

import me.liwk.karhu.util.location.CustomLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class TeleportPosition {
	protected final double x;
	protected final double y;
	protected final double z;

	public double horizontal(Vector vector) {
		return Math.sqrt(NumberConversions.square(this.x - vector.getX()) + NumberConversions.square(this.z - vector.getZ()));
	}

	public double distance(Vector vector) {
		return Math.sqrt(NumberConversions.square(this.x - vector.getX()) + NumberConversions.square(this.y - vector.getY()) + NumberConversions.square(this.z - vector.getZ()));
	}

	public double vertical(Vector vector) {
		return Math.sqrt(NumberConversions.square(this.y - vector.getY()));
	}

	@Override
	public String toString() {
		return "X " + this.x + ", Y " + this.y + ", Z " + this.z;
	}

	@NotNull
	public Location toLocation(@NotNull World world) {
		return new Location(world, this.x, this.y, this.z);
	}

	@NotNull
	public CustomLocation toCLocation() {
		return new CustomLocation(this.x, this.y, this.z);
	}

	public TeleportPosition(double x, double y, double z) {
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
