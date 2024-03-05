/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.framework;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class BukkitCommand extends org.bukkit.command.Command {
	private final Plugin owningPlugin;
	private final CommandExecutor executor;

	protected BukkitCommand(String label, CommandExecutor executor, Plugin owner) {
		super(label);
		this.executor = executor;
		this.owningPlugin = owner;
		this.usageMessage = "";
	}

	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean success = false;
		if (!this.owningPlugin.isEnabled()) {
			return false;
		} else if (!this.testPermission(sender)) {
			return true;
		} else {
			try {
				success = this.executor.onCommand(sender, this, commandLabel, args);
			} catch (Throwable var9) {
				throw new CommandException("Error while executing '" + commandLabel + "' in plugin " + this.owningPlugin.getDescription().getFullName(), var9);
			}

			if (!success && this.usageMessage.length() > 0) {
				for (String line : this.usageMessage.replace("<command>", commandLabel).split("\n")) {
					sender.sendMessage(line);
				}
			}

			return success;
		}
	}
}
