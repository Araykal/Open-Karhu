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
	name = "BadPackets (E)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsE extends PacketCheck {
	private int lastSlot = -420;

	public BadPacketsE(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof HeldItemSlotEvent) {
			int slot = ((HeldItemSlotEvent)packet).getSlot();
			if (slot == this.lastSlot && slot != this.data.lastServerSlot) {
				this.fail("* Sent 2 same held slot packets", this.getBanVL(), 210L);
			}

			this.lastSlot = ((HeldItemSlotEvent)packet).getSlot();
		}
	}
}
