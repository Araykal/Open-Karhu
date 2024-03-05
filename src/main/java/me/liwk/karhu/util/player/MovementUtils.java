/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.player;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MovementUtils {
	public static double getHorizontalDistanceSpeed(Location to, Location from, Player p) {
		double x = Math.abs(to.getX()) - Math.abs(from.getX());
		double z = Math.abs(to.getZ()) - Math.abs(from.getZ());
		return Math.sqrt(x * x + z * z);
	}

	public static double offset(Vector from, Vector to) {
		from.setY(0);
		to.setY(0);
		return to.subtract(from).length();
	}

	public static int getDepthStriderLevel(Player player) {
		if (player.getInventory().getBoots() != null) {
			Enchantment enchLegacy = Enchantment.getByName("DEPTH_STRIDER");
			Enchantment enchModern = Enchantment.getByName("depth_strider");
			if (enchLegacy != null && hasEnchantment(player.getInventory().getBoots(), enchLegacy)) {
				return (Integer)player.getInventory().getBoots().getEnchantments().get(enchLegacy);
			}

			if (enchModern != null && hasEnchantment(player.getInventory().getBoots(), enchModern)) {
				return (Integer)player.getInventory().getBoots().getEnchantments().get(enchModern);
			}
		}

		return 0;
	}

	public static int getSoulSpeedLevel(Player player) {
		return player.getInventory().getBoots() != null && hasEnchantment(player.getInventory().getBoots(), Enchantment.getByName("SOUL_SPEED"))
			? (Integer)player.getInventory().getBoots().getEnchantments().get(Enchantment.getByName("SOUL_SPEED"))
			: 0;
	}

	public static boolean hasEnchantment(ItemStack item, Enchantment enchantment) {
		return item.getEnchantments().containsKey(enchantment);
	}

	public static boolean searchEnchant(Player player, Enchantment enchantment) {
		for (ItemStack stack : player.getInventory()) {
			if (stack != null && !stack.getEnchantments().isEmpty() && stack.getEnchantments().containsKey(enchantment)) {
				return true;
			}
		}

		return false;
	}

	public static int getEnchantmentLevel(ItemStack item, Enchantment enchantment) {
		return item.getEnchantmentLevel(enchantment);
	}
}
