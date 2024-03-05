/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.world.nms.wrap;

import me.liwk.karhu.data.KarhuPlayer;

public final class WrappedEntityPlayer extends WrappedEntity {
	private final KarhuPlayer data;

	public WrappedEntityPlayer(KarhuPlayer data) {
		super(1.8F, 0.6F);
		this.data = data;
	}

	public void startSneaking() {
	}

	public void onStopSneaking() {
	}

	public void setPosition(double x, double y, double z) {
		this.data.setLastBoundingBox(this.data.getBoundingBox().clone());
		float f = this.w / 2.0F;
		this.data.getBoundingBox().setBounds(x - (double)f, y, z - (double)f, x + (double)f, y + (double)this.h, z + (double)f);
		this.data.getMcpCollision().setBounds(x - (double)f, y, z - (double)f, x + (double)f, y + (double)this.h, z + (double)f);
		this.data.getMcpCollision().expandMin(0.001, 0.001, 0.001).subtractMax(0.001, 0.001, 0.001);
		this.data.setBoundingBoxInited(true);
	}

	public void initChunks() {
		this.data.getBoundingBox().initChunkData();
	}
}
