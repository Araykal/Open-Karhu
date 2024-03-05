/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.block;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;

@CheckInfo(
	name = "GhostBreak (A)",
	category = Category.WORLD,
	subCategory = SubCategory.BLOCK,
	experimental = false,
	credits = "§c§lCREDITS: §aIslandscout §7for the check. https://github.com/HawkAnticheat/Hawk"
)
public final class GhostBreak extends PacketCheck {
	public GhostBreak(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
	}
}
