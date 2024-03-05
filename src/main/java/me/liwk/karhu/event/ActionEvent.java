/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.event;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;

public class ActionEvent extends Event {
	private final Action action;

	public ActionEvent(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return this.action;
	}
}
