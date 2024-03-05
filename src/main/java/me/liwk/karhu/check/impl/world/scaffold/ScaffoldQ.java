/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import java.util.LinkedList;
import java.util.Queue;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;
import org.bukkit.inventory.ItemStack;

@CheckInfo(
	name = "Scaffold (Q)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldQ extends PacketCheck {
	private final Queue<Integer> delays = new LinkedList<>();
	private int movements;

	public ScaffoldQ(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
		this.setSetback(false);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan16()) {
			if (packet instanceof BlockPlaceEvent) {
				if (this.movements != 4 && this.movements < 10) {
					ItemStack item = ((BlockPlaceEvent)packet).getItemStack();
					if (item != null && item.getType().isBlock() && this.delays.add(this.movements) && this.delays.size() == 35) {
						double avg = MathUtil.getAverage(this.delays);
						double stDev = MathUtil.getStandardDeviation(this.delays);
						double cps = 20.0 / avg;
						if (avg < 6.0 && stDev < 0.225) {
							String info = String.format("CPS %.3f AVG %s STD %s", cps, avg, stDev);
							this.fail("* Rightclicker\n" + info, this.getBanVL(), 250L);
						}

						this.delays.clear();
					}
				}

				this.movements = 0;
			} else if (packet instanceof FlyingEvent) {
				++this.movements;
			}
		}
	}
}
