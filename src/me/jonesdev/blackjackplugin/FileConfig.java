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
	private static FileConfiguration blackjackcfg;
	public static File blackjackfile;
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
	}

	public static FileConfiguration getBlackjackFile(){
		return blackjackcfg;
	}

	public static void saveBlackjack(){
		try{
			blackjackcfg.save(blackjackfile);
		} catch (IOException e){
			Bukkit.getServer().getConsoleSender().sendMessage(Utils.chat("&cCould not save blackjack.yml file."));
		}
	}

	public void reloadBlackjack(){
		blackjackcfg = YamlConfiguration.loadConfiguration(blackjackfile);
	}


}
