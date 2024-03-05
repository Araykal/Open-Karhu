/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.combat.aimassist.analysis;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.AttackEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.FlyingEvent;
import me.liwk.karhu.event.SwingEvent;
import me.liwk.karhu.util.MathUtil;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Analysis (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false
)
public final class AnalysisA extends PacketCheck {
	private int attacks;
	private int swings;
	private double nearCenterHits;
	private double lastX;
	private double lastZ;

	public AnalysisA(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof AttackEvent) {
			++this.attacks;
		} else if (packet instanceof SwingEvent) {
			if (++this.swings == 100) {
				double ratio = (double)this.attacks / (double)this.swings;
				double requiredAim = (double)this.attacks / 2.5;
				if (ratio >= 0.75 && this.nearCenterHits >= requiredAim) {
					this.fail(
						"* Combat analysis\n§f* ratio: §b" + this.format(3, Double.valueOf(ratio)) + "\n§f* a/s: §b" + this.attacks + "/" + this.swings + "\n§f* nch: §b" + this.nearCenterHits,
						this.getBanVL(),
						300L
					);
				}

				this.attacks = this.swings = 0;
				this.nearCenterHits = 0.0;
			}
		} else if (packet instanceof FlyingEvent && this.data.getLastAttackTick() <= 1 && this.data.getLastTarget() != null) {
			EntityData edata = this.data.getEntityData().get(this.data.getLastTarget().getEntityId());
			if (edata == null) {
				return;
			}

			double x = edata.getEntityBoundingBox().getCenterX();
			double z = edata.getEntityBoundingBox().getCenterZ();
			double direction = MathUtil.getDirection(this.data.getLastLocation(), new Vector(x, 0.0, z));
			double angleNormal = MathUtil.getAngleDistance((double)this.data.getLastLocation().getYaw(), direction);
			double angleMDFix = MathUtil.getAngleDistance((double)this.data.getLocation().getYaw(), direction);
			double angle = Math.min(angleNormal, angleMDFix);
			double distance = this.data.getBoundingBox().distance(x, z);
			boolean movement = this.data.deltas.deltaXZ >= 0.08
				&& this.data.deltas.deltaYaw >= 1.5F
				&& this.data.elapsed(this.data.getLastVelocityTaken()) <= 500
				&& Math.abs(x - this.lastX) >= 0.0325
				&& Math.abs(z - this.lastZ) >= 0.0325;
			if (angle <= 4.0 * Math.max(2.0, distance) && movement) {
				if (distance >= 0.4) {
					++this.nearCenterHits;
				} else {
					this.nearCenterHits += 0.55;
				}
			}

			this.lastX = x;
			this.lastZ = z;
		}
	}
}
