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
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

@CheckInfo(
		name = "AutoClicker (U)",
		category = Category.COMBAT,
		subCategory = SubCategory.AUTOCLICKER,
		experimental = true
)
public final class AutoClickerU extends PacketCheck {
	int flying;
	double lastSTD;
	Deque<Integer> samples = new ArrayDeque();

	public AutoClickerU(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (!this.data.isHasDig() && !this.data.isPlacing() && !this.data.isUsingItem() && this.data.elapsed(this.data.getDigTicks()) > 5) {
				if (this.data.isNewerThan8()) {
					if (this.flying < 10 && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
						this.samples.add(this.flying);
					}
				} else if (this.flying < 10) {
					this.samples.add(this.flying);
				}

				if (this.samples.size() == 300) {
					int outliers = MathUtil.getOutliers(this.samples);
					double cps = 20.0 / MathUtil.average(this.samples);
					double std = new StandardDeviation().evaluate(MathUtil.dequeTranslator(this.samples));
					double kur = new Kurtosis().evaluate(MathUtil.dequeTranslator(this.samples));
					double ske = new Skewness().evaluate(MathUtil.dequeTranslator(this.samples));
					if (kur < 0.0 && ske < -0.5 && outliers <= 3) {
						this.fail(
								"* Weird randomization\n§f* §b" + String.format("std %.3f : sk %.3f : o %s : ku %s : cps %.1f", std, ske, outliers, kur, cps),
								this.getBanVL(),
								600L
						);
					}

					this.samples.clear();
					this.lastSTD = std;
				}
			}

			this.flying = 0;
		} else if (packet instanceof FlyingEvent && !((FlyingEvent)packet).isTeleport()) {
			++this.flying;
		}

	}
}