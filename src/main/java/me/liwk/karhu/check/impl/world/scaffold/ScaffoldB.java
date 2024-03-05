/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

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
	name = "Scaffold (B)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false
)
public final class ScaffoldB extends PacketCheck {
	public Long lastSwing;
	public Long lastFlying;

	public ScaffoldB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			this.lastFlying = ((FlyingEvent)packet).getCurrentTimeMillis();
			if (this.lastSwing != null) {
				double delay = (double)(this.lastFlying - this.lastSwing);
				if (!(delay < 60.0)
					|| !(delay > 40.0)
					|| Karhu.getInstance().isViaRewind()
					|| this.data.hasFast()
					|| this.data.isPossiblyTeleporting()
					|| this.data.isLagging(this.data.getTotalTicks())) {
					this.violations = Math.max(this.violations - 0.35, 0.0);
				} else if (++this.violations > 3.0) {
					this.fail("* Post swing\n §f* D §b" + delay, this.getBanVL(), 60000L);
				}

				this.lastSwing = null;
			}
		} else if (packet instanceof SwingEvent) {
			if (this.lastFlying != null && ((SwingEvent)packet).getTimeStampMS() - this.lastFlying < 2L) {
				this.lastSwing = this.lastFlying;
			} else {
				this.violations = Math.max(this.violations - 0.35, 0.0);
			}
		}
	}
}
