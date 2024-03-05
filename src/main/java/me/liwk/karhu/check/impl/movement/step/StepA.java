/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.step;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.player.PlayerUtil;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Step (A)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.MOTION,
	experimental = false
)
public final class StepA extends PositionCheck {
	public StepA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		float max = 0.6F;
		double offsetY = this.data.deltas.motionY;
		double lastOffsetY = this.data.deltas.lastMotionY;
		if (this.data.getVelocityY() > 0.0) {
			max = (float)Math.max((double)max, this.data.getVelocityY() + 0.4F);
		}

		max += PlayerUtil.getJumpBooster(this.data);
		boolean newSwimming = this.data.isNewerThan12();
		if (this.data.elapsed(this.data.getLastInLiquid()) <= (newSwimming ? 10 : 3)) {
			max = (float)((double)max + (newSwimming ? 1.2 : 0.6));
		}

		if (this.data.elapsed(this.data.getLastOnSlime()) <= 50) {
			max = (float)((double)max + 4.0);
		}

		if (lastOffsetY < -0.4 && this.data.elapsed(this.data.getPlaceTicks()) < 15) {
			max = (float)((double)max + 4.0);
		}

		boolean valid = !this.data.isOnPiston()
			&& !this.data.isWasOnSlime()
			&& !this.data.isPossiblyTeleporting()
			&& !this.data.isSpectating()
			&& this.data.elapsed(this.data.getLastPistonPush()) > 2
			&& this.data.levitationLevel == 0
			&& this.data.elapsed(this.data.getLastRiptide()) > 30
			&& this.data.elapsed(this.data.getLastGlide()) > 30
			&& this.data.elapsed(this.data.getLastFlyTick()) > 40;
		if (this.data.elapsed(this.data.getLastGlide()) <= 100 || this.data.elapsed(this.data.getLastRiptide()) <= 100) {
			max = (float)((double)max + 6.0);
		}

		if (offsetY > (double)max && valid) {
			if ((this.violations += offsetY - (double)max + 0.5) > 1.0) {
				this.fail(
					"* Jumping higher than expected\n §f* D: §b" + this.format(3, Double.valueOf(offsetY)) + "\n §f* M: §b" + max + "\n §f* TP: §b" + this.data.getTeleportManager().teleportTicks,
					this.getBanVL(),
					300L
				);
			}
		} else {
			this.violations *= 0.1;
		}

		if (!update.from.ground && update.fromFrom.ground && valid && this.checkBlocks()) {
			double difference = offsetY - lastOffsetY;
			double combined = offsetY + lastOffsetY;
			if (difference > 0.0 && offsetY > 0.0 && lastOffsetY > 0.0 && combined > (double)max && !this.data.isTakingVertical()) {
				this.fail(
					"* Jumping higher than expected (V2)\n §f* D: §b"
						+ this.format(3, Double.valueOf(offsetY))
						+ "\n §f* M: §b"
						+ max
						+ "\n §f* DIFF: §b"
						+ this.format(3, Double.valueOf(difference))
						+ "\n §f* TP: §b"
						+ this.data.getTeleportManager().teleportTicks,
					this.getBanVL(),
					300L
				);
			}
		}

		if (this.checkBlocks() && valid && offsetY > 0.0 && lastOffsetY > 0.0 && update.isGround() && !this.data.isTakingVertical()) {
			this.fail(
				"* Impossible upwards movement (GROUND)\n §f* D/LD: §b" + this.format(3, Double.valueOf(offsetY)) + "/" + this.format(3, Double.valueOf(lastOffsetY)), this.getBanVL(), 300L
			);
		}
	}

	public boolean checkBlocks() {
		return this.data.elapsed(this.data.getLastOnSlime()) > 1
			&& this.data.elapsed(this.data.getLastOnHalfBlock()) > 3
			&& this.data.elapsed(this.data.getLastFence()) > 2
			&& this.data.elapsed(this.data.getLastInPowder()) > 1
			&& this.data.elapsed(this.data.getLastOnBoat()) > 2;
	}
}
