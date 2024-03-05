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

@CheckInfo(
	name = "Scaffold (G)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldG extends PacketCheck {
	private double lastDeltaPitch;

	public ScaffoldG(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent && !((BlockPlaceEvent)packet).isUsableItem()) {
			boolean validY = MathUtil.isNearlySame(this.data.getLocation().y, (double)((BlockPlaceEvent)packet).getBlockPos().getBlockY(), 2.0);
			if ((double)this.data.deltas.deltaPitch == this.lastDeltaPitch
				&& this.data.deltas.deltaYaw == 0.0F
				&& this.data.deltas.deltaXZ > 0.21
				&& this.data.deltas.deltaPitch > 1.0F
				&& validY) {
				if (++this.violations > 35.0) {
					this.fail("* Weird stuff\n §f* deltaPitch: §b" + this.data.deltas.deltaPitch + "\n §f* deltaXZ: §b" + this.data.deltas.deltaXZ, this.getBanVL(), 120L);
				}
			} else {
				this.decrease(0.35);
			}

			this.lastDeltaPitch = (double)this.data.deltas.deltaPitch;
		}
	}
}
