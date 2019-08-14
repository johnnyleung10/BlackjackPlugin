package me.jonesdev.blackjackplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jonesdev.blackjackplugin.BlackjackPlugin;

public class BlackjackCommand implements CommandExecutor{
	
private BlackjackPlugin plugin;
	
	public BlackjackCommand(BlackjackPlugin plugin) {
		this.plugin = plugin;
		plugin.getCommand("rewards").setExecutor(this);
	}
	
	@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
			
			return true;
		}

}
