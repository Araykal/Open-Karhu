/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.motion;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Motion (A)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.MOTION,
	experimental = false
)
public final class MotionA extends PositionCheck {
	public MotionA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		float jumpHeight = PlayerUtil.getJumpBooster(this.data);
		if (this.data.getJumpBoost() < 10
			&& !this.data.isOnSlime()
			&& this.data.isInitialized()
			&& this.data.deltas.motionY > 0.0
			&& this.data.elapsed(this.data.getLastRiptide()) > 15
			&& this.data.elapsed(this.data.getLastGlide()) > 100
			&& !this.data.isRiptiding()
			&& !this.data.isOnClimbable()
			&& !this.data.isGliding()
			&& !this.data.isOnBed()
			&& !this.data.isWasOnBed()
			&& !this.data.isInsideBlock()
			&& !this.data.isOnPiston()
			&& this.data.elapsed(this.data.getLastInBerry()) > 3
			&& this.data.elapsed(this.data.getLastPistonPush()) > 2
			&& this.data.elapsed(this.data.getPredictionTicks()) > 3
			&& this.data.elapsed(this.data.getLastFlyTick()) > 30
			&& this.data.getClientAirTicks() == 1
			&& this.data.elapsed(this.data.getLastInLiquid()) > 4
			&& this.data.elapsed(this.data.getPlaceTicks()) > 10
			&& !this.data.isInWeb()
			&& !this.data.isUnderGhostBlock()
			&& !this.data.isPossiblyTeleporting()) {
			float min = !this.data.isOnHoney() && !this.data.isWasOnHoney() ? jumpHeight : jumpHeight * 0.5F;
			min = this.data.isTakingVertical() ? (float)this.data.getVelocityY() - 0.25F : min;
			if (this.data.deltas.motionY < (double)min && Math.abs(this.data.getConfirmingY() - this.data.deltas.motionY) <= 0.005 && !this.data.isTakingVertical()) {
				min = (float)this.data.getConfirmingY();
			}

			min -= !this.data.isUnderBlock() && !this.data.isWasUnderBlock() ? 0.0F : 0.409F;
			min = (float)((double)min - (!this.data.isOnSoulsand() && !this.data.isWasOnSoulSand() ? 0.0 : 0.08));
			min = (float)((double)min - (this.data.elapsed(this.data.getPlaceTicks()) > 8 && this.data.elapsed(this.data.getPlaceTicks()) <= 30 ? 0.02 : 0.0));
			min = (float)((double)min - (this.data.elapsed(this.data.getLastOnSlime()) <= 15 ? 0.375 : 0.0));
			min = (float)((double)min - (this.data.isWasOnDoor() ? 0.03 : 0.0));
			min = (float)((double)min - (this.data.getBukkitPlayer().getMaximumNoDamageTicks() <= 10 ? 0.1 : 0.0));
			if (this.data.getMoveTicks() <= 1) {
				min = (float)((double)min - 0.015625);
			}

			double maxVL = this.data.elapsed(this.data.getLastVelocityTaken()) <= 12 ? 7.0 : 4.8;
			if (this.data.deltas.motionY < (double)min - 0.005) {
				if (++this.violations > maxVL) {
					this.fail(
						"* Jumping lower than expected\n §f* M: §b" + min + "\n §f* D: §b" + this.format(3, Double.valueOf(this.data.deltas.motionY)) + " tk " + this.data.isTakingVertical(),
						this.getBanVL(),
						150L
					);
				}
			} else {
				this.violations = Math.max(this.violations - 0.2, 0.0);
			}
		}
	}
}
