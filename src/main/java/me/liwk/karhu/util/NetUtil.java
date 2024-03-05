/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;

public class NetUtil {
	public static void download(File file, String from) throws Exception {
		FileOutputStream out = new FileOutputStream(file);
		out.getChannel().transferFrom(Channels.newChannel(new URL(from).openStream()), 0L, Long.MAX_VALUE);
	}

	public static void injectURL(URL url) {
	}

	public static int accessFile() {
		return 0;
	}

	public static void close(AutoCloseable... closeables) {
		try {
			for (AutoCloseable closeable : closeables) {
				if (closeable != null) {
					closeable.close();
				}
			}
		} catch (Exception var5) {
			var5.printStackTrace();
		}
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (Throwable var3) {
		}
	}
}
