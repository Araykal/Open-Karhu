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
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.update.MovementUpdate;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Motion (J)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.MOTION,
	experimental = true
)
public final class MotionJ extends PositionCheck {
	private int desyncPosTicks;
	private int desyncsInRow;

	public MotionJ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate e) {
		boolean canFlag = true;
		CustomLocation to = e.getTo();
		CustomLocation from = e.getFrom();
		Vector tickVel = this.data.getTickedVelocity();
		double min = this.data.getClientVersion().getProtocolVersion() > 47 ? 0.003 : 0.005;
		double threshold = min + 0.031;
		double motionY = this.data.deltas.motionY;
		double lastMotionY = this.data.deltas.lastMotionY;
		if (!(Math.abs(motionY + 0.098) <= 1.0E-5)) {
			double prediction = Math.abs((lastMotionY - 0.08) * 0.98F) <= threshold ? 0.0 : (lastMotionY - 0.08) * 0.98F;
			if (tickVel != null) {
				prediction = tickVel.getY();
			}

			if (this.data.getTeleportManager().teleportTicks == 1) {
				prediction = 0.0;
			}

			if ((!(Math.abs(prediction * prediction) <= 0.03125) || !(this.data.deltas.deltaXZ < 0.15)) && this.desyncPosTicks <= 0 && this.data.getMoveTicks() > 1) {
				this.desyncsInRow = 0;
			} else {
				threshold = 0.05;
				if (this.desyncPosTicks <= 0) {
					this.desyncPosTicks = 2;
				}

				++this.desyncsInRow;
			}

			if (this.data.getTeleportManager().teleportTicks == 2) {
				threshold += 0.08;
			}

			--this.desyncPosTicks;
			if (this.data.isWasOnWater()) {
				double fixedLastMotion = lastMotionY;
				if (motionY > 0.0) {
					fixedLastMotion = lastMotionY + 0.04F;
				}

				prediction = this.data.getVelocityYTicks() == 0 ? this.data.getVelocityY() : fixedLastMotion * 0.8F - 0.02;
				threshold += this.data.isCollidedHorizontally() ? 0.4 : 0.35;
			} else if (this.data.isOnWater()) {
				threshold += 0.4;
			}

			if (this.data.isWasOnLava()) {
				double fixedLastMotion = lastMotionY;
				if (motionY > 0.0) {
					fixedLastMotion = lastMotionY + 0.04F;
				}

				prediction = this.data.getVelocityYTicks() == 0 ? this.data.getVelocityY() : fixedLastMotion * 0.5 - 0.02;
				threshold += this.data.isCollidedHorizontally() ? 0.4 : 0.35;
			} else if (this.data.isOnLava()) {
				threshold += 0.4;
			}

			double predictionDifference = Math.abs(prediction - this.data.deltas.motionY);
			if (Math.abs(lastMotionY - 0.083) < 0.001 && predictionDifference > 0.078) {
				prediction = 0.0;
				predictionDifference = Math.abs(prediction - this.data.deltas.motionY);
			}

			double offsetPlace = Math.abs(lastMotionY - 0.404444914);
			double offsetPlacePrediction = Math.abs(predictionDifference - 0.01524);
			if (to.ground && motionY < 0.0 && prediction < motionY && MathUtil.onGround(Math.abs(to.getY()))) {
				canFlag = false;
			}

			if (from.horizontal(to) < 0.0025 && this.data.getJumpBoost() > 0) {
				canFlag = false;
			}

			if (offsetPlace <= 1.0E-5 && offsetPlacePrediction <= 0.001) {
				threshold += 0.015625;
			} else if (this.data.elapsed(this.data.getUnderPlaceTicks()) <= 10) {
				threshold += 0.031;
			}

			boolean underBlock = this.data.elapsed(this.data.getLastCollidedV()) <= 2
				|| this.data.isUnderBlock()
				|| this.data.elapsed(this.data.getLastCollidedVGhost()) <= 3
				|| this.data.isUnderGhostBlock();
			boolean climbable = this.data.elapsed(this.data.getLastOnClimbable()) <= 8 || this.data.isOnLadder();
			boolean slime = this.data.elapsed(this.data.getLastOnSlime()) <= 2 || this.data.isOnSlime();
			boolean web = this.data.elapsed(this.data.getLastInWeb()) <= 5 || this.data.isInWeb();
			boolean piston = this.data.elapsed(this.data.getLastPistonPush()) <= 3;
			double predictionOffset = Math.abs(motionY - prediction);
			double maxVL = tickVel != null ? 4.5 : 2.5;
			if (this.data.isUnderGhostBlock()) {
				this.violations /= 2.0;
			}

			if (this.data.elapsed(this.data.getLastFlyTick()) < 80) {
				threshold += 0.8;
			}

			if (!(Math.abs(predictionOffset) > threshold + min)
				|| !(Math.abs(prediction) >= threshold + min)
				|| !canFlag
				|| this.data.elapsed(this.data.getLastGlide()) < 30
				|| this.data.elapsed(this.data.getLastRiptide()) < 30
				|| this.data.isUnderGhostBlock()
				|| this.data.isOnLiquid()
				|| this.data.isSpectating()
				|| this.data.isInsideBlock()
				|| this.data.isPossiblyTeleporting()
				|| this.data.elapsed(this.data.getLastFlyTick()) <= 30) {
				this.violations = Math.max(this.violations - 0.065, 0.0);
			} else if (underBlock || climbable || slime || web || piston || this.data.isUnderWeb()) {
				this.violations = Math.max(this.violations - 0.06235, 0.0);
			} else if (!e.to.ground && !e.from.ground && this.data.getAirTicks() > 0 && ++this.violations > maxVL) {
				String info = String.format(
					"predict: %.3f, motionY: %.3f\nthreshold: %f, ct/st: %d/%d\nteleport: %d\nvelocity: %.4f\nmove: %d\ndeltaX/deltaZ: %.3f/%.3f",
					prediction,
					motionY,
					threshold,
					this.data.getClientAirTicks(),
					this.data.getAirTicks(),
					this.data.getTeleportManager().teleportTicks,
					tickVel != null ? tickVel.getY() : 0.0,
					this.data.getMoveTicks(),
					this.data.deltas.deltaX,
					this.data.deltas.deltaZ
				);
				this.fail(info, this.getBanVL(), 200L);
			}
		}
	}
}
