/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.liwk.karhu.data.task.IAbstractTickTask;
import me.liwk.karhu.util.KarhuStream;

public final class TaskManager<T> {
	private final Set<IAbstractTickTask<T>> activeTasks = new HashSet<>();
	private final Set<IAbstractTickTask<T>> pendingTasks = new HashSet<>();

	public void queueNewTask(IAbstractTickTask<T> task) {
		if (this.isAlreadyActive(task)) {
			this.pendingTasks.add(task);
		} else {
			this.activeTasks.add(task);
		}
	}

	public void queueNewTaskNoPending(IAbstractTickTask<T> task) {
		if (!this.isAlreadyActive(task)) {
			this.activeTasks.add(task);
		}
	}

	public void doTasks() {
		int pos = 0;
		if (!this.activeTasks.isEmpty()) {
			for (Iterator<IAbstractTickTask<T>> iterator = this.activeTasks.iterator(); iterator.hasNext(); ++pos) {
				IAbstractTickTask<T> abstractTickTask = iterator.next();

				try {
					if (!abstractTickTask.conditionUntil().test()) {
						abstractTickTask.getRunnable().run();
					} else {
						iterator.remove();
					}
				} catch (Throwable var5) {
					throw new RuntimeException("Exception running task#" + pos + " with id: " + abstractTickTask.getId(), var5);
				}
			}

			this.checkPendingQueue();
		}
	}

	private void checkPendingQueue() {
		if (!this.activeTasks.isEmpty()) {
			Iterator<IAbstractTickTask<T>> iterator = this.pendingTasks.iterator();

			while (iterator.hasNext()) {
				IAbstractTickTask<T> abstractTickTask = iterator.next();
				if (!this.isAlreadyActive(abstractTickTask)) {
					this.activeTasks.add(abstractTickTask);
					iterator.remove();
				}
			}
		}
	}

	private boolean isAlreadyActive(IAbstractTickTask<T> task) {
		return new KarhuStream<>(this.activeTasks).any(t -> t.getId().equals(task.getId()));
	}
}
