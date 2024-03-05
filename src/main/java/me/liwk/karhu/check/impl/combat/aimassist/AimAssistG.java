/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.aimassist;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.RotationCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "AimAssist (G)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = true
)
public class AimAssistG extends RotationCheck {
	double lastDeltaYaw;
	double lastDeltaYawAccel;
	double lastFlagAccel;

	public AimAssistG(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		double deltaPitch = (double)Math.abs(to.getPitch() - from.getPitch());
		double deltaYaw = (double)Math.abs(to.getYaw() - from.getYaw());
		double yawAccel = Math.abs(deltaYaw - this.lastDeltaYaw);
		double accelDiff = Math.abs(yawAccel - this.lastDeltaYawAccel);
		double gcdYAW = MathUtil.getGcd(deltaYaw, this.lastDeltaYaw);
		boolean invalidAim = deltaYaw > 1.0 && deltaYaw < 30.0 && deltaPitch < 25.0 && yawAccel < 0.0015;
		double addition = gcdYAW > 0.01 ? 1.0 : 0.75;
		double wildcard = yawAccel == this.lastFlagAccel ? 0.25 : 0.0;
		if (this.data.getLastAttackTick() <= 2 && !this.data.isPossiblyTeleporting()) {
			if (invalidAim) {
				this.violations = Math.min(this.violations + addition + wildcard, 10.0);
				if (this.violations > 5.0) {
					this.fail("* Consistent rotations\n §f* gcdY: §b" + gcdYAW + "\n §f* A: §b" + yawAccel + "\n §f* AD: §b" + accelDiff, this.getBanVL(), 300L);
				}

				this.lastFlagAccel = yawAccel;
			} else {
				this.violations = Math.max(this.violations - 0.5, 0.0);
			}
		}

		this.lastDeltaYaw = deltaYaw;
		this.lastDeltaYawAccel = yawAccel;
	}
}
