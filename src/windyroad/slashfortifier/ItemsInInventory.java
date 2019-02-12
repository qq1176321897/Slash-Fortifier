package windyroad.slashfortifier;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class ItemsInInventory {
	private int hasCount;
	private Player p;
	private Map nbts;
	
	public ItemsInInventory(Player p,Map nbts) {
		this.nbts=nbts;
		this.p=p;
		hasCount = getCounts();
	}
	
	public boolean hasEnoughItems(int needCount) {
		return hasCount >= needCount;
	}
	
	public ItemStack takeItems(int count) {
		Set<Entry> nbtSet = nbts.entrySet();
		ItemStack[] items = p.getInventory().getContents();
		for(int i = 0 ; i < items.length ; i++) {
			if(items[i]!=null) {
				net.minecraft.server.v1_7_R4.ItemStack nmsitem = CraftItemStack.asNMSCopy(items[i]);
				NBTTagCompound nbtt =  nmsitem.getTag();
				boolean pass = true;
				for(Entry entry : nbtSet) {
					try {
						if(entry.getValue() instanceof String) {
							if(!(entry.getValue().equals(nbtt.getString((String)entry.getKey())))) {
								pass = false;
								break;
							}
						}else {
							if((Integer) (entry.getValue()) !=nbtt.getInt((String)entry.getKey())) {
								pass = false;
								break;
							}
						}
					}catch(Exception e) {
						pass = false;
						break;
					}
				}
				if(pass) {
					int num = items[i].getAmount();
					if(num>count) {
						items[i].setAmount(num-count);
						count = 0;
						
						return items[i];
						
					}else {
						count = count - num;
						p.getInventory().clear(i);
						if(count == 0) {
							return items[i];
						}
					}
				}
			}
		}
		return null;
	}
	
	private int getCounts() {
		Set<Entry> nbtset = nbts.entrySet();
		ItemStack[] items = p.getInventory().getContents();
		hasCount=0;
		for(int i = 0 ; i < items.length ; i++) {
			if(items[i]!=null) {
				net.minecraft.server.v1_7_R4.ItemStack nmsitem = CraftItemStack.asNMSCopy(items[i]);
				NBTTagCompound nbtt =  nmsitem.getTag();
				boolean pass = true;
				for(Entry entry : nbtset) {
					try {
						if(entry.getValue() instanceof String) {
							if(!(nbtt.getString((String)entry.getKey()).equals(entry.getValue()))) {
								pass = false;
								break;
							}
						}else {
							if(nbtt.getInt((String)entry.getKey())!=((Integer)(entry.getValue())).intValue()) {
								pass = false;
								break;
							}
						}
					}catch(Exception e){
						pass = false;
						break;
					}
				}
				if(pass) {
					hasCount += items[i].getAmount();
				}
				
			}
		}
		return hasCount;
	}
}
