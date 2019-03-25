package windyroad.slashfortifier;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class GuiCloseEvent implements org.bukkit.event.Listener {
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		try {
			Inventory inv = e.getInventory();
			if (inv.getTitle().matches("拔刀-强化.*") || inv.getTitle().matches(".*强化券.*")) {
				ItemStack item = inv.getItem(13);
				inv.clear();
				if (item == null) return;
				Player p = (Player) e.getPlayer();
				p.getInventory().addItem(item);
				return;
			}
		}catch(Exception exp){
			return;
		}
	}
}
