/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.menu;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import java.util.Arrays;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.util.gui.Button;
import me.liwk.karhu.util.gui.Gui;
import me.liwk.karhu.util.gui.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KarhuMenu {
	private static ConfigManager cfg = Karhu.getInstance().getConfigManager();

	public static void openMenu(Player opener) {
		int[] blueGlass = new int[]{0, 1, 3, 4, 5, 7, 8, 9, 17, 18, 19, 21, 22, 23, 25, 26};
		int[] whiteGlass = new int[]{2, 6, 10, 12, 14, 16, 20, 24};
		final Gui gui = new Gui(
			ChatColor.translateAlternateColorCodes(
				'&', Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight() + "&l" + Karhu.getInstance().getConfigManager().getName() + "&7 - Menu"
			),
			27
		);
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			for (int pos : blueGlass) {
				gui.addItem(1, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), pos);
			}

			for (int pos : whiteGlass) {
				gui.addItem(1, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), pos);
			}
		} else {
			for (int pos : blueGlass) {
				gui.addItem(1, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short)3), pos);
			}

			for (int pos : whiteGlass) {
				gui.addItem(1, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short)0), pos);
			}
		}

		gui.addButton(
			new Button(
				1, 11, ItemUtil.makeItem(Material.BEACON, 1, cfg.getGuiHighlightColor() + "Checks", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Manage check states", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					gui.close(clicker);
					MChecksMenu.openMainMenu(clicker);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				13,
				ItemUtil.makeItem(
					Material.DIAMOND_CHESTPLATE,
					1,
					cfg.getGuiHighlightColor() + "Info",
					Arrays.asList(
						"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
						"§7Version: §a" + Karhu.getInstance().getVersion() + " | " + Karhu.getInstance().getBuild(),
						"§7TPS: §a" + Karhu.getInstance().getTPS(),
						"§7Disabled: §a" + Karhu.getInstance().hasRecentlyDropped(1000L),
						"§7",
						"§7" + cfg.getName() + " has §b" + Karhu.getInstance().getCheckState().loadOrGetClasses().size() + " §7different checks",
						"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
					)
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(
						Arrays.asList(
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
							"§7Version: §a" + Karhu.getInstance().getVersion() + " | " + Karhu.getInstance().getBuild(),
							"§7TPS: §a" + Karhu.getInstance().getTPS(),
							"§7Disabled: §a" + Karhu.getInstance().hasRecentlyDropped(1000L),
							"§7",
							"§7" + KarhuMenu.cfg.getName() + " has §b" + Karhu.getInstance().getCheckState().loadOrGetClasses().size() + " §7different checks",
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
						)
					);
					stack.setItemMeta(meta);
					this.inv.setItem(this.pos, this.item);
				}
			}
		);
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			gui.addButton(
				new Button(
					1,
					15,
					ItemUtil.makeItem(Material.WRITABLE_BOOK, 1, cfg.getGuiHighlightColor() + "Settings", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Control settings", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						gui.close(clicker);
						SettingsMenu.openSettingMenu(clicker);
					}
				}
			);
		} else {
			gui.addButton(
				new Button(
					1,
					15,
					ItemUtil.makeItem(
						Material.getMaterial("BOOK_AND_QUILL"), 1, cfg.getGuiHighlightColor() + "Settings", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Control settings", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						gui.close(clicker);
						SettingsMenu.openSettingMenu(clicker);
					}
				}
			);
		}

		gui.open(opener);
		opener.updateInventory();
	}
}
