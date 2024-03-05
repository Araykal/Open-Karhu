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
import me.liwk.karhu.event.HeldItemSlotEvent;

@CheckInfo(
	name = "BadPackets (J)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsJ extends PacketCheck {
	public BadPacketsJ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof HeldItemSlotEvent && ((HeldItemSlotEvent)packet).getSlot() < 0) {
			this.fail("* Negative slot packet", this.getBanVL(), 1L);
		}
	}
}
