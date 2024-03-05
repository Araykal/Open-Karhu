/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import me.liwk.karhu.data.EntityData;
import me.liwk.karhu.data.KarhuPlayer;
import org.bukkit.util.Vector;

public class EntityLocationHandler {
	public static void addEntity(KarhuPlayer data, EntityType entityType, double x, double y, double z, int eid) {
		EntityData ed = new EntityData(x, y, z, eid, entityType);
		data.entityData.put(eid, ed);
	}

	public static void updateEntityLook(KarhuPlayer data, int id) {
		EntityData edata = data.entityData.get(id);
		if (edata != null) {
			edata.posIncrements = 3;
		}
	}

	public static void updateEntityRelMove2(KarhuPlayer data, int id, double x, double y, double z) {
		EntityData edata = data.entityData.get(id);
		if (edata != null) {
			edata.newX += x;
			edata.newY += y;
			edata.newZ += z;
			edata.newLocations.add(new Vector(edata.newX, edata.newY, edata.newZ));
			if (data.getClientVersion().getProtocolVersion() > 47) {
				lenientBox(edata.newX, edata.newY, edata.newZ, edata);
			}

			edata.posIncrements = 3;
		}
	}

	public static void updateEntityTeleport2(KarhuPlayer data, int id, double x, double y, double z) {
		EntityData edata = data.entityData.get(id);
		if (edata != null) {
			edata.newX = x;
			edata.newY = y;
			edata.newZ = z;
			edata.newLocations.add(new Vector(edata.newX, edata.newY, edata.newZ));
			if (data.getClientVersion().getProtocolVersion() > 47) {
				lenientBox(x, y, z, edata);
			}

			edata.posIncrements = 3;
		}
	}

	public static void updateEntityLocations(KarhuPlayer data) {
		data.entityData.values().forEach(EntityData::interpolate);
	}

	public static void updateFlyingLocations(KarhuPlayer data, WrapperPlayClientPlayerFlying flying) {
		if (flying.hasPositionChanged()) {
			data.lastPos = 0;
			data.attackerX = flying.getLocation().getX();
			data.attackerY = flying.getLocation().getY();
			data.attackerZ = flying.getLocation().getZ();
		} else {
			++data.lastPos;
		}

		if (flying.hasRotationChanged()) {
			data.attackerYaw = flying.getLocation().getYaw();
			data.attackerPitch = flying.getLocation().getPitch();
		}
	}

	public static void destroyEntity(KarhuPlayer data, int[] id) {
		for (int a : id) {
			data.entityData.remove(a);

			for (EntityData edata : data.entityData.values()) {
				if (edata.isRiding() && a == edata.getVehicleId()) {
					edata.setRiding(false);
					edata.setVehicleId(-1);
				}
			}

			if (data.isRiding() && a == data.getVehicleId()) {
				data.setRiding(false);
				data.setBrokenVehicle(true);
				data.setVehicleId(-1);
				data.setVehicle(null);
			}
		}
	}

	private static void lenientBox(double x, double y, double z, EntityData edata) {
		edata.maxX = Math.max(edata.maxX, x);
		edata.minX = Math.min(edata.minX, x);
		edata.maxY = Math.max(edata.maxY, y);
		edata.minY = Math.min(edata.minY, y);
		edata.maxZ = Math.max(edata.maxZ, z);
		edata.minZ = Math.min(edata.minZ, z);
	}
}
