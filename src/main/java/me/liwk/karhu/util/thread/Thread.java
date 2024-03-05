/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.liwk.karhu.Karhu;

public class Thread {
	private final ExecutorService executorService = Executors.newSingleThreadExecutor(
		new ThreadFactoryBuilder().setNameFormat("karhu-user-thread-" + Karhu.getInstance().getThreadManager().getUserThreads().size()).build()
	);
	public int count;

	public ExecutorService getExecutorService() {
		return this.executorService;
	}

	public int getCount() {
		return this.count;
	}
}
