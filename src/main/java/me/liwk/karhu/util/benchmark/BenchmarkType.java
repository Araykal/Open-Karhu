/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.benchmark;

public enum BenchmarkType {
	PHYSICS_SIMULATOR(50),
	BLOCK_COLLISION(50),
	CHECKS(100),
	BLOCK_CACHE(100),
	TRANSACTION_TASK(100);

	private final int precision;

	private BenchmarkType(int precision) {
		this.precision = precision;
	}

	public int precision() {
		return this.precision;
	}
}
