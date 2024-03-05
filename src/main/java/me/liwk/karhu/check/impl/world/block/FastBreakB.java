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
import me.liwk.karhu.event.RespawnEvent;
import me.liwk.karhu.event.SwingEvent;

@CheckInfo(
	name = "FastBreak (B)",
	category = Category.WORLD,
	subCategory = SubCategory.BLOCK,
	experimental = false
)
public final class FastBreakB extends PacketCheck {
	private int blockHitDelay;

	public FastBreakB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof SwingEvent) {
			if (this.blockHitDelay > 0) {
				--this.blockHitDelay;
			}
		} else if (packet instanceof DigEvent) {
			DiggingAction digType = ((DigEvent)packet).getDigType();
			switch (digType) {
				case START_DIGGING:
					this.blockHitDelay = 5;
					break;
				case FINISHED_DIGGING:
					this.blockHitDelay = 5;
			}
		} else if (packet instanceof RespawnEvent) {
			this.blockHitDelay = 0;
		}
	}
}
