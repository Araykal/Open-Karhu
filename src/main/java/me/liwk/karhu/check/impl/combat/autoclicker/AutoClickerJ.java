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
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

@CheckInfo(
	name = "AutoClicker (J)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = true
)
public final class AutoClickerJ extends PacketCheck {
	private final Deque<Integer> delays = new ArrayDeque<>();
	private final Deque<Double> samples = new ArrayDeque<>();
	private int delay;

	public AutoClickerJ(KarhuPlayer data, Karhu karhu) {
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

				if (this.delays.size() >= 60) {
					double skewness = new Skewness().evaluate(MathUtil.dequeTranslator(this.delays));
					if (this.samples.add(skewness) && this.samples.size() >= 30) {
						double avgSkewness = MathUtil.average(this.samples);
						double stdSkewness = new StandardDeviation().evaluate(MathUtil.dequeTranslator(this.samples));
						if (!(avgSkewness < 0.0) || !(stdSkewness < 2.0) || !(this.data.getCps() > 8.0)) {
							this.decrease(0.75);
						} else if (this.increase(1.0) > 2.0) {
							this.fail("* Bad randomization\n§f* STD §b" + stdSkewness + "\n§f* AVG §b" + avgSkewness + "\n§f* CPS §b" + this.data.getCps(), this.getBanVL(), 200L);
						}

						this.samples.removeFirst();
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
