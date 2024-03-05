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
	name = "AutoClicker (C)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = false
)
public final class AutoClickerC extends PacketCheck {
	private final Deque<Integer> delays = new ArrayDeque<>();
	private int delay;

	public AutoClickerC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (this.checkClick()) {
				if (this.data.isNewerThan8()) {
					if (this.delay < 10 && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
						this.delays.add(this.delay);
					}
				} else if (this.delay < 10) {
					this.delays.add(this.delay);
				}

				if (this.delays.size() == 800) {
					int outliers = MathUtil.getOutliers(this.delays);
					double cps = 20.0 / MathUtil.average(this.delays);
					if (outliers <= 5) {
						if (++this.violations > 1.0) {
							this.fail("* Low outliers\n §f* O: §b" + outliers + "\n §f* CPS: §b" + cps, this.getBanVL(), 450L);
						}
					} else {
						this.violations = Math.max(this.violations - 0.5, 0.0);
					}

					this.delays.clear();
				}
			}

			this.delay = 0;
		} else if (packet instanceof FlyingEvent) {
			if (this.data.isUsingItem()) {
				this.delay = 0;
				return;
			}

			++this.delay;
		}
	}
}
