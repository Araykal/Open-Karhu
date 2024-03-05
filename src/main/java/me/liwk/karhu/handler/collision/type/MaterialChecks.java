/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.collision.type;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

public class MaterialChecks {
	public static Set<Material> AIR = null;
	public static Set<Material> MOVABLE = null;
	public static Set<Material> SHULKER_BOXES = null;
	public static Set<Material> ICE = null;
	public static Set<Material> SIGNS = null;
	public static Set<Material> HALFS = null;
	public static Set<Material> GRASS = null;
	public static Set<Material> DOORS = null;
	public static Set<Material> TRAPS = null;
	public static Set<Material> LIQUIDS = null;
	public static Set<Material> WATER = null;
	public static Set<Material> LAVA = null;
	public static Set<Material> SEASHIT = null;
	public static Set<Material> FENCES = null;
	public static Set<Material> PANES = null;
	public static Set<Material> WEIRD_SOLID = null;
	public static Set<Material> WEIRD_SOLID_NO_LIQUID = null;
	public static Set<Material> STAIRS = null;
	public static Set<Material> BED = null;
	public static Set<Material> LILY = null;
	public static Set<Material> WEB = null;
	public static Set<Material> SLIME = null;
	public static Set<Material> SOUL = null;
	public static Set<Material> HONEY = null;
	public static Set<Material> BERRIES = null;
	public static Set<Material> SCAFFOLD = null;
	public static Set<Material> CLIMBABLE = null;
	public static Set<Material> REDSTONE = null;
	public static Set<Material> CARPETS = null;
	public static Set<Material> ONETAPS = null;
	public static Set<Material> BUTTONS = null;
	public static Set<Material> TORCHES = null;
	public static Set<Material> RETARD_FACE = null;
	public static Set<Material> PORTAL = null;
	public static Set<Material> POWDERSNOW = null;
	public static Set<Material> DRIP_LEAF = null;
	public static Set<Material> EDIBLE_WITHOUT_HUNGER = null;
	public static Set<Material> SWORDS = null;
	public static Set<Material> BOWS = null;
	public static Set<Material> LIQUID_BUCKETS = null;
	public static Set<Material> CLEARICE = null;
	public static Set<Material> PACKEDICE = null;
	public static Set<Material> FROSTEDICE = null;
	public static Set<Material> BLUEICE = null;

	public static Set<Material> find(String... array) {
		Set<Material> mats = new HashSet<>();

		for (String shits : array) {
			for (Material c : Material.values()) {
				if (c.name().contains(shits)) {
					mats.add(c);
				}
			}
		}

		return mats;
	}

	public static Set<Material> fastFind(String... array) {
		Set<Material> mats = new HashSet<>();

		for (String shits : array) {
			try {
				Material material = Material.valueOf(shits);
				mats.add(material);
			} catch (IllegalArgumentException var7) {
			}
		}

		return mats;
	}

	static {
		try {
			AIR = fastFind("AIR", "CAVE_AIR", "VOID_AIR");
			BED = find("BED");
			MOVABLE = find("SHULKER_BOX", "PISTON");
			SHULKER_BOXES = find("SHULKER_BOX");
			FENCES = find("FENCE", "GATE", "WALL", "COBBLE_WALL", "PANE", "THIN");
			PANES = find("PANE", "THIN");
			ICE = find("ICE", "PACKED");
			GRASS = find("GRASS", "FLOWER", "ROSE");
			PORTAL = find("PORTAL_FRAME");
			DOORS = find("DOOR");
			TRAPS = find("TRAP");
			HALFS = find(
				"SLAB",
				"STEP",
				"DAYLIGHT",
				"SENSOR",
				"SNOW",
				"SKULL",
				"HEAD",
				"CAKE",
				"POT",
				"BEAN",
				"COCOA",
				"ENCH",
				"STONECUTTER",
				"LANTERN",
				"CAMPFIRE",
				"CANDLE",
				"PICKLE",
				"BELL",
				"AMETHYST",
				"BED"
			);
			STAIRS = find("STAIR");
			SIGNS = find("SIGN");
			CARPETS = find("CARPET");
			REDSTONE = find("DIODE", "REPEATER", "COMPARATOR");
			WEIRD_SOLID = find(
				"LILY",
				"COCOA",
				"REDSTONE_",
				"POT",
				"ROD",
				"CARPET",
				"WATER",
				"BUBBLE",
				"LAVA",
				"SKULL",
				"LADDER",
				"SNOW",
				"SCAFFOLD",
				"DIODE",
				"REPEATER",
				"COMPARATOR",
				"VINE",
				"CANDLE",
				"PICKLE",
				"DRIP_LEAF"
			);
			WEIRD_SOLID_NO_LIQUID = find(
				"LILY", "COCOA", "REDSTONE_", "POT", "ROD", "CARPET", "SKULL", "LADDER", "SNOW", "SCAFFOLD", "DIODE", "REPEATER", "COMPARATOR", "VINE", "CANDLE", "PICKLE", "DRIP_LEAF"
			);
			LIQUIDS = fastFind("WATER", "STATIONARY_WATER", "LAVA", "STATIONARY_LAVA");
			WATER = find("WATER", "STATIONARY_WATER", "BUBBLE_COLUMN");
			LAVA = find("LAVA", "STATIONARY_LAVA");
			SEASHIT = find("KELP", "SEAGRASS");
			LILY = find("LILY");
			DRIP_LEAF = find("DRIPLEAF");
			WEB = find("WEB");
			SLIME = find("SLIME_BLOCK");
			SOUL = fastFind("SOUL_SAND");
			HONEY = find("HONEY_BLOCK");
			BERRIES = find("SWEET");
			SCAFFOLD = find("SCAFFOLDING");
			POWDERSNOW = find("POWDER_SNOW");
			CLIMBABLE = find("VINE", "LADDER", "SCAFFOLDING");
			ONETAPS = find("SLIME_BLOCK", "FLOWER", "ROSE", "TORCH");
			BUTTONS = find("BUTTON");
			TORCHES = find("TORCH");
			RETARD_FACE = find("TORCH", "BUTTON", "SIGN");
			EDIBLE_WITHOUT_HUNGER = find("GOLDEN_APPLE", "POTION", "BOTTLE", "MILK_BUCKET");
			SWORDS = find("SWORD");
			BOWS = find("BOW");
			LIQUID_BUCKETS = fastFind("WATER_BUCKET", "LAVA_BUCKET");
			CLEARICE = fastFind("ICE");
			PACKEDICE = fastFind("PACKED_ICE");
			FROSTEDICE = fastFind("FROSTED_ICE");
			BLUEICE = fastFind("BLUE_ICE");
		} catch (Exception var1) {
			var1.printStackTrace();
		}
	}
}
