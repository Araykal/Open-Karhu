/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.packet.badpackets;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.util.Vector3f;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.InteractEvent;

@CheckInfo(
	name = "BadPackets (O)",
	category = Category.PACKET,
	subCategory = SubCategory.BADPACKETS,
	experimental = false
)
public final class BadPacketsO extends PacketCheck {
	public BadPacketsO(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (packet instanceof InteractEvent) {
			InteractEvent interactEvent = (InteractEvent)packet;
			if (interactEvent.isPlayer()) {
				Vector3f vector3d = interactEvent.getVec3D();
				if (vector3d != null && interactEvent.isAt()) {
					double x = (double)Math.abs(vector3d.x);
					double y = (double)vector3d.y;
					double z = (double)Math.abs(vector3d.z);
					EntityType e = this.data.getEntityData().get(((InteractEvent)packet).getEntityId()).getType();
					double expandX = x - 0.4005;
					double expandY = y - 1.905;
					double expandZ = z - 0.4005;
					String entityName = e.getName().toString();
					if (expandX > 0.0 || expandY > 0.0 || y < -0.105 || expandZ > 0.0) {
						int expand = (int)Math.round((expandX > 0.0 ? expandX : (0.0 + expandY > 0.0 ? expandY : (0.0 + expandZ > 0.0 ? expandZ : 0.0))) * 100.0);
						this.fail(
							"* Wrong hitbox in packet\n§f* expand §b" + expand + "%" + String.format("\n§f* x§b %.3f§f, y§b %.3f§f, z§b %.3f", expandX, expandY, expandZ) + "\n§f* type §b" + entityName,
							420L
						);
					}
				}
			}
		}
	}
}
