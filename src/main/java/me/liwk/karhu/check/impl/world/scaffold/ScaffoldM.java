/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

import com.github.retrooper.packetevents.util.Vector3i;
import lombok.SneakyThrows;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.BlockPlaceEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.mc.boundingbox.BoundingBox;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Scaffold (M)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = true,
	credits = "§c§lCREDITS: §aIslandscout §7for the base idea."
)
public final class ScaffoldM extends PacketCheck {
	private double buffer;

	public ScaffoldM(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan16() && packet instanceof BlockPlaceEvent) {
			Vector pos = ((BlockPlaceEvent)packet).getOrigin();
			Vector targetPos = ((BlockPlaceEvent)packet).getBlockPos();
			if (pos.getX() != -1.0 && (pos.getY() != 255.0 || pos.getY() != -1.0) && pos.getZ() != -1.0) {
				BoundingBox hitbox = new BoundingBox(this.data, targetPos.getX(), targetPos.getY(), targetPos.getZ(), targetPos.getX() + 1.0, targetPos.getY() + 1.0, targetPos.getZ() + 1.0);
				int face = ((BlockPlaceEvent)packet).getFace();
				Vector3i facePosInt = ((BlockPlaceEvent)packet).getBlockFacePosition();
				Vector facePos = new Vector(facePosInt.getX(), facePosInt.getY(), facePosInt.getZ());
				float sneakAmount1_8 = this.data.isSneaking() ? 1.54F : 1.62F;
				float sneakAmount1_13 = this.data.isSneaking() ? 1.27F : 1.62F;
				Vector eyeLocation = new Vector(
					this.data.getLocation().x, this.data.getLocation().y + (double)(!this.data.isNewerThan12() ? sneakAmount1_8 : sneakAmount1_13), this.data.getLocation().z
				);
				Vector eyeLocationFixed = new Vector(
					this.data.getLastLocation().x, this.data.getLastLocation().y + (double)(!this.data.isNewerThan12() ? sneakAmount1_8 : sneakAmount1_13), this.data.getLastLocation().z
				);
				if (!this.data.isInsideBlock()
					&& !this.data.isInWeb()
					&& !this.data.isOnWeb()
					&& !this.data.isCollidedHorizontally()
					&& !this.data.isRiding()
					&& !this.data.isSpectating()
					&& !this.data.isPossiblyTeleporting()) {
					if (!(facePos.dot(MathUtil.getDirection(this.data.getLocation().yaw, this.data.getLocation().pitch)) >= 0.0)
						|| !(facePos.dot(MathUtil.getDirection(this.data.getLastLocation().yaw, this.data.getLastLocation().pitch)) >= 0.0)
						|| hitbox.hasPoint(eyeLocation)
						|| hitbox.hasPoint(eyeLocationFixed)
						|| !this.isNotGroundBridging()) {
						this.buffer = Math.max(this.buffer - 0.5, 0.0);
					} else if (++this.buffer > 5.0) {
						this.fail("* Impossible block face\n §f* face: §b" + face, this.getBanVL(), 120L);
					}
				}
			}
		}
	}

	@SneakyThrows
	public boolean isNotGroundBridging() {
			Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(this.data.getLocation().clone().subtract(0.0, 2.0, 0.0).toLocation(this.data.getWorld()));
			if (block == null) {
				return false;
			} else {
				return block.getType() == Material.AIR;
			}

	}
}
