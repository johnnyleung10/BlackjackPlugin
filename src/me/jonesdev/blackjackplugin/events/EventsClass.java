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

import java.io.File;
import java.util.Arrays;
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
				FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", 0);

				//Setup base values for betting chips
				setup(player);

				bg.betInventory(player);


			}

			//Add $5 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$5 Chip"))&&(click.isLeftClick())){
				{
					//Bankroll check
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					if (bankroll < 5){
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					}
					else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip5.Amt");
						if (chips < 1){
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip5.Amt", chips);
						}
						else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip5.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip5.Stack", chips);
						}

						bankroll -= 5;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 5;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Subtract $5 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$5 Chip"))&&(click.isRightClick())){
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip5.Stack");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip5.Amt")==0){
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					}
					else {
						//Decrease chips
						if (chips > 1){
							chips--;
						}
						else if (chips == 1){
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip5.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip5.Amt")-1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip5.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 5;
						bankroll += 5;

					}

					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Add $25 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$25 Chip"))&&(click.isLeftClick())){
				{
					//Bankroll check
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					if (bankroll < 25){
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					}
					else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip25.Amt");
						if (chips < 1){
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip25.Amt", chips);
						}
						else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip25.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip25.Stack", chips);
						}

						bankroll -= 25;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 25;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);;
			}
			//Subtract $25 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$25 Chip"))&&(click.isRightClick())){
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip25.Stack");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip25.Amt")==0){
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					}
					else {
						//Decrease chips
						if (chips > 1){
							chips--;
						}
						else if (chips == 1){
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip25.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip25.Amt")-1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip25.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 25;
						bankroll += 25;

					}

					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);;
			}
			//Add $100 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$100 Chip"))&&(click.isLeftClick())){
				{


					//Bankroll check
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					if (bankroll < 100){
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					}
					else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip100.Amt");
						if (chips < 1){
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip100.Amt", chips);
						}
						else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip100.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip100.Stack", chips);
						}
						bankroll -= 100;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 100;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);;
			}
			//Subtract $100 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$100 Chip"))&&(click.isRightClick())){
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip100.Stack");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip100.Amt")==0){
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					}
					else {
						//Decrease chips
						if (chips > 1){
							chips--;
						}
						else if (chips == 1){
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip100.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip100.Amt")-1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip100.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 100;
						bankroll += 100;
					}
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);;
			}
			//Add $500 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$500 Chip"))&&(click.isLeftClick())){
				{
					//Bankroll check
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					if (bankroll < 500){
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					}
					else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip500.Amt");
						if (chips < 1){
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip500.Amt", chips);
						}
						else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip500.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip500.Stack", chips);
						}
						bankroll -= 500;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 500;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);;
			}
			//Subtract $500 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$500 Chip"))&&(click.isRightClick())){
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip500.Stack");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip500.Amt")==0){
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					}
					else {
						//Decrease chips
						if (chips > 1){
							chips--;
						}
						else if (chips == 1){
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip500.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip500.Amt")-1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip500.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 500;
						bankroll += 500;

					}
					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Add $1000 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$1000 Chip."))&&(click.isLeftClick())){
				{
					//Bankroll check
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					if (bankroll < 1000){
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					}
					else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip1000.Amt");
						if (chips < 1){
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip1000.Amt", chips);
						}
						else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip1000.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip1000.Stack", chips);
						}
						bankroll -= 1000;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 1000;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Subtract $1000 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$1000 Chip."))&&(click.isRightClick())){
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip1000.Stack");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip1000.Amt")==0){
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					}
					else {
						//Decrease chips
						if (chips > 1){
							chips--;
						}
						else if (chips == 1){
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip1000.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip1000.Amt")-1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Chip1000.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 1000;
						bankroll += 1000;

					}

					FileConfig.getBlackjackFile().set(""+player.getUniqueId()+".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}

			//Deal Cards
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

				//Player second card
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

				//Set Position of Player Cards
				int playerPosition = 29;
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerPosition", playerPosition);

				int dealerPosition = 1;
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".DealerPosition", dealerPosition);

				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Count",count);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Deck", deck);
				FileConfig.saveBlackjack();

				bg.gameInventory(player);
			}
			//Hit
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Hit"))){
				//Retrieve Deck and count
				List<String> deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString()+".Deck");
				int count = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".Count");

				//Retrieve position
				int position = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".PlayerPosition");

				//Create new card
				count++;
				position++;
				String card = Blackjack.deal(deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Data", card);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Amount", Blackjack.cardNum(card));
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Cards.Card"+count+".Material", Blackjack.material(card));
				ItemStack cardItem = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card" +count +".Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card" +count +".Amount"));{
					ItemMeta meta = cardItem.getItemMeta();
					meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card" +count +".Data"))));
					cardItem.setItemMeta(meta);
				}
				open.setItem(position, cardItem);


				//Add card value to total
				int numPlayerTotal = Blackjack.cardTotal(new String[] {card})+ FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".PlayerTotal");
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerTotal", numPlayerTotal);
				ItemStack playerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".PlayerTotal"));{
					ItemMeta meta = playerTotal.getItemMeta();
					meta.setDisplayName(Utils.chat("&DTotal"));
					playerTotal.setItemMeta(meta);
				}
				open.setItem(35, playerTotal);

				//If Bust
				if (numPlayerTotal>21){
					//Add a lose message
					ItemStack loseMessage = new ItemStack(Material.PAPER, 1);{
						ItemMeta meta = loseMessage.getItemMeta();
						meta.setDisplayName(Utils.chat("&cYou busted! You lose $"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount")));
						meta.setLore(Arrays.asList("","", Utils.chat("&5Click on the book to restart!"), "", ""));
						loseMessage.setItemMeta(meta);
					}
					open.setItem(40, loseMessage);

					//Remove hit and stand buttons
					ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
						ItemMeta meta = empty2.getItemMeta();
						empty2.setItemMeta(meta);
					}
					open.setItem(48, empty2);
					open.setItem(50, empty2);

					//Add a reset button
					ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
						ItemMeta meta = resetButton.getItemMeta();
						meta.setDisplayName(Utils.chat("&aReset"));
						resetButton.setItemMeta(meta);
					}
					open.setItem(49, resetButton);
				}

				else if (numPlayerTotal==21){
					stand(player, open);
				}

				//Save changes
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Count",count);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerPosition", position);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Deck", deck);
				FileConfig.saveBlackjack();
				player.updateInventory();
			}
			//Stand
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Stand"))){
				//Retrieve Deck and count
				List<String> deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString()+".Deck");
				int count = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".Count");

				//Retrieve dealer position
				int position = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".DealerPosition");

				//Setup dealer and player totals
				int numDealerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal");
				int numPlayerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".PlayerTotal");

				//Keep hitting until at least 17
				while (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".DealerTotal")<17){
					//Create new card
					count++;
					position++;
					String card = Blackjack.deal(deck);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", card);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(card));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(card));

					//Add to total
					numDealerTotal += Blackjack.cardTotal(new String[]{card});
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerTotal", numDealerTotal);
					ItemStack dealerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal"));{
						ItemMeta meta = dealerTotal.getItemMeta();
						meta.setDisplayName(Utils.chat("&DTotal"));
						dealerTotal.setItemMeta(meta);
					}
					open.setItem(8, dealerTotal);

					ItemStack cardItem = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount"));{
						ItemMeta meta = cardItem.getItemMeta();
						meta.setDisplayName(Utils.chat("&2" + Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data"))));
						cardItem.setItemMeta(meta);
					}
					open.setItem(position, cardItem);

					//Save changes
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Count",count);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Deck", deck);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".DealerPosition", position);
					FileConfig.saveBlackjack();
					player.updateInventory();
				}

				//If Dealer Busts
				if (numDealerTotal>21){
					//Add a win message
					ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
						ItemMeta meta = winMessage.getItemMeta();
						meta.setDisplayName(Utils.chat("&a&lDealer busted!"));
						meta.setLore(Arrays.asList("","", Utils.chat("&5You win &6$"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount")), Utils.chat("&5Click on the book to restart!"), "", ""));
						winMessage.setItemMeta(meta);
					}
					open.setItem(40, winMessage);

					//Win money
					int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					bankroll += winMoney*2;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

					//Remove hit and stand buttons
					ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
						ItemMeta meta = empty2.getItemMeta();
						empty2.setItemMeta(meta);
					}
					open.setItem(48, empty2);
					open.setItem(50, empty2);

					//Add a reset button
					ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
						ItemMeta meta = resetButton.getItemMeta();
						meta.setDisplayName(Utils.chat("&aReset"));
						resetButton.setItemMeta(meta);
					}
					open.setItem(49, resetButton);
				}

				//Win condition
				else if (numDealerTotal<numPlayerTotal){
					//Add a win message
					ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
						ItemMeta meta = winMessage.getItemMeta();
						meta.setDisplayName(Utils.chat("&a&lYou win!"));
						meta.setLore(Arrays.asList("","", Utils.chat("&5You receive &6$"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount")), Utils.chat("&5Click on the book to restart!"), "", ""));
						winMessage.setItemMeta(meta);
					}
					open.setItem(40, winMessage);

					//Win money
					int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					bankroll += winMoney*2;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

					//Remove hit and stand buttons
					ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
						ItemMeta meta = empty2.getItemMeta();
						empty2.setItemMeta(meta);
					}
					open.setItem(48, empty2);
					open.setItem(50, empty2);

					//Add a reset button
					ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
						ItemMeta meta = resetButton.getItemMeta();
						meta.setDisplayName(Utils.chat("&aReset"));
						resetButton.setItemMeta(meta);
					}
					open.setItem(49, resetButton);
				}

				//Lose condition
				else if (numDealerTotal>numPlayerTotal){
					//Add a lose message
					ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
						ItemMeta meta = winMessage.getItemMeta();
						meta.setDisplayName(Utils.chat("&cYou lose! $"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount") +" has been deducted."));
						meta.setLore(Arrays.asList("","", Utils.chat("&5Click on the book to restart!"), "", ""));
						winMessage.setItemMeta(meta);
					}
					open.setItem(40, winMessage);

					//Remove hit and stand buttons
					ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
						ItemMeta meta = empty2.getItemMeta();
						empty2.setItemMeta(meta);
					}
					open.setItem(48, empty2);
					open.setItem(50, empty2);

					//Add a reset button
					ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
						ItemMeta meta = resetButton.getItemMeta();
						meta.setDisplayName(Utils.chat("&aReset"));
						resetButton.setItemMeta(meta);
					}
					open.setItem(49, resetButton);
				}

				//Push Condition
				else {
					//Add a push condition message
					ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
						ItemMeta meta = winMessage.getItemMeta();
						meta.setDisplayName(Utils.chat("&9&lPush, nobody wins or loses"));
						meta.setLore(Arrays.asList("","", Utils.chat("&5You get &6$"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount") +"&5 back."), Utils.chat("&5Click on the book to restart!"), "", ""));
						winMessage.setItemMeta(meta);
					}
					open.setItem(40, winMessage);

					//Win money
					int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");
					int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
					bankroll += winMoney;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

					//Remove hit and stand buttons
					ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
						ItemMeta meta = empty2.getItemMeta();
						empty2.setItemMeta(meta);
					}
					open.setItem(48, empty2);
					open.setItem(50, empty2);

					//Add a reset button
					ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
						ItemMeta meta = resetButton.getItemMeta();
						meta.setDisplayName(Utils.chat("&aReset"));
						resetButton.setItemMeta(meta);
					}
					open.setItem(49, resetButton);
				}
			}
			//Reset game
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&aReset"))){
				resetGame(player);
				bg.betInventory(player);
			}
		}
	}

	private void setup(Player p){
		//Real amount of chips
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip5.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip25.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip100.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip500.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip1000.Amt", 0);

		//ItemStack stack value
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip5.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip25.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip100.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip500.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".Chip1000.Stack", 1);

		FileConfig.saveBlackjack();
	}

	private void resetGame(Player player){
		FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".BetAmount", 0);
		setup(player);
		FileConfig.saveBlackjack();
	}

	private void stand(Player player, Inventory open){
		//Retrieve Deck and count
		List<String> deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString()+".Deck");
		int count = FileConfig.getBlackjackFile().getInt(player.getUniqueId()+".Count");

		//Retrieve dealer position
		int position = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".DealerPosition");

		//Setup dealer and player totals
		int numDealerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal");
		int numPlayerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".PlayerTotal");

		//Keep hitting until at least 17
		while (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".DealerTotal")<17){
			//Create new card
			count++;
			position++;
			String card = Blackjack.deal(deck);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", card);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(card));
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(card));

			//Add to total
			numDealerTotal += Blackjack.cardTotal(new String[]{card});
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerTotal", numDealerTotal);
			ItemStack dealerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal"));{
				ItemMeta meta = dealerTotal.getItemMeta();
				meta.setDisplayName(Utils.chat("&DTotal"));
				dealerTotal.setItemMeta(meta);
			}
			open.setItem(8, dealerTotal);

			ItemStack cardItem = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount"));{
				ItemMeta meta = cardItem.getItemMeta();
				meta.setDisplayName(Utils.chat("&2" + Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data"))));
				cardItem.setItemMeta(meta);
			}
			open.setItem(position, cardItem);

			//Save changes
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Count",count);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".Deck", deck);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".DealerPosition", position);
			FileConfig.saveBlackjack();
			player.updateInventory();
		}

		//If Dealer Busts
		if (numDealerTotal>21){
			//Add a win message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&a&lDealer busted!"));
				meta.setLore(Arrays.asList("","", Utils.chat("&5You win &6$"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount")), Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Win money
			int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");
			int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
			bankroll += winMoney*2;
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}

		//Win condition
		else if (numDealerTotal<numPlayerTotal){
			//Add a win message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&a&lYou win!"));
				meta.setLore(Arrays.asList("","", Utils.chat("&5You receive &6$"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount")), Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Win money
			int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");
			int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
			bankroll += winMoney*2;
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}

		//Lose condition
		else if (numDealerTotal>numPlayerTotal){
			//Add a lose message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&cYou lose! $"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount") +" has been deducted."));
				meta.setLore(Arrays.asList("","", Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}

		//Push Condition
		else {
			//Add a push condition message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&9&lPush, nobody wins or loses"));
				meta.setLore(Arrays.asList("","", Utils.chat("&5You get &6$"+FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount") +"&5 back."), Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Win money
			int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");
			int bankroll = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Bankroll");
			bankroll += winMoney;
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Bankroll", bankroll);

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1); {
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}
	}
	
}
