package me.jonesdev.blackjackplugin;

import org.bukkit.plugin.java.JavaPlugin;

import me.jonesdev.blackjackplugin.commands.BlackjackCommand;
import me.jonesdev.blackjackplugin.events.BlackjackGame;
import me.jonesdev.blackjackplugin.events.EventsClass;
import me.jonesdev.blackjackplugin.utils.Utils;


public class BlackjackPlugin extends JavaPlugin{
	private FileConfig cfg;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		loadConfigManager();
		
		new BlackjackCommand(this);
		new BlackjackGame();
		new EventsClass();
		
		getServer().getConsoleSender().sendMessage(Utils.chat("&eBlackjack Plugin is successfully enabled."));
		getServer().getPluginManager().registerEvents(new EventsClass(), this);
		
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(Utils.chat("&eBlackjack Plugin has been disabled."));
	}
	
	public void loadConfigManager() {
		cfg = new FileConfig();
		cfg.setup();
	}
	
}
