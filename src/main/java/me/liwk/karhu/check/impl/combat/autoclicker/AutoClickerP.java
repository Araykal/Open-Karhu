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
	name = "AutoClicker (P)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = true
)
public final class AutoClickerP extends PacketCheck {
	private final Deque<Integer> delays = new ArrayDeque<>();
	private final Deque<Integer> delays2 = new ArrayDeque<>();
	private int delay;

	public AutoClickerP(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			boolean valid = !this.data.isPlacing() && !this.data.isHasDig() && !this.data.isUsingItem() && this.data.elapsed(this.data.getDigTicks()) > 4;
			if (this.data.isNewerThan8()) {
				if (this.delay < 10 && valid && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
					this.delays.add(this.delay);
				}
			} else if (this.delay < 10 && valid) {
				this.delays.add(this.delay);
			}

			if (this.delays.size() == 50) {
				int osc = (int)MathUtil.getOscillation(this.delays);
				this.delays2.add(osc);
				if (this.delays2.size() == 8) {
					double stdo = MathUtil.getStandardDeviation(this.delays2);
					double cps = 20.0 / MathUtil.getAverage(this.delays);
					if (cps > 6.5 && stdo < 0.3) {
						this.fail(String.format("C %.2f STDO %.2f", cps, stdo), this.getBanVL(), 120L);
					}

					this.delays2.clear();
				}

				this.delays.clear();
			}

			this.delay = 0;
		} else if (packet instanceof FlyingEvent) {
			++this.delay;
		}
	}
}
