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
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "AimAssist (J)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = true,
	credits = "§c§lCREDITS: §aWizzard §7made this check."
)
public class AimAssistJ extends RotationCheck {
	public AimAssistJ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		if (this.data.getSensitivityY() != -1.0F && this.data.getLastAttackTick() <= 1 && !this.data.recentlyTeleported(5)) {
			float fixedYaw = this.fixedSensitivity(this.data.getSensitivityY(), to.yaw);
			float fixedPitch = this.fixedSensitivity(this.data.getSensitivityY(), to.pitch);
			float diffYaw = Math.abs(to.yaw - fixedYaw);
			float diffPitch = Math.abs(to.pitch - fixedPitch);
			if ((double)Math.abs(to.yaw - fixedYaw) != 0.0 && (double)Math.abs(to.pitch - fixedPitch) != 0.0) {
				this.decrease(0.75);
			} else if (++this.violations > 10.0) {
				this.fail("* Round gcd patch\n §f* diffYaw: §b" + diffYaw + "\n §f* diffPitch: §b" + diffPitch, this.getBanVL(), 300L);
			}
		}
	}

	private float fixedSensitivity(float sensitivity, float angle) {
		float f = sensitivity * 0.6F + 0.2F;
		float gcd = f * f * f * 1.2F;
		return angle - angle % gcd;
	}
}
