/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.mc.boundingbox;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.handler.collision.CollisionBoxParser;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.mc.MathHelper;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.player.BlockUtil;
import me.liwk.karhu.util.set.ConcurrentSet;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public final class BoundingBox implements Cloneable {
	private final int MAX_BLOCKS_TO_CHECK = 512;
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	private final long timestamp = System.currentTimeMillis();
	private final KarhuPlayer data;
	private ConcurrentSet<Entity> nearbyEntities = new ConcurrentSet<>();
	public Chunk chunk;

	public BoundingBox(KarhuPlayer data, double x1, double y1, double z1, double x2, double y2, double z2) {
		this.minX = Math.min(x1, x2);
		this.minY = Math.min(y1, y2);
		this.minZ = Math.min(z1, z2);
		this.maxX = Math.max(x1, x2);
		this.maxY = Math.max(y1, y2);
		this.maxZ = Math.max(z1, z2);
		this.data = data;
	}

	public double distance(Location location) {
		return Math.sqrt(
			Math.min(FastMath.pow(location.getX() - this.minX, 2), FastMath.pow(location.getX() - this.maxX, 2))
				+ Math.min(FastMath.pow(location.getZ() - this.minZ, 2), FastMath.pow(location.getZ() - this.maxZ, 2))
		);
	}

	public double distance(double x, double z) {
		double dx = Math.min(FastMath.pow(x - this.minX, 2), FastMath.pow(x - this.maxX, 2));
		double dz = Math.min(FastMath.pow(z - this.minZ, 2), FastMath.pow(z - this.maxZ, 2));
		return Math.sqrt(dx + dz);
	}

	public double distance(BoundingBox box) {
		double dx = Math.min(FastMath.pow(box.minX - this.minX, 2), FastMath.pow(box.maxX - this.maxX, 2));
		double dz = Math.min(FastMath.pow(box.minZ - this.minZ, 2), FastMath.pow(box.maxZ - this.maxZ, 2));
		return Math.sqrt(dx + dz);
	}

	public double distance(AxisAlignedBB box) {
		double dx = Math.min(FastMath.pow(box.minX - this.minX, 2), FastMath.pow(box.maxX - this.maxX, 2));
		double dz = Math.min(FastMath.pow(box.minZ - this.minZ, 2), FastMath.pow(box.maxZ - this.maxZ, 2));
		return Math.sqrt(dx + dz);
	}

	public double distanceToHitbox(AxisAlignedBB box) {
		double cornerX = MathUtil.clamp(this.getCenterX(), box.getCenterX() - 0.4, box.getCenterX() + 0.4);
		double cornerZ = MathUtil.clamp(this.getCenterZ(), box.getCenterZ() - 0.4, box.getCenterZ() + 0.4);
		double distanceX = this.getCenterX() - cornerX;
		double distanceZ = this.getCenterZ() - cornerZ;
		return Math.hypot(distanceX, distanceZ);
	}

	public Vector getDirection(World world) {
		double centerX = (this.minX + this.maxX) / 2.0;
		double centerY = (this.minY + this.maxY) / 2.0;
		double centerZ = (this.minZ + this.maxZ) / 2.0;
		return new Location(world, centerX, centerY, centerZ).getDirection();
	}

	public boolean hasPoint(Vector point) {
		return point.getX() >= this.minX && point.getX() <= this.maxX && point.getY() >= this.minY && point.getY() <= this.maxY && point.getZ() >= this.minZ && point.getZ() <= this.maxZ;
	}

	public BoundingBox add(BoundingBox box) {
		this.minX += box.minX;
		this.minY += box.minY;
		this.minZ += box.minZ;
		this.maxX += box.maxX;
		this.maxY += box.maxY;
		this.maxZ += box.maxZ;
		return this;
	}

	public BoundingBox translate(double x, double y, double z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
		return this;
	}

	public BoundingBox expand(double val) {
		this.minX -= val;
		this.minY -= val;
		this.minZ -= val;
		this.maxX += val;
		this.maxY += val;
		this.maxZ += val;
		return this;
	}

	public BoundingBox expand(double x, double y, double z) {
		this.minX -= x;
		this.minY -= y;
		this.minZ -= z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
		return this;
	}

	public BoundingBox expand(Vector vec) {
		if (vec.getX() < 0.0) {
			this.minX += vec.getX();
		} else {
			this.maxX += vec.getX();
		}

		if (vec.getY() < 0.0) {
			this.minY += vec.getY();
		} else {
			this.maxY += vec.getY();
		}

		if (vec.getZ() < 0.0) {
			this.minZ += vec.getZ();
		} else {
			this.maxZ += vec.getZ();
		}

		return this;
	}

	public BoundingBox expandMin(double x, double y, double z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		return this;
	}

	public BoundingBox expandMax(double x, double y, double z) {
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
		return this;
	}

	public BoundingBox subtractMin(double x, double y, double z) {
		this.minX -= x;
		this.minY -= y;
		this.minZ -= z;
		return this;
	}

	public BoundingBox subtractMax(double x, double y, double z) {
		this.maxX -= x;
		this.maxY -= y;
		this.maxZ -= z;
		return this;
	}

	public BoundingBox initBox(BoundingBox pastBox) {
		double minX = Math.min(this.minX, pastBox.minX);
		double minY = Math.min(this.minY, pastBox.minY);
		double minZ = Math.min(this.minZ, pastBox.minZ);
		double maxX = Math.max(this.maxX, pastBox.maxX);
		double maxY = Math.max(this.maxY, pastBox.maxY);
		double maxZ = Math.max(this.maxZ, pastBox.maxZ);
		return new BoundingBox(pastBox.getData(), minX, minY, minZ, maxX, maxY, maxZ);
	}

	public void setBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
		this.minX = Math.min(x1, x2);
		this.minY = Math.min(y1, y2);
		this.minZ = Math.min(z1, z2);
		this.maxX = Math.max(x1, x2);
		this.maxY = Math.max(y1, y2);
		this.maxZ = Math.max(z1, z2);
	}

	public void initChunkData() {
		this.nearbyEntities.clear();
		int i = MathHelper.floor(this.minX - 2.0);
		int j = MathHelper.floor(this.maxX + 2.0);
		int k = MathHelper.floor(this.minZ - 2.0);
		int l = MathHelper.floor(this.maxZ + 2.0);
		long lastChunkPair = 69L;

		for (int i1 = i; i1 <= j; ++i1) {
			for (int j1 = k; j1 <= l; ++j1) {
				Location test = new Location(this.data.getWorld(), (double)i1, 64.0, (double)j1);
				long chunkPair;
				if ((chunkPair = BlockUtil.getChunkPair(test)) != lastChunkPair) {
					Karhu.getInstance().getChunkManager().getChunk(test, c -> {
						Entity[] entities = c.getEntities();
						if (entities != null) {
							this.nearbyEntities.addAll(Arrays.asList(entities));
						}
					});
					lastChunkPair = chunkPair;
				}
			}
		}
	}

	public List<Entity> getCollidingEntities() {
		List<Entity> list = new ArrayList<>();
		AxisAlignedBB bbThis = this.toBB();
		if (!this.nearbyEntities.isEmpty()) {
			for (Entity e : this.nearbyEntities) {
				if (e != null && e.getEntityId() != this.data.getBukkitPlayer().getEntityId()) {
					AxisAlignedBB bb = CollisionBoxParser.from(e);
					if (bb != null && e != this.data.getBukkitPlayer() && bb.distanceXYZ(bbThis) <= 4.0) {
						list.add(e);
					}
				}
			}
		}

		return list;
	}

	public List<EntityType> getCollidingEntitiesNew() {
		List<EntityType> list = new ArrayList<>();
		AxisAlignedBB bbThis = this.toBB();

		for (EntityData edata : this.data.getEntityData().values()) {
			if (edata.getEid() != this.data.getBukkitPlayer().getEntityId()) {
				AxisAlignedBB bb = edata.getEntityBoundingBox();
				if (bb != null && bb.distanceXYZ(bbThis) <= 4.0) {
					list.add(edata.getType());
				}
			}
		}

		return list;
	}

	public AxisAlignedBB toBB() {
		return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}

	public List<Block> getCollidingBlocks() {
		List<Block> blocks = new ArrayList<>();
		int xFloor = MathHelper.floor_double(this.minX);
		int xCeil = MathHelper.floor_double(this.maxX);
		int yFloor = MathHelper.floor_double(this.minY);
		int yCeil = MathHelper.floor_double(this.maxY);
		int zFloor = MathHelper.floor_double(this.minZ);
		int zCeil = MathHelper.floor_double(this.maxZ);
		int totalBlocks = (xCeil - xFloor + 1) * (yCeil - yFloor + 1) * (zCeil - zFloor + 1);
		if (totalBlocks > 512) {
			return blocks;
		} else {
			for (int x = xFloor; x <= xCeil; ++x) {
				for (int z = zFloor; z <= zCeil; ++z) {
					Location chunkLocation = new Location(this.data.getWorld(), (double)x, 64.0, (double)z);
					boolean chunkLoaded = Karhu.getInstance().getChunkManager().isChunkLoaded(chunkLocation);
					if (chunkLoaded) {
						for (int y = yFloor - 1; y <= yCeil; ++y) {
							chunkLocation.setY((double)y);
							Block block2 = Karhu.getInstance().getChunkManager().getChunkBlockAt(chunkLocation);
							if (block2 != null && !MaterialChecks.AIR.contains(block2.getType())) {
								blocks.add(block2);
							}
						}
					}
				}
			}

			return blocks;
		}
	}

	public List<Material> getCachedCollidingBlocks(List<Block> blocks) {
		List<Material> blocksWanted = new ArrayList<>();

		for (Block block : blocks) {
			float expandY = !MaterialChecks.FENCES.contains(block.getType()) && !MaterialChecks.MOVABLE.contains(block.getType()) ? 1.0F : 1.5F;
			AxisAlignedBB blockAABB = new AxisAlignedBB(block.getLocation(), 1.0, (double)expandY);
			if (this.intersectsWith(blockAABB)) {
				blocksWanted.add(block.getType());
			}
		}

		return blocksWanted;
	}

	public List<Material> getCollidingOnLanded(List<Block> blocks, double posX, double posY, double posZ) {
		List<Material> blocksWanted = new ArrayList<>();

		for (Block block : blocks) {
			AxisAlignedBB blockAABB = new AxisAlignedBB(block.getLocation(), 1.0);
			if (this.intersectsWith(posX, posY, posZ, blockAABB)) {
				blocksWanted.add(block.getType());
			}
		}

		return blocksWanted;
	}

	public List<Material> getCollidingMaterialAccel(List<Block> blocks) {
		int i = MathHelper.floor_double(this.minX);
		int j = MathHelper.floor_double(this.maxX + 1.0);
		int k = MathHelper.floor_double(this.minY);
		int l = MathHelper.floor_double(this.maxY + 1.0);
		int i1 = MathHelper.floor_double(this.minZ);
		int j1 = MathHelper.floor_double(this.maxZ + 1.0);
		List<Material> blocksWanted = new ArrayList<>();

		for (Block block : blocks) {
			AxisAlignedBB blockAABB = new AxisAlignedBB(block.getLocation(), 1.0);

			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						if (this.intersectsWith((double)k1, (double)l1, (double)i2, blockAABB)) {
							blocksWanted.add(block.getType());
						}
					}
				}
			}
		}

		return blocksWanted;
	}

	public boolean getAnyLiquid(List<Block> blocks) {
		int i = MathHelper.floor_double(this.minX);
		int j = MathHelper.floor_double(this.maxX);
		int k = MathHelper.floor_double(this.minY);
		int l = MathHelper.floor_double(this.maxY);
		int i1 = MathHelper.floor_double(this.minZ);
		int j1 = MathHelper.floor_double(this.maxZ);

		for (Block block : blocks) {
			AxisAlignedBB blockAABB = new AxisAlignedBB(block.getLocation(), 1.0);

			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
						if (this.intersectsWith((double)k1, (double)l1, (double)i2, blockAABB)) {
							return block.isLiquid();
						}
					}
				}
			}
		}

		return false;
	}

	public List<Block> getCollidingAir() {
		List<Block> blocks = new ArrayList<>();
		int xFloor = MathHelper.floor(this.minX);
		int xCeil = MathHelper.ceiling_double_int(this.maxX);
		int yFloor = MathHelper.floor(this.minY);
		int yCeil = MathHelper.ceiling_double_int(this.maxY);
		int zFloor = MathHelper.floor(this.minZ);
		int zCeil = MathHelper.ceiling_double_int(this.maxZ);

		for (int x = xFloor; x <= xCeil; ++x) {
			for (int z = zFloor; z <= zCeil; ++z) {
				Location chunkLocation = new Location(this.data.getWorld(), (double)x, 64.0, (double)z);
				Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(chunkLocation);
				if (block != null) {
					for (int y = yFloor; y <= yCeil; ++y) {
						chunkLocation.setY((double)y);
						Block block2 = Karhu.getInstance().getChunkManager().getChunkBlockAt(chunkLocation);
						if (block2.getType() == Material.AIR) {
							blocks.add(block2);
						}
					}
				}
			}
		}

		return blocks;
	}

	public boolean intersectsWith(AxisAlignedBB other, int floorX, int floorY, int floorZ, int ceilX, int ceilY, int ceilZ) {
		return other.minX < (double)ceilX
			&& other.maxX > (double)floorX
			&& other.minY < (double)ceilY
			&& other.maxY > (double)floorY
			&& other.minZ < (double)ceilZ
			&& other.maxZ > (double)floorZ;
	}

	public boolean intersectsWith(double posX, double posY, double posZ, AxisAlignedBB other) {
		return other.maxX >= posX && other.minX <= posX && other.maxY >= posY && other.minY <= posY && other.maxZ >= posZ && other.minZ <= posZ;
	}

	public boolean intersectsWith(AxisAlignedBB other) {
		return other.maxX >= this.minX && other.minX <= this.maxX && other.maxY >= this.minY && other.minY <= this.maxY && other.maxZ >= this.minZ && other.minZ <= this.maxZ;
	}

	public boolean intersectsWithTest(AxisAlignedBB other) {
		return other.minX <= this.maxX && other.maxX >= this.minX && other.minY <= this.maxY && other.maxY >= this.minY && other.minZ <= this.maxZ && other.maxZ >= this.minZ;
	}

	@Override
	public String toString() {
		return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
	}

	@SneakyThrows
	public BoundingBox clone() {

				return (BoundingBox)super.clone();


	}

	public double getCenterX() {
		return (this.minX + this.maxX) / 2.0;
	}

	public double getCenterY() {
		return (this.minY + this.maxY) / 2.0;
	}

	public double getCenterZ() {
		return (this.minZ + this.maxZ) / 2.0;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public int getMAX_BLOCKS_TO_CHECK() {
		return 512;
	}

	public double getMinX() {
		return this.minX;
	}

	public double getMinY() {
		return this.minY;
	}

	public double getMinZ() {
		return this.minZ;
	}

	public double getMaxX() {
		return this.maxX;
	}

	public double getMaxY() {
		return this.maxY;
	}

	public double getMaxZ() {
		return this.maxZ;
	}

	public KarhuPlayer getData() {
		return this.data;
	}

	public ConcurrentSet<Entity> getNearbyEntities() {
		return this.nearbyEntities;
	}

	public Chunk getChunk() {
		return this.chunk;
	}
}
