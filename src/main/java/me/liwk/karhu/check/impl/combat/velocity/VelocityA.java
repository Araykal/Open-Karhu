/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.velocity;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Velocity (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.VELOCITY,
	experimental = false
)
public final class VelocityA extends PacketCheck {
	private double kbY;
	private double startKbY;
	private int posDesyncStreak;
	private int tick;

	public VelocityA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			Vector tickVel = this.data.getTickedVelocity();
			if (this.data.getTickedVelocity() != null) {
				this.kbY = tickVel.getY();
				this.startKbY = this.kbY;
			}

			this.kbY = Math.abs(this.kbY) < (this.data.isNewerThan8() ? 0.003 : 0.005) ? 0.0 : this.kbY;
			double dClientKb = this.data.deltas.motionY;
			if (this.kbY > 0.0) {
				if (this.data.elapsed(this.data.getLastOnClimbable()) > 3
					&& this.data.elapsed(this.data.getLastInWeb()) > 3
					&& !this.data.isPossiblyTeleporting()
					&& !this.data.isGliding()
					&& !this.data.isJumped()
					&& !this.data.isInsideBlock()
					&& this.data.isInitialized()
					&& this.data.elapsed(this.data.getLastFlyTick()) > 30
					&& this.data.elapsed(this.data.getLastInLiquid()) > 3
					&& !this.data.isRiding()
					&& this.data.elapsed(this.data.getLastPistonPush()) > 3
					&& this.data.elapsed(this.data.getLastOnBoat()) > 1
					&& this.data.elapsed(this.data.getLastOnSlime()) > 10
					&& this.data.elapsed(this.data.getLastInLiquid()) > 10) {
					double tempKbY = this.kbY;
					tempKbY -= 0.08;
					tempKbY *= 0.98F;
					if (Math.abs(dClientKb - tempKbY) < 1.0E-4F) {
						if (this.posDesyncStreak < 10) {
							this.kbY = tempKbY;
						}

						++this.posDesyncStreak;
					} else {
						--this.posDesyncStreak;
					}

					if (this.checkHori() && this.kbY < 0.09 && this.startKbY < 0.4) {
						this.resetState(dClientKb, 2);
						return;
					}

					if (this.kbY < 0.0325) {
						this.resetState(dClientKb, 3);
						return;
					}

					if ((this.data.elapsed(this.data.getLastCollidedV()) < 1 || this.data.elapsed(this.data.getLastCollidedVGhost()) < 1) && this.data.deltas.motionY >= 0.0) {
						this.resetState(dClientKb, 4);
						return;
					}

					if (!this.checkHori() && !this.data.isWasCollidedHorizontally()) {
						if (this.data.elapsed(this.data.getLastOnBoat()) <= 1 && Math.abs(0.6 - dClientKb) < 0.005) {
							this.resetState(dClientKb, 6);
							return;
						}
					} else if (Math.abs(0.5 - dClientKb) < 0.005) {
						this.resetState(dClientKb, 5);
						return;
					}

					double dKb = this.kbY;
					double ptc = dClientKb / dKb * 100.0;
					double diff = Math.abs(dClientKb - dKb);
					double allowance = 5.0E-4;
					if (this.checkHori() && this.data.deltas.deltaXZ <= 0.1 && Math.abs(dKb - dClientKb) <= 0.03125) {
						allowance = 0.05;
					}

					++this.tick;
					double minPtc = this.data.getBukkitPlayer().getMaximumNoDamageTicks() < 5 ? 90.0 : 99.9915;
					double maxPtc = this.data.getBukkitPlayer().getMaximumNoDamageTicks() < 10 ? 600.0 : 101.0;
					double addition = 1.0 + Math.min(2.0, Math.abs((dKb - dClientKb) * 1.5));
					if ((ptc < minPtc || ptc > maxPtc) && diff >= allowance) {
						if ((this.violations += addition) > 3.5) {
							this.fail(
								"* Vertical Modification\n §f* approx pct: §b"
									+ this.format(2, Double.valueOf(ptc))
									+ "\n §f* client: §b"
									+ dClientKb
									+ "\n §f* server: §b"
									+ dKb
									+ "\n §f* tick: §b"
									+ this.tick,
								this.getBanVL(),
								300L
							);
						}

						if (this.violations > 2.0) {
							this.debug(String.format("PTC: %.3f cKB: %.3f", ptc, dClientKb));
						}

						this.resetState(dClientKb, 69);
					} else {
						this.violations = Math.max(this.violations - 0.125, 0.0);
					}

					this.kbY -= 0.08;
					this.kbY *= 0.98F;
					if (this.data.isOnGroundPacket() || this.kbY == 0.0) {
						this.resetState(dClientKb, 420);
					}
				} else {
					this.resetState(dClientKb, 1);
				}
			}
		}
	}

	public boolean checkHori() {
		return this.data.isCollidedHorizontally() || this.data.elapsed(this.data.getLastCollidedGhost()) <= 1;
	}

	private void resetState(double dClientKb, int state) {
		this.kbY = 0.0;
		this.tick = 0;
		this.debugMisc(String.format("RESET cKB: %.3f STATE: %d", dClientKb, state));
	}
}
