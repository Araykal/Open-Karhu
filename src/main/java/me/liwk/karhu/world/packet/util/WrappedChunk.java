/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.world.packet.util;

import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;

public class WrappedChunk {
	public final int x;
	public final int z;
	public BaseChunk[] chunks;

	public WrappedChunk(int x, int z, BaseChunk[] chunks) {
		this.x = x;
		this.z = z;
		this.chunks = chunks;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	public BaseChunk[] getChunks() {
		return this.chunks;
	}
}
