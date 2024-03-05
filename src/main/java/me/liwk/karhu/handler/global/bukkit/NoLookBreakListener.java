/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global.bukkit;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.impl.world.block.NoLookBreak;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.mc.axisalignedbb.Ray;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class NoLookBreakListener implements Listener {
	@EventHandler(
		priority = EventPriority.HIGHEST
	)
	public void onBlockReach(BlockBreakEvent e) {
		if (!e.isCancelled()) {
			Player p = e.getPlayer();
			KarhuPlayer data = Karhu.getInstance().getDataManager().getPlayerData(p.getUniqueId());
			if (data != null) {
				NoLookBreak check = data.getCheckManager().getCheck(NoLookBreak.class);
				if (Karhu.getInstance().getCheckState().isEnabled(check.getName())) {
					if (data.isRiding()) {
						return;
					}

					Block block = e.getBlock();
					Location playerLoc = p.getLocation();
					if (playerLoc.getWorld() == null || MaterialChecks.SIGNS.contains(block.getType()) || !block.getType().isSolid()) {
						return;
					}

					if (MaterialChecks.ONETAPS.contains(block.getType()) || block.getType().name().contains("GLASS")) {
						return;
					}

					Location loc = block.getLocation();
					float sneakAmount1_8 = !data.isWasSneaking() && !data.isWasWasSneaking() ? (data.isGliding() ? 0.4F : (data.isRiptiding() ? 0.4F : 1.62F)) : 1.54F;
					float sneakAmount1_13 = !data.isWasSneaking() && !data.isWasWasSneaking() ? (data.isGliding() ? 0.4F : (data.isRiptiding() ? 0.4F : 1.62F)) : 1.27F;
					Vector eyeLocation = playerLoc.toVector().clone().add(new Vector(0.0F, !data.isNewerThan12() ? sneakAmount1_8 : sneakAmount1_13, 0.0F));
					Vector direction = MathUtil.getDirection(data.getLocation().getYaw(), data.getLocation().getPitch());
					Vector direction2 = MathUtil.getDirection(data.getLastLocation().getYaw(), data.getLastLocation().getPitch());
					AxisAlignedBB targetAABB = new AxisAlignedBB(loc.toVector(), loc.toVector(), true);
					targetAABB = targetAABB.addCoord(1.0, 1.0, 1.0);
					double distance = targetAABB.distance(eyeLocation);
					Vector occludeIntersection = targetAABB.intersectsRay(new Ray(eyeLocation, direction), 0.0F, 8.0F);
					Vector occludeIntersection2 = targetAABB.intersectsRay(new Ray(eyeLocation, direction2), 0.0F, 8.0F);
					if (occludeIntersection == null && occludeIntersection2 == null && data.deltas.deltaYaw < 10.0F && data.deltas.deltaPitch < 10.0F) {
						check.setViolations(check.getViolations() + 1.0);
						if (check.getViolations() > (double)(MathUtil.getPingInTicks(data.getTransactionPing()) + 3)) {
							check.fail("Tried to break block without looking (cancelled)\n* distance: " + distance + "\n* broke: " + block.getType(), check.getBanVL(), 200L);
						}

						e.setCancelled(true);
					} else {
						check.setViolations(Math.max(0.0, check.getViolations() - 0.5));
					}
				}
			}
		}
	}
}
