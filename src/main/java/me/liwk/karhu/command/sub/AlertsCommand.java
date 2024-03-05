/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.command.sub;

import me.liwk.karhu.Karhu;
import me.liwk.karhu.command.CommandAPI;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.util.framework.Command;
import me.liwk.karhu.util.framework.CommandArgs;
import me.liwk.karhu.util.framework.CommandFramework;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AlertsCommand extends CommandAPI {
	public AlertsCommand(CommandFramework k) {
		super(k);
	}

	@Command(
		name = "alerts",
		permission = "karhu.alerts"
	)
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();
		String[] args = command.getArgs();
		if (command.getLabel().equalsIgnoreCase("alerts")) {
			ConfigManager cfg = Karhu.getInstance().getConfigManager();
			if (command.getSender() instanceof Player) {
				Karhu.getInstance().getAlertsManager().toggleAlerts(player);
				player.sendMessage(
					Karhu.getInstance().getAlertsManager().hasAlertsToggled(player.getUniqueId())
						? ChatColor.translateAlternateColorCodes('&', cfg.getConfig().getString("commands.alerts.enabled"))
						: ChatColor.translateAlternateColorCodes('&', cfg.getConfig().getString("commands.alerts.disabled"))
				);
			}
		}
	}
}
