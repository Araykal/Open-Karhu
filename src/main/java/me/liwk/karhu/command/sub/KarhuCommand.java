/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.command.sub;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.api.BanX;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.check.api.ViolationX;
import me.liwk.karhu.command.CommandAPI;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.manager.alert.AlertsManager;
import me.liwk.karhu.menu.KarhuMenu;
import me.liwk.karhu.menu.PlayerInfoMenu;
import me.liwk.karhu.util.APICaller;
import me.liwk.karhu.util.benchmark.Benchmark;
import me.liwk.karhu.util.benchmark.KarhuBenchmarker;
import me.liwk.karhu.util.bungee.BungeeAPI;
import me.liwk.karhu.util.framework.Command;
import me.liwk.karhu.util.framework.CommandArgs;
import me.liwk.karhu.util.framework.CommandFramework;
import me.liwk.karhu.util.haste.Hastebin;
import me.liwk.karhu.util.mojang.MojangPing;
import me.liwk.karhu.util.task.Tasker;
import me.liwk.karhu.util.text.TextUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class KarhuCommand extends CommandAPI {
	public KarhuCommand(CommandFramework k) {
		super(k);
	}

	@Command(
		name = "karhu",
		permission = "karhu.staff"
	)
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		CommandSender sender = command.getSender();
		String[] args = command.getArgs();
		String name2 = Karhu.getInstance().getConfigManager().getName();
		if (command.getLabel().equalsIgnoreCase("karhu") || command.getLabel().equalsIgnoreCase(name2)) {
			ConfigManager cfg = Karhu.getInstance().getConfigManager();
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("alerts") && command.getSender() instanceof Player) {
					if (player.hasPermission("karhu.alerts") || AlertsManager.ADMINS.contains(player.getUniqueId())) {
						Karhu.getInstance().getAlertsManager().toggleAlerts(player);
						player.sendMessage(
							Karhu.getInstance().getAlertsManager().hasAlertsToggled(player.getUniqueId())
								? ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("commands.alerts.enabled"))
								: ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("commands.alerts.disabled"))
						);
					}
				} else if (args[0].equalsIgnoreCase("mitigations") && command.getSender() instanceof Player) {
					if (player.hasPermission("karhu.mitigations") || AlertsManager.ADMINS.contains(player.getUniqueId())) {
						Karhu.getInstance().getAlertsManager().toggleMitigation(player);
						player.sendMessage(Karhu.getInstance().getAlertsManager().hasMitigationToggled(player) ? "§aMitigation alerts on" : "§cMitigation alerts off");
					}
				} else if (args[0].equalsIgnoreCase("debug")
					&& (AlertsManager.ADMINS.contains(player.getUniqueId()) || player.getName().equals("LIWK"))
					&& command.getSender() instanceof Player) {
					if (args.length == 1) {
						Karhu.getInstance().getAlertsManager().toggleDebug(player);
						player.sendMessage(Karhu.getInstance().getAlertsManager().hasDebugToggled(player) ? "§4§lDEBUG-mode §aenabled" : "§4§lDEBUG-mode §cdisabled");
					} else if (args[1].equalsIgnoreCase("misc")) {
						Karhu.getInstance().getAlertsManager().toggleMiscDebug(player);
						player.sendMessage(Karhu.getInstance().getAlertsManager().hasMiscDebugToggled(player) ? "§4§lK -> MISC DEBUG-mode §aenabled" : "§4§lK -> MISC DEBUG-mode §cdisabled");
					}
				} else if (!args[0].equalsIgnoreCase("gui") || !(command.getSender() instanceof Player)) {
					if (args[0].equalsIgnoreCase("reload")) {
						if (command.getSender() instanceof Player && !player.hasPermission("karhu.reload")) {
							player.sendMessage(ChatColor.RED + "You don't have enough permissions.");
						} else {
							command.getSender().sendMessage("§7Reloading configs....");
							Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), false);
							Karhu.getInstance().getConfigManager().loadChecks(Karhu.getInstance().getPlug(), false);
							command.getSender().sendMessage("§aReload succesful!");
						}
					} else if (args[0].equalsIgnoreCase("info") && command.getSender() instanceof Player) {
						if (player.hasPermission("karhu.info") || AlertsManager.ADMINS.contains(player.getUniqueId())) {
							if (args.length > 1) {
								Player target = Bukkit.getPlayer(args[1]);
								if (target == null) {
									player.sendMessage("§cSorry, i couldn't find that player");
									return;
								}

								PlayerInfoMenu.openMenu(player, target);
							} else {
								player.sendMessage("§cSorry, i couldn't find that player");
							}
						}
					} else if (args[0].equalsIgnoreCase("status") && command.getSender() instanceof Player) {
						if (player.hasPermission("karhu.status") || AlertsManager.ADMINS.contains(player.getUniqueId())) {
							if (args.length > 1) {
								Player target = Bukkit.getPlayer(args[1]);
								if (target == null) {
									player.sendMessage("§cSorry, i couldn't find that player");
									return;
								}

								KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(target.getUniqueId());
								if (data == null) {
									player.sendMessage("§cSorry, i couldn't find that player");
									return;
								}

								boolean cracked = Karhu.getInstance().getConfigManager().isCrackedServer();
								String highlight = Karhu.getInstance().getConfigManager().getGuiHighlightColor();
								boolean inBanWave = Karhu.getInstance().getWaveManager().getPlayersToBan().contains(cracked ? target.getName() : target.getUniqueId().toString());
								String color = inBanWave ? "§a" : "§c";
								long time = System.currentTimeMillis() - data.getLastJoinTime();
								player.sendMessage("");
								player.sendMessage(highlight + target.getName() + "'s Status:");
								player.sendMessage("§f- " + highlight + "Ban Wave: " + color + inBanWave);
								player.sendMessage("§f- " + highlight + "Ping: §f" + data.getTransactionPing());
								player.sendMessage("§f- " + highlight + "Version: §f" + data.getClientVersion().toString());
								player.sendMessage("§f- " + highlight + "Client: §f" + data.getCleanBrand());
								player.sendMessage("§f- " + highlight + "Highest CPS: §f" + data.getHighestCps());
								player.sendMessage("§f- " + highlight + "Highest Reach: §f" + data.getHighestReach());
								player.sendMessage("§f- " + highlight + "Session: §b" + time / 3600000L + "h " + time / 60000L + "m");
							} else {
								player.sendMessage("§cSorry, i couldn't find that player");
							}
						}
					} else if (args[0].equalsIgnoreCase("users")) {
						Map<String, Set<String>> playerBrands = new HashMap<>();

						for (Player p : Bukkit.getOnlinePlayers()) {
							KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(p.getUniqueId());
							if (data != null) {
								String brand = data.getBrand();
								if (!playerBrands.containsKey(brand)) {
									playerBrands.put(brand, new HashSet<>());
								}

								playerBrands.get(brand).add(p.getName());
							}
						}

						for (Entry<String, Set<String>> entry : playerBrands.entrySet()) {
							command.getSender()
								.sendMessage(
									String.format(
										"§7[" + cfg.getLogsHighlight() + "%s§7] (" + cfg.getLogsHighlight() + "%d§7):" + cfg.getLogsHighlight() + " %s",
										entry.getKey(),
										entry.getValue().size(),
										entry.getValue()
									)
								);
						}

						playerBrands.clear();
					} else if (args[0].equalsIgnoreCase("stats")) {
						Tasker.taskAsync(
							() -> {
								player.sendMessage("");
								player.sendMessage("§9Timings - Last 20 Ticks");
								player.sendMessage(cfg.getGuiHighlightColor() + "TPS: §f" + Karhu.getInstance().getTPS());
								player.sendMessage(cfg.getGuiHighlightColor() + "Players handled: §f" + Karhu.getInstance().getDataManager().getPlayerDataMap().size());
								player.sendMessage("");
								player.sendMessage("§9Other");
								player.sendMessage(cfg.getGuiHighlightColor() + "Online Players: §f" + Bukkit.getOnlinePlayers().size());
								StringBuilder stringBuilder = new StringBuilder();
								stringBuilder.append("\n§9Karhu Benchmark");
	
								for (Benchmark sortedProfile : KarhuBenchmarker.sortedProfiles()) {
									stringBuilder.append("\n")
										.append(cfg.getGuiHighlightColor())
										.append(sortedProfile.profileType())
										.append(": §f")
										.append(String.format("%.4f", sortedProfile.runningAverage()))
										.append("ms")
										.append(" (")
										.append(sortedProfile.results())
										.append(")");
								}
	
								player.sendMessage(stringBuilder.toString());
								player.sendMessage("");
							}
						);
					} else if (args[0].equalsIgnoreCase("manualban")) {
						if (command.getSender() instanceof Player && !player.hasPermission("karhu.manualban")) {
							player.sendMessage("§cNo permissions");
						} else if (args.length > 1) {
							Bukkit.getScheduler()
								.runTaskAsynchronously(
									Karhu.getInstance().getPlug(),
									() -> {
										UUID uuid = MojangPing.getUUID(args[1]);
										KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(uuid);
										if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
											if (data != null && Karhu.getInstance().getConfigManager().isDisallowFlagsAfterPunish()) {
												data.setBanned(true);
											}
		
											if (Karhu.getInstance().getConfigManager().isPunishBroadcast()) {
												Bukkit.broadcastMessage(
													ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("Punishments.message").replaceAll("%player%", args[1]))
												);
											}
										} else if (uuid != null) {
											if (data != null && Karhu.getInstance().getConfigManager().isDisallowFlagsAfterPunish()) {
												data.setBanned(true);
											}
		
											if (Karhu.getInstance().getConfigManager().isPunishBroadcast()) {
												Bukkit.broadcastMessage(
													ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("Punishments.message").replaceAll("%player%", args[1]))
												);
											}
										} else {
											command.getSender().sendMessage("§cSorry, i couldn't find that player");
										}
		
										List<String> banCMD = Karhu.getInstance().getConfigManager().getBanCommand();
										if (!Karhu.isAPIAvailable()) {
											if (!Karhu.getInstance().getConfigManager().isBungeeCommand()) {
												for (String ban : banCMD) {
													Tasker.run(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ban.replaceAll("%player%", args[1])));
												}
											} else {
												for (String var11x : banCMD) {
													BungeeAPI.sendCommand(var11x.replaceAll("%player%", args[1]));
												}
											}
										} else if (APICaller.callBan(player, null, null)) {
											if (!Karhu.getInstance().getConfigManager().isBungeeCommand()) {
												for (String ban : banCMD) {
													Tasker.run(() -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ban.replaceAll("%player%", args[1])));
												}
											} else {
												for (String ban : banCMD) {
													BungeeAPI.sendCommand(ban.replaceAll("%player%", args[1]));
												}
											}
										}
									}
								);
						} else {
							player.sendMessage("§c/karhu manualban <player>");
						}
					} else if (args[0].equalsIgnoreCase("antivpn")) {
						if (command.getSender() instanceof Player && !player.hasPermission("karhu.antivpn")) {
							command.getSender().sendMessage("§cUsage: /" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " antivpn whitelist add/remove PLAYER");
						} else if (args.length > 1) {
							if (args[1].equalsIgnoreCase("whitelist")) {
								if (args.length > 2) {
									String uuid = this.findUUID(args[3]);
									if (args[2].equalsIgnoreCase("add")) {
										if (uuid != null) {
											List<String> allowed = Karhu.getInstance().getConfigManager().getAntiVpnBypass();
											allowed.add(uuid);
											Karhu.getInstance().getConfigManager().getConfig().set("anti-vpn.bypass", allowed);
											Karhu.getInstance().getConfigManager().save();
											Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
											command.getSender().sendMessage("§aSuccesfully §7added §2" + args[3] + " §7to antivpn whitelist");
										} else {
											command.getSender().sendMessage("§cSorry, i couldn't find that player");
										}
									} else if (args[2].equalsIgnoreCase("remove")) {
										if (uuid != null) {
											List<String> allowed = Karhu.getInstance().getConfigManager().getAntiVpnBypass();
											if (allowed.contains(uuid)) {
												allowed.remove(uuid);
												Karhu.getInstance().getConfigManager().getConfig().set("anti-vpn.bypass", allowed);
												Karhu.getInstance().getConfigManager().save();
												Karhu.getInstance().getConfigManager().loadConfig(Karhu.getInstance().getPlug(), true);
												command.getSender().sendMessage("§aSuccesfully §7removed §4" + args[3] + " §7from antivpn whitelist");
											}
										} else {
											command.getSender().sendMessage("§cSorry, i couldn't find that player");
										}
									} else {
										command.getSender().sendMessage("§cUsage: /" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " antivpn whitelist add/remove PLAYER");
									}
								} else {
									command.getSender().sendMessage("§cUsage: /" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " antivpn whitelist add/remove PLAYER");
								}
							} else {
								command.getSender().sendMessage("§cUsage: /" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " antivpn whitelist add/remove PLAYER");
							}
						} else {
							command.getSender().sendMessage("§cUsage: /" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " antivpn whitelist add/remove PLAYER");
						}
					} else if (!args[0].equalsIgnoreCase("sessionlogs") && !args[0].equalsIgnoreCase("slogs")) {
						if (!args[0].toLowerCase().equalsIgnoreCase("compactlogs") && !args[0].toLowerCase().equalsIgnoreCase("clogs")) {
							if (!args[0].equalsIgnoreCase("logs") || !(command.getSender() instanceof Player)) {
								if (args[0].equalsIgnoreCase("top")) {
									if (sender instanceof ConsoleCommandSender || sender.hasPermission("karhu.top")) {
										Bukkit.getScheduler()
											.runTaskAsynchronously(
												Karhu.getInstance().getPlug(),
												() -> {
													TreeMap<UUID, Check> highestVl = new TreeMap<>();
		
													for (Player pl : Bukkit.getOnlinePlayers()) {
														KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(pl.getUniqueId());
														double highest = 0.0;
		
														for (Check checkxx : data.getCheckManager().getChecks()) {
															double vlxx = data.getCheckVl(checkxx);
															if (vlxx > highest) {
																highestVl.put(pl.getUniqueId(), checkxx);
																highest = vlxx;
															}
														}
													}
		
													if (!highestVl.isEmpty()) {
														sender.sendMessage("§7§m--------------------------------------");
														int var14x = 1;
		
														for (Entry<UUID, Check> e : highestVl.entrySet()) {
															Player var17x = Bukkit.getPlayer(e.getKey());
															if (var17x != null) {
																KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(var17x.getUniqueId());
																if (var14x > 10) {
																	break;
																}
		
																if (!Karhu.getInstance().getConfigManager().isCrackedServer()) {
																	sender.sendMessage(
																		cfg.getLogsHighlight()
																			+ "#"
																			+ var14x
																			+ " §7"
																			+ var17x.getDisplayName()
																			+ " - "
																			+ cfg.getLogsHighlight()
																			+ data.getCheckVl(e.getValue())
																			+ " §7- (Most: "
																			+ cfg.getLogsHighlight()
																			+ e.getValue().getName()
																			+ "§7) §7- (Total: "
																			+ cfg.getLogsHighlight()
																			+ Karhu.getStorage().getAllViolations(String.valueOf(e.getKey())).size()
																			+ "§7)"
																	);
																} else {
																	sender.sendMessage(
																		cfg.getLogsHighlight()
																			+ "#"
																			+ var14x
																			+ " §7"
																			+ var17x.getDisplayName()
																			+ " §7- "
																			+ cfg.getLogsHighlight()
																			+ data.getCheckVl(e.getValue())
																			+ " §7- (Most: "
																			+ cfg.getLogsHighlight()
																			+ e.getValue().getName()
																			+ "§7) §7- (Total: "
																			+ cfg.getLogsHighlight()
																			+ Karhu.getStorage().getAllViolations(var17x.getName()).size()
																			+ "§7)"
																	);
																}
		
																++var14x;
															}
														}
		
														sender.sendMessage("§7§m--------------------------------------");
													} else {
														sender.sendMessage("§c" + cfg.getName() + " is very sad to announce this, but your anticheat hasn't flagged anybody :(");
													}
												}
											);
									}
								} else if (args[0].equalsIgnoreCase("pastelogs")) {
									if (!(command.getSender() instanceof Player) || player.hasPermission("karhu.pastelogs")) {
										Bukkit.getScheduler()
											.runTaskAsynchronously(
												Karhu.getInstance().getPlug(),
												() -> {
													if (args.length >= 2) {
														String uuid = this.findUUID(args[1]);
														List<ViolationX> vls = Karhu.storage.getAllViolations(uuid);
														if (vls.isEmpty()) {
															sender.sendMessage("§cPlayer has no logs!");
															return;
														}
		
														StringBuilder end = new StringBuilder(
															"Anticheat logs for player " + args[1] + " pasted with " + Karhu.getInstance().getConfigManager().getName() + " " + Karhu.getInstance().getVersion()
														);
														Bukkit.getScheduler()
															.runTaskAsynchronously(
																Karhu.getInstance().getPlug(),
																() -> {
																	try {
																		for (ViolationX vx : vls) {
																			String logline = TextUtils.formatMillis(System.currentTimeMillis() - vx.time)
																				+ " ago | "
																				+ ChatColor.stripColor(vx.type).replaceAll("\n", " ")
																				+ " ["
																				+ ChatColor.stripColor(vx.data.replaceAll("\n", " ") + "] [" + vx.ping + "ms]/[" + vx.TPS + " TPS] (x" + vx.vl + ")");
																			end.append("\n").append(logline);
																		}
				
																		String var7x = Hastebin.uploadPaste(end.toString());
																		if (var7x == null) {
																			sender.sendMessage("§cCouldn't paste logs, maybe the file is too big?");
																			return;
																		}
				
																		sender.sendMessage("§aPasted logs to: §7" + var7x);
																	} catch (Exception var6x) {
																		sender.sendMessage("§cCouldn't paste logs, maybe the file is too big?");
																		var6x.printStackTrace();
																	}
																}
															);
													} else {
														sender.sendMessage("§c/" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " pastelogs <player> <page>");
													}
												}
											);
									}
								} else if (args[0].equalsIgnoreCase("teleport")) {
									if (sender instanceof Player && args.length > 1) {
										String[] coords = args[1].split(",");
										World world = Bukkit.getWorld(args[2]);
										Location location = null;
										if (world != null) {
											location = new Location(world, Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));
										}

										if (location != null) {
											player.teleport(location);
											player.sendMessage("§aTeleporting to " + location.toVector().toString());
										}
									}
								} else if (args[0].equalsIgnoreCase("recentbans")) {
									if (!(command.getSender() instanceof Player) || player.hasPermission("karhu.recentbans")) {
										Bukkit.getScheduler()
											.runTaskAsynchronously(
												Karhu.getInstance().getPlug(),
												() -> {
													List<BanX> bans = Karhu.storage.getRecentBans();
													if (bans.isEmpty()) {
														player.sendMessage("§cThere are no recent bans!");
													} else {
														player.sendMessage("§7Showing " + cfg.getLogsHighlight() + " 10 §7 recent bans.");
		
														for (BanX vx : bans) {
															String username = this.findName(vx.player);
															String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(vx.time));
															String msgText = "§7* §f"
																+ username
																+ " | "
																+ cfg.getLogsHighlight()
																+ vx.type
																+ " §7(§a"
																+ TextUtils.formatMillis(System.currentTimeMillis() - vx.time)
																+ " ago | "
																+ date
																+ "§7)";
															BaseComponent msg = new TextComponent(
																"§7* §f" + username + " | " + cfg.getLogsHighlight() + vx.type + " §7(§a" + TextUtils.formatMillis(System.currentTimeMillis() - vx.time) + " ago | " + date + "§7)"
															);
															msg.setHoverEvent(
																new HoverEvent(
																	Action.SHOW_TEXT,
																	new ComponentBuilder(
																			ChatColor.translateAlternateColorCodes('&', vx.data.replaceAll("§b", Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight()))
																				+ "\n"
																				+ cfg.getLogsHighlight()
																				+ vx.ping
																				+ "§7ms, "
																				+ cfg.getLogsHighlight()
																				+ vx.TPS
																				+ "TPS"
																		)
																		.create()
																)
															);
															msg.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/karhu logs " + username));
															if (Karhu.getInstance().getConfigManager().getConfig().getBoolean("hoverless-alert")
																|| !player.hasPermission("karhu.hover-debug")
																|| !AlertsManager.ADMINS.contains(player.getUniqueId())) {
																player.sendMessage(msgText);
															}
														}
													}
												}
											);
									}
								} else if (args[0].equalsIgnoreCase("version")) {
									command.getSender().sendMessage("§7Your build: §b" + Karhu.getInstance().getVersion() + " | " + Karhu.getInstance().getBuild());
								} else if (args[0].equalsIgnoreCase("banwave")) {
									String help = ChatColor.translateAlternateColorCodes(
										'&', Karhu.getInstance().getConfigManager().getConfig().getString("commands.banwave", "Check karhu discord customer announcements")
									);
									if (args.length > 1) {
										if (args[1].equalsIgnoreCase("list")) {
											player.sendMessage("List: (" + Karhu.getInstance().getWaveManager().getPlayersToBan().size() + ")");

											for (String uuid : Karhu.getInstance().getWaveManager().getPlayersToBan()) {
												String name = this.findName(uuid);
												player.sendMessage(name);
											}
										} else if (args[1].equalsIgnoreCase("run")) {
											if (!Karhu.getInstance().getWaveManager().isRunningBanwave() && Karhu.getInstance().getWaveManager().getPlayersToBan().size() > 0) {
												player.sendMessage("Starting banwave.");
												Karhu.getInstance().getWaveManager().startBanwave();
											} else {
												player.sendMessage("Banwave is already running or there is no players init!");
											}
										} else if (args[1].equalsIgnoreCase("add")) {
											if (args[2] != null) {
												String uuid = Karhu.getInstance().getConfigManager().isCrackedServer() ? args[2] : this.findUUID(args[2]);
												Karhu.getInstance().getWaveManager().addToWave(uuid, "Manual");
												player.sendMessage("Added " + args[2] + " to banwave");
											}
										} else if (args[1].equalsIgnoreCase("remove")) {
											if (args[2] != null) {
												String uuid = Karhu.getInstance().getConfigManager().isCrackedServer() ? args[2] : this.findUUID(args[2]);
												Karhu.getInstance().getWaveManager().removeFromWave(uuid);
												player.sendMessage("Removed " + args[2] + " from banwave");
											}
										} else if (args[1].equalsIgnoreCase(help)) {
											player.sendMessage(help);
										} else {
											player.sendMessage(help);
										}
									} else {
										player.sendMessage(help);
									}
								} else if (args[0].equalsIgnoreCase("setbacktracker")) {
									Karhu.getInstance().getAlertsManager().toggleSetback(player);
									player.sendMessage(Karhu.getInstance().getAlertsManager().hasSetbackToggled(player) ? "§aSetback tracker on" : "§cSetback tracker off");
								} else if (args[0].equalsIgnoreCase("worldtrack")) {
									Map<World, Long2ObjectMap<Chunk>> chunksWorlds = Karhu.getInstance().getChunkManager().getLoadedChunks();

									for (World world : chunksWorlds.keySet()) {
										sender.sendMessage("§bWorld name: §f" + world.getName() + " §bchunk amount: §f" + Karhu.getInstance().getChunkManager().getCacheSize(world));
									}
								} else {
									command.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("commands.help")));
								}
							} else if (!(command.getSender() instanceof Player) || player.hasPermission("karhu.logs")) {
								Bukkit.getScheduler()
									.runTaskAsynchronously(
										Karhu.getInstance().getPlug(),
										() -> {
											if (args.length >= 2) {
												String uuid = this.findUUID(args[1]);
												int page = args.length == 3 ? Integer.parseInt(args[2]) : 0;
												List<ViolationX> vls = Karhu.storage.getViolations(uuid, null, page, 10, -1L, -1L);
												if (vls.isEmpty()) {
													player.sendMessage("§cPlayer has no logs!");
													if (!Karhu.getInstance().getConfigManager().isCrackedServer()) {
														return;
													}
		
													uuid = Bukkit.getOfflinePlayer(args[1]).getName();
													vls = Karhu.storage.getViolations(uuid, null, page, 10, -1L, -1L);
													if (vls.isEmpty()) {
														player.sendMessage("§cPlayer has no logs!");
														return;
													}
												}
		
												int maxPages = Karhu.storage.getAllViolations(uuid).size() / 10;
												player.sendMessage("§7Showing logs of " + cfg.getLogsHighlight() + args[1] + " §7(§a" + page + "§7/§2" + maxPages + "§7)");
		
												for (ViolationX vx : vls) {
													if (!vx.data.contains("PUNISHED")) {
														String msgText = "§7* "
															+ cfg.getLogsHighlight()
															+ vx.type
															+ " §7VL: "
															+ cfg.getLogsHighlight()
															+ TextUtils.format((double)vx.vl, 1)
															+ " §7(§a"
															+ TextUtils.formatMillis(System.currentTimeMillis() - vx.time)
															+ " ago§7)";
														BaseComponent var14x = new TextComponent(
															"§7* "
																+ cfg.getLogsHighlight()
																+ vx.type
																+ " §7VL: "
																+ cfg.getLogsHighlight()
																+ TextUtils.format((double)vx.vl, 1)
																+ " §7(§a"
																+ TextUtils.formatMillis(System.currentTimeMillis() - vx.time)
																+ " ago§7)"
														);
														var14x.setHoverEvent(
															new HoverEvent(
																Action.SHOW_TEXT,
																new ComponentBuilder(
																		ChatColor.translateAlternateColorCodes('&', vx.data.replaceAll("§b", Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight()))
																			+ "\n"
																			+ cfg.getLogsHighlight()
																			+ vx.ping
																			+ "§7ms, "
																			+ cfg.getLogsHighlight()
																			+ vx.TPS
																			+ "TPS"
																	)
																	.create()
															)
														);
														var14x.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/karhu teleport " + vx.location + " " + vx.world));
														if (Karhu.getInstance().getConfigManager().getConfig().getBoolean("hoverless-alert")
															|| !player.hasPermission("karhu.hover-debug") && !AlertsManager.ADMINS.contains(player.getUniqueId())) {
															player.sendMessage(msgText);
														}
													} else {
														String msgText = "§7* "
															+ cfg.getLogsBan()
															+ vx.type
															+ " §7VL: "
															+ cfg.getLogsBan()
															+ TextUtils.format((double)vx.vl, 1)
															+ " §7("
															+ cfg.getLogsBan()
															+ TextUtils.formatMillis(System.currentTimeMillis() - vx.time)
															+ " ago§7)";
														BaseComponent msg = new TextComponent(
															"§7* "
																+ cfg.getLogsBan()
																+ vx.type
																+ " §7VL: "
																+ cfg.getLogsBan()
																+ TextUtils.format((double)vx.vl, 1)
																+ " §7("
																+ cfg.getLogsBan()
																+ TextUtils.formatMillis(System.currentTimeMillis() - vx.time)
																+ " ago§7)"
														);
														msg.setHoverEvent(
															new HoverEvent(
																Action.SHOW_TEXT,
																new ComponentBuilder(
																		ChatColor.translateAlternateColorCodes('&', vx.data.replaceAll("§b", Karhu.getInstance().getConfigManager().getAlertHoverMessageHighlight()))
																			+ "\n"
																			+ cfg.getLogsHighlight()
																			+ vx.ping
																			+ "§7ms, "
																			+ cfg.getLogsHighlight()
																			+ vx.TPS
																			+ "TPS"
																	)
																	.create()
															)
														);
														msg.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/karhu teleport " + vx.location + " " + vx.world));
														if (Karhu.getInstance().getConfigManager().getConfig().getBoolean("hoverless-alert")
															|| !player.hasPermission("karhu.hover-debug") && !AlertsManager.ADMINS.contains(player.getUniqueId())) {
															player.sendMessage(msgText);
														}
													}
												}
											} else {
												command.getSender().sendMessage("§c/" + Karhu.getInstance().getConfigManager().getName().toLowerCase() + " logs <player> <page>");
											}
										}
									);
							}
						} else {
							if (args.length == 1) {
								player.sendMessage(ChatColor.RED + "Use: /karhu compactlogs <player>");
								return;
							}

							Bukkit.getScheduler().runTaskAsynchronously(Karhu.getInstance().getPlug(), () -> {
								double vx = 0.0;
								String uuid = this.findUUID(args[1]);
								List<ViolationX> vls = Karhu.storage.getAllViolations(uuid);
								TreeMap<String, Integer> flagMap = new TreeMap<>();

								for (ViolationX violationX : vls) {
									flagMap.computeIfAbsent(violationX.type, k -> flagMap.put(k, 0));
									flagMap.put(violationX.type, flagMap.get(violationX.type) + 1);
									++vx;
								}

								player.sendMessage("§7§m--------------------------------------");
								player.sendMessage("§7Violations of " + cfg.getLogsHighlight() + args[1] + " (" + flagMap.size() + ")§f:");
								player.sendMessage(" ");

								for (Entry<String, Integer> e : flagMap.entrySet()) {
									player.sendMessage(" §7* " + cfg.getLogsHighlight() + e.getKey() + " §7- x" + cfg.getLogsHighlight() + e.getValue());
								}

								if (vx == 0.0) {
									player.sendMessage("§cPlayer has no logs!");
									player.sendMessage(" ");
								}

								player.sendMessage("§7§m--------------------------------------");
							});
						}
					} else {
						if (args.length == 1) {
							player.sendMessage(ChatColor.RED + "Use: /karhu sessionlogs <player>");
							return;
						}

						double v = 0.0;
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							KarhuPlayer karhuPlayer = Karhu.getInstance().getDataManager().getPlayerData(target.getUniqueId());
							player.sendMessage("§7§m--------------------------------------");
							player.sendMessage("§7Violations of " + cfg.getLogsHighlight() + target.getName() + "§f:");
							player.sendMessage(" ");

							for (Check<?> check : karhuPlayer.getCheckManager().getChecks()) {
								double vl = karhuPlayer.getCheckVl(check);
								if (vl > 0.0) {
									player.sendMessage(
										" §7* " + cfg.getLogsHighlight() + (check.isExperimental() ? check.getName() + cfg.getExpIcon() : check.getName()) + " §7- x" + cfg.getLogsHighlight() + vl
									);
									v = vl;
								}
							}

							if (v == 0.0) {
								player.sendMessage("§cPlayer has no logs!");
								player.sendMessage(" ");
							}

							player.sendMessage("§7§m--------------------------------------");
						} else {
							player.sendMessage(ChatColor.RED + "Couldn't find that player.");
						}
					}
				} else if (player.hasPermission("karhu.gui")) {
					KarhuMenu.openMenu(player);
				} else {
					player.sendMessage(ChatColor.RED + "You don't have enough permissions.");
				}
			} else {
				command.getSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("commands.help")));
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

	private String findName(String arg) {
		if (!arg.contains("-")) {
			return arg;
		} else {
			Player target = Bukkit.getPlayer(UUID.fromString(arg));
			return target != null ? target.getName() : Bukkit.getOfflinePlayer(UUID.fromString(arg)).getName();
		}
	}
}
