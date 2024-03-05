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
import me.liwk.karhu.event.TransactionEvent;

@CheckInfo(
	name = "BadPackets (I)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = true
)
public final class BadPacketsI extends PacketCheck {
	private long lastFlag;

	public BadPacketsI(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof TransactionEvent) {
			long time = (long)((double)((TransactionEvent)packet).getNow() / 1000000.0);
			long flying = (long)((double)this.data.lastFlying / 1000000.0);
			if (time - flying <= (long)(this.data.isNewerThan8() ? '鱀' : 4000) + this.data.getTransactionPing()
				|| time - this.lastFlag <= 250L
				|| this.data.isSpectating()
				|| this.data.getBukkitPlayer().isDead()) {
				this.violations *= 0.2;
			} else if (++this.violations > 20.0) {
				this.fail("* Blink?\n T: §b" + (time - flying) / 50L, this.getBanVL(), 110L);
				this.lastFlag = time;
			}
		}
	}
}
