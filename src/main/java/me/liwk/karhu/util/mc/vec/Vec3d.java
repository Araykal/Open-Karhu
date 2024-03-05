/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.mc.vec;

import javax.annotation.Nullable;
import me.liwk.karhu.util.mc.MathHelper;

public class Vec3d {
	public static final Vec3d ZERO = new Vec3d(0.0, 0.0, 0.0);
	public final double x;
	public final double y;
	public final double z;

	public Vec3d(double xIn, double yIn, double zIn) {
		if (xIn == -0.0) {
			xIn = 0.0;
		}

		if (yIn == -0.0) {
			yIn = 0.0;
		}

		if (zIn == -0.0) {
			zIn = 0.0;
		}

		this.x = xIn;
		this.y = yIn;
		this.z = zIn;
	}

	public Vec3d(Vec3i vector) {
		this((double)vector.getX(), (double)vector.getY(), (double)vector.getZ());
	}

	public Vec3d subtractReverse(Vec3d vec) {
		return new Vec3d(vec.x - this.x, vec.y - this.y, vec.z - this.z);
	}

	public Vec3d normalize() {
		double d0 = (double)MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		return d0 < 1.0E-4 ? ZERO : new Vec3d(this.x / d0, this.y / d0, this.z / d0);
	}

	public double dotProduct(Vec3d vec) {
		return this.x * vec.x + this.y * vec.y + this.z * vec.z;
	}

	public Vec3d crossProduct(Vec3d vec) {
		return new Vec3d(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
	}

	public Vec3d subtract(Vec3d vec) {
		return this.subtract(vec.x, vec.y, vec.z);
	}

	public Vec3d subtract(double x, double y, double z) {
		return this.addVector(-x, -y, -z);
	}

	public Vec3d add(Vec3d vec) {
		return this.addVector(vec.x, vec.y, vec.z);
	}

	public Vec3d addVector(double x, double y, double z) {
		return new Vec3d(this.x + x, this.y + y, this.z + z);
	}

	public double distanceTo(Vec3d vec) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		return (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	}

	public double squareDistanceTo(Vec3d vec) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public double squareDistanceTo(double xIn, double yIn, double zIn) {
		double d0 = xIn - this.x;
		double d1 = yIn - this.y;
		double d2 = zIn - this.z;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public Vec3d scale(double factor) {
		return new Vec3d(this.x * factor, this.y * factor, this.z * factor);
	}

	public double lengthVector() {
		return (double)MathHelper.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	@Nullable
	public Vec3d getIntermediateWithXValue(Vec3d vec, double x) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		if (d0 * d0 < 1.0E-7F) {
			return null;
		} else {
			double d3 = (x - this.x) / d0;
			return d3 >= 0.0 && d3 <= 1.0 ? new Vec3d(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
		}
	}

	@Nullable
	public Vec3d getIntermediateWithYValue(Vec3d vec, double y) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		if (d1 * d1 < 1.0E-7F) {
			return null;
		} else {
			double d3 = (y - this.y) / d1;
			return d3 >= 0.0 && d3 <= 1.0 ? new Vec3d(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
		}
	}

	@Nullable
	public Vec3d getIntermediateWithZValue(Vec3d vec, double z) {
		double d0 = vec.x - this.x;
		double d1 = vec.y - this.y;
		double d2 = vec.z - this.z;
		if (d2 * d2 < 1.0E-7F) {
			return null;
		} else {
			double d3 = (z - this.z) / d2;
			return d3 >= 0.0 && d3 <= 1.0 ? new Vec3d(this.x + d0 * d3, this.y + d1 * d3, this.z + d2 * d3) : null;
		}
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else if (!(p_equals_1_ instanceof Vec3d)) {
			return false;
		} else {
			Vec3d vec3d = (Vec3d)p_equals_1_;
			if (Double.compare(vec3d.x, this.x) != 0) {
				return false;
			} else if (Double.compare(vec3d.y, this.y) != 0) {
				return false;
			} else {
				return Double.compare(vec3d.z, this.z) == 0;
			}
		}
	}

	@Override
	public int hashCode() {
		long j = Double.doubleToLongBits(this.x);
		int i = (int)(j ^ j >>> 32);
		j = Double.doubleToLongBits(this.y);
		i = 31 * i + (int)(j ^ j >>> 32);
		j = Double.doubleToLongBits(this.z);
		return 31 * i + (int)(j ^ j >>> 32);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	public Vec3d rotatePitch(float pitch) {
		float f = MathHelper.cos(pitch);
		float f1 = MathHelper.sin(pitch);
		double d0 = this.x;
		double d1 = this.y * (double)f + this.z * (double)f1;
		double d2 = this.z * (double)f - this.y * (double)f1;
		return new Vec3d(d0, d1, d2);
	}

	public Vec3d rotateYaw(float yaw) {
		float f = MathHelper.cos(yaw);
		float f1 = MathHelper.sin(yaw);
		double d0 = this.x * (double)f + this.z * (double)f1;
		double d1 = this.y;
		double d2 = this.z * (double)f - this.x * (double)f1;
		return new Vec3d(d0, d1, d2);
	}

	public static Vec3d fromPitchYawVector(Vec2f p_189984_0_) {
		return fromPitchYaw(p_189984_0_.x, p_189984_0_.y);
	}

	public static Vec3d fromPitchYaw(float p_189986_0_, float p_189986_1_) {
		float f = MathHelper.cos(-p_189986_1_ * (float) (Math.PI / 180.0) - (float) Math.PI);
		float f1 = MathHelper.sin(-p_189986_1_ * (float) (Math.PI / 180.0) - (float) Math.PI);
		float f2 = -MathHelper.cos(-p_189986_0_ * (float) (Math.PI / 180.0));
		float f3 = MathHelper.sin(-p_189986_0_ * (float) (Math.PI / 180.0));
		return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
	}
}
