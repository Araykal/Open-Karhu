/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.net;

import java.util.LinkedList;
import me.liwk.karhu.util.gui.Callback;

public class TaskData {
	private final int id;
	private long timestamp;
	private final LinkedList<KarhuTask> tasks = new LinkedList<>();

	public TaskData(int id, long timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}

	public TaskData(int id, Callback<Integer> callback) {
		this.id = id;
		this.addTask(callback);
	}

	public void addTask(Callback<Integer> callback) {
		this.tasks.add(new KarhuTask(callback, this.id));
	}

	public void addTask(KarhuTask karhuTask) {
		karhuTask.setId(this.id);
		this.tasks.add(karhuTask);
	}

	public void consumeTask() {
		this.tasks.forEach(KarhuTask::runTask);
	}

	public boolean hasTask() {
		return this.tasks.size() > 0;
	}

	public int getId() {
		return this.id;
	}

	public long getTimestamp() {
		return this.timestamp;
	}
}
