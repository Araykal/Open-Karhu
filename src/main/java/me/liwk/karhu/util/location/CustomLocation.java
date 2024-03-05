/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class CustomLocation implements Cloneable {
	public double x;
	public double y;
	public double z;
	public float yaw;
	public float pitch;
	public boolean ground;
	public boolean cheats;
	public boolean teleport;
	public long timeStamp;

	public CustomLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.timeStamp = System.currentTimeMillis();
	}

	public CustomLocation(double x, double y, double z, boolean ground) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ground = ground;
		this.timeStamp = System.currentTimeMillis();
	}

	public CustomLocation(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.timeStamp = System.currentTimeMillis();
	}

	public CustomLocation(double x, double y, double z, float yaw, float pitch, long timeStamp) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.timeStamp = timeStamp;
	}

	public CustomLocation(Location loc) {
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = loc.getYaw();
		this.pitch = loc.getPitch();
		this.timeStamp = System.currentTimeMillis();
	}

	public CustomLocation(Vector vector) {
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
		this.timeStamp = System.currentTimeMillis();
	}

	public double distance(double x, double y, double z) {
		return Math.abs(this.x - x) + Math.abs(this.y - y) + Math.abs(this.z - z);
	}

	public double distance(com.github.retrooper.packetevents.protocol.world.Location loc) {
		return Math.abs(this.x - loc.getX()) + Math.abs(this.y - loc.getY()) + Math.abs(this.z - loc.getZ());
	}

	public double distance(CustomLocation o) {
		return Math.sqrt(NumberConversions.square(this.x - o.x) + NumberConversions.square(this.y - o.y) + NumberConversions.square(this.z - o.z));
	}

	public double distance(Location o) {
		return Math.sqrt(NumberConversions.square(this.x - o.getX()) + NumberConversions.square(this.y - o.getY()) + NumberConversions.square(this.z - o.getZ()));
	}

	public double horizontal(CustomLocation o) {
		return Math.sqrt(NumberConversions.square(this.x - o.x) + NumberConversions.square(this.z - o.z));
	}

	public double horizontal(Location o) {
		return Math.sqrt(NumberConversions.square(this.x - o.getX()) + NumberConversions.square(this.z - o.getZ()));
	}

	public double vertical(Location o) {
		return Math.abs(this.y - o.getY());
	}

	public double vertical(CustomLocation o) {
		return Math.abs(this.y - o.getY());
	}

	public CustomLocation clone() throws CloneNotSupportedException {
		try {
			return (CustomLocation)super.clone();
		} catch (Throwable var2) {
			throw var2;
		}
	}

	public Location toLocation(World world) {
		return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
	}

	public Vector toVector() {
		return new Vector(this.x, this.y, this.z);
	}

	public Vector toBlockVector() {
		return new Vector(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
	}

	public int getBlockZ() {
		return NumberConversions.floor(this.z);
	}

	public int getBlockX() {
		return NumberConversions.floor(this.x);
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return this.yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public long getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return String.format("%.2f", this.x) + ", " + String.format("%.2f", this.y) + ", " + String.format("%.2f", this.z);
	}

	public CustomLocation subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.z = z;
		this.y = y;
	}

	public void setRotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public void setGround(boolean ground) {
		this.ground = ground;
	}

	public void setCheats(boolean cheats) {
		this.cheats = cheats;
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}
}
