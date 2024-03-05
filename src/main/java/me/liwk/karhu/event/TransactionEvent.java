/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class TransactionEvent extends Event {
	private final long now;

	public TransactionEvent(long now) {
		this.now = now;
	}

	public long getNow() {
		return this.now;
	}
}
