/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.util.MathUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@CheckInfo(
	name = "Scaffold (I)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldI extends PacketCheck {
	public ScaffoldI(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			ItemStack stack = ((BlockPlaceEvent)packet).getItemStack();
			boolean validBlock = stack != null && stack.getType() != Material.AIR;
			if (validBlock && !((BlockPlaceEvent)packet).isUsableItem()) {
				if (this.data.deltas.deltaYaw > 1.5F && this.data.deltas.deltaXZ > 0.12 && MathUtil.isNearlySame(this.data.deltas.accelXZ, this.data.deltas.lastAccelXZ, 1.0E-7)) {
					if (++this.violations > 3.0) {
						this.fail(
							"* Not slowing down\n §f* deltaYaw: §b"
								+ this.data.deltas.deltaYaw
								+ "\n §f* deltaXZ: §b"
								+ this.data.deltas.deltaXZ
								+ "\n §f* ac: §b"
								+ Math.abs(this.data.deltas.accelXZ - this.data.deltas.lastAccelXZ),
							this.getBanVL(),
							150L
						);
					}
				} else {
					this.violations = Math.max(this.violations - 0.075, 0.0);
				}
			}
		}
	}
}
