/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.check;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.liwk.karhu.Karhu;
import org.bukkit.Material;

public enum SubCategory {
	REACH(Category.COMBAT, 20, Material.DIAMOND_SWORD),
	KILLAURA(Category.COMBAT, 21, Material.BLAZE_ROD),
	AIM(Category.COMBAT, 22, Material.NETHER_STAR),
	AUTOCLICKER(Category.COMBAT, 23, Material.LEVER),
	VELOCITY(Category.COMBAT, 24, Material.BLAZE_POWDER),
	FLY(Category.MOVEMENT, 11, Material.FEATHER),
	SPEED(Category.MOVEMENT, 12, Material.SUGAR),
	MOTION(Category.MOVEMENT, 13, Material.GOLD_INGOT),
	INVENTORY(Category.MOVEMENT, 14, Material.DIAMOND),
	JESUS(Category.MOVEMENT, 15, Material.WATER_BUCKET),
	SCAFFOLD(Category.WORLD, 33, Material.EMERALD),
	NOFALL(Category.WORLD, 32, Material.ANVIL),
	BLOCK(Category.WORLD, 29, Material.BRICK),
	BADPACKETS(Category.PACKET, 30, Material.TNT),
	TIMER(Category.PACKET, 31, Material.REDSTONE);

	private final Category category;
	private final int slot;
	private final Material item;

	private SubCategory(Category category, int slot, Material item) {
		this.category = category;
		this.slot = slot;
		if (item == Material.DIAMOND) {
			item = Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2) ? Material.CHEST_MINECART : Material.getMaterial("STORAGE_MINECART");
		} else if (item == Material.EMERALD) {
			item = Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2) ? Material.BIRCH_STAIRS : Material.getMaterial("BIRCH_WOOD_STAIRS");
		} else if (item == Material.GOLD_INGOT) {
			item = Karhu.SERVER_VERSION.isOlderThanOrEquals(ServerVersion.V_1_7_10) ? Material.GLASS_BOTTLE : Material.getMaterial("RABBIT_FOOT");
		}

		this.item = item;
	}

	public Category getCategory() {
		return this.category;
	}

	public int getSlot() {
		return this.slot;
	}

	public Material getItem() {
		return this.item;
	}
}
