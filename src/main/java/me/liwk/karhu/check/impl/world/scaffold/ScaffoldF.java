/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import lombok.SneakyThrows;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import org.bukkit.Material;
import org.bukkit.block.Block;

@CheckInfo(
	name = "Scaffold (F)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldF extends PacketCheck {
	public ScaffoldF(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			if (((BlockPlaceEvent)packet).isUsableItem()) {
				return;
			}

			if ((double)this.data.deltas.deltaPitch < 0.7 && this.data.deltas.deltaYaw > 100.0F && this.data.deltas.deltaXZ > 0.2 && this.isNotGroundBridging()) {
				if (++this.violations > 5.0) {
					this.fail(
						"* Weird stuff\n §f* deltaPitch | deltaYaw §b" + this.data.deltas.deltaPitch + " | " + this.data.deltas.deltaYaw + "\n §f* deltaXZ: §b" + this.data.deltas.deltaXZ, 120L
					);
				}
			} else {
				this.decrease(0.25);
			}
		}
	}
@SneakyThrows
	public boolean isNotGroundBridging() {

			Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(this.data.getLocation().clone().subtract(0.0, 2.0, 0.0).toLocation(this.data.getWorld()));
			if (block == null) {
				return false;
			} else {
				return block.getType() == Material.AIR;
			}

	}
}
