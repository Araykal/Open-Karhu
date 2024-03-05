/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.set;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PassiveExpiringSet<V> extends AbstractSetDecorator<V> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final long timeToLiveMillis;
	private final Map<Object, Long> expirationMap = new HashMap<>();

	public PassiveExpiringSet(long timeToLiveMillis) {
		super(new HashSet<>());
		this.timeToLiveMillis = timeToLiveMillis;
	}

	@Override
	public boolean add(V value) {
		this.removeAllExpired();
		this.expirationMap.put(value, this.now());
		return super.add(value);
	}

	@Override
	public boolean remove(Object key) {
		this.expirationMap.remove(key);
		return super.remove(key);
	}

	@Override
	public boolean contains(Object value) {
		this.removeAllExpired();
		return super.contains(value);
	}

	@Override
	public int size() {
		this.removeAllExpired();
		return super.size();
	}

	@Override
	public void clear() {
		this.expirationMap.clear();
		super.clear();
	}

	private void removeAllExpired() {
		Iterator<Entry<Object, Long>> iterator = this.expirationMap.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Object, Long> expirationEntry = iterator.next();
			if (this.expired(expirationEntry.getValue())) {
				super.remove(expirationEntry.getKey());
				iterator.remove();
			}
		}
	}

	private boolean expired(long insertTime) {
		return this.now() - insertTime >= this.timeToLiveMillis;
	}

	private long now() {
		return System.currentTimeMillis();
	}
}
