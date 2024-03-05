/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.data.task;

public interface IAbstractTickTask<T> {
	Runnable getRunnable();

	EmptyPredicate conditionUntil();

	String getId();
}
