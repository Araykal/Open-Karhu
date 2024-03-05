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
import me.liwk.karhu.event.SteerEvent;

@CheckInfo(
	name = "BadPackets (F)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false,
	credits = "§c§lCREDITS: §aOilSlug §7for the base idea."
)
public final class BadPacketsF extends PacketCheck {
	private double ticks;
	private boolean sent;

	public BadPacketsF(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SteerEvent && this.data.getTotalTicks() > 200) {
			this.sent = !((SteerEvent)packet).isUnmount();
		} else if (packet instanceof FlyingEvent) {
			if (this.sent) {
				if (++this.ticks > 3.0) {
					if (this.data.getBukkitPlayer().getVehicle() == null && !this.data.isRiding() && !this.data.isExitingVehicle()) {
						if (++this.violations > 5.0) {
							this.fail("* Sent vehicle packet without being inside a vehicle", this.getBanVL(), 110L);
						}
					} else {
						this.decrease(0.75);
					}
				}

				this.sent = false;
			} else {
				this.ticks = 0.0;
			}
		}
	}
}
