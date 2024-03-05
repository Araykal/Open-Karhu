/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import java.util.LinkedList;
import java.util.Queue;

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
import me.liwk.karhu.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@CheckInfo(
	name = "Scaffold (P)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false
)
public final class ScaffoldP extends PacketCheck {
	private final Queue<Integer> delays = new LinkedList<>();
	public int placed;
	public int movements;

	public ScaffoldP(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof BlockPlaceEvent) {
			BlockPlaceEvent place = (BlockPlaceEvent)packet;
			int face = place.getFace();
			Location blockPos = place.get420Johannes();
			ItemStack stack = place.getItemStack() == null ? new ItemStack(Material.AIR) : place.getItemStack();
			boolean validPlace = Math.abs(blockPos.getX() - this.data.getLocation().x) <= 1.5
				&& Math.abs(blockPos.getY() - this.data.getLocation().y) <= 2.5
				&& Math.abs(blockPos.getZ() - this.data.getLocation().z) <= 1.5;
			boolean canProcess = !this.data.isCollidedHorizontally()
				&& !place.isUsableItem()
				&& this.data.elapsed(this.data.getLastFlyTick()) > 10
				&& stack.getType().isSolid()
				&& stack.getType().isBlock();
			double offsetH = this.data.deltas.deltaXZ;
			double lastOffsetH = this.data.deltas.lastDXZ;
			if (this.movements < 10 && this.delays.add(this.movements) && this.delays.size() >= 50 && this.placed <= 2) {
				this.delays.clear();
			}

			if (face >= 0 && face <= 6 && validPlace) {
				if (offsetH > (this.data.elapsed(this.data.getLastSneakTick()) <= 10 ? 0.16 : 0.12) && canProcess) {
					Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(blockPos);
					if (block != null) {
						boolean additionable = block.getType() == stack.getType() && this.isNotGroundBridging();
						if (additionable && ++this.placed >= 10) {
							double avg = MathUtil.getAverage(this.delays);
							double cps = 20.0 / avg;
							this.fail(
								"* Impossible bridging\n §f* balance: §b"
									+ this.placed
									+ "\n §f* oH | lOH: §b"
									+ this.format(3, Double.valueOf(offsetH))
									+ " | "
									+ this.format(3, Double.valueOf(lastOffsetH))
									+ "\n §f* cps: §b"
									+ this.format(3, Double.valueOf(cps))
									+ "\n §f* avg: §b"
									+ this.format(3, Double.valueOf(avg)),
								this.getBanVL(),
								120L
							);
							this.delays.clear();
							this.placed = 2;
						}
					}
				}
			} else {
				this.placed = Math.max(this.placed - 2, -5);
			}

			this.movements = 0;
		} else if (packet instanceof FlyingEvent) {
			++this.movements;
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
