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
	name = "AimAssist (H)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false
)
public class AimAssistH extends RotationCheck {
	public AimAssistH(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
		if (this.data.getLastAttackTick() < 20 && !this.data.isPossiblyTeleporting()) {
			if (to.getPitch() != 0.0F || from.getPitch() != 0.0F || !(deltaYaw > 2.0F)) {
				this.violations *= 0.8;
			} else if (++this.violations > 3.0) {
				this.fail("* Weird rotation\n §f* p: §b" + to.getPitch() + "\n §f* lp: §b" + from.getPitch(), this.getBanVL(), 300L);
			}
		}
	}
}
