/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.motion;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Motion (B)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.MOTION,
	experimental = false
)
public final class MotionB extends PositionCheck {
	private double slimeHeight;

	public MotionB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		double deltaY = this.data.deltas.motionY;
		int jumpLevel = this.data.getJumpBoost();
		if (jumpLevel < 10
			&& !this.data.isPossiblyTeleporting()
			&& !this.data.isOnBed()
			&& !this.data.isWasOnBed()
			&& !this.data.isInBed()
			&& !this.data.isLastInBed()
			&& !this.data.isSpectating()
			&& !this.data.isTakingVertical()
			&& this.data.getGameMode() != GameMode.CREATIVE
			&& this.data.elapsed(this.data.getLastFlyTick()) > 30
			&& this.data.elapsed(this.data.getLastInBerry()) > 3
			&& this.data.elapsed(this.data.getLastInPowder()) > 6
			&& !this.data.isInUnloadedChunk()
			&& this.data.levitationLevel == 0
			&& this.data.deltas.motionY > 0.0
			&& this.data.elapsed(this.data.getLastInLiquid()) > 1) {
			double jumpMax = (double)PlayerUtil.getJumpHeight(this.data, this.data.elapsed(this.data.getLastOnHalfBlock()) <= 1 ? 0.565F : 0.42F);
			double maximum = (double)PlayerUtil.getJumpHeight(this.data, 0.5F);
			double stepHeight = (double)PlayerUtil.getJumpHeight(this.data, 0.6F);
			if (this.data.elapsed(this.data.getLastCollided()) <= 1 && this.data.elapsed(this.data.getLastOnHalfBlock()) <= 1) {
				return;
			}

			if (this.data.elapsed(this.data.getLastCollided()) <= 1) {
				jumpMax = (double)PlayerUtil.getJumpHeight(this.data, 0.565F);
			}

			if (this.data.elapsed(this.data.getLastOnBoat()) <= 3) {
				maximum = (double)PlayerUtil.getJumpHeight(this.data, 0.601F);
			}

			if (this.data.getMoveTicks() <= 1) {
				jumpMax += 0.03125;
				stepHeight += 0.03125;
				jumpMax += 0.03125;
			}

			if (this.data.elapsed(this.data.getLastOnSlime()) <= 50 || this.data.elapsed(this.data.getLastSlimePistonPush()) < 30) {
				maximum += 3.5;
				stepHeight += 3.5;
				jumpMax += 3.5;
			}

			if (this.data.isNewerThan8() && (this.data.elapsed(this.data.getLastGlide()) <= 100 || this.data.elapsed(this.data.getLastRiptide()) <= 100)) {
				jumpMax += 8.0;
				maximum += 8.0;
				stepHeight += 8.0;
			}

			if (this.data.isLastOnGroundPacket() && !this.data.isOnGroundPacket()) {
				if (this.data.deltas.motionY > jumpMax + 0.001) {
					double addition = Math.abs(deltaY - jumpMax + 0.3);
					if (deltaY > maximum) {
						this.fail("* Jumping higher than expected \n §f* mY: §b" + this.format(3, Double.valueOf(deltaY)) + " \n §f* mJ: §b" + maximum, this.getBanVL(), 300L);
					} else if ((this.violations += addition) > 2.0) {
						this.fail("* Jumping higher than expected \n §f* mY: §b" + this.format(3, Double.valueOf(deltaY)) + " \n §f* mJM: §b" + jumpMax, this.getBanVL(), 300L);
					}
				} else {
					this.violations = Math.max(this.violations - 0.25, 0.0);
				}
			} else if (update.isGround()) {
				if (deltaY > stepHeight + 0.001 && this.data.isLastOnGroundPacket()) {
					this.fail("* Stepped higher than expected on ground \n §f* mY: §b" + this.format(3, Double.valueOf(deltaY)) + " \n §f* sH: §b" + stepHeight, this.getBanVL(), 300L);
				} else if (!(deltaY > jumpMax + 0.001) || !(this.data.deltas.lastMotionY > jumpMax + 0.001) || this.data.elapsed(this.data.getLastOnHalfBlock()) <= 3) {
					this.violations = Math.max(this.violations - 0.3, 0.0);
				} else if (++this.violations > 1.0) {
					this.fail("* Stepped higher than expected on ground \n §f* mY: §b" + this.format(3, Double.valueOf(deltaY)) + " \n §f* jumpMaxFG: §b" + jumpMax, this.getBanVL(), 300L);
				}
			}
		}
	}
}
