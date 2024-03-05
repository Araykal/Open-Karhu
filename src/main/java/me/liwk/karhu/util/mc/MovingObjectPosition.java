/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.mc;

import me.liwk.karhu.util.mc.facing.EnumFacing;
import me.liwk.karhu.util.mc.vec.Vec3;
import org.bukkit.entity.Entity;

public class MovingObjectPosition {
	private BlockPos blockPos;
	public MovingObjectPosition.MovingObjectType typeOfHit;
	public EnumFacing sideHit;
	public Vec3 hitVec;
	public Entity entityHit;

	public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn) {
		this(MovingObjectPosition.MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
	}

	public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing) {
		this(MovingObjectPosition.MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
	}

	public MovingObjectPosition(MovingObjectPosition.MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn) {
		this.typeOfHit = typeOfHitIn;
		this.blockPos = blockPosIn;
		this.sideHit = sideHitIn;
		this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
	}

	public MovingObjectPosition(Entity entityHitIn, Vec3 hitVecIn) {
		this.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
		this.entityHit = entityHitIn;
		this.hitVec = hitVecIn;
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	@Override
	public String toString() {
		return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
	}

	public static enum MovingObjectType {
		MISS,
		BLOCK,
		ENTITY;
	}
}
