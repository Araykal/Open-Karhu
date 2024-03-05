/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.water;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Jesus (A)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.JESUS,
	experimental = true
)
public final class JesusA extends PositionCheck {
	public JesusA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		if ((this.data.isOnLiquid() || this.data.isAboveButNotInWater())
			&& !this.data.isPossiblyTeleporting()
			&& this.data.elapsed(this.data.getUnderPlaceTicks()) > this.data.getPingInTicks() + 5
			&& this.data.elapsed(this.data.getLastPistonPush()) > 3
			&& this.data.elapsed(this.data.getLastFlyTick()) > 30) {
			if (this.data.getAirTicks() > 4 && update.isGround()) {
				if (++this.violations > 3.0) {
					this.fail("* Wrong groundstate on liquid\n§f* Inside §b" + this.data.isOnLiquid() + "\n§f* Above §b" + this.data.isAboveButNotInWater(), this.getBanVL(), 300L);
				}
			} else {
				this.violations = Math.max(this.violations - 0.25, 0.0);
			}
		}
	}
}
