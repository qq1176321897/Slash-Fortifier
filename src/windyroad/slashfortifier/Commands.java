package windyroad.slashfortifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import windyroad.slashfortifier.main.Main;

import static javax.sql.rowset.spi.SyncFactory.getLogger;

public class Commands implements CommandExecutor{

	private final Plugin plugin;
	public Commands(Plugin plugin) {
        this.plugin = plugin; 
    }
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lab, String[] args) {
		if(cmd.getName().equalsIgnoreCase("slashfortifier") || cmd.getName().equalsIgnoreCase("sff")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.isOp()) {
					try {
					if("reload".equals(args[0])){
						((Main)plugin).doReloadConfig(new File(plugin.getDataFolder(), "config.yml"));
						p.sendMessage("§4§l[提示]§e成功重载载拔刀强化插件的配置");
						return true;
					}
					if("hand".equals(args[0])) {
						try {
						if (args[1].matches("^[0-9]*$")) {
							List idList = Main.config.getIntegerList("SlashId");
							ItemStack slash = p.getItemInHand();
							if (SlashUtils.isSlash(slash)) {
								int strength = Integer.parseInt(args[1]);
								if(strength>500000)strength=500000;
								if(strength<0)strength=0;
								SlashStrength slashItem= new SlashStrength(slash);
								slashItem.setStrength(strength);
								slash = slashItem.fixSlash();
								p.setItemInHand(slash);
								p.sendMessage("§4§l[提示]§e成功将武器强化等级设置为"+strength);
								return true;
							}else {
								sender.sendMessage("§4§l[提示]§e大哥,你确定手持的物品是拔刀吗?");
								return true;
							}
						}else {
							sender.sendMessage("§4§l[提示]§e将手持的拔刀的强化等级设置成指定等级,用法:手持拔刀,/slashfortifier hand 等级(正整数)");
							return true;
						}}catch(Exception e) {
							sender.sendMessage("§4§l[提示]§e将手持的拔刀的强化等级设置成指定等级,用法:手持拔刀,/slashfortifier hand 等级(正整数)");
							return true;
						}
					}
					if("voucher".equals(args[0])) {
						try {
							int strength = Integer.parseInt(args[1]);
							if(strength>500000)strength=500000;
							if(strength<1)strength=1;
							int chance = Integer.parseInt(args[2]);
							if(chance>100)strength=100;
							if(chance<1)strength=1;
							ItemStack item = new ItemStack(Material.PAPER);
							net.minecraft.server.v1_7_R4.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
							NBTTagCompound tagStrength = new NBTTagCompound();
							tagStrength.setString("sfPluginType", "qhq");
							tagStrength.setInt("sfPluginStrength", strength);
							tagStrength.setInt("sfPluginChance", chance);
							nmsitem.setTag(tagStrength);
							item = CraftItemStack.asBukkitCopy(nmsitem);
							ItemMeta im = item.getItemMeta();
							im.setDisplayName("+"+strength+"拔刀强化券");
							List<String> list = new ArrayList<String>();
							list.add("右键使用");
							list.add("可将拔刀强化设置至"+strength);
							list.add("成功率:"+chance);
							list.add("失败无惩罚");
							list.add("注意:强化券是覆盖式的");
							list.add("无论原强化等级是多少,");
							list.add("使用后都会变成+"+strength);
							list.add("你要想把它当做普通的纸用也没人说什么...");
							im.setLore(list);
							item.setItemMeta(im);
							item.addUnsafeEnchantment(new EnchantmentWrapper(34), 1);
							p.getInventory().addItem(item);
							sender.sendMessage("§4§l[提示]§e获得一张+"+strength+",成功率"+chance+"%的强化券");
							return true;
						}catch(Exception e) {
							sender.sendMessage("§4§l[提示]§e获得一个强化券,用法:/slashfortifier voucher 强化的等级(正整数) 几率(1-100之间的一个整数)");
							return true;
						}
					}
					if("getstone1".equals(args[0])) {
						try {
							int chanceUp = Integer.parseInt(args[1]);
							if(chanceUp>100)chanceUp=100;
							if(chanceUp<1)chanceUp=1;
							

							ItemStack item = new ItemStack(Integer.parseInt(Main.config.getString("id_QHS")));
							net.minecraft.server.v1_7_R4.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
							NBTTagCompound tagStrength = new NBTTagCompound();
							tagStrength.setString("sfPluginType", "qhs");
							tagStrength.setInt("sfPluginChanceUp", chanceUp);
							nmsitem.setTag(tagStrength);
							item = CraftItemStack.asBukkitCopy(nmsitem);
							ItemMeta im = item.getItemMeta();
							im.setDisplayName(chanceUp+"%拔刀强化石");
							List<String> list = new ArrayList<String>();
							list.add("放于背包,强化时自动消耗(对强化券无效)");
							list.add("强化成功率增加"+chanceUp);
							list.add("若背包有多种不同的强化石,则会随机消耗一枚");
							im.setLore(list);
							item.setItemMeta(im);
							item.addUnsafeEnchantment(new EnchantmentWrapper(34), 1);
							p.getInventory().addItem(item);
							sender.sendMessage("§4§l[提示]§e获得一枚增加成功率"+chanceUp+"%的强化石");
							return true;
						}catch(Exception e) {
							sender.sendMessage("§4§l[提示]§e获得一个强化石,用法:/slashfortifier getstone1 增加的强化几率(1-100的正整数) ");
							return true;
						}	
					}
					if("getstone2".equals(args[0])) {
						try {

							ItemStack item = new ItemStack(Integer.parseInt(Main.config.getString("id_BHS")));
							net.minecraft.server.v1_7_R4.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
							NBTTagCompound tagStrength = new NBTTagCompound();
							tagStrength.setString("sfPluginType", "qhp");
							nmsitem.setTag(tagStrength);
							item = CraftItemStack.asBukkitCopy(nmsitem);
							ItemMeta im = item.getItemMeta();
							im.setDisplayName("拔刀保护石");
							List<String> list = new ArrayList<String>();
							list.add("放于背包,强化失败时自动消耗(对强化券无效)");
							list.add("失败无损毁");
							im.setLore(list);
							item.setItemMeta(im);
							item.addUnsafeEnchantment(new EnchantmentWrapper(34), 1);
							p.getInventory().addItem(item);
							sender.sendMessage("获得一枚强化保护石");
							return true;
						}catch(Exception e) {
							return true;
						}
					}
					sender.sendMessage("§4§l[SLASH FORTIFIER]§e命令错误");
					sender.sendMessage("§e1.将手持的拔刀的强化等级设置成指定等级,用法:手持拔刀,/slashfortfier hand 等级(正整数)");
					sender.sendMessage("§e2.获得一张强化券,用法:/slashfortifier voucher 强化的等级(正整数) 几率(1-100之间的一个整数)");
					sender.sendMessage("§e3.获得一个强化石,用法:/slashfortifier getstone1 增加的强化几率(1-100的正整数)");
					sender.sendMessage("§e4.获得一个强化保护石,用法:/slashfortifier getstone2");
					sender.sendMessage("§e4.重载插件配置,用法:/slashfortifier reload");
					return true;
					}catch(Exception e) {
						sender.sendMessage("§4§l[SLASH FORTIFIER]§e命令帮助");
						sender.sendMessage("§e1.将手持的拔刀的强化等级设置成指定等级,用法:手持拔刀,/slashfortfier hand 等级(正整数)");
						sender.sendMessage("§e2.获得一张强化券,用法:/slashfortifier voucher 强化的等级(正整数) 几率(1-100之间的一个整数)");
						sender.sendMessage("§e3.获得一个强化石,用法:/slashfortifier getstone1 增加的强化几率(1-100的正整数)");
						sender.sendMessage("§e4.获得一个强化保护石,用法:/slashfortifier getstone2");
						sender.sendMessage("§e4.重载插件配置,用法:/slashfortifier reload");
						return true;
					}
				}else {
					sender.sendMessage("§4§l[提示]§e只有OP才能使用这个指令!");
					return true;
				}
			}else {
				sender.sendMessage("§4§l[提示]§e控制台不能使用这个指令!");
				return true;
			}
		};
		return true;
	}
}
