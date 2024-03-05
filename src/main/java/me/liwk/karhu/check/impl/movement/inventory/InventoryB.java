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
	name = "Inventory (B)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.INVENTORY,
	experimental = true
)
public final class InventoryB extends PacketCheck {
	public InventoryB(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof WindowEvent) {
			if (this.data.getLastAttackTick() <= 1) {
				if (++this.violations > 1.0) {
					this.fail("* Attacking while clicking inventory\n §f* deltaXZ §b" + this.data.deltas.deltaXZ, this.getBanVL(), 300L);
					if (this.violations > 2.0) {
						Player player = this.data.getBukkitPlayer();
						if (player != null) {
							Tasker.run(player::closeInventory);
							MiscellaneousAlertPoster.postMitigation(this.data.getName() + " -> inventory closed (B)");
						}
					}
				}
			} else {
				this.violations = Math.max(this.violations - 0.5, 0.0);
			}
		}
	}
}
