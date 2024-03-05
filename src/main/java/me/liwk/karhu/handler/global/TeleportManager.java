/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.LinkedList;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.Teleport;
import org.bukkit.util.Vector;

public final class TeleportManager {
	private final KarhuPlayer data;
	public final LinkedList<Teleport> locations = new LinkedList<>();
	public int teleportAmount;
	public int zeroAmount;
	public int teleportTicks;
	public int teleportsPending;
	public int trackedTps;

	public void handlePreFlying(WrapperPlayClientPlayerFlying packet) {
		boolean legacy = this.data.getClientVersion().getProtocolVersion() < 47;
		boolean ground = !legacy && packet.isOnGround();
		boolean moving = packet.hasPositionChanged();
		boolean rotating = packet.hasRotationChanged();
		Location location = packet.getLocation();
		if (!this.locations.isEmpty()) {
			Vector position = new Vector(location.getX(), location.getY(), location.getZ());

			for (Teleport teleport : this.locations) {
				double distance = teleport.position.distance(position);
				if (distance <= 1.0E-7 && moving && rotating && !ground) {
					this.teleportTicks = 0;
					++this.zeroAmount;
					++this.trackedTps;
					this.callTeleport(position);
					break;
				}
			}
		}

		this.data.setPossiblyTeleporting(this.isTeleporting());
	}

	public void handlePostFlying() {
		++this.teleportTicks;
	}

	public boolean isTeleporting() {
		return this.teleportTicks <= 0;
	}

	public void removeLocation(Teleport teleport) {
		this.locations.remove(teleport);
		--this.teleportsPending;
	}

	private void callTeleport(Vector vector) {
		this.data.setHasTeleportedOnce(true);
		this.data.getLocation().setTeleport(true);
	}

	public TeleportManager(KarhuPlayer data) {
		this.data = data;
	}
}
