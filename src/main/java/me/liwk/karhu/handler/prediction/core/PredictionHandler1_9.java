/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.prediction.core;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.handler.collision.BlockCollisionHandler;
import me.liwk.karhu.handler.collision.enums.CollisionType;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.handler.interfaces.AbstractPredictionHandler;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.VersionBridgeHelper;
import me.liwk.karhu.util.mc.MathHelper;
import me.liwk.karhu.util.mc.vec.Vec3d;
import me.liwk.karhu.util.player.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class PredictionHandler1_9 extends AbstractPredictionHandler {
	private boolean lock;
	private boolean onGround;
	private int jumpTicks;
	private boolean allowedJump;
	private float stepHeight = 0.6F;

	public PredictionHandler1_9(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void moveEntityWithHeading() {
		this.computeKeys();
		this.boundingBox = this.data.getBoundingBox().toBB();
		this.motX = this.data.deltas.lastDX;
		this.motZ = this.data.deltas.lastDZ;
		this.motY = this.data.deltas.lastMotionY;
		if (this.jumpTicks > 0) {
			--this.jumpTicks;
		}

		if (Math.abs(this.motX) < 0.003) {
			this.motX = 0.0;
		}

		if (Math.abs(this.motY) < 0.003) {
			this.motY = 0.0;
		}

		if (Math.abs(this.motZ) < 0.003) {
			this.motZ = 0.0;
		}

		ItemStack s = VersionBridgeHelper.getStackInHand(this.data);
		boolean flag = this.data.lastAttackTick <= 1 && this.data.isSprinting() || s != null && s.hasItemMeta() && s.getItemMeta().hasEnchant(Enchantment.KNOCKBACK);
		if (this.data.isJumpedCurrentTick()) {
			if (this.data.isLastOnGroundPacket() && this.jumpTicks == 0) {
				this.jump();
				this.jumpTicks = 10;
				this.allowedJump = true;
			} else {
				this.allowedJump = false;
			}
		} else {
			this.jumpTicks = 0;
		}

		if (flag) {
			this.motX *= 0.6;
			this.motZ *= 0.6;
		}

		boolean flagSlow = this.motY <= 0.0;
		if (flagSlow && this.data.getSlowFallingLevel() > 0) {
			this.motY = 0.01;
		}

		if (!this.data.isOnWater()) {
			if (!this.data.isOnLava()) {
				if (this.data.isGliding()) {
					Vec3d vec3d = MathUtil.getLook3d(1.0F, this.data);
					float f = this.data.getLocation().pitch * (float) (Math.PI / 180.0);
					double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
					double d8 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
					double d1 = vec3d.lengthVector();
					float f4 = MathHelper.cos(f);
					f4 = (float)((double)f4 * (double)f4 * Math.min(1.0, d1 / 0.4));
					this.motY += -0.08 + (double)f4 * 0.06;
					if (this.motY < 0.0 && d6 > 0.0) {
						double d2 = this.motY * -0.1 * (double)f4;
						this.motY += d2;
						this.motX += vec3d.x * d2 / d6;
						this.motZ += vec3d.z * d2 / d6;
					}

					if (f < 0.0F) {
						double d10 = d8 * (double)(-MathHelper.sin(f)) * 0.04;
						this.motY += d10 * 3.2;
						this.motX -= vec3d.x * d10 / d6;
						this.motZ -= vec3d.z * d10 / d6;
					}

					if (d6 > 0.0) {
						this.motX += (vec3d.x / d6 * d8 - this.motX) * 0.1;
						this.motZ += (vec3d.z / d6 * d8 - this.motZ) * 0.1;
					}

					this.motX *= 0.99F;
					this.motY *= 0.98F;
					this.motZ *= 0.99F;
					this.moveEntity(this.motX, this.motY, this.motZ);
				} else {
					float f6 = this.data.getCurrentFriction();
					float f7 = 0.16277136F / (f6 * f6 * f6);
					double f8 = this.data.isLastOnGroundPacket() ? (double)(this.data.getLastAttributeSpeed() * f7) : (this.data.isWasSprinting() ? 0.025999999F : 0.02F);
					this.moveFlying(this.moveStrafe, this.moveForward, (float)f8);
					f6 = this.data.getCurrentFriction();
					if (this.data.isOnClimbable()) {
						float f9 = 0.15F;
						this.motX = MathHelper.clamp_double(this.motX, (double)(-f9), (double)f9);
						this.motZ = MathHelper.clamp_double(this.motZ, (double)(-f9), (double)f9);
						if (this.motY < -0.15) {
							this.motY = -0.15;
						}

						if (this.data.isSneaking() && this.motY < 0.0) {
							this.motY = 0.0;
						}
					}

					this.moveEntity(this.motX, this.motY, this.motZ);
					if (this.data.getLevitationLevel() > 0) {
						this.motY += (0.05 * (double)this.data.getLevitationLevel() - this.motY) * 0.2;
					} else if (!this.data.isInUnloadedChunk()) {
						if (this.hasNoGravity()) {
							this.motY -= 0.08;
						}
					} else {
						this.motY = this.data.getLocation().y > 0.0 ? -0.1 : 0.0;
					}

					this.motY *= 0.98F;
					this.motX *= (double)f6;
					this.motZ *= (double)f6;
				}
			} else {
				double d4 = this.data.getLocation().y;
				this.moveFlying(this.moveStrafe, this.moveForward, 0.02F);
				this.moveEntity(this.motX, this.motY, this.motZ);
				this.motX *= 0.5;
				this.motY *= 0.5;
				this.motZ *= 0.5;
				if (this.hasNoGravity()) {
					this.motY -= 0.02;
				}
			}
		} else {
			float f1 = this.getWaterSlowDown();
			float f2 = 0.02F;
			float f3 = 0.0F;
			boolean checkDepthStrider = Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_7_10) && this.data.getBukkitPlayer().getInventory().getBoots() != null;
			if (checkDepthStrider) {
				f3 = (float)this.data.getBukkitPlayer().getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
			}

			if (f3 > 3.0F) {
				f3 = 3.0F;
			}

			if (!this.data.isLastOnGroundPacket()) {
				f3 *= 0.5F;
			}

			if (f3 > 0.0F) {
				f1 += (0.54600006F - f1) * f3 / 3.0F;
				f2 += (this.data.getLastAttributeSpeed() - f2) * f3 / 3.0F;
			}

			this.moveFlying(this.moveStrafe, this.moveForward, f2);
			this.moveEntity(this.motX, this.motY, this.motZ);
			this.motX *= (double)f1;
			this.motY *= 0.8F;
			this.motZ *= (double)f1;
			if (this.hasNoGravity()) {
				this.motY -= 0.02;
			}
		}

		this.nextMotX = this.motX;
		this.nextMotY = this.motY;
		this.nextMotZ = this.motZ;
		this.onGround = this.data.isOnGroundPacket();
	}

	private boolean hasNoGravity() {
		return VersionBridgeHelper.hasGravity(this.data.getBukkitPlayer());
	}

	private float getWaterSlowDown() {
		return 0.8F;
	}

	public void onFireworkUse() {
	}

	@Override
	public void moveFlying(float strafe, float forward, float friction) {
		float f = strafe * strafe + forward * forward;
		if (f >= 1.0E-4F) {
			if ((f = MathHelper.sqrt((double)f)) < 1.0F) {
				f = 1.0F;
			}

			f = friction / f;
			float f1 = MathHelper.sin(this.data.getLocation().yaw * (float) (Math.PI / 180.0));
			float f2 = MathHelper.cos(this.data.getLocation().yaw * (float) (Math.PI / 180.0));
			float var7;
			float var8;
			this.motX += (double)((var7 = strafe * f) * f2 - (var8 = forward * f) * f1);
			this.motY += 0.0;
			this.motZ += (double)(var8 * f2 + var7 * f1);
		}
	}

	@Override
	public void moveEntity(double x, double y, double z) {
		if (this.data.isInWeb()) {
			x *= 0.25;
			y *= 0.05F;
			z *= 0.25;
			this.motX = 0.0;
			this.motY = 0.0;
			this.motZ = 0.0;
		}

		double d2 = x;
		double d4 = z;
		if (this.data.isLastOnGroundPacket() && this.data.isSneaking()) {
			for (double d5 = 0.05;
				x != 0.0
					&& this.karhu.getNmsWorldProvider().getCollidingBoundingBoxes(this.data.getBukkitPlayer(), this.getEntityBoundingBox().offset(x, (double)(-this.stepHeight), 0.0)).isEmpty();
				d2 = x
			) {
				double var20;
				double var21;
				x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (var20 = x - 0.05) : (var21 = x + 0.05));
			}

			while (
				z != 0.0
					&& this.karhu.getNmsWorldProvider().getCollidingBoundingBoxes(this.data.getBukkitPlayer(), this.getEntityBoundingBox().offset(0.0, (double)(-this.stepHeight), z)).isEmpty()
			) {
				double var24;
				double var25;
				z = z < 0.05 && z >= -0.05 ? 0.0 : (z > 0.0 ? (var24 = z - 0.05) : (var25 = z + 0.05));
				d4 = z;
			}

			while (
				x != 0.0
					&& z != 0.0
					&& this.karhu.getNmsWorldProvider().getCollidingBoundingBoxes(this.data.getBukkitPlayer(), this.getEntityBoundingBox().offset(x, (double)(-this.stepHeight), z)).isEmpty()
			) {
				double var22;
				double var23;
				x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (var22 = x - 0.05) : (var23 = x + 0.05));
				d2 = x;
				double var26;
				double var27;
				z = z < 0.05 && z >= -0.05 ? 0.0 : (z > 0.0 ? (var26 = z - 0.05) : (var27 = z + 0.05));
				d4 = z;
			}
		}

		boolean flag = this.data.isLastOnGroundPacket() || y != y && y < 0.0;
		int j6 = MathHelper.floor_double(this.data.getLocation().x);
		int i1 = MathHelper.floor_double(this.data.getLocation().y - 0.2F);
		int k6 = MathHelper.floor_double(this.data.getLocation().z);
		Block block1 = null;
		Location location = new Location(this.data.getWorld(), (double)j6, (double)i1, (double)k6);
		if (BlockUtil.chunkLoaded(location)) {
			block1 = location.getBlock();
			Block block;
			if (location.getBlock().getType() == Material.AIR && MaterialChecks.FENCES.contains((block = location.subtract(0.0, 1.0, 0.0).getBlock()).getType())) {
				block1 = block;
			}
		}

		if (d2 != x) {
			this.motX = 0.0;
		}

		if (d4 != z) {
			this.motZ = 0.0;
		}

		if (y != y && block1 != null) {
			BlockCollisionHandler.run(block1, CollisionType.LANDED, this);
		}

		if (!this.data.getBukkitPlayer().isFlying() && !flag && this.data.getBukkitPlayer().getVehicle() == null) {
			if (this.data.isLastOnGroundPacket() && block1 != null) {
				BlockCollisionHandler.run(block1, CollisionType.WALKING, this);
			}

			this.doBlockCollisions();
		}
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public boolean isLock() {
		return this.lock;
	}

	public boolean isOnGround() {
		return this.onGround;
	}

	public int getJumpTicks() {
		return this.jumpTicks;
	}

	public boolean isAllowedJump() {
		return this.allowedJump;
	}
}
