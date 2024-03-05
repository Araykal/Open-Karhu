/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.handler.interfaces.IVehicleHandler;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;

public final class VehicleHandler implements IVehicleHandler {
	private final KarhuPlayer data;
	private int lastDismount;

	@Override
	public void handle(Entity e) {
		if (this.data.getTotalTicks() >= 20) {
			if (e == this.data.getBukkitPlayer()) {
				if (Karhu.getInstance().getConfigManager().isVehicleHandler()) {
					MiscellaneousAlertPoster.postMisc(
						Karhu.getInstance().getConfigManager().getConfig().getString("VehicleHandlerMessage").replaceAll("%player%", this.data.getName()), this.data, "Vehicle"
					);
					this.forceDismount();
				}
			} else if (e instanceof Vehicle) {
				double dist = e.getLocation().distanceSquared(this.data.getLocation().toLocation(this.data.getWorld()));
				if (dist <= 20.0) {
					this.data.setRiding(true);
				}
			}
		}
	}

	@Override
	public void handleMove() {
		if (this.data.getTotalTicks() - this.lastDismount > 1) {
			this.data.setExitingVehicle(false);
		}
	}

	private void forceDismount() {
		if (Karhu.getInstance().getConfigManager().isVehicleHandler() && this.data.getBukkitPlayer().getVehicle() != null && this.data.getBukkitPlayer().isInsideVehicle()) {
			this.data.getBukkitPlayer().leaveVehicle();
		}
	}

	public VehicleHandler(KarhuPlayer data) {
		this.data = data;
	}
}
