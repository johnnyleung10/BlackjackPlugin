package me.jonesdev.blackjackplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.utils.Utils;



public class BlackjackGame implements Listener{
	
	private Plugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);
	
	public void newInventory(Player player) {
		Inventory i = plugin.getServer().createInventory(null, 54, Utils.chat(plugin.getConfig().getString("blackjackgui_name")));
	}
	
	ItemStack []
	

}
