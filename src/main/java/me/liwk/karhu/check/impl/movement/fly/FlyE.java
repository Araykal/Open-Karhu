/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.fly;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.player.PlayerUtil;

@CheckInfo(
	name = "Fly (E)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.FLY,
	experimental = true
)
public final class FlyE extends PacketCheck {
	private int zeroPointThree;

	public FlyE(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event p) {
		if (p instanceof FlyingEvent && ((FlyingEvent)p).hasMoved()) {
			if (this.data.elapsed(this.data.getLastRiptide()) >= 5
				&& !this.data.isPossiblyTeleporting()
				&& !this.data.isInUnloadedChunk()
				&& !this.data.isSpectating()
				&& !this.data.isOnPiston()
				&& !this.data.isOnFence()
				&& !this.data.isOnLava()
				&& !this.data.isWasOnFence()
				&& this.data.elapsed(this.data.getLastPistonPush()) > 2
				&& this.data.getLevitationLevel() == 0
				&& this.data.getSlowFallingLevel() == 0
				&& this.data.elapsed(this.data.getLastOnBed()) > 5
				&& this.data.elapsed(this.data.getLastInBerry()) > 1
				&& this.data.elapsed(this.data.getLastGlide()) > 140
				&& !this.data.isOnScaffolding()
				&& this.data.getGameMode() != GameMode.CREATIVE
				&& !this.data.isInWeb()
				&& !this.data.isWasInWeb()
				&& !this.data.isWasWasInWeb()) {
				boolean executePreds = true;
				if (((this.data.elapsed(this.data.getLastCollidedV()) <= 3 || this.data.isUnderGhostBlock()) && this.data.getClientAirTicks() <= 10 || this.data.isInsideBlock())
					&& !this.data.isOnLadder()) {
					executePreds = false;
				}

				double clamp = this.data.isNewerThan8() ? 0.003 : 0.005;
				double motionY = this.data.deltas.motionY;
				double lastMotionY = Math.abs(this.data.deltas.lastMotionY) < clamp ? 0.0 : this.data.deltas.lastMotionY;
				double lastLastMotionY = this.data.deltas.lastLastMotionY;
				double jumpHeight = (double)PlayerUtil.getJumpHeight(this.data);
				double maxiumViolations = !this.data.isOnLadder() && !this.data.isLastLadder() ? 4.5 : 8.0;
				boolean ignoreServerGround = false;
				boolean trappa = false;
				double pred = this.data.getVelocityYTicks() == 0 ? this.data.getVelocityY() : (lastMotionY - 0.08) * 0.98F;
				double threshold = this.data.elapsed(this.data.getLastOnSlime()) <= 30 ? 0.2 : 0.005;
				if (executePreds) {
					if (this.data.isOnLadder() || this.data.isLastLadder()) {
						boolean goForward = true;
						if (this.data.isJumped()) {
							pred = (double)PlayerUtil.getJumpHeight(this.data);
							goForward = false;
						} else if (this.data.getVelocityYTicks() == 0) {
							pred = this.data.getVelocityY();
							goForward = false;
						}

						if (goForward) {
							if (this.data.isLastLadder() && motionY < 0.03125) {
								pred = Math.max(pred, -0.14700000286102294);
								if (this.data.isSneaking() && motionY <= 0.03) {
									pred = 0.0;
								}
							}

							if ((this.data.isOnLadder() || this.data.isLastLadder()) && motionY > 0.07 && this.data.getClientAirTicks() > 3) {
								pred = 0.11760000228881837;
								trappa = true;
							}
						}

						ignoreServerGround = true;
						if (this.data.getClientAirTicks() == 3) {
							++threshold;
						}

						threshold += 0.085;
					}

					if (this.data.getVelocityYTicks() <= 2) {
						threshold += 0.2;
					}

					if (this.data.isWasOnWater()) {
						double fixedLastMotion = lastMotionY;
						if (motionY > 0.0) {
							fixedLastMotion = lastMotionY + 0.04F;
						}

						pred = this.data.getVelocityYTicks() == 0 ? this.data.getVelocityY() : fixedLastMotion * 0.8F - 0.02;
						threshold += 0.6;
					} else if (this.data.isOnWater()) {
						threshold += 0.6;
						threshold += this.data.elapsed(this.data.getLastCollided()) <= 1 ? 0.1 : 0.0;
					}

					if (this.data.isWasOnLava()) {
						double fixedLastMotion = lastMotionY;
						if (motionY > 0.0) {
							fixedLastMotion = lastMotionY + 0.04F;
						}

						pred = this.data.getVelocityYTicks() == 0 ? this.data.getVelocityY() : fixedLastMotion * 0.5 - 0.02;
						threshold += 0.6;
					} else if (this.data.isOnLava()) {
						threshold += 0.6;
						threshold += this.data.elapsed(this.data.getLastCollided()) <= 1 ? 0.1 : 0.0;
					}

					if (this.data.elapsed(this.data.getLastInLiquid()) <= 3) {
						threshold += 0.12;
					}

					if (this.data.elapsed(this.data.getPredictionTicks()) > 1 && this.data.getMoveTicks() > 1) {
						--this.zeroPointThree;
					} else {
						if (this.zeroPointThree <= 10) {
							threshold += 0.0425;
						}

						++this.zeroPointThree;
					}

					if (!this.data.isTakingVertical() && this.data.elapsed(this.data.getLastVelocityTaken()) <= 10 && this.data.isCollidedHorizontally()) {
						threshold += 0.3;
					}

					if (Math.abs(motionY + 0.098) <= 1.0E-5) {
						return;
					}

					if (this.data.getLocation().y <= 0.0 && motionY == 0.0) {
						return;
					}

					if (this.data.elapsed(this.data.getPlaceTicks()) < Math.min(15, MathUtil.getPingInTicks(this.data.getTransactionPing() + 50L) + 20)) {
						threshold += 0.05;
					}

					if (this.data.getJumpBoost() != 0 && this.data.getMoveTicks() <= 2) {
						threshold += 0.1;
					}

					if (this.data.isTakingVertical()) {
						threshold += 0.1;
					}

					double clampedPred = Math.abs(pred) < clamp ? -0.0784000015258789 : pred;
					double ratio = Math.abs(motionY - clampedPred);
					double ratioAndIdcEtc = trappa ? 0.0 : Math.min(3.0, Math.ceil(ratio * 2.5));
					if (ratio >= threshold
						&& Math.abs(pred) > 0.03 + clamp
						&& this.data.elapsed(this.data.getLastOnSlime()) > 1
						&& !this.data.isOnBoat()
						&& this.data.getClientAirTicks() > 2
						&& (this.data.getAirTicks() > 2 || ignoreServerGround)) {
						if ((this.violations += 1.0 + ratioAndIdcEtc) > maxiumViolations) {
							if (this.data.elapsed(this.data.getLastFlyTick()) <= 6) {
								if (this.data.isConfirmingFlying() && !this.data.getBukkitPlayer().getAllowFlight() && this.data.elapsed(this.data.getLastConfirmingState()) > 3) {
								}
							} else {
								this.fail(
									"* Generic gravity modification \n §f* PRED §b"
										+ pred
										+ " \n §f* MOTION §b"
										+ motionY
										+ "/"
										+ this.format(3, Double.valueOf(this.data.deltas.deltaXZ))
										+ " \n §f* RAT §b"
										+ ratio
										+ " \n §f* TR §b"
										+ threshold
										+ " \n §f* IGS §b"
										+ ignoreServerGround
										+ " \n §f* F: §b"
										+ this.data.elapsed(this.data.getLastFlyTick())
										+ " \n §f* ST/CT: §b"
										+ this.data.getAirTicks()
										+ " | "
										+ this.data.getClientAirTicks(),
									this.getBanVL(),
									325L
								);
							}
						}
					} else {
						this.violations = Math.max(this.violations - 0.04, 0.0);
					}
				}

				if (this.data.isWasSlimeLand() && this.data.elapsed(this.data.getLastPistonPush()) >= 5 && !this.data.isOnPiston()) {
					double maxSlime = Math.max(Math.abs(lastMotionY), Math.abs(lastLastMotionY) + 0.2);
					if (motionY >= maxSlime + Math.abs(this.data.getVelocityY()) && motionY > jumpHeight + 0.2F) {
						this.fail(
							"* Generic gravity modification (slime) \n §f* PRED §b"
								+ pred
								+ " \n §f* MOTION §b"
								+ motionY
								+ "/"
								+ maxSlime
								+ " \n §f* TR §b"
								+ threshold
								+ " \n §f* ST/CT: §b"
								+ this.data.getAirTicks()
								+ " | "
								+ this.data.getClientAirTicks(),
							this.getBanVL(),
							325L
						);
					}
				}
			} else {
				this.violations = Math.max(this.violations - 0.0075, 0.0);
			}
		}
	}
}
