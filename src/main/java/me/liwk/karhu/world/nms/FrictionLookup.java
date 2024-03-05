/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.world.nms;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.util.KarhuStream;
import me.liwk.karhu.util.mc.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class FrictionLookup {
	private static final Map<Set<Material>, Float> FRICTION_CACHE = new HashMap<>();

	public static float lookup(KarhuPlayer data) {
		double downLookup = data.getClientVersion().getProtocolVersion() < 573 ? 1.0 : 0.5000001;
		Location loc = new Location(
			data.getWorld(),
			(double)MathHelper.floor_double(data.getLastLocation().getX()),
			(double)MathHelper.floor_double(data.getLastLocation().getY() - downLookup),
			(double)MathHelper.floor_double(data.getLastLocation().getZ())
		);
		Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(loc);
		data.setMovementBlock(block);
		if (block != null) {
			Material material = block.getType();
			if (data.getClientVersion().getProtocolVersion() < 47 && MaterialChecks.SLIME.contains(material)) {
				return 0.54600006F;
			} else if (data.getClientVersion().isOlderThan(ClientVersion.V_1_15) && MaterialChecks.HONEY.contains(material)) {
				return 0.72800004F;
			} else if (Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_13) && !data.isNewerThan12() && MaterialChecks.BLUEICE.contains(material)) {
				return 0.89180005F;
			} else {
				Set<Material> list = new KarhuStream<>(FRICTION_CACHE.keySet()).find(s -> s.contains(material));
				return (list == null ? 0.6F : FRICTION_CACHE.get(list)) * 0.91F;
			}
		} else {
			return 0.54600006F;
		}
	}

	static {
		FRICTION_CACHE.put(MaterialChecks.PACKEDICE, 0.98F);
		FRICTION_CACHE.put(MaterialChecks.FROSTEDICE, 0.98F);
		FRICTION_CACHE.put(MaterialChecks.CLEARICE, 0.98F);
		FRICTION_CACHE.put(MaterialChecks.BLUEICE, 0.989F);
		FRICTION_CACHE.put(MaterialChecks.SLIME, 0.8F);
	}
}
