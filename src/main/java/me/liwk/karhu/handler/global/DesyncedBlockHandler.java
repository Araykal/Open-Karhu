/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.setback.Setbacks;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.pending.BlockPlacePending;
import me.liwk.karhu.util.player.BlockUtil;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class DesyncedBlockHandler {
	private final KarhuPlayer data;
	private Location noFakeWaterLocation;
	private double desyncTicksUncertain;
	private double lavaDesyncTicks;
	private double waterDesyncTicks;
	private double wallTicks;
	private double uncertainTicks;
	private double aboveTicks;
	private double velocityAbuse;
	private int lastCollidesClientBlock;
	private boolean setbacked;
	private boolean setbackEnabled;
	private final Set<BlockPlacePending> clientSideBlocks = ConcurrentHashMap.newKeySet();
	public int invalidPlaces;

	public void handleFlying(boolean moved, boolean clientCollide, boolean moveEvent) {
		if (Karhu.getInstance().getConfigManager().isGhostBlock()) {
			this.setbackEnabled = Karhu.getInstance().getConfigManager().isGbLagback();
			boolean serverAir = this.data.getAirTicks() >= 3 + Math.min(10, MathUtil.getPingInTicks((this.data.getTransactionPing() + this.data.getLastTransactionPing()) / 2L));
			boolean serverCollideHuge = this.data.isGroundNearBox();
			boolean mathCollide = MathUtil.onGround(Math.abs(this.data.getLocation().y)) || this.data.getMoveTicks() <= 1;
			boolean unloadedChunk = this.data.elapsed(this.data.getLastInUnloadedChunk()) <= 3;
			boolean clientBlockCollide = this.collidesWithClientBlock(true) || this.checkClientSideBlock(this.data.getLocation().toVector(), 2.0);
			this.data.setOnGhostBlock(clientCollide && mathCollide && clientBlockCollide && serverAir);
			if (clientCollide && mathCollide && serverAir && !clientBlockCollide && this.data.elapsed(this.lastCollidesClientBlock) > 1 && !unloadedChunk) {
				this.handleBelowDesync(serverCollideHuge, moveEvent);
				this.data.setOnGhostBlock(true);
				if (!this.data.isAboveButNotInWater() && !this.data.isOnLiquid() && (this.setbacked || !this.setbackEnabled)) {
					this.data.setAirTicks(0);
				}
			} else {
				this.desyncTicksUncertain = Math.max(this.desyncTicksUncertain - 0.0075, 0.0);
			}

			if (clientCollide && serverAir && !mathCollide) {
				if (++this.uncertainTicks >= 2.0 && this.setbackEnabled && this.data.getMoveTicks() > 1) {
					CustomLocation location = Setbacks.forgeToRotatedLocation(this.data.getSafeGroundSetback(), this.data);
					if (!this.data.isPossiblyTeleporting() && !this.data.couldBeUnloadedClient() && this.data.elapsed(this.data.getLastFlyTick()) > 30) {
						Tasker.run(() -> {
							if (!this.data.isPossiblyTeleporting() && !this.data.couldBeUnloadedClient() && this.data.getTotalTicks() > 20) {
								this.data.teleport(location);
							}
						});
						this.data.setDidFlagMovement(true);
						this.data.setCancelHitsTick(this.data.getTotalTicks());
						this.data.setLastMovementFlag(this.data.getTotalTicks());
						Karhu.getInstance().printCool("&b> &fKarhu is cancelling hits for 20 ticks USER: " + this.data.getBukkitPlayer().getName());
					}

					MiscellaneousAlertPoster.postMiscPrivate("ghost tp BELOW (UCV2)");
				}
			} else {
				this.uncertainTicks = Math.max(this.uncertainTicks - 0.025, 0.0);
			}

			if (clientCollide && serverAir && this.invalidPlaces > 3 && this.setbackEnabled) {
				CustomLocation location = Setbacks.forgeToRotatedLocation(this.data.getSafeGroundSetback(), this.data);
				if (!this.data.isPossiblyTeleporting() && !this.data.couldBeUnloadedClient() && this.data.elapsed(this.data.getLastFlyTick()) > 30) {
					Tasker.run(() -> {
						if (!this.data.isPossiblyTeleporting() && !this.data.couldBeUnloadedClient() && this.data.getTotalTicks() > 20) {
							this.data.teleport(location);
						}
					});
					this.data.setDidFlagMovement(true);
					this.data.setLastMovementFlag(this.data.getTotalTicks());
				}

				--this.invalidPlaces;
				MiscellaneousAlertPoster.postMiscPrivate("ghost tp BELOW (PLACE)");
			}

			this.handleHorizontal(this.data.isFinalCollidedH());
			if (!clientCollide && !unloadedChunk) {
				this.handleAboveDesync();
			}

			if (Karhu.getInstance().getConfigManager().isLiquidDetect() && !unloadedChunk) {
				this.handleLiquidDesync(clientCollide);
			}

			this.lastCollidesClientBlock = clientBlockCollide ? this.data.getTotalTicks() : this.lastCollidesClientBlock;
			this.setbacked = false;
		}
	}

	public boolean checkInsidePlace() {
		return this.collidesWithClientBlock(false) && !this.data.isOnGroundServer() && !this.data.isOnGroundPacket();
	}

	public boolean checkBelowPlace() {
		return this.collidesWithClientBlock(true) && !this.data.isOnGroundServer();
	}

	private void handleBelowDesync(boolean serverCollideHuge, boolean moveEvent) {
		if (!serverCollideHuge) {
			if (this.setbackEnabled
				&& !this.data.isPossiblyTeleporting()
				&& !this.data.isOnClimbable()
				&& !this.data.couldBeUnloadedClient()
				&& this.data.elapsed(this.data.getLastFlyTick()) > 30) {
				CustomLocation location = Setbacks.forgeToRotatedLocation(this.data.getSafeGroundSetback(), this.data);
				double deltaY = this.data.deltas.motionY;
				double horizontal = this.data.getLocation().horizontal(location);
				double vertical = this.data.getLocation().vertical(location);
				this.setbacked = true;
				Tasker.run(() -> {
					this.data.teleport(location);
					if (deltaY < -0.1) {
						this.data.getBukkitPlayer().damage(1.0);
					}
				});
				this.data.setDidFlagMovement(true);
				this.data.setLastMovementFlag(this.data.getTotalTicks());
				this.data.setCancelHitsTick(this.data.getTotalTicks());
				Karhu.getInstance().printCool("&b> &fKarhu is cancelling hits for 20 ticks USER: " + this.data.getBukkitPlayer().getName());
				MiscellaneousAlertPoster.postSetback(this.data.getName() + " desync §aBELOW (CE) " + horizontal + " | " + vertical + " | " + location.toVector());
			}

			this.updateBlocksServerside();
		} else if (!moveEvent && ++this.desyncTicksUncertain > 15.0) {
			if (this.setbackEnabled) {
				CustomLocation location = Setbacks.forgeToRotatedLocation(this.data.getSafeGroundSetback(), this.data);
				if (!this.data.isPossiblyTeleporting() && !this.data.couldBeUnloadedClient() && this.data.elapsed(this.data.getLastFlyTick()) > 30) {
					this.setbacked = true;
					Tasker.run(() -> this.data.teleport(location));
				}

				this.data.setDidFlagMovement(true);
				this.data.setLastMovementFlag(this.data.getTotalTicks());
				MiscellaneousAlertPoster.postSetback(this.data.getName() + " desync §eBELOW (UN) " + this.data.getAirTicks());
			}

			this.desyncTicksUncertain = 0.0;
			this.updateBlocksServerside();
		}
	}

	private void handleHorizontal(boolean serverCollide) {
		boolean glitchWall = false;
		if (!serverCollide) {
			if (!this.data.isPossiblyTeleporting() && !this.data.couldBeUnloadedClient() && this.data.elapsed(this.data.getLastFlyTick()) > 30) {
				CustomLocation location = this.data.getLocation();
				double distanceX = MathUtil.distanceToHorizontalCollision(location.x);
				double distanceZ = MathUtil.distanceToHorizontalCollision(location.z);
				if (!(distanceX <= 1.0E-6) && !(distanceZ <= 1.0E-6)) {
					this.wallTicks = Math.max(this.wallTicks - 2.0, 0.0);
				} else {
					if (this.wallTicks > 0.0) {
						this.data.setCollidedHorizontally(true);
						this.data.setLastCollided(this.data.getTotalTicks());
						this.data.setLastCollidedH(this.data.getTotalTicks());
					}

					glitchWall = true;
					if (this.data.elapsed(this.data.getLastVelocityTaken()) <= 2) {
						if (++this.velocityAbuse >= 5.0) {
							this.data.setAbusingVelocity(true);
							if (this.velocityAbuse >= 20.0) {
								this.velocityAbuse = 3.0;
							}
						} else {
							this.data.setAbusingVelocity(false);
						}
					}

					this.data.setLastCollidedGhost(this.data.getTotalTicks());
					if (++this.wallTicks > 10.0) {
						this.wallTicks = 0.0;
					}
				}
			}
		} else {
			this.wallTicks = Math.max(this.wallTicks - 0.1, 0.0);
		}

		if (glitchWall && this.data.elapsed(this.data.getLastVelocityTaken()) >= 0 && this.data.elapsed(this.data.getLastVelocityTaken()) <= 5) {
			this.velocityAbuse = Math.max(this.velocityAbuse - 0.125, 0.0);
		}
	}

	private void handleAboveDesync() {
		if (!this.data.isPossiblyTeleporting()) {
			double motionY = this.data.deltas.motionY;
			double lMotionY = this.data.deltas.lastMotionY;
			double clamp = this.data.getClientVersion().getProtocolVersion() > 47 ? 0.003 : 0.005;
			double prediction = (lMotionY - 0.08) * 0.98F;
			if (prediction < clamp) {
				prediction = 0.0;
			}

			double jumpHeight = (double)PlayerUtil.getJumpHeight(this.data);
			if (this.data.isLastOnGroundPacket() && !this.data.isOnGroundPacket() && this.data.deltas.motionY >= jumpHeight - 0.03125) {
				prediction = Math.min(this.data.deltas.motionY, jumpHeight);
			}

			double diff = Math.abs(motionY - prediction);
			boolean slabHit = Math.abs(diff - 0.05) <= clamp;
			boolean higher = this.data.getLocation().getY() > this.data.getLastLocation().getY();
			boolean desyncedAbove = diff > 0.02
				&& (higher && motionY < 0.42F || lMotionY >= 0.42F && slabHit)
				&& (this.data.elapsed(this.data.getLastCollidedV()) > 2 || !this.data.isUnderBlockStrict())
				&& this.data.getClientAirTicks() <= 4
				&& this.data.elapsed(this.data.getLastVelocityTaken()) > 2
				&& this.data.elapsed(this.data.getLastInBerry()) > 2
				&& !this.data.isOnClimbable()
				&& !this.data.isOnSoulsand()
				&& !this.data.isOnSlime()
				&& !this.data.isWasOnClimbable()
				&& this.data.elapsed(this.data.getLastInPowder()) > 3
				&& this.data.elapsed(this.data.getLastInLiquidOffset()) > 3
				&& this.data.elapsed(this.data.getLastInLiquid()) > 3;
			if (desyncedAbove) {
				if (this.data.getTotalTicks() > 40 && this.data.elapsed(this.data.getLastFlyTick()) > 30) {
					this.data.setUnderGhostBlock(true);
					this.data.setLastCollidedVGhost(this.data.getTotalTicks());
					if (Karhu.getInstance().getConfigManager().isGbLagback() && ++this.aboveTicks > 3.0) {
						CustomLocation location = Setbacks.forgeToRotatedLocation(this.data.getSafeGroundSetback(), this.data);
						this.setbacked = true;
						Tasker.run(() -> {
							this.data.teleport(location);
							MiscellaneousAlertPoster.postSetback(this.data.getName() + " desync §4ABOVE diff " + diff);
						});
						this.data.setDidFlagMovement(true);
						this.data.setLastMovementFlag(this.data.getTotalTicks());
						this.data.setCancelHitsTick(this.data.getTotalTicks());
						Karhu.getInstance().printCool("&b> &fKarhu is cancelling hits for 5 ticks USER: " + this.data.getBukkitPlayer().getName());
					}

					this.updateBlocksServerside();
				}
			} else {
				this.data.setUnderGhostBlock(false);
				this.aboveTicks = Math.max(0.0, this.aboveTicks - 0.15);
			}
		} else {
			this.data.setUnderGhostBlock(false);
			this.aboveTicks = Math.max(0.0, this.aboveTicks - 0.2);
		}
	}

	private void handleLiquidDesync(boolean collided) {
		if (!collided && !this.data.isLastOnGroundPacket() && !this.data.isPossiblyTeleporting() && !this.data.isTakingVertical()) {
			double fixedLastMotion = this.data.deltas.lastMotionY > 0.0 ? (this.data.deltas.lastLastMotionY += 0.04F) : this.data.deltas.lastLastMotionY;
			double predictionLava = !this.data.isCollidedHorizontalClient() ? fixedLastMotion * 0.5 - 0.02 : 0.3F;
			double predictionWater = !this.data.isCollidedHorizontalClient() ? fixedLastMotion * 0.8F - 0.02 : 0.3F;
			double differenceLava = Math.abs(this.data.deltas.lastMotionY - predictionLava);
			double differenceWater = Math.abs(this.data.deltas.lastMotionY - predictionWater);
			if (differenceLava <= 1.0E-4 && this.data.elapsed(this.data.getLastInLiquid()) >= 3 && this.data.elapsed(this.data.getLastFlyTick()) > 30) {
				if (Karhu.getInstance().getConfigManager().isGbLagback() && ++this.lavaDesyncTicks >= 1.0 && this.noFakeWaterLocation != null) {
					Location location = Setbacks.forgeToRotatedLocation(this.noFakeWaterLocation.clone(), this.data);
					this.setbacked = true;
					Tasker.run(() -> {
						this.data.teleport(location);
						MiscellaneousAlertPoster.postSetback(this.data.getName() + " desync §cLAVA");
					});
					this.data.setDidFlagMovement(true);
					this.data.setLastMovementFlag(this.data.getTotalTicks());
				}

				this.data.setLastInLiquid(this.data.getTotalTicks());
				this.data.setLastInGhostLiquid(this.data.getTotalTicks());
				this.data.setOnLava(true);
				this.updateBlocksServerside();
			} else {
				this.lavaDesyncTicks = Math.max(this.lavaDesyncTicks - 0.01, 0.0);
			}

			if (differenceWater <= 1.0E-4
				&& this.data.elapsed(this.data.getLastInLiquid()) >= 3
				&& !this.data.isWasWasOnClimbable()
				&& !this.data.isOnClimbable()
				&& this.data.elapsed(this.data.getLastFlyTick()) > 30) {
				if (Karhu.getInstance().getConfigManager().isGbLagback() && ++this.waterDesyncTicks >= 3.0 && this.noFakeWaterLocation != null) {
					CustomLocation location = Setbacks.forgeToRotatedLocation(this.data.getSafeGroundSetback(), this.data);
					this.setbacked = true;
					Tasker.run(() -> {
						this.data.teleport(location);
						MiscellaneousAlertPoster.postSetback(this.data.getName() + " desync §bWATER");
					});
					this.data.setDidFlagMovement(true);
					this.data.setLastMovementFlag(this.data.getTotalTicks());
				}

				this.data.setLastInLiquid(this.data.getTotalTicks());
				this.data.setLastInGhostLiquid(this.data.getTotalTicks());
				this.data.setOnWater(true);
				this.updateBlocksServerside();
			} else {
				this.waterDesyncTicks = Math.max(this.waterDesyncTicks - 0.01, 0.0);
			}
		}

		if (!this.data.isPossiblyTeleporting()) {
			if (this.checkClientSideBlock(2.0, MaterialChecks.LIQUID_BUCKETS)) {
				this.data.setLastInLiquid(this.data.getTotalTicks());
				this.data.setOnWater(true);
				this.data.setOnLava(true);
			}

			if (this.checkClientSideBlock(2.0, MaterialChecks.WEB)) {
				this.data.setLastInWeb(this.data.getTotalTicks());
				this.data.setOnWeb(true);
				this.data.setInWeb(true);
			}
		}

		if (!this.data.isOnWater() && !this.data.isOnLava() && this.data.elapsed(this.data.getLastInGhostLiquid()) > 3) {
			this.noFakeWaterLocation = this.data.getLocation().toLocation(this.data.getWorld());
		}
	}

	private void updateBlocksServerside() {
		if (Karhu.getInstance().getConfigManager().isGbUpdate() && !this.collidesWithClientBlock(false) && this.data.isInitialized()) {
			BlockUtil.getTileEntitiesSync(
				this.data.getBoundingBox().clone().expand(1.0, 1.5, 1.0),
				blocks -> {
					for (Block block : blocks) {
						if (block != null) {
							Location blockLoc = block.getLocation();
							if (!this.checkClientSideBlock(blockLoc.toVector(), 2.0)) {
								Vector3i vector3i = new Vector3i(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ());
								PlayerUtil.sendPacket(
									this.data.getBukkitPlayer(), new WrapperPlayServerBlockChange(vector3i, SpigotConversionUtil.fromBukkitMaterialData(new MaterialData(block.getType())).getGlobalId())
								);
							}
						}
					}
				}
			);
		}
	}

	public boolean checkClientSideBlock(Vector vector, double radius) {
		for (BlockPlacePending block : this.getClientSideBlocks()) {
			Vector position = block.getBlockPosition();
			double distance = position.distance(vector);
			if (distance <= radius) {
				return true;
			}
		}

		return false;
	}

	public boolean checkClientSideBlock(double radius, Set<Material> checks) {
		for (BlockPlacePending block : this.getClientSideBlocks()) {
			if (checks.contains(block.getItem())) {
				Vector position = block.getBlockPosition();
				double distance = position.distance(this.data.getLocation().toVector());
				if (distance <= radius) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean collidesWithClientBlock(boolean lenient) {
		double expand = lenient ? 3.0 : 0.0;

		for (BlockPlacePending block : this.getClientSideBlocks()) {
			Vector position = block.getBlockPosition();
			AxisAlignedBB blockAABB = new AxisAlignedBB(position, position, true).addCoord(1.0, 1.0, 1.0);
			AxisAlignedBB playerAABB = this.data.getBoundingBox().toBB().expand(expand, expand, expand);
			if (playerAABB.intersectsWith(blockAABB)) {
				return true;
			}
		}

		return false;
	}

	public boolean collidesWithClientBlock(boolean lenient, Set<Material> checks) {
		double expand = lenient ? 2.0 : 0.0;

		for (BlockPlacePending block : this.getClientSideBlocks()) {
			if (checks.contains(block.getItem())) {
				Vector position = block.getBlockPosition();
				AxisAlignedBB blockAABB = new AxisAlignedBB(position, position, true).addCoord(1.0, 1.0, 1.0);
				AxisAlignedBB playerAABB = this.data.getBoundingBox().toBB().expand(expand, expand, expand);
				if (playerAABB.intersectsWith(blockAABB)) {
					return true;
				}
			}
		}

		return false;
	}

	public DesyncedBlockHandler(KarhuPlayer data) {
		this.data = data;
	}

	public Location getNoFakeWaterLocation() {
		return this.noFakeWaterLocation;
	}

	public void setNoFakeWaterLocation(Location noFakeWaterLocation) {
		this.noFakeWaterLocation = noFakeWaterLocation;
	}

	public Set<BlockPlacePending> getClientSideBlocks() {
		return this.clientSideBlocks;
	}
}
