/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.data.potion;

public class PotionData {
	private final PotionEffect potionEffect;
	private final int amplifier;

	public PotionData(PotionEffect potionEffect, int amplifier) {
		this.potionEffect = potionEffect;
		this.amplifier = amplifier;
	}

	public int getAmplifier() {
		return this.amplifier + 1;
	}

	public PotionEffect getPotionEffect() {
		return this.potionEffect;
	}
}
