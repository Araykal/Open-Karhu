/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.menu;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import java.util.Arrays;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.util.gui.Button;
import me.liwk.karhu.util.gui.Gui;
import me.liwk.karhu.util.gui.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInfoMenu {
	public static void openMenu(Player opener, Player target) {
		int[] blueGlass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
		final ConfigManager cfg = Karhu.getInstance().getConfigManager();
		final Gui gui = new Gui(cfg.getGuiHighlightColor() + target.getName(), 27);
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			for (int pos : blueGlass) {
				gui.addItem(1, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), pos);
			}
		} else {
			for (int pos : blueGlass) {
				gui.addItem(1, new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (short)3), pos);
			}
		}

		final KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(target.getUniqueId());
		if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
			gui.addButton(
				new Button(
					1,
					11,
					ItemUtil.makeSkullItem(
						target.getName(),
						1,
						cfg.getGuiHighlightColor() + target.getName(),
						false,
						Arrays.asList(
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
							cfg.getGuiHighlightColor() + "Ping §7➟ §f" + data.getTransactionPing() + " | " + data.getPing(),
							cfg.getGuiHighlightColor() + "Last lag §7➟ §f" + (data.getTotalTicks() - data.getLastDroppedPackets()) * 50 / 1000 + "s ago",
							"",
							cfg.getGuiHighlightColor() + "Client §7➟ §f" + data.getBrand(),
							cfg.getGuiHighlightColor() + "Version §7➟ §f" + data.getClientVersion().toString().replaceAll("_", ".").replaceAll("v.", ""),
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
								cfg.getGuiHighlightColor() + "Ping §7➟ §f" + data.getTransactionPing() + " | " + data.getPing(),
								cfg.getGuiHighlightColor() + "Last lag §7➟ §f" + (data.getTotalTicks() - data.lastPacketDrop) * 50 / 1000 + "s ago",
								"",
								cfg.getGuiHighlightColor() + "Client §7➟ §f" + data.getBrand(),
								cfg.getGuiHighlightColor() + "Version §7➟ §f" + data.getClientVersion().toString().replaceAll("_", ".").replaceAll("v.", ""),
								"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
							)
						);
						stack.setItemMeta(meta);
						this.inv.setItem(this.pos, stack);
					}
				}
			);
		} else {
			gui.addButton(
				new Button(
					1,
					11,
					ItemUtil.makeSkullItem(
						target.getName(),
						1,
						cfg.getGuiHighlightColor() + target.getName(),
						true,
						Arrays.asList(
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
							cfg.getGuiHighlightColor() + "Ping §7➟ §f" + data.getTransactionPing() + " | " + data.getPing(),
							cfg.getGuiHighlightColor() + "Last lag §7➟ §f" + (data.getTotalTicks() - data.lastPacketDrop) * 50 / 1000 + "s ago",
							"",
							cfg.getGuiHighlightColor() + "Client §7➟ §f" + data.getBrand(),
							cfg.getGuiHighlightColor() + "Version §7➟ §f" + data.getClientVersion().toString().replaceAll("_", ".").replaceAll("v.", ""),
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
								cfg.getGuiHighlightColor() + "Ping §7➟ §f" + data.getTransactionPing() + " | " + data.getPing(),
								cfg.getGuiHighlightColor() + "Last lag §7➟ §f" + (data.getTotalTicks() - data.lastPacketDrop) * 50 / 1000 + "s ago",
								"",
								cfg.getGuiHighlightColor() + "Client §7➟ §f" + data.getBrand(),
								cfg.getGuiHighlightColor() + "Version §7➟ §f" + data.getClientVersion().toString().replaceAll("_", ".").replaceAll("v.", ""),
								"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
							)
						);
						stack.setItemMeta(meta);
						this.inv.setItem(this.pos, stack);
					}
				}
			);
		}

		gui.addButton(
			new Button(
				1,
				12,
				ItemUtil.makeItem(
					Material.NAME_TAG,
					1,
					cfg.getGuiHighlightColor() + "Info",
					Arrays.asList(
						"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤",
						cfg.getGuiHighlightColor()
							+ "Game session §7➟ §f"
							+ (System.currentTimeMillis() - data.getLastJoinTime()) / 1000L / 60L / 60L
							+ "h "
							+ (System.currentTimeMillis() - data.getLastJoinTime()) / 1000L / 60L
							+ "m",
						cfg.getGuiHighlightColor() + "Sensitivity §7➟ §f" + data.getSensitivity() + "%",
						cfg.getGuiHighlightColor() + "Riding §7➟ §f" + data.isRiding() + "|" + data.isRidingUncertain(),
						cfg.getGuiHighlightColor() + "Teleporting §7➟ §f" + data.isPossiblyTeleporting(),
						cfg.getGuiHighlightColor() + "Flying §7➟ §f" + data.isAllowFlying(),
						cfg.getGuiHighlightColor() + "Reach bypass §7➟ §f" + data.isReachBypass(),
						cfg.getGuiHighlightColor() + "Collisions §7➟ §f" + data.isUnderBlock() + " | " + data.isCollidedHorizontally() + " | " + data.isOnGroundServer(),
						"",
						cfg.getGuiHighlightColor() + "CPS/LAST CPS/HIGHEST §7➟ §f" + data.getCps() + "/" + data.getLastCps() + "/" + data.getHighestCps(),
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
							cfg.getGuiHighlightColor()
								+ "Game session §7➟ §f"
								+ (System.currentTimeMillis() - data.getLastJoinTime()) / 1000L / 60L / 60L
								+ "h "
								+ (System.currentTimeMillis() - data.getLastJoinTime()) / 1000L / 60L
								+ "m",
							cfg.getGuiHighlightColor() + "Sensitivity §7➟ §f" + data.getSensitivity() + "%",
							cfg.getGuiHighlightColor() + "Riding §7➟ §f" + data.isRiding() + "|" + data.isRidingUncertain(),
							cfg.getGuiHighlightColor() + "Teleporting §7➟ §f" + data.isPossiblyTeleporting(),
							cfg.getGuiHighlightColor() + "Flying §7➟ §f" + data.isAllowFlying(),
							cfg.getGuiHighlightColor() + "Reach bypass §7➟ §f" + data.isReachBypass(),
							cfg.getGuiHighlightColor() + "Collisions §7➟ §f" + data.isUnderBlock() + " | " + data.isCollidedHorizontally() + " | " + data.isOnGroundServer(),
							"",
							cfg.getGuiHighlightColor() + "CPS/LAST CPS/HIGHEST §7➟ §f" + data.getCps() + "/" + data.getLastCps() + "/" + data.getHighestCps(),
							"§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"
						)
					);
					stack.setItemMeta(meta);
					this.inv.setItem(this.pos, stack);
				}
			}
		);
		gui.addButton(
			new Button(1, 15, ItemUtil.makeItem(Material.BOOK, 1, cfg.getGuiHighlightColor() + "Logs", Arrays.asList("§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤", "§7Show logs", "§7§m⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤⏤"))) {
				@Override
				public void onClick(Player clicker, ClickType clickType) {
					gui.close(clicker);
					clicker.chat("/" + cfg.getName().toLowerCase() + " logs " + target.getName());
				}
			}
		);
		gui.open(opener);
		opener.updateInventory();
	}
}
