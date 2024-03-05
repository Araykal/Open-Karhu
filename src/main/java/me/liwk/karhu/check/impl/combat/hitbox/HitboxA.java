/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.hitbox;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.mc.MovingObjectPosition;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.mc.vec.Vec3;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Hitbox (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.REACH,
	experimental = false
)
public final class HitboxA extends PacketCheck {
	public HitboxA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!Karhu.getInstance().getConfigManager().isDisableHitboxCheck() && !Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_19) && packet instanceof FlyingEvent) {
			for (int target : this.data.getLastTargets()) {
				if (this.data.getGameMode() != GameMode.CREATIVE
					&& this.data.getClientVersion() != null
					&& !this.data.isRiding()
					&& this.data.getBukkitPlayer().getVehicle() == null
					&& !this.data.isPossiblyTeleporting()
					&& this.data.getLastAttackTick() <= 1) {
					EntityData edata = this.data.getEntityData().get(target);
					if (edata == null) {
						this.data.setReachBypass(true);
						return;
					}

					if (edata.isRiding()) {
						this.data.setReachBypass(true);
						return;
					}

					float sneakAmount1_8 = !this.data.isWasSneaking() && !this.data.isWasWasSneaking() ? (this.data.isGliding() ? 0.4F : (this.data.isRiptiding() ? 0.4F : 1.62F)) : 1.54F;
					float sneakAmount1_13 = !this.data.isWasSneaking() && !this.data.isWasWasSneaking() ? (this.data.isGliding() ? 0.4F : (this.data.isRiptiding() ? 0.4F : 1.62F)) : 1.27F;
					Vec3 eyeLocation = MathUtil.getPositionEyes(this.data.attackerX, this.data.attackerY, this.data.attackerZ, !this.data.isNewerThan12() ? sneakAmount1_8 : sneakAmount1_13);
					Vec3 lookMouseDelayFix;
					Vec3 look;
					if (((FlyingEvent)packet).hasLooked()) {
						lookMouseDelayFix = MathUtil.getVectorForRotation(((FlyingEvent)packet).getPitch(), ((FlyingEvent)packet).getYaw(), this.data);
						look = MathUtil.getVectorForRotation(((FlyingEvent)packet).getPitch(), this.data.attackerYaw, this.data);
					} else {
						lookMouseDelayFix = MathUtil.getVectorForRotation(this.data.attackerPitch, this.data.attackerYaw, this.data);
						look = lookMouseDelayFix;
					}

					Vec3 vec32 = eyeLocation.addVector(look.xCoord * 7.5, look.yCoord * 7.5, look.zCoord * 7.5);
					Vec3 vec322 = eyeLocation.addVector(lookMouseDelayFix.xCoord * 7.5, lookMouseDelayFix.yCoord * 7.5, lookMouseDelayFix.zCoord * 7.5);
					boolean missed = false;
					this.data.setReachBypass(false);
					AxisAlignedBB box = edata.getEntityBoundingBox();
					AxisAlignedBB axisalignedbb = !edata.uncertainBox
						? MathUtil.getHitboxLenient(this.data, box)
						: MathUtil.getHitboxLenient(this.data, box).union(MathUtil.getHitboxLenient(this.data, edata.getEntityBoundingBoxLast()));
					double x = box.getCenterX();
					double z = box.getCenterZ();
					double direction = MathUtil.getDirection(this.data.getLocation(), new Vector(x, 0.0, z));
					double angle = MathUtil.getAngleDistance((double)this.data.getLocation().getYaw(), direction);
					MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(eyeLocation, vec32);
					MovingObjectPosition movingobjectposition2 = axisalignedbb.calculateIntercept(eyeLocation, vec322);
					if (movingobjectposition == null && movingobjectposition2 == null && !axisalignedbb.isVecInside(eyeLocation)) {
						missed = true;
					}

					double dist = this.data.getLastBoundingBox().distance(edata.getEntityBoundingBox());
					double addition = dist > 2.5 ? 1.0 : 0.75;
					if (missed && this.data.getLastTarget().getVehicle() == null) {
						if ((this.violations += addition) > (double)((angle > 20.0 ? 2 : 4) + (dist > 8.0 ? 1 : 2) + (angle < 6.0 ? 2 : 0))) {
							this.fail(
								"* Hit out of the box\n * dist §b"
									+ dist
									+ "\n * angle §b"
									+ angle
									+ "\n * mins §b"
									+ edata.minX
									+ " / "
									+ edata.minY
									+ " / "
									+ edata.minZ
									+ "\n * maxs §b"
									+ edata.maxX
									+ " / "
									+ edata.maxY
									+ " / "
									+ edata.maxZ
									+ "\n * locations §b"
									+ edata.newLocations.size()
									+ "\n * existed §b"
									+ edata.getExist()
									+ "\n §f* DEV DATA: §b"
									+ edata.posIncrements,
								this.getBanVL(),
								300L
							);
						}

						this.debug(String.format("A: %.3f D: %.3f B: %d", angle, dist, this.violations));
						this.data.setCancelNextHitH(true);
					} else {
						this.violations = Math.max(this.violations - 0.215, 0.0);
						this.data.setCancelNextHitH(false);
					}
				}
			}
		}
	}
}
