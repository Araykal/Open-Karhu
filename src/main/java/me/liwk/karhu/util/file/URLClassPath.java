/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.file;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import sun.misc.Unsafe;

public class URLClassPath {
	private final KarhuClassLoader classLoader;

	URLClassPath(KarhuClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void addURL(URL url) {
		Lookup lookup;
		Unsafe unsafe;
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe)theUnsafe.get(null);
			unsafe.ensureClassInitialized(Lookup.class);
			Field lookupField = Lookup.class.getDeclaredField("IMPL_LOOKUP");
			Object lookupBase = unsafe.staticFieldBase(lookupField);
			long lookupOffset = unsafe.staticFieldOffset(lookupField);
			lookup = (Lookup)unsafe.getObject(lookupBase, lookupOffset);
		} catch (Throwable var12) {
			throw new IllegalStateException("Unsafe not found");
		}

		Field field;
		try {
			field = URLClassLoader.class.getDeclaredField("ucp");
		} catch (NoSuchFieldException var11) {
			throw new RuntimeException("Couldn't find ucp field from ClassLoader!");
		}

		try {
			long ucpOffset = unsafe.objectFieldOffset(field);
			Object ucp = unsafe.getObject(this.classLoader, ucpOffset);
			MethodHandle methodHandle = lookup.findVirtual(ucp.getClass(), "addURL", MethodType.methodType(void.class, URL.class));
			methodHandle.invoke((Object)ucp, (URL)url);
		} catch (Throwable var10) {
			throw new RuntimeException("Something wrong while adding dependency to class path!", var10);
		}
	}
}
