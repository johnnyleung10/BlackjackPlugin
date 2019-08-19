package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.FileConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.utils.Utils;


public class EventsClass implements Listener{
	
	private Plugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);

	@EventHandler
	public void InvenClick(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		BlackjackGame bg = new BlackjackGame();
		ClickType click = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		
		if (open == null){
			return;
		}
		
		//Detect when blackjack window is open
		if (open.getName().equals(Utils.chat(plugin.getConfig().getString("BlackjackGUI.Title")))){
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()){
				return;
			}
			//Start Button
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&a&lStart"))&&(click.isLeftClick()||click.isRightClick())) {
				FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".Playing", true);
				FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", 0);
				bg.gameInventory(player);
			}
			//Add $5 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $5."))&&(click.isLeftClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount += 5;
					player.getServer().getConsoleSender().sendMessage("Bet is now "+betAmount);
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}

				bg.gameInventory(player);
			}
			//Subtract $5 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $5."))&&(click.isRightClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount -= 5;
					player.getServer().getConsoleSender().sendMessage("Bet is now "+betAmount);
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.gameInventory(player);
			}


		}
	}
	
	
}
