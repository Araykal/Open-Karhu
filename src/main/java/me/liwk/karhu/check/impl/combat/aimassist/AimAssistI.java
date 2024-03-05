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
	name = "AimAssist (I)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false
)
public class AimAssistI extends RotationCheck {
	private int zeroDeltaTicks;

	public AimAssistI(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
		float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
		if (this.data.getLastAttackTick() < 3 && !this.data.isPossiblyTeleporting()) {
			if (deltaPitch == 0.0F) {
				++this.zeroDeltaTicks;
			} else {
				this.zeroDeltaTicks = 0;
			}

			if (this.zeroDeltaTicks <= 40 || !(deltaYaw > 3.0F) || !(Math.abs(to.getPitch()) < 45.0F) || !(this.data.deltas.deltaXZ > 0.08)) {
				this.violations *= 0.75;
			} else if (++this.violations > 5.0) {
				this.fail("* Weird rotation\n §f* p: §b" + to.getPitch() + "\n §f* lp: §b" + from.getPitch(), this.getBanVL(), 300L);
			}
		}
	}
}
