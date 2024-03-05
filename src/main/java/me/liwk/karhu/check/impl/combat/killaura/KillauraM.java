/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.killaura;

import java.util.Deque;
import java.util.LinkedList;
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
	name = "Killaura (M)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = true
)
public final class KillauraM extends RotationCheck {
	private final Deque<Float> pitches = new LinkedList<>();

	public KillauraM(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		float pitch = Math.abs(to.pitch - from.pitch);
		float yaw = Math.abs(to.yaw - from.yaw);
		if (((double)yaw > 0.0 || (double)pitch > 0.0) && this.data.getLastAttackTick() <= 1) {
			this.pitches.add(pitch);
		}

		if (this.pitches.size() == 40) {
			double avg = MathUtil.getAverage(this.pitches);
			double std = MathUtil.getStandardDeviation(this.pitches);
			double osc = MathUtil.getOscillation(this.pitches);
			if (osc > 40.0 && std > 12.5 && avg > 25.0) {
				if (++this.violations > (double)(this.data.getSensitivity() > 90 ? 5 : 2)) {
					this.fail(
						"* Randomized aim\n §f* std: §b"
							+ this.format(3, Double.valueOf(std))
							+ "\n §f* avg: §b"
							+ this.format(3, Double.valueOf(avg))
							+ "\n §f* osc: §b"
							+ this.format(3, Double.valueOf(osc)),
						this.getBanVL(),
						300L
					);
				}
			} else {
				this.violations = Math.max(this.violations - (this.data.getSensitivity() > 90 ? 0.2 : 0.05), -0.2);
			}

			this.pitches.clear();
		}
	}
}
