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
	name = "Scaffold (H)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false
)
public final class ScaffoldH extends PacketCheck {
	public ScaffoldH(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			if (((BlockPlaceEvent)packet).isUsableItem() || this.data.isPossiblyTeleporting() || !this.isNotGroundBridging()) {
				return;
			}

			if (this.data.deltas.deltaPitch > 15.0F && this.data.deltas.deltaYaw == 0.0F && this.data.deltas.deltaXZ > 0.2) {
				if (++this.violations > 4.0) {
					this.fail("* Weird stuff\n §f* deltaPitch: §b" + this.data.deltas.deltaPitch + "\n §f* deltaXZ: §b" + this.data.deltas.deltaXZ, this.getBanVL(), 120L);
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
