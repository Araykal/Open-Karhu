/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.set;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ConcurrentSet<E> extends AbstractSet<E> implements Serializable {
	private static final long serialVersionUID = -6761513279741915432L;
	private final ConcurrentMap<E, Boolean> map = new ConcurrentHashMap<>();

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean contains(Object o) {
		return this.map.containsKey(o);
	}

	@Override
	public boolean add(E o) {
		return this.map.putIfAbsent(o, Boolean.TRUE) == null;
	}

	@Override
	public boolean remove(Object o) {
		return this.map.remove(o) != null;
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Iterator<E> iterator() {
		return this.map.keySet().iterator();
	}
}
