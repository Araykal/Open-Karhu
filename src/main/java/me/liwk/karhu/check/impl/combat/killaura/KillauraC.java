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
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.SwingEvent;

@CheckInfo(
	name = "Killaura (C)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = true
)
public final class KillauraC extends PacketCheck {
	private boolean swung;
	private int swungAt;
	private int swings;
	private int attacks;

	public KillauraC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (this.data.getClientVersion().getProtocolVersion() <= 47 && !Karhu.getInstance().isViaRewind()) {
			if (packet instanceof AttackEvent) {
				if (!this.swung) {
					this.fail("* NoSwing\n* sw " + this.data.elapsed(this.swungAt), this.getBanVL(), 300L);
				}
			} else if (packet instanceof FlyingEvent) {
				this.swung = false;
			} else if (packet instanceof SwingEvent) {
				this.swung = true;
				this.swungAt = this.data.getTotalTicks();
			}
		} else if (packet instanceof AttackEvent) {
			++this.attacks;
		} else if (packet instanceof FlyingEvent) {
			if (this.attacks > 1) {
				if (this.swings < 1) {
					this.fail("* NoSwing (1.9+/ViaRw)\n* sw " + this.data.elapsed(this.swungAt), this.getBanVL(), 300L);
				}

				this.attacks = 0;
				this.swings = 0;
			}
		} else if (packet instanceof SwingEvent) {
			++this.swings;
			this.swungAt = this.data.getTotalTicks();
		}
	}
}
