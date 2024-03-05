/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.badpackets;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AbilityEvent;
import me.liwk.karhu.event.Event;

@CheckInfo(
	name = "BadPackets (C)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsC extends PacketCheck {
	public BadPacketsC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof AbilityEvent) {
			if (this.data.elapsed(this.data.getLastFlyTick()) <= 30 || this.data.getTotalTicks() <= 60) {
				this.violations = 0.0;
			} else if (++this.violations > 2.0) {
				this.fail("* Sent ability packet without flying", this.getBanVL(), 110L);
			}
		}
	}
}
