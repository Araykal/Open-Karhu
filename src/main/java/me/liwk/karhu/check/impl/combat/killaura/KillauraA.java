/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.killaura;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "Killaura (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraA extends PacketCheck {
	public Long lastUseEntity;
	public Long lastFlying;

	public KillauraA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			this.lastFlying = ((FlyingEvent)packet).getCurrentTimeMillis();
			if (this.lastUseEntity != null) {
				double delay = (double)(this.lastFlying - this.lastUseEntity);
				if (!(delay < 60.0) || !(delay > 40.0) || this.data.hasFast() || this.data.isPossiblyTeleporting() || this.data.isLagging(this.data.getTotalTicks())) {
					this.violations = Math.max(this.violations - 0.35, 0.0);
				} else if (++this.violations > 3.0) {
					this.fail("* Post killaura\n §f* D §b" + delay, this.getBanVL(), 600L);
				}

				this.lastUseEntity = null;
			}
		} else if (packet instanceof AttackEvent) {
			if (this.lastFlying != null && ((AttackEvent)packet).getTimeMillis() - this.lastFlying < 2L) {
				this.lastUseEntity = this.lastFlying;
			} else {
				this.violations = Math.max(this.violations - 0.35, 0.0);
			}
		}
	}
}
