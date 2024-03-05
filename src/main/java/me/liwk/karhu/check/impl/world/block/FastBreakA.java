/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.block;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.RespawnEvent;

@CheckInfo(
	name = "FastBreak (A)",
	category = Category.WORLD,
	subCategory = SubCategory.BLOCK,
	experimental = false
)
public final class FastBreakA extends PacketCheck {
	private boolean tickStarted;

	public FastBreakA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (this.data.getClientVersion().getProtocolVersion() <= 47) {
			if (packet instanceof FlyingEvent) {
				this.tickStarted = false;
			} else if (packet instanceof DigEvent) {
				DiggingAction digType = ((DigEvent)packet).getDigType();
				switch (digType) {
					case START_DIGGING:
						this.tickStarted = true;
						break;
					case FINISHED_DIGGING:
						if (this.tickStarted) {
							this.fail("* Fastbreak (instant)\n\n(no debug provided)", this.getBanVL(), 600L);
						}
				}
			} else if (packet instanceof RespawnEvent) {
				this.tickStarted = false;
			}
		}
	}
}
