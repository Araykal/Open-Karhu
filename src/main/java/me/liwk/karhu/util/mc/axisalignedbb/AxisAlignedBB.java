/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.mc.axisalignedbb;

import me.liwk.karhu.util.mc.BlockPos;
import me.liwk.karhu.util.mc.MovingObjectPosition;
import me.liwk.karhu.util.mc.facing.EnumFacing;
import me.liwk.karhu.util.mc.vec.Vec3;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class AxisAlignedBB {
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	public Vector min;
	public Vector max;

	public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
		this.minX = Math.min(x1, x2);
		this.minY = Math.min(y1, y2);
		this.minZ = Math.min(z1, z2);
		this.maxX = Math.max(x1, x2);
		this.maxY = Math.max(y1, y2);
		this.maxZ = Math.max(z1, z2);
	}

	public AxisAlignedBB(BlockPos pos1, BlockPos pos2) {
		this.minX = (double)pos1.getX();
		this.minY = (double)pos1.getY();
		this.minZ = (double)pos1.getZ();
		this.maxX = (double)pos2.getX();
		this.maxY = (double)pos2.getY();
		this.maxZ = (double)pos2.getZ();
	}

	public AxisAlignedBB(Vector pos1, Vector pos2, boolean vector) {
		if (!vector) {
			this.minX = pos1.getX();
			this.minY = pos1.getY();
			this.minZ = pos1.getZ();
			this.maxX = pos2.getX();
			this.maxY = pos2.getY();
			this.maxZ = pos2.getZ();
		} else {
			this.minX = pos1.getX();
			this.minY = pos1.getY();
			this.minZ = pos1.getZ();
			this.maxX = pos2.getX();
			this.maxY = pos2.getY();
			this.maxZ = pos2.getZ();
			this.min = pos1;
			this.max = pos2;
		}
	}

	public AxisAlignedBB(Location location) {
		this.minX = (double)location.getBlockX();
		this.minY = (double)location.getBlockY();
		this.minZ = (double)location.getBlockZ();
		this.maxX = (double)location.getBlockX();
		this.maxY = (double)location.getBlockY();
		this.maxZ = (double)location.getBlockZ();
	}

	public AxisAlignedBB(Location location, double maxExpand) {
		this.minX = (double)location.getBlockX();
		this.minY = (double)location.getBlockY();
		this.minZ = (double)location.getBlockZ();
		this.maxX = (double)location.getBlockX() + maxExpand;
		this.maxY = (double)location.getBlockY() + maxExpand;
		this.maxZ = (double)location.getBlockZ() + maxExpand;
	}

	public AxisAlignedBB(Location location, double xzExpand, double yExpand) {
		this.minX = (double)location.getBlockX();
		this.minY = (double)location.getBlockY();
		this.minZ = (double)location.getBlockZ();
		this.maxX = (double)location.getBlockX() + xzExpand;
		this.maxY = (double)location.getBlockY() + yExpand;
		this.maxZ = (double)location.getBlockZ() + xzExpand;
	}

	public AxisAlignedBB addCoord(double x, double y, double z) {
		if (this.min != null && this.max != null) {
			double d0 = this.min.getX();
			double d1 = this.min.getY();
			double d2 = this.min.getZ();
			double d3 = this.max.getX();
			double d4 = this.max.getY();
			double d5 = this.max.getZ();
			if (x < 0.0) {
				d0 += x;
			} else if (x > 0.0) {
				d3 += x;
			}

			if (y < 0.0) {
				d1 += y;
			} else if (y > 0.0) {
				d4 += y;
			}

			if (z < 0.0) {
				d2 += z;
			} else if (z > 0.0) {
				d5 += z;
			}

			return new AxisAlignedBB(new Vector(d0, d1, d2), new Vector(d3, d4, d5), true);
		} else {
			double d0 = this.minX;
			double d1 = this.minY;
			double d2 = this.minZ;
			double d3 = this.maxX;
			double d4 = this.maxY;
			double d5 = this.maxZ;
			if (x < 0.0) {
				d0 += x;
			} else if (x > 0.0) {
				d3 += x;
			}

			if (y < 0.0) {
				d1 += y;
			} else if (y > 0.0) {
				d4 += y;
			}

			if (z < 0.0) {
				d2 += z;
			} else if (z > 0.0) {
				d5 += z;
			}

			return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
		}
	}

	public AxisAlignedBB expand(double x, double y, double z) {
		if (this.min != null && this.max != null) {
			double d0 = this.min.getX() - x;
			double d1 = this.min.getY() - y;
			double d2 = this.min.getZ() - z;
			double d3 = this.max.getX() + x;
			double d4 = this.max.getY() + y;
			double d5 = this.max.getZ() + z;
			return new AxisAlignedBB(new Vector(d0, d1, d2), new Vector(d3, d4, d5), true);
		} else {
			double d0 = this.minX - x;
			double d1 = this.minY - y;
			double d2 = this.minZ - z;
			double d3 = this.maxX + x;
			double d4 = this.maxY + y;
			double d5 = this.maxZ + z;
			return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
		}
	}

	public AxisAlignedBB union(AxisAlignedBB other) {
		double d0 = Math.min(this.minX, other.minX);
		double d1 = Math.min(this.minY, other.minY);
		double d2 = Math.min(this.minZ, other.minZ);
		double d3 = Math.max(this.maxX, other.maxX);
		double d4 = Math.max(this.maxY, other.maxY);
		double d5 = Math.max(this.maxZ, other.maxZ);
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}

	public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
		double d0 = Math.min(x1, x2);
		double d1 = Math.min(y1, y2);
		double d2 = Math.min(z1, z2);
		double d3 = Math.max(x1, x2);
		double d4 = Math.max(y1, y2);
		double d5 = Math.max(z1, z2);
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}

	public AxisAlignedBB offset(double x, double y, double z) {
		return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
	}

	public double calculateXOffset(AxisAlignedBB other, double offsetX) {
		if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
			if (offsetX > 0.0 && other.maxX <= this.minX) {
				double d1 = this.minX - other.maxX;
				if (d1 < offsetX) {
					offsetX = d1;
				}
			} else if (offsetX < 0.0 && other.minX >= this.maxX) {
				double d0 = this.maxX - other.minX;
				if (d0 > offsetX) {
					offsetX = d0;
				}
			}
		}

		return offsetX;
	}

	public double calculateYOffset(AxisAlignedBB other, double offsetY) {
		if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
			if (offsetY > 0.0 && other.maxY <= this.minY) {
				double d1 = this.minY - other.maxY;
				if (d1 < offsetY) {
					offsetY = d1;
				}
			} else if (offsetY < 0.0 && other.minY >= this.maxY) {
				double d0 = this.maxY - other.minY;
				if (d0 > offsetY) {
					offsetY = d0;
				}
			}
		}

		return offsetY;
	}

	public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
		if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
			if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
				double d1 = this.minZ - other.maxZ;
				if (d1 < offsetZ) {
					offsetZ = d1;
				}
			} else if (offsetZ < 0.0 && other.minZ >= this.maxZ) {
				double d0 = this.maxZ - other.minZ;
				if (d0 > offsetZ) {
					offsetZ = d0;
				}
			}
		}

		return offsetZ;
	}

	public boolean intersectsWith(AxisAlignedBB other) {
		return other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ;
	}

	public boolean isVecInside(Vec3 vec) {
		return vec.xCoord > this.minX && vec.xCoord < this.maxX && vec.yCoord > this.minY && vec.yCoord < this.maxY && vec.zCoord > this.minZ && vec.zCoord < this.maxZ;
	}

	public Vector intersectsRay(Ray ray, float minDist, float maxDist) {
		Vector invDir = new Vector(1.0 / ray.getDirection().getX(), 1.0 / ray.getDirection().getY(), 1.0 / ray.getDirection().getZ());
		boolean signDirX = invDir.getX() < 0.0;
		boolean signDirY = invDir.getY() < 0.0;
		boolean signDirZ = invDir.getZ() < 0.0;
		Vector bbox = signDirX ? this.max : this.min;
		double tmin = (bbox.getX() - ray.getOrigin().getX()) * invDir.getX();
		bbox = signDirX ? this.min : this.max;
		double tmax = (bbox.getX() - ray.getOrigin().getX()) * invDir.getX();
		bbox = signDirY ? this.max : this.min;
		double tymin = (bbox.getY() - ray.getOrigin().getY()) * invDir.getY();
		bbox = signDirY ? this.min : this.max;
		double tymax = (bbox.getY() - ray.getOrigin().getY()) * invDir.getY();
		if (!(tmin > tymax) && !(tymin > tmax)) {
			if (tymin > tmin) {
				tmin = tymin;
			}

			if (tymax < tmax) {
				tmax = tymax;
			}

			bbox = signDirZ ? this.max : this.min;
			double tzmin = (bbox.getZ() - ray.getOrigin().getZ()) * invDir.getZ();
			bbox = signDirZ ? this.min : this.max;
			double tzmax = (bbox.getZ() - ray.getOrigin().getZ()) * invDir.getZ();
			if (!(tmin > tzmax) && !(tzmin > tmax)) {
				if (tzmin > tmin) {
					tmin = tzmin;
				}

				if (tzmax < tmax) {
					tmax = tzmax;
				}

				return tmin < (double)maxDist && tmax > (double)minDist ? ray.getPointAtDistance(tmin) : null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public double getAverageEdgeLength() {
		double d0 = this.maxX - this.minX;
		double d1 = this.maxY - this.minY;
		double d2 = this.maxZ - this.minZ;
		return (d0 + d1 + d2) / 3.0;
	}

	public boolean betweenRays(Vector pos, Vector dir1, Vector dir2) {
		if (dir1.dot(dir2) > 0.999) {
			if (this.intersectsRay(new Ray(pos, dir2), 0.0F, Float.MAX_VALUE) == null) {
				return false;
			}
		} else {
			Vector planeNormal = dir2.clone().crossProduct(dir1);
			Vector[] vertices = this.getVertices();
			boolean hitPlane = false;
			boolean above = false;
			boolean below = false;

			for (Vector vertex : vertices) {
				vertex.subtract(pos);
				if (!hitPlane) {
					if (vertex.dot(planeNormal) > 0.0) {
						above = true;
					} else {
						below = true;
					}

					if (above && below) {
						hitPlane = true;
					}
				}
			}

			if (!hitPlane) {
				return false;
			}

			Vector extraDirToDirNormal = planeNormal.clone().crossProduct(dir2);
			Vector dirToExtraDirNormal = dir1.clone().crossProduct(planeNormal);
			boolean betweenVectors = false;
			boolean frontOfExtraDirToDir = false;
			boolean frontOfDirToExtraDir = false;

			for (Vector vertex : vertices) {
				if (!frontOfExtraDirToDir && vertex.dot(extraDirToDirNormal) >= 0.0) {
					frontOfExtraDirToDir = true;
				}

				if (!frontOfDirToExtraDir && vertex.dot(dirToExtraDirNormal) >= 0.0) {
					frontOfDirToExtraDir = true;
				}

				if (frontOfExtraDirToDir && frontOfDirToExtraDir) {
					betweenVectors = true;
					break;
				}
			}

			if (!betweenVectors) {
				return false;
			}
		}

		return true;
	}

	public AxisAlignedBB contract(double x, double y, double z) {
		double d0 = this.minX + x;
		double d1 = this.minY + y;
		double d2 = this.minZ + z;
		double d3 = this.maxX - x;
		double d4 = this.maxY - y;
		double d5 = this.maxZ - z;
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}

	public MovingObjectPosition calculateIntercept(Vec3 vecA, Vec3 vecB) {
		Vec3 vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
		Vec3 vec31 = vecA.getIntermediateWithXValue(vecB, this.maxX);
		Vec3 vec32 = vecA.getIntermediateWithYValue(vecB, this.minY);
		Vec3 vec33 = vecA.getIntermediateWithYValue(vecB, this.maxY);
		Vec3 vec34 = vecA.getIntermediateWithZValue(vecB, this.minZ);
		Vec3 vec35 = vecA.getIntermediateWithZValue(vecB, this.maxZ);
		if (!this.isVecInYZ(vec3)) {
			vec3 = null;
		}

		if (!this.isVecInYZ(vec31)) {
			vec31 = null;
		}

		if (!this.isVecInXZ(vec32)) {
			vec32 = null;
		}

		if (!this.isVecInXZ(vec33)) {
			vec33 = null;
		}

		if (!this.isVecInXY(vec34)) {
			vec34 = null;
		}

		if (!this.isVecInXY(vec35)) {
			vec35 = null;
		}

		Vec3 vec36 = null;
		if (vec3 != null) {
			vec36 = vec3;
		}

		if (vec31 != null && (vec36 == null || vecA.squareDistanceTo(vec31) < vecA.squareDistanceTo(vec36))) {
			vec36 = vec31;
		}

		if (vec32 != null && (vec36 == null || vecA.squareDistanceTo(vec32) < vecA.squareDistanceTo(vec36))) {
			vec36 = vec32;
		}

		if (vec33 != null && (vec36 == null || vecA.squareDistanceTo(vec33) < vecA.squareDistanceTo(vec36))) {
			vec36 = vec33;
		}

		if (vec34 != null && (vec36 == null || vecA.squareDistanceTo(vec34) < vecA.squareDistanceTo(vec36))) {
			vec36 = vec34;
		}

		if (vec35 != null && (vec36 == null || vecA.squareDistanceTo(vec35) < vecA.squareDistanceTo(vec36))) {
			vec36 = vec35;
		}

		if (vec36 == null) {
			return null;
		} else {
			EnumFacing enumfacing = null;
			if (vec36 == vec3) {
				enumfacing = EnumFacing.WEST;
			} else if (vec36 == vec31) {
				enumfacing = EnumFacing.EAST;
			} else if (vec36 == vec32) {
				enumfacing = EnumFacing.DOWN;
			} else if (vec36 == vec33) {
				enumfacing = EnumFacing.UP;
			} else if (vec36 == vec34) {
				enumfacing = EnumFacing.NORTH;
			} else {
				enumfacing = EnumFacing.SOUTH;
			}

			return new MovingObjectPosition(vec36, enumfacing);
		}
	}

	private boolean isVecInYZ(Vec3 vec) {
		return vec == null ? false : vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
	}

	private boolean isVecInXZ(Vec3 vec) {
		return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
	}

	private boolean isVecInXY(Vec3 vec) {
		return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
	}

	@Override
	public String toString() {
		return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
	}

	public boolean func_181656_b() {
		return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
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

	public double cornerX() {
		return (this.minX + this.maxX) * 0.5;
	}

	public double cornerZ() {
		return (this.minZ + this.maxZ) * 0.5;
	}

	public double getDistanceX() {
		return this.maxX - this.minX;
	}

	public double getDistanceY() {
		return this.maxY - this.minY;
	}

	public double getDistanceZ() {
		return this.maxZ - this.minZ;
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

	public double distance(AxisAlignedBB box) {
		double minDiffX = box.minX - this.minX;
		double maxDiffX = box.maxX - this.maxX;
		double minDiffZ = box.minZ - this.minZ;
		double maxDiffZ = box.maxZ - this.maxZ;
		double dx = Math.min(minDiffX * minDiffX, maxDiffX * maxDiffX);
		double dz = Math.min(minDiffZ * minDiffZ, maxDiffZ * maxDiffZ);
		return Math.sqrt(dx + dz);
	}

	public double distanceXYZ(AxisAlignedBB box) {
		double minDiffX = box.minX - this.minX;
		double maxDiffX = box.maxX - this.maxX;
		double minDiffY = box.minY - this.minY;
		double maxDiffY = box.maxY - this.maxY;
		double minDiffZ = box.minZ - this.minZ;
		double maxDiffZ = box.maxZ - this.maxZ;
		double dx = Math.min(minDiffX * minDiffX, maxDiffX * maxDiffX);
		double dy = Math.min(minDiffY * minDiffY, maxDiffY * maxDiffY);
		double dz = Math.min(minDiffZ * minDiffZ, maxDiffZ * maxDiffZ);
		return Math.sqrt(dx + dy + dz);
	}

	public double distance(Vector vector) {
		if (this.min != null && this.max != null) {
			double dx = Math.max(this.min.getX() - vector.getX(), Math.max(0.0, vector.getX() - this.max.getX()));
			double dy = Math.max(this.min.getY() - vector.getY(), Math.max(0.0, vector.getY() - this.max.getY()));
			double dz = Math.max(this.min.getZ() - vector.getZ(), Math.max(0.0, vector.getZ() - this.max.getZ()));
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		} else {
			double dx = Math.max(this.minX - vector.getX(), Math.max(0.0, vector.getX() - this.maxX));
			double dy = Math.max(this.minY - vector.getY(), Math.max(0.0, vector.getY() - this.maxY));
			double dz = Math.max(this.minZ - vector.getZ(), Math.max(0.0, vector.getZ() - this.maxZ));
			return Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
	}

	public Vector[] getVertices() {
		return new Vector[]{
			new Vector(this.min.getX(), this.min.getY(), this.min.getZ()),
			new Vector(this.min.getX(), this.min.getY(), this.max.getZ()),
			new Vector(this.min.getX(), this.max.getY(), this.min.getZ()),
			new Vector(this.min.getX(), this.max.getY(), this.max.getZ()),
			new Vector(this.max.getX(), this.min.getY(), this.min.getZ()),
			new Vector(this.max.getX(), this.max.getY(), this.min.getZ()),
			new Vector(this.max.getX(), this.min.getY(), this.max.getZ()),
			new Vector(this.max.getX(), this.max.getY(), this.max.getZ())
		};
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

	public Vector getMin() {
		return this.min;
	}

	public Vector getMax() {
		return this.max;
	}
}
