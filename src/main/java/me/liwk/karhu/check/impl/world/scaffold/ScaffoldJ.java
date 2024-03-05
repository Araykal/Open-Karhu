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
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Scaffold (J)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldJ extends PacketCheck {
	public ScaffoldJ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			Vector pos = ((BlockPlaceEvent)packet).getBlockPos();
			if (pos.getX() != -1.0 && (pos.getY() != 255.0 || pos.getY() != -1.0) && pos.getZ() != -1.0 && !this.data.isUsingItem() && this.data.isPlacing()) {
				double ydiff = (double)pos.getBlockY() - this.data.getLocation().y;
				boolean invalidX = pos.getX() == this.data.getLocation().getX();
				boolean invalidY = ydiff > 0.0 && ydiff < 1.0;
				boolean invalidZ = pos.getZ() == this.data.getLocation().getZ();
				if (invalidX && invalidY && invalidZ && !this.data.isOnClimbable() && !this.data.isNearClimbable()) {
					this.fail("* Placing block inside\n §f* blockY: §b" + pos.getY() + "\n §f* playerY: §b" + this.data.getLocation().y, this.getBanVL(), 120L);
				}
			}
		}
	}
}
