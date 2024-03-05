/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import java.util.ArrayDeque;
import java.util.Deque;

import lombok.SneakyThrows;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.ActionEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.bukkit.Material;
import org.bukkit.block.Block;

@CheckInfo(
	name = "Scaffold (O)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldO extends PacketCheck {
	Deque<Integer> interactions = new ArrayDeque<>();
	int flying;

	public ScaffoldO(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			if (((FlyingEvent)packet).hasMoved() || ((FlyingEvent)packet).hasLooked()) {
				++this.flying;
			}
		} else if (packet instanceof ActionEvent) {
			if (((ActionEvent)packet).getAction() != Action.STOP_SNEAKING) {
				return;
			}

			if (this.data.elapsed(this.data.getUnderPlaceTicks()) > 3 || !this.isNotGroundBridging()) {
				return;
			}

			if (this.interactions.add(this.flying) && this.interactions.size() >= 15) {
				double std = new StandardDeviation().evaluate(MathUtil.dequeTranslator(this.interactions));
				if (std < 0.325) {
					this.fail("* Eagle\n" + String.format("std: %.2f", std), this.getBanVL(), 125L);
				} else if (std < 0.65) {
					if (++this.violations > 2.0) {
						this.fail("* Eagle\n" + String.format("std: %.2f", std), this.getBanVL(), 125L);
					}
				} else {
					this.violations = Math.max(this.violations - 0.35, 0.0);
				}

				this.interactions.clear();
			}

			this.flying = 0;
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
