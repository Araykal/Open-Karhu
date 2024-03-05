/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.timer;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.PositionEvent;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.task.Tasker;

@CheckInfo(
	name = "Timer (A)",
	category = Category.PACKET,
	subCategory = SubCategory.TIMER,
	experimental = false
)
public final class TimerA extends PacketCheck {
	private long lastFlyingPacket = this.data.getTransactionClock();
	private long balance;
	private boolean capped;
	private static final long TELEPORT_OFFSET = 50000000L;
	private static final long FLYING_OFFSET = 50000000L;

	public TimerA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			if (this.data.getTransactionClock() == 0L && this.lastFlyingPacket == 0L) {
				return;
			}

			if (this.data.getTransactionClock() != 0L && this.lastFlyingPacket == 0L) {
				this.lastFlyingPacket = this.data.getTransactionClock() - 250000000L;
			}

			long capLenght = Karhu.getInstance().getConfigManager().getTimerACapLenght() + MathUtil.toNanos(2000L);
			long now = ((FlyingEvent)packet).getNanoTime();
			long delay = 50000000L - (now - this.lastFlyingPacket);
			long diff = Math.max(50000000L, now - this.lastFlyingPacket);
			this.balance = Math.max(-capLenght, this.balance + delay);
			if (this.balance > 50000000L + MathUtil.toNanos(5L)) {
				if (this.ready()) {
					if (++this.violations > 1.0) {
						if (!this.capped) {
							this.fail(
								"* Timer\n§f* BL §b" + this.balance / 1000000L + "\n§f* RATE §b" + Math.min(50000000L / diff, 10L) + "\n§f* EXISTED §b" + this.data.getTotalTicks(), this.getBanVL(), 120L
							);
						} else {
							this.kickTimer();
						}
					}
				} else {
					this.disallowMove(false);
				}

				this.balance = 0L;
			} else {
				this.violations = Math.max(0.0, this.violations - 0.005);
			}

			if (this.balance <= -capLenght) {
				this.capped = true;
			}

			this.lastFlyingPacket = now;
		} else if (packet instanceof PositionEvent) {
			this.balance -= 50000000L;
		}
	}

	private boolean ready() {
		return (this.data.isHasReceivedTransaction() || this.data.isHasReceivedKeepalive()) && this.data.getTotalTicks() > 100;
	}

	private void kickTimer() {
		if (!this.data.isTimerKicked()) {
			Tasker.run(() -> this.data.getBukkitPlayer().kickPlayer("Timed out (T.A)"));
			this.data.setTimerKicked(true);
		}
	}
}
