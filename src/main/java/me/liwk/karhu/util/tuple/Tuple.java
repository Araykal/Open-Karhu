/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.tuple;

public class Tuple<A, B> {
	private A a;
	private B b;

	public Tuple(A var1, B var2) {
		this.a = var1;
		this.b = var2;
	}

	public A a() {
		return this.a;
	}

	public B b() {
		return this.b;
	}
}
