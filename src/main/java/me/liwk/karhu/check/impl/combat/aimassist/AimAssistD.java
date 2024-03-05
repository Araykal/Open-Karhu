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
	name = "AimAssist (D)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false
)
public class AimAssistD extends RotationCheck {
	private float lastDeltaYaw;
	private float lastLastDeltaYaw;

	public AimAssistD(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		float deltaYaw = Math.abs(to.yaw - from.yaw);
		if ((this.data.getLastAttackTick() < 4 || this.data.elapsed(this.data.getUnderPlaceTicks()) <= 6) && this.data.getTotalTicks() > 40 && !this.data.isPossiblyTeleporting()) {
			double range = 69.0;
			if (this.data.getLastTarget() != null) {
				range = this.data.getBukkitPlayer().getEyeLocation().clone().toVector().setY(0.0).distance(this.data.getLastTarget().getEyeLocation().clone().toVector().setY(0.0));
			}

			if (deltaYaw < 3.0F && this.lastDeltaYaw > 30.0F && this.lastLastDeltaYaw < 3.0F && range > 1.3) {
				this.fail("* Snappy aim\n §f* now: §b" + deltaYaw + "\n §f* l: §b" + this.lastDeltaYaw + "\n §f* ll: §b" + this.lastLastDeltaYaw, this.getBanVL(), 100L);
			}
		}

		this.lastLastDeltaYaw = deltaYaw;
		this.lastDeltaYaw = deltaYaw;
	}
}
