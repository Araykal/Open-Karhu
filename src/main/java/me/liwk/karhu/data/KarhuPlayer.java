/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.data;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.check.api.CheckManager;
import me.liwk.karhu.handler.AbilityManager;
import me.liwk.karhu.handler.MovementHandler;
import me.liwk.karhu.handler.VehicleHandler;
import me.liwk.karhu.handler.collision.CollisionHandler;
import me.liwk.karhu.handler.crash.CrashHandler;
import me.liwk.karhu.handler.global.DesyncedBlockHandler;
import me.liwk.karhu.handler.global.EffectManager;
import me.liwk.karhu.handler.global.PlaceManager;
import me.liwk.karhu.handler.global.TeleportManager;
import me.liwk.karhu.handler.interfaces.AbstractPredictionHandler;
import me.liwk.karhu.handler.interfaces.ICrashHandler;
import me.liwk.karhu.handler.interfaces.IVehicleHandler;
import me.liwk.karhu.handler.interfaces.KarhuHandler;
import me.liwk.karhu.handler.net.NetHandler;
import me.liwk.karhu.handler.net.TaskData;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.benchmark.Benchmark;
import me.liwk.karhu.util.benchmark.BenchmarkType;
import me.liwk.karhu.util.benchmark.KarhuBenchmarker;
import me.liwk.karhu.util.gui.Callback;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.mc.boundingbox.BoundingBox;
import me.liwk.karhu.util.mc.vec.Vec3;
import me.liwk.karhu.util.pair.Pair;
import me.liwk.karhu.util.pending.VelocityPending;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.task.Tasker;
import me.liwk.karhu.util.thread.Thread;
import me.liwk.karhu.world.nms.NMSValueParser;
import me.liwk.karhu.world.nms.wrap.WrappedEntityPlayer;
import me.liwk.karhu.world.packet.KarhuWorld;
import me.liwk.karhu.world.packet.WorldTracker;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class KarhuPlayer {
	private boolean objectLoaded;
	private boolean removingObject = false;
	private boolean configChecked;
	private IVehicleHandler vehicleHandler;
	private UUID uuid;
	private boolean kicked;
	private CustomLocation location;
	private CustomLocation lastLocation;
	private CustomLocation lastLastLocation;
	private CustomLocation lastLastLastLocation;
	private boolean forceRunCollisions;
	private boolean placedInside;
	private boolean placedCancel;
	private int lastPlacedInside;
	private int predictionTicks;
	private int moveTicks;
	private int noMoveTicks;
	private PlaceManager placeManager;
	private final Map<Integer, TaskData> tasks = new LinkedHashMap<>();
	private CheckManager checkManager;
	private Map<Check, Set<Long>> checkViolationTimes;
	private Map<Check, Double> checkVlMap;
	private boolean cancelNextHitR;
	private boolean cancelNextHitH;
	private boolean reduceNextDamage;
	private boolean cancelBreak;
	private boolean forceCancelReach;
	private boolean cancelTripleHit;
	private boolean abusingVelocity;
	private int entityIdCancel;
	private int cancelHitsTick;
	private float jumpMovementFactor = 0.02F;
	private float speedInAir = 0.02F;
	private float lastJumpMovementFactor;
	private float attributeSpeed = 0.1F;
	private float lastAttributeSpeed;
	private float walkSpeed = 0.1F;
	private float jumpFactor;
	private boolean jumpedCurrentTick;
	private boolean jumpedLastTick;
	private boolean jumped;
	private String pressedKey;
	private Block blockBelow;
	private Block blockInside;
	private Block lastBlockInside;
	private boolean exitingVehicle;
	private String keyCombo;
	private ClientVersion clientVersion;
	private boolean isNewerThan8;
	private boolean isNewerThan12;
	private boolean isNewerThan13;
	private boolean isNewerThan16;
	private boolean confirmedVersion;
	private boolean confirmedVersion2;
	private boolean isViaMCP;
	private KarhuHandler collisionHandler;
	private DesyncedBlockHandler desyncedBlockHandler;
	private MovementHandler movementHandler;
	private AbstractPredictionHandler predictionHandler;
	private NetHandler netHandler;
	private ICrashHandler crashHandler;
	private WrappedEntityPlayer wrappedEntity;
	private boolean hasReceivedTransaction = false;
	private boolean hasReceivedKeepalive = false;
	private boolean isHasTeleportedOnce = false;
	public int sentConfirms;
	public int receivedConfirms;
	private final Short2ObjectMap<Consumer<Short>> scheduledTransactions = new Short2ObjectArrayMap();
	public Map<Short, ObjectArrayList<Consumer<Short>>> waitingConfirms = new HashMap<>();
	private short transactionId;
	private short confirmId;
	private short tickTransactionId;
	private int tickFirstConfirmationUid = -1;
	private int tickSecondConfirmationUid = -2;
	private int lastTickFirstConfirmationUid = -1;
	private int lastTickSecondConfirmationUid = -2;
	public boolean hasSentTickFirst = false;
	public boolean sendingPledgePackets;
	public boolean sentPingRequest = false;
	public boolean hasSentFirst;
	public boolean brokenVelocityVerify;
	private final TaskManager<KarhuPlayer> tasksManager = new TaskManager<>();
	public short lastPostId = -100;
	public final Map<Integer, ConcurrentLinkedDeque<VelocityPending>> velocityPending = new HashMap<>();
	private boolean takingVertical;
	private boolean confirmingVelocity;
	private boolean needExplosionAdditions;
	private int lastVelocityYReset;
	private int lastVelocityXZReset;
	private int lastVelocityTaken;
	private int velocityYTicks;
	private int maxVelocityXZTicks;
	private int maxVelocityYTicks;
	private int explosionExempt;
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	private double velocityHorizontal;
	private double confirmingY;
	private Vector tickedVelocity = null;
	private boolean playerVelocityCalled;
	private boolean playerExplodeCalled;
	private int totalTicks;
	private int lastSprintTick;
	private int lastSneakTick;
	private int airTicks;
	private int digTicks;
	private int fastDigTicks;
	private int digStopTicks;
	private int placeTicks;
	private int underPlaceTicks;
	private int bucketTicks;
	private int spoofPlaceTicks;
	private int useTicks;
	private int lClientAirTicks;
	private int clientAirTicks;
	private int lastFlyTick;
	private int lastAllowFlyTick;
	private int elapsedOnLiquid;
	private int elapsedUnderBlock;
	private int elapsedSinceBridgePlace;
	private int serverGroundTicks;
	private int clientGroundTicks;
	private int weirdTicks;
	private int lastInBerry;
	private int lastInUnloadedChunk;
	private int lastInLiquid;
	private int lastOnSlime;
	private int lastOnSoul;
	private int lastOnIce;
	private int lastOnClimbable;
	private int lastOnBed;
	private int lastInWeb;
	private int lastCollided;
	private int lastCollidedWithEntity;
	private int lastOnHalfBlock;
	private int lastCollidedV;
	private int lastCollidedVGhost;
	private int lastOnBoat;
	private int lastInPowder;
	private int lastPossibleInUnloadedChunk;
	private int lastCollidedGhost;
	private int positionPackets;
	private int lastInLiquidOffset;
	private int lastConfirmingState;
	private int lastCollidedH;
	private int lastSneakEdge;
	private int lastFence;
	private boolean digging;
	private boolean diggingBasic;
	private boolean traceDigging;
	private boolean placing;
	private boolean wasPlacing;
	private boolean skipNextSwing;
	private boolean noMoveNextFlying;
	private boolean collidedHorizontally;
	private boolean wasCollidedHorizontally;
	private boolean collidedHorizontalClient;
	private boolean collidedWithFence;
	private boolean edgeOfFence;
	private boolean collidedWithPane;
	private boolean collidedWithCactus;
	private boolean insideTrapdoor;
	private boolean insideBlock;
	private boolean wasFullyInsideBlock;
	private boolean fullyInsideBlock;
	private boolean inWeb;
	private boolean isWasInWeb;
	private boolean isWasWasInWeb;
	private boolean blocking;
	private boolean shit;
	private boolean bowing;
	private boolean finalCollidedH;
	private Vector lastAbortLoc;
	private int lastPistonPush;
	private int lastSlimePistonPush;
	public int moveCalls;
	private long serverTick;
	private long createdOnTick;
	private BoundingBox boundingBox;
	private BoundingBox mcpCollision;
	private BoundingBox lastBoundingBox;
	private double vehicleX;
	private double vehicleY;
	private double vehicleZ;
	public int ticksOnGhostBlock;
	public int ticksOnBlockHandlerNotEnabled;
	public int updateBuf;
	private LivingEntity lastTarget;
	private List<Integer> lastTargets = new ArrayList<>();
	public int lastAttackTick = 99;
	public int attacks;
	private long lastAttackPacket;
	public boolean attackedSinceVelocity;
	private boolean reachBypass;
	private boolean sprinting;
	private boolean wasSprinting;
	private boolean wasWasSprinting;
	private boolean sneaking;
	private boolean wasSneaking;
	private boolean wasWasSneaking;
	private boolean usingItem;
	private boolean lastUsingItem;
	private boolean eating;
	private boolean lastEating;
	private boolean recorrectingSprint;
	private boolean desyncSprint = true;
	private boolean inventoryOpen;
	private boolean crouching;
	private boolean inBed;
	private boolean lastInBed;
	private Vec3 bedPos;
	private int invStamp;
	private int slotSwitchTick;
	private int lastWorldChange;
	public boolean cinematic;
	public int lastCinematic;
	public final Deltas deltas = new Deltas();
	public float fallDistance;
	public float lFallDistance;
	public ConcurrentHashMap<Integer, EntityData> entityData = new ConcurrentHashMap<>();
	public int lastPos;
	public double attackerX;
	public double attackerY;
	public double attackerZ;
	public float attackerYaw;
	public float attackerPitch;
	public int teleports;
	public int addedTeleports;
	private List<Double> eyesFourteen = Arrays.asList(0.4, 1.27, 1.62);
	private List<Double> eyesNine = Arrays.asList(0.4, 1.54, 1.62);
	private List<Double> eyesLegacy = Arrays.asList(1.54, 1.62);
	private boolean onGroundServer;
	private boolean onBoat;
	private boolean groundNearBox;
	private boolean wasOnGroundServer;
	private boolean onWater;
	private boolean wasOnWater;
	private boolean aboveButNotInWater;
	private boolean waterAlmostOnFeet;
	private boolean onLava;
	private boolean wasOnLava;
	private boolean onIce;
	private boolean onLiquid;
	private boolean onSlab;
	private boolean wasOnSlab;
	private boolean onDoor;
	private boolean wasOnDoor;
	private boolean onFence;
	private boolean wasOnFence;
	private boolean onStairs;
	private boolean wasOnStairs;
	private boolean onBed;
	private boolean wasOnBed;
	private boolean underBlock;
	private boolean underBlockStrict;
	private boolean wasUnderBlock;
	private boolean underWeb;
	private boolean onWeb;
	private boolean onSoulsand;
	private boolean wasOnSoulSand;
	private boolean wasSlimeLand;
	private boolean slimeLand;
	private boolean onSlime;
	private boolean wasOnSlime;
	private boolean wasWasOnSlime;
	private boolean onCarpet;
	private boolean onComparator;
	private boolean wasOnComparator;
	private boolean onClimbable;
	private boolean wasOnClimbable;
	private boolean wasWasOnClimbable;
	private boolean onLadder;
	private boolean lastLadder;
	private boolean nearClimbable;
	private boolean onHoney;
	private boolean onSweet;
	private boolean wasOnHoney;
	private boolean onScaffolding;
	private boolean onPiston;
	private boolean onTopGhostBlock;
	private boolean atButton;
	private boolean onWaterOffset;
	private boolean lastOnWaterOffset;
	private boolean inPowder;
	private boolean sneakEdge;
	private boolean lastBlockSneak;
	private boolean atSign;
	private Block movementBlock;
	private boolean collidedWithLivingEntity;
	private float currentFriction;
	private float lastTickFriction;
	private boolean onGroundPacket;
	private boolean lastOnGroundPacket;
	private boolean lastLastOnGroundPacket;
	private boolean onGroundMath;
	private boolean lastOnGroundMath;
	private boolean lastLastOnGroundMath;
	private boolean onGhostBlock;
	private boolean underGhostBlock;
	private boolean isWasUnderGhostBlock;
	private int lastInGhostLiquid;
	public double ghostBlockSetbacks;
	private long ping;
	private long lastPingTime;
	private long transactionPing;
	private long lastTransactionPing;
	public final Map<Short, Long> transactionTime = new ConcurrentHashMap<>();
	public short timerTransactionSent;
	private boolean inUnloadedChunk;
	private boolean wasInUnloadedChunk;
	private boolean wasWasInUnloadedChunk;
	private long lastTransaction;
	private long lastTransactionPingUpdate;
	public int badPingTicks;
	public int pingInTicks;
	private TeleportManager teleportManager;
	private boolean possiblyTeleporting;
	private boolean seventeenPlacing;
	private int lastTeleport;
	private long lastTeleportPacket;
	private CustomLocation firstChunkMove;
	private boolean joining;
	private boolean fuckedTeleport;
	private boolean banned;
	private Player bukkitPlayer;
	private User user;
	private int entityId = -1;
	private String brand = "vanilla (not set)";
	private String cleanBrand = "vanilla (not set)";
	private boolean brandPosted = false;
	private EffectManager effectManager;
	public int jumpBoost;
	public int cacheBoost;
	public int speedBoost;
	public int slowness;
	public int haste;
	public int fatigue;
	public GameMode gameMode;
	public boolean allowFlying;
	public boolean flyingS;
	public boolean flyingC;
	public boolean flying;
	public boolean wasFlyingC;
	public boolean initedFlying;
	public boolean confirmingFlying;
	public boolean correctedFly;
	private AbilityManager abilityManager;
	private WorldTracker worldTracker;
	private KarhuWorld karhuWorld;
	public int lastServerSlot;
	public long lastFlying;
	public long flyingTime;
	public long lastJoinTime;
	public int lastFlyingTicks;
	public int velocityXZTicks;
	public int lastTransactionTick;
	public int trackCount;
	public int currentServerTransaction = -1;
	public int currentClientTransaction = -1;
	public int lastClientTransaction;
	public int lastLastClientTransaction;
	private boolean firstTransactionSent;
	public int lastDroppedPackets;
	public int lastPacketDrop;
	public int hurtTicks;
	private long lastFast;
	private boolean movementDesynced;
	private boolean riding;
	private boolean brokenVehicle;
	private boolean ridingUncertain;
	private int vehicleId;
	private Entity vehicle;
	private int lastUnmount;
	public final List<Pair<Integer, Integer>> exemptMap = new ArrayList<>();
	public final Map<SubCategory, Pair<Integer, Integer>> exemptCategoryMap = new HashMap<>();
	private double cps;
	private double lastCps;
	private double highestCps;
	private double highestReach;
	private boolean didFlagMovement;
	private int lastMovementFlag;
	private CustomLocation safeSetback;
	private CustomLocation safeGroundSetback;
	private CustomLocation flyCancel;
	private CustomLocation teleportLocation;
	private long lastLocationUpdate;
	public int invalidMovementTicks;
	private int sensitivity = -1;
	private float sensitivityY = -1.0F;
	private float sensitivityX = -1.0F;
	private float smallestRotationGCD;
	private float pitchGCD = 9999.0F;
	private float yawGCD;
	private float predictPitch;
	private float predictYaw;
	public Vec3 eyeLocation;
	public Vec3 look;
	public Vec3 lookMouseDelayFix;
	public boolean locationInited;
	public boolean boundingBoxInited;
	public int locationInitedAt;
	public long createdAt;
	public long transactionClock;
	private int currentSlot;
	private boolean pendingBackSwitch;
	public final Map<Integer, Deque<Integer>> backSwitchSlots = new HashMap<>();
	private boolean timerKicked = false;
	private Thread thread;
	private boolean gliding = false;
	private boolean riptiding = false;
	private boolean spectating = false;
	private int lastGlide = 0;
	private int lastRiptide = 0;
	public int dolphinLevel = 0;
	public int soulSpeedLevel = 0;
	public int depthStriderLevel = 0;
	public int slowFallingLevel = 0;
	public int levitationLevel = 0;

	public KarhuPlayer(UUID uuid, Karhu karhu, long now) {
		this.createdAt = now;
		this.forceRunCollisions = true;
		this.karhuWorld = new KarhuWorld(this);
		this.thread = Karhu.getInstance().getThreadManager().generate();
		this.teleportManager = new TeleportManager(this);
		this.abilityManager = new AbilityManager(this);
		this.worldTracker = new WorldTracker(this);
		this.bukkitPlayer = Bukkit.getPlayer(uuid);
		this.entityId = this.bukkitPlayer.getEntityId();
		this.transactionId = -32768;
		this.checkViolationTimes = new HashMap<>();
		this.checkVlMap = new HashMap<>();
		this.location = new CustomLocation(0.0, 0.0, 0.0, 0.0F, 0.0F);
		this.lastLocation = this.location;
		this.lastLastLocation = this.lastLocation;
		this.lastLastLastLocation = this.lastLastLocation;
		this.checkManager = new CheckManager(this, karhu);
		this.gameMode = GameMode.getById(this.bukkitPlayer.getGameMode().getValue());
		User user = this.bukkitPlayer != null ? PacketEvents.getAPI().getPlayerManager().getUser(this.bukkitPlayer) : null;
		if (user != null) {
			this.updateClientVersion(user.getClientVersion());
			this.user = user;
		}

		this.collisionHandler = new CollisionHandler(this);
		this.desyncedBlockHandler = new DesyncedBlockHandler(this);
		this.movementHandler = new MovementHandler(this);
		this.netHandler = new NetHandler(this);
		this.crashHandler = new CrashHandler(this);
		this.vehicleHandler = new VehicleHandler(this);
		this.wrappedEntity = new WrappedEntityPlayer(this);
		this.placeManager = new PlaceManager(this);
		this.teleportManager = new TeleportManager(this);
		this.effectManager = new EffectManager(this);
		this.inUnloadedChunk = true;
		BoundingBox BB = new BoundingBox(this, this.location.x - 0.3, this.location.y, this.location.z - 0.3, this.location.x + 0.3, this.location.y + 1.8, this.location.z + 0.3);
		this.boundingBox = BB.clone();
		this.lastBoundingBox = BB.clone();
		this.mcpCollision = BB.clone();
		this.confirmedVersion = false;
		this.locationInited = false;
		this.boundingBoxInited = false;
		this.lastJoinTime = System.currentTimeMillis();
		this.uuid = uuid;
		this.serverTick = Karhu.getInstance().getServerTick();
		this.createdOnTick = Karhu.getInstance().getServerTick();
		this.objectLoaded = true;
	}

	public void tick() {
		this.tasksManager.doTasks();
	}

	public int getViolations(Check<?> check, Long time) {
		Set<Long> timestamps = this.checkViolationTimes.get(check);
		long sys = System.currentTimeMillis();
		int vl = 0;
		if (timestamps != null) {
			for (Long man : timestamps) {
				if (sys - man <= time) {
					++vl;
				} else {
					timestamps.remove(man);
				}
			}

			return vl;
		} else {
			return 0;
		}
	}

	public void addViolation(Check<?> check) {
		Set<Long> timestamps = this.checkViolationTimes.get(check);
		if (timestamps == null) {
			timestamps = ConcurrentHashMap.newKeySet();
		}

		timestamps.add(System.currentTimeMillis());
		this.checkViolationTimes.put(check, timestamps);
	}

	public void addViolations(Check<?> check, int vl) {
		long sys = System.currentTimeMillis();

		for (int i = 0; i < vl; ++i) {
			Set<Long> timestamps = this.checkViolationTimes.get(check);
			if (timestamps == null) {
				timestamps = ConcurrentHashMap.newKeySet();
			}

			timestamps.add(sys + (long)i);
			this.checkViolationTimes.put(check, timestamps);
		}
	}

	public double getCheckVl(Check<?> check) {
		if (!this.checkVlMap.containsKey(check)) {
			this.checkVlMap.put(check, 0.0);
		}

		return this.checkVlMap.get(check);
	}

	public void setCheckVl(double vl, Check<?> check) {
		if (vl < 0.0) {
			vl = 0.0;
		}

		this.checkVlMap.put(check, vl);
	}

	public short getNextTransactionId() {
		++this.transactionId;
		if (this.transactionId > -20001) {
			this.transactionId = -32768;
		}

		return this.transactionId;
	}

	public short getNextTransactionIdSilent() {
		short predict = this.transactionId;
		if (++predict > -20001) {
			predict = -32768;
		}

		return predict;
	}

	public void sendTransaction(Consumer<Short> callback) {
		short id = this.getNextTransactionId();
		this.scheduledTransactions.put(id, callback);
		PlayerUtil.sendPacket(this.bukkitPlayer, id);
	}

	public short sendTransaction() {
		short id = this.getNextTransactionId();
		PlayerUtil.sendPacket(this.bukkitPlayer, id);
		return id;
	}

	public void useOldTransaction(Consumer<Short> callback, short uid) {
		ObjectArrayList<Consumer<Short>> map = this.waitingConfirms.computeIfAbsent(uid, k -> new ObjectArrayList());
		map.add(callback);
		this.waitingConfirms.put(uid, map);
	}

	public void queueToPrePing(Callback<Integer> callback) {
		this.netHandler.queueToPrePing(callback);
	}

	public void queueToPostPing(Callback<Integer> callback) {
		this.netHandler.queueToPostPing(callback);
	}

	public void queueToFlying(int delay, Callback<Integer> callback) {
		int key = this.totalTicks + delay;
		if (this.tasks.containsKey(key)) {
			this.tasks.get(key).addTask(callback);
		} else {
			this.tasks.put(key, new TaskData(key, callback));
		}
	}

	public int mostRecentPing() {
		return this.netHandler.mostRecentPing();
	}

	public boolean hasExempt() {
		return this.exemptMap.size() > 0 || this.exemptCategoryMap.size() > 0;
	}

	public void updateClientVersion(ClientVersion version) {
		if (version == null) {
			version = ClientVersion.getById(Karhu.SERVER_VERSION.getProtocolVersion());
		}

		this.clientVersion = version;
		this.isNewerThan8 = version.isNewerThan(ClientVersion.V_1_8);
		this.isNewerThan12 = version.isNewerThan(ClientVersion.V_1_12_2);
		this.isNewerThan13 = version.isNewerThan(ClientVersion.V_1_13_2);
		this.isNewerThan16 = version.isNewerThan(ClientVersion.V_1_16_4);
	}

	public ClientVersion getClientVersion() {
		return this.clientVersion == null ? ClientVersion.getById(Karhu.SERVER_VERSION.getProtocolVersion()) : this.clientVersion;
	}

	public boolean isBadClientVersion() {
		return this.clientVersion == null || this.clientVersion == ClientVersion.UNKNOWN;
	}

	public void handleKickAlert(String type) {
		if (!this.kicked) {
			Tasker.run(() -> this.bukkitPlayer.kickPlayer(Karhu.getInstance().getConfigManager().getAnticrashKickMsg()));
			MiscellaneousAlertPoster.postMisc(
				Karhu.getInstance().getConfigManager().getAntiCrashMessage().replaceAll("%debug%", type).replaceAll("%player%", this.bukkitPlayer.getName()), this, "Crash"
			);
			Karhu.getInstance().getPlug().getLogger().warning("-----------------Karhu Anticrash-----------------");
			Karhu.getInstance().getPlug().getLogger().warning(this.bukkitPlayer.getName() + " was kicked for suspicious packets (" + type + ")");
			Karhu.getInstance().getPlug().getLogger().warning("Keep an eye on the player!");
			Karhu.getInstance().getPlug().getLogger().warning("-----------------Karhu Anticrash-----------------");
			this.kicked = true;
		}
	}

	@Deprecated
	public boolean recentlyTeleported(int ticks) {
		return this.totalTicks - this.lastTeleport <= ticks;
	}

	@Deprecated
	public boolean couldBeTeleporting(int ticks) {
		return this.totalTicks - this.lastTeleport <= ticks || this.isPossiblyTeleporting();
	}

	@Deprecated
	public boolean couldBeUnloadedClient() {
		return this.elapsed(this.getLastInUnloadedChunk()) <= MathUtil.getPingInTicks(this.getTransactionPing()) + 2
			|| this.elapsed(this.getLastWorldChange()) <= MathUtil.getPingInTicks(this.getTransactionPing()) + 10
			|| this.elapsed(this.getLastPossibleInUnloadedChunk()) <= 2;
	}

	public boolean hasFast() {
		return this.lastFlying != 0L && this.lastFast != 0L && (double)(this.lastFlying - this.lastFast) / 1000000.0 < 100.0;
	}

	public boolean isLagging(int ticks) {
		return ticks - this.lastDroppedPackets < 2;
	}

	public boolean isLagging(int ticks, int time) {
		return ticks - this.lastDroppedPackets < time;
	}

	public int elapsed(int i) {
		return this.totalTicks - i == this.totalTicks ? 1000 : this.totalTicks - i;
	}

	public boolean isHasDig() {
		return this.elapsed(this.digStopTicks) <= 8 || this.elapsed(this.digTicks) <= 3 || this.digging;
	}

	public boolean isHasDig2() {
		return this.elapsed(this.digStopTicks) <= 8 || this.digging;
	}

	public long elapsedMS(long i) {
		return System.currentTimeMillis() - i;
	}

	public long elapsedMS(long now, long time) {
		return (long)((double)(now - time) / 1000000.0);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public void velocityTick(Vector vector) {
		this.setLastVelocityTaken(this.getTotalTicks());
		this.setVelocityXZTicks(0);
		this.setVelocityYTicks(0);
		this.setVelocityX(vector.getX());
		this.setVelocityY(vector.getY());
		this.setVelocityZ(vector.getZ());
		int velocityH = (int)Math.ceil((Math.abs(vector.getX()) + Math.abs(vector.getZ())) / 2.0 + 2.0) * 4;
		int velocityV = (int)Math.ceil(FastMath.pow(Math.abs(vector.getY()) + 2.0, 2)) * 2;
		this.setMaxVelocityXZTicks(velocityH + velocityV + 5);
		this.setMaxVelocityYTicks(velocityV);
		this.setTakingVertical(true);
		this.setVelocityHorizontal(MathUtil.hypot(vector.getX(), vector.getZ()));
		this.setTickedVelocity(vector.clone());
		this.setConfirmingVelocity(false);
		this.setNeedExplosionAdditions(false);
	}

	public void checkVelocity() {
		for (Entry<Integer, ConcurrentLinkedDeque<VelocityPending>> myMapEntry : this.velocityPending.entrySet()) {
			for (VelocityPending velocityCheck : myMapEntry.getValue()) {
				Vector velocity = velocityCheck.getVelocity();
				if (!velocityCheck.isMarkedSent()) {
					long nanoStart = System.nanoTime();
					double ogHz = MathUtil.hypot(velocity.getX(), velocity.getZ());
					double min = this.getClientVersion().getProtocolVersion() > 47 ? 0.003 : 0.005;
					if (this.attacks != 0) {
						Pair<Double, Double> kbs = NMSValueParser.bruteforceAttack(this, velocity.getX(), velocity.getZ());
						double kbY = Math.abs(velocity.getY()) < min ? 0.0 : velocity.getY();
						Vector knockbackVector = new Vector(kbs.getX(), kbY, kbs.getY());
						Vector playerVector = new Vector(this.deltas.deltaX, this.deltas.motionY, this.deltas.deltaZ);
						double horizontalDistance = MathUtil.horizontalDistance(knockbackVector, playerVector);
						double verticalDistance = MathUtil.verticalDistance(knockbackVector, playerVector);
						double precisionY = this.elapsed(this.lastCollidedV) <= 2 ? 0.205 : 0.03125;
						double precisionH = this.elapsed(this.lastCollidedH) <= 2 ? Math.max(ogHz, horizontalDistance) : 0.03125;
						if (horizontalDistance <= precisionH && verticalDistance <= precisionY) {
							this.velocityTick(velocity);
							velocityCheck.markSent();
						}
					} else {
						double kbToUseX = velocity.getX();
						double kbToUseY = velocity.getY();
						double kbToUseZ = velocity.getZ();
						double kbX = Math.abs(kbToUseX) < min ? 0.0 : kbToUseX;
						double kbY = Math.abs(kbToUseY) < min ? 0.0 : kbToUseY;
						double kbZ = Math.abs(kbToUseZ) < min ? 0.0 : kbToUseZ;
						Pair<Double, Double> sheesh = NMSValueParser.loopKeysGetKeys(this, kbX, kbZ);
						Vector knockbackVector = new Vector(sheesh.getX(), kbY, sheesh.getY());
						Vector playerVector = new Vector(this.deltas.deltaX, this.deltas.motionY, this.deltas.deltaZ);
						double horizontalDistance = MathUtil.horizontalDistance(knockbackVector, playerVector);
						double verticalDistance = MathUtil.verticalDistance(knockbackVector, playerVector);
						double precisionY = this.elapsed(this.lastCollidedV) <= 2 ? 0.43F : 0.03125;
						double precisionH = this.elapsed(this.lastCollidedH) <= 2 ? Math.max(ogHz, horizontalDistance) : 0.031;
						if (horizontalDistance <= precisionH && (verticalDistance <= precisionY || this.isJumped())) {
							this.velocityTick(velocity);
							velocityCheck.markSent();
						}
					}

					long nanoStop = System.nanoTime();
					Benchmark profileData = KarhuBenchmarker.getProfileData(BenchmarkType.PHYSICS_SIMULATOR);
					profileData.insertResult(nanoStart, nanoStop);
				}
			}
		}
	}

	public double offsetMove() {
		return this.clientVersion.isNewerThanOrEquals(ClientVersion.V_1_18_2) ? 2.0E-4 : 0.03;
	}

	public double clamp() {
		return this.clientVersion.getProtocolVersion() > 47 ? 0.003 : 0.005;
	}

	public ConcurrentLinkedDeque<VelocityPending> getTickVelocities(int transactionId) {
		return this.velocityPending.get(transactionId);
	}

	public ItemStack getStackInHand() {
		if (this.bukkitPlayer == null) {
			return new ItemStack(Material.AIR);
		} else {
			ItemStack stack = this.bukkitPlayer.getInventory().getItem(this.currentSlot);
			return stack == null ? new ItemStack(Material.AIR) : stack;
		}
	}

	public boolean isInitialized() {
		return this.bukkitPlayer != null;
	}

	public String getName() {
		return this.bukkitPlayer == null ? "null_player" : this.bukkitPlayer.getName();
	}

	public boolean isFlyingBukkit() {
		return this.bukkitPlayer == null ? false : this.bukkitPlayer.isFlying();
	}

	public boolean isAllowFlyingBukkit() {
		return this.bukkitPlayer == null ? false : this.bukkitPlayer.getAllowFlight();
	}

	public World getWorld() {
		return this.bukkitPlayer == null ? this.findWorld() : this.bukkitPlayer.getWorld();
	}

	public void teleport(Location location) {
		if (this.bukkitPlayer != null && this.getWorld() == location.getWorld()) {
			this.invalidMovementTicks = 0;
			this.bukkitPlayer.teleport(location);
		}
	}

	public void closeInventory() {
		if (this.bukkitPlayer != null) {
			this.bukkitPlayer.closeInventory();
			this.queueToPrePing(task -> this.inventoryOpen = false);
		}
	}

	public void teleport(CustomLocation location) {
		if (this.bukkitPlayer != null) {
			this.invalidMovementTicks = 0;
			this.bukkitPlayer.teleport(location.toLocation(this.getWorld()));
		}
	}

	private World findWorld() {
		if (this.entityId != -1) {
			for (World world : Bukkit.getWorlds()) {
				for (Entity entity : SpigotReflectionUtil.getEntityList(world)) {
					if (entity.getEntityId() == this.entityId) {
						return world;
					}
				}
			}
		}

		return Bukkit.getWorld(((World)Bukkit.getWorlds().get(0)).getUID());
	}

	public List<Double> getEyePositions() {
		if (this.isNewerThan13) {
			return this.eyesFourteen;
		} else {
			return this.isNewerThan8 ? this.eyesNine : this.eyesLegacy;
		}
	}

	public boolean isObjectLoaded() {
		return this.objectLoaded;
	}

	public boolean isRemovingObject() {
		return this.removingObject;
	}

	public boolean isConfigChecked() {
		return this.configChecked;
	}

	public IVehicleHandler getVehicleHandler() {
		return this.vehicleHandler;
	}

	public boolean isKicked() {
		return this.kicked;
	}

	public CustomLocation getLocation() {
		return this.location;
	}

	public CustomLocation getLastLocation() {
		return this.lastLocation;
	}

	public CustomLocation getLastLastLocation() {
		return this.lastLastLocation;
	}

	public CustomLocation getLastLastLastLocation() {
		return this.lastLastLastLocation;
	}

	public boolean isForceRunCollisions() {
		return this.forceRunCollisions;
	}

	public boolean isPlacedInside() {
		return this.placedInside;
	}

	public boolean isPlacedCancel() {
		return this.placedCancel;
	}

	public int getLastPlacedInside() {
		return this.lastPlacedInside;
	}

	public int getPredictionTicks() {
		return this.predictionTicks;
	}

	public int getMoveTicks() {
		return this.moveTicks;
	}

	public int getNoMoveTicks() {
		return this.noMoveTicks;
	}

	public PlaceManager getPlaceManager() {
		return this.placeManager;
	}

	public Map<Integer, TaskData> getTasks() {
		return this.tasks;
	}

	public CheckManager getCheckManager() {
		return this.checkManager;
	}

	public Map<Check, Set<Long>> getCheckViolationTimes() {
		return this.checkViolationTimes;
	}

	public Map<Check, Double> getCheckVlMap() {
		return this.checkVlMap;
	}

	public boolean isCancelNextHitR() {
		return this.cancelNextHitR;
	}

	public boolean isCancelNextHitH() {
		return this.cancelNextHitH;
	}

	public boolean isReduceNextDamage() {
		return this.reduceNextDamage;
	}

	public boolean isCancelBreak() {
		return this.cancelBreak;
	}

	public boolean isForceCancelReach() {
		return this.forceCancelReach;
	}

	public boolean isCancelTripleHit() {
		return this.cancelTripleHit;
	}

	public boolean isAbusingVelocity() {
		return this.abusingVelocity;
	}

	public int getEntityIdCancel() {
		return this.entityIdCancel;
	}

	public int getCancelHitsTick() {
		return this.cancelHitsTick;
	}

	public float getJumpMovementFactor() {
		return this.jumpMovementFactor;
	}

	public float getSpeedInAir() {
		return this.speedInAir;
	}

	public float getLastJumpMovementFactor() {
		return this.lastJumpMovementFactor;
	}

	public float getAttributeSpeed() {
		return this.attributeSpeed;
	}

	public float getLastAttributeSpeed() {
		return this.lastAttributeSpeed;
	}

	public float getWalkSpeed() {
		return this.walkSpeed;
	}

	public float getJumpFactor() {
		return this.jumpFactor;
	}

	public boolean isJumpedCurrentTick() {
		return this.jumpedCurrentTick;
	}

	public boolean isJumpedLastTick() {
		return this.jumpedLastTick;
	}

	public boolean isJumped() {
		return this.jumped;
	}

	public String getPressedKey() {
		return this.pressedKey;
	}

	public Block getBlockBelow() {
		return this.blockBelow;
	}

	public Block getBlockInside() {
		return this.blockInside;
	}

	public Block getLastBlockInside() {
		return this.lastBlockInside;
	}

	public boolean isExitingVehicle() {
		return this.exitingVehicle;
	}

	public String getKeyCombo() {
		return this.keyCombo;
	}

	public boolean isNewerThan8() {
		return this.isNewerThan8;
	}

	public boolean isNewerThan12() {
		return this.isNewerThan12;
	}

	public boolean isNewerThan13() {
		return this.isNewerThan13;
	}

	public boolean isNewerThan16() {
		return this.isNewerThan16;
	}

	public boolean isConfirmedVersion() {
		return this.confirmedVersion;
	}

	public boolean isConfirmedVersion2() {
		return this.confirmedVersion2;
	}

	public boolean isViaMCP() {
		return this.isViaMCP;
	}

	public KarhuHandler getCollisionHandler() {
		return this.collisionHandler;
	}

	public DesyncedBlockHandler getDesyncedBlockHandler() {
		return this.desyncedBlockHandler;
	}

	public MovementHandler getMovementHandler() {
		return this.movementHandler;
	}

	public AbstractPredictionHandler getPredictionHandler() {
		return this.predictionHandler;
	}

	public NetHandler getNetHandler() {
		return this.netHandler;
	}

	public ICrashHandler getCrashHandler() {
		return this.crashHandler;
	}

	public WrappedEntityPlayer getWrappedEntity() {
		return this.wrappedEntity;
	}

	public boolean isHasReceivedTransaction() {
		return this.hasReceivedTransaction;
	}

	public boolean isHasReceivedKeepalive() {
		return this.hasReceivedKeepalive;
	}

	public boolean isHasTeleportedOnce() {
		return this.isHasTeleportedOnce;
	}

	public int getSentConfirms() {
		return this.sentConfirms;
	}

	public int getReceivedConfirms() {
		return this.receivedConfirms;
	}

	public Short2ObjectMap<Consumer<Short>> getScheduledTransactions() {
		return this.scheduledTransactions;
	}

	public Map<Short, ObjectArrayList<Consumer<Short>>> getWaitingConfirms() {
		return this.waitingConfirms;
	}

	public short getTransactionId() {
		return this.transactionId;
	}

	public short getConfirmId() {
		return this.confirmId;
	}

	public short getTickTransactionId() {
		return this.tickTransactionId;
	}

	public int getTickFirstConfirmationUid() {
		return this.tickFirstConfirmationUid;
	}

	public int getTickSecondConfirmationUid() {
		return this.tickSecondConfirmationUid;
	}

	public int getLastTickFirstConfirmationUid() {
		return this.lastTickFirstConfirmationUid;
	}

	public int getLastTickSecondConfirmationUid() {
		return this.lastTickSecondConfirmationUid;
	}

	public boolean isHasSentTickFirst() {
		return this.hasSentTickFirst;
	}

	public boolean isSendingPledgePackets() {
		return this.sendingPledgePackets;
	}

	public boolean isHasSentFirst() {
		return this.hasSentFirst;
	}

	public boolean isBrokenVelocityVerify() {
		return this.brokenVelocityVerify;
	}

	public TaskManager<KarhuPlayer> getTasksManager() {
		return this.tasksManager;
	}

	public short getLastPostId() {
		return this.lastPostId;
	}

	public Map<Integer, ConcurrentLinkedDeque<VelocityPending>> getVelocityPending() {
		return this.velocityPending;
	}

	public boolean isTakingVertical() {
		return this.takingVertical;
	}

	public boolean isConfirmingVelocity() {
		return this.confirmingVelocity;
	}

	public boolean isNeedExplosionAdditions() {
		return this.needExplosionAdditions;
	}

	public int getLastVelocityYReset() {
		return this.lastVelocityYReset;
	}

	public int getLastVelocityXZReset() {
		return this.lastVelocityXZReset;
	}

	public int getLastVelocityTaken() {
		return this.lastVelocityTaken;
	}

	public int getVelocityYTicks() {
		return this.velocityYTicks;
	}

	public int getMaxVelocityXZTicks() {
		return this.maxVelocityXZTicks;
	}

	public int getMaxVelocityYTicks() {
		return this.maxVelocityYTicks;
	}

	public int getExplosionExempt() {
		return this.explosionExempt;
	}

	public double getVelocityX() {
		return this.velocityX;
	}

	public double getVelocityY() {
		return this.velocityY;
	}

	public double getVelocityZ() {
		return this.velocityZ;
	}

	public double getVelocityHorizontal() {
		return this.velocityHorizontal;
	}

	public double getConfirmingY() {
		return this.confirmingY;
	}

	public Vector getTickedVelocity() {
		return this.tickedVelocity;
	}

	public boolean isPlayerVelocityCalled() {
		return this.playerVelocityCalled;
	}

	public boolean isPlayerExplodeCalled() {
		return this.playerExplodeCalled;
	}

	public int getTotalTicks() {
		return this.totalTicks;
	}

	public int getLastSprintTick() {
		return this.lastSprintTick;
	}

	public int getLastSneakTick() {
		return this.lastSneakTick;
	}

	public int getAirTicks() {
		return this.airTicks;
	}

	public int getDigTicks() {
		return this.digTicks;
	}

	public int getFastDigTicks() {
		return this.fastDigTicks;
	}

	public int getDigStopTicks() {
		return this.digStopTicks;
	}

	public int getPlaceTicks() {
		return this.placeTicks;
	}

	public int getUnderPlaceTicks() {
		return this.underPlaceTicks;
	}

	public int getBucketTicks() {
		return this.bucketTicks;
	}

	public int getSpoofPlaceTicks() {
		return this.spoofPlaceTicks;
	}

	public int getUseTicks() {
		return this.useTicks;
	}

	public int getLClientAirTicks() {
		return this.lClientAirTicks;
	}

	public int getClientAirTicks() {
		return this.clientAirTicks;
	}

	public int getLastFlyTick() {
		return this.lastFlyTick;
	}

	public int getLastAllowFlyTick() {
		return this.lastAllowFlyTick;
	}

	public int getElapsedOnLiquid() {
		return this.elapsedOnLiquid;
	}

	public int getElapsedUnderBlock() {
		return this.elapsedUnderBlock;
	}

	public int getElapsedSinceBridgePlace() {
		return this.elapsedSinceBridgePlace;
	}

	public int getServerGroundTicks() {
		return this.serverGroundTicks;
	}

	public int getClientGroundTicks() {
		return this.clientGroundTicks;
	}

	public int getWeirdTicks() {
		return this.weirdTicks;
	}

	public int getLastInBerry() {
		return this.lastInBerry;
	}

	public int getLastInUnloadedChunk() {
		return this.lastInUnloadedChunk;
	}

	public int getLastInLiquid() {
		return this.lastInLiquid;
	}

	public int getLastOnSlime() {
		return this.lastOnSlime;
	}

	public int getLastOnSoul() {
		return this.lastOnSoul;
	}

	public int getLastOnIce() {
		return this.lastOnIce;
	}

	public int getLastOnClimbable() {
		return this.lastOnClimbable;
	}

	public int getLastOnBed() {
		return this.lastOnBed;
	}

	public int getLastInWeb() {
		return this.lastInWeb;
	}

	public int getLastCollided() {
		return this.lastCollided;
	}

	public int getLastCollidedWithEntity() {
		return this.lastCollidedWithEntity;
	}

	public int getLastOnHalfBlock() {
		return this.lastOnHalfBlock;
	}

	public int getLastCollidedV() {
		return this.lastCollidedV;
	}

	public int getLastCollidedVGhost() {
		return this.lastCollidedVGhost;
	}

	public int getLastOnBoat() {
		return this.lastOnBoat;
	}

	public int getLastInPowder() {
		return this.lastInPowder;
	}

	public int getLastPossibleInUnloadedChunk() {
		return this.lastPossibleInUnloadedChunk;
	}

	public int getLastCollidedGhost() {
		return this.lastCollidedGhost;
	}

	public int getPositionPackets() {
		return this.positionPackets;
	}

	public int getLastInLiquidOffset() {
		return this.lastInLiquidOffset;
	}

	public int getLastConfirmingState() {
		return this.lastConfirmingState;
	}

	public int getLastCollidedH() {
		return this.lastCollidedH;
	}

	public int getLastSneakEdge() {
		return this.lastSneakEdge;
	}

	public int getLastFence() {
		return this.lastFence;
	}

	public boolean isDigging() {
		return this.digging;
	}

	public boolean isDiggingBasic() {
		return this.diggingBasic;
	}

	public boolean isTraceDigging() {
		return this.traceDigging;
	}

	public boolean isPlacing() {
		return this.placing;
	}

	public boolean isWasPlacing() {
		return this.wasPlacing;
	}

	public boolean isSkipNextSwing() {
		return this.skipNextSwing;
	}

	public boolean isNoMoveNextFlying() {
		return this.noMoveNextFlying;
	}

	public boolean isCollidedHorizontally() {
		return this.collidedHorizontally;
	}

	public boolean isWasCollidedHorizontally() {
		return this.wasCollidedHorizontally;
	}

	public boolean isCollidedHorizontalClient() {
		return this.collidedHorizontalClient;
	}

	public boolean isCollidedWithFence() {
		return this.collidedWithFence;
	}

	public boolean isEdgeOfFence() {
		return this.edgeOfFence;
	}

	public boolean isCollidedWithPane() {
		return this.collidedWithPane;
	}

	public boolean isCollidedWithCactus() {
		return this.collidedWithCactus;
	}

	public boolean isInsideTrapdoor() {
		return this.insideTrapdoor;
	}

	public boolean isInsideBlock() {
		return this.insideBlock;
	}

	public boolean isWasFullyInsideBlock() {
		return this.wasFullyInsideBlock;
	}

	public boolean isFullyInsideBlock() {
		return this.fullyInsideBlock;
	}

	public boolean isInWeb() {
		return this.inWeb;
	}

	public boolean isWasInWeb() {
		return this.isWasInWeb;
	}

	public boolean isWasWasInWeb() {
		return this.isWasWasInWeb;
	}

	public boolean isBlocking() {
		return this.blocking;
	}

	public boolean isShit() {
		return this.shit;
	}

	public boolean isBowing() {
		return this.bowing;
	}

	public boolean isFinalCollidedH() {
		return this.finalCollidedH;
	}

	public Vector getLastAbortLoc() {
		return this.lastAbortLoc;
	}

	public int getLastPistonPush() {
		return this.lastPistonPush;
	}

	public int getLastSlimePistonPush() {
		return this.lastSlimePistonPush;
	}

	public int getMoveCalls() {
		return this.moveCalls;
	}

	public long getServerTick() {
		return this.serverTick;
	}

	public long getCreatedOnTick() {
		return this.createdOnTick;
	}

	public BoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	public BoundingBox getMcpCollision() {
		return this.mcpCollision;
	}

	public BoundingBox getLastBoundingBox() {
		return this.lastBoundingBox;
	}

	public double getVehicleX() {
		return this.vehicleX;
	}

	public double getVehicleY() {
		return this.vehicleY;
	}

	public double getVehicleZ() {
		return this.vehicleZ;
	}

	public int getTicksOnGhostBlock() {
		return this.ticksOnGhostBlock;
	}

	public int getTicksOnBlockHandlerNotEnabled() {
		return this.ticksOnBlockHandlerNotEnabled;
	}

	public int getUpdateBuf() {
		return this.updateBuf;
	}

	public LivingEntity getLastTarget() {
		return this.lastTarget;
	}

	public List<Integer> getLastTargets() {
		return this.lastTargets;
	}

	public int getLastAttackTick() {
		return this.lastAttackTick;
	}

	public int getAttacks() {
		return this.attacks;
	}

	public long getLastAttackPacket() {
		return this.lastAttackPacket;
	}

	public boolean isAttackedSinceVelocity() {
		return this.attackedSinceVelocity;
	}

	public boolean isReachBypass() {
		return this.reachBypass;
	}

	public boolean isSprinting() {
		return this.sprinting;
	}

	public boolean isWasSprinting() {
		return this.wasSprinting;
	}

	public boolean isWasWasSprinting() {
		return this.wasWasSprinting;
	}

	public boolean isSneaking() {
		return this.sneaking;
	}

	public boolean isWasSneaking() {
		return this.wasSneaking;
	}

	public boolean isWasWasSneaking() {
		return this.wasWasSneaking;
	}

	public boolean isUsingItem() {
		return this.usingItem;
	}

	public boolean isLastUsingItem() {
		return this.lastUsingItem;
	}

	public boolean isEating() {
		return this.eating;
	}

	public boolean isLastEating() {
		return this.lastEating;
	}

	public boolean isRecorrectingSprint() {
		return this.recorrectingSprint;
	}

	public boolean isDesyncSprint() {
		return this.desyncSprint;
	}

	public boolean isInventoryOpen() {
		return this.inventoryOpen;
	}

	public boolean isCrouching() {
		return this.crouching;
	}

	public boolean isInBed() {
		return this.inBed;
	}

	public boolean isLastInBed() {
		return this.lastInBed;
	}

	public Vec3 getBedPos() {
		return this.bedPos;
	}

	public int getInvStamp() {
		return this.invStamp;
	}

	public int getSlotSwitchTick() {
		return this.slotSwitchTick;
	}

	public int getLastWorldChange() {
		return this.lastWorldChange;
	}

	public boolean isCinematic() {
		return this.cinematic;
	}

	public int getLastCinematic() {
		return this.lastCinematic;
	}

	public Deltas getDeltas() {
		return this.deltas;
	}

	public float getFallDistance() {
		return this.fallDistance;
	}

	public float getLFallDistance() {
		return this.lFallDistance;
	}

	public ConcurrentHashMap<Integer, EntityData> getEntityData() {
		return this.entityData;
	}

	public int getLastPos() {
		return this.lastPos;
	}

	public double getAttackerX() {
		return this.attackerX;
	}

	public double getAttackerY() {
		return this.attackerY;
	}

	public double getAttackerZ() {
		return this.attackerZ;
	}

	public float getAttackerYaw() {
		return this.attackerYaw;
	}

	public float getAttackerPitch() {
		return this.attackerPitch;
	}

	public int getTeleports() {
		return this.teleports;
	}

	public int getAddedTeleports() {
		return this.addedTeleports;
	}

	public List<Double> getEyesFourteen() {
		return this.eyesFourteen;
	}

	public List<Double> getEyesNine() {
		return this.eyesNine;
	}

	public List<Double> getEyesLegacy() {
		return this.eyesLegacy;
	}

	public boolean isOnGroundServer() {
		return this.onGroundServer;
	}

	public boolean isOnBoat() {
		return this.onBoat;
	}

	public boolean isGroundNearBox() {
		return this.groundNearBox;
	}

	public boolean isWasOnGroundServer() {
		return this.wasOnGroundServer;
	}

	public boolean isOnWater() {
		return this.onWater;
	}

	public boolean isWasOnWater() {
		return this.wasOnWater;
	}

	public boolean isAboveButNotInWater() {
		return this.aboveButNotInWater;
	}

	public boolean isWaterAlmostOnFeet() {
		return this.waterAlmostOnFeet;
	}

	public boolean isOnLava() {
		return this.onLava;
	}

	public boolean isWasOnLava() {
		return this.wasOnLava;
	}

	public boolean isOnIce() {
		return this.onIce;
	}

	public boolean isOnLiquid() {
		return this.onLiquid;
	}

	public boolean isOnSlab() {
		return this.onSlab;
	}

	public boolean isWasOnSlab() {
		return this.wasOnSlab;
	}

	public boolean isOnDoor() {
		return this.onDoor;
	}

	public boolean isWasOnDoor() {
		return this.wasOnDoor;
	}

	public boolean isOnFence() {
		return this.onFence;
	}

	public boolean isWasOnFence() {
		return this.wasOnFence;
	}

	public boolean isOnStairs() {
		return this.onStairs;
	}

	public boolean isWasOnStairs() {
		return this.wasOnStairs;
	}

	public boolean isOnBed() {
		return this.onBed;
	}

	public boolean isWasOnBed() {
		return this.wasOnBed;
	}

	public boolean isUnderBlock() {
		return this.underBlock;
	}

	public boolean isUnderBlockStrict() {
		return this.underBlockStrict;
	}

	public boolean isWasUnderBlock() {
		return this.wasUnderBlock;
	}

	public boolean isUnderWeb() {
		return this.underWeb;
	}

	public boolean isOnWeb() {
		return this.onWeb;
	}

	public boolean isOnSoulsand() {
		return this.onSoulsand;
	}

	public boolean isWasOnSoulSand() {
		return this.wasOnSoulSand;
	}

	public boolean isWasSlimeLand() {
		return this.wasSlimeLand;
	}

	public boolean isSlimeLand() {
		return this.slimeLand;
	}

	public boolean isOnSlime() {
		return this.onSlime;
	}

	public boolean isWasOnSlime() {
		return this.wasOnSlime;
	}

	public boolean isWasWasOnSlime() {
		return this.wasWasOnSlime;
	}

	public boolean isOnCarpet() {
		return this.onCarpet;
	}

	public boolean isOnComparator() {
		return this.onComparator;
	}

	public boolean isWasOnComparator() {
		return this.wasOnComparator;
	}

	public boolean isOnClimbable() {
		return this.onClimbable;
	}

	public boolean isWasOnClimbable() {
		return this.wasOnClimbable;
	}

	public boolean isWasWasOnClimbable() {
		return this.wasWasOnClimbable;
	}

	public boolean isOnLadder() {
		return this.onLadder;
	}

	public boolean isLastLadder() {
		return this.lastLadder;
	}

	public boolean isNearClimbable() {
		return this.nearClimbable;
	}

	public boolean isOnHoney() {
		return this.onHoney;
	}

	public boolean isOnSweet() {
		return this.onSweet;
	}

	public boolean isWasOnHoney() {
		return this.wasOnHoney;
	}

	public boolean isOnScaffolding() {
		return this.onScaffolding;
	}

	public boolean isOnPiston() {
		return this.onPiston;
	}

	public boolean isOnTopGhostBlock() {
		return this.onTopGhostBlock;
	}

	public boolean isAtButton() {
		return this.atButton;
	}

	public boolean isOnWaterOffset() {
		return this.onWaterOffset;
	}

	public boolean isLastOnWaterOffset() {
		return this.lastOnWaterOffset;
	}

	public boolean isInPowder() {
		return this.inPowder;
	}

	public boolean isSneakEdge() {
		return this.sneakEdge;
	}

	public boolean isLastBlockSneak() {
		return this.lastBlockSneak;
	}

	public boolean isAtSign() {
		return this.atSign;
	}

	public Block getMovementBlock() {
		return this.movementBlock;
	}

	public boolean isCollidedWithLivingEntity() {
		return this.collidedWithLivingEntity;
	}

	public float getCurrentFriction() {
		return this.currentFriction;
	}

	public float getLastTickFriction() {
		return this.lastTickFriction;
	}

	public boolean isOnGroundPacket() {
		return this.onGroundPacket;
	}

	public boolean isLastOnGroundPacket() {
		return this.lastOnGroundPacket;
	}

	public boolean isLastLastOnGroundPacket() {
		return this.lastLastOnGroundPacket;
	}

	public boolean isOnGroundMath() {
		return this.onGroundMath;
	}

	public boolean isLastOnGroundMath() {
		return this.lastOnGroundMath;
	}

	public boolean isLastLastOnGroundMath() {
		return this.lastLastOnGroundMath;
	}

	public boolean isOnGhostBlock() {
		return this.onGhostBlock;
	}

	public boolean isUnderGhostBlock() {
		return this.underGhostBlock;
	}

	public boolean isWasUnderGhostBlock() {
		return this.isWasUnderGhostBlock;
	}

	public int getLastInGhostLiquid() {
		return this.lastInGhostLiquid;
	}

	public double getGhostBlockSetbacks() {
		return this.ghostBlockSetbacks;
	}

	public long getPing() {
		return this.ping;
	}

	public long getLastPingTime() {
		return this.lastPingTime;
	}

	public long getTransactionPing() {
		return this.transactionPing;
	}

	public long getLastTransactionPing() {
		return this.lastTransactionPing;
	}

	public Map<Short, Long> getTransactionTime() {
		return this.transactionTime;
	}

	public short getTimerTransactionSent() {
		return this.timerTransactionSent;
	}

	public boolean isInUnloadedChunk() {
		return this.inUnloadedChunk;
	}

	public boolean isWasInUnloadedChunk() {
		return this.wasInUnloadedChunk;
	}

	public boolean isWasWasInUnloadedChunk() {
		return this.wasWasInUnloadedChunk;
	}

	public long getLastTransaction() {
		return this.lastTransaction;
	}

	public long getLastTransactionPingUpdate() {
		return this.lastTransactionPingUpdate;
	}

	public int getBadPingTicks() {
		return this.badPingTicks;
	}

	public int getPingInTicks() {
		return this.pingInTicks;
	}

	public TeleportManager getTeleportManager() {
		return this.teleportManager;
	}

	public boolean isPossiblyTeleporting() {
		return this.possiblyTeleporting;
	}

	public boolean isSeventeenPlacing() {
		return this.seventeenPlacing;
	}

	public int getLastTeleport() {
		return this.lastTeleport;
	}

	public long getLastTeleportPacket() {
		return this.lastTeleportPacket;
	}

	public CustomLocation getFirstChunkMove() {
		return this.firstChunkMove;
	}

	public boolean isJoining() {
		return this.joining;
	}

	public boolean isFuckedTeleport() {
		return this.fuckedTeleport;
	}

	public boolean isBanned() {
		return this.banned;
	}

	public Player getBukkitPlayer() {
		return this.bukkitPlayer;
	}

	public User getUser() {
		return this.user;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public String getBrand() {
		return this.brand;
	}

	public String getCleanBrand() {
		return this.cleanBrand;
	}

	public boolean isBrandPosted() {
		return this.brandPosted;
	}

	public EffectManager getEffectManager() {
		return this.effectManager;
	}

	public int getJumpBoost() {
		return this.jumpBoost;
	}

	public int getCacheBoost() {
		return this.cacheBoost;
	}

	public int getSpeedBoost() {
		return this.speedBoost;
	}

	public int getSlowness() {
		return this.slowness;
	}

	public int getHaste() {
		return this.haste;
	}

	public int getFatigue() {
		return this.fatigue;
	}

	public GameMode getGameMode() {
		return this.gameMode;
	}

	public boolean isAllowFlying() {
		return this.allowFlying;
	}

	public boolean isFlyingS() {
		return this.flyingS;
	}

	public boolean isFlyingC() {
		return this.flyingC;
	}

	public boolean isFlying() {
		return this.flying;
	}

	public boolean isWasFlyingC() {
		return this.wasFlyingC;
	}

	public boolean isInitedFlying() {
		return this.initedFlying;
	}

	public boolean isConfirmingFlying() {
		return this.confirmingFlying;
	}

	public boolean isCorrectedFly() {
		return this.correctedFly;
	}

	public AbilityManager getAbilityManager() {
		return this.abilityManager;
	}

	public WorldTracker getWorldTracker() {
		return this.worldTracker;
	}

	public KarhuWorld getKarhuWorld() {
		return this.karhuWorld;
	}

	public int getLastServerSlot() {
		return this.lastServerSlot;
	}

	public long getLastFlying() {
		return this.lastFlying;
	}

	public long getFlyingTime() {
		return this.flyingTime;
	}

	public long getLastJoinTime() {
		return this.lastJoinTime;
	}

	public int getLastFlyingTicks() {
		return this.lastFlyingTicks;
	}

	public int getVelocityXZTicks() {
		return this.velocityXZTicks;
	}

	public int getLastTransactionTick() {
		return this.lastTransactionTick;
	}

	public int getTrackCount() {
		return this.trackCount;
	}

	public int getCurrentServerTransaction() {
		return this.currentServerTransaction;
	}

	public int getCurrentClientTransaction() {
		return this.currentClientTransaction;
	}

	public int getLastClientTransaction() {
		return this.lastClientTransaction;
	}

	public int getLastLastClientTransaction() {
		return this.lastLastClientTransaction;
	}

	public boolean isFirstTransactionSent() {
		return this.firstTransactionSent;
	}

	public int getLastDroppedPackets() {
		return this.lastDroppedPackets;
	}

	public int getLastPacketDrop() {
		return this.lastPacketDrop;
	}

	public int getHurtTicks() {
		return this.hurtTicks;
	}

	public long getLastFast() {
		return this.lastFast;
	}

	public boolean isMovementDesynced() {
		return this.movementDesynced;
	}

	public boolean isRiding() {
		return this.riding;
	}

	public boolean isBrokenVehicle() {
		return this.brokenVehicle;
	}

	public boolean isRidingUncertain() {
		return this.ridingUncertain;
	}

	public int getVehicleId() {
		return this.vehicleId;
	}

	public Entity getVehicle() {
		return this.vehicle;
	}

	public int getLastUnmount() {
		return this.lastUnmount;
	}

	public List<Pair<Integer, Integer>> getExemptMap() {
		return this.exemptMap;
	}

	public Map<SubCategory, Pair<Integer, Integer>> getExemptCategoryMap() {
		return this.exemptCategoryMap;
	}

	public double getCps() {
		return this.cps;
	}

	public double getLastCps() {
		return this.lastCps;
	}

	public double getHighestCps() {
		return this.highestCps;
	}

	public double getHighestReach() {
		return this.highestReach;
	}

	public boolean isDidFlagMovement() {
		return this.didFlagMovement;
	}

	public int getLastMovementFlag() {
		return this.lastMovementFlag;
	}

	public CustomLocation getSafeSetback() {
		return this.safeSetback;
	}

	public CustomLocation getSafeGroundSetback() {
		return this.safeGroundSetback;
	}

	public CustomLocation getFlyCancel() {
		return this.flyCancel;
	}

	public CustomLocation getTeleportLocation() {
		return this.teleportLocation;
	}

	public long getLastLocationUpdate() {
		return this.lastLocationUpdate;
	}

	public int getInvalidMovementTicks() {
		return this.invalidMovementTicks;
	}

	public int getSensitivity() {
		return this.sensitivity;
	}

	public float getSensitivityY() {
		return this.sensitivityY;
	}

	public float getSensitivityX() {
		return this.sensitivityX;
	}

	public float getSmallestRotationGCD() {
		return this.smallestRotationGCD;
	}

	public float getPitchGCD() {
		return this.pitchGCD;
	}

	public float getYawGCD() {
		return this.yawGCD;
	}

	public float getPredictPitch() {
		return this.predictPitch;
	}

	public float getPredictYaw() {
		return this.predictYaw;
	}

	public Vec3 getEyeLocation() {
		return this.eyeLocation;
	}

	public Vec3 getLook() {
		return this.look;
	}

	public Vec3 getLookMouseDelayFix() {
		return this.lookMouseDelayFix;
	}

	public boolean isLocationInited() {
		return this.locationInited;
	}

	public boolean isBoundingBoxInited() {
		return this.boundingBoxInited;
	}

	public int getLocationInitedAt() {
		return this.locationInitedAt;
	}

	public long getCreatedAt() {
		return this.createdAt;
	}

	public long getTransactionClock() {
		return this.transactionClock;
	}

	public int getCurrentSlot() {
		return this.currentSlot;
	}

	public boolean isPendingBackSwitch() {
		return this.pendingBackSwitch;
	}

	public Map<Integer, Deque<Integer>> getBackSwitchSlots() {
		return this.backSwitchSlots;
	}

	public boolean isTimerKicked() {
		return this.timerKicked;
	}

	public Thread getThread() {
		return this.thread;
	}

	public boolean isGliding() {
		return this.gliding;
	}

	public boolean isRiptiding() {
		return this.riptiding;
	}

	public boolean isSpectating() {
		return this.spectating;
	}

	public int getLastGlide() {
		return this.lastGlide;
	}

	public int getLastRiptide() {
		return this.lastRiptide;
	}

	public int getDolphinLevel() {
		return this.dolphinLevel;
	}

	public int getSoulSpeedLevel() {
		return this.soulSpeedLevel;
	}

	public int getDepthStriderLevel() {
		return this.depthStriderLevel;
	}

	public int getSlowFallingLevel() {
		return this.slowFallingLevel;
	}

	public int getLevitationLevel() {
		return this.levitationLevel;
	}

	public void setObjectLoaded(boolean objectLoaded) {
		this.objectLoaded = objectLoaded;
	}

	public void setRemovingObject(boolean removingObject) {
		this.removingObject = removingObject;
	}

	public void setConfigChecked(boolean configChecked) {
		this.configChecked = configChecked;
	}

	public void setVehicleHandler(IVehicleHandler vehicleHandler) {
		this.vehicleHandler = vehicleHandler;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setKicked(boolean kicked) {
		this.kicked = kicked;
	}

	public void setLocation(CustomLocation location) {
		this.location = location;
	}

	public void setLastLocation(CustomLocation lastLocation) {
		this.lastLocation = lastLocation;
	}

	public void setLastLastLocation(CustomLocation lastLastLocation) {
		this.lastLastLocation = lastLastLocation;
	}

	public void setLastLastLastLocation(CustomLocation lastLastLastLocation) {
		this.lastLastLastLocation = lastLastLastLocation;
	}

	public void setForceRunCollisions(boolean forceRunCollisions) {
		this.forceRunCollisions = forceRunCollisions;
	}

	public void setPlacedInside(boolean placedInside) {
		this.placedInside = placedInside;
	}

	public void setPlacedCancel(boolean placedCancel) {
		this.placedCancel = placedCancel;
	}

	public void setLastPlacedInside(int lastPlacedInside) {
		this.lastPlacedInside = lastPlacedInside;
	}

	public void setPredictionTicks(int predictionTicks) {
		this.predictionTicks = predictionTicks;
	}

	public void setMoveTicks(int moveTicks) {
		this.moveTicks = moveTicks;
	}

	public void setNoMoveTicks(int noMoveTicks) {
		this.noMoveTicks = noMoveTicks;
	}

	public void setPlaceManager(PlaceManager placeManager) {
		this.placeManager = placeManager;
	}

	public void setCheckManager(CheckManager checkManager) {
		this.checkManager = checkManager;
	}

	public void setCheckViolationTimes(Map<Check, Set<Long>> checkViolationTimes) {
		this.checkViolationTimes = checkViolationTimes;
	}

	public void setCheckVlMap(Map<Check, Double> checkVlMap) {
		this.checkVlMap = checkVlMap;
	}

	public void setCancelNextHitR(boolean cancelNextHitR) {
		this.cancelNextHitR = cancelNextHitR;
	}

	public void setCancelNextHitH(boolean cancelNextHitH) {
		this.cancelNextHitH = cancelNextHitH;
	}

	public void setReduceNextDamage(boolean reduceNextDamage) {
		this.reduceNextDamage = reduceNextDamage;
	}

	public void setCancelBreak(boolean cancelBreak) {
		this.cancelBreak = cancelBreak;
	}

	public void setForceCancelReach(boolean forceCancelReach) {
		this.forceCancelReach = forceCancelReach;
	}

	public void setCancelTripleHit(boolean cancelTripleHit) {
		this.cancelTripleHit = cancelTripleHit;
	}

	public void setAbusingVelocity(boolean abusingVelocity) {
		this.abusingVelocity = abusingVelocity;
	}

	public void setEntityIdCancel(int entityIdCancel) {
		this.entityIdCancel = entityIdCancel;
	}

	public void setCancelHitsTick(int cancelHitsTick) {
		this.cancelHitsTick = cancelHitsTick;
	}

	public void setJumpMovementFactor(float jumpMovementFactor) {
		this.jumpMovementFactor = jumpMovementFactor;
	}

	public void setSpeedInAir(float speedInAir) {
		this.speedInAir = speedInAir;
	}

	public void setLastJumpMovementFactor(float lastJumpMovementFactor) {
		this.lastJumpMovementFactor = lastJumpMovementFactor;
	}

	public void setAttributeSpeed(float attributeSpeed) {
		this.attributeSpeed = attributeSpeed;
	}

	public void setLastAttributeSpeed(float lastAttributeSpeed) {
		this.lastAttributeSpeed = lastAttributeSpeed;
	}

	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	public void setJumpFactor(float jumpFactor) {
		this.jumpFactor = jumpFactor;
	}

	public void setJumpedCurrentTick(boolean jumpedCurrentTick) {
		this.jumpedCurrentTick = jumpedCurrentTick;
	}

	public void setJumpedLastTick(boolean jumpedLastTick) {
		this.jumpedLastTick = jumpedLastTick;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public void setPressedKey(String pressedKey) {
		this.pressedKey = pressedKey;
	}

	public void setBlockBelow(Block blockBelow) {
		this.blockBelow = blockBelow;
	}

	public void setBlockInside(Block blockInside) {
		this.blockInside = blockInside;
	}

	public void setLastBlockInside(Block lastBlockInside) {
		this.lastBlockInside = lastBlockInside;
	}

	public void setExitingVehicle(boolean exitingVehicle) {
		this.exitingVehicle = exitingVehicle;
	}

	public void setKeyCombo(String keyCombo) {
		this.keyCombo = keyCombo;
	}

	public void setClientVersion(ClientVersion clientVersion) {
		this.clientVersion = clientVersion;
	}

	public void setNewerThan8(boolean isNewerThan8) {
		this.isNewerThan8 = isNewerThan8;
	}

	public void setNewerThan12(boolean isNewerThan12) {
		this.isNewerThan12 = isNewerThan12;
	}

	public void setNewerThan13(boolean isNewerThan13) {
		this.isNewerThan13 = isNewerThan13;
	}

	public void setNewerThan16(boolean isNewerThan16) {
		this.isNewerThan16 = isNewerThan16;
	}

	public void setConfirmedVersion(boolean confirmedVersion) {
		this.confirmedVersion = confirmedVersion;
	}

	public void setConfirmedVersion2(boolean confirmedVersion2) {
		this.confirmedVersion2 = confirmedVersion2;
	}

	public void setViaMCP(boolean isViaMCP) {
		this.isViaMCP = isViaMCP;
	}

	public void setCollisionHandler(KarhuHandler collisionHandler) {
		this.collisionHandler = collisionHandler;
	}

	public void setDesyncedBlockHandler(DesyncedBlockHandler desyncedBlockHandler) {
		this.desyncedBlockHandler = desyncedBlockHandler;
	}

	public void setMovementHandler(MovementHandler movementHandler) {
		this.movementHandler = movementHandler;
	}

	public void setPredictionHandler(AbstractPredictionHandler predictionHandler) {
		this.predictionHandler = predictionHandler;
	}

	public void setNetHandler(NetHandler netHandler) {
		this.netHandler = netHandler;
	}

	public void setCrashHandler(ICrashHandler crashHandler) {
		this.crashHandler = crashHandler;
	}

	public void setWrappedEntity(WrappedEntityPlayer wrappedEntity) {
		this.wrappedEntity = wrappedEntity;
	}

	public void setHasReceivedTransaction(boolean hasReceivedTransaction) {
		this.hasReceivedTransaction = hasReceivedTransaction;
	}

	public void setHasReceivedKeepalive(boolean hasReceivedKeepalive) {
		this.hasReceivedKeepalive = hasReceivedKeepalive;
	}

	public void setHasTeleportedOnce(boolean isHasTeleportedOnce) {
		this.isHasTeleportedOnce = isHasTeleportedOnce;
	}

	public void setSentConfirms(int sentConfirms) {
		this.sentConfirms = sentConfirms;
	}

	public void setReceivedConfirms(int receivedConfirms) {
		this.receivedConfirms = receivedConfirms;
	}

	public void setWaitingConfirms(Map<Short, ObjectArrayList<Consumer<Short>>> waitingConfirms) {
		this.waitingConfirms = waitingConfirms;
	}

	public void setTransactionId(short transactionId) {
		this.transactionId = transactionId;
	}

	public void setConfirmId(short confirmId) {
		this.confirmId = confirmId;
	}

	public void setTickTransactionId(short tickTransactionId) {
		this.tickTransactionId = tickTransactionId;
	}

	public void setTickFirstConfirmationUid(int tickFirstConfirmationUid) {
		this.tickFirstConfirmationUid = tickFirstConfirmationUid;
	}

	public void setTickSecondConfirmationUid(int tickSecondConfirmationUid) {
		this.tickSecondConfirmationUid = tickSecondConfirmationUid;
	}

	public void setLastTickFirstConfirmationUid(int lastTickFirstConfirmationUid) {
		this.lastTickFirstConfirmationUid = lastTickFirstConfirmationUid;
	}

	public void setLastTickSecondConfirmationUid(int lastTickSecondConfirmationUid) {
		this.lastTickSecondConfirmationUid = lastTickSecondConfirmationUid;
	}

	public void setHasSentTickFirst(boolean hasSentTickFirst) {
		this.hasSentTickFirst = hasSentTickFirst;
	}

	public void setSendingPledgePackets(boolean sendingPledgePackets) {
		this.sendingPledgePackets = sendingPledgePackets;
	}

	public void setHasSentFirst(boolean hasSentFirst) {
		this.hasSentFirst = hasSentFirst;
	}

	public void setBrokenVelocityVerify(boolean brokenVelocityVerify) {
		this.brokenVelocityVerify = brokenVelocityVerify;
	}

	public void setLastPostId(short lastPostId) {
		this.lastPostId = lastPostId;
	}

	public void setTakingVertical(boolean takingVertical) {
		this.takingVertical = takingVertical;
	}

	public void setConfirmingVelocity(boolean confirmingVelocity) {
		this.confirmingVelocity = confirmingVelocity;
	}

	public void setNeedExplosionAdditions(boolean needExplosionAdditions) {
		this.needExplosionAdditions = needExplosionAdditions;
	}

	public void setLastVelocityYReset(int lastVelocityYReset) {
		this.lastVelocityYReset = lastVelocityYReset;
	}

	public void setLastVelocityXZReset(int lastVelocityXZReset) {
		this.lastVelocityXZReset = lastVelocityXZReset;
	}

	public void setLastVelocityTaken(int lastVelocityTaken) {
		this.lastVelocityTaken = lastVelocityTaken;
	}

	public void setVelocityYTicks(int velocityYTicks) {
		this.velocityYTicks = velocityYTicks;
	}

	public void setMaxVelocityXZTicks(int maxVelocityXZTicks) {
		this.maxVelocityXZTicks = maxVelocityXZTicks;
	}

	public void setMaxVelocityYTicks(int maxVelocityYTicks) {
		this.maxVelocityYTicks = maxVelocityYTicks;
	}

	public void setExplosionExempt(int explosionExempt) {
		this.explosionExempt = explosionExempt;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public void setVelocityZ(double velocityZ) {
		this.velocityZ = velocityZ;
	}

	public void setVelocityHorizontal(double velocityHorizontal) {
		this.velocityHorizontal = velocityHorizontal;
	}

	public void setConfirmingY(double confirmingY) {
		this.confirmingY = confirmingY;
	}

	public void setTickedVelocity(Vector tickedVelocity) {
		this.tickedVelocity = tickedVelocity;
	}

	public void setPlayerVelocityCalled(boolean playerVelocityCalled) {
		this.playerVelocityCalled = playerVelocityCalled;
	}

	public void setPlayerExplodeCalled(boolean playerExplodeCalled) {
		this.playerExplodeCalled = playerExplodeCalled;
	}

	public void setTotalTicks(int totalTicks) {
		this.totalTicks = totalTicks;
	}

	public void setLastSprintTick(int lastSprintTick) {
		this.lastSprintTick = lastSprintTick;
	}

	public void setLastSneakTick(int lastSneakTick) {
		this.lastSneakTick = lastSneakTick;
	}

	public void setAirTicks(int airTicks) {
		this.airTicks = airTicks;
	}

	public void setDigTicks(int digTicks) {
		this.digTicks = digTicks;
	}

	public void setFastDigTicks(int fastDigTicks) {
		this.fastDigTicks = fastDigTicks;
	}

	public void setDigStopTicks(int digStopTicks) {
		this.digStopTicks = digStopTicks;
	}

	public void setPlaceTicks(int placeTicks) {
		this.placeTicks = placeTicks;
	}

	public void setUnderPlaceTicks(int underPlaceTicks) {
		this.underPlaceTicks = underPlaceTicks;
	}

	public void setBucketTicks(int bucketTicks) {
		this.bucketTicks = bucketTicks;
	}

	public void setSpoofPlaceTicks(int spoofPlaceTicks) {
		this.spoofPlaceTicks = spoofPlaceTicks;
	}

	public void setUseTicks(int useTicks) {
		this.useTicks = useTicks;
	}

	public void setLClientAirTicks(int lClientAirTicks) {
		this.lClientAirTicks = lClientAirTicks;
	}

	public void setClientAirTicks(int clientAirTicks) {
		this.clientAirTicks = clientAirTicks;
	}

	public void setLastFlyTick(int lastFlyTick) {
		this.lastFlyTick = lastFlyTick;
	}

	public void setLastAllowFlyTick(int lastAllowFlyTick) {
		this.lastAllowFlyTick = lastAllowFlyTick;
	}

	public void setElapsedOnLiquid(int elapsedOnLiquid) {
		this.elapsedOnLiquid = elapsedOnLiquid;
	}

	public void setElapsedUnderBlock(int elapsedUnderBlock) {
		this.elapsedUnderBlock = elapsedUnderBlock;
	}

	public void setElapsedSinceBridgePlace(int elapsedSinceBridgePlace) {
		this.elapsedSinceBridgePlace = elapsedSinceBridgePlace;
	}

	public void setServerGroundTicks(int serverGroundTicks) {
		this.serverGroundTicks = serverGroundTicks;
	}

	public void setClientGroundTicks(int clientGroundTicks) {
		this.clientGroundTicks = clientGroundTicks;
	}

	public void setWeirdTicks(int weirdTicks) {
		this.weirdTicks = weirdTicks;
	}

	public void setLastInBerry(int lastInBerry) {
		this.lastInBerry = lastInBerry;
	}

	public void setLastInUnloadedChunk(int lastInUnloadedChunk) {
		this.lastInUnloadedChunk = lastInUnloadedChunk;
	}

	public void setLastInLiquid(int lastInLiquid) {
		this.lastInLiquid = lastInLiquid;
	}

	public void setLastOnSlime(int lastOnSlime) {
		this.lastOnSlime = lastOnSlime;
	}

	public void setLastOnSoul(int lastOnSoul) {
		this.lastOnSoul = lastOnSoul;
	}

	public void setLastOnIce(int lastOnIce) {
		this.lastOnIce = lastOnIce;
	}

	public void setLastOnClimbable(int lastOnClimbable) {
		this.lastOnClimbable = lastOnClimbable;
	}

	public void setLastOnBed(int lastOnBed) {
		this.lastOnBed = lastOnBed;
	}

	public void setLastInWeb(int lastInWeb) {
		this.lastInWeb = lastInWeb;
	}

	public void setLastCollided(int lastCollided) {
		this.lastCollided = lastCollided;
	}

	public void setLastCollidedWithEntity(int lastCollidedWithEntity) {
		this.lastCollidedWithEntity = lastCollidedWithEntity;
	}

	public void setLastOnHalfBlock(int lastOnHalfBlock) {
		this.lastOnHalfBlock = lastOnHalfBlock;
	}

	public void setLastCollidedV(int lastCollidedV) {
		this.lastCollidedV = lastCollidedV;
	}

	public void setLastCollidedVGhost(int lastCollidedVGhost) {
		this.lastCollidedVGhost = lastCollidedVGhost;
	}

	public void setLastOnBoat(int lastOnBoat) {
		this.lastOnBoat = lastOnBoat;
	}

	public void setLastInPowder(int lastInPowder) {
		this.lastInPowder = lastInPowder;
	}

	public void setLastPossibleInUnloadedChunk(int lastPossibleInUnloadedChunk) {
		this.lastPossibleInUnloadedChunk = lastPossibleInUnloadedChunk;
	}

	public void setLastCollidedGhost(int lastCollidedGhost) {
		this.lastCollidedGhost = lastCollidedGhost;
	}

	public void setPositionPackets(int positionPackets) {
		this.positionPackets = positionPackets;
	}

	public void setLastInLiquidOffset(int lastInLiquidOffset) {
		this.lastInLiquidOffset = lastInLiquidOffset;
	}

	public void setLastConfirmingState(int lastConfirmingState) {
		this.lastConfirmingState = lastConfirmingState;
	}

	public void setLastCollidedH(int lastCollidedH) {
		this.lastCollidedH = lastCollidedH;
	}

	public void setLastSneakEdge(int lastSneakEdge) {
		this.lastSneakEdge = lastSneakEdge;
	}

	public void setLastFence(int lastFence) {
		this.lastFence = lastFence;
	}

	public void setDigging(boolean digging) {
		this.digging = digging;
	}

	public void setDiggingBasic(boolean diggingBasic) {
		this.diggingBasic = diggingBasic;
	}

	public void setTraceDigging(boolean traceDigging) {
		this.traceDigging = traceDigging;
	}

	public void setPlacing(boolean placing) {
		this.placing = placing;
	}

	public void setWasPlacing(boolean wasPlacing) {
		this.wasPlacing = wasPlacing;
	}

	public void setSkipNextSwing(boolean skipNextSwing) {
		this.skipNextSwing = skipNextSwing;
	}

	public void setNoMoveNextFlying(boolean noMoveNextFlying) {
		this.noMoveNextFlying = noMoveNextFlying;
	}

	public void setCollidedHorizontally(boolean collidedHorizontally) {
		this.collidedHorizontally = collidedHorizontally;
	}

	public void setWasCollidedHorizontally(boolean wasCollidedHorizontally) {
		this.wasCollidedHorizontally = wasCollidedHorizontally;
	}

	public void setCollidedHorizontalClient(boolean collidedHorizontalClient) {
		this.collidedHorizontalClient = collidedHorizontalClient;
	}

	public void setCollidedWithFence(boolean collidedWithFence) {
		this.collidedWithFence = collidedWithFence;
	}

	public void setEdgeOfFence(boolean edgeOfFence) {
		this.edgeOfFence = edgeOfFence;
	}

	public void setCollidedWithPane(boolean collidedWithPane) {
		this.collidedWithPane = collidedWithPane;
	}

	public void setCollidedWithCactus(boolean collidedWithCactus) {
		this.collidedWithCactus = collidedWithCactus;
	}

	public void setInsideTrapdoor(boolean insideTrapdoor) {
		this.insideTrapdoor = insideTrapdoor;
	}

	public void setInsideBlock(boolean insideBlock) {
		this.insideBlock = insideBlock;
	}

	public void setWasFullyInsideBlock(boolean wasFullyInsideBlock) {
		this.wasFullyInsideBlock = wasFullyInsideBlock;
	}

	public void setFullyInsideBlock(boolean fullyInsideBlock) {
		this.fullyInsideBlock = fullyInsideBlock;
	}

	public void setInWeb(boolean inWeb) {
		this.inWeb = inWeb;
	}

	public void setWasInWeb(boolean isWasInWeb) {
		this.isWasInWeb = isWasInWeb;
	}

	public void setWasWasInWeb(boolean isWasWasInWeb) {
		this.isWasWasInWeb = isWasWasInWeb;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	public void setShit(boolean shit) {
		this.shit = shit;
	}

	public void setBowing(boolean bowing) {
		this.bowing = bowing;
	}

	public void setFinalCollidedH(boolean finalCollidedH) {
		this.finalCollidedH = finalCollidedH;
	}

	public void setLastAbortLoc(Vector lastAbortLoc) {
		this.lastAbortLoc = lastAbortLoc;
	}

	public void setLastPistonPush(int lastPistonPush) {
		this.lastPistonPush = lastPistonPush;
	}

	public void setLastSlimePistonPush(int lastSlimePistonPush) {
		this.lastSlimePistonPush = lastSlimePistonPush;
	}

	public void setMoveCalls(int moveCalls) {
		this.moveCalls = moveCalls;
	}

	public void setServerTick(long serverTick) {
		this.serverTick = serverTick;
	}

	public void setCreatedOnTick(long createdOnTick) {
		this.createdOnTick = createdOnTick;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public void setMcpCollision(BoundingBox mcpCollision) {
		this.mcpCollision = mcpCollision;
	}

	public void setLastBoundingBox(BoundingBox lastBoundingBox) {
		this.lastBoundingBox = lastBoundingBox;
	}

	public void setVehicleX(double vehicleX) {
		this.vehicleX = vehicleX;
	}

	public void setVehicleY(double vehicleY) {
		this.vehicleY = vehicleY;
	}

	public void setVehicleZ(double vehicleZ) {
		this.vehicleZ = vehicleZ;
	}

	public void setTicksOnGhostBlock(int ticksOnGhostBlock) {
		this.ticksOnGhostBlock = ticksOnGhostBlock;
	}

	public void setTicksOnBlockHandlerNotEnabled(int ticksOnBlockHandlerNotEnabled) {
		this.ticksOnBlockHandlerNotEnabled = ticksOnBlockHandlerNotEnabled;
	}

	public void setUpdateBuf(int updateBuf) {
		this.updateBuf = updateBuf;
	}

	public void setLastTarget(LivingEntity lastTarget) {
		this.lastTarget = lastTarget;
	}

	public void setLastTargets(List<Integer> lastTargets) {
		this.lastTargets = lastTargets;
	}

	public void setLastAttackTick(int lastAttackTick) {
		this.lastAttackTick = lastAttackTick;
	}

	public void setAttacks(int attacks) {
		this.attacks = attacks;
	}

	public void setLastAttackPacket(long lastAttackPacket) {
		this.lastAttackPacket = lastAttackPacket;
	}

	public void setAttackedSinceVelocity(boolean attackedSinceVelocity) {
		this.attackedSinceVelocity = attackedSinceVelocity;
	}

	public void setReachBypass(boolean reachBypass) {
		this.reachBypass = reachBypass;
	}

	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}

	public void setWasSprinting(boolean wasSprinting) {
		this.wasSprinting = wasSprinting;
	}

	public void setWasWasSprinting(boolean wasWasSprinting) {
		this.wasWasSprinting = wasWasSprinting;
	}

	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}

	public void setWasSneaking(boolean wasSneaking) {
		this.wasSneaking = wasSneaking;
	}

	public void setWasWasSneaking(boolean wasWasSneaking) {
		this.wasWasSneaking = wasWasSneaking;
	}

	public void setUsingItem(boolean usingItem) {
		this.usingItem = usingItem;
	}

	public void setLastUsingItem(boolean lastUsingItem) {
		this.lastUsingItem = lastUsingItem;
	}

	public void setEating(boolean eating) {
		this.eating = eating;
	}

	public void setLastEating(boolean lastEating) {
		this.lastEating = lastEating;
	}

	public void setRecorrectingSprint(boolean recorrectingSprint) {
		this.recorrectingSprint = recorrectingSprint;
	}

	public void setDesyncSprint(boolean desyncSprint) {
		this.desyncSprint = desyncSprint;
	}

	public void setInventoryOpen(boolean inventoryOpen) {
		this.inventoryOpen = inventoryOpen;
	}

	public void setCrouching(boolean crouching) {
		this.crouching = crouching;
	}

	public void setInBed(boolean inBed) {
		this.inBed = inBed;
	}

	public void setLastInBed(boolean lastInBed) {
		this.lastInBed = lastInBed;
	}

	public void setBedPos(Vec3 bedPos) {
		this.bedPos = bedPos;
	}

	public void setInvStamp(int invStamp) {
		this.invStamp = invStamp;
	}

	public void setSlotSwitchTick(int slotSwitchTick) {
		this.slotSwitchTick = slotSwitchTick;
	}

	public void setLastWorldChange(int lastWorldChange) {
		this.lastWorldChange = lastWorldChange;
	}

	public void setCinematic(boolean cinematic) {
		this.cinematic = cinematic;
	}

	public void setLastCinematic(int lastCinematic) {
		this.lastCinematic = lastCinematic;
	}

	public void setFallDistance(float fallDistance) {
		this.fallDistance = fallDistance;
	}

	public void setLFallDistance(float lFallDistance) {
		this.lFallDistance = lFallDistance;
	}

	public void setEntityData(ConcurrentHashMap<Integer, EntityData> entityData) {
		this.entityData = entityData;
	}

	public void setLastPos(int lastPos) {
		this.lastPos = lastPos;
	}

	public void setAttackerX(double attackerX) {
		this.attackerX = attackerX;
	}

	public void setAttackerY(double attackerY) {
		this.attackerY = attackerY;
	}

	public void setAttackerZ(double attackerZ) {
		this.attackerZ = attackerZ;
	}

	public void setAttackerYaw(float attackerYaw) {
		this.attackerYaw = attackerYaw;
	}

	public void setAttackerPitch(float attackerPitch) {
		this.attackerPitch = attackerPitch;
	}

	public void setTeleports(int teleports) {
		this.teleports = teleports;
	}

	public void setAddedTeleports(int addedTeleports) {
		this.addedTeleports = addedTeleports;
	}

	public void setEyesFourteen(List<Double> eyesFourteen) {
		this.eyesFourteen = eyesFourteen;
	}

	public void setEyesNine(List<Double> eyesNine) {
		this.eyesNine = eyesNine;
	}

	public void setEyesLegacy(List<Double> eyesLegacy) {
		this.eyesLegacy = eyesLegacy;
	}

	public void setOnGroundServer(boolean onGroundServer) {
		this.onGroundServer = onGroundServer;
	}

	public void setOnBoat(boolean onBoat) {
		this.onBoat = onBoat;
	}

	public void setGroundNearBox(boolean groundNearBox) {
		this.groundNearBox = groundNearBox;
	}

	public void setWasOnGroundServer(boolean wasOnGroundServer) {
		this.wasOnGroundServer = wasOnGroundServer;
	}

	public void setOnWater(boolean onWater) {
		this.onWater = onWater;
	}

	public void setWasOnWater(boolean wasOnWater) {
		this.wasOnWater = wasOnWater;
	}

	public void setAboveButNotInWater(boolean aboveButNotInWater) {
		this.aboveButNotInWater = aboveButNotInWater;
	}

	public void setWaterAlmostOnFeet(boolean waterAlmostOnFeet) {
		this.waterAlmostOnFeet = waterAlmostOnFeet;
	}

	public void setOnLava(boolean onLava) {
		this.onLava = onLava;
	}

	public void setWasOnLava(boolean wasOnLava) {
		this.wasOnLava = wasOnLava;
	}

	public void setOnIce(boolean onIce) {
		this.onIce = onIce;
	}

	public void setOnLiquid(boolean onLiquid) {
		this.onLiquid = onLiquid;
	}

	public void setOnSlab(boolean onSlab) {
		this.onSlab = onSlab;
	}

	public void setWasOnSlab(boolean wasOnSlab) {
		this.wasOnSlab = wasOnSlab;
	}

	public void setOnDoor(boolean onDoor) {
		this.onDoor = onDoor;
	}

	public void setWasOnDoor(boolean wasOnDoor) {
		this.wasOnDoor = wasOnDoor;
	}

	public void setOnFence(boolean onFence) {
		this.onFence = onFence;
	}

	public void setWasOnFence(boolean wasOnFence) {
		this.wasOnFence = wasOnFence;
	}

	public void setOnStairs(boolean onStairs) {
		this.onStairs = onStairs;
	}

	public void setWasOnStairs(boolean wasOnStairs) {
		this.wasOnStairs = wasOnStairs;
	}

	public void setOnBed(boolean onBed) {
		this.onBed = onBed;
	}

	public void setWasOnBed(boolean wasOnBed) {
		this.wasOnBed = wasOnBed;
	}

	public void setUnderBlock(boolean underBlock) {
		this.underBlock = underBlock;
	}

	public void setUnderBlockStrict(boolean underBlockStrict) {
		this.underBlockStrict = underBlockStrict;
	}

	public void setWasUnderBlock(boolean wasUnderBlock) {
		this.wasUnderBlock = wasUnderBlock;
	}

	public void setUnderWeb(boolean underWeb) {
		this.underWeb = underWeb;
	}

	public void setOnWeb(boolean onWeb) {
		this.onWeb = onWeb;
	}

	public void setOnSoulsand(boolean onSoulsand) {
		this.onSoulsand = onSoulsand;
	}

	public void setWasOnSoulSand(boolean wasOnSoulSand) {
		this.wasOnSoulSand = wasOnSoulSand;
	}

	public void setWasSlimeLand(boolean wasSlimeLand) {
		this.wasSlimeLand = wasSlimeLand;
	}

	public void setSlimeLand(boolean slimeLand) {
		this.slimeLand = slimeLand;
	}

	public void setOnSlime(boolean onSlime) {
		this.onSlime = onSlime;
	}

	public void setWasOnSlime(boolean wasOnSlime) {
		this.wasOnSlime = wasOnSlime;
	}

	public void setWasWasOnSlime(boolean wasWasOnSlime) {
		this.wasWasOnSlime = wasWasOnSlime;
	}

	public void setOnCarpet(boolean onCarpet) {
		this.onCarpet = onCarpet;
	}

	public void setOnComparator(boolean onComparator) {
		this.onComparator = onComparator;
	}

	public void setWasOnComparator(boolean wasOnComparator) {
		this.wasOnComparator = wasOnComparator;
	}

	public void setOnClimbable(boolean onClimbable) {
		this.onClimbable = onClimbable;
	}

	public void setWasOnClimbable(boolean wasOnClimbable) {
		this.wasOnClimbable = wasOnClimbable;
	}

	public void setWasWasOnClimbable(boolean wasWasOnClimbable) {
		this.wasWasOnClimbable = wasWasOnClimbable;
	}

	public void setOnLadder(boolean onLadder) {
		this.onLadder = onLadder;
	}

	public void setLastLadder(boolean lastLadder) {
		this.lastLadder = lastLadder;
	}

	public void setNearClimbable(boolean nearClimbable) {
		this.nearClimbable = nearClimbable;
	}

	public void setOnHoney(boolean onHoney) {
		this.onHoney = onHoney;
	}

	public void setOnSweet(boolean onSweet) {
		this.onSweet = onSweet;
	}

	public void setWasOnHoney(boolean wasOnHoney) {
		this.wasOnHoney = wasOnHoney;
	}

	public void setOnScaffolding(boolean onScaffolding) {
		this.onScaffolding = onScaffolding;
	}

	public void setOnPiston(boolean onPiston) {
		this.onPiston = onPiston;
	}

	public void setOnTopGhostBlock(boolean onTopGhostBlock) {
		this.onTopGhostBlock = onTopGhostBlock;
	}

	public void setAtButton(boolean atButton) {
		this.atButton = atButton;
	}

	public void setOnWaterOffset(boolean onWaterOffset) {
		this.onWaterOffset = onWaterOffset;
	}

	public void setLastOnWaterOffset(boolean lastOnWaterOffset) {
		this.lastOnWaterOffset = lastOnWaterOffset;
	}

	public void setInPowder(boolean inPowder) {
		this.inPowder = inPowder;
	}

	public void setSneakEdge(boolean sneakEdge) {
		this.sneakEdge = sneakEdge;
	}

	public void setLastBlockSneak(boolean lastBlockSneak) {
		this.lastBlockSneak = lastBlockSneak;
	}

	public void setAtSign(boolean atSign) {
		this.atSign = atSign;
	}

	public void setMovementBlock(Block movementBlock) {
		this.movementBlock = movementBlock;
	}

	public void setCollidedWithLivingEntity(boolean collidedWithLivingEntity) {
		this.collidedWithLivingEntity = collidedWithLivingEntity;
	}

	public void setCurrentFriction(float currentFriction) {
		this.currentFriction = currentFriction;
	}

	public void setLastTickFriction(float lastTickFriction) {
		this.lastTickFriction = lastTickFriction;
	}

	public void setOnGroundPacket(boolean onGroundPacket) {
		this.onGroundPacket = onGroundPacket;
	}

	public void setLastOnGroundPacket(boolean lastOnGroundPacket) {
		this.lastOnGroundPacket = lastOnGroundPacket;
	}

	public void setLastLastOnGroundPacket(boolean lastLastOnGroundPacket) {
		this.lastLastOnGroundPacket = lastLastOnGroundPacket;
	}

	public void setOnGroundMath(boolean onGroundMath) {
		this.onGroundMath = onGroundMath;
	}

	public void setLastOnGroundMath(boolean lastOnGroundMath) {
		this.lastOnGroundMath = lastOnGroundMath;
	}

	public void setLastLastOnGroundMath(boolean lastLastOnGroundMath) {
		this.lastLastOnGroundMath = lastLastOnGroundMath;
	}

	public void setOnGhostBlock(boolean onGhostBlock) {
		this.onGhostBlock = onGhostBlock;
	}

	public void setUnderGhostBlock(boolean underGhostBlock) {
		this.underGhostBlock = underGhostBlock;
	}

	public void setWasUnderGhostBlock(boolean isWasUnderGhostBlock) {
		this.isWasUnderGhostBlock = isWasUnderGhostBlock;
	}

	public void setLastInGhostLiquid(int lastInGhostLiquid) {
		this.lastInGhostLiquid = lastInGhostLiquid;
	}

	public void setGhostBlockSetbacks(double ghostBlockSetbacks) {
		this.ghostBlockSetbacks = ghostBlockSetbacks;
	}

	public void setPing(long ping) {
		this.ping = ping;
	}

	public void setLastPingTime(long lastPingTime) {
		this.lastPingTime = lastPingTime;
	}

	public void setTransactionPing(long transactionPing) {
		this.transactionPing = transactionPing;
	}

	public void setLastTransactionPing(long lastTransactionPing) {
		this.lastTransactionPing = lastTransactionPing;
	}

	public void setTimerTransactionSent(short timerTransactionSent) {
		this.timerTransactionSent = timerTransactionSent;
	}

	public void setInUnloadedChunk(boolean inUnloadedChunk) {
		this.inUnloadedChunk = inUnloadedChunk;
	}

	public void setWasInUnloadedChunk(boolean wasInUnloadedChunk) {
		this.wasInUnloadedChunk = wasInUnloadedChunk;
	}

	public void setWasWasInUnloadedChunk(boolean wasWasInUnloadedChunk) {
		this.wasWasInUnloadedChunk = wasWasInUnloadedChunk;
	}

	public void setLastTransaction(long lastTransaction) {
		this.lastTransaction = lastTransaction;
	}

	public void setLastTransactionPingUpdate(long lastTransactionPingUpdate) {
		this.lastTransactionPingUpdate = lastTransactionPingUpdate;
	}

	public void setBadPingTicks(int badPingTicks) {
		this.badPingTicks = badPingTicks;
	}

	public void setPingInTicks(int pingInTicks) {
		this.pingInTicks = pingInTicks;
	}

	public void setTeleportManager(TeleportManager teleportManager) {
		this.teleportManager = teleportManager;
	}

	public void setPossiblyTeleporting(boolean possiblyTeleporting) {
		this.possiblyTeleporting = possiblyTeleporting;
	}

	public void setSeventeenPlacing(boolean seventeenPlacing) {
		this.seventeenPlacing = seventeenPlacing;
	}

	public void setLastTeleport(int lastTeleport) {
		this.lastTeleport = lastTeleport;
	}

	public void setLastTeleportPacket(long lastTeleportPacket) {
		this.lastTeleportPacket = lastTeleportPacket;
	}

	public void setFirstChunkMove(CustomLocation firstChunkMove) {
		this.firstChunkMove = firstChunkMove;
	}

	public void setJoining(boolean joining) {
		this.joining = joining;
	}

	public void setFuckedTeleport(boolean fuckedTeleport) {
		this.fuckedTeleport = fuckedTeleport;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public void setBukkitPlayer(Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setCleanBrand(String cleanBrand) {
		this.cleanBrand = cleanBrand;
	}

	public void setBrandPosted(boolean brandPosted) {
		this.brandPosted = brandPosted;
	}

	public void setEffectManager(EffectManager effectManager) {
		this.effectManager = effectManager;
	}

	public void setJumpBoost(int jumpBoost) {
		this.jumpBoost = jumpBoost;
	}

	public void setCacheBoost(int cacheBoost) {
		this.cacheBoost = cacheBoost;
	}

	public void setSpeedBoost(int speedBoost) {
		this.speedBoost = speedBoost;
	}

	public void setSlowness(int slowness) {
		this.slowness = slowness;
	}

	public void setHaste(int haste) {
		this.haste = haste;
	}

	public void setFatigue(int fatigue) {
		this.fatigue = fatigue;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public void setAllowFlying(boolean allowFlying) {
		this.allowFlying = allowFlying;
	}

	public void setFlyingS(boolean flyingS) {
		this.flyingS = flyingS;
	}

	public void setFlyingC(boolean flyingC) {
		this.flyingC = flyingC;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public void setWasFlyingC(boolean wasFlyingC) {
		this.wasFlyingC = wasFlyingC;
	}

	public void setInitedFlying(boolean initedFlying) {
		this.initedFlying = initedFlying;
	}

	public void setConfirmingFlying(boolean confirmingFlying) {
		this.confirmingFlying = confirmingFlying;
	}

	public void setCorrectedFly(boolean correctedFly) {
		this.correctedFly = correctedFly;
	}

	public void setAbilityManager(AbilityManager abilityManager) {
		this.abilityManager = abilityManager;
	}

	public void setWorldTracker(WorldTracker worldTracker) {
		this.worldTracker = worldTracker;
	}

	public void setKarhuWorld(KarhuWorld karhuWorld) {
		this.karhuWorld = karhuWorld;
	}

	public void setLastServerSlot(int lastServerSlot) {
		this.lastServerSlot = lastServerSlot;
	}

	public void setLastFlying(long lastFlying) {
		this.lastFlying = lastFlying;
	}

	public void setFlyingTime(long flyingTime) {
		this.flyingTime = flyingTime;
	}

	public void setLastJoinTime(long lastJoinTime) {
		this.lastJoinTime = lastJoinTime;
	}

	public void setLastFlyingTicks(int lastFlyingTicks) {
		this.lastFlyingTicks = lastFlyingTicks;
	}

	public void setVelocityXZTicks(int velocityXZTicks) {
		this.velocityXZTicks = velocityXZTicks;
	}

	public void setLastTransactionTick(int lastTransactionTick) {
		this.lastTransactionTick = lastTransactionTick;
	}

	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	public void setCurrentServerTransaction(int currentServerTransaction) {
		this.currentServerTransaction = currentServerTransaction;
	}

	public void setCurrentClientTransaction(int currentClientTransaction) {
		this.currentClientTransaction = currentClientTransaction;
	}

	public void setLastClientTransaction(int lastClientTransaction) {
		this.lastClientTransaction = lastClientTransaction;
	}

	public void setLastLastClientTransaction(int lastLastClientTransaction) {
		this.lastLastClientTransaction = lastLastClientTransaction;
	}

	public void setFirstTransactionSent(boolean firstTransactionSent) {
		this.firstTransactionSent = firstTransactionSent;
	}

	public void setLastDroppedPackets(int lastDroppedPackets) {
		this.lastDroppedPackets = lastDroppedPackets;
	}

	public void setLastPacketDrop(int lastPacketDrop) {
		this.lastPacketDrop = lastPacketDrop;
	}

	public void setHurtTicks(int hurtTicks) {
		this.hurtTicks = hurtTicks;
	}

	public void setLastFast(long lastFast) {
		this.lastFast = lastFast;
	}

	public void setMovementDesynced(boolean movementDesynced) {
		this.movementDesynced = movementDesynced;
	}

	public void setRiding(boolean riding) {
		this.riding = riding;
	}

	public void setBrokenVehicle(boolean brokenVehicle) {
		this.brokenVehicle = brokenVehicle;
	}

	public void setRidingUncertain(boolean ridingUncertain) {
		this.ridingUncertain = ridingUncertain;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public void setVehicle(Entity vehicle) {
		this.vehicle = vehicle;
	}

	public void setLastUnmount(int lastUnmount) {
		this.lastUnmount = lastUnmount;
	}

	public void setCps(double cps) {
		this.cps = cps;
	}

	public void setLastCps(double lastCps) {
		this.lastCps = lastCps;
	}

	public void setHighestCps(double highestCps) {
		this.highestCps = highestCps;
	}

	public void setHighestReach(double highestReach) {
		this.highestReach = highestReach;
	}

	public void setDidFlagMovement(boolean didFlagMovement) {
		this.didFlagMovement = didFlagMovement;
	}

	public void setLastMovementFlag(int lastMovementFlag) {
		this.lastMovementFlag = lastMovementFlag;
	}

	public void setSafeSetback(CustomLocation safeSetback) {
		this.safeSetback = safeSetback;
	}

	public void setSafeGroundSetback(CustomLocation safeGroundSetback) {
		this.safeGroundSetback = safeGroundSetback;
	}

	public void setFlyCancel(CustomLocation flyCancel) {
		this.flyCancel = flyCancel;
	}

	public void setTeleportLocation(CustomLocation teleportLocation) {
		this.teleportLocation = teleportLocation;
	}

	public void setLastLocationUpdate(long lastLocationUpdate) {
		this.lastLocationUpdate = lastLocationUpdate;
	}

	public void setInvalidMovementTicks(int invalidMovementTicks) {
		this.invalidMovementTicks = invalidMovementTicks;
	}

	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}

	public void setSensitivityY(float sensitivityY) {
		this.sensitivityY = sensitivityY;
	}

	public void setSensitivityX(float sensitivityX) {
		this.sensitivityX = sensitivityX;
	}

	public void setSmallestRotationGCD(float smallestRotationGCD) {
		this.smallestRotationGCD = smallestRotationGCD;
	}

	public void setPitchGCD(float pitchGCD) {
		this.pitchGCD = pitchGCD;
	}

	public void setYawGCD(float yawGCD) {
		this.yawGCD = yawGCD;
	}

	public void setPredictPitch(float predictPitch) {
		this.predictPitch = predictPitch;
	}

	public void setPredictYaw(float predictYaw) {
		this.predictYaw = predictYaw;
	}

	public void setEyeLocation(Vec3 eyeLocation) {
		this.eyeLocation = eyeLocation;
	}

	public void setLook(Vec3 look) {
		this.look = look;
	}

	public void setLookMouseDelayFix(Vec3 lookMouseDelayFix) {
		this.lookMouseDelayFix = lookMouseDelayFix;
	}

	public void setLocationInited(boolean locationInited) {
		this.locationInited = locationInited;
	}

	public void setBoundingBoxInited(boolean boundingBoxInited) {
		this.boundingBoxInited = boundingBoxInited;
	}

	public void setLocationInitedAt(int locationInitedAt) {
		this.locationInitedAt = locationInitedAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public void setTransactionClock(long transactionClock) {
		this.transactionClock = transactionClock;
	}

	public void setCurrentSlot(int currentSlot) {
		this.currentSlot = currentSlot;
	}

	public void setPendingBackSwitch(boolean pendingBackSwitch) {
		this.pendingBackSwitch = pendingBackSwitch;
	}

	public void setTimerKicked(boolean timerKicked) {
		this.timerKicked = timerKicked;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public void setGliding(boolean gliding) {
		this.gliding = gliding;
	}

	public void setRiptiding(boolean riptiding) {
		this.riptiding = riptiding;
	}

	public void setSpectating(boolean spectating) {
		this.spectating = spectating;
	}

	public void setLastGlide(int lastGlide) {
		this.lastGlide = lastGlide;
	}

	public void setLastRiptide(int lastRiptide) {
		this.lastRiptide = lastRiptide;
	}

	public void setDolphinLevel(int dolphinLevel) {
		this.dolphinLevel = dolphinLevel;
	}

	public void setSoulSpeedLevel(int soulSpeedLevel) {
		this.soulSpeedLevel = soulSpeedLevel;
	}

	public void setDepthStriderLevel(int depthStriderLevel) {
		this.depthStriderLevel = depthStriderLevel;
	}

	public void setSlowFallingLevel(int slowFallingLevel) {
		this.slowFallingLevel = slowFallingLevel;
	}

	public void setLevitationLevel(int levitationLevel) {
		this.levitationLevel = levitationLevel;
	}
}
