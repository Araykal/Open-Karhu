/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global.bukkit;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import java.util.UUID;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.antivpn.VPNCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import me.liwk.karhu.util.APICaller;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public final class BukkitHandler implements Listener {
	@EventHandler(
		priority = EventPriority.LOWEST
	)
	public void onJoin(PlayerJoinEvent event) {
		long now = System.nanoTime();
		Player player = event.getPlayer();
		if ((!Karhu.getInstance().getConfigManager().isGeyserSupport() || !PlayerUtil.isGeyserPlayer(player.getUniqueId()))
			&& (!Karhu.getInstance().getConfigManager().isGeyserPrefixCheck() || !player.getName().contains(Karhu.getInstance().getConfigManager().getGeyserPrefix()))) {
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(player.getUniqueId());
			if (data == null) {
				Karhu.getInstance().getDataManager().add(player.getUniqueId(), now);
			} else {
				data.setBukkitPlayer(event.getPlayer());
			}

			if (Karhu.isAPIAvailable()) {
				APICaller.callRegister(player);
			}

			boolean permission = player.hasPermission("karhu.alerts");
			Tasker.taskAsync(() -> {
				if (permission) {
					if (Karhu.getInstance().getConfigManager().isCrackedServer()) {
						Karhu.getInstance().getAlertsManager().setReceiveAlerts(player, Karhu.getStorage().getAlerts(player.getName()));
					} else {
						Karhu.getInstance().getAlertsManager().setReceiveAlerts(player, Karhu.getStorage().getAlerts(player.getUniqueId().toString()));
					}
				}

				if (data != null && !Karhu.getInstance().getConfigManager().isLogSync()) {
					if (!Karhu.getInstance().getConfigManager().isCrackedServer()) {
						Karhu.getStorage().loadActiveViolations(player.getUniqueId().toString(), data);
					} else {
						Karhu.getStorage().loadActiveViolations(player.getName(), data);
					}
				}
			});
		}
	}

	@EventHandler(
		priority = EventPriority.MONITOR
	)
	public void onJoinMonitor(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Karhu.getInstance().getConfigManager().isGeyserSupport() && PlayerUtil.isGeyserPlayer(player)) {
			Karhu.getInstance().printCool(ChatColor.RED + player.getName() + " joined using geyser");
			Karhu.getInstance().getDataManager().remove(player.getUniqueId());
		}
	}

	@EventHandler(
		priority = EventPriority.LOWEST
	)
	public void onQuit(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if (Karhu.getInstance().getAlertsManager().hasAlertsToggled(uuid)) {
			Karhu.getInstance().getAlertsManager().removeFromList(uuid);
		}

		Karhu.getInstance().getDataManager().remove(uuid);
		if (Karhu.isAPIAvailable()) {
			APICaller.callUnregister(null);
		}
	}

	@EventHandler(
		priority = EventPriority.HIGHEST,
		ignoreCancelled = true
	)
	public void onTeleport(PlayerTeleportEvent e) {
		if (Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_13)) {
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(e.getPlayer());
			if (data != null) {
				data.getCollisionHandler().cacheBlocks();
			}
		}
	}

	@EventHandler(
		priority = EventPriority.HIGHEST,
		ignoreCancelled = true
	)
	public void onMove(PlayerMoveEvent e) {
		Location to = e.getTo();
		Location from = e.getFrom();
		if (to != null && to.getWorld() == from.getWorld() && to.distanceSquared(from) >= 2.0E-4) {
			Player p = e.getPlayer();
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(p);
			if (data != null) {
				if (data.getBukkitPlayer() == null) {
					data.setBukkitPlayer(p);
				}

				if (++data.moveCalls % 5 == 0 && Karhu.SERVER_VERSION.isOlderThan(ServerVersion.V_1_19)) {
					data.getWrappedEntity().initChunks();
				}

				if (Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_13)) {
					data.getCollisionHandler().cacheBlocks();
				}
			}
		}
	}

	@EventHandler(
		priority = EventPriority.HIGHEST
	)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!e.isCancelled() && e.getDamager() instanceof Player) {
			Player player = (Player)e.getDamager();
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(player);
			if (data != null) {
				boolean hitbox = Karhu.getInstance().getConfigManager().isHitboxCancel();
				boolean reach = Karhu.getInstance().getConfigManager().isReachCancel();
				boolean tripleBlock = Karhu.getInstance().getConfigManager().isTriplehitBlock();
				if (data.isCancelNextHitR() && reach) {
					e.setDamage(0.0);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> hit cancelled after reach flag");
				} else if (data.isCancelNextHitH() && hitbox) {
					e.setDamage(0.0);
					e.setCancelled(true);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> hit cancelled after hitbox flag");
				}

				if (data.elapsed(data.getCancelHitsTick()) < 5) {
					e.setDamage(0.0);
					e.setCancelled(true);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> hit cancelled for suspicious actions");
				}

				if (data.isForceCancelReach() && e.getEntity().getEntityId() == data.getEntityIdCancel()) {
					e.setDamage(0.0);
					e.setCancelled(true);
					data.setForceCancelReach(false);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> hit cancelled, karhu doesn't track opponent locations");
				}

				if (tripleBlock && data.isCancelTripleHit()) {
					e.setDamage(0.0);
					e.setCancelled(true);
					data.setForceCancelReach(false);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> hit cancelled for triple hit");
				}

				if (data.isReduceNextDamage()) {
					e.setDamage(e.getDamage() * 0.75);
					data.setReduceNextDamage(false);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> damage slightly reduced for suspicious actions (1)");
				}

				if (data.isAbusingVelocity()) {
					e.setDamage(e.getDamage() * 0.7);
					data.setAbusingVelocity(false);
					MiscellaneousAlertPoster.postMitigation(player.getName() + " -> damage slightly reduced for suspicious actions (2)");
				}
			}
		}
	}

	@EventHandler(
		priority = EventPriority.HIGHEST
	)
	public void onBlockBreak(BlockBreakEvent e) {
		if (!e.isCancelled()) {
			Player player = e.getPlayer();
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(player);
			if (data != null && data.isCancelBreak()) {
				e.setCancelled(true);
				data.setCancelBreak(false);
			}
		}
	}

	@EventHandler(
		priority = EventPriority.MONITOR
	)
	public void onPreJoin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		if (Karhu.getInstance().getConfigManager().isAntivpn()
			&& (Karhu.getInstance().getConfigManager().isProxycheck() || Karhu.getInstance().getConfigManager().isMaliciouscheck())
			&& !Karhu.getInstance().getConfigManager().getAntiVpnBypass().contains(player.getUniqueId().toString())) {
			Karhu.getInstance().getAntiVPNThread().execute(() -> {
				if (VPNCheck.checkAddress(e.getAddress())) {
					e.disallow(Result.KICK_BANNED, Karhu.getInstance().getConfigManager().getAntivpnKickMsg());
				}
			});
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player p = event.getPlayer();
		KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(p);
		if (data != null) {
			this.recorrectPlayerStates(data);
		}
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			Player p = (Player)e.getEntity();
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(p);
			if (data != null && data.isRecorrectingSprint()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(
		priority = EventPriority.MONITOR,
		ignoreCancelled = true
	)
	public void onEvent(BlockPistonExtendEvent event) {
		World blockWorld = event.getBlock().getWorld();
		Location blockLocation = event.getBlock().getLocation();

		for (Player player : event.getBlock().getWorld().getPlayers()) {
			if (!blockWorld.equals(player.getWorld())) {
				return;
			}

			if (blockLocation.distance(player.getLocation()) <= 14.0) {
				KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(player);
				data.queueToPrePing(task -> {
					data.setLastPistonPush(data.getTotalTicks());
					data.setLastSlimePistonPush(data.getTotalTicks());
				});
			}
		}
	}

	@EventHandler(
		priority = EventPriority.MONITOR,
		ignoreCancelled = true
	)
	public void onEvent(BlockPistonRetractEvent event) {
		Block block = event.getBlock();
		Location blockLocation = block.getLocation();

		for (Player player : block.getWorld().getPlayers()) {
			if (!block.getWorld().equals(player.getWorld())) {
				return;
			}

			if (blockLocation.distance(player.getLocation()) <= 14.0) {
				KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(player);
				data.queueToPrePing(task -> {
					data.setLastPistonPush(data.getTotalTicks());
					data.setLastSlimePistonPush(data.getTotalTicks());
				});
			}
		}
	}

	private void recorrectPlayerStates(KarhuPlayer data) {
		data.setRecorrectingSprint(true);
		data.setDesyncSprint(true);
		data.setLastWorldChange(data.getTotalTicks());
		data.getBukkitPlayer().setSprinting(true);
		data.getBukkitPlayer().setSprinting(false);
	}
}
