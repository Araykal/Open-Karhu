/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.util.TimeStampMode;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import me.liwk.karhu.api.KarhuLogger;
import me.liwk.karhu.api.check.CheckState;
import me.liwk.karhu.command.CommandAPI;
import me.liwk.karhu.command.sub.AlertsCommand;
import me.liwk.karhu.command.sub.KarhuCommand;
import me.liwk.karhu.command.sub.LogsCommand;
import me.liwk.karhu.database.Storage;
import me.liwk.karhu.database.mongo.MongoStorage;
import me.liwk.karhu.database.mysql.MySQL;
import me.liwk.karhu.database.mysql.MySQLStorage;
import me.liwk.karhu.database.sqlite.LocalStorage;
import me.liwk.karhu.handler.global.PacketProcessor;
import me.liwk.karhu.handler.global.TransactionHandler;
import me.liwk.karhu.handler.global.bukkit.BlockReachListener;
import me.liwk.karhu.handler.global.bukkit.BukkitHandler;
import me.liwk.karhu.handler.global.bukkit.GhostBreakListener;
import me.liwk.karhu.handler.global.bukkit.InventoryHandler;
import me.liwk.karhu.handler.global.bukkit.NoLookBreakListener;
import me.liwk.karhu.handler.global.bukkit.PlayerVelocityHandler;
import me.liwk.karhu.handler.prediction.NMSWorldProvider;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.manager.PlayerDataManager;
import me.liwk.karhu.manager.WaveManager;
import me.liwk.karhu.manager.alert.AlertsManager;
import me.liwk.karhu.util.APICaller;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.Metrics;
import me.liwk.karhu.util.benchmark.KarhuBenchmarker;
import me.liwk.karhu.util.framework.CommandFramework;
import me.liwk.karhu.util.framework.CommandManager1_19;
import me.liwk.karhu.util.task.Tasker;
import me.liwk.karhu.util.thread.KarhuThreadManager;
import me.liwk.karhu.util.thread.ThreadManager;
import me.liwk.karhu.world.chunk.ChunkListeners;
import me.liwk.karhu.world.chunk.IChunkManager;
import me.liwk.karhu.world.chunk.WorldChunkManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Karhu extends JavaPlugin {
	private IChunkManager chunkManager;
	private static Karhu instance;
	private boolean isViaRewind;
	private boolean isViaVersion;
	private boolean isProtocolSupport;
	private boolean isFloodgate;
	private ExecutorService alertsThread;
	private ExecutorService discordThread;
	private ExecutorService antiVPNThread;
	private ExecutorService packetThread;
	private ExecutorService statsThread;
	public static Storage storage;
	private ConfigManager configManager;
	private CheckState checkState;
	private PlayerDataManager dataManager;
	private AlertsManager alertsManager;
	private CommandFramework framework;
	private CommandManager1_19 commandManager;
	private NMSWorldProvider nmsWorldProvider;
	public static ServerVersion SERVER_VERSION;
	public static boolean PING_PONG_MODE;
	private double tps;
	public long tpsMilliseconds;
	public long ticks;
	public long lastTimeStamp;
	public long lastTick;
	public long lastPerformanceDrop;
	public long lastPerformanceAnnounce;
	private long serverTick;
	private JavaPlugin plugin;
	private static Boolean apiAvailability = null;
	public static boolean crackedServer = false;
	public ThreadManager threadManager;
	public static double DIVISOR = 32.0;
	private TransactionHandler transactionHandler;
	private WaveManager waveManager;
	private String bungeeChannel = "karhu:proxy";
	private final LegacyComponentSerializer componentSerializer = LegacyComponentSerializer.builder().character('&').hexCharacter('#').build();

	public void onEnable() {
		Bukkit.getScheduler().runTaskLater(this, () -> {
			long enable = System.nanoTime();
			instance = this;
			this.plugin = this;
			List<String> no = new ArrayList<>();
			no.add(" ___  __    ________  ________  ___  ___  ___  ___     ");
			no.add("|\\  \\|\\  \\ |\\   __  \\|\\   __  \\|\\  \\|\\  \\|\\  \\|\\  \\    ");
			no.add("\\ \\  \\/  /|\\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\\\\\  \\ \\  \\\\\\  \\   ");
			no.add(" \\ \\   ___  \\ \\   __  \\ \\   _  _\\ \\   __  \\ \\  \\\\\\  \\  ");
			no.add("  \\ \\  \\\\ \\  \\ \\  \\ \\  \\ \\  \\\\  \\\\ \\  \\ \\  \\ \\  \\\\\\  \\ ");
			no.add("   \\ \\__\\\\ \\__\\ \\__\\ \\__\\ \\__\\\\ _\\\\ \\__\\ \\__\\ \\_______\\");
			no.add("    \\|__| \\|__|\\|__|\\|__|\\|__|\\|__|\\|__|\\|__|\\|_______|");
			no.forEach(msg -> this.printCool(ChatColor.BLUE + msg));
			no.clear();
			this.printCool(ChatColor.BLUE + "Version: " + this.getVersion() + " | " + this.getBuild());
			long start = System.currentTimeMillis();
			File f = new File(this.plugin.getDataFolder().getAbsolutePath() + File.separator + "libs" + File.separator);
			if (f.mkdir()) {
				this.printCool(ChatColor.GREEN + "Folder" + this.plugin.getDataFolder().getAbsolutePath() + File.separator + "libs" + File.separator + " created!");
			} else {
				this.printCool(
						ChatColor.RED + "Folder" + this.plugin.getDataFolder().getAbsolutePath() + File.separator + "libs" + File.separator + " failed to create, maybe it's already there!"
				);
			}
			this.threadManager = new ThreadManager();
			this.chunkManager = new WorldChunkManager();
			this.waveManager = new WaveManager();
			this.transactionHandler = new TransactionHandler();
			KarhuBenchmarker.registerProfiles();
			Tasker.load(this.plugin);
			this.printCool("&b> &fTasker initialized");
			this.packetThread = KarhuThreadManager.createNewNormalExecutor("karhu-packet-thread");
			this.alertsThread = KarhuThreadManager.createNewNormalExecutor("karhu-alert-thread");
			this.discordThread = KarhuThreadManager.createNewExecutor("karhu-discord-thread");
			this.antiVPNThread = KarhuThreadManager.createNewExecutor("karhu-antivpn-thread");
			this.statsThread = KarhuThreadManager.createNewExecutor(2, "karhu-stats-thread");
			this.printCool("&b> &fThreads initialized");
			this.initPockets();
			this.checkState = new CheckState();
			this.checkState.loadOrGetClasses();
			this.dataManager = new PlayerDataManager(this);
			this.configManager = new ConfigManager(this.plugin);
			this.printCool("&b> &fPacketEvents settings setup");
			Bukkit.getMessenger().registerOutgoingPluginChannel(this.plugin, this.bungeeChannel);
			PacketEvents.getAPI().getEventManager().registerListener(new PacketProcessor(this));
			this.printCool("&b> &fPacketEvents loaded " + SERVER_VERSION);
			this.plugin.getServer().getPluginManager().registerEvents(new BukkitHandler(), this.plugin);
			this.plugin.getServer().getPluginManager().registerEvents(new InventoryHandler(), this.plugin);
			this.plugin.getServer().getPluginManager().registerEvents(new GhostBreakListener(), this.plugin);
			this.plugin.getServer().getPluginManager().registerEvents(new NoLookBreakListener(), this.plugin);
			this.plugin.getServer().getPluginManager().registerEvents(new BlockReachListener(), this.plugin);
			this.plugin.getServer().getPluginManager().registerEvents(new ChunkListeners(), this.plugin);
			this.plugin.getServer().getPluginManager().registerEvents(new PlayerVelocityHandler(), this.plugin);
			this.printCool("&b> &fEvents initialized");
			this.framework = new CommandFramework(this.plugin);
			this.commandManager = new CommandManager1_19(this.plugin);
			this.alertsManager = new AlertsManager();
			this.printCool("&b> &fManagers initialized");
			this.registerCommands();
			this.printCool("&b> &fCommands initialized");
			PING_PONG_MODE = SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_17);
			DIVISOR = SERVER_VERSION.getProtocolVersion() <= 47 ? 32.0 : 4098.0;
			String via = this.configManager.getConfig().getString("database").toLowerCase();
			switch (via) {
				case "mongodb":
				case "mongo":
					storage = new MongoStorage();
					this.printCool("&b> &fMongo initialized");
					break;
				case "mysql":
					storage = new MySQLStorage();
					this.printCool("&b> &fMySQL initialized");
					break;
				default:
					storage = new LocalStorage();
					this.printCool("&b> &fSQLite initialized");
			}

			try {
				storage.init();
			} catch (SQLException var16) {
				throw new RuntimeException(var16);
			}

			Tasker.run(() -> {
				this.printCool("&b> &fStarting world chunk load...");
				long startTime = System.currentTimeMillis();
				AtomicInteger chunkAmountServer = new AtomicInteger();
				AtomicInteger chunkAmountCache = new AtomicInteger();
				Bukkit.getWorlds().forEach(w -> {
					this.chunkManager.addWorld(w);
					Chunk[] array = w.getLoadedChunks();
					this.printCool("&b> &fChunkManager is going to cache " + array.length + " chunks from world " + w.getName());
					if (SERVER_VERSION.getProtocolVersion() >= 47) {
						for (Chunk c : array) {
							this.chunkManager.onChunkLoad(c);
						}
					} else {
						int size = Math.min(array.length, 32);

						for (int i = 0; i < size; ++i) {
							this.chunkManager.onChunkLoad(array[i]);
						}
					}

					this.printCool("&b> &fChunkManager cached " + array.length + " chunks from world " + w.getName());
					chunkAmountServer.addAndGet(array.length);
					chunkAmountCache.addAndGet(this.chunkManager.getCacheSize(w));
				});
				long finishedAt = System.currentTimeMillis();
				this.printCool("&b> &fFinished chunk load in " + (finishedAt - startTime) + "ms");
				this.printCool("&b> &fYour server had &b" + chunkAmountServer.get() + " &fchunks pre-loaded, karhu cached &b" + chunkAmountCache.get() + " &fchunks");
			});
			this.runTicks();
			this.printCool("&b> &fTPS counter & Tick handler initialized");
			this.nmsWorldProvider = new NMSWorldProvider(this);
			this.printCool("&b> &fChunkManager initialized");
			Plugin viaVersion = Bukkit.getPluginManager().getPlugin("ViaVersion");
			this.isFloodgate = Bukkit.getPluginManager().getPlugin("floodgate") != null;
			this.isViaRewind = Bukkit.getPluginManager().getPlugin("ViaRewind") != null;
			this.isViaVersion = viaVersion != null;
			this.isProtocolSupport = Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null;
			long loadMs = System.currentTimeMillis() - start;
			if (!this.isFloodgate && this.configManager.isGeyserSupport()) {
				this.printCool(ChatColor.DARK_RED + "Geyser support is enabled, but floodgate plugin was not found");
			}

			if (this.isFloodgate && !this.configManager.isGeyserSupport()) {
				this.printCool(ChatColor.DARK_RED + "Floodgate was found, but geyser support is disabled in config");
			}

			this.printCool(ChatColor.DARK_GREEN + "Finished loading in " + loadMs + "ms");
			if (isAPIAvailable()) {
				APICaller.callInit(loadMs);
			}

			try {
				BufferedReader is = new BufferedReader(new FileReader("server.properties"));
				Properties props = new Properties();
				props.load(is);
				is.close();
				crackedServer = Boolean.parseBoolean(props.getProperty("online-mode"));
			} catch (IOException var15) {
				try {
					BufferedReader isx = new BufferedReader(new FileReader(getInstance().getPlug().getDataFolder().getParent() + "server.properties"));
					Properties propsx = new Properties();
					propsx.load(isx);
					isx.close();
					crackedServer = Boolean.parseBoolean(propsx.getProperty("online-mode"));
				} catch (IOException var141) {
					crackedServer = false;
					KarhuLogger.critical("Couldn't find server.properties, cracked server is set to false.");
				}
			}

			new Metrics(this.plugin, 11204);
			Tasker.taskAsync(() -> this.waveManager.importFromDb());
			Plugin plib = Bukkit.getPluginManager().getPlugin("ProtocolLib");
			if (plib != null && viaVersion != null && !plib.getDescription().getVersion().startsWith("5")) {
				this.printCool(
						"&b> &cThis version of ProtocolLib doesn't support Karhu, download latest from: https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/target/ProtocolLib.jar)"
				);
				Bukkit.getServer().getScheduler().cancelTasks(this.plugin);
				Bukkit.getPluginManager().disablePlugin(this.plugin);
			}
		},15);
	}

	public void onDisable() {
		Tasker.stop();
		PacketEvents.getAPI().terminate();
		KarhuThreadManager.shutdown();
		this.chunkManager.unloadAll();
		this.dataManager.getPlayerDataMap().clear();
		this.checkState.getCheckClasses().clear();
		this.checkState.getAutobanMap().clear();
		this.checkState.getEnabledMap().clear();
		this.checkState.getVlMap().clear();
	}

	private void registerCommands() {
		new CommandAPI(this.framework);
		new KarhuCommand(this.framework);
		new AlertsCommand(this.framework);
		new LogsCommand(this.framework);
	}

	public void runTicks() {
		(new BukkitRunnable() {
			int ticks;

			public void run() {
				long nano = (long)((double)System.nanoTime() / 1000000.0);
				long timeStamp = System.currentTimeMillis();
				if (Karhu.this.isServerLagging(timeStamp)) {
					Karhu.this.lastPerformanceDrop = timeStamp;
				}

				if (Karhu.this.serverTick == Long.MAX_VALUE) {
					Karhu.this.serverTick = 0L;
				}

				++Karhu.this.serverTick;
				++this.ticks;
				if (this.ticks >= 20) {
					Karhu.this.tpsMilliseconds = nano - Karhu.this.lastTimeStamp;
					Karhu.this.tps = 1000.0 / (double)Karhu.this.tpsMilliseconds * 20.0;
					Karhu.this.lastTimeStamp = nano;
					this.ticks = 0;
				}

				Karhu.this.lastTick = timeStamp;
			}
		}).runTaskTimer(this.plugin, 0L, 1L);

	}

	public static boolean isAPIAvailable() {
		return apiAvailability == null ? (apiAvailability = Bukkit.getPluginManager().isPluginEnabled("KarhuAPI")) : apiAvailability;
	}

	public boolean isServerLagging(long time) {
		return this.tps < 19.6 || time - this.lastTick > this.configManager.getMaxTickLenght();
	}

	public boolean hasRecentlyDropped(long time) {
		return System.currentTimeMillis() - this.lastPerformanceDrop <= time;
	}

	public double getTPS() {
		return Math.min(MathUtil.round(this.tps, 2), 20.0);
	}

	public void printCool(String text) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}

	public void initPockets() {
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this.getPlug()));
		PacketEvents.getAPI().getSettings().bStats(false).checkForUpdates(false).debug(false).timeStampMode(TimeStampMode.NANO);
		PacketEvents.getAPI().load();
		SERVER_VERSION = PacketEvents.getAPI().getServerManager().getVersion();
	}

	public IChunkManager getChunkManager() {
		return this.chunkManager;
	}

	public static Karhu getInstance() {
		return instance;
	}

	public String getVersion() {
		return "2.7.5";
	}

	public String getBuild() {
		return "230";
	}

	public boolean isViaRewind() {
		return this.isViaRewind;
	}

	public boolean isViaVersion() {
		return this.isViaVersion;
	}

	public boolean isProtocolSupport() {
		return this.isProtocolSupport;
	}

	public boolean isFloodgate() {
		return this.isFloodgate;
	}

	public ExecutorService getAlertsThread() {
		return this.alertsThread;
	}

	public ExecutorService getDiscordThread() {
		return this.discordThread;
	}

	public ExecutorService getAntiVPNThread() {
		return this.antiVPNThread;
	}

	public ExecutorService getPacketThread() {
		return this.packetThread;
	}

	public ExecutorService getStatsThread() {
		return this.statsThread;
	}

	public static Storage getStorage() {
		return storage;
	}

	public ConfigManager getConfigManager() {
		return this.configManager;
	}

	public CheckState getCheckState() {
		return this.checkState;
	}

	public PlayerDataManager getDataManager() {
		return this.dataManager;
	}

	public AlertsManager getAlertsManager() {
		return this.alertsManager;
	}

	public CommandFramework getFramework() {
		return this.framework;
	}

	public CommandManager1_19 getCommandManager() {
		return this.commandManager;
	}

	public NMSWorldProvider getNmsWorldProvider() {
		return this.nmsWorldProvider;
	}

	public static ServerVersion getSERVER_VERSION() {
		return SERVER_VERSION;
	}

	public static boolean isPING_PONG_MODE() {
		return PING_PONG_MODE;
	}

	public long getTpsMilliseconds() {
		return this.tpsMilliseconds;
	}

	public long getTicks() {
		return this.ticks;
	}

	public long getLastTimeStamp() {
		return this.lastTimeStamp;
	}

	public long getLastTick() {
		return this.lastTick;
	}

	public long getLastPerformanceDrop() {
		return this.lastPerformanceDrop;
	}

	public long getLastPerformanceAnnounce() {
		return this.lastPerformanceAnnounce;
	}

	public long getServerTick() {
		return this.serverTick;
	}

	public JavaPlugin getPlug() {
		return this.plugin;
	}

	public static boolean isCrackedServer() {
		return crackedServer;
	}

	public ThreadManager getThreadManager() {
		return this.threadManager;
	}

	public TransactionHandler getTransactionHandler() {
		return this.transactionHandler;
	}

	public WaveManager getWaveManager() {
		return this.waveManager;
	}

	public String getBungeeChannel() {
		return this.bungeeChannel;
	}

	public LegacyComponentSerializer getComponentSerializer() {
		return this.componentSerializer;
	}
}
