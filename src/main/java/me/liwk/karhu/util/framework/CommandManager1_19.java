/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.framework;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandManager1_19 {
	private static final Field knownCommandsField;
	private static final CommandMap bukkitCommandMap;
	private static Method syncCommandsMethod;
	protected final JavaPlugin plugin;
	private final Map<String, org.bukkit.command.Command> registered = new HashMap<>();

	public CommandManager1_19(@NotNull JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static void syncCommand() {
		if (syncCommandsMethod != null) {
			try {
				syncCommandsMethod.invoke(Bukkit.getServer());
			} catch (IllegalAccessException | InvocationTargetException var1) {
				Bukkit.getLogger().log(Level.WARNING, "Error when syncing commands", (Throwable)var1);
			}
		}
	}

	public static void unregisterFromKnownCommands(@NotNull org.bukkit.command.Command command) throws IllegalAccessException {
		Map<?, ?> knownCommands = (Map)knownCommandsField.get(bukkitCommandMap);
		knownCommands.values().removeIf(command::equals);
		command.unregister(bukkitCommandMap);
	}

	public static void registerCommandToCommandMap(@NotNull String label, @NotNull org.bukkit.command.Command command) {
		bukkitCommandMap.register(label, command);
	}

	public final void register(@NotNull org.bukkit.command.Command command) {
		String name = command.getLabel();
		if (this.registered.containsKey(name)) {
			this.plugin.getLogger().log(Level.WARNING, "Duplicated \"{0}\" command ! Ignored", name);
		} else {
			registerCommandToCommandMap(this.plugin.getName(), command);
			this.registered.put(name, command);
		}
	}

	public final void unregister(@NotNull org.bukkit.command.Command command) {
		try {
			unregisterFromKnownCommands(command);
			this.registered.remove(command.getLabel());
		} catch (ReflectiveOperationException var3) {
			this.plugin.getLogger().log(Level.WARNING, "Something wrong when unregister the command", (Throwable)var3);
		}
	}

	public final void unregister(@NotNull String command) {
		if (this.registered.containsKey(command)) {
			this.unregister(this.registered.remove(command));
		}
	}

	public final void unregisterAll() {
		this.registered.values().forEach(command -> {
			try {
				unregisterFromKnownCommands(command);
			} catch (ReflectiveOperationException var3) {
				this.plugin.getLogger().log(Level.WARNING, "Something wrong when unregister the command", (Throwable)var3);
			}
		});
		this.registered.clear();
	}

	@NotNull
	public final Map<String, org.bukkit.command.Command> getRegistered() {
		return Collections.unmodifiableMap(this.registered);
	}

	static {
		try {
			Method commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
			bukkitCommandMap = (CommandMap)commandMapMethod.invoke(Bukkit.getServer());
			knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
		} catch (ReflectiveOperationException var6) {
			throw new ExceptionInInitializerError(var6);
		}

		try {
			Class<?> craftServer = Bukkit.getServer().getClass();
			syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
		} catch (Exception var5) {
		} finally {
			if (syncCommandsMethod != null) {
				syncCommandsMethod.setAccessible(true);
			}
		}
	}
}
