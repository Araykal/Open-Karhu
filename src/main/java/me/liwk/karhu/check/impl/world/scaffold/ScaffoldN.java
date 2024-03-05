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
		name = "Scaffold (N)",
		category = Category.WORLD,
		subCategory = SubCategory.SCAFFOLD,
		experimental = true,
		credits = "§c§lCREDITS: §afrogsmasha §7for the base idea."
)
public final class ScaffoldN extends PacketCheck {
	public ScaffoldN(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			Vector pos = ((BlockPlaceEvent)packet).getBlockPos();
			if (pos.getX() != -1.0 && (pos.getY() != 255.0 || pos.getY() != -1.0) && pos.getZ() != -1.0) {
				int face = ((BlockPlaceEvent)packet).getFace();
				if (face < 0 || face > 6) {
					this.fail("* Impossible block face\n §f* face: §b" + face, this.getBanVL(), 120L);
				}
			} else if (pos.getX() == -1.0 && (pos.getY() == 255.0 || pos.getY() == -1.0) && pos.getZ() == -1.0) {
				int face = ((BlockPlaceEvent)packet).getFace();
				if (face >= 0 && face <= 6) {
					this.fail("* Impossible block face\n §f* face: §b" + face, this.getBanVL(), 120L);
				}
			}
		}

	}
}