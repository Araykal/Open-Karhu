/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.collision;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import me.liwk.karhu.handler.collision.enums.Boxes;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class CollisionBoxParser {
	public static AxisAlignedBB from(Entity e) {
		Location l = e.getLocation();
		AxisAlignedBB b;
		switch (e.getType()) {
			case BOAT:
				b = fromBoxEnum(l, Boxes.BOAT);
				break;
			case PLAYER:
				b = fromBoxEnum(l, Boxes.PLAYER);
				break;
			default:
				b = MathUtil.getEntityBoundingBox(l);
		}

		return b;
	}

	public static Boxes from(EntityType e) {
		return EntityTypes.BOAT.equals(e) ? Boxes.BOAT : Boxes.PLAYER;
	}

	private static AxisAlignedBB fromBoxEnum(Location l, Boxes e) {
		return new AxisAlignedBB(
			l.getX() - (double)e.getWidth(), l.getY(), l.getZ() - (double)e.getWidth(), l.getX() + (double)e.getWidth(), l.getY() + (double)e.getHeight(), l.getZ() + (double)e.getWidth()
		);
	}
}
