package me.jonesdev.blackjackplugin;

import me.jonesdev.blackjackplugin.commands.BlackjackCommand;
import me.jonesdev.blackjackplugin.events.BlackjackGame;
import me.jonesdev.blackjackplugin.events.EventsClass;
import me.jonesdev.blackjackplugin.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class BlackjackPlugin extends JavaPlugin{
	private FileConfig cfg;
	private static Economy econ = null;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		loadConfigManager();

		new BlackjackCommand(this);
		new BlackjackGame();
		new EventsClass();

		//Vault
		if (!setupEconomy()) {
			getServer().getConsoleSender().sendMessage(Utils.chat("&cDisabled due to no Vault dependency found!"));
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		getServer().getConsoleSender().sendMessage(Utils.chat("&eBlackjack Plugin is successfully enabled."));
		getServer().getPluginManager().registerEvents(new EventsClass(), this);
	}

	@Override
	public void onDisable() {
		cfg.saveBlackjack();
		getServer().getConsoleSender().sendMessage(Utils.chat("&eBlackjack Plugin has been disabled."));
	}

	private void loadConfigManager() {
		cfg = new FileConfig();
		cfg.setup();
		cfg.saveBlackjack();
	}

	private boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

}
