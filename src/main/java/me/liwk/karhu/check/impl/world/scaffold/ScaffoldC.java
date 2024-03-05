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
import me.liwk.karhu.event.FlyingEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@CheckInfo(
	name = "Scaffold (C)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false
)
public final class ScaffoldC extends PacketCheck {
	private int delay;
	private int lastDelay;
	private int susClicks;
	private int clicks;

	public ScaffoldC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
		this.setSetback(false);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			BlockPlaceEvent place = (BlockPlaceEvent)packet;
			if (place.isUsableItem() || this.data.isRiding()) {
				return;
			}

			Location blockPos = place.get420Johannes();
			Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(blockPos);
			if (block != null) {
				ItemStack stack = place.getItemStack() == null ? new ItemStack(Material.AIR) : place.getItemStack();
				boolean additionable = block.getType() == stack.getType();
				if (additionable && this.delay <= 8) {
					if (this.delay == this.lastDelay) {
						++this.susClicks;
					}

					if (++this.clicks == 100) {
						if (this.susClicks > 70) {
							this.fail("* Scaffold like click pattern\n§f* SU §b" + this.susClicks, this.getBanVL(), 250L);
						}

						this.susClicks = 0;
						this.clicks = 0;
					}
				}
			}

			this.lastDelay = this.delay;
			this.delay = 0;
		} else if (packet instanceof FlyingEvent) {
			++this.delay;
		}
	}
}
