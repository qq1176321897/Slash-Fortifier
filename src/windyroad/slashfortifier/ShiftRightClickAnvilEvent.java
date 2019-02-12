package windyroad.slashfortifier;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ShiftRightClickAnvilEvent implements org.bukkit.event.Listener {
	@EventHandler
	public void onInventClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if(item!=null && item.getType().equals(Material.PAPER)) {
			net.minecraft.server.v1_7_R4.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
			try {
				String type = nmsitem.getTag().getString("sfPluginType");
				int strength = nmsitem.getTag().getInt("sfPluginStrength");
				int chance = nmsitem.getTag().getInt("sfPluginChance");
				if("qhq".equals(type)) {
					e.setCancelled(true);
					Inventory inv = new Gui(strength,chance).inv;
					p.openInventory(inv);
				}
			}catch(Exception e1) {
				
			}
			return;
		}
		Block b = e.getClickedBlock();
		if(b == null)return;
		if(b.getType()!=Material.ANVIL)return;
		if(!(p.isSneaking()))return;
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)return;

		Location l = b.getLocation();
		l.setY(l.getY()-1);
		if(l.getBlock().getTypeId()!=42)return;
		e.setCancelled(true);
		Inventory inv = new Gui("").inv;
		p.openInventory(inv);
	}
}
