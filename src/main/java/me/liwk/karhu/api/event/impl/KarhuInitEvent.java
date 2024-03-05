/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.impl;

import me.liwk.karhu.api.event.KarhuEvent;

public final class KarhuInitEvent extends KarhuEvent {
	private final long loadTime;

	@Override
	public boolean isCancellable() {
		return false;
	}

	public long getLoadTime() {
		return this.loadTime;
	}

	public KarhuInitEvent(long loadTime) {
		this.loadTime = loadTime;
	}
}
