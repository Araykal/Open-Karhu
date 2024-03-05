/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.global.bukkit;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.util.gui.Button;
import me.liwk.karhu.util.gui.Gui;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryHandler implements Listener {
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getCurrentItem() != null) {
			Player player = (Player)e.getWhoClicked();
			Gui gui = Gui.getGui(player);
			if (player.getOpenInventory().getTitle().contains("§r") && gui != null) {
				e.setCancelled(true);
			}

			if (gui != null) {
				for (Button b : gui.getButtons()) {
					if (b.item.clone().equals(e.getCurrentItem())) {
						e.setCancelled(true);
						player.playSound(
							player.getLocation(), Karhu.SERVER_VERSION.isNewerThan(ServerVersion.V_1_8_8) ? Sound.valueOf("ENTITY_CHICKEN_EGG") : Sound.valueOf("CHICKEN_EGG_POP"), 0.5F, 1.0F
						);
						b.onClick(player, e.getClick());
					}
				}
			}
		}
	}

	@EventHandler(
		priority = EventPriority.HIGHEST
	)
	public void onInvClose(InventoryCloseEvent e) {
		Player player = (Player)e.getPlayer();
		if (e.getView().getTitle().contains("§r")) {
			Gui gui = Gui.getGui(player);
			if (gui != null) {
				gui.close(player);
			}
		}
	}
}
