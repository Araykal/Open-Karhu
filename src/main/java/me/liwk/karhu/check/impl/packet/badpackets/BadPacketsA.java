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
	name = "BadPackets (A)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsA extends PacketCheck {
	public BadPacketsA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent
			&& (((FlyingEvent)packet).hasMoved() || ((FlyingEvent)packet).hasLooked())
			&& Math.abs(((FlyingEvent)packet).getPitch()) > 90.0F
			&& !this.data.isPossiblyTeleporting()) {
			this.fail("* Improper pitch\n §f* P: §b" + this.format(0, Float.valueOf(((FlyingEvent)packet).getPitch())), this.getBanVL(), 110L);
		}
	}
}
