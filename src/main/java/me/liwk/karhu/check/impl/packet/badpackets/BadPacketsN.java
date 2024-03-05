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
	name = "BadPackets (N)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = true
)
public final class BadPacketsN extends PacketCheck {
	private int lastMove;

	public BadPacketsN(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			boolean collidedHorizontally = this.data.elapsed(this.data.getLastCollided()) < 10 && this.data.elapsed(this.data.getLastCollidedV()) > 10;
			if (!collidedHorizontally && ((FlyingEvent)packet).hasMoved()) {
				if (this.lastMove++ >= 3) {
					double moveDist = this.data.getLocation().toVector().distanceSquared(this.data.getLastLocation().toVector());
					boolean invalid = moveDist <= 1.0E-12 && moveDist != 0.0 && !this.data.isPossiblyTeleporting();
					if (invalid && !this.data.isRiding() && !this.data.recentlyTeleported(5)) {
						++this.violations;
						if (this.violations >= 2.5) {
							this.fail("Invalid movement packet state", this.getBanVL(), 300L);
						}
					} else {
						this.violations = Math.max(this.violations - 0.05, 0.0);
					}
				}
			} else {
				this.lastMove = 0;
			}
		}
	}
}
