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
import me.liwk.karhu.event.TransactionEvent;

@CheckInfo(
	name = "Killaura (N)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = true
)
public final class KillauraN extends PacketCheck {
	public int targetAmount;
	public int lastEntity;

	public KillauraN(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof AttackEvent) {
			int currentTarget = ((AttackEvent)packet).getEntityId();
			if (currentTarget != this.lastEntity) {
				++this.targetAmount;
			}

			this.lastEntity = currentTarget;
		} else if (packet instanceof FlyingEvent) {
			if (this.data.isPossiblyTeleporting()) {
				this.targetAmount = 0;
				return;
			}

			if (this.data.getClientVersion().getProtocolVersion() <= 47 && this.targetAmount > 1) {
				this.fail("* Multiaura\n §f* targets: §b" + this.targetAmount + "\n §f* cps: §b" + this.format(3, Double.valueOf(this.data.getCps())), this.getBanVL(), 300L);
			}

			this.targetAmount = 0;
		} else if (packet instanceof TransactionEvent && this.data.getClientVersion().getProtocolVersion() > 47) {
			if (this.targetAmount > 1) {
				this.fail("* hMultiaura (1.9)\n §f* targets: §b" + this.targetAmount + "\n §f* cps: §b" + this.format(3, Double.valueOf(this.data.getCps())), this.getBanVL(), 300L);
			}

			this.targetAmount = 0;
		}
	}
}
