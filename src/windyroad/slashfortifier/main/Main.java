package windyroad.slashfortifier.main;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
		config = getConfig();
		doReloadConfig(file);
		getLogger().info("成功加载拔刀强化插件");
		getLogger().info("作者:WindyRoad");

		super.onEnable();
	}

	public void doReloadConfig(File file){
		try {
			config.load(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
		} catch (IOException e) {
			getLogger().info("读取拔刀剑强化插件配置失败！");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			getLogger().info("读取拔刀剑强化插件配置失败！");
			e.printStackTrace();
		}
	}

	public void onDisable() {
		getLogger().info("强化插件已卸载");
	}
}
