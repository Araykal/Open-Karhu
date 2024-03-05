/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.text;

import java.text.DecimalFormat;

public class TextUtils {
	private static final int[] decimalPlaces = new int[]{0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
	public static final DecimalFormat df = new DecimalFormat("#.#");

	public static double format(double d, int dec) {
		return (double)((long)(d * (double)decimalPlaces[dec] + 0.5)) / (double)decimalPlaces[dec];
	}

	public static String formatMillis(Long milis) {
		double seconds = (double)Math.max(0L, milis) / 1000.0;
		double minutes = seconds / 60.0;
		double hours = minutes / 60.0;
		double days = hours / 24.0;
		double weeks = days / 7.0;
		double months = days / 31.0;
		double years = months / 12.0;
		if (years >= 1.0) {
			return df.format(years) + " year" + (years != 1.0 ? "s" : "");
		} else if (months >= 1.0) {
			return df.format(months) + " month" + (months != 1.0 ? "s" : "");
		} else if (weeks >= 1.0) {
			return df.format(weeks) + " week" + (weeks != 1.0 ? "s" : "");
		} else if (days >= 1.0) {
			return df.format(days) + " day" + (days != 1.0 ? "s" : "");
		} else if (hours >= 1.0) {
			return df.format(hours) + " hour" + (hours != 1.0 ? "s" : "");
		} else {
			return minutes >= 1.0 ? df.format(minutes) + " minute" + (minutes != 1.0 ? "s" : "") : df.format(seconds) + " second" + (seconds != 1.0 ? "s" : "");
		}
	}
}
