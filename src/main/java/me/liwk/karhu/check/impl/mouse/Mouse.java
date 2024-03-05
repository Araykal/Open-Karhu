/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.mouse;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.RotationCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.update.MovementUpdate;

@CheckInfo(
	name = "Cinematic (A)",
	category = Category.COMBAT,
	subCategory = SubCategory.AIM,
	experimental = false,
	silent = true
)
public final class Mouse extends RotationCheck {
	private double deltaX;
	private double deltaY;
	private double mouseX;
	private double mouseY;
	private float lastPitch;
	private float lastPitchAccel;
	private float lastYaw;
	private float lastYawAccel;
	private int ticks;

	public Mouse(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(MovementUpdate update) {
		CustomLocation to = update.getTo();
		CustomLocation from = update.getFrom();
		this.deltaX = (double)Math.abs(to.getYaw() - from.getYaw());
		this.deltaY = (double)Math.abs(to.getPitch() - from.getPitch());
		double deltaYAccel = Math.abs(this.deltaY - (double)this.lastPitch);
		double deltaXAccel = Math.abs(this.deltaX - (double)this.lastYaw);
		this.mouseX = (double)from.getYaw();
		this.mouseY = (double)from.getPitch();
		float f = (float)this.data.getSensitivity() * 0.6F + 0.2F;
		float f1 = f * f * f * 8.0F;
		float f2 = (float)this.mouseX * f1;
		float f3 = (float)this.mouseY * f1;
		byte b0 = 1;
		float[] angles = this.getAngles(f2, f3 * (float)b0);
		this.data.setPredictYaw(angles[0]);
		this.data.setPredictPitch(angles[1]);
		if (!this.isNearlySame(this.deltaY, (double)this.lastPitch)
			&& !this.isNearlySame(deltaYAccel, (double)this.lastPitchAccel)
			&& !this.isNearlySame(this.deltaX, deltaXAccel)
			&& !this.isNearlySame(deltaXAccel, (double)this.lastYawAccel)) {
			this.ticks = Math.max(this.ticks - 1, 0);
			if (this.ticks <= 1) {
				this.data.setCinematic(false);
			}
		} else {
			this.ticks = Math.min(80, this.ticks + 1);
			if (this.ticks >= 3) {
				this.data.setCinematic(true);
				this.data.setLastCinematic(this.data.getTotalTicks());
			}
		}

		this.lastPitch = (float)this.deltaY;
		this.lastPitchAccel = (float)deltaYAccel;
		this.lastYaw = (float)this.deltaX;
		this.lastYawAccel = (float)deltaXAccel;
	}

	public boolean isNearlySame(double d1, double d2) {
		double max = this.data.getSensitivity() >= 100
			? 0.0425 * (double)this.data.getSensitivityY() * 3.1
			: (this.data.getSensitivity() >= 160 ? 0.07 * (double)this.data.getSensitivityY() * 3.2 : 0.0325);
		if (this.data.getSensitivity() >= 160 && Math.abs(d1 - d2) > 1.0 && Math.abs(d1 - d2) < 8.0) {
			return true;
		} else {
			return Math.abs(d1 - d2) < max && Math.abs(d1 - d2) > 0.0015;
		}
	}

	public float[] getAngles(float yaw, float pitch) {
		float yaw2 = (float)this.mouseX;
		float pitch2 = (float)this.mouseY;
		yaw2 = (float)((double)yaw2 + (double)yaw * 0.15);
		pitch2 = (float)((double)pitch2 - (double)pitch * 0.15);
		return new float[]{yaw2, pitch2};
	}
}
