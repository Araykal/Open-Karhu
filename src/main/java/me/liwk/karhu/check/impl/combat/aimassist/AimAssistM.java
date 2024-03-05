/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.aimassist;

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
	name = "AimAssist (M)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = true
)
public class AimAssistM extends RotationCheck {
	private final Deque<Float> pitchList = new LinkedList<>();

	public AimAssistM(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		if (this.data.deltas.deltaYaw > 2.5F && Math.abs(to.pitch) <= 80.0F && !this.data.isCinematic() && this.data.getLastAttackTick() <= 3 && this.data.getSensitivity() != -1) {
			this.pitchList.add(this.data.deltas.deltaPitch);
		}

		if (this.pitchList.size() == 200) {
			double min = MathUtil.lowest(this.pitchList);
			double max = MathUtil.highest(this.pitchList);
			double difference = Math.abs(max - min);
			if (difference < (double)this.data.getPitchGCD() * 1.25) {
				this.fail(
					"* Weird change\n §f* d: §b" + this.format(4, Double.valueOf(difference)) + "\n §f* e: §b" + this.format(4, Double.valueOf((double)this.data.getPitchGCD() * 1.5)),
					this.getBanVL(),
					300L
				);
			}

			this.pitchList.clear();
		}
	}
}
