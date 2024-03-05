/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.movement.vehicle;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.VehicleEvent;
import org.bukkit.entity.Boat;

@CheckInfo(
	name = "VehicleFly (A)",
	category = Category.MOVEMENT,
	subCategory = SubCategory.FLY,
	experimental = true
)
public final class VehicleFly extends PacketCheck {
	private double lastX;
	private double lastY;
	private double lastZ;
	private boolean lastGround;
	private boolean lastGravity;
	private double violationsZero;
	private int ticks;

	public VehicleFly(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event event) {
		if (event instanceof VehicleEvent) {
			double deltaX = Math.abs(this.data.getVehicleX() - this.lastX);
			double deltaY = this.data.getVehicleY() - this.lastY;
			double deltaZ = Math.abs(this.data.getVehicleZ() - this.lastZ);
			if (this.data.getVehicle() != null) {
				boolean gravity = this.data.getVehicle().hasGravity();
				boolean ground = this.data.getVehicle().isOnGround();
				if (gravity && this.lastGravity) {
					if (++this.ticks > 3) {
						if (deltaY > 1.5) {
							this.fail("* Moving upwards with " + this.data.getVehicle().getType().getName(), 300L);
							this.setLast(ground);
							return;
						}

						if (deltaY > 0.01 && this.data.getVehicle() instanceof Boat && !ground && !this.lastGround) {
							this.fail("* Moving upwards with boat", 300L);
							this.setLast(ground);
							return;
						}

						if (deltaY > 0.5 && this.data.getVehicle() instanceof Boat) {
							this.fail("* Moving upwards with boat (2)", 300L);
							this.setLast(ground);
							return;
						}

						if (deltaY == 0.0 && (deltaX > 0.0 || deltaZ > 0.0) && this.data.getVehicle() instanceof Boat && !ground && !this.lastGround) {
							if (++this.violationsZero > 2.0) {
								this.fail("* Moving 0 vertical with boat", 300L);
								this.setLast(ground);
								return;
							}
						} else {
							this.violationsZero = Math.max(this.violationsZero - 0.1, 0.0);
						}
					}

					this.setLast(ground);
				}

				this.lastGravity = gravity;
			}
		}

		if (this.data.getVehicleId() == -1) {
			this.ticks = 0;
		}
	}

	private void setLast(boolean ground) {
		this.lastX = this.data.getVehicleX();
		this.lastY = this.data.getVehicleY();
		this.lastZ = this.data.getVehicleZ();
		this.lastGround = ground;
	}
}
