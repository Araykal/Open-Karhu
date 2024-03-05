/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global;

import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.MathUtil;

public final class VelocityHandler {
	public static void handle(KarhuPlayer data) {
		double check = data.getClientVersion() != null && data.isNewerThan8() ? 0.003 : 0.005;
		if (data.isTakingVertical()) {
			double velocityNew = data.getVelocityY();
			if (Math.abs(data.deltas.motionY - velocityNew) > 0.001 + (data.elapsed(data.getPredictionTicks()) < 2 ? 0.0309 : 0.0)) {
				velocityNew -= 0.08;
				velocityNew *= 0.98F;
			}

			data.setVelocityY(velocityNew);
			if (Math.abs(velocityNew) < check || data.isOnGroundPacket() || data.getVelocityYTicks() >= data.getMaxVelocityYTicks() * 2) {
				data.setVelocityY(0.0);
				data.setConfirmingY(0.0);
				data.setLastVelocityYReset(data.getTotalTicks());
				data.setTakingVertical(false);
			}
		}

		if (data.getVelocityHorizontal() > 0.0) {
			if (data.lastAttackTick <= 1) {
				data.setVelocityX(data.getVelocityX() * 0.6);
				data.setVelocityZ(data.getVelocityZ() * 0.6);
			}

			float f4 = 0.91F;
			if (data.isLastOnGroundPacket()) {
				f4 = data.getCurrentFriction();
			}

			if (Math.abs(data.deltas.deltaXZ - data.getVelocityHorizontal()) > 0.001 + (data.elapsed(data.getPredictionTicks()) < 2 ? 0.0309 : 0.0)) {
				data.setVelocityX(data.getVelocityX() * (double)f4);
				data.setVelocityZ(data.getVelocityZ() * (double)f4);
				if (Math.abs(data.getVelocityX()) < check) {
					data.setVelocityX(0.0);
					data.setLastVelocityXZReset(data.getTotalTicks());
				}

				if (Math.abs(data.getVelocityZ()) < check) {
					data.setVelocityZ(0.0);
					data.setLastVelocityXZReset(data.getTotalTicks());
				}

				if (data.getVelocityXZTicks() >= data.getMaxVelocityXZTicks() * 2) {
					data.setVelocityX(0.0);
					data.setVelocityZ(0.0);
				}
			}

			data.setVelocityHorizontal(MathUtil.hypot(data.getVelocityX(), data.getVelocityZ()));
		}

		data.setVelocityXZTicks(data.getVelocityXZTicks() + 1);
		data.setVelocityYTicks(data.getVelocityYTicks() + 1);
	}
}
