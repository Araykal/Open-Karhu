/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event;

import me.liwk.karhu.api.exception.EventNotCancellableException;

public abstract class KarhuEvent {
	private boolean cancelled = false;

	public final void cancel() {
		if (!this.isCancellable()) {
			throw new EventNotCancellableException(this);
		} else {
			this.cancelled = true;
		}
	}

	public boolean isCancellable() {
		return true;
	}

	public final boolean isCancelled() {
		return this.isCancellable() && this.cancelled;
	}
}
