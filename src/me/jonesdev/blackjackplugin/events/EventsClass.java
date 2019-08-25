package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;


public class EventsClass implements Listener {

	private Plugin plugin = BlackjackPlugin.getPlugin(BlackjackPlugin.class);
	//Variable Declaration//

	Economy economy = BlackjackPlugin.getEconomy();

	//Number of cards
	private int numOfPlayerCards = 0;
	private int numOfDealerCards = 0;

	//Totals
	private int numPlayerTotal = 0;
	private int numDealerTotal = 0;

	int count = 1;

	//Player Positions
	private int playerPosition = 0;
	private int earlyPlayerPosition = 0;
	//DealerPositions
	private int dealerPosition = 0;
	private int earlyDealerPosition;


	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		BlackjackGame bg = new BlackjackGame();
		ClickType click = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();

		if (open == null) {
			return;
		}

		//Detect when blackjack window is open
		if (open.getName().equals(Utils.chat(plugin.getConfig().getString("BlackjackGUI.Title")))) {
			e.setCancelled(true);

			if (item == null || !item.hasItemMeta()) {
				return;
			}

			//Start Button
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&a&lStart")) && (click.isLeftClick() || click.isRightClick())) {
				FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", 0);

				//Setup base values for betting chips
				setup(player);


				bg.betInventory(player);
			}

			//Add $5 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$5 Chip")) && (click.isLeftClick())) {
				{
					//Bankroll check
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					if (bankroll < 5) {
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					} else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip5.Amt");
						if (chips < 1) {
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip5.Amt", chips);
						} else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip5.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip5.Stack", chips);
						}

						bankroll -= 5;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
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
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$5 Chip")) && (click.isRightClick())) {
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip5.Stack");
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip5.Amt") == 0) {
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					} else {
						//Decrease chips
						if (chips > 1) {
							chips--;
						} else if (chips == 1) {
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip5.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip5.Amt") - 1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip5.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 5;
						bankroll += 5;

					}

					FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Add $25 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$25 Chip")) && (click.isLeftClick())) {
				{
					//Bankroll check
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					if (bankroll < 25) {
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					} else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip25.Amt");
						if (chips < 1) {
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip25.Amt", chips);
						} else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip25.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip25.Stack", chips);
						}

						bankroll -= 25;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));

						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 25;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Subtract $25 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$25 Chip")) && (click.isRightClick())) {
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip25.Stack");
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip25.Amt") == 0) {
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					} else {
						//Decrease chips
						if (chips > 1) {
							chips--;
						} else if (chips == 1) {
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip25.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip25.Amt") - 1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip25.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 25;
						bankroll += 25;

					}

					FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Add $100 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$100 Chip")) && (click.isLeftClick())) {
				{
					//Bankroll check
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					if (bankroll < 100) {
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					} else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip100.Amt");
						if (chips < 1) {
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip100.Amt", chips);
						} else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip100.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip100.Stack", chips);
						}
						bankroll -= 100;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 100;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Subtract $100 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$100 Chip")) && (click.isRightClick())) {
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip100.Stack");
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip100.Amt") == 0) {
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					} else {
						//Decrease chips
						if (chips > 1) {
							chips--;
						} else if (chips == 1) {
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip100.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip100.Amt") - 1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip100.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 100;
						bankroll += 100;
					}
					FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Add $500 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$500 Chip")) && (click.isLeftClick())) {
				{
					//Bankroll check
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					if (bankroll < 500) {
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					} else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip500.Amt");
						if (chips < 1) {
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip500.Amt", chips);
						} else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip500.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip500.Stack", chips);
						}
						bankroll -= 500;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
						//Add bet amount
						int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
						betAmount += 500;
						FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					}
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Subtract $500 from bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$500 Chip")) && (click.isRightClick())) {
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip500.Stack");
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip500.Amt") == 0) {
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					} else {
						//Decrease chips
						if (chips > 1) {
							chips--;
						} else if (chips == 1) {
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip500.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip500.Amt") - 1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip500.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 500;
						bankroll += 500;

					}
					FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}
			//Add $1000 to bet
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$1000 Chip.")) && (click.isLeftClick())) {
				{
					//Bankroll check
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					if (bankroll < 1000) {
						player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
					} else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip1000.Amt");
						if (chips < 1) {
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip1000.Amt", chips);
						} else {
							chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip1000.Stack");
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip1000.Stack", chips);
						}
						bankroll -= 1000;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
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
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d$1000 Chip.")) && (click.isRightClick())) {
				{
					//Variable declaration
					int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip1000.Stack");
					String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
					double bankroll = Double.parseDouble(temp);
					int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");

					//Chip Amount
					if (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip1000.Amt") == 0) {
						player.sendMessage(Utils.chat("&c&l[!]&c You cannot make a a negative bet!"));
					} else {
						//Decrease chips
						if (chips > 1) {
							chips--;
						} else if (chips == 1) {
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip1000.Amt", FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip1000.Amt") - 1);
						}
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip1000.Stack", chips);

						//Decrease bet and increase bankroll
						betAmount -= 1000;
						bankroll += 1000;

					}

					FileConfig.getBlackjackFile().set("" + player.getUniqueId() + ".BetAmount", betAmount);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));
				}
				FileConfig.saveBlackjack();
				bg.betInventory(player);
			}

			//Deal Cards
			if (item.getItemMeta().getDisplayName().equals((Utils.chat("&aDeal")))) {
				int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
				if (betAmount < plugin.getConfig().getInt("BlackjackGame.MinBet")){
					player.sendMessage(Utils.chat("&c&l[!] &cYou do not meet the minimum bet of $" +plugin.getConfig().getInt("BlackjackGame.MinBet") +"."));
					bg.betInventory(player);
				}
				else {
					List<String> deck = Blackjack.deckMaker();
					Blackjack.shuffle(deck);
					count = 1;

					//Players first card
					String p1 = Blackjack.deal(deck);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", p1);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(p1));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(p1));

					//Dealers first card
					count++;
					String d1 = Blackjack.deal(deck);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", d1);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(d1));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(d1));

					//Player second card
					count++;
					String p2 = Blackjack.deal(deck);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", p2);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(p2));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(p2));

					//Save number of player and dealer cards
					numOfPlayerCards = 2;
					numOfDealerCards = 1;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".numPlayerCards", numOfPlayerCards);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".numDealerCards", numOfDealerCards);

					//Set Position of Player Cards
					playerPosition = 31;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".PlayerPosition", playerPosition);
					earlyPlayerPosition = 30;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".EarlyPlayerPosition", earlyPlayerPosition);

					//Set Position of Dealer Cards
					dealerPosition = 4;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerPosition", dealerPosition);
					earlyDealerPosition = 4;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".EarlyDealerPosition", earlyDealerPosition);

					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Count", count);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Deck", deck);
					FileConfig.saveBlackjack();

					bg.gameInventory(player);
				}
			}
			//Hit
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Hit"))) {
				//Retrieve Deck and count
				List<String> deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
				count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");

				//Retrieve position
				playerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerPosition");
				earlyPlayerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".EarlyPlayerPosition");

				//Create new card
				count++;
				String card = Blackjack.deal(deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", card);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(card));
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(card));
				ItemStack cardItem = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount"));{
					ItemMeta meta = cardItem.getItemMeta();
					meta.setDisplayName(Utils.chat("&2" + Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data"))));
					cardItem.setItemMeta(meta);
				}

				//Add card value to total
				numPlayerTotal = Blackjack.cardTotal(new String[]{card}) + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerTotal");
				ItemStack playerTotal = new ItemStack(Material.MINECART, numPlayerTotal);{
					ItemMeta meta = playerTotal.getItemMeta();
					meta.setDisplayName(Utils.chat("&DTotal"));
					playerTotal.setItemMeta(meta);
				}

				//Check if odd or even player cards
				numOfPlayerCards = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".numPlayerCards");

				//Even
				if (numOfPlayerCards % 2 == 0) {
					try{
						Thread.sleep(150);
						playerPosition++;
						open.setItem(playerPosition, cardItem);
						open.setItem(35, playerTotal);
						player.updateInventory();
					}catch (InterruptedException ex){
						//Do nothing
					}

				}
				//Odd
				else {
					//Move current cards over
					try {
						for (int i = earlyPlayerPosition; i <= playerPosition; i++) {
							//player.sendMessage("At index " + i);
							open.setItem(i - 1, open.getItem(i));
							open.setItem(i, new ItemStack(Material.AIR, 1));
							player.updateInventory();
							Thread.sleep(150);
						}
					} catch (InterruptedException ex) {
						//Do nothing
					}

					//Set new card
					open.setItem(FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerPosition"), cardItem);
					earlyPlayerPosition--;

					//Update Inventory and Total
					open.setItem(35, playerTotal);
					player.updateInventory();
				}

				//Save Changes


				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".PlayerTotal", numPlayerTotal);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Count", count);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".PlayerPosition", playerPosition);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Deck", deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".EarlyPlayerPosition", earlyPlayerPosition);
				FileConfig.saveBlackjack();

				//If Bust
				if (numPlayerTotal > 21) {
					//Add a lose message
					ItemStack loseMessage = new ItemStack(Material.PAPER, 1);
					{
						ItemMeta meta = loseMessage.getItemMeta();
						meta.setDisplayName(Utils.chat("&cYou busted! You lose $" + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount")));
						meta.setLore(Arrays.asList("", "", Utils.chat("&5Click on the book to restart!"), "", ""));
						loseMessage.setItemMeta(meta);
					}
					open.setItem(40, loseMessage);
					player.updateInventory();

					//Remove hit and stand buttons
					ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
					{
						ItemMeta meta = empty2.getItemMeta();
						empty2.setItemMeta(meta);
					}
					open.setItem(48, empty2);
					open.setItem(50, empty2);

					//Add a reset button
					ItemStack resetButton = new ItemStack(Material.BOOK, 1);
					{
						ItemMeta meta = resetButton.getItemMeta();
						meta.setDisplayName(Utils.chat("&aReset"));
						resetButton.setItemMeta(meta);
					}
					open.setItem(49, resetButton);
				}

				//If 21
				else if (numPlayerTotal == 21) {
					stand(player, open);
				}

				//Save Number of Player Cards
				numOfPlayerCards++;
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".numPlayerCards", numOfPlayerCards);
				FileConfig.saveBlackjack();

				player.updateInventory();
			}

			//Stand
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Stand"))) {
				stand(player, open);
			}
			//Reset game
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&aReset"))) {
				resetGame(player);
				bg.betInventory(player);
			}
			//Cashout
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d&lCash Out"))){
				//Retrieve Bankroll
				String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+ ".Bankroll");
				double bankroll = Double.parseDouble(temp);

				//Deposit bankroll
				EconomyResponse response = economy.depositPlayer((OfflinePlayer) player, bankroll);
				if (response.transactionSuccess()){
					player.closeInventory();
					player.sendMessage(Utils.chat("&6&l[!] &a$" +temp +" &6has been deposited into your account. Come play again!"));
				}
				else{
					player.sendMessage(Utils.chat("&c&l[!] &cFailed to deposit money into your account."));
				}
			}
		}
	}

	private void setup (Player p){
		//Real amount of chips
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip5.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip25.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip100.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip500.Amt", 0);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip1000.Amt", 0);

		//ItemStack stack value
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip5.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip25.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip100.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip500.Stack", 1);
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Chip1000.Stack", 1);

		//Empty cards
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".Cards", 0);
		FileConfig.saveBlackjack();
	}
	private void resetGame (Player player){
		FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".BetAmount", 0);
		setup(player);
		FileConfig.saveBlackjack();
	}
	private void stand (Player player, Inventory open){
		//Retrieve Deck, Count, Dealer Position, and numOfDealerCards
		List<String> deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
		count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");
		dealerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerPosition");
		numOfDealerCards = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".numDealerCards");

		//Setup dealer and player totals
		numDealerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal");
		numPlayerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerTotal");

		//Keep hitting until at least 17
		while (FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal") < 17) {
			//Create new card
			count++;
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

			//Even
			if (numOfDealerCards % 2 == 0) {
				try {
					Thread.sleep(150);
					dealerPosition++;
					open.setItem(dealerPosition, cardItem);
					open.setItem(8, dealerTotal);
					player.updateInventory();
				} catch (InterruptedException ex) {
					//Do nothing
				}
			}
			//Odd
			else {
				//Move current cards over
				try {
					for (int i = earlyDealerPosition; i <= dealerPosition; i++) {
						//player.sendMessage("At index " + i);
						open.setItem(i - 1, open.getItem(i));
						open.setItem(i, new ItemStack(Material.AIR, 1));
						player.updateInventory();
						Thread.sleep(150);
					}
				} catch (InterruptedException ex) {
					//Do nothing
				}

				//Set new card
				open.setItem(FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerPosition"), cardItem);
				earlyDealerPosition--;

				//Update Inventory and Total
				open.setItem(8, dealerTotal);
				player.updateInventory();
			}

			numOfDealerCards++;

			//Save changes
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".numDealerCards", numOfDealerCards);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerTotal", numDealerTotal);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Count", count);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Deck", deck);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerPosition", dealerPosition);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".EarlyDealerPosition", earlyDealerPosition);

			FileConfig.saveBlackjack();
			player.updateInventory();
		}

		//If Dealer Busts
		if (numDealerTotal > 21) {
			//Add a win message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&a&lDealer busted!"));
				meta.setLore(Arrays.asList("", "", Utils.chat("&5You win &6$" + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount")), Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Win money
			int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount");
			String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
			double bankroll = Double.parseDouble(temp);
			bankroll += winMoney * 2;
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
			{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1);
			{
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}

		//Win condition
		else if (numDealerTotal < numPlayerTotal) {
			//Add a win message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);
			{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&a&lYou win!"));
				meta.setLore(Arrays.asList("", "", Utils.chat("&5You receive &6$" + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount")), Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Win money
			int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount");
			String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
			double bankroll = Double.parseDouble(temp);
			bankroll += winMoney * 2;
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
			{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1);
			{
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}

		//Lose condition
		else if (numDealerTotal > numPlayerTotal) {
			//Add a lose message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);
			{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&cYou lose! $" + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount") + " has been deducted."));
				meta.setLore(Arrays.asList("", "", Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
			{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1);
			{
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}

		//Push Condition
		else {
			//Add a push condition message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);
			{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&9&lPush, nobody wins or loses"));
				meta.setLore(Arrays.asList("", "", Utils.chat("&5You get &6$" + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount") + "&5 back."), Utils.chat("&5Click on the book to restart!"), "", ""));
				winMessage.setItemMeta(meta);
			}
			open.setItem(40, winMessage);

			//Win money
			int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount");
			String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
			double bankroll = Double.parseDouble(temp);
			bankroll += winMoney;
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));

			//Remove hit and stand buttons
			ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);
			{
				ItemMeta meta = empty2.getItemMeta();
				empty2.setItemMeta(meta);
			}
			open.setItem(48, empty2);
			open.setItem(50, empty2);

			//Add a reset button
			ItemStack resetButton = new ItemStack(Material.BOOK, 1);
			{
				ItemMeta meta = resetButton.getItemMeta();
				meta.setDisplayName(Utils.chat("&aReset"));
				resetButton.setItemMeta(meta);
			}
			open.setItem(49, resetButton);
		}
	}



}
