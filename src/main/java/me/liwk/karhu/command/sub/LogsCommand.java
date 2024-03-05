/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.command.sub;

import java.util.List;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.api.ViolationX;
import me.liwk.karhu.command.CommandAPI;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.manager.alert.AlertsManager;
import me.liwk.karhu.util.framework.Command;
import me.liwk.karhu.util.framework.CommandArgs;
import me.liwk.karhu.util.framework.CommandFramework;
import me.liwk.karhu.util.text.TextUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LogsCommand extends CommandAPI {
	public LogsCommand(CommandFramework k) {
		super(k);
	}

	@Command(
		name = "logs",
		permission = "karhu.logs"
	)
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();
		if (command.getSender() instanceof Player) {
			ConfigManager cfg = Karhu.getInstance().getConfigManager();
			if (command.getLabel().equalsIgnoreCase("logs") && (!(command.getSender() instanceof Player) || player.hasPermission("karhu.logs"))) {
				Bukkit.getScheduler()
					.runTaskAsynchronously(
						Karhu.getInstance().getPlug(),
						() -> {
							if (args.length >= 1) {
								String uuid = this.findUUID(args[0]);
								int page = args.length == 2 ? Integer.parseInt(args[1]) : 0;
								List<ViolationX> vls = Karhu.storage.getViolations(uuid, null, page, 10, -1L, -1L);
								if (vls.isEmpty()) {
									player.sendMessage("§cPlayer has no logs!");
									if (!Karhu.getInstance().getConfigManager().isCrackedServer()) {
										return;
									}
		
									uuid = Bukkit.getOfflinePlayer(args[0]).getName();
									vls = Karhu.storage.getViolations(uuid, null, page, 10, -1L, -1L);
									if (vls.isEmpty()) {
										player.sendMessage("§cPlayer has no logs!");
										return;
									}
								}
		
								int maxPages = Karhu.storage.getAllViolations(uuid).size() / 10;
								player.sendMessage("§7Showing logs of " + cfg.getLogsHighlight() + args[0] + " §7(§a" + page + "§7/§2" + maxPages + "§7)");
		
								for (ViolationX v : vls) {
									if (!v.data.contains("PUNISHED")) {
										String textMsg = "§7* "
											+ cfg.getLogsHighlight()
											+ v.type
											+ " §7VL: "
											+ cfg.getLogsHighlight()
											+ TextUtils.format((double)v.vl, 1)
											+ " §7(§a"
											+ TextUtils.formatMillis(System.currentTimeMillis() - v.time)
											+ " ago§7)";
										BaseComponent msg = new TextComponent(
											"§7* "
												+ cfg.getLogsHighlight()
												+ v.type
												+ " §7VL: "
												+ cfg.getLogsHighlight()
												+ TextUtils.format((double)v.vl, 1)
												+ " §7(§a"
												+ TextUtils.formatMillis(System.currentTimeMillis() - v.time)
												+ " ago§7)"
										);
										msg.setHoverEvent(
											new HoverEvent(
												Action.SHOW_TEXT,
												new ComponentBuilder(
														ChatColor.translateAlternateColorCodes('&', v.data.replaceAll("§b", Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight()))
															+ "\n"
															+ cfg.getLogsHighlight()
															+ v.ping
															+ "§7ms, "
															+ cfg.getLogsHighlight()
															+ v.TPS
															+ "TPS"
													)
													.create()
											)
										);
										msg.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/karhu teleport " + v.location + " " + v.world));
										if (!Karhu.getInstance().getConfigManager().getConfig().getBoolean("hoverless-alert")
											&& (player.hasPermission("karhu.hover-debug") || AlertsManager.ADMINS.contains(player.getUniqueId()))) {
											if (Karhu.getInstance().getConfigManager().getConfig().getBoolean("spigot-api-alert")) {
												player.spigot().sendMessage(msg);
											} else {
												player.spigot().sendMessage(msg);
											}
										} else {
											player.sendMessage(textMsg);
										}
									} else {
										String textMsg = "§7* "
											+ cfg.getLogsBan()
											+ v.type
											+ " §7VL: "
											+ cfg.getLogsBan()
											+ TextUtils.format((double)v.vl, 1)
											+ " §7("
											+ cfg.getLogsBan()
											+ TextUtils.formatMillis(System.currentTimeMillis() - v.time)
											+ " ago§7)";
										BaseComponent msg = new TextComponent(
											"§7* "
												+ cfg.getLogsBan()
												+ v.type
												+ " §7VL: "
												+ cfg.getLogsBan()
												+ TextUtils.format((double)v.vl, 1)
												+ " §7("
												+ cfg.getLogsBan()
												+ TextUtils.formatMillis(System.currentTimeMillis() - v.time)
												+ " ago§7)"
										);
										msg.setHoverEvent(
											new HoverEvent(
												Action.SHOW_TEXT,
												new ComponentBuilder(
														ChatColor.translateAlternateColorCodes('&', v.data.replaceAll("§b", Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight()))
															+ "\n"
															+ cfg.getLogsHighlight()
															+ v.ping
															+ "§7ms, "
															+ cfg.getLogsHighlight()
															+ v.TPS
															+ "TPS"
													)
													.create()
											)
										);
										msg.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/karhu teleport " + v.location + " " + v.world));
										if (!Karhu.getInstance().getConfigManager().getConfig().getBoolean("hoverless-alert")
											&& (player.hasPermission("karhu.hover-debug") || AlertsManager.ADMINS.contains(player.getUniqueId()))) {
											if (Karhu.getInstance().getConfigManager().getConfig().getBoolean("spigot-api-alert")) {
												player.spigot().sendMessage(msg);
											} else {
												player.spigot().sendMessage(msg);
											}
										} else {
											player.sendMessage(textMsg);
										}
									}
								}
							} else {
								command.getSender().sendMessage("§c/" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " logs <player> <page>");
							}
						}
					);
			}
		}
	}

	private String findUUID(String arg) {
		if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
			Player target = Bukkit.getPlayer(arg);
			return target != null ? arg : arg;
		} else {
			Player target = Bukkit.getPlayer(arg);
			return target != null ? target.getUniqueId().toString() : Bukkit.getOfflinePlayer(arg).getUniqueId().toString();
		}
	}
}
