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
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.InteractEvent;

@CheckInfo(
	name = "Killaura (J)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false,
	credits = "§c§lCREDITS: §aMexify §7made this check."
)
public final class KillauraJ extends PacketCheck {
	private boolean sent;

	public KillauraJ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan8()) {
			if (!(packet instanceof AttackEvent) && !(packet instanceof InteractEvent)) {
				if (packet instanceof DigEvent) {
					DiggingAction type = ((DigEvent)packet).getDigType();
					if (type == DiggingAction.START_DIGGING || type == DiggingAction.CANCELLED_DIGGING || type == DiggingAction.RELEASE_USE_ITEM) {
						this.sent = true;
					}
				} else if (packet instanceof FlyingEvent) {
					this.sent = false;
				}
			} else if (this.sent && !this.data.isPossiblyTeleporting()) {
				this.fail("* Illegal block order", this.getBanVL(), 90L);
			}
		}
	}
}
