/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3i;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockPlaceEvent extends Event {
	private final Vector blockPos;
	private final Vector origin;
	private final ItemStack itemStack;
	private final long nanoTime;
	private final long timeMillis;
	private final BlockFace direction;
	private final int face;
	private final double blockX;
	private final double blockY;
	private final double blockZ;
	private final World world;

	public BlockPlaceEvent(
		Vector blockPos, Vector origin, ItemStack itemStack, double blockX, double blockY, double blockZ, BlockFace direction, int face, long nanoTime, long timeMillis, World world
	) {
		this.blockPos = blockPos;
		this.origin = origin;
		this.itemStack = itemStack;
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;
		this.nanoTime = nanoTime;
		this.timeMillis = timeMillis;
		this.direction = direction;
		this.face = face;
		this.world = world;
	}

	public long getTimeStamp() {
		return this.timeMillis;
	}

	public boolean isUsableItem() {
		return this.itemStack != null
			&& this.origin.getX() == -1.0
			&& (this.origin.getY() == -1.0 || this.origin.getY() == 255.0 || this.origin.getY() == 4095.0)
			&& this.origin.getZ() == -1.0
			&& this.face == 255;
	}

	public Location getTargetedBlockLocation() {
		switch (this.face) {
			case 0:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)(this.origin.getBlockY() - 1), (double)this.origin.getBlockZ());
			case 1:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)(this.origin.getBlockY() + 1), (double)this.origin.getBlockZ());
			case 2:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)this.origin.getBlockY(), (double)(this.origin.getBlockZ() - 1));
			case 3:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)this.origin.getBlockY(), (double)(this.origin.getBlockZ() + 1));
			case 4:
				return new Location(this.world, (double)(this.origin.getBlockX() - 1), (double)this.origin.getBlockY(), (double)this.origin.getBlockZ());
			case 5:
				return new Location(this.world, (double)(this.origin.getBlockX() + 1), (double)this.origin.getBlockY(), (double)this.origin.getBlockZ());
			default:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)this.origin.getBlockY(), (double)this.origin.getBlockZ());
		}
	}

	public Location get420Johannes() {
		switch (this.face) {
			case 0:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)(this.origin.getBlockY() + 1), (double)this.origin.getBlockZ());
			case 1:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)(this.origin.getBlockY() - 1), (double)this.origin.getBlockZ());
			case 2:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)this.origin.getBlockY(), (double)(this.origin.getBlockZ() + 1));
			case 3:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)this.origin.getBlockY(), (double)(this.origin.getBlockZ() - 1));
			case 4:
				return new Location(this.world, (double)(this.origin.getBlockX() + 1), (double)this.origin.getBlockY(), (double)this.origin.getBlockZ());
			case 5:
				return new Location(this.world, (double)(this.origin.getBlockX() - 1), (double)this.origin.getBlockY(), (double)this.origin.getBlockZ());
			default:
				return new Location(this.world, (double)this.origin.getBlockX(), (double)this.origin.getBlockY(), (double)this.origin.getBlockZ());
		}
	}

	public Vector3i getBlockFacePosition() {
		switch (this.face) {
			case 0:
				return new Vector3i(0, -1, 0);
			case 1:
				return new Vector3i(0, 1, 0);
			case 2:
				return new Vector3i(0, 0, -1);
			case 3:
				return new Vector3i(0, 0, 1);
			case 4:
				return new Vector3i(-1, 0, 0);
			case 5:
				return new Vector3i(1, 0, 0);
			default:
				return new Vector3i(0, 0, 0);
		}
	}

	public Vector getBlockPos() {
		return this.blockPos;
	}

	public Vector getOrigin() {
		return this.origin;
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public long getNanoTime() {
		return this.nanoTime;
	}

	public long getTimeMillis() {
		return this.timeMillis;
	}

	public BlockFace getDirection() {
		return this.direction;
	}

	public int getFace() {
		return this.face;
	}

	public double getBlockX() {
		return this.blockX;
	}

	public double getBlockY() {
		return this.blockY;
	}

	public double getBlockZ() {
		return this.blockZ;
	}

	public World getWorld() {
		return this.world;
	}
}
