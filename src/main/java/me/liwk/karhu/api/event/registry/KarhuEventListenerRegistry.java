/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.event.registry;

import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.KarhuListener;

public interface KarhuEventListenerRegistry {
	boolean fireEvent(KarhuEvent karhuEvent);

	void shutdown();

	void addListener(KarhuListener karhuListener);

	void removeListener(KarhuListener karhuListener);
}
