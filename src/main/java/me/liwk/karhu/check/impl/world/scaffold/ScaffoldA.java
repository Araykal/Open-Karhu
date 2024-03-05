/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "Scaffold (A)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false
)
public final class ScaffoldA extends PacketCheck {
	private long lastFlying;

	public ScaffoldA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			long diff = ((BlockPlaceEvent)packet).getTimeMillis() - this.lastFlying;
			if (((BlockPlaceEvent)packet).getItemStack() != null && ((BlockPlaceEvent)packet).getItemStack().getType().isBlock() && !this.data.isPossiblyTeleporting()) {
				if (diff >= 10L
					|| this.data.isLagging(this.data.getTotalTicks())
					|| this.data.elapsed(this.data.getLastPacketDrop()) <= 5
					|| this.getKarhu().isServerLagging(((BlockPlaceEvent)packet).getTimeMillis())
					|| !(this.getKarhu().getTPS() >= 19.98)) {
					this.decrease(0.8);
				} else if (++this.violations > 8.0) {
					this.fail("* Irregular place\n §f* delta: §b" + diff + "\n §f* deltaXZ: §b" + this.data.deltas.deltaXZ, this.getBanVL(), 120L);
				}
			}
		} else if (packet instanceof FlyingEvent) {
			this.lastFlying = ((FlyingEvent)packet).getCurrentTimeMillis();
		}
	}
}
