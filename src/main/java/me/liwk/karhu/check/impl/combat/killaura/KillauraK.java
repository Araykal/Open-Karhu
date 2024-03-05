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
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;

@CheckInfo(
	name = "Killaura (K)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false,
	credits = "§c§lCREDITS: §aMexify §7made this check."
)
public final class KillauraK extends PacketCheck {
	public KillauraK(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof DigEvent) {
			DiggingAction type = ((DigEvent)packet).getDigType();
			if (this.data.getClientVersion().getProtocolVersion() <= 47 && type == DiggingAction.RELEASE_USE_ITEM && (this.data.isPlacing() || this.data.isUsingItem())) {
				this.fail("* Illegal block order", this.getBanVL(), 120L);
			}
		}
	}
}
