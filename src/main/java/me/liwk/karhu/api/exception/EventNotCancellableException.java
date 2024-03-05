/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.exception;

import me.liwk.karhu.api.event.KarhuEvent;

public final class EventNotCancellableException extends RuntimeException {
	public EventNotCancellableException(KarhuEvent event) {
		super(event.getClass().getSimpleName() + " cannot be force-cancelled (is it cancellable?)");
	}
}
