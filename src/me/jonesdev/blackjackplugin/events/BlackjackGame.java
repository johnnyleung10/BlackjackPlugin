package me.jonesdev.blackjackplugin.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.utils.Utils;



public class BlackjackGame implements Listener{
	
	private Plugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);
	
	public void newInventory(Player player) {
		Inventory i = plugin.getServer().createInventory(null, 54, Utils.chat(plugin.getConfig().getString("BlackjackGUI.Title")));
		
		
		ItemStack startButton = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta startMeta = startButton.getItemMeta();
		startMeta.setDisplayName(Utils.chat("&a&lStart"));
		startButton.setItemMeta(startMeta);
		
		//Fill with green glass
		ItemStack empty1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
		ItemMeta empty1Meta = empty1.getItemMeta();
		empty1.setItemMeta(empty1Meta);
		
		//Fill with blue glass
		ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
		ItemMeta empty2Meta = empty2.getItemMeta();
		empty2.setItemMeta(empty2Meta);
		
		
		for(int slot = 0; slot < 27; slot++){
	        if(slot!=13) {
	        	i.setItem(slot, empty1);
	        }
		}
		
		for(int slot = 27; slot < i.getSize(); slot++){
			i.setItem(slot, empty2);    
		}
		
		i.setItem(13, startButton);
		
		player.openInventory(i);
	}
	
	public void gameInventory(Player player){
		Inventory i = plugin.getServer().createInventory(null, 54, Utils.chat(plugin.getConfig().getString("BlackjackGUI.Title")));
		
		//Fill with green glass
		ItemStack empty1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
		ItemMeta empty1Meta = empty1.getItemMeta();
		empty1.setItemMeta(empty1Meta);
				
		//Fill with blue glass
		ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
		ItemMeta empty2Meta = empty2.getItemMeta();
		empty2.setItemMeta(empty2Meta);
				
				
		for(int slot = 0; slot < 27; slot++){
			i.setItem(slot, empty1);     
		}
				
		for(int slot = 27; slot < i.getSize(); slot++){
			i.setItem(slot, empty2);    
		}
	}
	
	
	

}
