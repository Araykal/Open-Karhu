/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.autoclicker;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.SwingEvent;

@CheckInfo(
	name = "AutoClicker (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.AUTOCLICKER,
	experimental = false
)
public final class AutoClickerA extends PacketCheck {
	private int delay;
	private int clicks;

	public AutoClickerA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (this.canClick()) {
				if (this.data.isNewerThan8()) {
					if (this.data.elapsedMS(((SwingEvent)packet).getTimeStamp(), this.data.getLastFlying()) <= 60L) {
						++this.clicks;
					}
				} else {
					++this.clicks;
				}
			}
		} else if (packet instanceof FlyingEvent && ++this.delay >= 20) {
			if (this.clicks >= 1) {
				this.data.setLastCps(this.data.getCps());
				this.data.setCps((double)this.clicks);
				this.data.setHighestCps(Math.max((double)this.clicks, this.data.getHighestCps()));
			}

			if ((double)this.clicks > Karhu.getInstance().getConfigManager().getMaxCps() && !this.data.isHasDig()) {
				this.fail("* Too high cps\n §f* CPS: §b" + this.clicks, 300L);
			}

			this.delay = 0;
			this.clicks = 0;
		}
	}
}
