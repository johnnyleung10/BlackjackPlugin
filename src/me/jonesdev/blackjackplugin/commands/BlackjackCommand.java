package me.jonesdev.blackjackplugin.commands;

import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.events.BlackjackGame;

import java.io.File;

public class BlackjackCommand implements CommandExecutor{
	
	private BlackjackPlugin plugin;



	
	public BlackjackCommand(BlackjackPlugin plugin) {
		this.plugin = plugin;
		plugin.getCommand("blackjack").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use this command");
			return true;
		}

		if (arg.length != 1){
			sender.sendMessage(Utils.chat("&c&l[!] &cCorrect usage is /blackjack [money] "));
			return true;
		}

		int bankroll = Integer.parseInt(arg[0]);

		Player p = (Player) sender;
		//Save Bankroll
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString()+ ".Bankroll", bankroll);

		//Open GameMenu
		BlackjackGame bg = new BlackjackGame();
		bg.menuInventory(p);

		return true;
	}

}
