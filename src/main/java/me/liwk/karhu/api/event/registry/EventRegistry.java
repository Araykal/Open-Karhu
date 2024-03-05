/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.registry;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.liwk.karhu.api.KarhuLogger;
import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.KarhuListener;

public final class EventRegistry implements KarhuEventListenerRegistry {
	private final List<KarhuListener> listeners = new CopyOnWriteArrayList<>();
	private int errors;
	private boolean forceShutdown;

	@Override
	public boolean fireEvent(KarhuEvent event) {
		if (!this.forceShutdown) {
			try {
				this.listeners.forEach(l -> l.onEvent(event));
				return !event.isCancelled();
			} catch (Throwable var3) {
				var3.printStackTrace();
				if (++this.errors >= 5) {
					KarhuLogger.critical("The event service has been set to idle due to numerous unknown errors\nIf you still want to utilize the event system, please restart your server");
					this.forceShutdown = true;
				}

				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public void addListener(KarhuListener listener) {
		if (!this.forceShutdown) {
			if (!this.listeners.contains(listener)) {
				this.listeners.add(listener);
			}
		} else {
			throw new RuntimeException("event registry is halted");
		}
	}

	@Override
	public void removeListener(KarhuListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void shutdown() {
		this.listeners.clear();
	}

	public void setForceShutdown(boolean forceShutdown) {
		this.forceShutdown = forceShutdown;
	}
}
