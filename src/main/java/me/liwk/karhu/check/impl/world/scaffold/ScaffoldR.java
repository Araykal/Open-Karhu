/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.scaffold;

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
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@CheckInfo(
	name = "Scaffold (R)",
	category = Category.WORLD,
	subCategory = SubCategory.SCAFFOLD,
	experimental = false,
	credits = "§c§lCREDITS: §aIslandscout §7for the base idea."
)
public final class ScaffoldR extends PacketCheck {
	public ScaffoldR(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!this.data.isNewerThan16() && packet instanceof BlockPlaceEvent) {
			BlockPlaceEvent place = (BlockPlaceEvent)packet;
			float sneakAmount1_8 = this.data.isSneaking() ? 1.54F : 1.62F;
			float sneakAmount1_13 = this.data.isSneaking() ? 1.27F : 1.62F;
			Location blockPos = place.getTargetedBlockLocation();
			ItemStack stack = place.getItemStack() == null ? new ItemStack(Material.AIR) : place.getItemStack();
			if (place.isUsableItem() || !stack.getType().isSolid() || !stack.getType().isBlock()) {
				this.decrease(0.15);
				return;
			}

			Vector eyeLocation = new Vector(
				this.data.getLocation().x, this.data.getLocation().y + (double)(!this.data.isNewerThan12() ? sneakAmount1_8 : sneakAmount1_13), this.data.getLocation().z
			);
			Vector dir = MathUtil.getDirection(this.data.getLocation().getYaw(), this.data.getLocation().getPitch());
			Vector extraDir = MathUtil.getDirection(this.data.getLastLastLocation().getYaw(), this.data.getLastLastLocation().getPitch());
			Vector extraDir2 = MathUtil.getDirection(this.data.getLastLocation().getYaw(), this.data.getLastLocation().getPitch());
			AxisAlignedBB targetAABB = new AxisAlignedBB(blockPos.toVector(), blockPos.toVector(), true);
			targetAABB = targetAABB.addCoord(1.0, 1.0, 1.0).expand(0.75, 0.75, 0.75);
			boolean betweenRays = targetAABB.betweenRays(eyeLocation, dir, dir);
			boolean betweenFirst = targetAABB.betweenRays(eyeLocation, dir, extraDir);
			boolean betweenSecond = targetAABB.betweenRays(eyeLocation, dir, extraDir2);
			if (!betweenRays && !betweenFirst && !betweenSecond && !this.data.isRiding() && !this.data.isAtSign() && !this.data.isNearClimbable()) {
				if (!this.isNotGroundBridging()) {
					if (++this.violations > 5.0) {
						this.fail("* Invalid place (expand scaffold?)\n* face: " + place.getFace() + "\n* pos: " + place.getOrigin(), this.getBanVL(), 300L);
					}
				} else {
					this.decrease(0.5);
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
