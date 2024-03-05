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
	name = "AimAssist (B)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false
)
public class AimAssistB extends RotationCheck {
	private double bufferP;
	private double bufferY;

	public AimAssistB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		if (!this.data.recentlyTeleported(3) && !this.data.isRiding()) {
			float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
			float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
			float moduloYaw = deltaYaw % 1.0F;
			float moduloYaw2 = deltaYaw % 0.1F;
			float moduloYaw3 = deltaYaw % 0.05F;
			float roundYaw = (float)Math.round(to.yaw);
			float moduloPitch = deltaPitch % 1.0F;
			float moduloPitch2 = deltaPitch % 0.1F;
			float moduloPitch3 = deltaPitch % 0.05F;
			float roundPitch = (float)Math.round(to.pitch);
			if (Math.abs(to.pitch) < 90.0F && to.pitch > 0.0F && (double)deltaPitch > 0.0 && (moduloPitch == 0.0F || moduloPitch2 == 0.0F || moduloPitch3 == 0.0F || to.pitch == roundPitch)
				)
			 {
				if (++this.bufferP > 10.0) {
					this.fail("* Rounded pitch\n §f* deltaPitch: §b" + deltaPitch + "\n §f* moduloPitch: §b" + moduloPitch, this.getBanVL(), 300L);
				}
			} else {
				this.bufferP = Math.max(this.bufferP - 0.8, 0.0);
			}

			if ((double)deltaYaw > 0.0 && (moduloYaw == 0.0F || moduloYaw2 == 0.0F || moduloYaw3 == 0.0F || to.yaw == roundYaw)) {
				if (++this.bufferY > 10.0) {
					this.fail("* Rounded yaw\n §f* deltaYaw: §b" + deltaYaw + "\n §f* moduloYaw: §b" + moduloYaw, this.getBanVL(), 300L);
				}
			} else {
				this.bufferY = Math.max(this.bufferY - 1.0, 0.0);
			}
		}
	}
}
