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
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "Killaura (H)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraH extends PacketCheck {
	private boolean sentDig;
	private boolean sentPlace;

	public KillauraH(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan8()) {
			if (packet instanceof FlyingEvent) {
				this.sentDig = false;
				this.sentPlace = false;
			} else if (packet instanceof AttackEvent) {
				if (!this.sentPlace & this.sentDig) {
					if (++this.violations > 1.0) {
						this.fail("* Illegal block order", this.getBanVL(), 60L);
					}
				} else {
					this.violations = Math.max(this.violations - 0.1, 0.0);
				}
			} else if (packet instanceof DigEvent) {
				DiggingAction type = ((DigEvent)packet).getDigType();
				if (type != DiggingAction.DROP_ITEM_STACK && type != DiggingAction.DROP_ITEM) {
					this.sentDig = true;
				}
			} else if (packet instanceof BlockPlaceEvent) {
				this.sentPlace = true;
			}
		}
	}
}
