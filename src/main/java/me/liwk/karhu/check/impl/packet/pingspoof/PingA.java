/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.pingspoof;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.ConnectionHeartbeatEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "Ping (A)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = true
)
public final class PingA extends PacketCheck {
	private boolean kReceived;
	private boolean suspicious;

	public PingA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof ConnectionHeartbeatEvent) {
			this.suspicious = Math.abs(this.data.getPing() - this.data.getTransactionPing()) > 150L;
			this.kReceived = true;
		} else if (packet instanceof FlyingEvent && this.kReceived) {
			if (!this.suspicious || this.data.getPing() <= this.data.getTransactionPing() + 200L) {
				this.violations = Math.max(this.violations - 0.25, 0.0);
			} else if (++this.violations >= 5.0) {
				this.fail(
					"§f* Spoofed ping\n §f* diff: §b"
						+ Math.abs(this.data.getPing() - this.data.getTransactionPing())
						+ "\n §f* TRANSAC: §b"
						+ this.data.getTransactionPing()
						+ "\n §f* KEEPAL: §b"
						+ this.data.getPing(),
					this.getMaxvl(),
					40L
				);
			}

			this.kReceived = false;
		}
	}
}
