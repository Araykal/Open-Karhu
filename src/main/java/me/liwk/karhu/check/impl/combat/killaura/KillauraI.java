/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.killaura;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.InteractEvent;

@CheckInfo(
	name = "Killaura (I)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraI extends PacketCheck {
	private long sentDig;

	public KillauraI(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!(packet instanceof AttackEvent) && !(packet instanceof InteractEvent)) {
			if (packet instanceof DigEvent) {
				DiggingAction type = ((DigEvent)packet).getDigType();
				if (type != DiggingAction.DROP_ITEM_STACK && type != DiggingAction.DROP_ITEM) {
					this.sentDig = ((DigEvent)packet).getNow();
				}
			}
		} else {
			long now;
			if (packet instanceof AttackEvent) {
				now = ((AttackEvent)packet).getNow();
			} else {
				now = ((InteractEvent)packet).getNow();
			}

			long delay = (long)((double)(now - this.sentDig) / 1000000.0);
			if (delay >= 10L || this.data.elapsed(this.data.getLastPacketDrop()) <= 5 || this.getKarhu().isServerLagging(now) || !(this.getKarhu().getTPS() >= 19.95)) {
				this.violations = Math.max(this.violations - 0.35, 0.0);
			} else if (++this.violations > 5.0) {
				this.fail("* Illegal block order", this.getBanVL(), 60L);
			}
		}
	}
}
