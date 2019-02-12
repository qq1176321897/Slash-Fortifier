package windyroad.slashfortifier;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlashStrength {
	private int repairCount;	//锻造值
	private int strength;		//强化值
	private ItemStack item;
	net.minecraft.server.v1_7_R4.ItemStack nmsitem;
	/**
	 * 该方法用于检测武器的锻造值并设置好对应的强化等级
	 */
	public SlashStrength(ItemStack item) {
		this.item = item;
		nmsitem = CraftItemStack.asNMSCopy(item);
		repairCount = nmsitem.getTag().getInt("RepairCounter");
		if(repairCount<0)repairCount=0;
		int src = 0;
		int i = 0;
		while(true) {
			src += i++;
			if(src>=repairCount) {
				i--;
				break;
			}
		}
		strength = i;
		fixRepairCount();	//将锻造值修正
	}
	public void fixRepairCount() {	//根据强化值修正锻造值
		if(strength == 0) {
			repairCount = 0;
		}else {
			int rpc =0;
			for(int i = 0;i<=strength;i++) {
				rpc += i;
			}
		repairCount = rpc-strength+1;
		}
	}
	public int getRepairCount() {	
		return repairCount;
	}
	public int getStrength() {		
		return strength;
	}
	/**
	 * 改变强化等级的方法
	 */
	public void changeStrength(int n) {
		strength+=n;
		if(strength<0)strength=0;
		fixRepairCount();
	}
	public void setStrength(int n) {
		strength = n;
		if(strength<0)strength=0;
		fixRepairCount();
	}
	public ItemStack fixSlash() {	
		nmsitem.getTag().setInt("RepairCounter", repairCount);
		item = CraftItemStack.asBukkitCopy(nmsitem);
		ItemMeta im = item.getItemMeta();
		String name;
		if(im.getDisplayName()!=null) {
			name = im.getDisplayName();
		}else {
			name = "slash";
		}
		im.setDisplayName("+"+strength+" "+name.replaceAll("\\+\\d+\\s", ""));
		item.setItemMeta(im);
		return item;
		
	}
}
