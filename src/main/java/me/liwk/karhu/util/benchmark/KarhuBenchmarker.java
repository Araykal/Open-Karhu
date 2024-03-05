/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.benchmark;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KarhuBenchmarker {
	private static final Map<BenchmarkType, Benchmark> profiles = new HashMap<>();

	public static void registerProfiles() {
		for (BenchmarkType profileType : BenchmarkType.values()) {
			profiles.put(profileType, new Benchmark(profileType, profileType.precision()));
		}
	}

	public static Benchmark getProfileData(BenchmarkType profileType) {
		return profiles.get(profileType);
	}

	public static List<Benchmark> sortedProfiles() {
		List<Benchmark> sorted = profiles.values().stream().sorted(Comparator.comparingDouble(Benchmark::runningAverage)).collect(Collectors.toList());
		Collections.reverse(sorted);
		return sorted;
	}
}
