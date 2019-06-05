package windyroad.slashfortifier;

import java.math.MathContext;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import com.earth2me.essentials.api.Economy;
import java.math.BigDecimal;

//import net.milkbowl.vault.economy.*;

import windyroad.slashfortifier.main.Main;

public class GuiEvent implements org.bukkit.event.Listener {
	Plugin plugin;
	//private Economy vault;
	public GuiEvent(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		ItemStack item = event.getCurrentItem();
		if (item == null)
			return;
		if (inv instanceof AnvilInventory) {
			if (SlashUtils.isSlash(item)) {
				((Player) event.getWhoClicked()).sendMessage(Main.config.getString("Message_Tip"));
			}
		}
		if (SlashUtils.isSlash(item)) {
			try {
				SlashStrength ss = new SlashStrength(item);
				event.setCurrentItem(ss.fixSlash());
			}catch (Exception e){

			}
		}

		if (event.getWhoClicked() instanceof Player == false)
			return;
		Player p = (Player) event.getWhoClicked();
		if (event.getInventory().getTitle().matches("强化券:.*")) {
			int slot = event.getRawSlot();
			if ((slot >= 0) && (slot <= 26)) {
				if (slot == 13) {
					return;
				}
				event.setCancelled(true);
			}
		}
		if (event.getInventory().getTitle().matches(".*强化券,成功率.*")) {
			int slot = event.getRawSlot();
			if ((slot >= 0) && (slot <= 26)) {
				if (slot == 13) {
					return;
				}
				if (slot == 8) {
					ItemStack itemstack = inv.getItem(13);
					if (itemstack == null) {
						event.setCancelled(true);
						p.sendMessage(Main.config.getString("Message_nothing"));
						p.closeInventory();
						return;
					} else if (!SlashUtils.isSlash(itemstack)) {
						event.setCancelled(true);
						p.closeInventory();
						p.sendMessage(Main.config.getString("Message_NOT_Slash"));
						return;
					} else {
						event.setCancelled(true);
						SlashStrength ss = new SlashStrength(itemstack);
						int strength = Integer.parseInt(inv.getTitle().split("强化券")[0].substring(1));
						int chance = Integer.parseInt(inv.getTitle().split("百分之")[1]);

						Map nbts = new HashMap();
						nbts.put("sfPluginType", "qhq");
						nbts.put("sfPluginStrength", strength);
						nbts.put("sfPluginChance", chance);
						ItemsInInventory iii = new ItemsInInventory(p, nbts);
						if (iii.hasEnoughItems(1)) {
							iii.takeItems(1);
							inv.clear(13);
							p.closeInventory();
							Random random = new Random();
							Inventory newInv;
							if (chance >= random.nextInt(100)) {
								new BukkitRunnable() {
									int i = 0;

									@Override
									public void run() { // 播放强化成功提示音
										switch (++i) {
										case 1:
											p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1.5);
											break;
										case 2:
											p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1);
											break;
										case 3:
											p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1.3);
											break;
										case 4:
											p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1.15);
											this.cancel();
										}

									}
								}.runTaskTimer(plugin, 0, 8);
								ss.setStrength(strength);
								itemstack = ss.fixSlash();
								newInv = Bukkit.createInventory(null, InventoryType.CHEST, "强化券:成功");
							} else {
								p.getWorld().createExplosion(p.getLocation(), 0, false);
								newInv = Bukkit.createInventory(null, InventoryType.CHEST, "强化券:失败");
							}

							ItemStack cut = new ItemStack(Material.STAINED_GLASS_PANE, 1);
							ItemMeta im2 = cut.getItemMeta();
							im2.setDisplayName("§e[占位符]");
							cut.setItemMeta(im2);
							for (int i = 0; i < 27; i++) {
								newInv.setItem(i, cut);
							}
							newInv.setItem(13, itemstack);
							p.openInventory(newInv);

						} else {
							p.sendMessage(Main.config.getString("Message_noVoucher:"));
							p.closeInventory();
						}
					}
					return;
				}
				event.setCancelled(true);
			}
			if (slot == 26) {
				event.setCancelled(true);
				p.closeInventory();
				return;
			}
		}

		if (event.getInventory().getTitle().matches("拔刀-强化.*")) {
			int slot = event.getRawSlot();
			if ((slot >= 0) && (slot <= 26)) {
				if (slot == 10) {
					event.setCancelled(true);
					if (inv.getItem(13) != null) {
						if (!SlashUtils.isSlash(inv.getItem(13))) {
							ItemStack check = new ItemStack(Material.BOOK);
							ItemMeta im = check.getItemMeta();
							im.setDisplayName(Main.config.getString("Message_notSlash"));
							check.setItemMeta(im);
							inv.setItem(10, check);
							return;
						} else {
							ItemStack check = new ItemStack(Material.BOOK);
							ItemMeta im = check.getItemMeta();
							im.setDisplayName("强化需求");
							List<String> list = new ArrayList<String>();
							getRequire(inv.getItem(13), list, p);
							im.setLore(list);
							check.setItemMeta(im);
							inv.setItem(10, check);
							return;
						}
					}
					return;
				}
				if (slot == 13) {
					return;
				}
				if (slot == 8) {
					ItemStack itemstack = inv.getItem(13);
					if (itemstack == null) {
						event.setCancelled(true);
						p.sendMessage(Main.config.getString("Message_nothing"));
						p.closeInventory();
						return;
					} else if (!SlashUtils.isSlash(itemstack)) {
						event.setCancelled(true);
						p.closeInventory();
						p.sendMessage(Main.config.getString("Message_NOT_Slash"));
						return;
					} else if (!enoughMoney(itemstack, p)){
						event.setCancelled(true);
						p.closeInventory();
						p.sendMessage("§4§l[提示]§e没钱还想白嫖？");
						return;
					} else if (checkIfOk(itemstack, p)) {
						event.setCancelled(true);
						inv.clear(13);
						p.closeInventory();
						SlashStrength ss = new SlashStrength(itemstack);
						int strength = ss.getStrength();
						try{
							Economy.substract(p.getName(), new BigDecimal(getCoin(strength)));
						}catch(Throwable e)
						{
							e.printStackTrace();
							return;
						}
						p.sendMessage("§e正在强化");
						Player[] ps = p.getServer().getOnlinePlayers();
						if (
								Main.config.getBoolean("Broadcast")
								&&
								strength >= Main.config.getInt("StrengthLimit")) {
							for (Player player : ps) {
								if (player.isOnline()) {
									String n = CraftItemStack.asNMSCopy(itemstack).getName();
									player.sendMessage("§e"+p.getDisplayName() + "正在强化他的"+ strength +"级的"
											+ n + "...");
								}
							}
						}
						new BukkitRunnable() { // 强化过程中播放火花特效和铁砧声音
							int i = 0;

							@Override
							public void run() {
								switch (++i) {
								case 1:
								case 2:
								case 3:
									p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);
									p.sendMessage(".");
									p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
									break;
								case 4:
									startFort(itemstack, p);
									this.cancel();
								}
							}
						}.runTaskTimer(plugin, 10, 15);
						return;
					} else {
						event.setCancelled(true);
						p.closeInventory();
						p.sendMessage("§4§l[提示]§e材料不足");
						return;
					}
				}
				if (slot == 26) {
					event.setCancelled(true);
					p.closeInventory();
					return;
				}
				event.setCancelled(true);
			}
		}
	}
	private static boolean enoughMoney(ItemStack itemStack, Player p)
	{
		try {
			return Economy.hasEnough(p.getName(), new BigDecimal(getCoin(new SlashStrength(itemStack).getStrength())));
		}catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
	private void startFort(ItemStack item, Player p) {

		SlashStrength ss = new SlashStrength(item);
		int strength = ss.getStrength();
		Player[] ps = p.getServer().getOnlinePlayers();
		if (checkIfOk(item, p)) {
			int number = getMaterial(strength);
			ItemStack[] is = p.getInventory().getContents();
			for (int i2 = 0; i2 < is.length; i2++) {
				if (is[i2] != null) {
					if ((is[i2].getTypeId() == Main.config.getInt("SlashId2"))) {
						if (is[i2].getDurability() == 0) {
							if (is[i2].getAmount() <= number) {
								number = number - is[i2].getAmount();
								p.getInventory().clear(i2);
							} else {
								p.getInventory().getItem(i2).setAmount(is[i2].getAmount() - number);
								number = 0;
							}
						}
					}
				}
			}
			Random r = new Random();
			ChanceGetter cg = new ChanceGetter(strength);
			int chance = cg.chance;
			int punish = cg.punish;
			boolean ifTo0 = cg.ifTo0;
			boolean ifBreak = cg.ifBreak;

			if (chance < 0)
				chance = 1;
			Map map1 = new HashMap();
			map1.put("sfPluginType", "qhs");
			ItemsInInventory iii = new ItemsInInventory(p, map1);
			if (iii.hasEnoughItems(1)) {
				int chanceUp = CraftItemStack.asNMSCopy(iii.takeItems(1)).getTag().getInt("sfPluginChanceUp");
				p.sendMessage("§4§l[提示]§e消耗了一枚强化石,成功率增加" + chanceUp + "%");
				chance += chanceUp;
			}
			int chancer = r.nextInt(100);
			boolean isSuccess = chance >= chancer;
			p.sendMessage("§4§l[提示]§e本次强化拔刀,最终成功率:" + chance + "%,本次不幸值:" + chancer);
			plugin.getLogger().info(p.getName() + "正在强化拔刀,成功率:" + chance + "%,本次不幸值:" + chancer+",结果:"+(isSuccess?"成功":"失败"));
			if (isSuccess) {
				if (
						Main.config.getBoolean("Broadcast")
						&&
						strength >= Main.config.getInt("StrengthLimit")) {
					for (Player player : ps) {
						if (player.isOnline()) {
							player.sendMessage(Main.config.getString("Message_suc"));
						}
					}
				}
				p.sendRawMessage("§4§l[恭喜]§e强化成功");
				ss.changeStrength(1);
				item = ss.fixSlash();
				new BukkitRunnable() {
					int i = 0;

					@Override
					public void run() {
						switch (++i) {
						case 1:
							p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1.5);
							break;
						case 2:
							p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1);
							break;
						case 3:
							p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1.3);
							break;
						case 4:
							p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 1.15);
							this.cancel();
						}

					}
				}.runTaskTimer(plugin, 0, 8);
				Inventory invresult = new Gui("-成功").inv;
				invresult.setItem(13, item);
				if (p.isOnline())
					p.openInventory(invresult);
			} else {
				if (
						Main.config.getBoolean("Broadcast")
						&&
						Main.config.getBoolean("FailBuff")
						&&
						strength >= Main.config.getInt("StrengthLimit")
						) {
					for (Player player : ps) {
						if (player.isOnline()) {
							player.sendMessage(Main.config.getString("Message_def"));
							if (!(player.getDisplayName().equals(p.getDisplayName())))
								player.chat(Main.config.getString("Message_def1"));
							player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 2));
							player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 5));
							player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 5));
						}
					}
				}
				p.sendMessage("§4§l[提示]§e强化失败");
				p.getWorld().createExplosion(p.getLocation(), 0, false);

				Map map2 = new HashMap();
				map2.put("sfPluginType", "qhp");
				ItemsInInventory iii2 = new ItemsInInventory(p, map2);
				if (iii2.hasEnoughItems(1)) {
					iii2.takeItems(1);
					p.sendMessage(Main.config.getString("Message_def2"));
				} else {
					ss.changeStrength(punish * -1);
					item = ss.fixSlash();
					if (ifBreak) {
						item = null;
						p.sendMessage(Main.config.getString("Message_def3"));
					} else if (ifTo0) {
						ss.setStrength(0);
						item = ss.fixSlash();
						p.sendMessage(Main.config.getString("Message_def4"));
					} else if (punish == 0) {
						p.sendMessage(Main.config.getString("Message_def5"));
					} else {
						p.sendMessage("§e武器受到部分损毁,强化等级-" + punish);
					}
				}
				Inventory invresult = new Gui("-失败").inv;
				invresult.setItem(13, item);
				if (p.isOnline()) {
					p.openInventory(invresult);
				}else {
					putOfflineItems(item);
				}
			}
		} else {
			p.sendMessage("§4§l[警告]物品不足!别打小心思.");
			p.getInventory().addItem(item);
			if (strength >= Main.config.getInt("StrengthLimit")) {
				for (Player player : ps) {
					if (player.isOnline()) {
						player.sendMessage(Main.config.getString("Message_stop"));
					}
				}
			}
		}
	}

	private void putOfflineItems(ItemStack item) {

	}

	private boolean checkIfOk(ItemStack item, Player p) { // 判断是否符合强化条件(材料是否齐全)
		int repair = CraftItemStack.asNMSCopy(item).getTag().getInt("RepairCounter");
		int shouldRepair = 0;
		int i = 0;
		while (true) {
			shouldRepair += i++;
			if (shouldRepair >= repair) {
				i--;
				break;
			}
		}
		int number = 0;
		ItemStack[] is = new ItemStack[36];
		for (int a = 0; a < 36; a++) {
			is[a] = p.getInventory().getItem(a);
		}

		for (int i2 = 0; i2 < is.length; i2++) {
			if (is[i2] != null && (is[i2].getTypeId() == Main.config.getInt("SlashId2"))
					&& (is[i2].getDurability() == 0)) {
				number += is[i2].getAmount();
			}
		}

		boolean bool = number >= getMaterial(i);
		return bool;
	}
	private static int getMaterial(int strength)
	{
		if (strength < 5) return 20;
		else if (strength < 9) return 32;
		else if (strength < 11) return 48;
		else if (strength < 16) return 64;
		else return 64 + 10 * (strength - 15);
	}
	private static int getCoin(int strength) {
		return new ChanceGetter(strength).coin;
	}
	private void getRequire(ItemStack item, List<String> list, Player p) {
		SlashStrength ss = new SlashStrength(item);
		int repair = ss.getRepairCount();
		int strength = ss.getStrength();
		ChanceGetter cg = new ChanceGetter(strength);
		list.add("当前强化等级:" + strength);
		list.add("强化至下一级,锻造数将会变成:" + (repair + strength));
		list.add("成功率:" + cg.chance + "%");
		if (cg.ifBreak) {
			list.add("如果失败:武器将会损坏,永远消失");
		} else if (cg.ifTo0) {
			list.add("如果失败:武器强化等级归0");
		} else if (cg.punish == 0) {
			list.add("如果失败:并不会对武器产生影响");
		} else {
			list.add("如果失败:强化等级减少" + cg.punish);
		}
		list.add("需要材料:耀魂碎片 " + getMaterial(strength) + "个");
		list.add("需要金钱：" + getCoin(strength));
		list.add("请将材料放于背包中,点击确认开始强化");
		list.add("背包里放一些道具可提高成功率,或失败保护");
	}

}
