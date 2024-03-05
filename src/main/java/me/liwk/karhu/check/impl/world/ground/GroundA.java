/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.ground;

import com.github.retrooper.packetevents.protocol.player.GameMode;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;

@CheckInfo(
	name = "Ground (A)",
	category = Category.WORLD,
	subCategory = SubCategory.NOFALL,
	experimental = false
)
public final class GroundA extends PacketCheck {
	private int noGroundTicks;

	public GroundA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent && this.data.deltas.motionY != 0.0 && this.data.deltas.deltaXZ >= 0.031) {
			if (this.data.elapsed(this.data.getLastFlyTick()) <= 20 || this.data.getGameMode() == GameMode.CREATIVE) {
				return;
			}

			if (!this.data.isOnGroundServer()
				&& !this.data.isWasOnComparator()
				&& !this.data.isOnComparator()
				&& !this.data.isOnLiquid()
				&& !this.data.isPossiblyTeleporting()
				&& !this.data.isOnGhostBlock()
				&& !this.data.isInUnloadedChunk()
				&& !this.data.isOnClimbable()
				&& !this.data.isSpectating()
				&& !this.data.isRiding()
				&& !this.data.isCollidedHorizontally()) {
				if (this.data.isOnGroundPacket() && this.data.elapsed(this.data.getPlaceTicks()) > Math.min(15, MathUtil.getPingInTicks(this.data.getTransactionPing() + 50L) + 6)) {
					++this.noGroundTicks;
				} else {
					this.noGroundTicks = Math.max(this.noGroundTicks - 5, 0);
				}

				if (this.noGroundTicks >= 10) {
					if (++this.violations > 1.0) {
						this.fail("ticks=" + this.noGroundTicks + " server=" + this.data.isOnGroundServer() + " client=" + this.data.isOnGroundPacket(), this.getBanVL(), 100L);
					}
				} else {
					this.violations *= 0.95;
				}
			}
		}
	}
}
