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

@CheckInfo(
	name = "Killaura (F)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraF extends PacketCheck {
	public KillauraF(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof AttackEvent && (this.data.isPlacing() || this.data.isBlocking() && this.data.getBukkitPlayer().isBlocking())) {
			this.fail("* Illegal sword blocking order\n §f* P: §b" + this.data.isPlacing() + "\n §f* B: §b" + this.data.isBlocking(), this.getBanVL(), 600L);
		}
	}
}
