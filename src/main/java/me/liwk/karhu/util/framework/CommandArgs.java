/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.framework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandArgs {
	private final CommandSender sender;
	private final org.bukkit.command.Command command;
	private final String label;
	private final String[] args;

	protected CommandArgs(CommandSender sender, org.bukkit.command.Command command, String label, String[] args, int subCommand) {
		String[] modArgs = new String[args.length - subCommand];

		for (int i = 0; i < args.length - subCommand; ++i) {
			modArgs[i] = args[i + subCommand];
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append(label);

		for (int x = 0; x < subCommand; ++x) {
			buffer.append(".").append(args[x]);
		}

		String cmdLabel = buffer.toString();
		this.sender = sender;
		this.command = command;
		this.label = cmdLabel;
		this.args = modArgs;
	}

	public CommandSender getSender() {
		return this.sender;
	}

	public org.bukkit.command.Command getCommand() {
		return this.command;
	}

	public String getLabel() {
		return this.label;
	}

	public String[] getArgs() {
		return this.args;
	}

	public String getArgs(int index) {
		return this.args[index];
	}

	public int length() {
		return this.args.length;
	}

	public boolean isPlayer() {
		return this.sender instanceof Player;
	}

	public Player getPlayer() {
		return this.sender instanceof Player ? (Player)this.sender : null;
	}
}
