/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.task;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class Tasker {
	private static Plugin plugin;

	public static void run(Runnable runnable) {
		if (plugin != null) {
			Bukkit.getServer().getScheduler().runTask(plugin, runnable);
		}
	}

	public static void runTaskLater(Runnable runnable, long time) {
		if (plugin != null) {
			Bukkit.getServer().getScheduler().runTaskLater(plugin, runnable, time);
		}
	}

	public static void runTaskLaterAsync(Runnable runnable, long time) {
		if (plugin != null) {
			Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, time);
		}
	}

	private static BukkitTask taskTimer(Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0L, 1L);
	}

	public static void taskAsync(Runnable runnable) {
		if (plugin != null) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
		}
	}

	private static BukkitTask taskTimerAsync(Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, 1L);
	}

	public static void runSyncRepeating(Runnable runnable) {
		if (plugin != null) {
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 1L, 1L);
		}
	}

	public static void load(Plugin p) {
		plugin = p;
	}

	public static void stop() {
		plugin = null;
	}
}
