/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.pending;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BlockPlacePending implements Cloneable {
	public Vector blockPosition;
	public int face;
	public long serverTick;
	public Material item;

	public BlockPlacePending clone() throws CloneNotSupportedException {
		try {
			return (BlockPlacePending)super.clone();
		} catch (Throwable var2) {
			throw var2;
		}
	}

	public BlockPlacePending(Vector blockPosition, int face, long serverTick, Material item) {
		this.blockPosition = blockPosition;
		this.face = face;
		this.serverTick = serverTick;
		this.item = item;
	}

	public Vector getBlockPosition() {
		return this.blockPosition;
	}

	public int getFace() {
		return this.face;
	}

	public long getServerTick() {
		return this.serverTick;
	}

	public Material getItem() {
		return this.item;
	}

	public void setBlockPosition(Vector blockPosition) {
		this.blockPosition = blockPosition;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public void setServerTick(long serverTick) {
		this.serverTick = serverTick;
	}

	public void setItem(Material item) {
		this.item = item;
	}
}
