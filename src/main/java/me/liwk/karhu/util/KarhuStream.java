/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class KarhuStream<T> {
	private Collection<T> c;

	public KarhuStream(Collection<T> l) {
		this.c = l;
	}

	public boolean any(Predicate<T> p) {
		for (T t : this.c) {
			if (p.test(t)) {
				return true;
			}
		}

		return false;
	}

	public boolean all(Predicate<T> p) {
		for (T t : this.c) {
			if (!p.test(t)) {
				return false;
			}
		}

		return true;
	}

	public T find(Predicate<T> p) {
		for (T t : this.c) {
			if (p.test(t)) {
				return t;
			}
		}

		return null;
	}

	public void setCollection(Collection<T> c) {
		this.c = c;
	}

	public boolean isEmpty() {
		return this.c.isEmpty();
	}

	public void forEach(Consumer<? super T> consumer) {
		this.c.forEach(consumer);
	}
}
