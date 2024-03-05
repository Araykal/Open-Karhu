/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.gui;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtil {
	public static ItemStack makeItem(Material mat, int amount, String displayName, List<String> lore) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		meta.setLore(lore);
		item.setAmount(amount);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack makeItem(Material mat, short damage, int amount, String displayName, List<String> lore) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		meta.setLore(lore);
		item.setAmount(amount);
		item.setItemMeta(meta);
		item.setDurability(damage);
		return item;
	}

	public static ItemStack makeItem(Material mat, int amount, String displayName) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		item.setAmount(amount);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack makeSkullItem(String target, int amount, String displayName, boolean legacy, List<String> lore) {
		ItemStack item;
		if (legacy) {
			item = new ItemStack(Material.getMaterial("SKULL_ITEM"), amount, (short)3);
		} else {
			item = new ItemStack(Material.PLAYER_HEAD, amount);
		}

		SkullMeta meta = (SkullMeta)item.getItemMeta();
		meta.setOwner(target);
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack makeItem(Material mat, int amount) {
		return new ItemStack(mat, amount);
	}

	public static ItemStack makeItem(Material mat) {
		return new ItemStack(mat);
	}
}
