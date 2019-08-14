package me.jonesdev.blackjackplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.jonesdev.blackjackplugin.BlackjackPlugin;


public class EventsClass implements Listener{
	
	private Plugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);

	@EventHandler
	public void InvenClick(InventoryClickEvent e){
		Player player = (Player) e.getWhoClicked();
		
		ClickType click = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		
		if (open == null){
			return;
		}
	}
	
	
}
