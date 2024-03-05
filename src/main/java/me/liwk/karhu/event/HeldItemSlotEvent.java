/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

public class HeldItemSlotEvent extends Event {
	private final int slot;

	public HeldItemSlotEvent(int slot) {
		this.slot = slot;
	}

	public int getSlot() {
		return this.slot;
	}
}
