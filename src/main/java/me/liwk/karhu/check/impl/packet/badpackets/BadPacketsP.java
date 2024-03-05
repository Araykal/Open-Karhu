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
	name = "BadPackets (P)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsP extends PacketCheck {
	private float lpitch;
	private float lyaw;

	public BadPacketsP(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent && ((FlyingEvent)packet).hasLooked()) {
			if (((FlyingEvent)packet).getPitch() == this.lpitch && ((FlyingEvent)packet).getYaw() == this.lyaw && !this.data.isPossiblyTeleporting()) {
				this.fail("* Improper pitch\n §f* P: §b" + this.format(0, Float.valueOf(((FlyingEvent)packet).getPitch())), this.getBanVL(), 110L);
			}

			this.lpitch = ((FlyingEvent)packet).getPitch();
			this.lyaw = ((FlyingEvent)packet).getYaw();
		}
	}
}
