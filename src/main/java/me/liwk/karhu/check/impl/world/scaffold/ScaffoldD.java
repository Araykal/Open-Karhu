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
import me.liwk.karhu.event.FlyingEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;

@CheckInfo(
	name = "Scaffold (D)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false
)
public final class ScaffoldD extends PacketCheck {
	private int delay;

	public ScaffoldD(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			int face = ((BlockPlaceEvent)packet).getFace();
			if (((BlockPlaceEvent)packet).isUsableItem()) {
				return;
			}

			if (face < 0 || face > 6) {
				return;
			}

			this.delay = 0;
		} else if (packet instanceof FlyingEvent
			&& ++this.delay <= 6
			&& !this.data.isPossiblyTeleporting()
			&& this.isNotGroundBridging()
			&& ((FlyingEvent)packet).hasLooked()
			&& Math.abs(((FlyingEvent)packet).getPitch()) < 90.0F) {
			if (this.data.deltas.deltaPitch < 1.0F && this.data.deltas.deltaYaw > 200.0F
				|| this.data.deltas.deltaPitch > 50.0F && this.data.deltas.deltaYaw < 1.0F
				|| this.data.deltas.deltaPitch < 70.0F
					&& this.data.deltas.deltaPitch > 20.0F
					&& this.data.deltas.deltaYaw < 200.0F
					&& this.data.deltas.deltaYaw > 100.0F
					&& this.data.deltas.deltaXZ > 0.16) {
				if (++this.violations > 5.25) {
					this.fail(
						"* Invalid rotation\n §f* X | Y: §b" + this.data.deltas.deltaYaw + " | " + this.data.deltas.deltaPitch + "\n §f* deltaXZ: §b" + this.data.deltas.deltaXZ,
						this.getBanVL(),
						120L
					);
				}
			} else {
				this.violations = Math.max(this.violations - 0.125, 0.0);
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
