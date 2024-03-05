/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.menu;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import java.util.Arrays;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.util.gui.Button;
import me.liwk.karhu.util.gui.Gui;
import me.liwk.karhu.util.gui.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ViolationMenu {
	private static ConfigManager cfg = Karhu.getInstance().getConfigManager();

	public static void openViolationGui(Player opener, Check check, CheckInfo checkInfo, SubCategory subCategory) {
		final Gui gui = new Gui(
			ChatColor.translateAlternateColorCodes(
				'&', Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight() + "&l" + Karhu.getInstance().getConfigManager().getName() + "&7 - " + checkInfo.name()
			),
			27
		);
		final String name = checkInfo.name();
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			gui.addButton(
				new Button(
					1,
					9,
					ItemUtil.makeItem(
						Material.RED_STAINED_GLASS_PANE, 1, cfg.getGuiHighlightColor() + "§2-1VL", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Subtract by §c1 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 1, false);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(1, 10, ItemUtil.makeItem(Material.RED_STAINED_GLASS_PANE, 1, "§c-5VL", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Subtract by 5 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 5, false);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(1, 11, ItemUtil.makeItem(Material.RED_STAINED_GLASS_PANE, 1, "§c-10VL", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Subtract by §c10 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 10, false);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
		} else {
			gui.addButton(
				new Button(
					1,
					9,
					ItemUtil.makeItem(
						Material.getMaterial("STAINED_GLASS_PANE"),
						(short)14,
						1,
						cfg.getGuiHighlightColor() + "§c-1VL",
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Substract by §c1 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 1, false);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(
					1,
					10,
					ItemUtil.makeItem(
						Material.getMaterial("STAINED_GLASS_PANE"),
						(short)14,
						1,
						cfg.getGuiHighlightColor() + "§c-5VL",
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Substract by §c5 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 5, false);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(
					1,
					11,
					ItemUtil.makeItem(
						Material.getMaterial("STAINED_GLASS_PANE"),
						(short)14,
						1,
						cfg.getGuiHighlightColor() + "§c-10VL",
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Substract by §c10 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 10, false);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
		}

		gui.addButton(
			new Button(
				1,
				13,
				ItemUtil.makeItem(
					Karhu.getInstance().getCheckState().isEnabled(checkInfo.name()) ? Material.ENCHANTED_BOOK : Material.BOOK,
					1,
					cfg.getGuiHighlightColor() + (checkInfo.experimental() ? checkInfo.name() + "§aΔ" : checkInfo.name()),
					checkInfo.credits().equals("")
						? Arrays.asList(
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
							"§7Enabled: " + getCheckMark(name, false),
							"§7Punishable: " + getCheckMark(name, true),
							"",
							"§7Punish-VL: " + cfg.getGuiHighlightColor() + Karhu.getInstance().getCheckState().getCheckVl(name),
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
						)
						: Arrays.asList(
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
							"§7Enabled: " + getCheckMark(name, false),
							"§7Punishable: " + getCheckMark(name, true),
							"",
							"§7Punish-VL: " + cfg.getGuiHighlightColor() + Karhu.getInstance().getCheckState().getCheckVl(name),
							"",
							"" + checkInfo.credits(),
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
						)
				)
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					gui.close(clicker);
					MChecksMenu.openMainMenu(clicker);
				}
			}
		);
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			gui.addButton(
				new Button(
					1,
					15,
					ItemUtil.makeItem(
						Material.GREEN_STAINED_GLASS_PANE, 1, cfg.getGuiHighlightColor() + "§21VL", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Additon by §21 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 1, true);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(1, 16, ItemUtil.makeItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§25VL", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Additon by §25 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 5, true);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(1, 17, ItemUtil.makeItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2-0VL", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Additon by §210 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 10, true);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
		} else {
			gui.addButton(
				new Button(
					1,
					15,
					ItemUtil.makeItem(
						Material.getMaterial("STAINED_GLASS_PANE"),
						(short)5,
						1,
						cfg.getGuiHighlightColor() + "§21VL",
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Additon by §21 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 1, true);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(
					1,
					16,
					ItemUtil.makeItem(
						Material.getMaterial("STAINED_GLASS_PANE"),
						(short)5,
						1,
						cfg.getGuiHighlightColor() + "§25VL",
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Additon by §25 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 5, true);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
			gui.addButton(
				new Button(
					1,
					17,
					ItemUtil.makeItem(
						Material.getMaterial("STAINED_GLASS_PANE"),
						(short)5,
						1,
						cfg.getGuiHighlightColor() + "§210VL",
						Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Additon by §210 vl", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤")
					)
				) {
					@Override
					public void onClick(Player clicker, ClickType clickType) {
						int newVl = ViolationMenu.updateCheckVl(check, 10, true);
						Karhu.getInstance().getCheckState().setPunishVl(checkInfo.name(), newVl);
						Karhu.getInstance().getConfigManager().saveChecks();
						Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
						ViolationMenu.updateCheckMiddle(this, checkInfo, name);
					}
				}
			);
		}

		gui.addButton(
			new Button(
				1,
				26,
				ItemUtil.makeItem(Material.EMERALD, 1, cfg.getGuiHighlightColor() + "Back", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Go back to the last menu", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					gui.close(clicker);
					ChecksMenuLegacy.openCheckSettingGUI(clicker, subCategory);
				}
			}
		);
		gui.open(opener);
		opener.updateInventory();
	}

	public static String getCheckMark(String name, boolean ab) {
		if (ab) {
			return Karhu.getInstance().getCheckState().isAutoban(name) ? "§a✔" : "§c✗";
		} else {
			return Karhu.getInstance().getCheckState().isEnabled(name) ? "§a✔" : "§c✗";
		}
	}

	public static int updateCheckVl(Check check, int decinc, boolean increment) {
		ConfigManager checkConfig = Karhu.getInstance().getConfigManager();
		FileConfiguration checkConfiguration = checkConfig.getChecks();
		String name = check.getName();
		String category = check.getCategory().name();
		String[] idk;
		if (name.contains(" ")) {
			idk = name.split(" ");
		} else {
			idk = new String[]{name, "(A)"};
		}

		String realTypeName = idk[0];
		String typeChars = idk[1].replaceAll("[^a-zA-Z0-9]", "");
		int vl;
		if (increment) {
			vl = Karhu.getInstance().getCheckState().getCheckVl(name) + decinc;
		} else {
			vl = Karhu.getInstance().getCheckState().getCheckVl(name) - decinc;
		}

		if (vl < 1) {
			vl = 1;
		}

		checkConfiguration.set(category + "." + realTypeName + "." + typeChars + ".punish-vl", vl);
		return vl;
	}

	public static void updateCheckMiddle(Button button, CheckInfo checkInfo, String name) {
		ItemStack stack = button.item;
		ItemMeta meta = stack.getItemMeta();
		meta.setLore(
			checkInfo.credits().equals("")
				? Arrays.asList(
					"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
					"§7Enabled: " + getCheckMark(name, false),
					"§7Punishable: " + getCheckMark(name, true),
					"",
					"§7Punish-VL: " + cfg.getGuiHighlightColor() + Karhu.getInstance().getCheckState().getCheckVl(name),
					"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
				)
				: Arrays.asList(
					"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
					"§7Enabled: " + getCheckMark(name, false),
					"§7Punishable: " + getCheckMark(name, true),
					"",
					"§7Punish-VL: " + cfg.getGuiHighlightColor() + Karhu.getInstance().getCheckState().getCheckVl(name),
					"",
					"" + checkInfo.credits(),
					"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
				)
		);
		stack.setItemMeta(meta);
		stack.setType(Karhu.getInstance().getCheckState().isEnabled(checkInfo.name()) ? Material.ENCHANTED_BOOK : Material.BOOK);
		button.inv.setItem(13, stack);
	}
}
