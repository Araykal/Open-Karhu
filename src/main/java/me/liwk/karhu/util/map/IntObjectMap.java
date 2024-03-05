/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.map;

import java.util.Map;

public interface IntObjectMap<V> extends Map<Integer, V> {
	V get(int integer);

	V put(int integer, V object);

	V remove(int integer);

	Iterable<IntObjectMap.PrimitiveEntry<V>> entries();

	boolean containsKey(int integer);

	public interface PrimitiveEntry<V> {
		int key();

		V value();

		void setValue(V object);
	}
}
