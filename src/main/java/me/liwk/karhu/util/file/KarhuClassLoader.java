/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.file;

import java.net.URL;
import java.net.URLClassLoader;

public class KarhuClassLoader extends URLClassLoader {
	private final URLClassPath ucp = new URLClassPath(this);

	public KarhuClassLoader(URL url, ClassLoader classLoader) {
		super(new URL[]{url}, classLoader);
	}

	@Override
	public void addURL(URL url) {
		this.ucp.addURL(url);
	}

	static {
		ClassLoader.registerAsParallelCapable();
	}
}
