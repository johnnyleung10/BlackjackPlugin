package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.utils.Utils;
import org.bukkit.Bukkit;
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

import java.io.File;
import java.util.List;

public class BlackjackGame implements Listener{
	
	private Plugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);
	public static List<String> deck;
	
	public void menuInventory(Player player) {
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
	
	public void betInventory(Player player){
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

		ItemStack bet1000 = new ItemStack(Material.INK_SACK, 1, (byte) 4);{
			ItemMeta meta = bet1000.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Bet $1000."));
			bet1000.setItemMeta(meta);
		}

		//Paper that shows current bet amount
		ItemStack betAmount = new ItemStack(Material.PAPER, 1);{
			ItemMeta meta = betAmount.getItemMeta();
			meta.setDisplayName(Utils.chat("&4Current Bet: $" + FileConfig.getBlackjackFile().getString(player.getUniqueId() + ".BetAmount")));

			betAmount.setItemMeta(meta);
		}

		ItemStack dealButton = new ItemStack(Material.BOOK, 1); {
			ItemMeta meta = dealButton.getItemMeta();
			meta.setDisplayName(Utils.chat("&aDeal"));
			dealButton.setItemMeta(meta);
		}
		//Set Player Heads
		i.setItem(0, dealerHead);
		i.setItem(18, playerHead);
		//Set Bet Increments
		i.setItem(20, bet5);
		i.setItem(21, bet25);
		i.setItem(22, bet100);
		i.setItem(23, bet500);
		i.setItem(24, bet1000);
		//Set Other Buttons and Controls
		i.setItem(26, betAmount);
		i.setItem(49, dealButton);

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

		//Set Player Heads
		i.setItem(0, dealerHead);
		i.setItem(18, playerHead);


		//Player first card
		ItemStack p1 = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card1.Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card1.Amount"));{
			ItemMeta meta = p1.getItemMeta();
			meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card1.Data"))));
			p1.setItemMeta(meta);
		}
		//Dealer first card
		ItemStack d1 = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card2.Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card2.Amount"));{
			ItemMeta meta = d1.getItemMeta();
			meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card2.Data"))));
			d1.setItemMeta(meta);
		}
		//Player first card
		ItemStack p2 = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card3.Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card3.Amount"));{
			ItemMeta meta = p2.getItemMeta();
			meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card3.Data"))));
			p2.setItemMeta(meta);
		}
		//First cards dealt
		i.setItem(28, p1);
		i.setItem(3, d1);
		i.setItem(29, p2);

		//CardTotals
		ItemStack playerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".PlayerTotal"));{
			ItemMeta meta = playerTotal.getItemMeta();
			meta.setDisplayName(Utils.chat("&DTotal"));
			playerTotal.setItemMeta(meta);
		}
		ItemStack dealerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".DealerTotal"));{
			ItemMeta meta = dealerTotal.getItemMeta();
			meta.setDisplayName(Utils.chat("&DTotal"));
			dealerTotal.setItemMeta(meta);
		}
		i.setItem(8, dealerTotal);
		i.setItem(35, playerTotal);

		//Player functions
		ItemStack hit = new ItemStack(Material.GOLD_PLATE, 1);{
			ItemMeta meta = hit.getItemMeta();
			meta.setDisplayName(Utils.chat("&6Hit"));
			hit.setItemMeta(meta);
		}
		ItemStack stand = new ItemStack(Material.BREWING_STAND_ITEM, 1);{
			ItemMeta meta = stand.getItemMeta();
			meta.setDisplayName(Utils.chat("&6Stand"));
			stand.setItemMeta(meta);
		}

		i.setItem(48, hit);
		i.setItem(50, stand);

		player.openInventory(i);
	}
	
	
	

}
