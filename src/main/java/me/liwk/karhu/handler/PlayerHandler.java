/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.player.MovementUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class PlayerHandler {
	public static void checkConditions(KarhuPlayer data) {
		if (data.getBukkitPlayer() != null) {
			if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_7_10)) {
				data.setDepthStriderLevel(MovementUtils.getDepthStriderLevel(data.getBukkitPlayer()));
			}

			if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_8_8)) {
				data.setGliding(data.getBukkitPlayer().isGliding());
				data.setLastGlide(data.getBukkitPlayer().isGliding() ? data.getTotalTicks() : data.getLastGlide());
			}

			if (Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_12_2)) {
				data.setRiptiding(data.getBukkitPlayer().isRiptiding());
				data.setLastRiptide(data.getBukkitPlayer().isRiptiding() ? data.getTotalTicks() : data.getLastRiptide());
			}

			if (Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_16)) {
				data.setSoulSpeedLevel(MovementUtils.getSoulSpeedLevel(data.getBukkitPlayer()));
			}
		}
	}

	public static boolean hasEnchantment(ItemStack item, Enchantment enchantment) {
		return item.getEnchantments().containsKey(enchantment);
	}
}
