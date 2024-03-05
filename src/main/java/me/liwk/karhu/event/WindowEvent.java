/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class WindowEvent extends Event {
	private final long timeStamp;

	public WindowEvent(long nano) {
		this.timeStamp = nano;
	}

	public long getTimeStamp() {
		return this.timeStamp;
	}
}
