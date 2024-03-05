/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.check.impl.world.block;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import me.liwk.karhu.Karhu;
import me.liwk.karhu.api.check.Category;
import me.liwk.karhu.api.check.CheckInfo;
import me.liwk.karhu.api.check.SubCategory;
import me.liwk.karhu.check.type.PacketCheck;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.event.DigEvent;
import me.liwk.karhu.event.Event;
import me.liwk.karhu.event.HeldItemSlotEvent;
import me.liwk.karhu.event.RespawnEvent;
import me.liwk.karhu.event.SwingEvent;
import me.liwk.karhu.handler.collision.type.MaterialChecks;
import me.liwk.karhu.util.ReflectionUtil;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.mc.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

@CheckInfo(
	name = "FastBreak (C)",
	category = Category.WORLD,
	subCategory = SubCategory.BLOCK,
	experimental = false
)
public final class FastBreakC extends PacketCheck {
	public boolean digStarted;
	public float toolDigEfficiency;
	public float blockHardness;
	public float curBlockDamage;
	public Block block;

	public FastBreakC(KarhuPlayer data, Karhu karhu) {
		super(data, karhu);
	}

	@Override
	public void handle(Event packet) {
		if (!Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_9)) {
			if (packet instanceof SwingEvent) {
				if (this.digStarted && this.block != null) {
					this.blockHardness = Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_16) ? 0.05F : ReflectionUtil.getBlockDurability(this.block);
					boolean canBreak = ReflectionUtil.canDestroyBlock(this.data, this.block);
					this.simulateDig(canBreak);
					this.curBlockDamage += this.toolDigEfficiency / this.blockHardness / (!canBreak ? 100.0F : 30.0F);
				}
			} else if (packet instanceof DigEvent) {
				Player player = this.data.getBukkitPlayer();
				DiggingAction digType = ((DigEvent)packet).getDigType();
				Location blockLocation = new Location(
					player.getWorld(), ((DigEvent)packet).getBlockPos().getX(), ((DigEvent)packet).getBlockPos().getY(), ((DigEvent)packet).getBlockPos().getZ()
				);
				Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(blockLocation);
				if (block == null) {
					return;
				}

				if (Karhu.SERVER_VERSION.getProtocolVersion() >= 47 && block.getType() == Material.BARRIER && this.data.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
					return;
				}

				switch (digType) {
					case START_DIGGING:
						this.digStarted = true;
						this.curBlockDamage = 0.0F;
						this.block = block;
						boolean canBreak = ReflectionUtil.canDestroyBlock(this.data, block);
						this.simulateDig(canBreak);
						this.blockHardness = Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_16) ? 0.05F : ReflectionUtil.getBlockDurability(block);
						this.curBlockDamage += this.toolDigEfficiency / this.blockHardness / (!canBreak ? 100.0F : 30.0F);
						break;
					case FINISHED_DIGGING:
						if (this.digStarted) {
							if (this.curBlockDamage < 0.7F) {
								double speed = (double)(1.0F / this.curBlockDamage);
								this.fail("* Fastbreak (speed edited)\n\n§f* speed: §b" + speed + "\n§f* item: §b" + this.data.getStackInHand().getType(), this.getBanVL(), 600L);
								this.data.setCancelBreak(true);
							}

							this.curBlockDamage = this.toolDigEfficiency = 0.0F;
							this.digStarted = false;
							this.block = null;
						}
						break;
					case CANCELLED_DIGGING:
						this.digStarted = false;
						break;
					case RELEASE_USE_ITEM:
						if (this.digStarted) {
							boolean canDestroyBlock = ReflectionUtil.canDestroyBlock(this.data, block);
							this.simulateDig(canDestroyBlock);
							this.curBlockDamage += Math.abs(this.toolDigEfficiency / this.blockHardness / (!canDestroyBlock ? 100.0F : 30.0F));
						}
						break;
					default:
						if (this.digStarted) {
							boolean destroyBlock = ReflectionUtil.canDestroyBlock(this.data, block);
							this.simulateDig(destroyBlock);
							this.curBlockDamage += Math.abs(this.toolDigEfficiency / this.blockHardness / (!destroyBlock ? 100.0F : 30.0F));
						}
				}
			} else if (packet instanceof RespawnEvent) {
				this.curBlockDamage = 1.1F;
			} else if (packet instanceof HeldItemSlotEvent) {
				this.curBlockDamage = 1.1F;
				this.toolDigEfficiency = 0.0F;
				this.digStarted = false;
				this.block = null;
			}
		}
	}

	private boolean testWater(KarhuPlayer data) {
		CustomLocation location = data.getLocation();
		double d0 = location.getY() + 1.62;
		int i = MathHelper.floor_double(location.getX());
		int j = (int)MathHelper.floor_double_long((double)((float)MathHelper.floor_double(d0)));
		int k = MathHelper.floor_double(location.getZ());
		Location bukkitLocation = new Location(data.getWorld(), (double)i, (double)j, (double)k);
		Block block = Karhu.getInstance().getChunkManager().getChunkBlockAt(bukkitLocation);
		if (block == null) {
			return false;
		} else if (MaterialChecks.WATER.contains(block.getType())) {
			float f = this.testWaterHeight(block.getData()) - 0.11111111F;
			float f1 = (float)(j + 1) - f;
			return d0 < (double)f1;
		} else {
			return false;
		}
	}

	private float testWaterHeight(int i) {
		if (i >= 8) {
			i = 0;
		}

		return (float)(i + 1) / 9.0F;
	}

	private void simulateDig(boolean canBreak) {
		float destroySpeed = this.data.getStackInHand().getType() == Material.AIR ? 1.0F : 1.0F * ReflectionUtil.getDestroySpeed(this.block, this.data);
		if (destroySpeed > 1.0F) {
			int enchLvl = this.data.getStackInHand().getEnchantmentLevel(Enchantment.DIG_SPEED);
			if (enchLvl > 0) {
				float f1 = (float)(enchLvl * enchLvl + 1);
				if (!canBreak && destroySpeed <= 1.0F) {
					destroySpeed += f1 * 0.08F;
				} else {
					destroySpeed += f1;
				}
			}
		}

		if (this.data.getHaste() != 0) {
			destroySpeed *= 1.0F + (float)this.data.getHaste() * 0.2F;
		}

		if (this.data.getFatigue() != 0) {
			if (Karhu.SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_8)) {
				float f1;
				switch (this.data.getFatigue()) {
					case 1:
						f1 = 0.3F;
						break;
					case 2:
						f1 = 0.09F;
						break;
					case 3:
						f1 = 0.0027F;
						break;
					case 4:
					default:
						f1 = 8.1E-4F;
				}

				destroySpeed *= f1;
			} else {
				destroySpeed *= 1.0F - (float)this.data.getFatigue() * 0.2F;
			}
		}

		this.toolDigEfficiency = destroySpeed;
	}
}
