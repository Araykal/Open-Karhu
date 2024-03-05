/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Button {
	public int pos;
	public Inventory inv;
	public ItemStack item;
	public int page;

	@Deprecated
	public Button(Inventory inv, int pos, ItemStack item) {
		this.inv = inv;
		this.pos = pos;
		this.item = item;
		this.page = 1;
	}

	public Button(int page, int pos, ItemStack item) {
		this.page = page;
		this.pos = pos;
		this.item = item;
	}

	public abstract void onClick(Player player, ClickType clickType);
}
