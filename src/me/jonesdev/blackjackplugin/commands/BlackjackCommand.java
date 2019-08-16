package me.jonesdev.blackjackplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.events.BlackjackGame;

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
		Player p = (Player) sender;
		BlackjackGame bg = new BlackjackGame();
		
		bg.newInventory(p);
		return true;
	}

}
