package windyroad.slashfortifier;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui {
	public Inventory inv;

	public Gui(String name) {
		inv = Bukkit.createInventory(null, InventoryType.CHEST, "拔刀-强化" + name);
		ItemStack cut = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta im2 = cut.getItemMeta();
		im2.setDisplayName("§e[占位符]");
		cut.setItemMeta(im2);
		for (int i = 0; i < 27; i++) {
			inv.setItem(i, cut);
		}
		ItemStack book = new ItemStack(Material.BOOK);
		ItemStack t = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemStack f = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta im = t.getItemMeta();
		im.setDisplayName("确认强化这个武器");
		t.setItemMeta(im);
		im = f.getItemMeta();
		im.setDisplayName("取消");
		f.setItemMeta(im);
		im = book.getItemMeta();
		im.setDisplayName("点击获取本次强化相关信息");
		book.setItemMeta(im);
		inv.setItem(8, t);
		inv.setItem(10, book);
		inv.setItem(26, f);
		inv.setItem(13, null);
	}
	public Gui(int level,int chance) {
		inv = Bukkit.createInventory(null, InventoryType.CHEST, "+"+level+"强化券,成功率 百分之"+chance);
		ItemStack cut = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta im2 = cut.getItemMeta();
		im2.setDisplayName("§e[占位符]");
		cut.setItemMeta(im2);
		for (int i = 0; i < 27; i++) {
			inv.setItem(i, cut);
		}
		ItemStack t = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemStack f = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta im = t.getItemMeta();
		im.setDisplayName("确认强化这个武器");
		t.setItemMeta(im);
		im = f.getItemMeta();
		im.setDisplayName("取消");
		f.setItemMeta(im);
		inv.setItem(8, t);
		inv.setItem(26, f);
		inv.setItem(13, null);
	}
}
