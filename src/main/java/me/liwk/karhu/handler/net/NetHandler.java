/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.handler.net;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.manager.alert.MiscellaneousAlertPoster;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.benchmark.Benchmark;
import me.liwk.karhu.util.benchmark.BenchmarkType;
import me.liwk.karhu.util.benchmark.KarhuBenchmarker;
import me.liwk.karhu.util.gui.Callback;
import me.liwk.karhu.util.task.Tasker;

public final class NetHandler {
	private final KarhuPlayer data;
	public final LinkedList<TaskData> pings = new LinkedList<>();
	private final Queue<KarhuTask> postPendingTask = new ConcurrentLinkedQueue<>();
	public final Set<Long> pendingKeepalive = ConcurrentHashMap.newKeySet();
	private final Set<Long> invalidKeepalive = ConcurrentHashMap.newKeySet();
	public int sentKeep;
	public int sentTransac;
	public int receivedT;
	public int pReceived;
	private boolean kicked;
	private double violations;
	private boolean canCheck;
	private final ConfigManager configManager = Karhu.getInstance().getConfigManager();
	private static final String KICK_MSG = "java.net.IOException Connection timed out: no further information";

	public void handleFlying(boolean pos) {
		if (this.pendingKeepalive.size() > 100) {
			this.kicklol(true, "(ka)");
		}
	}

	public void handleKeepAlive(long id) {
		if (this.pendingKeepalive.contains(id)) {
			this.pendingKeepalive.remove(id);
			this.invalidKeepalive.remove(id);
		} else {
			this.invalidKeepalive.add(id);
			if (this.invalidKeepalive.size() > 80) {
				this.kickxd();
			}
		}
	}

	public void handleClientTransaction(short number) {
		if (number < 0) {
			TaskData received = this.pings.pollLast();
			if (received == null) {
				if (this.canCheck && ++this.violations > 3.0) {
					this.kicklol(false, "(null)");
				}

				return;
			}

			if (number != received.getId()) {
				if (!this.canCheck && ++this.pReceived > 60) {
					this.canCheck = true;
				}

				if (this.canCheck && ++this.violations > 2.0) {
					this.handleInvalidTransaction(number, (short)received.getId());
				}

				return;
			}

			long start = System.nanoTime();
			received.consumeTask();
			long end = System.nanoTime();
			Benchmark profileData = KarhuBenchmarker.getProfileData(BenchmarkType.TRANSACTION_TASK);
			profileData.insertResult(start, end);
			++this.receivedT;
			this.violations *= 0.985;
		}
	}

	public void handleServerTransaction(short id, long now) {
		if (id < 0) {
			TaskData pendingTask = this.pings.peekLast();
			if (pendingTask != null && now - pendingTask.getTimestamp() > MathUtil.toNanos(30000L)) {
				this.kicklol(false, "(30s)");
			}

			TaskData taskData = new TaskData(id, now);
			this.postPendingTask.forEach(taskData::addTask);
			this.postPendingTask.clear();
			this.pings.push(taskData);
			++this.sentTransac;
		}
	}

	public void handleServerKeepalive(long id) {
		this.pendingKeepalive.add(id);
		++this.sentKeep;
	}

	public void queueToPrePing(Callback<Integer> callback) {
		TaskData mostRecent = this.pings.peek();
		if (mostRecent != null) {
			mostRecent.addTask(callback);
		} else {
			callback.call(this.data.getCurrentServerTransaction());
		}
	}

	public int mostRecentPing() {
		TaskData mostRecent = this.pings.peek();
		return mostRecent != null ? mostRecent.getId() : this.data.getCurrentServerTransaction();
	}

	public void queueToPostPing(Callback<Integer> callback) {
		this.postPendingTask.add(new KarhuTask(callback));
	}

	private void kicklol(boolean keep, String reason) {
		if (!this.kicked && Karhu.getInstance().getConfigManager().isNethandler() && Karhu.getInstance().getConfigManager().isDelay()) {
			this.kicked = true;
			String msg = !keep
				? this.configManager
					.getCancelTransactions()
					.replaceAll("%player%", this.data.getName())
					.replaceAll("%invalid%", String.valueOf(this.pings.size()))
					.replaceAll("%total%", String.valueOf(this.sentTransac))
				: this.configManager
					.getCancelKeepalives()
					.replaceAll("%player%", this.data.getName())
					.replaceAll("%invalid%", String.valueOf(this.pendingKeepalive.size()))
					.replaceAll("%total%", String.valueOf(this.sentKeep));
			Tasker.run(() -> {
				MiscellaneousAlertPoster.postMisc(msg, this.data, "Delay");
				this.data.getBukkitPlayer().kickPlayer("Timed out " + reason);
			});
		}
	}

	private void kickxd() {
		if (!this.kicked && Karhu.getInstance().getConfigManager().isNethandler() && Karhu.getInstance().getConfigManager().isSpoof()) {
			this.kicked = true;
			MiscellaneousAlertPoster.postMisc(
				this.configManager
					.getOwnKeepalives()
					.replaceAll("%player%", this.data.getName())
					.replaceAll("%invalid%", String.valueOf(this.invalidKeepalive.size()))
					.replaceAll("%total%", String.valueOf(this.sentKeep)),
				this.data,
				"Spoof"
			);
			Tasker.run(() -> this.data.getBukkitPlayer().kickPlayer(this.configManager.getCancelOwnKick()));
		}
	}

	private void handleInvalidTransaction(short id, short first) {
		if (this.configManager.isNethandler() && this.configManager.isDelay() && !this.kicked) {
			this.kicked = true;
			Tasker.run(
				() -> this.data
						.getBukkitPlayer()
						.kickPlayer(this.configManager.getOrderKick().replaceAll("%first%", String.valueOf((int)first)).replaceAll("%received%", String.valueOf((int)id)))
			);
			MiscellaneousAlertPoster.postMisc(
				this.configManager
					.getTransactionOrder()
					.replaceAll("%player%", this.data.getName())
					.replaceAll("%first%", String.valueOf((int)first))
					.replaceAll("%sent%", String.valueOf((int)id)),
				this.data,
				"Spoof"
			);
		}
	}

	public NetHandler(KarhuPlayer data) {
		this.data = data;
	}
}
