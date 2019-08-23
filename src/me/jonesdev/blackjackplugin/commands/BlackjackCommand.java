package me.jonesdev.blackjackplugin.commands;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.events.BlackjackGame;
import me.jonesdev.blackjackplugin.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        //Setup Variables
		Player p = (Player) sender;
		Economy economy = BlackjackPlugin.getEconomy();

        try {
            double bankroll = Double.parseDouble(arg[0]);
        } catch (NumberFormatException ignore) {
            sender.sendMessage(Utils.chat("&c&l[!] &cCorrect usage is /blackjack [money] "));
            return true;
        }

        double bankroll = Double.valueOf(arg[0]);

        if (bankroll < plugin.getConfig().getInt("BlackjackGame.MinBuyIn")){
            p.sendMessage(Utils.chat("&c&l[!] &cYou do not meet the minimum buy in of $" +plugin.getConfig().getInt("BlackjackGame.MinBuyIn") +"."));
            return true;
        }

        else if (bankroll > economy.getBalance((OfflinePlayer) sender)){
            sender.sendMessage(Utils.chat("&c&l[!] &cYou do not have that much money!"));
            return true;
        }

        EconomyResponse response = economy.withdrawPlayer((OfflinePlayer)sender, bankroll);

        if (response.transactionSuccess()){
            //Save Bankroll
            FileConfig.getBlackjackFile().set(p.getUniqueId().toString()+ ".Bankroll", String.format("%.2f", bankroll));
            FileConfig.getBlackjackFile().set(p.getUniqueId().toString()+ ".InitialBuyIn", bankroll);

            //Open GameMenu
            BlackjackGame bg = new BlackjackGame();
            bg.menuInventory(p);
            return true;
        }
        else{
            sender.sendMessage(Utils.chat("&c&l[!] &cFailed to withdraw money from your balance."));
            sender.sendMessage(Utils.chat("&c&l[!] &cDo you have enough money to play?"));
            return true;
        }

	}

}
