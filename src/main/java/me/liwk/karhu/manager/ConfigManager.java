/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.liwk.karhu.Karhu;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {
	private FileConfiguration config = null;
	private File configFile = null;
	private FileConfiguration checks = null;
	private File checkFile = null;
	private String prefix = null;
	private String expIcon;
	private String noPunishIcon;
	private String miscPrefix = null;
	private String alertMessage = null;
	private String clientCheckMessage;
	private String alertHoverMessage = null;
	private String alertClickCommand = null;
	private String alertHoverMessageHighlight = null;
	private String guiHighlightColor = null;
	private String punishMsg;
	private List<String> banCommand = null;
	private List<String> punishmentsBan;
	private List<String> punishmentsKick;
	private long commandDelay;
	private boolean autoban;
	private boolean punishBroadcast;
	private boolean disallowFlagsAfterPunish;
	private boolean spigotApiAlert;
	private boolean hoverlessAlert;
	private boolean discordAlert;
	private boolean sendAlerts;
	private boolean sendBans;
	private boolean bungeeAlert;
	private boolean bypass;
	private boolean pullback;
	private boolean crackedServer;
	private boolean bungeeCommand;
	private String defaultVersion;
	private String license;
	private String pullbackMode;
	private int bungeePostRate;
	private long alertDelay = 0L;
	private String name;
	private String serverName;
	private double maxCps = 25.0;
	private boolean reachTransaction = true;
	private boolean reachCancel;
	private boolean hitboxCancel;
	private boolean triplehitBlock;
	private boolean disableHitboxCheck;
	private boolean checkHitbox;
	private boolean reachSafe;
	private long timerACapLenght;
	private long exemptTicksJoin;
	private String banwavePunish;
	private String banwaveCaught;
	private String banwaveComplete;
	private String geyserPrefix;
	private boolean brComplete;
	private boolean brCaught;
	private boolean geyserSupport;
	private boolean geyserPrefixCheck;
	private boolean anticrash;
	private boolean largeMove;
	private boolean moveSpam;
	private boolean armSpam;
	private boolean placeSpam;
	private boolean payloadSpam;
	private boolean slotSpam;
	private boolean windowSpam;
	private String anticrashKickMsg;
	private String antiCrashMessage;
	private boolean pingKick;
	private int pingKickMaxPing;
	private int pingKickTicks;
	private String pingKickMsg;
	boolean ghostBlock;
	boolean gbLagback;
	boolean gbUpdate;
	boolean liquidDetect;
	private boolean nethandler = false;
	private boolean delay = false;
	private boolean spoof = false;
	private String transactionOrder;
	private String cancelTransactions;
	private String cancelKeepalives;
	private String ownTransactions;
	private String ownKeepalives;
	private String cancelOwnKick;
	private String orderKick;
	private String logsHighlight;
	private String logsBan;
	private String antivpnKickMsg;
	private boolean logSync;
	private boolean vehicleHandler;
	private boolean antivpn;
	private boolean proxycheck;
	private boolean maliciouscheck;
	private boolean clientCheck;
	private List<String> antiVpnBypass;
	private double reachToFlag;
	private double reachBuffer;
	private double reachDecayPerMiss;
	private int reachBackTrack = 3;
	private boolean fixEat;
	private boolean flagNoWeb;
	private boolean flagNoSlow;
	private double speedAMult;
	private double speedBMult;
	private double speedCMult;
	private long maxTickLenght;
	private String lagWarnMsg;
	private String lagWarnDisplay;
	private String noPermission = "&cNo permissions.";
	private boolean injectEarly;
	private boolean injectAsync;
	private boolean ejectAsync;
	private boolean kickUninjected;
	private String uninjectedKick;
	private boolean fixAsyncKb;
	private boolean firstTime = true;

	public ConfigManager(JavaPlugin karhu) {
		this.loadConfig(karhu, false);
		this.loadChecks(karhu, false);
	}

	public void loadConfig(Plugin karhu, boolean silent) {
		this.configFile = new File(karhu.getDataFolder(), "config.yml");
		if (!this.configFile.exists()) {
			karhu.saveResource("config.yml", false);
			if (!silent) {
				Karhu.getInstance().printCool("&b> &fGenerating file config.yml");
			}
		} else if (!silent) {
			Karhu.getInstance().printCool("&b> &fLoading file config.yml");
		}

		this.config = YamlConfiguration.loadConfiguration(this.configFile);
		this.license = this.config.getString("license-key");
		if (!this.config.isSet("version-to-download")) {
			this.config.set("version-to-download", "autoupdate");
		}

		if (!this.config.isSet("Prefix")) {
			this.config.set("Prefix", "&7[&b&l❀&7] ");
		}

		if (!this.config.isSet("MiscPrefix")) {
			this.config.set("MiscPrefix", "&7[&6⚠&7] ");
		}

		if (!this.config.isSet("AlertsMessage")) {
			this.config.set("AlertsMessage", "&f%player% &7failed &b%check% &7[x&b%vl%&7]");
		}

		if (!this.config.isSet("ClientCheckMessage")) {
			this.config.set("ClientCheckMessage", "&f%player% &7joined using &7[&e%brand%&7]");
		}

		if (!this.config.isSet("experimental-alert-symbol")) {
			this.config.set("experimental-alert-symbol", "&aΔ");
		}

		if (!this.config.isSet("noautoban-alert-symbol")) {
			this.config.set("noautoban-alert-symbol", "&c≠");
		}

		if (!this.config.isSet("AntiCrashMessage")) {
			this.config.set("AntiCrashMessage", "&e%player% &fwas kicked for &esuspicious activity &7(&6%debug%&7)");
		}

		if (!this.config.isSet("AlertsHoverableMessage")) {
			this.config.set("AlertsHoverableMessage", "&7%info% (Ping: %ping% TPS: %tps%) &b(Click to teleport)");
		}

		if (!this.config.isSet("reset-violations-on-leave")) {
			this.config.set("reset-violations-on-leave", true);
		}

		if (!this.config.isSet("PunishCommand")) {
			List<String> l = new ArrayList<>();
			l.add("kick %player% Hacked client");
			this.config.set("PunishCommand", l);
		}

		if (this.config.getStringList("PunishCommand").isEmpty()) {
			List<String> l = new ArrayList<>();
			l.add(this.config.get("PunishCommand").toString());
			this.config.set("PunishCommand", l);
		}

		if (!this.config.isSet("Punishments.banCommand")) {
			List<String> l = new ArrayList<>();
			l.add("ban %player% Hacked client");
			this.config.set("Punishments.banCommand", l);
		}

		if (this.config.getStringList("Punishments.banCommand").isEmpty()) {
			List<String> l = new ArrayList<>();
			l.add(this.config.get("PunishCommand").toString());
			this.config.set("Punishments.banCommand", l);
		}

		if (!this.config.isSet("Punishments.kickCommand")) {
			List<String> l = new ArrayList<>();
			l.add("kick %player% Hacked client");
			this.config.set("Punishments.kickCommand", l);
		}

		if (this.config.getStringList("Punishments.kickCommand").isEmpty()) {
			List<String> l = new ArrayList<>();
			l.add(this.config.get("Punishments.kickCommand").toString());
			this.config.set("Punishments.kickCommand", l);
		}

		if (!this.config.isSet("autoban")) {
			this.config.set("autoban", true);
		}

		if (!this.config.isSet("disallow-flags-after-punishment")) {
			this.config.set("disallow-flags-after-punishment", true);
		}

		if (!this.config.isSet("alert-delay")) {
			this.config.set("alert-delay", 0L);
		}

		if (!this.config.isSet("spigot-api-alert")) {
			this.config.set("spigot-api-alert", true);
		}

		if (!this.config.isSet("hoverless-alert")) {
			this.config.set("hoverless-alert", false);
		}

		if (!this.config.isSet("cracked-server")) {
			this.config.set("cracked-server", false);
		}

		if (!this.config.isSet("anticrash.enabled")) {
			this.config.set("anticrash.enabled", true);
		}

		if (!this.config.isSet("anticrash.move-spam")) {
			this.config.set("anticrash.move-spam", true);
		}

		if (!this.config.isSet("anticrash.place-spam")) {
			this.config.set("anticrash.place-spam", true);
		}

		if (!this.config.isSet("anticrash.large-move")) {
			this.config.set("anticrash.large-move", true);
		}

		if (!this.config.isSet("anticrash.window-spam")) {
			this.config.set("anticrash.window-spam", true);
		}

		if (!this.config.isSet("anticrash.payload-spam")) {
			this.config.set("anticrash.payload-spam", true);
		}

		if (!this.config.isSet("anticrash.arm-spam")) {
			this.config.set("anticrash.window-spam", true);
		}

		if (!this.config.isSet("anticrash.slot-spam")) {
			this.config.set("anticrash.payload-spam", true);
		}

		if (!this.config.isSet("anticrash.kick-message")) {
			this.config.set("anticrash.kick-message", "java.net.IOException Connection timed out: no further information");
		}

		if (!this.config.isSet("nethandler.enabled")) {
			this.config.set("nethandler.enabled", false);
		}

		if (!this.config.isSet("nethandler.delay")) {
			this.config.set("nethandler.delay", false);
		}

		if (!this.config.isSet("nethandler.spoof")) {
			this.config.set("nethandler.spoof", false);
		}

		if (!this.config.isSet("nethandler.transaction-order")) {
			this.config.set("nethandler.transaction-order", "&c%player% &7ignored order of transactions &7first was &a%first% &7sent §c%sent%");
		}

		if (!this.config.isSet("nethandler.cancel-keepalives-alert")) {
			this.config.set("nethandler.cancel-keepalives-alert", "&c%player% &7cancelled keepalive packets with total of &a%invalid% / %total%");
		}

		if (!this.config.isSet("nethandler.cancel-transactions-alert")) {
			this.config.set("nethandler.cancel-transactions-alert", "&c%player% &7cancelled transaction packets with total of &a%invalid% / %total%");
		}

		if (!this.config.isSet("nethandler.own-keepalives-alert")) {
			this.config.set("nethandler.own-keepalives-alert", "&c%player% &7sent own keepalives &7total of &a%invalid% / %total%");
		}

		if (!this.config.isSet("nethandler.own-transactions-alert")) {
			this.config.set("nethandler.own-transactions-alert", "&c%player% &7sent own transactions &7total of &a%invalid% / %total%");
		}

		if (!this.config.isSet("nethandler.cancel-and-own-kick-message")) {
			this.config.set("nethandler.cancel-and-own-kick-message", "java.net.IOException Connection timed out: no further information");
		}

		if (!this.config.isSet("nethandler.wrong-order-kick-message")) {
			this.config.set("nethandler.wrong-order-kick-message", "Timed out (%first% != %received%)");
		}

		if (!this.config.isSet("pullback.enabled")) {
			this.config.set("pullback.enabled", true);
		}

		if (!this.config.isSet("pullback.type")) {
			this.config.set("pullback.type", "generic");
		}

		if (!this.config.isSet("default-version")) {
			this.config.set("default-version", "1_8");
		}

		if (!this.config.isSet("bypass-permission")) {
			this.config.set("bypass-permission", "true");
		}

		if (!this.config.isSet("anticheat-name")) {
			this.config.set("anticheat-name", "Karhu");
		}

		if (!this.config.isSet("server-name")) {
			this.config.set("server-name", "Karhu");
		}

		if (!this.config.isSet("geyser.stop-injecting-bedrock-players")) {
			this.config.set("geyser.stop-injecting-bedrock-players", true);
		}

		if (!this.config.isSet("geyser.check-for-name-prefix")) {
			this.config.set("geyser.check-for-name-prefix", false);
		}

		if (!this.config.isSet("geyser.name-prefix")) {
			this.config.set("geyser.name-prefix", "*");
		}

		if (!this.config.isSet("packetevents.injectAsync")) {
			this.config.set("packetevents.injectAsync", true);
		}

		if (!this.config.isSet("packetevents.ejectAsync")) {
			this.config.set("packetevents.ejectAsync", true);
		}

		if (!this.config.isSet("packetevents.injectEarly")) {
			this.config.set("packetevents.injectEarly", true);
		}

		if (!this.config.isSet("packetevents.kickUninjected")) {
			this.config.set("packetevents.kickUninjected", true);
		}

		if (!this.config.isSet("packetevents.uninjected-kick-message")) {
			this.config.set("packetevents.uninjected-kick-message", "&cWe've failed to load your data, please reconnect!");
		}

		if (!this.config.isSet("discord.enabled")) {
			this.config.set("discord.enabled", true);
		}

		if (!this.config.isSet("ghostblock-support.enabled")) {
			this.config.set("ghostblock-support.enabled", true);
		}

		if (!this.config.isSet("ghostblock-support.lagback-on-walk")) {
			this.config.set("ghostblock-support.lagback-on-walk", false);
		}

		if (!this.config.isSet("ghostblock-support.update-on-walk")) {
			this.config.set("ghostblock-support.update-on-walk", true);
		}

		if (!this.config.isSet("ghostblock-support.liquid-dector")) {
			this.config.set("ghostblock-support.liquid-dector", false);
		}

		if (!this.config.isSet("vehicle-handler.unmount")) {
			this.config.set("vehicle-handler.unmount", false);
		}

		if (!this.config.isSet("high-ping-kick.max-ping")) {
			this.config.set("high-ping-kick.max-ping", 1000);
		}

		if (!this.config.isSet("high-ping-kick.ping-over-max-ticks-before-kick")) {
			this.config.set("high-ping-kick.ping-over-max-ticks-before-kick", 250);
		}

		if (!this.config.isSet("high-ping-kick.enabled")) {
			this.config.set("high-ping-kick.enabled", false);
		}

		if (!this.config.isSet("high-ping-kick.kick-message")) {
			this.config.set("high-ping-kick.kick-message", "Your ping constantly too high (over 1000ms), do something");
		}

		if (!this.config.isSet("GuiHighlightColor")) {
			this.config.set("GuiHighlightColor", "&l&b");
		}

		if (!this.config.isSet("commands.logs.ban-color")) {
			this.config.set("commands.logs.ban-color", "&c");
		}

		if (!this.config.isSet("commands.logs.ban-color")) {
			this.config.set("commands.logs.ban-color", "&c");
		}

		if (!this.config.isSet("commands.logs.highlight-color")) {
			this.config.set("commands.logs.highlight-color", "&b");
		}

		if (!this.config.isSet("commands.no-permission")) {
			this.config.set("commands.no-permission", "&cYou don''t have required permissions!");
		}

		if (!this.config.isSet("anti-vpn.enabled")) {
			this.config.set("anti-vpn.enabled", true);
		}

		if (!this.config.isSet("anti-vpn.proxy-check")) {
			this.config.set("anti-vpn.proxy-check", true);
		}

		if (!this.config.isSet("anti-vpn.malicious-check")) {
			this.config.set("anti-vpn.malicious-check", true);
		}

		if (!this.config.isSet("anti-vpn.kick-message")) {
			this.config.set("anti-vpn.kick-message", "&cUsage of VPN is prohibited!");
		}

		if (!this.config.isSet("anti-vpn.bypass")) {
			List<String> l = new ArrayList<>();
			l.add("UUID1");
			this.config.set("anti-vpn.bypass", l);
		}

		if (!this.config.isSet("client-check")) {
			this.config.set("client-check", true);
		}

		if (!this.config.isSet("Punishments.command-delay-seconds")) {
			this.config.set("Punishments.command-delay-seconds", 0L);
		}

		if (!this.config.isSet("bungee.alerts")) {
			this.config.set("bungee.alerts", false);
		}

		if (!this.config.isSet("discord.send-alerts")) {
			this.config.set("discord.send-alerts", true);
		}

		if (!this.config.isSet("discord.send-bans")) {
			this.config.set("discord.send-bans", true);
		}

		if (!this.config.isSet("bungee.alert-post-vl-rate")) {
			this.config.set("bungee.alert-post-vl-rate", 10);
		}

		if (!this.config.isSet("server-lag-protection.max-tick-length")) {
			this.config.set("server-lag-protection.max-tick-length", 120L);
		}

		if (!this.config.isSet("server-lag-protection.warning-message")) {
			this.config.set("server-lag-protection.warning-message", "%prefix% &c%player% &fwould've flagged, but server lagged within &c1 second&f.");
		}

		if (!this.config.isSet("server-lag-protection.warning-display-type")) {
			this.config.set("server-lag-protection.warning-display-type", "CONSOLE");
		}

		if (!this.config.isSet("async-kb-fix")) {
			this.config.set("async-kb-fix", false);
		}

		if (!this.config.isSet("join-exempt-ticks")) {
			this.config.set("join-exempt-ticks", 100);
		}

		if (!this.config.isSet("banwaves.punish")) {
			this.config.set("banwaves.punish", "configurethis %player%");
		}

		if (!this.config.isSet("banwaves.messages.caught")) {
			this.config.set("banwaves.messages.caught", "&b%player% &3has been caught in the &bBan Wave!");
		}

		if (!this.config.isSet("banwaves.messages.complete")) {
			this.config.set("banwaves.messages.complete", "&bKarhu &3has finished the banwave. A total of &b%bans% players &3were banned.");
		}

		if (!this.config.isSet("banwaves.broadcast-caught")) {
			this.config.set("banwaves.broadcast-caught", true);
		}

		if (!this.config.isSet("banwaves.broadcast-complete")) {
			this.config.set("banwaves.broadcast-complete", true);
		}

		if (!this.config.isSet("bungee.execute-ban-command-in-bungee")) {
			this.config.set("bungee.execute-ban-command-in-bungee", false);
		}

		if (!this.config.isSet("libs.mongo")) {
			this.config.set("libs.mongo", true);
		}

		if (!this.config.isSet("libs.classindex")) {
			this.config.set("libs.classindex", true);
		}

		if (!this.config.isSet("libs.fastutil")) {
			this.config.set("libs.fastutil", true);
		}

		if (!this.config.isSet("libs.fastutil-core")) {
			this.config.set("libs.fastutil-core", true);
		}

		if (!this.config.isSet("libs.sqlite")) {
			this.config.set("libs.sqlite", true);
		}

		if (!this.config.isSet("libs.gson")) {
			this.config.set("libs.gson", true);
		}

		if (!this.config.isSet("libs.apache-math3")) {
			this.config.set("libs.apache-math3", true);
		}

		if (!this.config.isSet("karhu-whitelist-msg")) {
			this.config.set("karhu-whitelist-msg", "This server is whitelisted!");
		}

		this.banwavePunish = this.config.getString("banwaves.punish", "configurethis %player%");
		this.banwaveCaught = ChatColor.translateAlternateColorCodes('&', this.config.getString("banwaves.messages.caught", "&b%player% &3has been caught in the &bBan Wave!"));
		this.banwaveComplete = ChatColor.translateAlternateColorCodes(
			'&', this.config.getString("banwaves.messages.complete", "&bKarhu &3has finished the banwave. A total of &b%bans% players &3were banned.")
		);
		this.brComplete = this.config.getBoolean("banwaves.broadcast-complete");
		this.brCaught = this.config.getBoolean("banwaves.broadcast-caught");
		this.bungeeCommand = this.config.getBoolean("bungee.execute-ban-command-in-bungee");
		this.prefix = this.config.getString("Prefix", "§7[§b§l❀§7] ").replace("&", "§");
		this.miscPrefix = this.config.getString("MiscPrefix", "§7[§e§l⚠§7] ").replace("&", "§");
		this.expIcon = this.config.getString("experimental-alert-symbol", "&aΔ").replace("&", "§");
		this.noPunishIcon = this.config.getString("noautoban-alert-symbol", "&c≠").replace("&", "§");
		this.alertMessage = this.config.getString("AlertsMessage", "&f%player% &7failed &b%check% &7[x&b%vl%&7]").replace("&", "§");
		this.clientCheckMessage = this.config.getString("ClientCheckMessage", "&f%player% &7joined using &7[&e%brand%&7]").replace("&", "§");
		this.antiCrashMessage = this.config.getString("AntiCrashMessage", "&e%player% &fwas kicked for &esuspicious activity &7(&6%debug%&7)").replace("&", "§");
		this.alertHoverMessage = this.config.getString("AlertsHoverableMessage").replace("&", "§");
		this.alertClickCommand = this.config.getString("AlertsClickCommand.command");
		this.hoverlessAlert = this.config.getBoolean("hoverless-alert");
		this.spigotApiAlert = this.config.getBoolean("spigot-api-alert");
		this.alertHoverMessageHighlight = this.config.getString("AlertsHoverableMessageHighlightColor", "&b").replace("&", "§");
		this.guiHighlightColor = this.config.getString("GuiHighlightColor", "&l&b").replace("&", "§");
		this.logsBan = this.config.getString("commands.logs.ban-color", "&c").replace("&", "§");
		this.logsHighlight = this.config.getString("commands.logs.highlight-color", "&b").replace("&", "§");
		this.alertDelay = this.config.getLong("alert-delay");
		this.banCommand = this.config.getStringList("PunishCommand");
		this.punishmentsBan = this.config.getStringList("Punishments.banCommand");
		this.punishmentsKick = this.config.getStringList("Punishments.kickCommand");
		this.punishMsg = this.config.getString("Punishments.message");
		this.commandDelay = this.config.getLong("Punishments.command-delay-seconds");
		this.punishBroadcast = this.config.getBoolean("Punishments.broadcast");
		this.autoban = this.config.getBoolean("autoban");
		this.disallowFlagsAfterPunish = this.config.getBoolean("disallow-flags-after-punishment");
		this.pullback = this.config.getBoolean("pullback.enabled");
		this.pullbackMode = this.config.getString("pullback.type");
		this.defaultVersion = this.config.getString("default-version");
		this.bypass = this.config.getBoolean("bypass-permission");
		this.name = this.config.getString("anticheat-name");
		this.serverName = this.config.getString("server-name");
		this.geyserSupport = this.config.getBoolean("geyser.stop-injecting-bedrock-players");
		this.geyserPrefixCheck = this.config.getBoolean("geyser.check-for-name-prefix");
		this.geyserPrefix = this.config.getString("geyser.name-prefix");
		this.logSync = this.config.getBoolean("reset-violations-on-leave");
		this.discordAlert = this.config.getBoolean("discord.enabled");
		this.sendAlerts = this.config.getBoolean("discord.send-alerts");
		this.sendBans = this.config.getBoolean("discord.send-bans");
		this.bungeeAlert = this.config.getBoolean("bungee.alerts");
		this.bungeePostRate = this.config.getInt("bungee.alert-post-vl-rate");
		this.anticrash = this.config.getBoolean("anticrash.enabled");
		this.moveSpam = this.config.getBoolean("anticrash.move-spam");
		this.placeSpam = this.config.getBoolean("anticrash.place-spam");
		this.largeMove = this.config.getBoolean("anticrash.large-move");
		this.windowSpam = this.config.getBoolean("anticrash.window-spam");
		this.slotSpam = this.config.getBoolean("anticrash.slot-spam");
		this.armSpam = this.config.getBoolean("anticrash.arm-spam");
		this.payloadSpam = this.config.getBoolean("anticrash.payload-spam");
		this.anticrashKickMsg = ChatColor.translateAlternateColorCodes('&', this.config.getString("anticrash.kick-message"));
		this.nethandler = this.config.getBoolean("nethandler.enabled");
		this.spoof = this.config.getBoolean("nethandler.spoof");
		this.delay = this.config.getBoolean("nethandler.delay");
		this.cancelTransactions = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.cancel-transactions-alert"));
		this.cancelKeepalives = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.cancel-keepalives-alert"));
		this.ownTransactions = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.own-transactions-alert"));
		this.ownKeepalives = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.own-keepalives-alert"));
		this.transactionOrder = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.transaction-order"));
		this.orderKick = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.wrong-order-kick-message"));
		this.cancelOwnKick = ChatColor.translateAlternateColorCodes('&', this.config.getString("nethandler.cancel-and-own-kick-message"));
		this.vehicleHandler = this.config.getBoolean("vehicle-handler.unmount");
		this.pingKick = this.config.getBoolean("high-ping-kick.enabled");
		this.pingKickMaxPing = this.config.getInt("high-ping-kick.max-ping");
		this.pingKickTicks = this.config.getInt("high-ping-kick.ping-over-max-ticks-before-kick");
		this.pingKickMsg = ChatColor.translateAlternateColorCodes('&', this.config.getString("high-ping-kick.kick-message"));
		this.antivpn = this.config.getBoolean("anti-vpn.enabled");
		this.proxycheck = this.config.getBoolean("anti-vpn.proxy-check");
		this.maliciouscheck = this.config.getBoolean("anti-vpn.malicious-check");
		this.antivpnKickMsg = ChatColor.translateAlternateColorCodes('&', this.config.getString("anti-vpn.kick-message"));
		this.antiVpnBypass = this.config.getStringList("anti-vpn.bypass");
		this.clientCheck = this.config.getBoolean("client-check");
		this.injectEarly = this.config.getBoolean("packetevents.injectEarly");
		this.injectAsync = this.config.getBoolean("packetevents.injectAsync");
		this.ejectAsync = this.config.getBoolean("packetevents.ejectAsync");
		this.kickUninjected = this.config.getBoolean("packetevents.kickUninjected");
		this.uninjectedKick = this.config.getString("packetevents.uninjected-kick-message");
		this.ghostBlock = this.config.getBoolean("ghostblock-support.enabled");
		this.gbLagback = this.config.getBoolean("ghostblock-support.lagback-on-walk");
		this.gbUpdate = this.config.getBoolean("ghostblock-support.update-on-walk");
		this.liquidDetect = this.config.getBoolean("ghostblock-support.liquid-dector");
		this.maxTickLenght = this.config.getLong("server-lag-protection.max-tick-length");
		this.lagWarnMsg = this.config.getString("server-lag-protection.warning-message");
		this.lagWarnDisplay = this.config.getString("server-lag-protection.warning-display-type");
		this.noPermission = this.config.getString("commands.no-permission");
		this.fixAsyncKb = this.config.getBoolean("async-kb-fix");
		this.crackedServer = this.config.getBoolean("cracked-server");
		this.exemptTicksJoin = (long)this.config.getInt("join-exempt-ticks");
		String acname = this.license.equals(" ") ? "VengeanceLoader" : "KarhuLoader";
		this.save();
	}

	public void loadChecks(JavaPlugin karhu, boolean silent) {
		this.checkFile = new File(karhu.getDataFolder(), "checks.yml");
		if (!this.checkFile.exists()) {
			karhu.saveResource("checks.yml", false);
			if (!silent) {
				Karhu.getInstance().printCool("&b> &fGenerating file checks.yml");
			}
		} else if (!silent) {
			Karhu.getInstance().printCool("&b> &fLoading file checks.yml");
		}

		this.checks = YamlConfiguration.loadConfiguration(this.checkFile);
		if (!this.checks.isSet("PACKET.Timer.A.cap")) {
			this.checks.set("PACKET.Timer.A.cap", 10000);
		}

		if (!this.checks.isSet("COMBAT.AutoClicker.A.max-cps")) {
			this.checks.set("COMBAT.AutoClicker.A.max-cps", 25);
		}

		if (!this.checks.isSet("COMBAT.Hitbox.A.cancel-out-box-hits")) {
			this.checks.set("COMBAT.Hitbox.A.cancel-out-box-hits", false);
		}

		if (!this.checks.isSet("COMBAT.Reach.A.cancel-reach-hits")) {
			this.checks.set("COMBAT.Reach.A.cancel-reach-hits", false);
		}

		if (!this.checks.isSet("COMBAT.Reach.A.reach-to-flag")) {
			this.checks.set("COMBAT.Reach.A.reach-to-flag", 3.01);
		}

		if (!this.checks.isSet("COMBAT.Reach.A.buffer")) {
			this.checks.set("COMBAT.Reach.A.buffer", 1.5);
		}

		if (!this.checks.isSet("COMBAT.Reach.A.decay-per-miss")) {
			this.checks.set("COMBAT.Reach.A.decay-per-miss", 0.01);
		}

		if (!this.checks.isSet("COMBAT.Reach.A.safe-mode")) {
			this.checks.set("COMBAT.Reach.A.safe-mode", false);
		}

		if (!this.checks.isSet("COMBAT.Reach.A.remove-triple-hits")) {
			this.checks.set("COMBAT.Reach.A.remove-triple-hits", true);
		}

		if (!this.checks.isSet("MOVEMENT.Speed.A.fix-noslow-eat-shoot-glitch")) {
			this.checks.set("MOVEMENT.Speed.A.fix-noslow-eat-shoot-glitch", false);
		}

		if (!this.checks.isSet("MOVEMENT.Speed.A.detect-noslow")) {
			this.checks.set("MOVEMENT.Speed.A.detect-noslow", false);
		}

		if (!this.checks.isSet("MOVEMENT.Speed.A.detect-noweb")) {
			this.checks.set("MOVEMENT.Speed.A.detect-noweb", false);
		}

		if (!this.checks.isSet("MOVEMENT.Speed.A.threshold-multiplier")) {
			this.checks.set("MOVEMENT.Speed.A.threshold-multiplier", 1.03);
		}

		if (!this.checks.isSet("MOVEMENT.Speed.B.threshold-multiplier")) {
			this.checks.set("MOVEMENT.Speed.B.threshold-multiplier", 1.0);
		}

		if (!this.checks.isSet("MOVEMENT.Speed.C.threshold-multiplier")) {
			this.checks.set("MOVEMENT.Speed.C.threshold-multiplier", 1.0);
		}

		this.maxCps = this.checks.getDouble("COMBAT.AutoClicker.A.max-cps");
		this.reachCancel = this.checks.getBoolean("COMBAT.Reach.A.cancel-reach-hits");
		this.triplehitBlock = this.checks.getBoolean("COMBAT.Reach.A.remove-triple-hits");
		this.reachSafe = this.checks.getBoolean("COMBAT.Reach.A.safe-mode");
		this.hitboxCancel = this.checks.getBoolean("COMBAT.Hitbox.A.cancel-out-box-hits");
		this.disableHitboxCheck = this.checks.getBoolean("COMBAT.Reach.A.enabled");
		this.checkHitbox = this.checks.getBoolean("COMBAT.Hitbox.A.enabled");
		this.timerACapLenght = this.checks.getLong("PACKET.Timer.A.cap") * 1000000L;
		this.reachToFlag = this.checks.getDouble("COMBAT.Reach.A.reach-to-flag");
		this.reachBuffer = this.checks.getDouble("COMBAT.Reach.A.buffer");
		this.reachDecayPerMiss = this.checks.getDouble("COMBAT.Reach.A.decay-per-miss");
		this.fixEat = this.checks.getBoolean("MOVEMENT.Speed.A.fix-noslow-eat-shoot-glitch");
		this.flagNoSlow = this.checks.getBoolean("MOVEMENT.Speed.A.detect-noslow");
		this.flagNoWeb = this.checks.getBoolean("MOVEMENT.Speed.A.detect-noweb");
		this.speedAMult = this.checks.getDouble("MOVEMENT.Speed.A.threshold-multiplier");
		this.speedBMult = this.checks.getDouble("MOVEMENT.Speed.B.threshold-multiplier");
		this.speedCMult = this.checks.getDouble("MOVEMENT.Speed.C.threshold-multiplier");
		if (!silent) {
			Karhu.getInstance().getCheckState().initConfig(this.checks);
		}

		if (!this.firstTime) {
			Karhu.getInstance().getCheckState().updateChecks();
		}

		this.saveChecks();
		this.firstTime = false;
	}

	public void save() {
		try {
			this.config.save(this.configFile);
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	public void saveChecks() {
		try {
			this.checks.save(this.checkFile);
		} catch (IOException var2) {
			var2.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public File getConfigFile() {
		return this.configFile;
	}

	public FileConfiguration getChecks() {
		return this.checks;
	}

	public File getCheckFile() {
		return this.checkFile;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getExpIcon() {
		return this.expIcon;
	}

	public String getNoPunishIcon() {
		return this.noPunishIcon;
	}

	public String getMiscPrefix() {
		return this.miscPrefix;
	}

	public String getAlertMessage() {
		return this.alertMessage;
	}

	public String getClientCheckMessage() {
		return this.clientCheckMessage;
	}

	public String getAlertHoverMessage() {
		return this.alertHoverMessage;
	}

	public String getAlertClickCommand() {
		return this.alertClickCommand;
	}

	public String getAlertHoverMessageHighlight() {
		return this.alertHoverMessageHighlight;
	}

	public String getGuiHighlightColor() {
		return this.guiHighlightColor;
	}

	public String getPunishMsg() {
		return this.punishMsg;
	}

	public List<String> getBanCommand() {
		return this.banCommand;
	}

	public List<String> getPunishmentsBan() {
		return this.punishmentsBan;
	}

	public List<String> getPunishmentsKick() {
		return this.punishmentsKick;
	}

	public long getCommandDelay() {
		return this.commandDelay;
	}

	public boolean isAutoban() {
		return this.autoban;
	}

	public boolean isPunishBroadcast() {
		return this.punishBroadcast;
	}

	public boolean isDisallowFlagsAfterPunish() {
		return this.disallowFlagsAfterPunish;
	}

	public boolean isSpigotApiAlert() {
		return this.spigotApiAlert;
	}

	public boolean isHoverlessAlert() {
		return this.hoverlessAlert;
	}

	public boolean isDiscordAlert() {
		return this.discordAlert;
	}

	public boolean isSendAlerts() {
		return this.sendAlerts;
	}

	public boolean isSendBans() {
		return this.sendBans;
	}

	public boolean isBungeeAlert() {
		return this.bungeeAlert;
	}

	public boolean isBypass() {
		return this.bypass;
	}

	public boolean isPullback() {
		return this.pullback;
	}

	public boolean isCrackedServer() {
		return this.crackedServer;
	}

	public boolean isBungeeCommand() {
		return this.bungeeCommand;
	}

	public String getDefaultVersion() {
		return this.defaultVersion;
	}

	public String getLicense() {
		return this.license;
	}

	public String getPullbackMode() {
		return this.pullbackMode;
	}

	public int getBungeePostRate() {
		return this.bungeePostRate;
	}

	public long getAlertDelay() {
		return this.alertDelay;
	}

	public String getName() {
		return this.name;
	}

	public String getServerName() {
		return this.serverName;
	}

	public double getMaxCps() {
		return this.maxCps;
	}

	public boolean isReachTransaction() {
		return this.reachTransaction;
	}

	public boolean isReachCancel() {
		return this.reachCancel;
	}

	public boolean isHitboxCancel() {
		return this.hitboxCancel;
	}

	public boolean isTriplehitBlock() {
		return this.triplehitBlock;
	}

	public boolean isDisableHitboxCheck() {
		return this.disableHitboxCheck;
	}

	public boolean isCheckHitbox() {
		return this.checkHitbox;
	}

	public boolean isReachSafe() {
		return this.reachSafe;
	}

	public long getTimerACapLenght() {
		return this.timerACapLenght;
	}

	public long getExemptTicksJoin() {
		return this.exemptTicksJoin;
	}

	public String getBanwavePunish() {
		return this.banwavePunish;
	}

	public String getBanwaveCaught() {
		return this.banwaveCaught;
	}

	public String getBanwaveComplete() {
		return this.banwaveComplete;
	}

	public String getGeyserPrefix() {
		return this.geyserPrefix;
	}

	public boolean isBrComplete() {
		return this.brComplete;
	}

	public boolean isBrCaught() {
		return this.brCaught;
	}

	public boolean isGeyserSupport() {
		return this.geyserSupport;
	}

	public boolean isGeyserPrefixCheck() {
		return this.geyserPrefixCheck;
	}

	public boolean isAnticrash() {
		return this.anticrash;
	}

	public boolean isLargeMove() {
		return this.largeMove;
	}

	public boolean isMoveSpam() {
		return this.moveSpam;
	}

	public boolean isArmSpam() {
		return this.armSpam;
	}

	public boolean isPlaceSpam() {
		return this.placeSpam;
	}

	public boolean isPayloadSpam() {
		return this.payloadSpam;
	}

	public boolean isSlotSpam() {
		return this.slotSpam;
	}

	public boolean isWindowSpam() {
		return this.windowSpam;
	}

	public String getAnticrashKickMsg() {
		return this.anticrashKickMsg;
	}

	public String getAntiCrashMessage() {
		return this.antiCrashMessage;
	}

	public boolean isPingKick() {
		return this.pingKick;
	}

	public int getPingKickMaxPing() {
		return this.pingKickMaxPing;
	}

	public int getPingKickTicks() {
		return this.pingKickTicks;
	}

	public String getPingKickMsg() {
		return this.pingKickMsg;
	}

	public boolean isGhostBlock() {
		return this.ghostBlock;
	}

	public boolean isGbLagback() {
		return this.gbLagback;
	}

	public boolean isGbUpdate() {
		return this.gbUpdate;
	}

	public boolean isLiquidDetect() {
		return this.liquidDetect;
	}

	public boolean isNethandler() {
		return this.nethandler;
	}

	public boolean isDelay() {
		return this.delay;
	}

	public boolean isSpoof() {
		return this.spoof;
	}

	public String getTransactionOrder() {
		return this.transactionOrder;
	}

	public String getCancelTransactions() {
		return this.cancelTransactions;
	}

	public String getCancelKeepalives() {
		return this.cancelKeepalives;
	}

	public String getOwnTransactions() {
		return this.ownTransactions;
	}

	public String getOwnKeepalives() {
		return this.ownKeepalives;
	}

	public String getCancelOwnKick() {
		return this.cancelOwnKick;
	}

	public String getOrderKick() {
		return this.orderKick;
	}

	public String getLogsHighlight() {
		return this.logsHighlight;
	}

	public String getLogsBan() {
		return this.logsBan;
	}

	public String getAntivpnKickMsg() {
		return this.antivpnKickMsg;
	}

	public boolean isLogSync() {
		return this.logSync;
	}

	public boolean isVehicleHandler() {
		return this.vehicleHandler;
	}

	public boolean isAntivpn() {
		return this.antivpn;
	}

	public boolean isProxycheck() {
		return this.proxycheck;
	}

	public boolean isMaliciouscheck() {
		return this.maliciouscheck;
	}

	public boolean isClientCheck() {
		return this.clientCheck;
	}

	public List<String> getAntiVpnBypass() {
		return this.antiVpnBypass;
	}

	public double getReachToFlag() {
		return this.reachToFlag;
	}

	public double getReachBuffer() {
		return this.reachBuffer;
	}

	public double getReachDecayPerMiss() {
		return this.reachDecayPerMiss;
	}

	public int getReachBackTrack() {
		return this.reachBackTrack;
	}

	public boolean isFixEat() {
		return this.fixEat;
	}

	public boolean isFlagNoWeb() {
		return this.flagNoWeb;
	}

	public boolean isFlagNoSlow() {
		return this.flagNoSlow;
	}

	public double getSpeedAMult() {
		return this.speedAMult;
	}

	public double getSpeedBMult() {
		return this.speedBMult;
	}

	public double getSpeedCMult() {
		return this.speedCMult;
	}

	public long getMaxTickLenght() {
		return this.maxTickLenght;
	}

	public String getLagWarnMsg() {
		return this.lagWarnMsg;
	}

	public String getLagWarnDisplay() {
		return this.lagWarnDisplay;
	}

	public String getNoPermission() {
		return this.noPermission;
	}

	public boolean isInjectEarly() {
		return this.injectEarly;
	}

	public boolean isInjectAsync() {
		return this.injectAsync;
	}

	public boolean isEjectAsync() {
		return this.ejectAsync;
	}

	public boolean isKickUninjected() {
		return this.kickUninjected;
	}

	public String getUninjectedKick() {
		return this.uninjectedKick;
	}

	public boolean isFixAsyncKb() {
		return this.fixAsyncKb;
	}

	public boolean isFirstTime() {
		return this.firstTime;
	}
}
