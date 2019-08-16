package me.jonesdev.blackjackplugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.jonesdev.blackjackplugin.utils.Utils;

public class FileConfig {
	private BlackjackPlugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);
	
	//Files and File configs here
	public FileConfiguration blackjackcfg;
	public File blackjackfile;
	//----------------------------
	
	
	public void setup() {
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		blackjackfile = new File(plugin.getDataFolder(), "blackjack.yml");
		
		if(!blackjackfile.exists()) {
			try {
				blackjackfile.createNewFile();
			} catch(IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&cCould not create blackjack.yml file."));
			}
		}
		
		blackjackcfg = YamlConfiguration.loadConfiguration(blackjackfile);
		Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&2blackjack.yml file has been successfully created."));
		
	}
}
