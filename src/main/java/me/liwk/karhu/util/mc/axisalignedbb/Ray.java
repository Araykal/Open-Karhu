/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.mc.axisalignedbb;

import me.liwk.karhu.util.pair.Pair;
import org.bukkit.util.Vector;

public class Ray implements Cloneable {
	private Vector origin;
	private Vector direction;

	public Ray(Vector origin, Vector direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Vector getPointAtDistance(double distance) {
		Vector dir = new Vector(this.direction.getX(), this.direction.getY(), this.direction.getZ());
		Vector orig = new Vector(this.origin.getX(), this.origin.getY(), this.origin.getZ());
		return orig.add(dir.multiply(distance));
	}

	public Ray clone() {
		try {
			Ray clone = (Ray)super.clone();
			clone.origin = this.origin.clone();
			clone.direction = this.direction.clone();
			return clone;
		} catch (CloneNotSupportedException var2) {
			var2.printStackTrace();
			return null;
		}
	}

	public Pair<Vector, Vector> closestPointsBetweenLines(Ray other) {
		Vector n1 = this.direction.clone().crossProduct(other.direction.clone().crossProduct(this.direction));
		Vector n2 = other.direction.clone().crossProduct(this.direction.clone().crossProduct(other.direction));
		Vector c1 = this.origin.clone().add(this.direction.clone().multiply(other.origin.clone().subtract(this.origin).dot(n2) / this.direction.dot(n2)));
		Vector c2 = other.origin.clone().add(other.direction.clone().multiply(this.origin.clone().subtract(other.origin).dot(n1) / other.direction.dot(n1)));
		return new Pair<>(c1, c2);
	}

	@Override
	public String toString() {
		return "origin: " + this.origin + " direction: " + this.direction;
	}

	public Vector getOrigin() {
		return this.origin;
	}

	public Vector getDirection() {
		return this.direction;
	}
}
