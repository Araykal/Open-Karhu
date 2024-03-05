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
import org.apache.commons.math3.stat.descriptive.moment.SemiVariance;

@CheckInfo(
	name = "AutoClicker (K)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = true
)
public final class AutoClickerK extends PacketCheck {
	private int flying;
	private Deque<Integer> samples = new ArrayDeque<>();

	public AutoClickerK(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (this.data.isNewerThan8()) {
				if (this.flying < 8 && this.canClick() && this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 70L) {
					this.samples.add(this.flying);
				}
			} else if (this.flying < 8 && this.canClick()) {
				this.samples.add(this.flying);
			}

			if (this.samples.size() == 500) {
				double std = MathUtil.getStandardDeviation(this.samples);
				double cps = 20.0 / MathUtil.getAverage(this.samples);
				double semiVar = new SemiVariance().evaluate(MathUtil.dequeTranslator(this.samples));
				double divided = semiVar / std;
				if (cps > 8.0 && divided < 0.06 && std < 0.75) {
					if (this.increase(1.0) > 1.0) {
						this.fail("* Low variation\n §f* STD: §b" + std + "\n §f* DIVIDED: §b" + divided + "\n §f* SEMIV: §b" + semiVar + "\n §f* CPS: §b" + cps, this.getBanVL(), 450L);
					}
				} else {
					this.decrease(0.25);
				}

				this.samples.clear();
			}

			this.flying = 0;
		} else if (packet instanceof FlyingEvent && !((FlyingEvent)packet).isTeleport()) {
			++this.flying;
		}
	}
}
