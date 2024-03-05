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
import me.liwk.karhu.util.player.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Scaffold (L)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true
)
public final class ScaffoldL extends PacketCheck {
	private int movements;
	private int jumps;
	private int noJump;
	private int sameYStreak;
	private double lastY;
	private boolean sameY;
	private boolean placed;

	public ScaffoldL(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan16()) {
			if (packet instanceof BlockPlaceEvent) {
				Vector pos = ((BlockPlaceEvent)packet).getBlockPos();
				if (pos.getX() != -1.0 && pos.getY() != 255.0 && pos.getY() != -1.0 && pos.getZ() != -1.0 && this.movements <= 10) {
					ItemStack item = ((BlockPlaceEvent)packet).getItemStack();
					if (item != null && item.getType().isBlock()) {
						Vector location = this.data.getLocation().toVector();
						Vector blockLocation = ((BlockPlaceEvent)packet).getBlockPos();
						if (location.distance(blockLocation) <= 2.0 && this.isNotGroundBridging() && this.isNotGroundBridging2() && location.getY() > blockLocation.getY()) {
							this.sameY = this.lastY == blockLocation.getY();
							this.lastY = blockLocation.getY();
							this.placed = true;
						}
					}
				}

				this.movements = 0;
			} else if (packet instanceof FlyingEvent) {
				++this.movements;
				if (this.placed) {
					boolean eligible = this.data.deltas.deltaXZ > (double)PlayerUtil.getBaseSpeedAttribute(this.data, 1.8F) && this.data.elapsed(this.data.getLastVelocityTaken()) > 5;
					if (eligible) {
						if (!this.sameY) {
							this.sameYStreak = 0;
						}

						if (this.data.isJumped() || this.data.isJumpedLastTick() || !this.data.isOnGroundPacket() && this.sameY) {
							if (this.sameY) {
								++this.sameYStreak;
							}

							++this.jumps;
						} else {
							++this.noJump;
						}
					}
				}

				if (this.jumps > 10 && this.noJump >= 0) {
					if (this.jumps > this.noJump && this.sameYStreak > 2) {
						String info = String.format("J %s, NJ %s SY %s", this.jumps, this.noJump, this.sameYStreak);
						this.fail("* Scaffold pattern\n" + info, this.getBanVL(), 250L);
					}

					this.sameYStreak = this.jumps = this.noJump = 0;
				}

				this.placed = false;
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
	@SneakyThrows
	public boolean isNotGroundBridging2() {
		Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(this.data.getLocation().clone().subtract(0.0, 3.0, 0.0).toLocation(this.data.getWorld()));
		if (block == null) {
			return false;
		} else {
			return block.getType() == Material.AIR;
		}
	}
}
