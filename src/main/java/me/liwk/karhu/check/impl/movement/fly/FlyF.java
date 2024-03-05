/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.fly;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Fly (F)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.FLY,
	experimental = false
)
public final class FlyF extends PositionCheck {
	private Double lastMotionY;

	public FlyF(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate e) {
		double min = this.data.getClientVersion().getProtocolVersion() > 47 ? 0.003 : 0.005;
		double motionY = this.data.deltas.motionY;
		if (this.lastMotionY != null && !this.data.isPossiblyTeleporting() && this.data.elapsed(this.data.getLastFlyTick()) > 10 && !this.data.isTakingVertical()) {
			double prediction = Math.abs((this.data.deltas.lastMotionY - 0.08) * 0.98F) < min ? -0.0784000015258789 : (this.data.deltas.lastMotionY - 0.08) * 0.98F;
			double chunkMove = this.data.getLocation().y > 0.0 ? 0.09800000190735147 : 0.0;
			double chunk = Math.abs(motionY + chunkMove);
			if (chunk <= 1.0E-7) {
				this.lastMotionY = null;
				return;
			}

			if (this.data.isOnGhostBlock()) {
				this.lastMotionY = null;
				return;
			}

			if (e.to.ground && motionY < 0.0 && prediction < motionY && MathUtil.onGround(Math.abs(e.to.getY())) || e.from.horizontal(e.to) < min && this.data.getJumpBoost() > 0) {
				this.setMotion(e.to, e.from);
				return;
			}

			if (Math.abs(this.data.deltas.motionY + 0.078) < 0.01 && Math.abs(this.data.deltas.motionY - prediction) > 0.078) {
				prediction = !this.data.isNewerThan8() ? -0.0784000015258789 : 0.0;
			}

			double difference = Math.abs(prediction - motionY);
			if (!(difference > 0.0325) || !(motionY < 0.0) || !(Math.abs(prediction) > min + 0.001)) {
				this.decrease(0.0325);
			} else if (!this.incompatibility()) {
				if (++this.violations > 3.0) {
					this.fail(
						"* Downwards gravity modification\n"
							+ this.format(3, Double.valueOf(prediction - this.data.deltas.motionY))
							+ " ("
							+ this.format(4, Double.valueOf(this.data.deltas.motionY))
							+ "/"
							+ this.format(10, Double.valueOf(prediction))
							+ ")",
						this.getBanVL(),
						300L
					);
				}
			} else {
				this.decrease(0.0755);
			}
		}

		this.setMotion(e.to, e.from);
	}

	public void setMotion(CustomLocation to, CustomLocation from) {
		if (to.ground && from.ground) {
			this.lastMotionY = null;
		} else {
			this.lastMotionY = to.getY() - from.getY();
		}
	}

	public boolean incompatibility() {
		double motionY = this.data.deltas.motionY;
		return this.data.elapsed(this.data.getLastCollidedV()) <= 2
			|| this.data.elapsed(this.data.getLastOnSlime()) <= 40
			|| this.data.elapsed(this.data.getLastGlide()) <= 30
			|| this.data.elapsed(this.data.getLastRiptide()) <= 30
			|| this.data.isOnCarpet()
			|| this.data.elapsed(this.data.getLastPistonPush()) < 3
			|| this.data.isOnFence()
			|| this.data.isWasOnFence()
			|| this.data.isWasOnGroundServer()
			|| this.data.isOnGhostBlock()
			|| this.data.elapsed(this.data.getLastInPowder()) <= 6
			|| this.data.elapsed(this.data.getLastInBerry()) <= 3
			|| this.data.getLevitationLevel() != 0
			|| this.data.getSlowFallingLevel() != 0
			|| this.data.elapsed(this.data.getLastOnClimbable()) <= 1
			|| this.data.elapsed(this.data.getLastInLiquid()) <= 3
			|| this.data.elapsed(this.data.getLastOnHalfBlock()) <= 6 && (motionY >= 0.5 || motionY < 0.0 && motionY > -0.2)
			|| this.data.isInWeb()
			|| this.data.isWasInWeb();
	}
}
