/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Server;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus.Action;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.check.impl.movement.fly.FlyA;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.data.potion.PotionEffect;
import me.liwk.karhu.event.AbilityEvent;
import me.liwk.karhu.event.ActionEvent;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.ConnectionHeartbeatEvent;
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.HeldItemSlotEvent;
import me.liwk.karhu.event.InteractEvent;
import me.liwk.karhu.event.PositionEvent;
import me.liwk.karhu.event.SteerEvent;
import me.liwk.karhu.event.SwingEvent;
import me.liwk.karhu.event.TransactionEvent;
import me.liwk.karhu.event.VehicleEvent;
import me.liwk.karhu.event.VelocityEvent;
import me.liwk.karhu.event.WindowEvent;
import me.liwk.karhu.handler.PlayerHandler;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.Teleport;
import me.liwk.karhu.util.TeleportPosition;
import me.liwk.karhu.util.VersionBridgeHelper;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.pending.BlockPlacePending;
import me.liwk.karhu.util.player.BlockUtil;
import me.liwk.karhu.util.set.PassiveExpiringSet;
import me.liwk.karhu.util.task.Tasker;
import me.liwk.karhu.util.update.MovementUpdate;
import me.liwk.karhu.world.nms.NMSValueParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class PacketProcessor extends SimplePacketListenerAbstract {
	private final Karhu plugin;
	private final PassiveExpiringSet<UUID> closedConnections = new PassiveExpiringSet<>(1000L);

	public PacketProcessor(Karhu plugin) {
		super(PacketListenerPriority.MONITOR);
		this.plugin = plugin;
	}

	public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
		long nano = e.getTimestamp();
		long nowTimeMillis = System.currentTimeMillis();
		PacketPlayReceiveEvent cloned = null;
		if (e.getUser() != null && e.getUser().getUUID() != null) {
			KarhuPlayer data = this.plugin.getDataManager().getPlayerData(e.getUser());
			Client type = e.getPacketType();
			boolean handleOthers = true;
			if (data != null) {
				if (data.getBukkitPlayer() == null && e.getPlayer() != null) {
					data.setBukkitPlayer((Player)e.getPlayer());
				}

				if (WrapperPlayClientPlayerFlying.isFlying(type)) {
					cloned = e.clone();
					WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(e);
					Location location = packet.getLocation();
					if (Karhu.getInstance().getConfigManager().isAnticrash()
						&& Karhu.getInstance().getConfigManager().isLargeMove()
						&& (
							Math.abs(location.getX()) > 3.0E7
								|| Math.abs(location.getY()) > 3.0E7
								|| Math.abs(location.getZ()) > 3.0E7
								|| (double)Math.abs(location.getPitch()) >= 1000.0
								|| Math.abs(location.getYaw()) >= Float.MAX_VALUE
						)) {
						e.setCancelled(true);
						data.handleKickAlert("Invalid position");
						handleOthers = false;
					}
				} else if (type == Client.CHAT_MESSAGE) {
					handleOthers = false;
					if (Karhu.SERVER_VERSION.isOlderThan(ServerVersion.V_1_19)) {
						WrapperPlayClientChatMessage packet = new WrapperPlayClientChatMessage(e);
						String chat = packet.getMessage();
						if (chat != null && chat.toLowerCase().contains("${")) {
							e.setCancelled(true);
						}
					}
				} else if (type == Client.PLUGIN_MESSAGE) {
					cloned = e.clone();
					WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(e);
					Object channelObject = packet.getChannelName();
					String channelName;
					if (channelObject != null) {
						channelName = (String)channelObject;
					} else {
						ResourceLocation resourceLocation = (ResourceLocation)channelObject;
						channelName = resourceLocation.getNamespace() + ":" + resourceLocation.getKey();
					}

					if (packet.getChannelName().equals("MC|Brand") || channelName.equals("minecraft:brand")) {
						byte[] dataBytes = packet.getData();
						String brand = new String(Arrays.copyOfRange(dataBytes, 1, dataBytes.length));
						data.setBrand(brand);
						brand = brand.replaceAll("[^a-zA-Z0-9_-]", "");
						brand = brand.replaceAll("Velocity", "");
						if (brand.equalsIgnoreCase("Cave Client")) {
							brand = "Cave Client";
						} else if (brand.contains("lunarclient")) {
							brand = "Lunar";
						} else if (brand.contains("PLC18")) {
							brand = "PvPLounge";
						} else if (brand.contains("fmlforge")) {
							brand = "Forge";
						} else if (brand.contains("salwyrr")) {
							brand = "Salwyrr";
						}

						String brand2;
						try {
							brand2 = brand.toUpperCase().charAt(0) + brand.substring(1);
						} catch (StringIndexOutOfBoundsException var17) {
							brand2 = brand;
						}

						if (brand2.length() > 30) {
							brand2 = "INVALID_BRAND";
						}

						data.setCleanBrand(brand2);
						if (!brand.equalsIgnoreCase("vanilla") && !data.isBrandPosted() && Karhu.getInstance().getConfigManager().isClientCheck()) {
							data.setBrandPosted(true);
							MiscellaneousAlertPoster.postMisc(
								Karhu.getInstance().getConfigManager().getClientCheckMessage().replace("%player%", data.getName()).replace("%brand%", brand2), data, "Brand"
							);
						}

						String unallowed = Karhu.getInstance().getConfigManager().getConfig().getString("unallowed-brands.brands", "Vivecraft");
						if (unallowed.contains(brand)) {
							Tasker.run(
								() -> data.getBukkitPlayer()
										.kickPlayer(ChatColor.translateAlternateColorCodes('&', Karhu.getInstance().getConfigManager().getConfig().getString("unallowed-brands.kick-msg")))
							);
						}
					}
				}

				if (handleOthers) {
					if (!data.isRemovingObject()) {
						PacketPlayReceiveEvent finalCloned = cloned == null ? e.clone() : cloned;
						data.getThread().getExecutorService().execute(() -> {
							try {
								this.handlePlayReceive(finalCloned, data, nano, nowTimeMillis);
							} catch (CloneNotSupportedException var8x) {
								throw new RuntimeException(var8x);
							}

							finalCloned.cleanUp();
						});
					} else if (cloned != null) {
						cloned.cleanUp();
					}
				} else if (cloned != null) {
					cloned.cleanUp();
				}
			}
		}
	}

	public void onPacketPlaySend(PacketPlaySendEvent e) {
		long nanoTime = e.getTimestamp();
		PacketPlaySendEvent cloned = null;
		if (e.getUser() != null && e.getUser().getUUID() != null) {
			KarhuPlayer data = this.plugin.getDataManager().getPlayerData(e.getUser());
			if (data != null) {
				Server packetID = e.getPacketType();
				boolean handleAsync = true;
				if (data.getBukkitPlayer() == null && e.getPlayer() != null) {
					data.setBukkitPlayer((Player)e.getPlayer());
				}

				if (!data.isRemovingObject()) {
					switch (packetID) {
						case ENTITY_EFFECT:
							cloned = e.clone();
							WrapperPlayServerEntityEffect packet = new WrapperPlayServerEntityEffect(e);
							boolean via = Karhu.getInstance().isViaVersion();
							PotionType type = packet.getPotionType();
							int typeId = type.getId(data.getClientVersion());
							if (data.getClientVersion().getProtocolVersion() <= 47 && via && typeId > 23) {
								e.setCancelled(true);
								handleAsync = false;
							} else if (!data.isNewerThan12() && via && typeId == 30) {
								e.setCancelled(true);
								handleAsync = false;
							}
							break;
						case PING:
							WrapperPlayServerPing ping = new WrapperPlayServerPing(e);
							if (ping.getId() > 32767 || ping.getId() < -32768) {
								return;
							}

							short id = (short)ping.getId();
							data.getThread().getExecutorService().execute(() -> Karhu.getInstance().getTransactionHandler().handleTransaction(id, nanoTime, data));
							handleAsync = false;
							break;
						case WINDOW_CONFIRMATION:
							WrapperPlayServerWindowConfirmation transaction = new WrapperPlayServerWindowConfirmation(e);
							short tid = transaction.getActionId();
							if (!transaction.isAccepted()) {
								data.getThread().getExecutorService().execute(() -> Karhu.getInstance().getTransactionHandler().handleTransaction(tid, nanoTime, data));
							}

							handleAsync = false;
					}

					if (handleAsync) {
						PacketPlaySendEvent finalCloned = cloned == null ? e.clone() : cloned;
						data.getThread().getExecutorService().execute(() -> {
							this.handlePacketPlaySend(finalCloned, data, nanoTime);
							finalCloned.cleanUp();
						});
					} else if (cloned != null) {
						cloned.cleanUp();
					}
				}
			}
		}
	}

	public void handlePostPlayReceive(WrapperPlayClientPlayerFlying packet, KarhuPlayer data) throws CloneNotSupportedException {
		long now = System.currentTimeMillis();
		if (!packet.hasPositionChanged()
			&& !packet.hasRotationChanged()
			&& packet.isOnGround() == data.isLastOnGroundPacket()
			&& !data.recentlyTeleported(2)
			&& !data.isPossiblyTeleporting()
			&& Karhu.getInstance().getServerTick() - data.getCreatedOnTick() > 80L
			&& !data.isViaMCP()
			&& data.isNewerThan8()) {
			Tasker.run(
				() -> Karhu.getInstance()
						.getAlertsManager()
						.getAlertsToggled()
						.stream()
						.<Player>map(Bukkit::getPlayer)
						.filter(Objects::nonNull)
						.forEach(staff -> staff.sendMessage("§7[§c!§7] §c" + data.getBukkitPlayer().getName() + " §7is on a protocol that is related to hacked clients"))
			);
			data.setViaMCP(true);
		}

		data.setLastInBed(data.isInBed());
		data.wasFlyingC = data.flyingC;
		if (!data.isCorrectedFly() && data.isInitialized()) {
			Tasker.run(() -> {
				if (Karhu.getInstance().getServerTick() - data.getServerTick() > 40L && !data.isAllowFlying() && data.getBukkitPlayer().getAllowFlight()) {
					data.getBukkitPlayer().setAllowFlight(true);
					data.setCorrectedFly(true);
				}
			});
		}

		VelocityHandler.handle(data);
		EntityLocationHandler.updateFlyingLocations(data, packet);
		if (data.getTeleportManager().teleportTicks != 0 && !data.isSeventeenPlacing()) {
			EntityLocationHandler.updateEntityLocations(data);
		}

		if (data.getTasks().containsKey(data.getTotalTicks())) {
			data.getTasks().remove(data.getTotalTicks()).consumeTask();
		}

		data.getTeleportManager().handlePostFlying();
		data.getLastTargets().clear();
		data.getDesyncedBlockHandler().getClientSideBlocks().removeIf(block -> data.getServerTick() - block.getServerTick() > 2L);
		if (data.getTickedVelocity() != null) {
			data.setTickedVelocity(null);
		}

		data.setAttacks(0);
		if (!data.isDidFlagMovement()) {
			boolean mathGround = MathUtil.onGround(Math.abs(data.getLocation().y));
			double distance = data.getLastLocation().distance(packet.getLocation());
			boolean ground = data.isOnGroundServer() && packet.isOnGround() && (mathGround || data.getMoveTicks() <= 1);
			boolean inside = data.isInsideBlock();
			boolean teleport = data.isPossiblyTeleporting() || data.getTeleportManager().teleportsPending > 0;
			if (ground && !inside && !teleport && distance <= 10.0 && now - data.getLastLocationUpdate() > 40L) {
				if (++data.invalidMovementTicks > 5) {
					CustomLocation groundSet = data.getLocation().clone();
					CustomLocation safeSet = data.getLocation().clone();
					if (mathGround) {
						groundSet.setY(groundSet.getY() + 0.1);
						safeSet.setY(safeSet.getY() + 0.1);
					}

					if (data.invalidMovementTicks > 100) {
						groundSet.setY(groundSet.getY() - 0.1);
						safeSet.setY(groundSet.getY() - 0.1);
						data.invalidMovementTicks = 0;
					}

					data.setSafeGroundSetback(groundSet);
					data.setSafeSetback(safeSet);
				}

				data.setLastLocationUpdate(now);
			}

			data.getLocation().setCheats(false);
			data.getLocation().setTeleport(false);
		}

		data.setDidFlagMovement(false);
	}

	public void handlePlayReceive(PacketPlayReceiveEvent e, KarhuPlayer data, long nanoTime, long timeMillis) throws CloneNotSupportedException {
		Client type = e.getPacketType();
		if (data != null) {
			boolean isFlying = WrapperPlayClientPlayerFlying.isFlying(type);
			WrapperPlayClientPlayerFlying packet = null;
			Event callEvent = null;
			Karhu.getInstance().getTransactionHandler().handlePlayReceive(e, nanoTime, data);
			if (isFlying) {
				packet = new WrapperPlayClientPlayerFlying(e);
				Location location = packet.getLocation();
				data.setSeventeenPlacing(false);
				if (data.isBadClientVersion() && data.getBukkitPlayer() != null) {
					User userCheck = PacketEvents.getAPI().getPlayerManager().getUser(data.getBukkitPlayer());
					if (userCheck != null) {
						data.updateClientVersion(userCheck.getClientVersion());
					}
				}

				data.getAbilityManager().onFlying();
				data.getTeleportManager().handlePreFlying(packet);
				if (data.getTotalTicks() <= 3 && data.getTeleportManager().teleportAmount == 0) {
					data.setFuckedTeleport(true);
				}

				data.setTotalTicks(data.getTeleportManager().teleportTicks != 0 ? data.getTotalTicks() + 1 : data.getTotalTicks());
				long packetDiff = (long)((double)(nanoTime - data.getLastFlying()) / 1000000.0);
				int nowTicks = data.getTotalTicks();
				data.lastPacketDrop = packetDiff > 2L && packetDiff < 90L ? data.lastPacketDrop : nowTicks;
				data.setFlyingTime(packetDiff);
				data.setLastFlying(nanoTime);
				data.setLastFlyingTicks(nowTicks);
				PlayerHandler.checkConditions(data);
				float yaw = location.getYaw();
				float pitch = location.getPitch();
				boolean position = packet.hasPositionChanged();
				boolean look = packet.hasRotationChanged();
				boolean ground = packet.isOnGround();
				double x;
				double y;
				double z;
				if (position) {
					x = location.getX();
					y = location.getY();
					z = location.getZ();
					data.setMoveTicks(data.getMoveTicks() + 1);
					data.setNoMoveTicks(0);
				} else {
					x = data.getLocation().getX();
					y = data.getLocation().getY();
					z = data.getLocation().getZ();
					data.setMoveTicks(0);
					data.setNoMoveTicks(data.getNoMoveTicks() - 1);
				}

				ItemStack stack8 = data.getStackInHand();
				data.setLastUsingItem(data.isUsingItem());
				data.setLastEating(data.isEating());
				if (data.getClientVersion().getProtocolVersion() <= 47 && stack8.getType().isBlock()) {
					data.setUsingItem(false);
					data.setEating(false);
				}

				if (packetDiff < 25L) {
					data.setLastFast(nanoTime);
				}

				if (data.isAllowFlying() || data.isFlying() || data.isFlyingBukkit() || data.isAllowFlyingBukkit()) {
					data.setLastFlyTick(nowTicks);
				}

				if (data.isAllowFlying() || data.isSpectating() || data.isAllowFlyingBukkit()) {
					data.setLastAllowFlyTick(nowTicks);
				}

				if (data.getClientVersion().getProtocolVersion() > 754 && position && look) {
					double distance = data.getLocation().distance(location);
					if (data.getPositionPackets() > 0 && distance == 0.0 && !data.recentlyTeleported(2)) {
						data.setTotalTicks(Math.max(0, data.getTotalTicks() - 1));
						data.setSeventeenPlacing(true);
						return;
					}
				}

				if (position) {
					data.setPositionPackets(data.getPositionPackets() + 1);
					if (data.getPositionPackets() % 10 == 1) {
						if (data.getSafeGroundSetback() == null) {
							data.setSafeGroundSetback(data.getLocation().clone());
						}

						data.setSafeSetback(data.getLocation().clone());
						data.setFlyCancel(data.getLocation().clone());
					}
				}

				++data.lastAttackTick;
				data.setLastLastOnGroundPacket(data.isLastOnGroundPacket());
				data.setLastOnGroundPacket(data.isOnGroundPacket());
				data.setOnGroundPacket(ground);
				data.setLastSprintTick(data.isSprinting() ? data.getTotalTicks() : data.getLastSprintTick());
				data.setLastSneakTick(data.isSneaking() ? data.getTotalTicks() : data.getLastSneakTick());
				data.setLastLastLastLocation(data.getLastLastLocation().clone());
				data.setLastLastLocation(data.getLastLocation().clone());
				data.setLastLocation(data.getLocation().clone());
				data.getLocation().setGround(packet.isOnGround());
				data.setWasWasInUnloadedChunk(data.isWasInUnloadedChunk());
				data.setWasInUnloadedChunk(data.isInUnloadedChunk());
				data.setInUnloadedChunk(!BlockUtil.chunkLoaded(data.getLocation().toLocation(data.getWorld())));
				data.setLastInUnloadedChunk(data.isInUnloadedChunk() ? data.getTotalTicks() : data.getLastInUnloadedChunk());
				EffectManager eManager = data.getEffectManager();
				data.jumpBoost = eManager.getEffectStrenght(PotionEffect.JUMP_BOOST);
				data.speedBoost = eManager.getEffectStrenght(PotionEffect.SPEED);
				data.slowness = eManager.getEffectStrenght(PotionEffect.SLOWNESS);
				data.haste = eManager.getEffectStrenght(PotionEffect.HASTE);
				data.fatigue = eManager.getEffectStrenght(PotionEffect.MINING_FATIGUE);
				data.dolphinLevel = eManager.getEffectStrenght(PotionEffect.DOLPHIN_GRACE);
				data.slowFallingLevel = eManager.getEffectStrenght(PotionEffect.SLOW_FALLING);
				data.levitationLevel = eManager.getEffectStrenght(PotionEffect.LEVITATION);
				if (position) {
					data.getLocation().setPosition(location.getX(), location.getY(), location.getZ());
					if (data.isPossiblyTeleporting()) {
						data.setLastTeleport(data.getTotalTicks());
					}

					if (data.getTeleportManager().teleportTicks == 0) {
						data.getLastLocation().setPosition(location.getX(), location.getY(), location.getZ());
						data.setClientAirTicks(0);
						data.setAirTicks(0);
					}

					data.getWrappedEntity().setPosition(x, y, z);
					if (!data.isLocationInited()) {
						data.setLocationInited(true);
						data.setLocationInitedAt(nowTicks);
					}
				}

				if (look) {
					data.getLastLocation().setRotation(data.getLocation().yaw, data.getLocation().pitch);
					data.getLocation().setRotation(yaw, pitch);
				}

				data.getMovementHandler().handleMotions(position, look);
				boolean run = data.getLocation().distance(data.getLastLocation()) > 0.0 || data.getLocation().distance(data.getLastLastLocation()) > 0.0;
				if (run) {
					data.getCollisionHandler().handleLastTicks();
					data.getCollisionHandler().handle(position);
					data.getCollisionHandler().handleTicks();
				} else if (data.isForceRunCollisions()) {
					data.getCollisionHandler().handleLastTicks();
					data.getCollisionHandler().handle(position);
					data.getCollisionHandler().handleTicks();
					data.setForceRunCollisions(false);
				} else {
					data.getCollisionHandler().handleLastTicks();
					data.getCollisionHandler().handleTicks();
				}

				data.getMovementHandler().handleOther(ground);
				if (data.isNeedExplosionAdditions()) {
					double velocityX = data.getVelocityX();
					double velocityY = data.getVelocityY();
					double velocityZ = data.getVelocityZ();
					data.setVelocityX(data.deltas.lastDX + velocityX);
					data.setVelocityY(data.deltas.lastMotionY + velocityY);
					data.setVelocityZ(data.deltas.lastDZ + velocityZ);
					data.setVelocityHorizontal(MathUtil.hypot(data.getVelocityX(), data.getVelocityZ()));
					data.setNeedExplosionAdditions(false);
					data.setExplosionExempt(data.getTotalTicks());
				}

				NMSValueParser.parse(data);
				float friction = data.isOnGroundPacket() ? data.getCurrentFriction() : 0.91F;
				double xDiff = data.deltas.deltaX * (double)friction;
				double zDiff = data.deltas.deltaZ * (double)friction;
				double prediction = (data.deltas.motionY - 0.08) * 0.98F;
				if (xDiff * xDiff + prediction * prediction + zDiff * zDiff <= 9.0E-4) {
					data.deltas.predictX = xDiff * xDiff;
					data.deltas.predictY = prediction * prediction;
					data.deltas.predictZ = zDiff * zDiff;
					data.setPredictionTicks(data.getTotalTicks());
				}

				data.getDesyncedBlockHandler().handleFlying(position, ground, false);
				data.setPlacedInside(data.getDesyncedBlockHandler().checkInsidePlace());
				data.setPlacedCancel(data.getDesyncedBlockHandler().checkBelowPlace());
				data.setLastPlacedInside(data.isPlacedInside() ? data.getTotalTicks() : data.getLastPlacedInside());
				double chunkMove = data.getLocation().y > 0.0 ? 0.09800000190735147 : 0.0;
				boolean exemptable = data.getTickedVelocity() != null;
				boolean unload = Math.abs(data.deltas.motionY + chunkMove) <= 1.0E-7;
				if (unload
					&& data.elapsed(data.getLastInUnloadedChunk()) > MathUtil.getPingInTicks(data.getTransactionPing()) + 8
					&& !exemptable
					&& !data.isDidFlagMovement()
					&& !data.isPossiblyTeleporting()
					&& data.elapsed(data.getLastFlyTick()) > 30) {
					Check<?> flyA = data.getCheckManager().getCheck(FlyA.class);
					if (Karhu.getInstance().getCheckState().isEnabled(flyA.getName())) {
						flyA.setViolations(flyA.getViolations() + 1.0);
						if (flyA.getViolations() >= 3.0) {
							flyA.disallowMove(true);
							flyA.setViolations(0.0);
						}
					}
				}

				data.setLastPossibleInUnloadedChunk(unload ? data.getTotalTicks() : data.getLastPossibleInUnloadedChunk());
				if (look) {
					data.getCheckManager()
						.runChecks(data.getCheckManager().getRotationChecks(), new MovementUpdate(data.getLastLastLocation(), data.getLastLocation(), data.getLocation(), packet.isOnGround()), null);
				}

				boolean teleport = data.getTeleportManager().teleportTicks == 0;
				callEvent = new FlyingEvent(data.getLocation().getX(), data.getLocation().getY(), data.getLocation().getZ(), yaw, pitch, position, look, ground, teleport, nanoTime, timeMillis);
				data.setElapsedOnLiquid(data.isOnLiquid() ? data.getElapsedOnLiquid() + 1 : 0);
				data.setElapsedUnderBlock(data.isUnderBlock() ? data.getElapsedUnderBlock() + 1 : 0);
				data.setWasPlacing(data.isPlacing());
				data.setPlacing(false);
				data.setWasWasSneaking(data.isWasSprinting());
				data.setWasSprinting(data.isSprinting());
				data.setWasWasSneaking(data.isWasSneaking());
				data.setWasSneaking(data.isSneaking());
				data.getCrashHandler().handleFlying(position, look, data.getLocation(), data.getLastLocation());
				data.tick();
				if (!data.getVelocityPending().isEmpty()) {
					data.checkVelocity();
				}

				if (data.isPossiblyTeleporting()) {
					data.setVelocityX(0.0);
					data.setVelocityY(0.0);
					data.setVelocityZ(0.0);
					data.setVelocityHorizontal(0.0);
					data.setTickedVelocity(null);
					data.setTakingVertical(false);
				}

				if (position) {
					data.getCheckManager()
						.runChecks(data.getCheckManager().getPositionChecks(), new MovementUpdate(data.getLastLastLocation(), data.getLastLocation(), data.getLocation(), packet.isOnGround()), null);
				}

				if (Karhu.getInstance().getConfigManager().isPingKick()) {
					if (data.getTransactionPing() > (long)Karhu.getInstance().getConfigManager().getPingKickMaxPing()) {
						if (++data.badPingTicks >= Karhu.getInstance().getConfigManager().getPingKickTicks()) {
							Tasker.run(() -> data.getBukkitPlayer().kickPlayer(Karhu.getInstance().getConfigManager().getPingKickMsg()));
						}
					} else {
						data.badPingTicks = Math.max(data.getBadPingTicks() - 70, 0);
					}
				}
			} else {
				switch (type) {
					case PONG:
					case WINDOW_CONFIRMATION:
						callEvent = new TransactionEvent(nanoTime);
						break;
					case INTERACT_ENTITY:
						WrapperPlayClientInteractEntity use = new WrapperPlayClientInteractEntity(e);
						int id = use.getEntityId();
						Entity entity = SpigotReflectionUtil.getEntityById(id);
						EntityData eData = data.getEntityData().get(id);
						if (eData == null) {
							return;
						}

						EntityType entityType = eData.getType();
						boolean isPlayer = Karhu.SERVER_VERSION.isOlderThan(ServerVersion.V_1_19) ? entity instanceof Player : EntityTypes.PLAYER.equals(entityType);
						if (use.getAction() == InteractAction.ATTACK) {
							callEvent = new AttackEvent(id, isPlayer, nanoTime, timeMillis);
							data.lastAttackTick = 0;
							++data.attacks;
							data.setLastAttackPacket(nanoTime);
							data.setDigTicks(0);
							if (isPlayer) {
								LivingEntity target = (LivingEntity)entity;
								data.setLastTarget(target);
								data.getLastTargets().add(id);
							} else {
								data.setLastTarget(null);
							}

							data.setLastAbortLoc(null);
							data.setDigging(false);
							data.setUsingItem(false);
							data.setEating(false);
							data.setBlocking(false);
						} else if ((use.getAction() == InteractAction.INTERACT || use.getAction() == InteractAction.INTERACT_AT) && entity instanceof LivingEntity) {
							callEvent = new InteractEvent(id, isPlayer, use.getTarget().isPresent() ? (Vector3f)use.getTarget().get() : null, use.getAction() == InteractAction.INTERACT_AT, nanoTime);
						}
						break;
					case ANIMATION:
						Vector aborted = data.getLastAbortLoc();
						if (aborted != null) {
							boolean state = data.getLocation().distance(new CustomLocation(aborted.getX(), aborted.getY(), aborted.getZ())) < 7.0;
							if (data.getLastAttackTick() <= 1) {
								state = false;
								data.setLastAbortLoc(null);
							}

							if (!state) {
								if (!data.isPossiblyTeleporting()) {
									data.setDigging(state);
								}
							} else {
								data.setDigging(state);
							}
						}

						if (data.isHasDig2()) {
							data.setDigTicks(data.getTotalTicks());
						}

						data.getCrashHandler().handleArm();
						if (!data.isSkipNextSwing()) {
							callEvent = new SwingEvent(nanoTime, timeMillis);
						} else {
							data.setSkipNextSwing(false);
						}
						break;
					case STEER_VEHICLE:
						WrapperPlayClientSteerVehicle steerVehicle = new WrapperPlayClientSteerVehicle(e);
						callEvent = new SteerEvent(steerVehicle.isUnmount());
						break;
					case CLICK_WINDOW:
						WrapperPlayClientClickWindow winClick = new WrapperPlayClientClickWindow(e);
						data.getCrashHandler().handleWindowClick(winClick.getSlot(), winClick.getStateId().orElse(0), winClick.getWindowId(), winClick.getButton());
						callEvent = new WindowEvent(nanoTime);
						break;
					case PLAYER_ABILITIES:
						WrapperPlayClientPlayerAbilities abilities = new WrapperPlayClientPlayerAbilities(e);
						data.getAbilityManager().onAbilityClient(abilities);
						callEvent = new AbilityEvent();
						break;
					case CLIENT_STATUS:
						WrapperPlayClientClientStatus status = new WrapperPlayClientClientStatus(e);
						if (status.getAction() == Action.OPEN_INVENTORY_ACHIEVEMENT) {
							data.setBlocking(false);
							data.setUsingItem(false);
							data.setEating(false);
							data.setInventoryOpen(true);
							data.setInvStamp(data.getTotalTicks());
						}
						break;
					case CLOSE_WINDOW:
						data.setInventoryOpen(false);
						break;
					case ENTITY_ACTION:
						WrapperPlayClientEntityAction action = new WrapperPlayClientEntityAction(e);
						switch (action.getAction()) {
							case START_SPRINTING:
								data.setWasSprinting(data.isSprinting());
								data.setSprinting(true);
								data.setDesyncSprint(false);
								break;
							case STOP_SPRINTING:
								data.setWasSprinting(data.isSprinting());
								data.setSprinting(false);
								data.setDesyncSprint(false);
								data.setRecorrectingSprint(false);
								break;
							case START_SNEAKING:
								data.setWasSneaking(data.isSneaking());
								data.setSneaking(true);
								break;
							case STOP_SNEAKING:
								data.setWasSneaking(data.isSneaking());
								data.setSneaking(false);
								break;
							case START_FLYING_WITH_ELYTRA:
								data.setGliding(true);
						}

						callEvent = new ActionEvent(action.getAction());
						break;
					case KEEP_ALIVE:
						new WrapperPlayClientKeepAlive(e);
						data.setPing(MathUtil.toMillis(nanoTime - data.getLastPingTime()));
						data.getCrashHandler().handleClientKeepAlive();
						data.setHasReceivedKeepalive(true);
						callEvent = new ConnectionHeartbeatEvent();
						break;
					case PLAYER_DIGGING:
						WrapperPlayClientPlayerDigging dig = new WrapperPlayClientPlayerDigging(e);
						Vector position = new Vector(dig.getBlockPosition().x, dig.getBlockPosition().y, dig.getBlockPosition().z);
						switch (dig.getAction()) {
							case START_DIGGING:
								data.setDigging(true);
								data.setDiggingBasic(true);
								data.setDigTicks(data.getTotalTicks());
								if (Karhu.SERVER_VERSION.isOlderThanOrEquals(ServerVersion.V_1_12_2) && !data.isInUnloadedChunk()) {
									org.bukkit.Location blockIn = new org.bukkit.Location(data.getWorld(), position.getX(), position.getY(), position.getZ());
									Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(blockIn);
									if (block != null && MaterialChecks.ONETAPS.contains(blockIn.getBlock().getType()) && data.getLocation().toVector().distance(position) <= 2.0) {
										data.setFastDigTicks(data.getTotalTicks());
									}
								}
								break;
							case DROP_ITEM:
								if (data.getClientVersion().isNewerThan(ClientVersion.V_1_14_4)) {
									data.setSkipNextSwing(true);
								}
							case RELEASE_USE_ITEM:
							case SWAP_ITEM_WITH_OFFHAND:
							case DROP_ITEM_STACK:
								data.setBlocking(false);
								data.setUsingItem(false);
								data.setEating(false);
								break;
							case CANCELLED_DIGGING:
								data.setDiggingBasic(false);
								data.setLastAbortLoc(position);
								break;
							case FINISHED_DIGGING:
								data.setDigging(false);
								data.setDiggingBasic(false);
								data.setLastAbortLoc(null);
								data.setDigStopTicks(data.getTotalTicks());
						}

						callEvent = new DigEvent(position, dig.getBlockFace().getFaceValue(), dig.getAction(), nanoTime);
						break;
					case PLAYER_BLOCK_PLACEMENT:
					case USE_ITEM:
						if (type == Client.PLAYER_BLOCK_PLACEMENT && Karhu.SERVER_VERSION.isOlderThan(ServerVersion.V_1_9)) {
							WrapperPlayClientPlayerBlockPlacement place = new WrapperPlayClientPlayerBlockPlacement(e);
							int x = place.getBlockPosition().getX();
							int y = place.getBlockPosition().getY();
							int z = place.getBlockPosition().getZ();
							Vector loc = new Vector(x, y, z);
							ItemStack stack = VersionBridgeHelper.getStackInHand(data);
							ItemStack stack8 = data.getStackInHand();
							Material stackType = stack8.getType();
							BlockFace face = place.getFace();
							boolean placed = false;
							boolean pebug = loc.getY() == 4095.0;
							if (loc.getX() != -1.0 || loc.getY() != 255.0 && !pebug && loc.getY() != -1.0 || loc.getZ() != -1.0 || face.getFaceValue() != 255 && !pebug) {
								data.setSpoofPlaceTicks(data.getTotalTicks());
								boolean bucket = MaterialChecks.LIQUID_BUCKETS.contains(stackType);
								if (stack.getType().isBlock() || bucket) {
									double diffX = Math.abs(data.getLocation().x - (double)loc.getBlockX());
									double diffZ = Math.abs(data.getLocation().z - (double)loc.getBlockZ());
									double blockY = (double)loc.getBlockY();
									if (diffX <= 3.0 && blockY <= data.getLocation().y + 3.0 && diffZ <= 3.0) {
										data.setPlaceTicks(data.getTotalTicks());
										if (bucket) {
											data.setBucketTicks(data.getTotalTicks());
										}
									}

									if (diffX <= 2.0 && blockY <= data.getLocation().y + 2.0 && diffZ <= 2.0) {
										data.setUnderPlaceTicks(data.getTotalTicks());
									}

									data.setPlacing(true);
								}

								placed = true;
							} else if (stack8.getDurability() <= 16384) {
								boolean eating = false;
								boolean blocking = false;
								boolean bowing = false;
								if (MaterialChecks.SWORDS.contains(stackType)) {
									blocking = true;
									data.setBlocking(true);
								} else if (MaterialChecks.BOWS.contains(stackType) && data.getBukkitPlayer().getInventory().contains(Material.ARROW)) {
									bowing = true;
									data.setBowing(true);
								} else if (stack8.getType().isEdible() && (data.getBukkitPlayer().getFoodLevel() < 20 || MaterialChecks.EDIBLE_WITHOUT_HUNGER.contains(stackType))) {
									eating = true;
								}

								if (blocking || bowing || eating) {
									data.setUsingItem(true);
									if (eating) {
										data.setEating(true);
									}

									data.setUseTicks(data.getTotalTicks());
								}
							}

							switch (face.getFaceValue()) {
								case 0:
									--y;
									break;
								case 1:
									++y;
									break;
								case 2:
									--z;
									break;
								case 3:
									++z;
									break;
								case 4:
									--x;
									break;
								case 5:
									++x;
							}

							Vector placedLocation = new Vector(x, y, z);
							Vector3f cursor = place.getCursorPosition();
							if (placed) {
								org.bukkit.Location against = new org.bukkit.Location(data.getWorld(), loc.getX(), loc.getY(), loc.getZ());
								Block blockAgainst = Karhu.getInstance().getChunkManager().getChunkBlockAt(against);
								Material material = blockAgainst == null ? Material.AIR : blockAgainst.getType();
								DesyncedBlockHandler blockHandler = data.getDesyncedBlockHandler();
								Material itemInHand = data.getStackInHand().getType();
								boolean invalid = false;
								if (MaterialChecks.AIR.contains(material) || MaterialChecks.LIQUIDS.contains(material)) {
									invalid = true;
								}

								if (!invalid) {
									BlockPlacePending bpp = new BlockPlacePending(placedLocation, face.getFaceValue(), Karhu.getInstance().getServerTick(), itemInHand);
									blockHandler.getClientSideBlocks().add(bpp);
									blockHandler.invalidPlaces = Math.max(blockHandler.invalidPlaces - 1, 0);
								} else {
									++blockHandler.invalidPlaces;
								}
							}

							callEvent = new BlockPlaceEvent(
								placedLocation, loc, stack8, (double)cursor.getX(), (double)cursor.getY(), (double)cursor.getZ(), face, face.getFaceValue(), nanoTime, timeMillis, data.getWorld()
							);
							data.getCrashHandler().handlePlace();
						} else if (type == Client.USE_ITEM && Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_8_8)) {
						}
						break;
					case HELD_ITEM_CHANGE:
						WrapperPlayClientHeldItemChange held = new WrapperPlayClientHeldItemChange(e);
						data.setWasPlacing(data.isPlacing());
						data.setUsingItem(false);
						data.setEating(false);
						data.setBlocking(false);
						data.setPlacing(false);
						data.setSlotSwitchTick(data.getTotalTicks());
						data.setCurrentSlot(held.getSlot());
						callEvent = new HeldItemSlotEvent(held.getSlot());
						data.getCrashHandler().handleSlot();
						break;
					case PLUGIN_MESSAGE:
						data.getCrashHandler().handleCustomPayload();
						break;
					case VEHICLE_MOVE:
						WrapperPlayClientVehicleMove move = new WrapperPlayClientVehicleMove(e);
						if (data.isRiding()) {
							data.setVehicleX(move.getPosition().getX());
							data.setVehicleY(move.getPosition().getY());
							data.setVehicleZ(move.getPosition().getZ());
						}

						callEvent = new VehicleEvent(move.getPosition().getX(), move.getPosition().getY(), move.getPosition().getZ());
				}
			}

			if (callEvent != null) {
				data.getCheckManager().runChecks(data.getCheckManager().getPacketChecks(), callEvent, null);
			}

			if (isFlying) {
				this.handlePostPlayReceive(packet, data);
			}
		}
	}

	public void handlePacketPlaySend(PacketPlaySendEvent e, KarhuPlayer data, long nanoTime) {
		Server packetID = e.getPacketType();
		if (data != null) {
			Event callEvent = null;
			TransactionHandler network = Karhu.getInstance().getTransactionHandler();
			network.handlePacketPlaySend(e, nanoTime, data);
			if (!data.isObjectLoaded()) {
				return;
			}

			switch (packetID) {
				case ENTITY_VELOCITY:
					WrapperPlayServerEntityVelocity velocity = new WrapperPlayServerEntityVelocity(e);
					Vector3d vecVelocity = velocity.getVelocity();
					if (velocity.getEntityId() == e.getUser().getEntityId()) {
						callEvent = new VelocityEvent(vecVelocity.getX(), vecVelocity.getY(), vecVelocity.getZ(), velocity.getEntityId());
					}
					break;
				case PLAYER_POSITION_AND_LOOK:
					WrapperPlayServerPlayerPositionAndLook position = new WrapperPlayServerPlayerPositionAndLook(e);
					Vector3d pos = new Vector3d(position.getX(), position.getY(), position.getZ());
					CustomLocation locationPlayer = data.getLocation();
					if (!data.isNewerThan8()) {
						if (position.isRelativeFlag(RelativeFlag.X)) {
							pos = pos.add(new Vector3d(locationPlayer.x, 0.0, 0.0));
						}

						if (position.isRelativeFlag(RelativeFlag.Y)) {
							pos = pos.add(new Vector3d(0.0, locationPlayer.y, 0.0));
						}

						if (position.isRelativeFlag(RelativeFlag.Z)) {
							pos = pos.add(new Vector3d(0.0, 0.0, locationPlayer.z));
						}

						position.setX(pos.getX());
						position.setY(pos.getY());
						position.setZ(pos.getZ());
						position.setRelativeMask((byte)0);
					}

					boolean checkBB = Karhu.SERVER_VERSION.isOlderThanOrEquals(ServerVersion.V_1_7_10);
					double x = pos.getX();
					double y = !checkBB ? pos.getY() : pos.getY() - 1.62F;
					double z = pos.getZ();
					Teleport teleport = new Teleport(new TeleportPosition(x, y, z));
					++data.getTeleportManager().teleportAmount;
					++data.getTeleportManager().teleportsPending;
					data.setSafeGroundSetback(teleport.position.toCLocation());
					data.setSafeSetback(teleport.position.toCLocation());
					data.setTeleportLocation(teleport.position.toCLocation());
					data.setFlyCancel(teleport.position.toCLocation());
					data.getDesyncedBlockHandler().setNoFakeWaterLocation(teleport.position.toLocation(data.getWorld()));
					callEvent = new PositionEvent(x, y, z, position.getYaw(), position.getPitch());
			}

			if (callEvent != null) {
				data.getCheckManager().runChecks(data.getCheckManager().getPacketChecks(), callEvent, null);
			}
		}
	}
}
