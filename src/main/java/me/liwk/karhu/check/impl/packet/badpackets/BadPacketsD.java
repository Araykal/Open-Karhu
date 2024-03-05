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
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "BadPackets (D)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsD extends PacketCheck {
	public BadPacketsD(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			if (this.data.getTotalTicks() < 300) {
				return;
			}

			if (this.data.getPing() == 0L && this.data.getTransactionPing() > 1L || this.data.getPing() > 1L && this.data.getTransactionPing() == 0L) {
				if (this.violations > 5.0) {
					this.fail("§b* §fNull ping\n§b* §fKPing=§b" + this.data.getPing() + "\n§b* §fTPing=§b" + this.data.getTransactionPing(), this.getBanVL(), 110L);
				}
			} else {
				this.violations = 0.0;
			}
		}
	}
}
