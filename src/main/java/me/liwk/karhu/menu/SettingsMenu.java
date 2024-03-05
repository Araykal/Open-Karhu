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

public class SettingsMenu {
	private static ConfigManager cfg = Karhu.getInstance().getConfigManager();

	public static Material getItem(boolean conf) {
		return conf ? Material.ENCHANTED_BOOK : Material.BOOK;
	}

	public static void openSettingMenu(Player opener) {
		int[] blueGlass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
		final Gui gui = new Gui(ChatColor.translateAlternateColorCodes('&', cfg.getGuiHighlightColor() + Karhu.getInstance().getConfigManager().getName() + "§7 - Settings"), 27);
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			for (int pos : blueGlass) {
				gui.addItem(1, new ItemStack(Material.WHITE_STAINED_GLASS_PANE), pos);
			}
		} else {
			for (int pos : blueGlass) {
				gui.addItem(1, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short)0), pos);
			}
		}

		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			gui.addItem(1, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), 9);
			gui.addItem(1, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), 17);
		} else {
			gui.addItem(1, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short)3), 9);
			gui.addItem(1, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short)3), 17);
		}

		gui.addButton(
			new Button(
				1,
				10,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isAnticrash()),
					1,
					cfg.getGuiHighlightColor() + "Anticrash",
					Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isAnticrash()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (Karhu.getInstance().getConfigManager().isAnticrash()) {
						Karhu.getInstance().getConfigManager().getConfig().set("anticrash.enabled", false);
					} else {
						Karhu.getInstance().getConfigManager().getConfig().set("anticrash.enabled", true);
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isAnticrash()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"));
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isAnticrash()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				11,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isAutoban()),
					1,
					cfg.getGuiHighlightColor() + "Autoban",
					Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isAutoban()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (Karhu.getInstance().getConfigManager().isAutoban()) {
						Karhu.getInstance().getConfigManager().getConfig().set("autoban", false);
					} else {
						Karhu.getInstance().getConfigManager().getConfig().set("autoban", true);
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isAutoban()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"));
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isAutoban()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				12,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isPunishBroadcast()),
					1,
					cfg.getGuiHighlightColor() + "Punishment broadcast",
					Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isPunishBroadcast()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (Karhu.getInstance().getConfigManager().isPunishBroadcast()) {
						Karhu.getInstance().getConfigManager().getConfig().set("Punishments.broadcast", false);
					} else {
						Karhu.getInstance().getConfigManager().getConfig().set("Punishments.broadcast", true);
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isPunishBroadcast()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					);
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isPunishBroadcast()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				13,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isBypass()),
					1,
					cfg.getGuiHighlightColor() + "Bypass permission",
					Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isBypass()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (Karhu.getInstance().getConfigManager().isBypass()) {
						Karhu.getInstance().getConfigManager().getConfig().set("bypass-permission", false);
					} else {
						Karhu.getInstance().getConfigManager().getConfig().set("bypass-permission", true);
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isBypass()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"));
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isBypass()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				14,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isClientCheck()),
					1,
					cfg.getGuiHighlightColor() + "Client check",
					Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isClientCheck()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (Karhu.getInstance().getConfigManager().isClientCheck()) {
						Karhu.getInstance().getConfigManager().getConfig().set("client-check", false);
					} else {
						Karhu.getInstance().getConfigManager().getConfig().set("client-check", true);
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isClientCheck()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					);
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isClientCheck()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				15,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isPullback()),
					1,
					cfg.getGuiHighlightColor() + "Pullback",
					Arrays.asList(
						"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
						"§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isPullback()),
						"§7Mode: §b" + Karhu.getInstance().getConfigManager().getPullbackMode(),
						"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
					)
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (clickType == ClickType.LEFT) {
						if (Karhu.getInstance().getConfigManager().isPullback()) {
							Karhu.getInstance().getConfigManager().getConfig().set("pullback.enabled", false);
						} else {
							Karhu.getInstance().getConfigManager().getConfig().set("pullback.enabled", true);
						}
					} else if (clickType == ClickType.RIGHT) {
						if (Karhu.getInstance().getConfigManager().getPullbackMode().equalsIgnoreCase("to-the-void")) {
							Karhu.getInstance().getConfigManager().getConfig().set("pullback.type", "generic");
						} else {
							Karhu.getInstance().getConfigManager().getConfig().set("pullback.type", "to-the-void");
						}
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(
						Arrays.asList(
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
							"§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isPullback()),
							"§7Mode: §b" + Karhu.getInstance().getConfigManager().getPullbackMode(),
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
						)
					);
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isPullback()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				16,
				ItemUtil.makeItem(
					getItem(Karhu.getInstance().getConfigManager().isDiscordAlert()),
					1,
					cfg.getGuiHighlightColor() + "Discord webhook",
					Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + getCheckMark(Karhu.getInstance().getConfigManager().isDiscordAlert()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					if (Karhu.getInstance().getConfigManager().isDiscordAlert()) {
						Karhu.getInstance().getConfigManager().getConfig().set("discord.enabled", false);
					} else {
						Karhu.getInstance().getConfigManager().getConfig().set("discord.enabled", true);
					}
	
					Karhu.getInstance().getConfigManager().save();
					Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
					ItemStack stack = this.item;
					ItemMeta meta = stack.getItemMeta();
					meta.setLore(
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Enabled: " + SettingsMenu.getCheckMark(Karhu.getInstance().getConfigManager().isDiscordAlert()), "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					);
					stack.setItemMeta(meta);
					stack.setType(SettingsMenu.getItem(Karhu.getInstance().getConfigManager().isDiscordAlert()));
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(
				1,
				26,
				ItemUtil.makeItem(Material.EMERALD, 1, cfg.getGuiHighlightColor() + "Back", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Go back to the last menu", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					gui.close(clicker);
					KarhuMenu.openMenu(clicker);
				}
			}
		);
		gui.open(opener);
		opener.updateInventory();
	}

	public static String getCheckMark(boolean conf) {
		return conf ? "§a✔" : "§c✗";
	}
}
