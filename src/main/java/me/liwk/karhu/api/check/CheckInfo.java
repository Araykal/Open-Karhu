/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInfo {
	String name();

	String desc() default "";

	Category category();

	SubCategory subCategory();

	boolean experimental();

	boolean subCheck() default false;

	boolean silent() default false;

	String credits() default "";
}
