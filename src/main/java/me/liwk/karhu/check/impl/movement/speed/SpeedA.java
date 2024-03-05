/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.speed;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Speed (A)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.SPEED,
	experimental = false
)
public final class SpeedA extends PositionCheck {
	private double lastDeltaH;
	private boolean flagNoSlowLast;
	private double zeroPointThree;

	public SpeedA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		double deltaH = this.data.deltas.deltaXZ;
		double offsetY = this.data.deltas.motionY;
		double jumpHeight = (double)PlayerUtil.getJumpHeight(this.data);
		float movementSpeed = this.data.getWalkSpeed();
		if (movementSpeed < 0.0F) {
			movementSpeed = 0.0F;
		}

		boolean backTrack = false;
		boolean jump = false;
		List<String> tags = new ArrayList<>();
		if (this.data.elapsed(this.data.getLastSprintTick()) <= 3) {
			tags.add("sprinting");
		}

		float friction = 0.91F;
		float baseFriction = 0.54600006F;
		if (update.from.ground) {
			friction = this.data.getCurrentFriction();
			float frictionDiff = Math.abs(friction - 0.54600006F);
			float tMovementSpeed = movementSpeed;
			if ((double)frictionDiff >= 0.05 && this.data.elapsed(this.data.getFastDigTicks()) <= this.data.getPingInTicks()) {
				tMovementSpeed = movementSpeed * 1.3F;
				tMovementSpeed *= 0.75357103F;
				backTrack = true;
			}

			movementSpeed += movementSpeed * 0.3F;
			movementSpeed *= 0.16277136F / (friction * friction * friction);
			tags.add("ground");
			if (backTrack) {
				double tDiff = deltaH - this.lastDeltaH;
				double speedupServer = tDiff - (double)movementSpeed;
				double speedupClient = tDiff - (double)tMovementSpeed;
				if (speedupClient < speedupServer) {
					if (update.to.ground) {
						friction = 0.54600006F;
					}

					movementSpeed = (float)((double)tMovementSpeed + 0.05);
					tags.add("backtracked-slime-friction");
				}
			}

			double offsetJump = offsetY - jumpHeight;
			if (!update.to.ground
				&& (
					offsetJump >= -0.03125
						|| offsetY > 8.0 && this.data.getJumpBoost() >= 95
						|| this.data.elapsed(this.data.getLastInBerry()) <= 1
						|| this.data.isOnHoney()
						|| this.data.isWasOnHoney()
						|| this.data.getJumpBoost() < 0
						|| this.data.elapsed(this.data.getLastCollidedVGhost()) <= 3
						|| this.data.elapsed(this.data.getLastCollidedV()) < 3
				)) {
				movementSpeed = (float)((double)movementSpeed + 0.2);
				tags.add("jump");
				jump = true;
			}
		} else {
			tags.add("air");
			movementSpeed = 0.026F;
		}

		double movementSpeed2 = 0.02F;
		if (this.data.isWasOnWater()) {
			tags.add("water");
			double attributeSpeed = (double)this.data.getWalkSpeed();
			attributeSpeed += attributeSpeed * 0.3F;
			float f3 = (float)this.data.getDepthStriderLevel();
			float f9 = this.data.isSprinting() && this.data.isNewerThan12() ? 0.9F : 0.8F;
			if (f3 > 3.0F) {
				f3 = 3.0F;
			}

			if (!update.from.ground) {
				f3 *= 0.5F;
			}

			if (f3 > 0.0F) {
				f9 += (0.54600006F - f9) * f3 / 3.0F;
				movementSpeed2 += (attributeSpeed - movementSpeed2) * (double)f3 / 3.0;
			}

			if (this.data.getDolphinLevel() > 0) {
				f9 = 0.96F;
			}

			movementSpeed2 *= (double)f9;
		}

		if (!jump) {
			if (this.data.getTeleportManager().teleportTicks <= 1 && !update.isGround()) {
				movementSpeed = (float)((double)movementSpeed + 0.3125);
				jump = true;
			}

			if (!jump) {
				if ((this.data.elapsed(this.data.getLastOnHalfBlock()) <= 1 || this.data.elapsed(this.data.getLastOnBoat()) <= 3)
					&& (this.data.deltas.motionY >= 0.42F || this.data.deltas.lastMotionY >= 0.42F)
					&& this.data.elapsed(this.data.getLastCollided()) <= 1) {
					movementSpeed = (float)((double)movementSpeed + 0.2);
					tags.add("jump");
				} else if ((this.data.elapsed(this.data.getLastCollidedVGhost()) <= 10 || this.data.getTeleportManager().teleportTicks <= 2) && deltaH - this.lastDeltaH > 0.1) {
					movementSpeed = (float)((double)movementSpeed + 0.2);
					tags.add("jump");
				}
			}

			if (!jump && this.data.getTeleportManager().teleportTicks <= 2) {
				movementSpeed = (float)((double)movementSpeed + 0.3125);
			}
		}

		if (this.data.getTickedVelocity() != null) {
			movementSpeed = (float)((double)movementSpeed + this.data.getVelocityHorizontal());
			tags.add("velocity");
		}

		if (this.data.elapsed(this.data.getLastCollidedWithEntity()) <= 10) {
			tags.add("entity-collision");
			movementSpeed += 0.15F;
		}

		if (this.data.isWasInWeb()) {
			tags.add("web");
		}

		if (this.data.getMoveTicks() <= 2) {
			if (this.zeroPointThree <= 15.0) {
				movementSpeed = (float)((double)movementSpeed + 0.1525);
			}

			++this.zeroPointThree;
		} else {
			if (this.lastDeltaH <= 0.06) {
				movementSpeed = (float)((double)movementSpeed + 0.03);
			}

			this.zeroPointThree = Math.max(0.0, this.zeroPointThree - 0.2);
		}

		boolean flag1 = false;
		if (this.data.isUsingItem()) {
			tags.add("item-use");
			if (Karhu.getInstance().getConfigManager().isFlagNoSlow() && this.data.getVelocityHorizontal() == 0.0 && this.data.getBukkitPlayer().getWalkSpeed() < 0.21F) {
				if (this.data.isBlocking() && !this.data.isEating() && this.data.getClientVersion().getProtocolVersion() > 47) {
					flag1 = false;
				} else {
					movementSpeed = (float)((double)movementSpeed * (this.data.elapsed(this.data.getSlotSwitchTick()) <= 4 ? 0.98 : 0.4));
					flag1 = true;
				}
			}
		}

		if (this.data.elapsed(this.data.getExplosionExempt()) <= 2) {
			movementSpeed = (float)((double)movementSpeed + 0.6);
		}

		boolean setback = false;
		double velocity2 = this.data.getVelocityHorizontal() != 0.0 ? this.data.getVelocityHorizontal() + 0.2 : 0.0;
		if (this.data.elapsed(this.data.getLastInLiquid()) <= 3 || this.data.elapsed(this.data.getLastInGhostLiquid()) <= 3) {
			if (this.data.isConfirmingVelocity()) {
				movementSpeed += 3.0F;
			}

			movementSpeed = (float)((double)movementSpeed + Math.abs(movementSpeed2) + velocity2 + 0.03);
			setback = true;
		}

		if (this.data.elapsed(this.data.getLastSneakEdge()) <= 5 && this.data.getTickedVelocity() != null) {
			movementSpeed = (float)((double)movementSpeed + this.data.getVelocityHorizontal() + 0.2);
		}

		double diff = deltaH - this.lastDeltaH;
		double speedup = diff - (double)movementSpeed;
		double p = diff / (double)movementSpeed * 100.0;
		boolean item = tags.contains("item-use");
		if (diff > (double)movementSpeed * Math.max(Karhu.getInstance().getConfigManager().getSpeedAMult(), 1.03)) {
			if (this.data.elapsed(this.data.getLastFlyTick()) > 5
				&& !this.data.isPossiblyTeleporting()
				&& !this.data.isSpectating()
				&& this.data.elapsed(this.data.getLastPistonPush()) > 3
				&& !this.data.isOnPiston()
				&& !this.data.isRiding()
				&& deltaH > 0.15
				&& !this.data.isInBed()
				&& !this.data.isLastInBed()
				&& this.data.elapsed(this.data.getLastRiptide()) > 15
				&& this.data.elapsed(this.data.getLastGlide()) > 150
				&& !this.data.isRiptiding()
				&& !this.data.isGliding()) {
				double maxVL = this.data.isUsingItem() && this.data.isEating() ? 50.0 : (this.data.isUsingItem() && this.data.isBowing() ? 60.0 : (this.data.isUsingItem() ? 10.0 : 3.5));
				if (!Karhu.getInstance().getConfigManager().isFlagNoSlow()) {
					maxVL = 3.5;
				}

				boolean shouldFix = !this.data.isEating() && !this.data.isUsingItem() ? this.violations > 10.0 : this.violations > 15.0;
				if (Karhu.getInstance().getConfigManager().isFixEat() && item && shouldFix && !this.data.isPendingBackSwitch()) {
					int currentSlot = this.data.getCurrentSlot();
					int switchSlot = currentSlot == 8 ? 1 : currentSlot + 1;
					PlayerUtil.sendPacket(this.data.getBukkitPlayer(), new WrapperPlayServerHeldItemChange(switchSlot));
					this.data.setPendingBackSwitch(true);
					int uid = this.data.getCurrentServerTransaction();
					Deque<Integer> slots = this.data.getBackSwitchSlots().getOrDefault(uid, new LinkedList<>());
					slots.add(currentSlot);
					this.data.getBackSwitchSlots().put(uid, slots);
					if (flag1) {
						this.flagNoSlowLast = true;
					}
				}

				if (!flag1) {
					this.flagNoSlowLast = false;
				}

				if (speedup > (this.data.isConfirmingVelocity() ? 0.8 : 0.4) && maxVL <= 3.0 && this.violations >= 1.2) {
					this.disallowMove(false);
				}

				if (setback) {
					if (this.data.elapsed(this.data.getLastSneakEdge()) > 5) {
						this.disallowMove(false);
					}
				} else if (!flag1 && (this.violations += 1.0 + speedup * 0.7) > maxVL) {
					this.fail(
						"* Moving faster than possible\n §f* p §b"
							+ this.format(1, Double.valueOf(p))
							+ "\n §f* mSpeed §b"
							+ this.format(4, Float.valueOf(movementSpeed))
							+ "\n §f* diff §b"
							+ this.format(4, Double.valueOf(diff))
							+ "\n §f* jump §b"
							+ this.format(4, Double.valueOf(offsetY))
							+ " | "
							+ this.format(4, Double.valueOf(jumpHeight))
							+ "\n §f* f5 §b"
							+ friction
							+ " | "
							+ this.data.getClientAirTicks()
							+ "\n §f* tags §b"
							+ String.join("§f, §b", tags)
							+ "\n §f* kb §b"
							+ this.data.elapsed(this.data.getLastVelocityTaken())
							+ "\n §f* dXZ | lDXZ §b"
							+ this.format(4, Double.valueOf(deltaH))
							+ " | "
							+ this.format(4, Double.valueOf(this.lastDeltaH))
							+ "\n §f* tp §b"
							+ this.data.getTeleportManager().zeroAmount
							+ " | "
							+ this.data.getTeleportManager().teleportTicks,
						300L
					);
				}
			} else {
				this.violations = Math.max(this.violations - 0.0045, 0.0);
			}
		} else {
			this.violations = Math.max(this.violations - (item && this.flagNoSlowLast ? 0.475 : 0.015), 0.0);
		}

		this.lastDeltaH = deltaH * (double)friction;
	}
}
