/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.fly;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Fly (D)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.FLY,
	experimental = true
)
public final class FlyD extends PositionCheck {
	public FlyD(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		if (this.data.getVelocityYTicks() == 0) {
			double excepted = this.data.getVelocityY();
			if (excepted < this.data.deltas.motionY - 1.0 && !this.data.isGroundNearBox()) {
				if (++this.violations > 3.0) {
					this.fail(
						"* Not taking fall damage velocity\n §f* EXP: §b"
							+ excepted
							+ "\n §f* RES: §b"
							+ this.data.deltas.motionY
							+ "\n §f* TICK: §b"
							+ this.data.getAirTicks()
							+ " | "
							+ this.data.getClientAirTicks(),
						this.getBanVL(),
						300L
					);
				}
			} else {
				this.violations = Math.max(this.violations - 0.25, 0.0);
			}
		}
	}
}
