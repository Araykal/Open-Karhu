/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.collision.enums;

public enum Boxes {
	BOAT(0.6F, 1.5F),
	PLAYER(1.8F, 0.6F),
	CROUCH(1.5F, 0.6F);

	private final float height;
	private final float width;

	private Boxes(float h, float w) {
		this.width = w / 2.0F;
		this.height = h;
	}

	public float getHeight() {
		return this.height;
	}

	public float getWidth() {
		return this.width;
	}
}
