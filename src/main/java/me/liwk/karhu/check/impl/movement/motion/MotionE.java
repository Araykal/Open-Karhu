/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.motion;

import java.util.ArrayList;
import java.util.List;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PositionCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.VersionBridgeHelper;
import me.liwk.karhu.util.update.MovementUpdate;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CheckInfo(
	name = "Motion (E)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.MOTION,
	experimental = false
)
public final class MotionE extends PositionCheck {
	private double lastDeltaX;
	private double lastDeltaZ;

	public MotionE(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate e) {
		double deltaX = this.data.deltas.deltaX;
		double deltaZ = this.data.deltas.deltaZ;
		double deltaXZ = this.data.deltas.deltaXZ;
		boolean velocity = false;
		if (!e.to.ground) {
			float movementSpeed = 0.0263F;
			List<String> tags = new ArrayList<>();
			tags.add("air");
			if (this.data.elapsed(this.data.getLastSprintTick()) <= 3) {
				tags.add("sprinting");
			}

			if (this.data.getSpeedBoost() > 0) {
				tags.add("speed-effect");
			}

			if (this.data.getSlowness() > 0) {
				tags.add("slowness-effect");
			}

			if (this.data.elapsed(this.data.getLastOnHalfBlock()) <= 3) {
				movementSpeed = (float)((double)movementSpeed + 0.22);
				tags.add("half-block");
			}

			if (this.data.elapsed(this.data.getLastCollidedV()) <= 5) {
				movementSpeed = (float)((double)movementSpeed + 0.18);
				tags.add("under-block");
			}

			float friction = !e.from.ground ? 0.91F : this.data.getCurrentFriction();
			movementSpeed = (float)(
				(double)movementSpeed + (this.data.getSoulSpeedLevel() > 0 ? ((double)((float)this.data.getSoulSpeedLevel() * 0.65F) + 0.5) * (double)movementSpeed : 0.0)
			);
			if (this.data.getSoulSpeedLevel() > 0) {
				tags.add("soulspeed");
			}

			if (this.data.isWasOnWater()) {
				tags.add("water");
				double attributeSpeed = (double)this.data.getWalkSpeed();
				if (this.data.isSprinting()) {
					attributeSpeed *= 1.3F;
				}

				movementSpeed = 0.02F;
				float f3 = (float)this.data.getDepthStriderLevel();
				float f9 = this.data.isSprinting() && this.data.isNewerThan12() ? 0.9F : 0.8F;
				if (f3 > 3.0F) {
					f3 = 3.0F;
				}

				if (!e.from.ground) {
					f3 *= 0.5F;
				}

				if (f3 > 0.0F) {
					f9 += (0.54600006F - f9) * f3 / 3.0F;
					movementSpeed = (float)((double)movementSpeed + (attributeSpeed - (double)movementSpeed) * (double)f3 / 3.0);
				}

				if (this.data.getDolphinLevel() > 0) {
					f9 = 0.96F;
				}

				movementSpeed *= f9;
				movementSpeed = (float)((double)movementSpeed + 0.1);
			}

			if (this.data.elapsed(this.data.getLastInLiquid()) <= 4) {
				movementSpeed = (float)((double)movementSpeed * 2.2);
			}

			if (this.data.getVelocityXZTicks() == 0) {
				velocity = true;
				tags.add("velocity");
			}

			if (this.data.elapsed(this.data.getLastCollidedWithEntity()) <= 10) {
				tags.add("entity-collision");
				movementSpeed = (float)((double)movementSpeed + 0.1);
			}

			double movementSpeedX = (double)movementSpeed + (velocity ? Math.abs(this.data.getVelocityX() + 0.3) : 0.0);
			double movementSpeedZ = (double)movementSpeed + (velocity ? Math.abs(this.data.getVelocityZ() + 0.3) : 0.0);
			if (this.data.getLastAttackTick() <= 1) {
				ItemStack s = this.data.getStackInHand();
				Entity entity = this.data.getLastTarget();
				int i = s.hasItemMeta() && s.getItemMeta().hasEnchant(Enchantment.KNOCKBACK) ? 1 : 0;
				if (this.data.isWasSprinting()) {
					++i;
				}

				if (entity != null) {
					boolean flag5 = !VersionBridgeHelper.isInvulnerable(entity) && entity instanceof Player;
					if (flag5 && i > 0) {
						for (int j = 0; ++j <= this.data.getAttacks(); movementSpeedZ += 0.1) {
							this.lastDeltaX *= 0.6;
							this.lastDeltaZ *= 0.6;
							movementSpeedX += 0.1;
						}
					}
				}
			}

			double predX = velocity ? this.data.getVelocityX() : this.lastDeltaX * (double)friction;
			double predZ = velocity ? this.data.getVelocityZ() : this.lastDeltaZ * (double)friction;
			double diffX = Math.abs(deltaX - predX);
			double diffZ = Math.abs(deltaZ - predZ);
			double combinedPred = MathUtil.hypot(predX, predZ);
			double combinedSpeed = MathUtil.hypot(movementSpeedX, movementSpeedZ);
			double speedup = combinedPred - combinedSpeed;
			if (this.data.elapsed(this.data.getLastFlyTick()) <= 30
				|| !this.data.isRiding()
				|| this.data.isSpectating()
				|| this.data.isPossiblyTeleporting()
				|| this.data.getClientAirTicks() <= 3
				|| this.data.elapsed(this.data.getLastCollided()) <= 2
				|| this.data.elapsed(this.data.getLastSlimePistonPush()) <= 10
				|| this.data.isOnPiston()
				|| this.data.elapsed(this.data.getLastFlyTick()) <= 30
				|| this.data.elapsed(this.data.getLastRiptide()) <= 15
				|| this.data.elapsed(this.data.getLastGlide()) <= 150
				|| this.data.isWasOnLava()
				|| this.data.isOnLava()
				|| this.data.isRiptiding()
				|| this.data.isGliding()
				|| !(deltaXZ > 0.15)) {
				this.violations = Math.max(this.violations - 0.005, 0.0);
			} else if (!(diffX > movementSpeedX * 1.02) && !(diffZ > movementSpeedZ * 1.02)) {
				this.violations = Math.max(this.violations - 0.1525, 0.0);
			} else {
				this.violations += 1.0 + Math.min(1.0, speedup * 1.5);
				if (this.violations > 6.0) {
					this.fail(
						"* Moving faster than possible in air\n §f* speed §b"
							+ this.format(3, Double.valueOf(speedup))
							+ "\n §f* diff §b"
							+ this.format(4, Double.valueOf(diffX - movementSpeedX))
							+ ", "
							+ this.format(4, Double.valueOf(diffZ - movementSpeedX))
							+ "\n §f* mSpeed §b"
							+ movementSpeed
							+ "\n §f* tags §b"
							+ String.join("§f, §b", tags)
							+ "\n §f* tick §b"
							+ this.data.getClientAirTicks()
							+ "\n §f* friction §b"
							+ friction
							+ "/"
							+ this.data.getLastTickFriction()
							+ "\n §f* dXZ §b"
							+ deltaXZ,
						this.getBanVL(),
						300L
					);
				}
			}
		}

		this.lastDeltaX = deltaX;
		this.lastDeltaZ = deltaZ;
	}
}
