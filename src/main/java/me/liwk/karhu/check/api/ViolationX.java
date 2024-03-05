/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.api;

public final class ViolationX {
	public String player;
	public String type;
	public int vl;
	public long time;
	public String data;
	public String location;
	public String world;
	public long ping;
	public double TPS;

	public ViolationX() {
	}

	public ViolationX(String player, String type, int vl, long time, String data, String location, String world, long ping, double TPS) {
		this.player = player;
		this.type = type;
		this.vl = vl;
		this.time = time;
		this.data = data;
		this.location = location;
		this.world = world;
		this.ping = ping;
		this.TPS = TPS;
	}
}
