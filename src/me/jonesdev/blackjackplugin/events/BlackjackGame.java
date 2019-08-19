package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

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
		ItemStack empty1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);{
			ItemMeta meta = empty1.getItemMeta();
			empty1.setItemMeta(meta);
		}
				
		//Fill with blue glass
		ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
			ItemMeta meta = empty2.getItemMeta();
			empty2.setItemMeta(meta);
		}


		for(int slot = 0; slot < 18; slot++){
			i.setItem(slot, empty1);     
		}
				
		for(int slot = 18; slot < i.getSize(); slot++){
			i.setItem(slot, empty2);
		}

		//Creates dealer head
		ItemStack dealerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());{
			SkullMeta meta = (SkullMeta) dealerHead.getItemMeta();
			meta.setOwner(plugin.getConfig().getString("BlackjackGame.Dealer.Head"));
			meta.setDisplayName(Utils.chat(plugin.getConfig().getString("BlackjackGame.Dealer.Name")));
			dealerHead.setItemMeta(meta);
		}

		//Creates player head
		ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());{
			SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
			meta.setOwner(player.getName());
			meta.setDisplayName(Utils.chat("&6Player "+player.getName()));
			playerHead.setItemMeta(meta);
		}

		ItemStack bet5 = new ItemStack(Material.INK_SACK, 1, (byte) 1);{
			ItemMeta meta = bet5.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Bet $5."));
			bet5.setItemMeta(meta);
		}

		ItemStack bet25 = new ItemStack(Material.INK_SACK, 1, (byte) 14);{
			ItemMeta meta = bet25.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Bet $25."));
			bet25.setItemMeta(meta);
		}

		ItemStack bet100 = new ItemStack(Material.INK_SACK, 1, (byte) 11);{
			ItemMeta meta = bet100.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Bet $100."));
			bet100.setItemMeta(meta);
		}

		ItemStack bet500 = new ItemStack(Material.INK_SACK, 1, (byte) 2);{
			ItemMeta meta = bet500.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Bet $500."));
			bet500.setItemMeta(meta);
		}

		ItemStack bet1000 = new ItemStack(Material.INK_SACK, 1, (byte) 4);

		ItemStack betAmount = new ItemStack(Material.PAPER, 1);{
			ItemMeta meta = betAmount.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Current Bet: " + FileConfig.getBlackjackFile().getString(player.getUniqueId() + ".BetAmount")));

			betAmount.setItemMeta(meta);
		}

		i.setItem(0, dealerHead);
		i.setItem(18, playerHead);
		i.setItem(20, bet5);
		i.setItem(21, bet25);
		i.setItem(22, bet100);
		i.setItem(23, bet500);
		i.setItem(24, bet1000);

		i.setItem(26, betAmount);

		player.openInventory(i);
	}
	
	
	

}
