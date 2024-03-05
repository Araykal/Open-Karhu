/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api;

import me.liwk.karhu.api.event.registry.EventRegistry;
import me.liwk.karhu.api.event.registry.KarhuEventListenerRegistry;
import me.liwk.karhu.api.users.UserAccessor;
import org.bukkit.plugin.java.JavaPlugin;

public final class KarhuAPI extends JavaPlugin {
	private static KarhuEventListenerRegistry eventRegistry = null;
	private static UserAccessor userAccessor = null;

	public void onEnable() {
	}

	public void onDisable() {
		eventRegistry.shutdown();
	}

	public static UserAccessor getUserAccessor() {
		if (userAccessor == null) {
			userAccessor = new UserAccessor();
		}

		return userAccessor;
	}

	public static KarhuEventListenerRegistry getEventRegistry() {
		if (eventRegistry == null) {
			eventRegistry = new EventRegistry();
		}

		return eventRegistry;
	}

	public static void shutdown() {
		if (eventRegistry != null) {
			eventRegistry.shutdown();
		}
	}

	public static long getFreeMemory() {
		Runtime r = Runtime.getRuntime();
		return r.freeMemory() / 1024L / 1024L;
	}

	public static long getMaxMemory() {
		Runtime r = Runtime.getRuntime();
		return r.maxMemory() / 1024L / 1024L;
	}

	public static long getTotalMemory() {
		Runtime r = Runtime.getRuntime();
		return r.totalMemory() / 1024L / 1024L;
	}
}
