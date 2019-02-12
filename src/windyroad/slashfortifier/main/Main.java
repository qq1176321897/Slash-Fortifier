package windyroad.slashfortifier.main;

import java.io.File;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import windyroad.slashfortifier.Commands;
import windyroad.slashfortifier.GuiCloseEvent;
import windyroad.slashfortifier.GuiEvent;
import windyroad.slashfortifier.ShiftRightClickAnvilEvent;

public class Main extends JavaPlugin {
	public static FileConfiguration config = null;

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ShiftRightClickAnvilEvent(), this);
		Bukkit.getPluginManager().registerEvents(new GuiEvent(this), this);
		Bukkit.getPluginManager().registerEvents(new GuiCloseEvent(), this);
		getCommand("slashfortifier").setExecutor(new Commands(this));
		getCommand("sff").setExecutor(new Commands(this));

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveDefaultConfig();
		}
		reloadConfig();
		config = getConfig();
		getLogger().info("成功加载拔刀强化插件");
		getLogger().info("作者:WindyRoad");

		super.onEnable();
	}

	public void onDisable() {
		getLogger().info("强化插件已卸载");
	}
}
