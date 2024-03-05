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
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

@CheckInfo(
	name = "AutoClicker (D)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = true
)
public final class AutoClickerD extends PacketCheck {
	int flying;
	double lastSTD;
	Deque<Integer> samples = new ArrayDeque<>();

	public AutoClickerD(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (this.flying < 10 && !this.data.isHasDig() && !this.data.isPlacing() && !this.data.isUsingItem()) {
				this.samples.add(this.flying);
			}

			if (this.samples.size() == 1000) {
				int outliers = MathUtil.getOutliers(this.samples);
				double std = new StandardDeviation().evaluate(MathUtil.dequeTranslator(this.samples));
				double kur = new Kurtosis().evaluate(MathUtil.dequeTranslator(this.samples));
				double sdd = Math.abs(std - this.lastSTD);
				double cps = 20.0 / MathUtil.average(this.samples);
				if (std < 0.8 && kur < 0.5 && sdd < 0.04 && outliers <= 6) {
					if (++this.violations > 1.0) {
						this.fail("* Repeating pattern\n §f* O: §b" + outliers + "\n §f* CPS: §b" + cps, this.getBanVL(), 450L);
					}
				} else {
					this.decrease(0.5);
				}

				this.samples.clear();
				this.lastSTD = std;
			}

			this.flying = 0;
		} else if (packet instanceof FlyingEvent && !((FlyingEvent)packet).isTeleport()) {
			++this.flying;
		}
	}
}
