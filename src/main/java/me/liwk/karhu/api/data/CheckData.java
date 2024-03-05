/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.api.data;

public final class CheckData {
	private String name;
	private String desc;
	private Category category;
	private SubCategory subCategory;
	private boolean experimental;
	private boolean silent;
	private String credits;

	public String getName() {
		return this.name;
	}

	public String getDesc() {
		return this.desc;
	}

	public Category getCategory() {
		return this.category;
	}

	public SubCategory getSubCategory() {
		return this.subCategory;
	}

	public boolean isExperimental() {
		return this.experimental;
	}

	public boolean isSilent() {
		return this.silent;
	}

	public String getCredits() {
		return this.credits;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public void setExperimental(boolean experimental) {
		this.experimental = experimental;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}
}
