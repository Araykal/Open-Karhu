/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.util;

public final class Conditions {
	public static void notNull(Object o, String msg) {
		if (o == null) {
			throw new IllegalArgumentException(msg);
		}
	}
}
