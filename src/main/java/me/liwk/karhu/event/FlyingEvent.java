/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class FlyingEvent extends Event {
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private boolean hasMoved;
	private boolean hasLooked;
	private boolean isOnGround;
	private boolean isTeleport;
	private final long nanoTime;
	private final long currentTimeMillis;

	public FlyingEvent(
		double x, double y, double z, float yaw, float pitch, boolean hasMoved, boolean hasLooked, boolean isOnGround, boolean isTeleport, long nanoTime, long currentTimeMillis
	) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.hasMoved = hasMoved;
		this.hasLooked = hasLooked;
		this.isOnGround = isOnGround;
		this.isTeleport = isTeleport;
		this.nanoTime = nanoTime;
		this.currentTimeMillis = currentTimeMillis;
	}

	public boolean hasMoved() {
		return this.hasMoved;
	}

	public boolean hasLooked() {
		return this.hasLooked;
	}

	public boolean isOnGround() {
		return this.isOnGround;
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

	public boolean isHasMoved() {
		return this.hasMoved;
	}

	public boolean isHasLooked() {
		return this.hasLooked;
	}

	public boolean isTeleport() {
		return this.isTeleport;
	}

	public long getNanoTime() {
		return this.nanoTime;
	}

	public long getCurrentTimeMillis() {
		return this.currentTimeMillis;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public void setHasLooked(boolean hasLooked) {
		this.hasLooked = hasLooked;
	}

	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
	}

	public void setTeleport(boolean isTeleport) {
		this.isTeleport = isTeleport;
	}
}
