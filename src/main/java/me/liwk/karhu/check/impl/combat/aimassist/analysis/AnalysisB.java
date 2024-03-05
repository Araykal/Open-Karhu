/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.aimassist.analysis;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.RotationCheck;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.update.MovementUpdate;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Analysis (B)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = true
)
public class AnalysisB extends RotationCheck {
	private double lastAngle;
	private double lastAngleDiff;

	public AnalysisB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		if (this.data.getLastAttackTick() <= 60 && this.data.getLastTarget() != null) {
			if (this.data.getLastAttackTick() <= 40) {
				EntityData edata = this.data.getEntityData().get(this.data.getLastTarget().getEntityId());
				if (edata != null) {
					CustomLocation to = update.getTo();
					CustomLocation from = update.getFrom();
					float deltaPitch = Math.abs(to.getPitch() - from.getPitch());
					float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
					double x = edata.getEntityBoundingBox().getCenterX();
					double z = edata.getEntityBoundingBox().getCenterZ();
					double direction = MathUtil.getDirection(this.data.getLocation(), new Vector(x, 0.0, z));
					double angle = MathUtil.getAngleDistance((double)this.data.getLocation().getYaw(), direction);
					double aDiff = Math.abs(angle - this.lastAngle);
					double angleDiffDiff = Math.abs(aDiff - this.lastAngleDiff);
					if ((double)deltaYaw > 3.5 && aDiff <= 0.075 && this.data.deltas.deltaXZ > 0.1) {
						if (++this.violations > 5.0) {
							this.fail(
								"* Aimlock\n §f* p: §b" + deltaPitch + "\n §f* y: §b" + deltaYaw + "\n §f* ang: §b" + angle + "\n §f* ad: §b" + aDiff + "\n §f* add: §b" + angleDiffDiff,
								this.getBanVL(),
								300L
							);
						}
					} else {
						this.violations = Math.max(this.violations - 0.15, 0.0);
					}

					this.lastAngle = angle;
					this.lastAngleDiff = aDiff;
				}
			}
		} else {
			this.violations = 0.0;
		}
	}
}
