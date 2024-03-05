/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class AbilityEvent extends Event {
	private final long timeStamp = System.nanoTime() / 1000000L;

	public long getTimeStamp() {
		return this.timeStamp;
	}
}
