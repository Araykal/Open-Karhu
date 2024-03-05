/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.killaura;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.InteractEvent;

@CheckInfo(
	name = "Killaura (B)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraB extends PacketCheck {
	private boolean sentInteract;
	private boolean sentAttack;

	public KillauraB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan8()) {
			if (packet instanceof FlyingEvent) {
				this.sentInteract = false;
				this.sentAttack = false;
			} else if (packet instanceof AttackEvent) {
				this.sentAttack = true;
			} else if (packet instanceof InteractEvent) {
				this.sentInteract = true;
			} else if (packet instanceof BlockPlaceEvent) {
				if (this.sentAttack && !this.sentInteract && this.data.getLastTarget() != null) {
					if (++this.violations > 1.0) {
						this.fail("* Illegal block order", this.getBanVL(), 60L);
					}
				} else {
					this.violations = Math.max(this.violations - 0.1, 0.0);
				}
			}
		}
	}
}
