/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.location.CustomLocation;

public class AbilityManager {
	private final KarhuPlayer data;
	private int badTicks = -1;
	private boolean flySet;

	public AbilityManager(KarhuPlayer data) {
		this.data = data;
	}

	public void onFlying() {
		this.flySet = false;
		if (this.badTicks == 0) {
			this.badTicks = 1;
		} else if (this.badTicks == 1) {
			this.data.flying = false;
			this.badTicks = -1;
		}
	}

	public void onAbilityClient(WrapperPlayClientPlayerAbilities abilities) {
		boolean fly = abilities.isFlying();
		if (this.flySet && !abilities.isFlying()) {
			this.flySet = false;
			this.badTicks = 0;
		} else {
			if (abilities.isFlying()) {
				this.flySet = true;
			}

			this.data.flying = fly && this.data.allowFlying;
		}
	}

	public void onAbilityServer(WrapperPlayServerPlayerAbilities packet) {
		if (!packet.isFlightAllowed()) {
			CustomLocation location = this.data.getLocation();
			long time = this.data.getServerTick() - this.data.getLastTeleportPacket();
			if (this.data.getTeleportManager().teleportsPending <= 0 && time > 3L) {
				this.data.setFlyCancel(new CustomLocation(location.x, location.y, location.z));
			}

			this.data.confirmingFlying = true;
			this.data.setLastConfirmingState(this.data.getTotalTicks());
		}

		this.data.queueToPrePing(uid -> {
			this.data.flying = packet.isFlying();
			this.data.allowFlying = packet.isFlightAllowed();
			this.data.confirmingFlying = false;
			this.badTicks = -1;
		});
	}
}
