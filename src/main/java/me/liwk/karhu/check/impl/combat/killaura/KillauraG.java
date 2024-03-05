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
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;

@CheckInfo(
	name = "Killaura (G)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraG extends PacketCheck {
	private boolean sentInteract;

	public KillauraG(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan8()) {
			if (packet instanceof FlyingEvent) {
				this.sentInteract = false;
			} else if (packet instanceof BlockPlaceEvent && ((BlockPlaceEvent)packet).isUsableItem()) {
				this.sentInteract = true;
			} else if (packet instanceof DigEvent && ((DigEvent)packet).getDigType() == DiggingAction.RELEASE_USE_ITEM && this.sentInteract && !this.data.isPossiblyTeleporting()) {
				this.fail("* Illegal sword blocking order", this.getBanVL(), 600L);
			}
		}
	}
}
