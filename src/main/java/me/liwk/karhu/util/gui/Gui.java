/*
 * Class file decompiled by SakuraFlower
 */

package me.liwk.karhu.util.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import me.liwk.karhu.util.task.Tasker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class Gui implements Listener {
	private HashMap<Integer, Inventory> pages;
	private HashMap<Integer, HashMap<Integer, ItemStack>> items;
	private static final List<Gui> guis = new ArrayList<>();
	private HashMap<Player, Integer> playerPages = new HashMap<>();
	private final Set<Button> buttons;
	private String title;
	private int size;
	private Inventory inv;
	private boolean partiallyTouchable;
	private int[] allowedSlots;
	private BukkitScheduler task = null;

	public Gui(String title, int size) {
		if (!guis.contains(this)) {
			guis.add(this);
		}

		this.pages = new HashMap<>();
		this.items = new HashMap<>();
		this.buttons = new HashSet<>();
		this.title = title;
		this.size = size;
		this.partiallyTouchable = false;
		this.allowedSlots = new int[0];
		this.inv = null;
		this.init();
	}

	public boolean isPartiallyTouchable() {
		return this.partiallyTouchable;
	}

	public void setPartiallyTouchable(boolean value) {
		this.partiallyTouchable = value;
	}

	public int[] getAllowedSlots() {
		return this.allowedSlots;
	}

	public void setAllowedSlots(int... slots) {
		this.allowedSlots = slots;
	}

	public boolean clickedAllowedSlot(int clickedSlot) {
		if (this.allowedSlots.length < 1) {
			return false;
		} else {
			for (int i = 0; i < this.allowedSlots.length; ++i) {
				int allowedSlot = this.allowedSlots[i];
				if (clickedSlot == allowedSlot) {
					return true;
				}
			}

			return false;
		}
	}

	public HashMap<Integer, Inventory> getPages() {
		return this.pages;
	}

	private String getTitle() {
		return this.title;
	}

	public int getSize() {
		return this.size;
	}

	private void init() {
		this.createPage(false);
	}

	public ItemStack getItem(int pos) {
		HashMap<Integer, ItemStack> items = this.items.get(1);
		return items != null && !items.isEmpty() && items.containsKey(pos) ? items.get(pos) : null;
	}

	public int nextEmptySlot() {
		ItemStack[] items = this.getPages().get(1).getContents();

		for (int i = 0; i < items.length; ++i) {
			ItemStack item = items[i];
			if (item == null) {
				return i;
			}
		}

		return 0;
	}

	public void addButton(Button button) {
		if (button != null && button.item != null) {
			if (button.inv == null) {
				button.inv = this.inv;
			}

			this.buttons.add(button);
		}
	}

	private Inventory createPage(boolean addPageButtons) {
		return this.getPages() != null && !this.getPages().isEmpty() ? this.createPage(this.getPages().size() + 1, addPageButtons) : this.createPage(1, false);
	}

	private Inventory createPage(int page, boolean addPageButtons) {
		Inventory inv = Bukkit.createInventory(null, this.getSize(), this.getTitle());
		this.getPages().put(page, inv);
		if (addPageButtons) {
			this.addPageButtons(inv);
		}

		return inv;
	}

	@Deprecated
	public void hardRefresh(Player player) {
		this.close(player);
		this.open(player);
	}

	public void softRefresh(Player player) {
		player.updateInventory();
	}

	@Deprecated
	public void refresh(Player player) {
		this.hardRefresh(player);
	}

	private void addPageButtons(Inventory inv) {
		ItemStack nextPageItem = new ItemStack(Material.PAPER);
		ItemMeta nextMeta = nextPageItem.getItemMeta();
		nextMeta.setDisplayName("§7Next page");
		nextPageItem.setItemMeta(nextMeta);
		ItemStack prevPageItem = new ItemStack(Material.PAPER);
		ItemMeta prevMeta = nextPageItem.getItemMeta();
		prevMeta.setDisplayName("§7Previous page");
		prevPageItem.setItemMeta(prevMeta);
		this.addButton(new Button(inv, inv.getSize() - 1, nextPageItem) {
			@Override
			public void onClick(Player clicker, ClickType clickType) {
				Gui.this.nextPage(clicker);
			}
		});
		this.addButton(new Button(inv, inv.getSize() - 9, prevPageItem) {
			@Override
			public void onClick(Player clicker, ClickType clickType) {
				Gui.this.previousPage(clicker);
			}
		});
	}

	public void addPageButtons(int page) {
		if (this.getPages() != null && !this.getPages().isEmpty() && this.getPages().get(page) != null) {
			Inventory inv = this.getPages().get(page);
			this.addPageButtons(inv);
		} else {
			throw new IllegalArgumentException("You must have at least 1 page in your gui!");
		}
	}

	public void removePage(int page) {
		if (this.getPages() != null && !this.getPages().isEmpty() && this.getPages().get(page) != null) {
			this.getPages().remove(page);
		} else {
			throw new IllegalArgumentException("You don't have any pages in your inventory or the page you're removing doesn't exist!");
		}
	}

	public void removeAllPages() {
		this.getPages().clear();
	}

	public void open(Player player) {
		Tasker.taskAsync(() -> {
			if (this.getPages() != null && !this.getPages().isEmpty()) {
				this.inv = Bukkit.createInventory(null, this.size, this.title + "§r");
				if (this.items.isEmpty() && this.buttons.isEmpty()) {
					player.openInventory(this.inv);
				} else {
					HashMap<Integer, ItemStack> items = this.items.get(1);
					if (!this.items.isEmpty()) {
						for (Entry<Integer, ItemStack> e : items.entrySet()) {
							this.inv.setItem(e.getKey(), e.getValue());
						}
					}

					if (!this.buttons.isEmpty()) {
						for (Button b : this.getButtons()) {
							this.inv.setItem(b.pos, b.item);
							if (b.inv == null) {
								b.inv = this.inv;
							}
						}
					}

					this.playerPages.put(player, 1);
					Tasker.run(() -> player.openInventory(this.inv));
				}
			} else {
				throw new IllegalArgumentException("You must have at least 1 page in your gui!");
			}
		});
	}

	public void addItem(int page, ItemStack item, int pos) {
		if (this.getPages() != null && !this.getPages().isEmpty()) {
			if (!this.items.containsKey(page)) {
				HashMap<Integer, HashMap<Integer, ItemStack>> list = new HashMap<>();
				HashMap<Integer, ItemStack> items = new HashMap<>();
				items.put(pos, item);
				list.put(page, items);
				this.items.put(page, items);
			} else {
				HashMap<Integer, ItemStack> items = this.items.get(page);
				if (items.containsKey(pos)) {
					items.replace(pos, item);
				} else {
					items.put(pos, item);
				}

				this.items.replace(page, items);
			}
		} else {
			throw new IllegalArgumentException("You must have at least 1 page in your gui!");
		}
	}

	public Inventory openPage(Player player, int page) {
		if (this.getPages() != null && !this.getPages().isEmpty() && this.getPages().get(page) != null) {
			Inventory inv = this.getPages().get(page);
			player.openInventory(inv);
			this.playerPages.put(player, page);
			return inv;
		} else {
			throw new IllegalArgumentException("You must have at least 1 page in your gui!");
		}
	}

	private void nextPage(Player player) {
		if (this.getPages() != null && !this.getPages().isEmpty()) {
			if (this.playerPages.containsKey(player)) {
				int currentPage = this.playerPages.get(player);
				if (this.getPages().size() >= currentPage + 1) {
					Inventory nextPage = this.getPages().get(currentPage + 1);
					player.openInventory(nextPage);
					this.playerPages.put(player, currentPage + 1);
				}
			}
		} else {
			throw new IllegalArgumentException("You must have at least 1 page in your gui!");
		}
	}

	private void previousPage(Player player) {
		if (this.getPages() != null && !this.getPages().isEmpty()) {
			if (this.playerPages.containsKey(player)) {
				int currentPage = this.playerPages.get(player);
				if (currentPage - 1 >= 1) {
					Inventory nextPage = this.getPages().get(currentPage - 1);
					player.openInventory(nextPage);
					this.playerPages.put(player, currentPage - 1);
				}
			}
		} else {
			throw new IllegalArgumentException("You must have at least 1 page in your gui!");
		}
	}

	public Button getButton(int pos) {
		return this.getButtons().stream().filter(b -> b.pos == pos).findFirst().orElse(null);
	}

	public int getPage(Player player) {
		return this.playerPages.getOrDefault(player, 0);
	}

	public void close(Player player) {
		this.close(player, true);
	}

	public void close(Player player, boolean closeInventory) {
		this.getPlayerPages().remove(player);
		if (closeInventory) {
			player.closeInventory();
		}

		guis.remove(this);
	}

	public Set<Button> getButtons() {
		return this.buttons;
	}

	private HashMap<Player, Integer> getPlayerPages() {
		return this.playerPages;
	}

	public static void closeCurrent(Player player) {
		Gui gui = getGui(player);
		if (gui != null) {
			gui.close(player);
		}
	}

	public static Gui getGui(Player player) {
		return guis.stream().filter(gui -> gui.getPlayerPages().containsKey(player)).findFirst().orElse(null);
	}
}
