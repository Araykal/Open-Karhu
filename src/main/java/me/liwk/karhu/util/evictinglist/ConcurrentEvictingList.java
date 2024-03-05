/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.evictinglist;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class ConcurrentEvictingList<T> extends ConcurrentLinkedDeque<T> {
	private int maxSize;

	public ConcurrentEvictingList(int maxSize) {
		this.maxSize = maxSize;
	}

	public ConcurrentEvictingList(Collection<? extends T> c, int maxSize) {
		super(c);
		this.maxSize = maxSize;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	@Override
	public boolean add(T t) {
		if (this.size() >= this.maxSize) {
			this.remove(Integer.valueOf(0));
		}

		return super.add(t);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return c.stream().anyMatch(this::add);
	}

	@Override
	public Stream<T> stream() {
		return new CopyOnWriteArrayList<>(this).stream();
	}
}
