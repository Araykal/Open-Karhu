/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.aimassist.analysis;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.RotationCheck;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.mc.MathHelper;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Analysis (C)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = true
)
public class AnalysisC extends RotationCheck {
	private final Deque<Float> pitchMatchList = new LinkedList<>();
	private final Deque<Float> yawMatchList = new LinkedList<>();

	public AnalysisC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		if (this.data.getLastAttackTick() <= 1 && this.data.getLastTarget() != null && this.data.deltas.deltaXZ > 0.1) {
			EntityData edata = this.data.getEntityData().get(this.data.getLastTarget().getEntityId());
			if (edata != null) {
				AxisAlignedBB entityBB = edata.getEntityBoundingBox();
				float deltaYaw = this.data.deltas.deltaYaw;
				float deltaPitch = this.data.deltas.deltaPitch;
				float[] rotationBasic = this.getRotations(update.from, entityBB);
				if (deltaYaw > 0.0F) {
					float delta = MathUtil.getAngleDistance(rotationBasic[0], update.to.yaw);
					this.yawMatchList.add(delta);
				}

				if (deltaPitch > 0.0F) {
					float delta = MathUtil.getAngleDistance(rotationBasic[1], update.to.pitch);
					this.pitchMatchList.add(delta);
				}

				if (this.yawMatchList.size() == 100) {
					Deque<Float> closes = this.yawMatchList.stream().filter(delta -> delta <= 1.5F).collect(Collectors.toCollection(LinkedList::new));
					int matches = closes.size();
					if (matches >= 70) {
						if (++this.violations > 1.0) {
							double average = MathUtil.getAverage(closes);
							this.fail("* Rotation analysis (generic, yaw)\n §f* avg: §b" + average + "\n §f* rate: §b" + matches, this.getBanVL(), 300L);
						}
					} else {
						this.decrease(0.05);
					}

					this.yawMatchList.clear();
				}

				if (this.pitchMatchList.size() == 100) {
					Deque<Float> closes = this.pitchMatchList.stream().filter(delta -> delta <= 1.5F).collect(Collectors.toCollection(LinkedList::new));
					int matches = closes.size();
					if (matches >= 70) {
						if (++this.violations > 1.0) {
							double average = MathUtil.getAverage(closes);
							this.fail("* Rotation analysis (generic, pitch)\n §f* avg: §b" + average + "\n §f* rate: §b" + matches, this.getBanVL(), 300L);
						}
					} else {
						this.decrease(0.05);
					}

					this.pitchMatchList.clear();
				}
			}
		}
	}

	private float[] getRotations(CustomLocation playerLocation, AxisAlignedBB aabb) {
		double diffX = aabb.getCenterX() + 0.1 - playerLocation.x;
		double diffY = aabb.minY - 2.2 + 1.62 - playerLocation.y;
		double diffZ = aabb.getCenterZ() + 0.1 - playerLocation.z;
		double dist = (double)MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0F;
		float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
		double pitchToEntity = -Math.toDegrees(Math.atan(diffY / dist));
		pitch += -MathHelper.wrapAngleTo180_float(pitch - (float)pitchToEntity) - 2.5F;
		return new float[]{yaw, pitch};
	}
}
