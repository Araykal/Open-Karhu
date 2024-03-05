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
	name = "AimAssist (E)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false
)
public class AimAssistE extends RotationCheck {
	private float lastDeltaPitch;
	private float lastDeltaYaw;
	private float lastGCD;

	public AimAssistE(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.to;
		CustomLocation from = update.from;
		float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
		float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
		boolean canCheck = this.data.elapsed(this.data.getLastCinematic()) > 5
			&& Math.abs(to.pitch) < 90.0F
			&& Math.abs(from.pitch) < 90.0F
			&& deltaPitch <= 5.0F
			&& !this.data.recentlyTeleported(5);
		double addition = (double)this.lastGCD < 0.003 ? 0.5 : 0.0;
		float gcdPITCH = MathUtil.getGcd(deltaPitch, this.lastDeltaPitch);
		float gcdYAW = MathUtil.getGcd(deltaYaw, this.lastDeltaYaw);
		if (this.data.getLastAttackTick() < 3 || this.data.elapsed(this.data.getPlaceTicks()) <= 4) {
			if (canCheck) {
				if ((double)deltaPitch > 0.2 && (double)Math.abs(deltaPitch - this.lastDeltaPitch) > 0.2 && (double)gcdPITCH < 0.008) {
					this.violations = Math.min(30.0, this.violations + 0.5 + addition);
					if (this.violations > 17.5) {
						this.fail("* Consistent rotations\n §f* gcd: §b" + gcdPITCH + " | " + gcdYAW + "\n §f* deltaPitch: §b" + deltaPitch, this.getBanVL(), 300L);
					}

					if (this.violations > 5.0) {
						this.data.setReduceNextDamage(true);
					}
				} else {
					this.violations = Math.max(this.violations - 0.65, 0.0);
				}

				this.lastGCD = gcdPITCH;
			} else {
				this.violations = Math.max(this.violations - 1.1, 0.0);
			}
		}

		this.lastDeltaPitch = deltaPitch;
		this.lastDeltaYaw = deltaYaw;
	}
}
