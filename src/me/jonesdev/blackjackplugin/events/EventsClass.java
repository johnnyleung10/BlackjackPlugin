package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
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

	private int altPlayerTotal = 0;
	private int altDealerTotal = 0;

	int count = 0;

	//Player Positions
	private int playerPosition = 0;
	private int earlyPlayerPosition = 0;
	//DealerPositions
	private int dealerPosition = 0;
	private int earlyDealerPosition;

	//Create deck
	private List<String> deck;


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
				//Reset bet
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".BetAmount", 0);

				//Reset cards and count
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Cards", 0);
				count = 0;
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".Count", count);

				//Setup base values for betting chips
				setup(player);

				//Create deck and shuffle
				deck = Blackjack.deckMaker();
				Blackjack.shuffle(deck);

				//Save deck
				FileConfig.getBlackjackFile().set(player.getUniqueId() +".Deck", deck);
				FileConfig.saveBlackjack();

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
					}
					else {
						//Chip amount
						int chips = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Chip5.Amt");
						if (chips < 1) {
							chips++;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Chip5.Amt", chips);
						}
						else {
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
					}
					else {
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
			if (item.getItemMeta().getDisplayName().equals((Utils.chat("&a&lDeal")))) {
				//Create hands
				List<String> playerHand = new ArrayList<>();
				List<String> dealerHand = new ArrayList<>();
				//Retrieve deck and count
				deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
				count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");

				int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".BetAmount");
				if (betAmount < plugin.getConfig().getInt("BlackjackGame.MinBet")){
					player.sendMessage(Utils.chat("&c&l[!] &cYou do not meet the minimum bet of $" +plugin.getConfig().getInt("BlackjackGame.MinBet") +"."));
					bg.betInventory(player);
				}
				else {
					//Players first card
					count++;
					String p1 = Blackjack.deal(deck);
					playerHand.add(p1);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", p1);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(p1));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(p1));
						//Save count location
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.P1.Count", count);

					//Dealers first card
					count++;
					String d1 = Blackjack.deal(deck);
					dealerHand.add(d1);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", d1);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(d1));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(d1));
						//Save count location
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.D1.Count", count);

					//Player second card
					count++;
					String p2 = Blackjack.deal(deck);
					playerHand.add(p2);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", p2);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(p2));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(p2));
						//Save count location
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.P2.Count", count);

					//Check if player hand contains ace
					if (checkAce(playerHand)){
						int total = Blackjack.cardTotal(new String [] {p1, p2});
						total += 10;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".AltPlayerTotal", total);
					}
					else{
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".AltPlayerTotal", 0);
					}

					//Check if player hand adds up to 9, 10, or 11
					if (Arrays.asList(9, 10, 11).contains(Blackjack.cardTotal(new String[] {p1, p2}))){
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".DoubleDown", true);
					}


					//Check if dealer hand contains ace
					if (checkAce(dealerHand)){
						int total = numDealerTotal + 10;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".AltDealerTotal", total);
					}
					else {
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".AltDealerTotal", 0);
					}

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

					//Save changes
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Count", count);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Deck", deck);
						//Save Hands
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".PlayerHand", playerHand);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".DealerHand", dealerHand);
					FileConfig.saveBlackjack();

					bg.gameInventory(player);

				}
			}
			//Hit
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Hit"))) {
				//Disable Double Down
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".DoubleDown", false);

				//Retrieve Deck and count
				deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
				count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");

				//Retrieve playerHand
				List<String> playerHand = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() +".PlayerHand");

				//Retrieve position
				playerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerPosition");
				earlyPlayerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".EarlyPlayerPosition");

				//Create new card
				count++;
				String card = Blackjack.deal(deck);
				playerHand.add(card);
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

				//Check if player hand contains ace
				if (checkAce(playerHand)){
					int total = numPlayerTotal + 10;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".AltPlayerTotal", total);
					ItemStack altPlayerTotal = new ItemStack(Material.MINECART, total);{
						ItemMeta meta = altPlayerTotal.getItemMeta();
						meta.setDisplayName(Utils.chat("&DAlternate Total"));
						altPlayerTotal.setItemMeta(meta);
					}
					open.setItem(44, altPlayerTotal);
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
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 0);
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
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 0);
					open.setItem(FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerPosition"), cardItem);
					earlyPlayerPosition--;

					//Update Inventory and Total
					open.setItem(35, playerTotal);
					player.updateInventory();
				}

				//Save Changes
					//Save Hand
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".PlayerHand", playerHand);

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
                //Retrieve Deck, Count
                deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
                count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");

                //Get playerHand and dealerHand
                List<String> playerHand = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() +".PlayerHand");
                List<String> dealerHand = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() +".DealerHand");

                //Retrieve totals
				numDealerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal");

			    if (checkAce(playerHand)) {
                    String p1 = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() +".Cards.Card" +FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Cards.P1.Count") +".Data");
                    String p2 = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() +".Cards.Card" +FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Cards.P2.Count") +".Data");
                    int total = Blackjack.cardTotal(new String[]{p1, p2});
                    total += 10;

                    //Check if blackjack
                    if (total == 21) {
                        //Deal another card to dealer
                        count++;
                        String d2 = Blackjack.deal(deck);
                        dealerHand.add(d2);
                        FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", d2);
                        FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(d2));
                        FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(d2));

                        //Add to total
						numDealerTotal += Blackjack.cardTotal(new String[] {d2});

                        ItemStack dealerTotal = new ItemStack(Material.MINECART, numDealerTotal);{
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
						open.setItem(5, cardItem);

                    	//Check if dealer hand contains ace
                        if (checkAce(dealerHand)) {
                            int newDealerTotal = numDealerTotal + 10;
                            FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".AltDealerTotal", newDealerTotal);
                            ItemStack altDealerTotal = new ItemStack(Material.MINECART, newDealerTotal);{
                                ItemMeta meta = altDealerTotal.getItemMeta();
                                meta.setDisplayName(Utils.chat("&DAlternate Total"));
                                altDealerTotal.setItemMeta(meta);
                            }
                            open.setItem(17, altDealerTotal);

                            if (newDealerTotal == 21) {
                                //Add a push condition message
                                ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
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
                                player.updateInventory();
                            }
                        }
						else {
							//Win money
							int winMoney = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount");
							String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
							double bankroll = Double.parseDouble(temp);
							bankroll += winMoney * 1.5;
							FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));

							//Add a win message
							ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
								ItemMeta meta = winMessage.getItemMeta();
								meta.setDisplayName(Utils.chat("&a&lBlackjack!"));
								meta.setLore(Arrays.asList("", "", Utils.chat("&5You win!"), Utils.chat("&5You receive &6$" +winMoney/2), Utils.chat("&5Click on the book to restart!"), "", ""));
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
							player.updateInventory();
						}




                    }
                    else{
						stand(player, open);
					}
                }
			    else{
                    stand(player, open);
                }

			    //Save
				FileConfig.getBlackjackFile().set(player.getUniqueId() + ".Count", count);
			    FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Deck", deck);
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerTotal", numDealerTotal);
			    FileConfig.saveBlackjack();
			}
			//Double Down
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&6Double Down")) && FileConfig.getBlackjackFile().getBoolean(player.getUniqueId().toString() +".DoubleDown")){
				//Double bet
				//Bankroll check
				String temp = FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Bankroll");
				double bankroll = Double.parseDouble(temp);
				int betAmount = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".BetAmount");

				if (bankroll < betAmount) {
					player.sendMessage(Utils.chat("&c&l[!] &cYou do not have enough funds!"));
				}
				else {
					bankroll -= betAmount;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Bankroll", String.format("%.2f", bankroll));

					//Add bet amount
					betAmount *= 2;
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".BetAmount", betAmount);
					FileConfig.saveBlackjack();
				}


				//Hit
				{
					//Retrieve Deck and count
					deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
					count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");

					//Retrieve playerHand
					List<String> playerHand = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".PlayerHand");

					//Retrieve position
					playerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerPosition");
					earlyPlayerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".EarlyPlayerPosition");

					//Create new card
					count++;
					String card = Blackjack.deal(deck);
					playerHand.add(card);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", card);
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(card));
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(card));
					ItemStack cardItem = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount"));
					{
						ItemMeta meta = cardItem.getItemMeta();
						meta.setDisplayName(Utils.chat("&2" + Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data"))));
						cardItem.setItemMeta(meta);
					}

					//Add card value to total
					numPlayerTotal = Blackjack.cardTotal(new String[]{card}) + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerTotal");
					ItemStack playerTotal = new ItemStack(Material.MINECART, numPlayerTotal);
					{
						ItemMeta meta = playerTotal.getItemMeta();
						meta.setDisplayName(Utils.chat("&DTotal"));
						playerTotal.setItemMeta(meta);
					}

					//Check if player hand contains ace
					if (checkAce(playerHand)) {
						int total = numPlayerTotal + 10;
						FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".AltPlayerTotal", total);
						ItemStack altPlayerTotal = new ItemStack(Material.MINECART, total);
						{
							ItemMeta meta = altPlayerTotal.getItemMeta();
							meta.setDisplayName(Utils.chat("&DAlternate Total"));
							altPlayerTotal.setItemMeta(meta);
						}
						open.setItem(44, altPlayerTotal);
					}

					//Check if odd or even player cards
					numOfPlayerCards = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".numPlayerCards");

					//Even
					if (numOfPlayerCards % 2 == 0) {
						try {
							Thread.sleep(150);
							playerPosition++;
							open.setItem(playerPosition, cardItem);
							open.setItem(35, playerTotal);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 0);
							player.updateInventory();
						} catch (InterruptedException ex) {
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
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 0);
						open.setItem(FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerPosition"), cardItem);
						earlyPlayerPosition--;

						//Update Inventory and Total
						open.setItem(35, playerTotal);
						player.updateInventory();
					}

					//Save Changes
					//Save Hand
					FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".PlayerHand", playerHand);

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
				}

				stand(player, open);
				player.updateInventory();

			}

			//Reset game
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&aReset"))) {
				resetGame(player);
				bg.betInventory(player);
			}
			//Cashout
			if (item.getItemMeta().getDisplayName().equals(Utils.chat("&d&lCash Out"))){
				//Empty cards
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards", 0);

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

		//Disable Double Down
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() +".DoubleDown", false);

		//Reset Alt Dealer Total
		FileConfig.getBlackjackFile().set(p.getUniqueId().toString() + ".AltDealerTotal", 0);

		FileConfig.saveBlackjack();
	}
	private void resetGame (Player player){
		FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".BetAmount", 0);
		setup(player);
		FileConfig.saveBlackjack();
	}
	private void stand (Player player, Inventory open){
		//Retrieve Deck, Count, Dealer Position, and numOfDealerCards
		deck = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() + ".Deck");
		count = FileConfig.getBlackjackFile().getInt(player.getUniqueId() + ".Count");
		dealerPosition = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerPosition");
		numOfDealerCards = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".numDealerCards");

		//Get Dealer Hand
		List<String> dealerHand = FileConfig.getBlackjackFile().getStringList(player.getUniqueId().toString() +".DealerHand");

		//Setup dealer and player totals
		numDealerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".DealerTotal");
		numPlayerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".PlayerTotal");

		//Check to use alt or main total
		altPlayerTotal = FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".AltPlayerTotal");
		if (altPlayerTotal<=21 && altPlayerTotal>numPlayerTotal){
			numPlayerTotal = altPlayerTotal;
		}

		//Keep hitting until at least 17
		while (numDealerTotal < 17) {
			//Create new card
			count++;
			String card = Blackjack.deal(deck);
			dealerHand.add(card);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Data", card);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Amount", Blackjack.cardNum(card));
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Cards.Card" + count + ".Material", Blackjack.material(card));

			//Add to total
			numDealerTotal += Blackjack.cardTotal(new String[]{card});
			ItemStack dealerTotal = new ItemStack(Material.MINECART, numDealerTotal);{
				ItemMeta meta = dealerTotal.getItemMeta();
				meta.setDisplayName(Utils.chat("&DTotal"));
				dealerTotal.setItemMeta(meta);
			}
			open.setItem(8, dealerTotal);

			//Check if dealer hand contains ace
			if (checkAce(dealerHand)){
				int total = numDealerTotal + 10;
				altDealerTotal = total;
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".AltDealerTotal", total);
				ItemStack altDealerTotal = new ItemStack(Material.MINECART, total);{
					ItemMeta meta = altDealerTotal.getItemMeta();
					meta.setDisplayName(Utils.chat("&DAlternate Total"));
					altDealerTotal.setItemMeta(meta);
				}
				open.setItem(17, altDealerTotal);
			}

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
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".AltDealerTotal", altDealerTotal);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".numDealerCards", numOfDealerCards);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerTotal", numDealerTotal);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Count", count);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".Deck", deck);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".DealerPosition", dealerPosition);
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() + ".EarlyDealerPosition", earlyDealerPosition);

				//Save Hand
			FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".DealerHand", dealerHand);

			FileConfig.saveBlackjack();
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 1, 0);
			player.updateInventory();

			//Check if blackjack
			if (checkAce(dealerHand)&&altDealerTotal==21){
				break;
			}
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

		//Dealer blackjack condition
		else if (dealerHand.size() == 2 && altDealerTotal==21){
			//Add a lose message
			ItemStack winMessage = new ItemStack(Material.PAPER, 1);{
				ItemMeta meta = winMessage.getItemMeta();
				meta.setDisplayName(Utils.chat("&c&lBlackjack!"));
				meta.setLore(Arrays.asList("", "", Utils.chat("&5You lose! $" + FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() + ".BetAmount") + " has been deducted."), Utils.chat("&5Click on the book to restart!"), "", ""));
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
			ItemStack resetButton = new ItemStack(Material.BOOK, 1);{
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
			ItemStack resetButton = new ItemStack(Material.BOOK, 1);{
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
	private boolean checkAce (List<String> hand){
		for(String s : hand){
			//Check if card is an Ace
			if (Blackjack.cardNum(s)==1){
				return true;
			}
		}
		return false;
	}


}
