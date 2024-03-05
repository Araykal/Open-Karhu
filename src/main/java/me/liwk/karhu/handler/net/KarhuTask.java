/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.net;

import me.liwk.karhu.util.gui.Callback;

public class KarhuTask {
	private int id;
	private final Callback<Integer> callback;

	public KarhuTask(Callback<Integer> callback) {
		this.callback = callback;
	}

	public KarhuTask(Callback<Integer> callback, int id) {
		this.callback = callback;
		this.id = id;
	}

	public void runTask() {
		this.callback.call(this.id);
	}

	public int getId() {
		return this.id;
	}

	public Callback<Integer> getCallback() {
		return this.callback;
	}

	public void setId(int id) {
		this.id = id;
	}
}
