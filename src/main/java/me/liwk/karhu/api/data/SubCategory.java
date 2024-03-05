/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.data;

public enum SubCategory {
	REACH(Category.COMBAT, 10),
	KILLAURA(Category.COMBAT, 11),
	AIM(Category.COMBAT, 13),
	AUTOCLICKER(Category.COMBAT, 15),
	VELOCITY(Category.COMBAT, 16),
	FLY(Category.MOVEMENT, 10),
	SPEED(Category.MOVEMENT, 11),
	JESUS(Category.MOVEMENT, 12),
	MOTION(Category.MOVEMENT, 14),
	INVENTORY(Category.MOVEMENT, 16),
	SCAFFOLD(Category.WORLD, 10),
	NOFALL(Category.WORLD, 14),
	BLOCK(Category.WORLD, 16),
	BADPACKETS(Category.PACKET, 11),
	TIMER(Category.PACKET, 13);

	private final Category category;
	private final int slot;

	private SubCategory(Category category, int slot) {
		this.category = category;
		this.slot = slot;
	}

	public Category getCategory() {
		return this.category;
	}

	public int getSlot() {
		return this.slot;
	}
}
