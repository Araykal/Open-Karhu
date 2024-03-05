/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.badpackets;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.HeldItemSlotEvent;

@CheckInfo(
	name = "BadPackets (B)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsB extends PacketCheck {
	private boolean placing;

	public BadPacketsB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof HeldItemSlotEvent) {
			if (!this.placing || !this.data.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
				this.violations = Math.max(this.violations - 0.3, 0.0);
			} else if (++this.violations > 1.0) {
				this.fail("* Placing while changing slot", this.getBanVL(), 110L);
			}
		} else if (packet instanceof FlyingEvent) {
			this.placing = false;
		} else if (packet instanceof BlockPlaceEvent) {
			this.placing = true;
		}
	}
}
