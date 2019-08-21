package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.FileConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.utils.Utils;

import java.util.List;


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
				bg.betInventory(player);
			}

			//Add $5 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $5."))&&(click.isLeftClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount += 5;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);
			}
			//Subtract $5 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $5."))&&(click.isRightClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount -= 5;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);
			}
			//Add $25 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $25."))&&(click.isLeftClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount += 25;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}
			//Subtract $25 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $25."))&&(click.isRightClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount -= 25;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}
			//Add $100 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $100."))&&(click.isLeftClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount += 100;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}
			//Subtract $100 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $100."))&&(click.isRightClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount -= 100;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}
			//Add $500 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $500."))&&(click.isLeftClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount += 5;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}
			//Subtract $500 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $500."))&&(click.isRightClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount -= 500;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}
			//Add $1000 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $1000."))&&(click.isLeftClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount += 1000;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);
			}
			//Subtract $1000 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&4Bet $1000."))&&(click.isRightClick())){
				{
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");
					betAmount -= 1000;
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
				}
				bg.betInventory(player);;
			}

			if (item.getItemMeta().getDisplayName().equals((Utils.chat("&aDeal")))){
				List<String> deck = Blackjack.deckMaker();
				Blackjack.shuffle(deck);
				int count = 1;

				//Players first card
				String p1 = Blackjack.deal(deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Data", p1);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Amount", Blackjack.cardNum(p1));
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Material", Blackjack.material(p1));
				//Dealers first card
				count++;
				String d1 = Blackjack.deal(deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Data", d1);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Amount", Blackjack.cardNum(d1));
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Material", Blackjack.material(d1));
				count++;
				String p2 = Blackjack.deal(deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Data", p2);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Amount", Blackjack.cardNum(p2));
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Material", Blackjack.material(p2));

				//Add up values
				int playerTotal = Blackjack.cardTotal(new String[]{p1, p2});
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerTotal", playerTotal);
				int dealerTotal = Blackjack.cardTotal(new String[]{d1});
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".DealerTotal", dealerTotal);

				//Set Position
				int position = 29;
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Position", position);

				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Count",count);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Deck", deck);
				FileConfig.saveBlackjack();

				bg.gameInventory(player);
			}

			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Hit"))){
				//Retrieve Deck and count
				List<String> deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString()+".Deck");
				int count = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".Count");

				//Retrieve position
				int position = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Position");

				//Create new card
				count++;
				position++;
				String card = Blackjack.deal(deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Data", card);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Amount", Blackjack.cardNum(card));
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Material", Blackjack.material(card));
				//Add to total
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerTotal", Blackjack.cardTotal(new String[] {card})+ FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".PlayerTotal"));

				ItemStack cardItem = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card" +count +".Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card" +count +".Amount"));{
					ItemMeta meta = cardItem.getItemMeta();
					meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card" +count +".Data"))));
					cardItem.setItemMeta(meta);
				}
				//Set card down
				open.setItem(position, cardItem);

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
				open.setItem(8, dealerTotal);
				open.setItem(35, playerTotal);

				//Save changes
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Count",count);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Deck", deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Position", position);
				FileConfig.saveBlackjack();
				player.updateInventory();
			}
		}
	}
	
	
}
