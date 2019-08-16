package me.jonesdev.blackjackplugin.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.BlackjackPlugin;

public class BlackjackCommand implements CommandExecutor{
	
private BlackjackPlugin plugin;
	
	public BlackjackCommand(BlackjackPlugin plugin) {
		this.plugin = plugin;
		plugin.getCommand("blackjack").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		List<String> deck = Blackjack.deckMaker();
		Blackjack.shuffle(deck);
		sender.sendMessage(deck.toString());
		sender.sendMessage(Blackjack.deal(deck));
		sender.sendMessage(Blackjack.deal(deck));
		sender.sendMessage(Blackjack.deal(deck));
		
			
	return true;
	}

}
