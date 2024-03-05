/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.autoclicker;

import java.util.ArrayDeque;
import java.util.Deque;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;

@CheckInfo(
	name = "AutoClicker (I)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = false
)
public final class AutoClickerI extends PacketCheck {
	private final Deque<Integer> delays = new ArrayDeque<>();
	private int delay;

	public AutoClickerI(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof AttackEvent) {
			boolean valid = !this.data.isPlacing() && !this.data.isHasDig() && !this.data.isUsingItem() && this.data.elapsed(this.data.getDigTicks()) > 5;
			if (valid) {
				if (this.delay <= 5 && this.delay > 0) {
					this.delays.add(this.delay);
				}

				if (this.delays.size() == 40) {
					double average = MathUtil.average(this.delays);
					double std = MathUtil.stdDev(average, this.delays);
					if (!(average <= 2.0) || !(std < 0.15) || !(this.data.getCps() > 8.0)) {
						this.decrease(this.violations);
					} else if (++this.violations > 10.0) {
						this.fail("* No randomization\n§f* STD §b" + std + "\n§f* AVG §b" + average + "\n§f* CPS §b" + this.data.getCps(), this.getBanVL(), 200L);
						this.decrease(this.violations);
					}

					this.delays.removeFirst();
				}

				this.delay = 0;
			}
		} else if (packet instanceof FlyingEvent) {
			++this.delay;
		}
	}
}
