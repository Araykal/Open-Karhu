/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.killaura;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

@CheckInfo(
	name = "Killaura (E)",
	category = Category.COMBAT,
	subCategory = SubCategory.KILLAURA,
	experimental = false
)
public final class KillauraE extends PacketCheck {
	private int attacks;

	public KillauraE(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof FlyingEvent) {
			if (this.attacks > 0) {
				if (!this.data.isRiding()
					&& this.data.deltas.deltaXZ > 0.1
					&& this.data.elapsed(this.data.getLastOnIce()) > 2
					&& this.data.getVelocityXZTicks() > 1
					&& this.data.getLastTarget() instanceof Player
					&& !this.data.isSpectating()
					&& this.data.elapsed(this.data.getLastFlyTick()) > 30) {
					double deltaX = this.data.deltas.lastDX *= this.data.isLastLastOnGroundPacket() ? (double)this.data.getLastTickFriction() : 0.91F;
					double deltaZ = this.data.deltas.lastDZ *= this.data.isLastLastOnGroundPacket() ? (double)this.data.getLastTickFriction() : 0.91F;
					deltaX *= 0.6;
					deltaZ *= 0.6;
					double deltaXZ = MathUtil.hypot(deltaX, deltaZ);
					double attackMotion = Math.abs(this.data.deltas.deltaXZ - deltaXZ);
					double acceleration = Math.abs(this.data.deltas.accelXZ);
					double extrabuffer = this.data.isSprinting() ? 1.0 : 0.0;
					double moveSpeed = (double)this.data.getWalkSpeed();
					moveSpeed += (double)(this.data.getWalkSpeed() * 0.3F);
					if (!(attackMotion > moveSpeed) || !(acceleration < 0.005)) {
						this.decrease(0.4);
					} else if ((this.violations += 0.5 + extrabuffer) > 6.0) {
						this.fail(
							"* Invalid motion when attacking\n §f* motion: §b" + attackMotion + "/" + moveSpeed + "\n §f* acceleration: §b" + acceleration + "\n §f* attacks: §b" + this.attacks,
							this.getBanVL(),
							600L
						);
					} else {
						this.debug(
							"* Invalid motion when attacking\n §f* motion: §b" + attackMotion + "/" + moveSpeed + "\n §f* acceleration: §b" + acceleration + "\n §f* attacks: §b" + this.attacks
						);
					}
				} else {
					this.decrease(0.2);
				}
			}

			this.attacks = 0;
		} else if (packet instanceof AttackEvent) {
			if (this.data.getStackInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) < 0 || this.data.getLastTarget() == null) {
				return;
			}

			if (this.data.isWasSprinting() || this.data.getStackInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) > 0) {
				++this.attacks;
			}
		}
	}
}
