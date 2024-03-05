/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.inventory;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.WindowEvent;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.entity.Player;

@CheckInfo(
	name = "Inventory (A)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.INVENTORY,
	experimental = true
)
public final class InventoryA extends PacketCheck {
	public InventoryA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof WindowEvent) {
			if (this.data.elapsed(this.data.getLastPistonPush()) <= 3) {
				return;
			}

			double offsetH = this.data.deltas.deltaXZ;
			double lastOffsetH = this.data.deltas.lastDXZ;
			if (!(offsetH - lastOffsetH >= 0.0) || !(offsetH > 0.1) || this.data.isAllowFlying() || this.data.isPossiblyTeleporting() || this.data.getVelocityHorizontal() != 0.0) {
				this.violations = Math.max(this.violations - 0.5, 0.0);
			} else if (++this.violations > (double)(this.data.isSprinting() ? 2 : 4)) {
				this.fail("* Moving while clicking inventory slots\n §f* deltaXZ §b" + offsetH, this.getBanVL(), 300L);
				if (this.violations > (double)(this.data.isSprinting() ? 3 : 5)) {
					Player player = this.data.getBukkitPlayer();
					if (player != null) {
						Tasker.run(player::closeInventory);
						MiscellaneousAlertPoster.postMitigation(this.data.getName() + " -> inventory closed (A)");
					}
				}
			}
		}
	}
}
