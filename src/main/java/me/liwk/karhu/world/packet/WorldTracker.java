/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.world.packet;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_18;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange.EncodedBlock;
import java.io.ByteArrayInputStream;
import me.liwk.karhu.data.KarhuPlayer;

public class WorldTracker {
	private final KarhuPlayer data;

	public void handleData(WrapperPlayServerChunkData packet) {
		int x = packet.getColumn().getX();
		int z = packet.getColumn().getZ();
		BaseChunk[] chunks = new ChunkReader_v1_18()
			.read(null, null, null, true, false, false, 69, null, new NetStreamInput(new ByteArrayInputStream(packet.getColumn().getBiomeDataBytes())));

		for (int i = 0; i < chunks.length; ++i) {
			Chunk_v1_18 chunk = (Chunk_v1_18)chunks[i];
			if (chunk != null) {
				chunks[i] = new Chunk_v1_18(chunk.getBlockCount(), chunk.getChunkData(), null);
			}
		}
	}

	public void handleBlock(WrapperPlayServerBlockChange packet) {
		Vector3i blockPosition = packet.getBlockPosition();
		this.data.queueToPrePing(uid -> this.data.getKarhuWorld().updateBlock(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), packet.getBlockId()));
	}

	public void handleMultiBlock(WrapperPlayServerMultiBlockChange packet) {
		for (EncodedBlock encodedBlock : packet.getBlocks()) {
			this.data.queueToPrePing(uid -> this.data.getKarhuWorld().updateBlock(encodedBlock.getX(), encodedBlock.getY(), encodedBlock.getZ(), encodedBlock.getBlockId()));
		}
	}

	public void handleUnload(long position) {
	}

	public WorldTracker(KarhuPlayer data) {
		this.data = data;
	}
}
