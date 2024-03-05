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
	name = "Ground (C)",
	category = Category.WORLD,
	subCategory = SubCategory.NOFALL,
	experimental = false
)
public final class GroundC extends PacketCheck {
	public GroundC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			double groundDiff = Math.abs(this.data.getLocation().y) % 0.015625;
			if (this.data.elapsed(this.data.getLastFlyTick()) <= 20 || this.data.getGameMode() == GameMode.CREATIVE) {
				return;
			}

			if (!this.data.isOnClimbable()
				&& !this.data.isOnSlab()
				&& !this.data.isInsideBlock()
				&& !this.data.isOnStairs()
				&& !this.data.isOnBoat()
				&& !this.data.isInUnloadedChunk()
				&& this.data.elapsed(this.data.getLastPossibleInUnloadedChunk()) > 1
				&& !this.data.isWasInUnloadedChunk()
				&& !this.data.isPossiblyTeleporting()
				&& !this.data.isOnSlime()
				&& !this.data.isGroundNearBox()
				&& this.data.getPositionPackets() > 60
				&& !this.data.isRiding()
				&& !this.data.isWasOnSlime()
				&& this.data.elapsed(this.data.getLastInLiquid()) > 2
				&& !this.data.isInWeb()
				&& this.data.getLevitationLevel() == 0
				&& this.data.elapsed(this.data.getPredictionTicks()) >= 1
				&& !this.data.isWasInWeb()
				&& !this.data.isSpectating()
				&& this.data.elapsed(this.data.getPlaceTicks()) > Math.min(15, MathUtil.getPingInTicks(this.data.getTransactionPing() + 50L) + 5)) {
				double MAX = this.data.getClientAirTicks() < this.data.getAirTicks() / 3 ? 1.0 : 2.0;
				boolean groundServer = groundDiff == 0.0;
				boolean clientCollide = ((FlyingEvent)packet).isOnGround();
				if (clientCollide != groundServer && this.data.getAirTicks() > 2) {
					if (this.violations++ > MAX) {
						this.fail(
							"* Spoofed ground status\n §f* ST/CT: §b"
								+ this.data.getAirTicks()
								+ " | "
								+ this.data.getClientAirTicks()
								+ "\n §f* MG/CG: §b"
								+ groundServer
								+ " | "
								+ clientCollide
								+ "\n §f* difference: §b"
								+ groundDiff,
							this.getBanVL(),
							50L
						);
					}
				} else {
					this.violations = Math.max(this.violations - 0.045, 0.0);
				}
			}
		}
	}
}
