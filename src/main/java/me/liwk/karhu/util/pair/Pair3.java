/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.pair;

public class Pair3<X, Y, Z> {
	private X x;
	private Y y;
	private Z z;

	public X getX() {
		return this.x;
	}

	public Y getY() {
		return this.y;
	}

	public Z getZ() {
		return this.z;
	}

	public void setX(X x) {
		this.x = x;
	}

	public void setY(Y y) {
		this.y = y;
	}

	public void setZ(Z z) {
		this.z = z;
	}

	public Pair3() {
	}

	public Pair3(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
