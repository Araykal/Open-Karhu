/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.reach;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import java.util.ArrayList;
import java.util.List;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.impl.combat.hitbox.HitboxA;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.mc.MovingObjectPosition;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.mc.vec.Vec3;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Reach (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.REACH,
	experimental = false
)
public final class ReachA extends PacketCheck {
	public ReachA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_19)) {
			if (packet instanceof FlyingEvent) {
				this.handleReach(((FlyingEvent)packet).hasLooked());
			} else if (packet instanceof AttackEvent) {
				ClientVersion version = this.data.getClientVersion();
				if (!this.data.isPossiblyTeleporting()) {
					for (int target : this.data.getLastTargets()) {
						EntityData edata = this.data.getEntityData().get(target);
						if (edata != null) {
							if (this.data.isNewerThan8()) {
								double dist = this.data.getBoundingBox().distanceToHitbox(edata.getEntityBoundingBox());
								if (Math.abs(this.data.getLocation().pitch) != 90.0F) {
									dist /= Math.cos(Math.toRadians((double)this.data.getLocation().pitch));
								}

								if (dist > 4.0) {
									Karhu.getInstance().printCool("&b> &fKarhu USER: " + this.data.getBukkitPlayer().getName() + " is reaching far " + dist);
									this.data.setEntityIdCancel(target);
								}

								this.data.setReduceNextDamage(dist > 4.0);
							}
						} else {
							this.data.setReduceNextDamage(true);
							this.data.setEntityIdCancel(target);
						}
					}
				}

				if (!this.data.isForceCancelReach()) {
					int max = version.getProtocolVersion() <= 47 ? 2 : 21;
					this.data.setCancelTripleHit(this.data.getAttacks() > max);
				}
			}
		}
	}

	private void handleReach(boolean look) {
		for (int target : this.data.getLastTargets()) {
			if (this.data.getGameMode() != GameMode.CREATIVE && !this.data.isRiding() && !this.data.isSpectating() && !this.data.isPossiblyTeleporting()) {
				if (this.data.getLastAttackTick() > 1) {
					return;
				}

				EntityData edata = this.data.getEntityData().get(target);
				if (edata == null) {
					this.data.setReachBypass(true);
					return;
				}

				if (edata.isRiding()) {
					this.data.setReachBypass(true);
					return;
				}

				float pitch = this.data.getLocation().pitch;
				float yaw = this.data.getLocation().yaw;
				List<Vec3> rotationVectors = new ArrayList<>();
				if (look) {
					rotationVectors.add(MathUtil.getVectorForRotation(pitch, yaw, this.data));
					if (this.data.getClientVersion().getProtocolVersion() >= 47) {
						rotationVectors.add(MathUtil.getVectorForRotation(pitch, this.data.attackerYaw, this.data));
					}
				} else {
					rotationVectors.add(MathUtil.getVectorForRotation(this.data.attackerPitch, this.data.attackerYaw, this.data));
				}

				this.data.setReachBypass(false);
				AxisAlignedBB box = edata.getEntityBoundingBox();
				AxisAlignedBB axisalignedbb = !edata.uncertainBox
					? MathUtil.getHitbox(this.data, box)
					: MathUtil.getHitbox(this.data, box).union(MathUtil.getHitbox(this.data, edata.getEntityBoundingBoxLast())).expand(0.1, 0.1, 0.1);
				double distance = Double.MAX_VALUE;

				for (Vec3 rLook : rotationVectors) {
					for (double height : this.data.getEyePositions()) {
						Vec3 eyeLocation = new Vec3(this.data.attackerX, this.data.attackerY + height, this.data.attackerZ);
						Vec3 search = eyeLocation.addVector(rLook.xCoord * 6.0, rLook.yCoord * 6.0, rLook.zCoord * 6.0);
						MovingObjectPosition intercept = axisalignedbb.calculateIntercept(eyeLocation, search);
						if (axisalignedbb.isVecInside(eyeLocation)) {
							distance = 0.0;
							break;
						}

						if (intercept != null) {
							distance = Math.min(eyeLocation.distanceTo(intercept.hitVec), distance);
							if (Karhu.getInstance().getAlertsManager().hasDebugToggled(this.data.getBukkitPlayer()) && distance > 3.0 && distance != Double.MAX_VALUE) {
								this.data.getBukkitPlayer().sendMessage(this.format(4, Double.valueOf(distance)));
							}
						}
					}
				}

				double x = box.getCenterX();
				double z = box.getCenterZ();
				double direction = MathUtil.getDirection(this.data.getLocation(), new Vector(x, 0.0, z));
				double angle = MathUtil.getAngleDistance((double)this.data.getLocation().getYaw(), direction);
				double dist = this.data.getLastBoundingBox().distanceToHitbox(edata.getEntityBoundingBox());
				if (Math.abs(this.data.getLocation().pitch) != 90.0F) {
					dist /= Math.cos(Math.toRadians((double)this.data.getLocation().pitch));
				}

				double buffer = Math.max(this.cfg.getReachBuffer(), 1.2);
				double removal = Math.max(this.cfg.getReachDecayPerMiss(), 0.001);
				double minReach = Math.max(this.cfg.getReachToFlag(), 3.0);
				boolean checkReach = true;
				if (distance == Double.MAX_VALUE) {
					if (Karhu.getInstance().getConfigManager().isCheckHitbox()) {
						if (++this.subVl > (double)((angle > 20.0 ? 2 : 4) + (dist > 8.0 ? 1 : 2))) {
							this.data
								.getCheckManager()
								.getCheck(HitboxA.class)
								.fail(
									"* Hit out of the box\n * dist §b"
										+ dist
										+ "\n * angle §b"
										+ angle
										+ "\n * mins §b"
										+ edata.minX
										+ " / "
										+ edata.minY
										+ " / "
										+ edata.minZ
										+ "\n * maxs §b"
										+ edata.maxX
										+ " / "
										+ edata.maxY
										+ " / "
										+ edata.maxZ
										+ "\n * locations §b"
										+ edata.newLocations.size()
										+ "\n * existed §b"
										+ edata.getExist()
										+ "\n §f* DEV DATA: §b"
										+ edata.posIncrements,
									300L
								);
							this.data.setCancelNextHitH(true);
						} else {
							this.data.getCheckManager().getCheck(HitboxA.class).debug(String.format("A: %.3f D: %.3f B: %.3f", angle, dist, this.subVl));
						}
					}

					checkReach = false;
				} else {
					this.subVl = Math.max(this.subVl - 0.175, 0.0);
					this.data.setCancelNextHitH(false);
				}

				if (distance > this.data.getHighestReach()) {
					this.data.setHighestReach(distance);
				}

				if (distance >= minReach && checkReach) {
					this.violations += distance - minReach + 0.4;
					if (this.violations >= buffer) {
						this.fail(
							"§f* Longer arms\n §f* Range: §b"
								+ distance
								+ "\n §f* DEV DATA: §b"
								+ edata.posIncrements
								+ "\n §f* existed: §b"
								+ edata.getExist()
								+ "\n §f* locations: §b"
								+ edata.newLocations.size()
								+ " | "
								+ edata.isUncertainBox()
								+ "\n §f* DEV DATA: §b"
								+ this.data.getTeleportManager().zeroAmount
								+ "/"
								+ this.data.getTeleportManager().teleportAmount,
							this.getBanVL(),
							300L
						);
						this.data.setCancelNextHitR(true);
					}
				} else if (checkReach) {
					this.decrease(removal);
					this.data.setCancelNextHitR(false);
				}
			}
		}
	}
}
