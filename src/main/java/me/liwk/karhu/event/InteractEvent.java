/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

import com.github.retrooper.packetevents.util.Vector3f;

public class InteractEvent extends Event {
	private final int entityId;
	private final boolean isPlayer;
	private final Vector3f vec3D;
	private final boolean at;
	private final long now;

	public InteractEvent(int entityId, boolean isPlayer, Vector3f vec3D, boolean at, long now) {
		this.entityId = entityId;
		this.isPlayer = isPlayer;
		this.vec3D = vec3D;
		this.at = at;
		this.now = now;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public boolean isPlayer() {
		return this.isPlayer;
	}

	public Vector3f getVec3D() {
		return this.vec3D;
	}

	public boolean isAt() {
		return this.at;
	}

	public long getNow() {
		return this.now;
	}
}
