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
	name = "BlockReach (A)",
	category = Category.WORLD,
	subCategory = SubCategory.BLOCK,
	experimental = true
)
public final class BlockReach extends PacketCheck {
	public BlockReach(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
	}
}
