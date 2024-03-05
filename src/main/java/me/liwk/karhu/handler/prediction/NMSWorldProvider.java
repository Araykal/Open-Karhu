/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.prediction;

import com.google.common.collect.Lists;
import java.util.List;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.util.mc.BlockPos;
import me.liwk.karhu.util.mc.MathHelper;
import me.liwk.karhu.util.mc.BlockPos.MutableBlockPos;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.player.BlockUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public final class NMSWorldProvider {
	private final Karhu karhu;

	public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entityIn, AxisAlignedBB bb) {
		List<AxisAlignedBB> list = Lists.newArrayList();
		int i = MathHelper.floor_double(bb.minX);
		int j = MathHelper.floor_double(bb.maxX + 1.0);
		int k = MathHelper.floor_double(bb.minY);
		int l = MathHelper.floor_double(bb.maxY + 1.0);
		int i1 = MathHelper.floor_double(bb.minZ);
		int j1 = MathHelper.floor_double(bb.maxZ + 1.0);
		int amount = j - i * (i1 - j1);
		if (amount > 50000) {
			return list;
		} else {
			MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = i1; l1 < j1; ++l1) {
					for (int i2 = k - 1; i2 < l; ++i2) {
						MutableBlockPos b = blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
						b.func_181079_c(k1, i2, l1);
						Location loc = new Location(entityIn.getWorld(), (double)b.getX(), entityIn.getLocation().getY(), (double)b.getZ());
						Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(loc);
						if (block != null) {
							this.addCollisionBoxesToList(block, b, bb, list);
						}
					}
				}
			}

			return list;
		}
	}

	public void addCollisionBoxesToList(Block b, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list) {
		double maxY = !MaterialChecks.FENCES.contains(b.getType()) && !MaterialChecks.SHULKER_BOXES.contains(b.getType()) ? 1.0 : 1.5;
		AxisAlignedBB axisalignedbb = new AxisAlignedBB(
			(double)pos.getX() + 0.0, (double)pos.getY() + 0.0, (double)pos.getZ() + 0.0, (double)pos.getX() + 1.0, (double)pos.getY() + maxY, (double)pos.getZ() + 1.0
		);
		if (mask.intersectsWith(axisalignedbb)) {
			list.add(axisalignedbb);
		}
	}

	public boolean isAreaLoaded(World w, BlockPos from, BlockPos to) {
		return this.isAreaLoaded(w, from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ());
	}

	private boolean isAreaLoaded(World world, int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
		if (yEnd >= 0 && yStart < 256) {
			xStart >>= 4;
			zStart >>= 4;
			xEnd >>= 4;
			zEnd >>= 4;

			for (int i = xStart; i <= xEnd; ++i) {
				for (int j = zStart; j <= zEnd; ++j) {
					if (!BlockUtil.chunkLoaded(world, i, j)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public NMSWorldProvider(Karhu karhu) {
		this.karhu = karhu;
	}
}
