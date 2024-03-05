/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.interfaces;

import me.liwk.karhu.util.location.CustomLocation;

public interface ICrashHandler {
	void handleClientKeepAlive();

	void handleFlying(boolean boolean1, boolean boolean2, CustomLocation customLocation3, CustomLocation customLocation4);

	void handleArm();

	void handleWindowClick(int integer1, int integer2, int integer3, int integer4);

	void handleSlot();

	void handleCustomPayload();

	void handlePlace();
}
