/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class SteerEvent extends Event {
	private final boolean unmount;

	public SteerEvent(boolean unmount) {
		this.unmount = unmount;
	}

	public boolean isUnmount() {
		return this.unmount;
	}
}
