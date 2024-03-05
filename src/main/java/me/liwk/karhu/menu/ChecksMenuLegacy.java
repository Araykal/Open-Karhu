/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.menu;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.data.KarhuPlayer;
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

public class ChecksMenuLegacy {
	private static ConfigManager cfg = Karhu.getInstance().getConfigManager();

	public static void openCheckSettingGUI(Player opener, SubCategory subCategory) {
		final Gui gui = new Gui(
			ChatColor.translateAlternateColorCodes(
				'&', Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight() + "&l" + Karhu.getInstance().getConfigManager().getName() + "&7 - " + subCategory.name()
			),
			subCategory.name().equals("AUTOCLICKER") ? 45 : 27
		);
		KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(opener);
		List<Check> guis = Arrays.stream(data.getCheckManager().getChecks()).filter(check -> check.getSubCategory() == subCategory).collect(Collectors.toList());
		int currentSlot = 0;
		gui.addButton(
			new Button(
				1,
				subCategory.name().equals("AUTOCLICKER") ? 44 : 26,
				ItemUtil.makeItem(Material.EMERALD, 1, cfg.getGuiHighlightColor() + "Back", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Go back to the last menu", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))
			) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					gui.close(clicker);
					MChecksMenu.openMainMenu(clicker);
				}
			}
		);

		for (final Check checkClass : guis) {
			if (!checkClass.isSilent()) {
				gui.addButton(
					new Button(
						1,
						currentSlot,
						ItemUtil.makeItem(
							Karhu.getInstance().getCheckState().isEnabled(checkClass.getName()) ? Material.ENCHANTED_BOOK : Material.BOOK,
							1,
							cfg.getGuiHighlightColor() + (checkClass.isExperimental() ? checkClass.getName() + "§aΔ" : checkClass.getName()),
							checkClass.getCredits().equals("")
								? Arrays.asList(
									"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
									"§7Enabled: " + getCheckMark(checkClass, false),
									"§7Punishable: " + getCheckMark(checkClass, true),
									"",
									"§7Punish-VL: " + cfg.getGuiHighlightColor() + checkClass.getBanVL(),
									"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
								)
								: Arrays.asList(
									"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
									"§7Enabled: " + getCheckMark(checkClass, false),
									"§7Punishable: " + getCheckMark(checkClass, true),
									"",
									"§7Punish-VL: " + cfg.getGuiHighlightColor() + checkClass.getBanVL(),
									"",
									"" + checkClass.getCredits(),
									"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
								)
						)
					) {
						@Override
						public void onClick(Player clicker, ClickType clickType) {
							boolean openAgain = true;
							if (clickType == ClickType.LEFT) {
								if (Karhu.getInstance().getCheckState().isEnabled(checkClass.getName())) {
									ChecksMenuLegacy.updateCheckStatus(checkClass, false, false);
									Karhu.getInstance().getCheckState().setEnabled(checkClass.getName(), false);
								} else {
									ChecksMenuLegacy.updateCheckStatus(checkClass, false, true);
									Karhu.getInstance().getCheckState().setEnabled(checkClass.getName(), true);
								}
							} else if (clickType == ClickType.RIGHT) {
								if (Karhu.getInstance().getCheckState().isAutoban(checkClass.getName())) {
									ChecksMenuLegacy.updateCheckStatus(checkClass, true, false);
									Karhu.getInstance().getCheckState().setAutoban(checkClass.getName(), false);
								} else {
									ChecksMenuLegacy.updateCheckStatus(checkClass, true, true);
									Karhu.getInstance().getCheckState().setAutoban(checkClass.getName(), true);
								}
							} else if (clickType == ClickType.MIDDLE) {
								gui.close(clicker);
								ViolationMenu.openViolationGui(clicker, checkClass, checkClass.getCheckInfo(), subCategory);
								openAgain = false;
							}
	
							if (openAgain) {
								Karhu.getInstance().getConfigManager().saveChecks();
								Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), true);
								ItemStack stack = this.item;
								ItemMeta meta = stack.getItemMeta();
								meta.setLore(
									checkClass.getCredits().equals("")
										? Arrays.asList(
											"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
											"§7Enabled: " + ChecksMenuLegacy.getCheckMark(checkClass, false),
											"§7Punishable: " + ChecksMenuLegacy.getCheckMark(checkClass, true),
											"",
											"§7Punish-VL: " + ChecksMenuLegacy.cfg.getGuiHighlightColor() + checkClass.getBanVL(),
											"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
										)
										: Arrays.asList(
											"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
											"§7Enabled: " + ChecksMenuLegacy.getCheckMark(checkClass, false),
											"§7Punishable: " + ChecksMenuLegacy.getCheckMark(checkClass, true),
											"",
											"§7Punish-VL: " + ChecksMenuLegacy.cfg.getGuiHighlightColor() + checkClass.getBanVL(),
											"",
											"" + checkClass.getCredits(),
											"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
										)
								);
								stack.setItemMeta(meta);
								stack.setType(Karhu.getInstance().getCheckState().isEnabled(checkClass.getName()) ? Material.ENCHANTED_BOOK : Material.BOOK);
								this.inv.setItem(this.pos, stack);
							}
						}
					}
				);
				++currentSlot;
			}
		}

		gui.open(opener);
		opener.updateInventory();
	}

	public static String getCheckMark(Check cs, boolean ab) {
		if (ab) {
			return Karhu.getInstance().getCheckState().isAutoban(cs.getName()) ? "§a✔" : "§c✗";
		} else {
			return Karhu.getInstance().getCheckState().isEnabled(cs.getName()) ? "§a✔" : "§c✗";
		}
	}

	public static void updateCheckStatus(Check check, boolean autoban, boolean status) {
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
		if (autoban) {
			checkConfiguration.set(category + "." + realTypeName + "." + typeChars + ".autoban", status);
		} else {
			checkConfiguration.set(category + "." + realTypeName + "." + typeChars + ".enabled", status);
		}
	}
}
