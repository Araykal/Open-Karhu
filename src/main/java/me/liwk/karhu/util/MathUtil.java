/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.liwk.karhu.data.KarhuPlayer;
import me.liwk.karhu.util.evictinglist.EvictingList;
import me.liwk.karhu.util.location.CustomLocation;
import me.liwk.karhu.util.mc.MathHelper;
import me.liwk.karhu.util.mc.axisalignedbb.AxisAlignedBB;
import me.liwk.karhu.util.mc.vec.Vec3;
import me.liwk.karhu.util.mc.vec.Vec3d;
import me.liwk.karhu.util.player.BlockUtil;
import me.liwk.karhu.util.tuple.Tuple;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public final class MathUtil {
	public static final double LN_2 = Math.log(2.0);
	private static final float PI_2 = 1.5707963F;
	private static final Set<Integer> FORWARD_DIRECTION = new HashSet<>(Arrays.asList(0, 45, 360));
	private static final Set<Integer> BACKWARD_DIRECTION = new HashSet<>(Arrays.asList(135, 180));
	private static final Set<Integer> SIDE_DIRECTION = new HashSet<>(Arrays.asList(45, 135, 90));
	public static final Random RANDOM = new Random();

	public static float fastatan2_float(float y, float x) {
		if (x == 0.0F && y == 0.0F) {
			return 0.0F;
		} else {
			float angle;
			if (y >= 0.0F) {
				if (x >= 0.0F) {
					angle = y / (x + y);
				} else {
					angle = 1.0F - x / (-x + y);
				}
			} else if (x < 0.0F) {
				angle = -y / (-x - y) - 2.0F;
			} else {
				angle = x / (x - y) - 1.0F;
			}

			return angle * 1.5707963F;
		}
	}

	public static double fastatan2_double(double y, double x) {
		if (x == 0.0 && y == 0.0) {
			return 0.0;
		} else {
			double angle;
			if (y >= 0.0) {
				if (x >= 0.0) {
					angle = y / (x + y);
				} else {
					angle = 1.0 - x / (-x + y);
				}
			} else if (x < 0.0) {
				angle = -y / (-x - y) - 2.0;
			} else {
				angle = x / (x - y) - 1.0;
			}

			return angle * 1.5707963F;
		}
	}

	public static double getRandomDouble(double maximum, double minimum) {
		Random r = new Random();
		return minimum + (maximum - minimum) * r.nextDouble();
	}

	public static double average(Iterable<? extends Number> iterable) {
		double n = 0.0;
		int n2 = 0;

		for (Number number : iterable) {
			n += number.doubleValue();
			++n2;
		}

		return n / (double)n2;
	}

	public static double stdDev(double average, Iterable<? extends Number> numbers) {
		double stdDev = 0.0;
		int i = 0;

		for (Number number : numbers) {
			stdDev += Math.pow(number.doubleValue() - average, 2.0);
			++i;
		}

		stdDev /= (double)i;
		return Math.sqrt(stdDev);
	}

	public static boolean checkStackIntegrity(ItemStack itemStack) {
		return itemStack != null && itemStack.getType() != Material.AIR;
	}

	public static int getIndex(Set<? extends Object> set, Object value) {
		int result = 0;

		for (Object entry : set) {
			if (entry.equals(value)) {
				return result;
			}

			++result;
		}

		return -1;
	}

	public static <E> E randomElement(Collection<? extends E> collection) {
		if (collection.isEmpty()) {
			return null;
		} else {
			int index = new Random().nextInt(collection.size());
			if (collection instanceof List) {
				return (E) ((List)collection).get(index);
			} else {
				Iterator<? extends E> iter = collection.iterator();

				for (int i = 0; i < index; ++i) {
					iter.next();
				}

				return iter.next();
			}
		}
	}

	public static double getEntropy(Collection<? extends Number> values) {
		double n = (double)values.size();
		if (n < 2.0) {
			return Double.NaN;
		} else {
			Map<Integer, Integer> map = new HashMap<>();
			values.stream().mapToInt(Number::intValue).forEach(value -> map.computeIfAbsent(value, k -> 0));
			double entropy = map.values().stream().mapToDouble(freq -> (double)freq.intValue() / n).map(probability -> probability * log2(probability)).sum();
			return -entropy;
		}
	}

	private static double log2(double n) {
		return Math.log(n) / LN_2;
	}

	public static double offset(Vector from, Vector to) {
		from.setY(0);
		to.setY(0);
		return to.subtract(from).length();
	}

	public static AxisAlignedBB getHitbox(KarhuPlayer data, AxisAlignedBB baseBox) {
		// $QF: Couldn't be decompiled
		// Please report this to the Quiltflower issue tracker, at https://github.com/QuiltMC/quiltflower/issues with a copy of the class file (if you have the rights to distribute it!)
		// java.lang.NullPointerException: Cannot invoke "org.objectweb.asm.Label.resolve(byte[], int)" because "label" is null
		//   at org.objectweb.asm.MethodWriter.visitLabel(MethodWriter.java:1202)
		//   at org.objectweb.asm.tree.LabelNode.accept(LabelNode.java:68)
		//   at org.objectweb.asm.tree.InsnList.accept(InsnList.java:144)
		//   at org.objectweb.asm.tree.MethodNode.accept(MethodNode.java:749)
		//   at org.objectweb.asm.tree.MethodNode.accept(MethodNode.java:647)
		//   at org.objectweb.asm.tree.ClassNode.accept(ClassNode.java:468)
		//   at cuchaz.enigma.utils.AsmUtil.nodeToBytes(AsmUtil.java:10)
		//   at cuchaz.enigma.source.quiltflower.EnigmaContextSource.getInputStream(EnigmaContextSource.java:64)
		//   at cuchaz.enigma.source.quiltflower.EnigmaContextSource$ExternalContextSource.getInputStream(EnigmaContextSource.java:113)
		//   at org.jetbrains.java.decompiler.main.extern.IContextSource.getClassBytes(IContextSource.java:67)
		//   at org.jetbrains.java.decompiler.struct.ContextUnit.getClassBytes(ContextUnit.java:89)
		//   at org.jetbrains.java.decompiler.struct.StructContext.tryLoadClass(StructContext.java:110)
		//   at org.jetbrains.java.decompiler.struct.StructContext.lambda$getClass$0(StructContext.java:87)
		//
		// Bytecode:
		// 00: aload 0
		// 01: invokevirtual me/liwk/karhu/data/KarhuPlayer.isNewerThan8 ()Z
		// 04: ifeq 18
		// 07: aload 1
		// 08: ldc2_w 0.0305
		// 0b: ldc2_w 0.0305
		// 0e: ldc2_w 0.0305
		// 11: invokevirtual me/liwk/karhu/util/mc/axisalignedbb/AxisAlignedBB.expand (DDD)Lme/liwk/karhu/util/mc/axisalignedbb/AxisAlignedBB;
		// 14: astore 1
		// 15: goto 46
		// 18: aload 0
		// 19: getfield me/liwk/karhu/data/KarhuPlayer.lastPos I
		// 1c: ifgt 38
		// 1f: aload 0
		// 20: invokevirtual me/liwk/karhu/data/KarhuPlayer.getMoveTicks ()I
		// 23: bipush 1
		// 24: if_icmple 38
		// 27: aload 1
		// 28: ldc2_w 0.1
		// 2b: ldc2_w 0.1
		// 2e: ldc2_w 0.1
		// 31: invokevirtual me/liwk/karhu/util/mc/axisalignedbb/AxisAlignedBB.expand (DDD)Lme/liwk/karhu/util/mc/axisalignedbb/AxisAlignedBB;
		// 34: astore 1
		// 35: goto 46
		// 38: aload 1
		// 39: ldc2_w 0.1305
		// 3c: ldc2_w 0.1305
		// 3f: ldc2_w 0.1305
		// 42: invokevirtual me/liwk/karhu/util/mc/axisalignedbb/AxisAlignedBB.expand (DDD)Lme/liwk/karhu/util/mc/axisalignedbb/AxisAlignedBB;
		// 45: astore 1
		// 46: aload 1
		// 47: areturn
        return baseBox;
    }

	public static AxisAlignedBB getHitboxLenient(KarhuPlayer data, AxisAlignedBB baseBox) {
		if (data.isNewerThan8()) {
			baseBox = baseBox.expand(0.1, 0.1, 0.1);
		} else if (data.getMoveTicks() <= 2) {
			baseBox = baseBox.expand(0.18, 0.18, 0.18);
		} else {
			baseBox = baseBox.expand(0.15, 0.15, 0.15);
		}

		return baseBox;
	}

	public static double square(double number) {
		return FastMath.pow(number, 2.0);
	}

	public static double varianceSquared(Number value, Iterable<? extends Number> numbers) {
		double variance = 0.0;
		int i = 0;

		for (Number number : numbers) {
			variance += FastMath.pow(number.doubleValue() - value.doubleValue(), 2.0);
			++i;
		}

		return variance / (double)(i - 1);
	}

	public static double getDifference(Iterable<? extends Number> list) {
		double i = 0.0;
		double p = -1.0;
		int count = 0;

		for (Number z : list) {
			if (p != -1.0) {
				i += Math.abs(p - z.doubleValue());
			}

			p = z.doubleValue();
			++count;
		}

		return i / (double)count;
	}

	public static double getOscillation(Iterable<? extends Number> samples) {
		return highest(samples) - lowest(samples);
	}

	public static double difference(Iterable<? extends Number> numbers) {
		double total = 0.0;
		double lastNum = 0.0;
		int i = 0;

		for (Number number : numbers) {
			total += Math.abs(number.doubleValue() - lastNum);
			lastNum = number.doubleValue();
			++i;
		}

		return total / (double)i;
	}

	public static int getV(Deque<Integer> list) {
		int f = getNumbers(list, 1) + 1;
		int s = getNumbers(list, 2) + 1;
		int t = getNumbers(list, 3) + 1;
		int v = (f + s) / t * 50;
		return v / list.size();
	}

	public static int getNumbers(Deque<Integer> list, int num) {
		int amount = 0;

		for (int i : list) {
			if (i == num) {
				++amount;
			}
		}

		return amount;
	}

	public static int[] getNumbersArray(Deque<Integer> samples, int size) {
		int[] counter = new int[size];

		for (int i : samples) {
			if (i > 0 && i <= size) {
				counter[i - 1]++;
			}
		}

		return counter;
	}

	public static int getOutliers(Deque<Integer> list) {
		return (int)list.stream().filter(delay -> delay > 3).count();
	}

	public static int getRepeated(Deque<Integer> list) {
		return (int)list.stream().distinct().count();
	}

	public static int getDuplicatedNumbers(Deque<Integer> list) {
		int amount = 0;
		Iterator var2 = list.iterator();

		while (var2.hasNext()) {
			double i = (double)((Integer)var2.next()).intValue();
			Iterator var5 = list.iterator();

			while (var5.hasNext()) {
				double ii = (double)((Integer)var5.next()).intValue();
				if (i == ii) {
					++amount;
				}
			}
		}

		return amount / list.size();
	}

	public static double getStuff(Iterable<Integer> numbers) {
		int previous = -1;
		double n0 = 0.0;

		double z;
		for (Iterator statistic = numbers.iterator(); statistic.hasNext(); n0 += z / 2.0) {
			z = (double)((Integer)statistic.next()).intValue();
		}

		double statistic = 0.0;

		for (int number : numbers) {
			if (previous == -1) {
				previous = number;
			} else {
				statistic += n0 / ((n0 - 1.0) * (n0 - 2.0)) * (double)number;
			}
		}

		return (statistic - 2.0) * 2.0;
	}

	public static int getW(Deque<Integer> list) {
		int f = getNumbers(list, 1) + 1;
		int s = getNumbers(list, 2) + 1;
		int t = getNumbers(list, 3) + 1;
		int f1 = getNumbers(list, 4) + 1;
		int f2 = getNumbers(list, 5) + 1;
		int w = (f + s) / (t + f1 + f2) * 50;
		return w / list.size();
	}

	public static double getRatio(Deque<Integer> list) {
		return ((double)getNumbers(list, 1) + 1.0) / ((double)getNumbers(list, 3) + 1.0);
	}

	public static double[] dequeTranslator(Collection<? extends Number> numbers) {
		return numbers.stream().mapToDouble(Number::doubleValue).toArray();
	}

	public static final String format(int places, Object obj) {
		return String.format("%." + places + "f", obj);
	}

	public static double hypotFast(double... numbers) {
		double squaredSum = 0.0;

		for (double number : numbers) {
			squaredSum += FastMath.pow(number, 2.0);
		}

		return sqrt(squaredSum);
	}

	public static double power(double number) {
		return FastMath.pow(number, 2.0);
	}

	public static <T> Stream<T> stream(T... array) {
		return Arrays.stream(array);
	}

	public static <T> T firstNonNull(@Nullable T t, @Nullable T t2) {
		return (T)(t != null ? t : t2);
	}

	public static <T> Queue<T> trim(Queue<T> queue, int n) {
		for (int i = queue.size(); i > n; --i) {
			queue.poll();
		}

		return queue;
	}

	public static double trimDouble(int degree, double d) {
		StringBuilder format = new StringBuilder("#.#");

		for (int i = 1; i < degree; ++i) {
			format.append("#");
		}

		DecimalFormat twoDForm = new DecimalFormat(format.toString());
		return Double.parseDouble(twoDForm.format(d).replaceAll(",", "."));
	}

	public static double getStandardDeviation(Collection<? extends Number> doubles) {
		double average = 0.0;
		double std = 0.0;
		double size = (double)doubles.size();

		for (Number number : doubles) {
			average += number.doubleValue();
		}

		double nigger = average / size;

		for (Number doubler : doubles) {
			std += FastMath.pow(doubler.doubleValue() - nigger, 2.0);
		}

		return FastMath.sqrt(std / size);
	}

	public static double getStandardDeviation(double[] doubles) {
		double average = 0.0;
		double std = 0.0;
		double size = (double)doubles.length;

		for (double number : doubles) {
			average += number;
		}

		double nigger = average / size;

		for (double doubler : doubles) {
			std += FastMath.pow(doubler - nigger, 2.0);
		}

		return FastMath.sqrt(std / size);
	}

	public static double getVariance(Collection<? extends Number> data) {
		int count = 0;
		double sum = 0.0;
		double variance = 0.0;

		for (Number number : data) {
			sum += number.doubleValue();
			++count;
		}

		double average = sum / (double)count;

		for (Number number : data) {
			variance += FastMath.pow(number.doubleValue() - average, 2.0);
		}

		return variance;
	}

	public static double getAverage(Collection<? extends Number> values) {
		double average = 0.0;
		double size = (double)values.size();

		for (Number number : values) {
			average += number.doubleValue();
		}

		return average / size;
	}

	public static double getSkewness(Collection<? extends Number> data) {
		double sum = 0.0;
		int count = 0;
		List<Double> numbers = Lists.newArrayList();

		for (Number number : data) {
			sum += number.doubleValue();
			++count;
			numbers.add(number.doubleValue());
		}

		Collections.sort(numbers);
		double mean = sum / (double)count;
		double median = count % 2 != 0 ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2.0;
		double variance = getVariance(data);
		return 3.0 * (mean - median) / variance;
	}

	public static boolean isReallyPlacingBlock(Vector block, Vector player, BlockFace face) {
		switch (face) {
			case UP:
				return true;
			case DOWN:
				double limitDown = block.getY() - 0.03;
				return player.getY() < limitDown;
			case NORTH:
				double limitNorth = block.getZ() + 0.03;
				return player.getZ() < limitNorth;
			case SOUTH:
				double limitSouth = block.getZ() - 0.03;
				return player.getZ() > limitSouth;
			case EAST:
				double limitEast = block.getX() + 0.03;
				return player.getX() < limitEast;
			case WEST:
				double limitWest = block.getX() - 0.03;
				return player.getX() > limitWest;
			default:
				return true;
		}
	}

	public static double getKurtosis(Collection<? extends Number> data) {
		double sum = 0.0;
		int count = 0;

		for (Number number : data) {
			sum += number.doubleValue();
			++count;
		}

		if ((double)count < 3.0) {
			return 0.0;
		} else {
			double efficiencyFirst = (double)count * ((double)count + 1.0) / (((double)count - 1.0) * ((double)count - 2.0) * ((double)count - 3.0));
			double efficiencySecond = 3.0 * FastMath.pow((double)count - 1.0, 2.0) / (((double)count - 2.0) * ((double)count - 3.0));
			double average = sum / (double)count;
			double variance = 0.0;
			double varianceSquared = 0.0;

			for (Number number : data) {
				variance += FastMath.pow(average - number.doubleValue(), 2.0);
				varianceSquared += FastMath.pow(average - number.doubleValue(), 4.0);
			}

			return efficiencyFirst * (varianceSquared / FastMath.pow(variance / sum, 2.0)) - efficiencySecond;
		}
	}

	public static double getKurtosis2(Collection<Double> data) {
		double size = (double)data.size();
		if (size < 3.0) {
			return Double.NaN;
		} else {
			double black = data.stream().mapToDouble(value -> value).average().getAsDouble();
			double dark = getStandardDeviation(data.stream().mapToDouble(value -> value).toArray());
			AtomicDouble atomicDouble = new AtomicDouble(0.0);
			data.forEach(value -> atomicDouble.getAndAdd(FastMath.pow(value - black, 4.0)));
			return size * (size + 1.0) / (size - 1.0) * (size - 2.0) * (size - 3.0) * atomicDouble.get() / FastMath.pow(dark, 4.0)
				- 3.0 * FastMath.pow(size - 1.0, 2.0) / (size - 2.0) * (size - 3.0);
		}
	}

	public static double getMedian(List<Double> data) {
		return data.size() % 2 == 0 ? (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2.0 : data.get(data.size() / 2);
	}

	public static double getCPS(Collection<? extends Number> data) {
		double average = data.stream().mapToDouble(Number::doubleValue).average().orElse(0.0);
		return 20.0 / average;
	}

	public static double distanceToHorizontalCollision(double position) {
		double dividedPos = Math.abs(position) % 0.0015625;
		return Math.min(dividedPos, Math.abs(dividedPos - 0.0015625));
	}

	public static double getCPSLong(Collection<? extends Number> values) {
		return 1000.0 / getAverage(values);
	}

	public static float getMoveAngle(CustomLocation from, CustomLocation to, boolean clamp) {
		double dx = to.getX() - from.getX();
		double dz = to.getZ() - from.getZ();
		float moveAngle = (float)(Math.toDegrees(FastMath.atan2(dz, dx)) - 90.0);
		return clamp ? Math.abs(wrapAngleTo180_float(moveAngle - to.getYaw())) : Math.abs(moveAngle - to.getYaw());
	}

	public static Vector getMoveChange(CustomLocation from, CustomLocation to, KarhuPlayer data) {
		float friction = data.isLastOnGroundPacket() ? data.getCurrentFriction() : 0.91F;
		double dx = (to.getX() - from.getX()) / (double)friction;
		double dz = (to.getZ() - from.getZ()) / (double)friction;
		if (data.isJumped()) {
			float f = to.yaw * (float) Math.PI / 180.0F;
			dx += (double)(MathHelper.sin(f) * 0.2F);
			dz -= (double)(MathHelper.cos(f) * 0.2F);
		}

		dx -= data.deltas.lastDX;
		dz -= data.deltas.lastDZ;
		return new Vector(dx, 0.0, dz);
	}

	public static float[] getStrafeForward(CustomLocation from, CustomLocation to, KarhuPlayer data) {
		float forward = 0.0F;
		float strafe = 0.0F;
		float friction = data.isLastOnGroundPacket() ? data.getCurrentFriction() : 0.91F;
		double dx = to.getX() - from.getX();
		double dz = to.getZ() - from.getZ();
		dx /= (double)friction;
		dz /= (double)friction;
		if (data.isJumped()) {
			float f = to.yaw * (float) Math.PI / 180.0F;
			dx += (double)(MathHelper.sin(f) * 0.2F);
			dz -= (double)(MathHelper.cos(f) * 0.2F);
		}

		dx -= data.deltas.lastDX;
		dz -= data.deltas.lastDZ;
		Vector move = new Vector(dx, 0.0, dz);
		if (move.length() < 0.01) {
			return new float[]{0.0F, 0.0F};
		} else {
			move.normalize();
			Vector angle = new Vector(-Math.sin(Math.toRadians((double)to.getYaw())), 0.0, Math.cos(Math.toRadians((double)to.getYaw())));
			double degree = Math.toDegrees((double)angle.angle(move));

			for (int direction : FORWARD_DIRECTION) {
				double diff = Math.abs((double)direction - degree);
				if (diff < 5.0) {
					forward = 1.0F;
					break;
				}
			}

			for (int direction : BACKWARD_DIRECTION) {
				double diff = Math.abs((double)direction - degree);
				if (diff < 5.0) {
					forward = -1.0F;
					break;
				}
			}

			for (int direction : SIDE_DIRECTION) {
				double diff = Math.abs((double)direction - degree);
				if (diff < 5.0) {
					strafe = angle.getX() * move.getZ() - angle.getZ() * move.getX() > 0.0 ? 1.0F : -1.0F;
					break;
				}
			}

			return new float[]{forward, strafe};
		}
	}

	public static float getMoveAngleNoAbs(CustomLocation from, CustomLocation to) {
		double dx = to.getX() - from.getX();
		double dz = to.getZ() - from.getZ();
		float moveAngle = (float)(Math.toDegrees(FastMath.atan2(dz, dx)) - 90.0);
		return wrapAngleTo180_float(moveAngle - to.getYaw());
	}

	public static Vector getVectorSpeed(CustomLocation from, CustomLocation to) {
		return new Vector(to.getX() - from.getX(), 0.0, to.getZ() - from.getZ());
	}

	public static Vector getDirection(KarhuPlayer data) {
		return new Vector(
			(double)(-MathHelper.sin(data.getLocation().getYaw() * (float) Math.PI / 180.0F)) * 1.0 * 0.5,
			0.0,
			(double)MathHelper.cos(data.getLocation().getYaw() * (float) Math.PI / 180.0F) * 1.0 * 0.5
		);
	}

	public static double getDirectionShit(Location from, Location to) {
		if (from != null && to != null) {
			double difX = to.getX() - from.getX();
			double difZ = to.getZ() - from.getZ();
			return (double)((float)(FastMath.atan2(difZ, difX) * 180.0 / Math.PI - 90.0));
		} else {
			return 0.0;
		}
	}

	public static double lowestAbs(Iterable<? extends Number> iterable) {
		Double value = null;

		for (Number n : iterable) {
			if (value == null || !(Math.abs(n.doubleValue()) >= Math.abs(value))) {
				value = n.doubleValue();
			}
		}

		return firstNonNull(value, 0.0);
	}

	public static double lowest(Iterable<? extends Number> numbers) {
		double lowest = Double.MAX_VALUE;
		int i = 0;

		for (Number number : numbers) {
			if (number.doubleValue() < lowest) {
				lowest = number.doubleValue();
			}

			++i;
		}

		return lowest;
	}

	public static double highest(Iterable<? extends Number> numbers) {
		double lowest = 0.0;
		int i = 0;

		for (Number number : numbers) {
			if (number.doubleValue() > lowest) {
				lowest = number.doubleValue();
			}

			++i;
		}

		return lowest;
	}

	public static float averageFloat(List<Float> list) {
		float avg = 0.0F;

		for (float value : list) {
			avg += value;
		}

		return list.size() > 0 ? avg / (float)list.size() : 0.0F;
	}

	public static float averageLong(Deque<Long> list) {
		float avg = 0.0F;

		float value;
		for (Iterator var2 = list.iterator(); var2.hasNext(); avg += value) {
			value = (float)((Long)var2.next()).longValue();
		}

		return list.size() > 0 ? avg / (float)list.size() : 0.0F;
	}

	public static Double findMin(EvictingList<Double> list) {
		if (list != null && list.size() != 0) {
			EvictingList<Double> sortedlist = new EvictingList<>(list.size());
			Collections.sort(sortedlist);
			return sortedlist.get(0);
		} else {
			return Double.MAX_VALUE;
		}
	}

	public static Double findMax(EvictingList<Double> list) {
		if (list != null && list.size() != 0) {
			EvictingList<Double> sortedlist = new EvictingList<>(list.size());
			Collections.sort(sortedlist);
			return sortedlist.get(sortedlist.size() - 1);
		} else {
			return Double.MIN_VALUE;
		}
	}

	public static int getPingInTicks(long ping) {
		return (int)Math.floor((double)ping / 50.0);
	}

	public static int getPingToTimer(long ping) {
		return (int)ping / 10000;
	}

	public static double deviation(Iterable<? extends Number> iterable) {
		return FastMath.sqrt(deviationSquared(iterable));
	}

	public static boolean onGround(double coord) {
		return coord % 0.015625 == 0.0;
	}

	public static double deviationSquared(Iterable<? extends Number> iterable) {
		double n = 0.0;
		int n2 = 0;

		for (Number number : iterable) {
			n += number.doubleValue();
			++n2;
		}

		double n3 = n / (double)n2;
		double n4 = 0.0;

		for (Number number : iterable) {
			n4 += FastMath.pow(number.doubleValue() - n3, 2.0);
		}

		return n4 == 0.0 ? 0.0 : n4 / (double)(n2 - 1);
	}

	public static double sqrt(double number) {
		return FastMath.sqrt(number);
	}

	public static double horizontalDistance(Vector vector1, Vector vector2) {
		return Math.sqrt(NumberConversions.square(vector1.getX() - vector2.getX()) + NumberConversions.square(vector1.getZ() - vector2.getZ()));
	}

	public static double verticalDistance(Vector vector1, Vector vector2) {
		return Math.sqrt(NumberConversions.square(vector1.getY() - vector2.getY()));
	}

	public static float f(List<Float> list) {
		float n = 0.0F;
		Iterator<Float> iterator = list.iterator();
		if (iterator.hasNext()) {
			n += iterator.next();

			try {
				if (iterator.toString() == null) {
					return 0.0F;
				}
			} catch (IllegalArgumentException var5) {
				var5.printStackTrace();
			}
		}

		try {
			if (list.size() > 0) {
				return n / (float)list.size();
			}
		} catch (IllegalArgumentException var4) {
			var4.printStackTrace();
		}

		return 0.0F;
	}

	public static double clamp180(double theta) {
		theta %= 360.0;
		if (theta >= 180.0) {
			theta -= 360.0;
		}

		if (theta < -180.0) {
			theta += 360.0;
		}

		return theta;
	}

	public static double getDirection(Location from, Location to) {
		if (from != null && to != null) {
			double difX = to.getX() - from.getX();
			double difZ = to.getZ() - from.getZ();
			return (double)((float)(FastMath.atan2(difZ, difX) * 180.0 / Math.PI - 90.0));
		} else {
			return 0.0;
		}
	}

	public static double getDirection(Location from, Vector vector) {
		if (from != null && vector != null) {
			double difX = vector.getX() - from.getX();
			double difZ = vector.getZ() - from.getZ();
			return (double)((float)(FastMath.atan2(difZ, difX) * 180.0 / Math.PI - 90.0));
		} else {
			return 0.0;
		}
	}

	public static double getDirection(CustomLocation location, Vector vector) {
		double difX = vector.getX() - location.getX();
		double difZ = vector.getZ() - location.getZ();
		return (double)((float)(FastMath.atan2(difZ, difX) * 180.0 / Math.PI - 90.0));
	}

	public static Vec3 getPositionEyes(double x, double y, double z, float eyeHeight) {
		return new Vec3(x, y + (double)eyeHeight, z);
	}

	public static int floor_double(double value) {
		int i = (int)value;
		return value < (double)i ? i - 1 : i;
	}

	public static Vec3 getVectorForRotation(float pitch, float yaw, KarhuPlayer data) {
		if (!data.isNewerThan12()) {
			float f = MathHelper.cos(-yaw * (float) (Math.PI / 180.0) - (float) Math.PI);
			float f1 = MathHelper.sin(-yaw * (float) (Math.PI / 180.0) - (float) Math.PI);
			float f2 = -MathHelper.cos(-pitch * (float) (Math.PI / 180.0));
			float f3 = MathHelper.sin(-pitch * (float) (Math.PI / 180.0));
			return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
		} else {
			float f = pitch * (float) (Math.PI / 180.0);
			float f1 = -yaw * (float) (Math.PI / 180.0);
			float f2 = MathHelper.cos(f1);
			float f3 = MathHelper.sin(f1);
			float f4 = MathHelper.cos(f);
			float f5 = MathHelper.sin(f);
			return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
		}
	}

	public static Vec3d getLook3d(float partialTicks, KarhuPlayer karhuPlayer) {
		if (partialTicks == 1.0F) {
			return getVectorForRotation3d(karhuPlayer.getLocation().getPitch(), karhuPlayer.getLocation().getYaw());
		} else {
			float f = karhuPlayer.getLastLocation().getPitch() + (karhuPlayer.getLocation().getPitch() - karhuPlayer.getLastLocation().getPitch()) * partialTicks;
			float f1 = karhuPlayer.getLastLocation().getYaw() + (karhuPlayer.getLocation().getYaw() - karhuPlayer.getLastLocation().getYaw()) * partialTicks;
			return getVectorForRotation3d(f, f1);
		}
	}

	public static Vec3d getVectorForRotation3d(float pitch, float yaw) {
		float f = MathHelper.cos(-yaw * (float) (Math.PI / 180.0) - (float) Math.PI);
		float f1 = MathHelper.sin(-yaw * (float) (Math.PI / 180.0) - (float) Math.PI);
		float f2 = -MathHelper.cos(-pitch * (float) (Math.PI / 180.0));
		float f3 = MathHelper.sin(-pitch * (float) (Math.PI / 180.0));
		return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
	}

	public static AxisAlignedBB getEntityBoundingBox(Location l) {
		return getEntityBoundingBox(l.getX(), l.getY(), l.getZ());
	}

	public static AxisAlignedBB getEntityBoundingBox(double x, double y, double z) {
		float f = 0.3F;
		float f1 = 1.8F;
		return new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f);
	}

	public static double getGcd(double current, double previous) {
		if (previous > current) {
			double temp = current;
			current = previous;
			previous = temp;
		}

		while (previous > 0.001) {
			double temp = current % previous;
			current = previous;
			previous = temp;
		}

		return current;
	}

	public static float getGcd(float current, float previous) {
		if (previous > current) {
			float temp = current;
			current = previous;
			previous = temp;
		}

		while (previous > 0.001F) {
			float temp = current % previous;
			current = previous;
			previous = temp;
		}

		return current;
	}

	public static double calculateGcd(double a, double b) {
		if (a == 0.0) {
			return b;
		} else if (!(a > Float.MAX_VALUE) && !(b > Float.MAX_VALUE) && !(a < Float.MIN_VALUE) && !(b < Float.MIN_VALUE)) {
			int quotient = calculateWholeQuotient(b, a);
			double remainder = (b / a - (double)quotient) * a;
			if (Math.abs(remainder) < Math.max(a, b) * 0.001) {
				remainder = 0.0;
			}

			return calculateGcd(remainder, a);
		} else {
			return 0.0;
		}
	}

	public static int calculateWholeQuotient(double dividend, double divisor) {
		double result = dividend / divisor;
		double remainder = Math.max(dividend, divisor) * 0.001;
		return (int)(result + remainder);
	}

	public static double gcd(double a, double b) {
		if (a == 0.0) {
			return b;
		} else if (Math.abs(a) >= Float.MAX_VALUE || Math.abs(b) >= Float.MAX_VALUE) {
			return 0.0;
		} else if (a < b) {
			return gcd(b, a);
		} else {
			return Math.abs(b) < 0.001 ? a : gcd(b, a - (double)MathHelper.floor(a / b) * b);
		}
	}

	public static float gcdFloat(float a, float b) {
		if (a == 0.0F) {
			return b;
		} else if (Math.abs(a) >= Float.MAX_VALUE || Math.abs(b) >= Float.MAX_VALUE) {
			return 0.0F;
		} else if (a < b) {
			return gcdFloat(b, a);
		} else {
			return Math.abs(b) < 0.001F ? a : gcdFloat(b, a - (float)MathHelper.floor_float(a / b) * b);
		}
	}

	public static double gcdTest(double a, double b) {
		if (a == 0.0) {
			return b;
		} else if (Math.abs(a) >= Float.MAX_VALUE || Math.abs(b) >= Float.MAX_VALUE) {
			return 0.0;
		} else if (a < b) {
			return gcdTest(b, a);
		} else {
			return Math.abs(b) < 0.001F ? a : gcdTest(b, a - a / b * b);
		}
	}

	public static float gcdTestFloat(float a, float b) {
		if (a == 0.0F) {
			return b;
		} else if (a >= Float.MAX_VALUE || b >= Float.MAX_VALUE || a <= Float.MIN_VALUE || b <= Float.MIN_VALUE) {
			return 0.0F;
		} else if (a < b) {
			return gcdTestFloat(b, a);
		} else {
			return Math.abs(b) < 0.001F ? a : gcdTestFloat(b, a - a / b * b);
		}
	}

	public static float absFloat(float a) {
		return a <= 0.0F ? 0.0F - a : a;
	}

	public static double absDouble(double a) {
		return a <= 0.0 ? 0.0 - a : a;
	}

	public static double trim(int degree, double d) {
		String format = "#.#";

		for (int i = 1; i < degree; ++i) {
			format = format + "#";
		}

		DecimalFormat twoDForm = new DecimalFormat(format);
		return Double.parseDouble(twoDForm.format(d).replaceAll(",", "."));
	}

	public static float trimFloat(int degree, float d) {
		StringBuilder format = new StringBuilder("#.#");

		for (int i = 1; i < degree; ++i) {
			format.append("#");
		}

		DecimalFormat twoDForm = new DecimalFormat(format.toString());
		return Float.parseFloat(twoDForm.format((double)d).replaceAll(",", "."));
	}

	public static String parseVersion(ClientVersion ver) {
		return ver.toString().replaceAll("_", ".").replaceAll("v.", "");
	}

	public static <T extends Number> T getMode(Collection<T> collect) {
		Map<T, Integer> repeated = new HashMap<>();

		for (T c : collect) {
			int number = repeated.getOrDefault(c, 0);
			repeated.put(c, number + 1);
		}

		return repeated.keySet()
			.stream()
			.map(key -> new Tuple<>(key, repeated.get(key)))
			.max(Comparator.comparing(Tuple::b, Comparator.naturalOrder()))
			.orElseThrow(NullPointerException::new)
			.a();
	}

	private static long getDelta(long alpha, long beta) {
		return alpha % beta;
	}

	public static float[] getRotationFromPosition(CustomLocation playerLocation, CustomLocation targetLocation) {
		double xDiff = targetLocation.getX() - playerLocation.getX();
		double zDiff = targetLocation.getZ() - playerLocation.getZ();
		double yDiff = targetLocation.getY() - (playerLocation.getY() + 0.12);
		double dist = sqrt(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float)(FastMath.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0F;
		float pitch = (float)(-(FastMath.atan2(yDiff, dist) * 180.0 / Math.PI));
		return new float[]{yaw, pitch};
	}

	public static float getRotationYaw(double mx, double mz, float yaw) {
		float yaw2 = (float)(Math.atan2(mz, mx) * 180.0 / Math.PI) - 90.0F;
		yaw2 -= yaw;

		while (yaw2 > 360.0F) {
			yaw2 -= 360.0F;
		}

		while (yaw2 < 0.0F) {
			yaw2 += 360.0F;
		}

		return yaw2;
	}

	public static double pingFormula(long ping) {
		return Math.ceil((double)(ping + 5L) / 50.0);
	}

	public static double invSqrt(double x) {
		double xhalf = 0.5 * x;
		long i = Double.doubleToLongBits(x);
		i = 6910470738111508698L - (i >> 1);
		x = Double.longBitsToDouble(i);
		return x * (1.5 - xhalf * x * x);
	}

	public static double hypotNEW(double... value) {
		double total = 0.0;

		for (double val : value) {
			total += val * val;
		}

		return FastMath.sqrt(total);
	}

	public static double hypot(double... value) {
		double total = 0.0;

		for (double val : value) {
			total += val * val;
		}

		return FastMath.sqrt(total);
	}

	public static float hypot(float... value) {
		float total = 0.0F;

		for (float val : value) {
			total += val * val;
		}

		return (float)FastMath.sqrt((double)total);
	}

	public static float round(float value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal((double)value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.floatValue();
		}
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
	}

	public static double roundDown(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_DOWN);
			return bd.doubleValue();
		}
	}

	public static float roundFloat(float value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal((double)value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.floatValue();
		}
	}

	public static float round(float value, int places, RoundingMode mode) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal((double)value);
			bd = bd.setScale(places, mode);
			return bd.floatValue();
		}
	}

	public static float round(float value) {
		BigDecimal bd = new BigDecimal((double)value);
		bd = bd.setScale(0, RoundingMode.UP);
		return bd.floatValue();
	}

	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		float distance = Math.abs(angle1 - angle2) % 360.0F;
		if (distance > 180.0F) {
			distance = 360.0F - distance;
		}

		return distance;
	}

	public static float getAngleDiff(float a, float b) {
		float diff = Math.abs(a - b);
		float altDiff = b + 360.0F - a;
		float altAltDiff = a + 360.0F - b;
		if (altDiff < diff) {
			diff = altDiff;
		}

		if (altAltDiff < diff) {
			diff = altAltDiff;
		}

		return diff;
	}

	public static double getAngleDistance(double alpha, double beta) {
		double abs = Math.abs(alpha % 360.0 - beta % 360.0);
		return Math.abs(Math.min(360.0 - abs, abs));
	}

	public static float getAngleDistance(float alpha, float beta) {
		float abs = Math.abs(alpha % 360.0F - beta % 360.0F);
		return Math.abs(Math.min(360.0F - abs, abs));
	}

	public static List<Double> calculateDelta(List<Double> doubleList) {
		if (doubleList.size() <= 1) {
			throw new IllegalArgumentException("The list must contain 2 or more elements in order to calculate delta");
		} else {
			List<Double> out = new ArrayList<>();

			for (int i = 1; i <= doubleList.size() - 1; ++i) {
				out.add(doubleList.get(i) - doubleList.get(i - 1));
			}

			return out;
		}
	}

	public static List<Double> toDoubleList(List<Float> floatList) {
		return floatList.stream().map(e -> (double)e.floatValue()).collect(Collectors.toList());
	}

	public static double mean(List<Double> angles) {
		return angles.stream().mapToDouble(e -> e).sum() / (double)angles.size();
	}

	public static double stddev(List<Double> angles) {
		double mean = mean(angles);
		double output = 0.0;

		for (double angle : angles) {
			output += FastMath.pow(angle - mean, 2);
		}

		return output / (double)angles.size();
	}

	public static double euclideanDistance(double[] vectorA, double[] vectorB) {
		validateDimension("Two vectors need to have exact the same dimension", vectorA, vectorB);
		double dist = 0.0;

		for (int i = 0; i <= vectorA.length - 1; ++i) {
			dist += FastMath.pow(vectorA[i] - vectorB[i], 2);
		}

		return FastMath.sqrt(dist);
	}

	public static List<Double> toList(double[] doubleArray) {
		return Arrays.asList(ArrayUtils.toObject(doubleArray));
	}

	public static double[] toArray(List<Double> doubleList) {
		return doubleList.stream().mapToDouble(e -> e).toArray();
	}

	public static double[] randomArray(int length) {
		double[] randomArray = new double[length];
		applyFunc(randomArray, e -> ThreadLocalRandom.current().nextDouble());
		return randomArray;
	}

	public static void applyFunc(double[] doubleArray, Function<Double, Double> func) {
		for (int i = 0; i <= doubleArray.length - 1; ++i) {
			doubleArray[i] = func.apply(doubleArray[i]);
		}
	}

	public static double[] add(double[] vectorA, double[] vectorB) {
		validateDimension("Two vectors need to have exact the same dimension", vectorA, vectorB);
		double[] output = new double[vectorA.length];

		for (int i = 0; i <= vectorA.length - 1; ++i) {
			output[i] = vectorA[i] + vectorB[i];
		}

		return output;
	}

	public static double[] subtract(double[] vectorA, double[] vectorB) {
		validateDimension("Two vectors need to have exact the same dimension", vectorA, vectorB);
		return add(vectorA, opposite(vectorB));
	}

	public static double[] opposite(double[] vector) {
		return multiply(vector, -1.0);
	}

	public static double[] multiply(double[] vector, double factor) {
		double[] output = (double[])vector.clone();
		applyFunc(output, e -> e * factor);
		return output;
	}

	public static double normalize(double value, double min, double max) {
		return (value - min) / (max - min);
	}

	public static double round(double value, int precision, RoundingMode mode) {
		return BigDecimal.valueOf(value).round(new MathContext(precision, mode)).doubleValue();
	}

	public static double roundBD(double value, int places, RoundingMode mode) {
		if (places < 0) {
			throw new IllegalArgumentException();
		} else {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, mode);
			return bd.doubleValue();
		}
	}

	public static double round(double value) {
		return value - value % 1000.0;
	}

	private static void validateDimension(String message, double[]... vectors) {
		for (int i = 0; i <= vectors.length - 1; ++i) {
			if (vectors[0].length != vectors[i].length) {
				throw new IllegalArgumentException(message);
			}
		}
	}

	public static float wrapAngleTo180_float(float value) {
		value %= 360.0F;
		if (value >= 180.0F) {
			value -= 360.0F;
		}

		if (value < -180.0F) {
			value += 360.0F;
		}

		return value;
	}

	public static double wrapAngleTo180_double(double value) {
		value %= 360.0;
		if (value >= 180.0) {
			value -= 360.0;
		}

		if (value < -180.0) {
			value += 360.0;
		}

		return value;
	}

	public static double positiveSmaller(Number n, Number n2) {
		return Math.abs(n.doubleValue()) < Math.abs(n2.doubleValue()) ? n.doubleValue() : n2.doubleValue();
	}

	public static double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	public static double getDistanceToGround(Player p) {
		Location loc = p.getLocation().clone();
		double y = (double)loc.getBlockY();
		double distance = 0.0;

		for (double i = y; i >= 0.0; --i) {
			loc.setY(i);
			if (loc.getBlock().getType().isSolid()) {
				break;
			}

			++distance;
		}

		return distance;
	}

	public static boolean isNegative(short number) {
		return number < 0;
	}

	public static boolean isNegativeDouble(double number) {
		return number < 0.0;
	}

	public static boolean isNearlySame(double d1, double d2, double number) {
		return Math.abs(d1 - d2) < number;
	}

	public static double delta(double d1, double d2) {
		return Math.abs(d1 - d2);
	}

	public static Vector getDirection(float yaw, float pitch) {
		Vector vector = new Vector();
		float radiansYaw = (float)Math.toRadians((double)yaw);
		float radiansPitch = (float)Math.toRadians((double)pitch);
		vector.setY(-MathHelper.sin(radiansPitch));
		double xz = (double)MathHelper.cos(radiansPitch);
		vector.setX(-xz * (double)MathHelper.sin(radiansYaw));
		vector.setZ(xz * (double)MathHelper.cos(radiansYaw));
		return vector;
	}

	public static double angle(Vector a, Vector b) {
		double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1.0), 1.0);
		return Math.acos(dot);
	}

	public static String booleanToString(boolean b) {
		return b ? "true" : "false";
	}

	public static Block getTargetedBlock(Player player, int range) {
		BlockIterator bi = new BlockIterator(player, range);
		if (!BlockUtil.chunkLoaded(player.getWorld(), bi.next().getX(), bi.next().getZ())) {
			return null;
		} else {
			Block lastBlock = null;

			while (bi.hasNext()) {
				lastBlock = bi.next();
				if (lastBlock.getType() != Material.AIR) {
					break;
				}
			}

			return lastBlock;
		}
	}

	public static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(HashMap<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);
		HashMap<K, V> result = new LinkedHashMap<>();

		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	public static double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}

	public static boolean getIntAsBoolean(int i) {
		switch (i) {
			case 0:
				return false;
			case 1:
				return true;
			default:
				return true;
		}
	}

	public static long toMillis(long time) {
		return TimeUnit.NANOSECONDS.toMillis(time);
	}

	public static long toNanos(long time) {
		return TimeUnit.MILLISECONDS.toNanos(time);
	}
}
