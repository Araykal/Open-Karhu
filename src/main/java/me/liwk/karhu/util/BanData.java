/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

import org.bukkit.entity.Player;

public final class BanData {
	public String license;
	public String karhuVer;
	public String serverVer;
	public double tps;
	public Player playerObj;
	public String player;
	public String type;
	public String client;
	public String sessionTime;
	public String coordinates;
	public long ping;
	public String logLink;

	public BanData() {
	}

	public BanData(
		String license,
		String karhuVer,
		String serverVer,
		double tps,
		Player playerObj,
		String player,
		String type,
		String client,
		String sessionTime,
		String coordinates,
		long ping,
		String logLink
	) {
		this.license = license;
		this.karhuVer = karhuVer;
		this.serverVer = serverVer;
		this.tps = tps;
		this.playerObj = playerObj;
		this.player = player;
		this.type = type;
		this.client = client;
		this.sessionTime = sessionTime;
		this.coordinates = coordinates;
		this.ping = ping;
		this.logLink = logLink;
	}
}
