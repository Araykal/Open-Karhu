/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.database.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.check.api.AlertsX;
import me.liwk.karhu.check.api.BanWaveX;
import me.liwk.karhu.check.api.BanX;
import me.liwk.karhu.check.api.Check;
import me.liwk.karhu.check.api.ViolationX;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.database.Storage;
import me.liwk.karhu.manager.ConfigManager;
import me.liwk.karhu.util.MathUtil;
import me.liwk.karhu.util.NetUtil;
import me.liwk.karhu.util.task.Tasker;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;

public class MongoStorage implements Storage {
	private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
			MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	private MongoCollection<ViolationX> loggedViolations;
	private MongoCollection<BanX> loggedBans;
	private MongoCollection<AlertsX> loggedStatus;
	private MongoCollection<BanWaveX> loggedBanwavePlayers;
	private final ConcurrentLinkedQueue<ViolationX> violations = new ConcurrentLinkedQueue<>();
	private final ConcurrentLinkedQueue<BanX> bans = new ConcurrentLinkedQueue<>();
	private final ConcurrentLinkedQueue<BanWaveX> banWaveQueue = new ConcurrentLinkedQueue<>();
	public String host;
	public String database;
	public String username;
	public String password;
	public int port;
	public boolean auth;

	public MongoStorage() {
		ConfigManager cfg = Karhu.getInstance().getConfigManager();
		this.host = cfg.getConfig().getString("mongo.host");
		this.port = cfg.getConfig().getInt("mongo.port");
		this.database = cfg.getConfig().getString("mongo.database");
		this.auth = cfg.getConfig().getBoolean("mongo.authentication.enabled");
		this.username = cfg.getConfig().getString("mongo.authentication.username");
		this.password = cfg.getConfig().getString("mongo.authentication.password");
	}

	@Override
	public void init() {
		MongoClient client;
		if (this.auth) {
			MongoCredential credentials = MongoCredential.createCredential(this.username, this.database, this.password.toCharArray());
			client = new MongoClient(
					new ServerAddress(this.host, this.port), credentials, MongoClientOptions.builder().codecRegistry(this.pojoCodecRegistry).build()
			);
		} else {
			client = new MongoClient(new ServerAddress(this.host, this.port), MongoClientOptions.builder().codecRegistry(this.pojoCodecRegistry).build());
		}

		MongoDatabase mongodb = client.getDatabase(this.database);
		this.loggedViolations = mongodb.getCollection("violations", ViolationX.class);
		this.loggedBans = mongodb.getCollection("bans", BanX.class);
		this.loggedStatus = mongodb.getCollection("status", AlertsX.class);
		this.loggedBanwavePlayers = mongodb.getCollection("banwave", BanWaveX.class);
		new Thread(() -> {
			while(Karhu.getInstance() != null && Karhu.getInstance().getPlug().isEnabled()) {
				try {
					NetUtil.sleep(10000L);
					if (!this.violations.isEmpty() || !this.bans.isEmpty() || !this.banWaveQueue.isEmpty()) {
						if (!this.violations.isEmpty()) {
							try {
								this.loggedViolations.insertMany(new ArrayList<>(this.violations));
							} catch (Exception var4) {
								var4.printStackTrace();
							}

							this.violations.clear();
						}

						if (!this.bans.isEmpty()) {
							try {
								this.loggedBans.insertMany(new ArrayList<>(this.bans));
							} catch (Exception var3xx) {
								var3xx.printStackTrace();
							}

							this.bans.clear();
						}

						if (!this.banWaveQueue.isEmpty()) {
							try {
								this.loggedBanwavePlayers.insertMany(new ArrayList<>(this.banWaveQueue));
							} catch (Exception var2xx) {
								var2xx.printStackTrace();
							}

							this.banWaveQueue.clear();
						}
					}
				} catch (Exception var5) {
					var5.printStackTrace();
				}
			}
		}, "KarhuMongoCommitter").start();
	}

	@Override
	public void addAlert(ViolationX violation) {
		this.violations.add(violation);
	}

	@Override
	public void addBan(BanX ban) {
		this.bans.add(ban);
	}

	@Override
	public void setAlerts(String uuid, int status) {
		this.loggedStatus.replaceOne(Filters.eq("player", uuid), new AlertsX(uuid, status));
	}

	@Override
	public boolean getAlerts(String uuid) {
		AlertsX alertsX = this.loggedStatus.find(Filters.eq("player", uuid)).limit(1).first();
		if (alertsX != null) {
			return MathUtil.getIntAsBoolean(alertsX.status);
		} else {
			this.loggedStatus.replaceOne(Filters.eq("player", uuid), new AlertsX(uuid, 1));
			return true;
		}
	}

	@Override
	public void loadActiveViolations(String uuid, KarhuPlayer data) {
		Tasker.taskAsync(() -> {
			List<ViolationX> violations = new ArrayList<>();
			Map<String, Integer> validVls = new HashMap<>();
			this.loggedViolations.find(Filters.eq("player", uuid)).sort(new Document("time", -1)).forEach((Block<? super ViolationX>) vx -> violations.add(vx));

			for(ViolationX v : violations) {
				if (System.currentTimeMillis() - v.time < 200000L) {
					if (!validVls.containsKey(v.type)) {
						validVls.put(v.type, v.vl);
					} else if (v.vl > validVls.get(v.type)) {
						validVls.replace(v.type, v.vl);
					}
				}
			}

			for(Check c : data.getCheckManager().getChecks()) {
				if (validVls.containsKey(c.getCheckInfo().name())) {
					data.addViolations(c, validVls.get(c.getName()));
					int vl = data.getViolations(c, 100000L);
					data.setCheckVl((double)vl, c);
				}
			}
		});
	}

	@Override
	public List<ViolationX> getViolations(String uuid, Check type, int page, int limit, long from, long to) {
		List<ViolationX> violations = new ArrayList<>();
		this.loggedViolations.find(Filters.eq("player", uuid)).skip(page * limit).limit(limit).sort(new Document("time", -1)).forEach((Block<? super ViolationX>) v -> violations.add(v));
		return violations;
	}

	@Override
	public int getViolationAmount(String uuid) {
		AtomicInteger violations = new AtomicInteger();
		this.loggedViolations.find(Filters.eq("player", uuid)).sort(new Document("time", -1)).forEach((Block<? super ViolationX>) v -> violations.incrementAndGet());
		return violations.get();
	}

	@Override
	public List<ViolationX> getAllViolations(String uuid) {
		List<ViolationX> violations = new ArrayList<>();
		this.loggedViolations.find(Filters.eq("player", uuid)).sort(new Document("time", -1)).forEach((Block<? super ViolationX>) v -> violations.add(v));
		return violations;
	}

	@Override
	public List<String> getBanwaveList() {
		List<String> players = new ArrayList<>();
		this.loggedBanwavePlayers.find().forEach((Block<? super BanWaveX>) huora -> players.add(huora.player));
		return players;
	}

	@Override
	public int getAllViolationsInStorage() {
		List<ViolationX> violations = new ArrayList<>();
		this.loggedViolations.find().forEach((Block<? super ViolationX>) v -> violations.add(v));
		return violations.size();
	}

	@Override
	public List<BanX> getRecentBans() {
		List<BanX> bans = new ArrayList<>();
		this.loggedBans.find().limit(10).forEach((Block<? super BanX>) b -> bans.add(b));
		return bans;
	}

	@Override
	public void purge(String uuid, boolean all) {
		if (all) {
			this.loggedViolations.drop();
		} else {
			this.loggedViolations.deleteMany(Filters.eq("player", uuid));
		}
	}

	@Override
	public void addToBanWave(BanWaveX bwRequest) {
		if (!this.isInBanwave(bwRequest.player)) {
			this.banWaveQueue.add(bwRequest);
		}
	}

	@Override
	public boolean isInBanwave(String uuid) {
		BanWaveX bw = (BanWaveX)this.loggedBanwavePlayers.find(Filters.eq("player", uuid)).first();
		return bw != null;
	}

	@Override
	public void removeFromBanWave(String uuid) {
		this.loggedBanwavePlayers.findOneAndDelete(Filters.eq("player", uuid));
	}


}
