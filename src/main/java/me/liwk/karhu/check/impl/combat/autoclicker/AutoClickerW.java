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
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.commons.math3.util.FastMath;

@CheckInfo(
	name = "AutoClicker (W)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = true
)
public final class AutoClickerW extends PacketCheck {
	int flying;
	Deque<Integer> samples = new ArrayDeque<>();

	public AutoClickerW(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (!this.data.isHasDig() && !this.data.isPlacing() && !this.data.isUsingItem()) {
				if (this.data.isNewerThan8()) {
					if (this.flying <= 5 && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
						this.samples.add(this.flying);
					}
				} else if (this.flying <= 5) {
					this.samples.add(this.flying);
				}

				if (this.samples.size() >= 250) {
					int outliers = MathUtil.getOutliers(this.samples);
					int w = MathUtil.getW(this.samples);
					double kur = new Kurtosis().evaluate(MathUtil.dequeTranslator(this.samples));
					double ratio = MathUtil.getRatio(this.samples);
					double std = new StandardDeviation().evaluate(MathUtil.dequeTranslator(this.samples));
					double product = new Product().evaluate(MathUtil.dequeTranslator(this.samples));
					product *= FastMath.pow(2.0, -10);
					double cps = 20.0 / MathUtil.average(this.samples);
					if (cps > 8.5 && outliers <= 1 && w > 7 && product > 1.0E3 && product < 1.0E8 && kur < 0.5 && ratio > 10.0 && std > 0.4 && std < 1.0) {
						if (++this.violations > 3.0) {
							this.fail(
								"* No outliers\n§f* STD §b" + this.format(2, Double.valueOf(std)) + "\n§f* W §b" + w + "\n§f* KU §b" + kur + "\n§f* RAT §b" + ratio + "\n§f* O §b" + outliers,
								this.getBanVL(),
								250L
							);
						}
					} else {
						this.violations = Math.max(this.violations - 0.25, 0.0);
					}

					this.samples.clear();
				}
			}

			this.flying = 0;
		} else if (packet instanceof FlyingEvent && !((FlyingEvent)packet).isTeleport()) {
			++this.flying;
		}
	}
}
