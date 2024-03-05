/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.badpackets;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;

@CheckInfo(
	name = "BadPackets (Q)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsQ extends PacketCheck {
	public BadPacketsQ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent && Karhu.SERVER_VERSION.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
			int face = ((BlockPlaceEvent)packet).getFace();
			double blockX = ((BlockPlaceEvent)packet).getBlockX();
			double blockY = ((BlockPlaceEvent)packet).getBlockY();
			double blockZ = ((BlockPlaceEvent)packet).getBlockZ();
			boolean invalidX = blockX > 1.0 || blockX < 0.0;
			boolean invalidY = blockY > 1.0 || blockY < 0.0;
			boolean invalidZ = blockZ > 1.0 || blockZ < 0.0;
			if (invalidX || invalidY || invalidZ) {
				this.fail("* Invalid blockplace\n §f* FACE: §b" + face + "\n §f* SUM2: §b" + (blockX + blockY + blockZ), this.getBanVL(), 310L);
			}
		}
	}
}
