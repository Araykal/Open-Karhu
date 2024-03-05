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
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.SwingEvent;
import me.liwk.karhu.util.MathUtil;

@CheckInfo(
	name = "AutoClicker (L)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = false
)
public final class AutoClickerL extends PacketCheck {
	private final Deque<Integer> delays = new ArrayDeque<>();
	private int delay;

	public AutoClickerL(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			boolean valid = !this.data.isPlacing() && !this.data.isHasDig() && !this.data.isUsingItem() && this.data.elapsed(this.data.getDigTicks()) > 5;
			if (valid) {
				if (this.data.isNewerThan8()) {
					if (this.delay < 10 && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
						this.delays.add(this.delay);
					}
				} else if (this.delay < 10) {
					this.delays.add(this.delay);
				}

				if (this.delays.size() == 150) {
					double std = MathUtil.getStandardDeviation(this.delays);
					double cps = 20.0 / MathUtil.average(this.delays);
					if (std < 0.445) {
						if (++this.violations > 3.0) {
							this.fail("* Poor randomization\n§f* STD §b" + MathUtil.getStandardDeviation(this.delays) + "\n§f* CPS §b" + cps, this.getBanVL(), 200L);
						}
					} else {
						this.decrease(0.65);
					}

					this.delays.clear();
				}

				this.delay = 0;
			}
		} else if (packet instanceof FlyingEvent) {
			++this.delay;
		}
	}
}
