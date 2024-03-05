/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class SwingEvent extends Event {
	private final long nanoTime;
	private final long timeMillis;

	public SwingEvent(long nanoTime, long timeMillis) {
		this.nanoTime = nanoTime;
		this.timeMillis = timeMillis;
	}

	public long getTimeStamp() {
		return this.nanoTime;
	}

	public long getTimeStampMS() {
		return this.timeMillis;
	}
}
