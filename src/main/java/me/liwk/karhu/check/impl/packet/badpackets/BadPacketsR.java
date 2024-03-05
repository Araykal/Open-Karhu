/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.badpackets;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.ActionEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "BadPackets (R)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsR extends PacketCheck {
	private int sprints;

	public BadPacketsR(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan8()) {
			if (packet instanceof FlyingEvent) {
				if (this.sprints > 1 && !this.data.isPossiblyTeleporting()) {
					this.fail("* Too many actions", 300L);
				}

				this.sprints = 0;
			} else if (packet instanceof ActionEvent) {
				if (((ActionEvent)packet).getAction().equals(Action.START_SPRINTING)) {
					++this.sprints;
				}

				if (((ActionEvent)packet).getAction().equals(Action.STOP_SPRINTING)) {
					++this.sprints;
				}
			}
		}
	}
}
