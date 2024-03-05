/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.world.chunk;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.util.Conditions;
import me.liwk.karhu.util.gui.Callback;
import me.liwk.karhu.util.player.BlockUtil;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class WorldChunkManager implements IChunkManager {
	private final Map<World, Long2ObjectMap<Chunk>> loadedChunks = new HashMap<>();
	private long lastAskTick;

	@Override
	public void getChunk(Location location, Callback<Chunk> chunkCallback) {
		synchronized (this.loadedChunks) {
			World world = location.getWorld();
			Conditions.notNull(world, "location world cannot be null");
			Long2ObjectMap<Chunk> chunkMap = this.loadedChunks.computeIfAbsent(world, k -> new Long2ObjectOpenHashMap());
			if (chunkMap.isEmpty()) {
				this.somethingTriedDoingSomethingStupidErrorMessage(world);
			} else {
				Chunk chunk = (Chunk)chunkMap.get(BlockUtil.getChunkPair(location));
				if (chunk != null) {
					chunkCallback.call(chunk);
				}
			}
		}
	}

	@Override
	public Block getChunkBlockAt(Location location) {
		synchronized (this.loadedChunks) {
			World world = location.getWorld();
			Conditions.notNull(world, "location world cannot be null");
			Long2ObjectMap<Chunk> chunkMap = this.loadedChunks.computeIfAbsent(world, k -> new Long2ObjectOpenHashMap());
			if (chunkMap.isEmpty()) {
				return null;
			} else {
				Chunk chunk = (Chunk)chunkMap.get(BlockUtil.getChunkPair(location));
				if (chunk == null) {
					if (Karhu.getInstance().getServerTick() - this.lastAskTick >= 1L) {
						Tasker.run(() -> {
							for (Chunk c : world.getLoadedChunks()) {
								this.onChunkLoad(c);
							}
						});
					}

					this.lastAskTick = Karhu.getInstance().getServerTick();
					return null;
				} else {
					int blockY = location.getBlockY();
					boolean invalidCoord = blockY > world.getMaxHeight() || blockY < 0;
					return Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_13) && invalidCoord
						? location.getBlock()
						: chunk.getBlock(location.getBlockX() & 15, blockY, location.getBlockZ() & 15);
				}
			}
		}
	}

	@Override
	public void onChunkLoad(Chunk chunk) {
		synchronized (this.loadedChunks) {
			this.loadedChunks.computeIfAbsent(chunk.getWorld(), k -> new Long2ObjectOpenHashMap()).put(BlockUtil.getChunkPair(chunk), chunk);
		}
	}

	@Override
	public void onChunkUnload(Chunk chunk) {
		synchronized (this.loadedChunks) {
			Map<Long, Chunk> chunkMap = (Map)this.loadedChunks.get(chunk.getWorld());
			if (chunkMap != null) {
				chunkMap.remove(BlockUtil.getChunkPair(chunk));
			}
		}
	}

	@Override
	public boolean isChunkLoaded(Location l) {
		synchronized (this.loadedChunks) {
			World world = l.getWorld();
			Conditions.notNull(world, "location world cannot be null");
			Long2ObjectMap<Chunk> chunkMap = this.loadedChunks.get(world);
			boolean invalid = chunkMap == null;
			boolean empty = !invalid && chunkMap.isEmpty();
			if (!invalid && !empty) {
				Chunk chunk = (Chunk)chunkMap.get(BlockUtil.getChunkPair(l));
				return chunk != null && chunk.isLoaded();
			} else {
				return world.isChunkLoaded(l.getBlockX() >> 4, l.getBlockZ() >> 4);
			}
		}
	}

	@Override
	public void addWorld(World world) {
		synchronized (this.loadedChunks) {
			this.loadedChunks.computeIfAbsent(world, k -> new Long2ObjectOpenHashMap());
		}
	}

	@Override
	public void removeWorld(World world) {
		synchronized (this.loadedChunks) {
			this.loadedChunks.remove(world);
		}
	}

	@Override
	public void unloadAll() {
		synchronized (this.loadedChunks) {
			this.loadedChunks.clear();
		}
	}

	@Override
	public int getCacheSize(World world) {
		synchronized (this.loadedChunks) {
			return this.loadedChunks.get(world).size();
		}
	}

	@Override
	public Map<World, Long2ObjectMap<Chunk>> getLoadedChunks() {
		return this.loadedChunks;
	}

	private void somethingTriedDoingSomethingStupidErrorMessage(World world) {
		if (world == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Karhu attempted to access a chunk in a non-existent world, this should never happen null");
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "Karhu attempted to access a chunk in a non-existent world, this should never happen " + world.getName());
		}
	}
}
