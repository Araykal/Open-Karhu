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
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

@CheckInfo(
	name = "AutoClicker (H)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = true
)
public final class AutoClickerH extends PacketCheck {
	int flying;
	Deque<Integer> samples = new ArrayDeque<>();

	public AutoClickerH(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!(packet instanceof SwingEvent)) {
			if (packet instanceof FlyingEvent && !((FlyingEvent)packet).isTeleport()) {
				++this.flying;
			}
		} else {
			if (this.canClick()) {
				if (this.data.isNewerThan8()) {
					if (this.flying < 10 && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
						this.samples.add(this.flying);
					}
				} else if (this.flying < 10) {
					this.samples.add(this.flying);
				}

				if (this.samples.size() >= 75) {
					double entropy = MathUtil.getEntropy(this.samples);
					if (entropy >= 0.635) {
						this.clearSamples();
						return;
					}

					double std = new StandardDeviation().evaluate(MathUtil.dequeTranslator(this.samples));
					if (std >= 0.5) {
						this.clearSamples();
						return;
					}

					double cps = 20.0 / MathUtil.average(this.samples);
					if (cps > 9.0 && entropy < 0.635 && std < 0.5 && cps != 20.0) {
						if (++this.violations > 1.0) {
							this.fail(
								"* Low randomization\n§f* §b" + String.format("std %.3f : entropy %.3f : o %s : cps %.1f", std, entropy, MathUtil.getOutliers(this.samples), cps), this.getBanVL(), 125L
							);
						}
					} else {
						this.violations = 0.0;
					}

					this.clearSamples();
				}
			}

			this.flying = 0;
		}
	}

	private void clearSamples() {
		this.samples.clear();
		this.flying = 0;
		this.violations = 0.0;
	}
}
