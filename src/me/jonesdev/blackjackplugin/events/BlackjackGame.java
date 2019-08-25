package me.jonesdev.blackjackplugin.events;

import me.jonesdev.blackjackplugin.Blackjack;
import me.jonesdev.blackjackplugin.BlackjackPlugin;
import me.jonesdev.blackjackplugin.FileConfig;
import me.jonesdev.blackjackplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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
		for(int slot = 0; slot < 18; slot++){
			i.setItem(slot, empty1);
		}
		//Fill with blue glass
		ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
			ItemMeta meta = empty2.getItemMeta();
			empty2.setItemMeta(meta);
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
		i.setItem(0, dealerHead);

		//Creates player head
		ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());{
			SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
			meta.setOwner(player.getName());
			meta.setDisplayName(Utils.chat("&6Player "+player.getName()));
			playerHead.setItemMeta(meta);
		}
		i.setItem(18, playerHead);

		//Betting Chips
		ItemStack bet5 = new ItemStack(Material.INK_SACK, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip5.Stack"), (byte) 1);{
			ItemMeta meta = bet5.getItemMeta();
			meta.setDisplayName(Utils.chat("&d$5 Chip"));
			bet5.setItemMeta(meta);
		}

		ItemStack bet25 = new ItemStack(Material.INK_SACK, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip25.Stack"), (byte) 14);{
			ItemMeta meta = bet25.getItemMeta();
			meta.setDisplayName(Utils.chat("&d$25 Chip"));
			bet25.setItemMeta(meta);
		}

		ItemStack bet100 = new ItemStack(Material.INK_SACK, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip100.Stack"), (byte) 11);{
			ItemMeta meta = bet100.getItemMeta();
			meta.setDisplayName(Utils.chat("&d$100 Chip"));
			bet100.setItemMeta(meta);
		}

		ItemStack bet500 = new ItemStack(Material.INK_SACK, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip500.Stack"), (byte) 2);{
			ItemMeta meta = bet500.getItemMeta();
			meta.setDisplayName(Utils.chat("&d$500 Chip"));
			bet500.setItemMeta(meta);
		}

		ItemStack bet1000 = new ItemStack(Material.INK_SACK, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".Chip1000.Stack"), (byte) 4);{
			ItemMeta meta = bet1000.getItemMeta();
			meta.setDisplayName(Utils.chat("&d$1000 Chip."));
			bet1000.setItemMeta(meta);
		}

		//Paper that shows current bet amount
		ItemStack betAmount = new ItemStack(Material.PAPER, 1);{
			ItemMeta meta = betAmount.getItemMeta();
			meta.setDisplayName(Utils.chat("&c&lCurrent Bet: $" + FileConfig.getBlackjackFile().getString(player.getUniqueId() + ".BetAmount")));
			betAmount.setItemMeta(meta);
		}

		//Paper that shows current bankroll
		ItemStack bankroll = new ItemStack(Material.PAPER, 1);{
			ItemMeta meta = bankroll.getItemMeta();
			meta.setDisplayName(Utils.chat("&c&lCurrent Bankroll: $" + FileConfig.getBlackjackFile().getString(player.getUniqueId() + ".Bankroll")));
			bankroll.setItemMeta(meta);
		}

		ItemStack cashout = new ItemStack(Material.GOLD_INGOT, 1);{
			ItemMeta meta = bankroll.getItemMeta();
			meta.setDisplayName(Utils.chat("&d&lCash Out"));
			cashout.setItemMeta(meta);
		}

		//Button to start game and deal cards
		ItemStack dealButton = new ItemStack(Material.BOOK, 1); {
			ItemMeta meta = dealButton.getItemMeta();
			meta.setDisplayName(Utils.chat("&aDeal"));
			dealButton.setItemMeta(meta);
		}

		//Set Bet Increments
		i.setItem(20, bet5);
		i.setItem(21, bet25);
		i.setItem(22, bet100);
		i.setItem(23, bet500);
		i.setItem(24, bet1000);
		//Set Other Buttons and Controls
		i.setItem(26, betAmount);
		i.setItem(49, dealButton);
		i.setItem(53, bankroll);
		i.setItem(45, cashout);

		player.openInventory(i);
	}

	public void gameInventory(final Player player){
		final Inventory i = plugin.getServer().createInventory(null, 54, Utils.chat(plugin.getConfig().getString("BlackjackGUI.Title")));

		//Fill with green glass
		ItemStack empty1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);{
			ItemMeta meta = empty1.getItemMeta();
			empty1.setItemMeta(meta);
		}
		for(int slot = 0; slot < 18; slot++){
			i.setItem(slot, empty1);
		}
		//Fill with blue glass
		ItemStack empty2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 3);{
			ItemMeta meta = empty2.getItemMeta();
			empty2.setItemMeta(meta);
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
		i.setItem(0, dealerHead);
		//Creates player head
		ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());{
			SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
			meta.setOwner(player.getName());
			meta.setDisplayName(Utils.chat("&6Player "+player.getName()));
			playerHead.setItemMeta(meta);
		}
		i.setItem(18, playerHead);

		//Player first card
		final ItemStack p1 = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card1.Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card1.Amount"));{
			ItemMeta meta = p1.getItemMeta();
			meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card1.Data"))));
			p1.setItemMeta(meta);
		}
		//Dealer first card
		final ItemStack d1 = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card2.Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card2.Amount"));{
			ItemMeta meta = d1.getItemMeta();
			meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card2.Data"))));
			d1.setItemMeta(meta);
		}
		//Player first card
		final ItemStack p2 = new ItemStack(Material.getMaterial(FileConfig.getBlackjackFile().get(player.getUniqueId().toString()+".Cards.Card3.Material").toString()), FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString()+".Cards.Card3.Amount"));{
			ItemMeta meta = p2.getItemMeta();
			meta.setDisplayName(Utils.chat("&2"+Blackjack.properName(FileConfig.getBlackjackFile().getString(player.getUniqueId().toString()+".Cards.Card3.Data"))));
			p2.setItemMeta(meta);
		}


		//CardTotals
		FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".PlayerTotal", 1);
		FileConfig.getBlackjackFile().set(player.getUniqueId().toString() +".DealerTotal", 1);
		FileConfig.saveBlackjack();
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
		final ItemStack hit = new ItemStack(Material.GOLD_PLATE, 1);{
			ItemMeta meta = hit.getItemMeta();
			meta.setDisplayName(Utils.chat("&6Hit"));
			hit.setItemMeta(meta);
		}
		final ItemStack stand = new ItemStack(Material.BREWING_STAND_ITEM, 1);{
			ItemMeta meta = stand.getItemMeta();
			meta.setDisplayName(Utils.chat("&6Stand"));
			stand.setItemMeta(meta);
		}

		player.openInventory(i);

		//Player's first card
		new BukkitRunnable() {
			@Override
			public void run() {
				//Set cards
				i.setItem(31, p1);
				//Set totals
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerTotal", Blackjack.cardTotal(new String[]{FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() +".Cards.Card1.Data")}));
				FileConfig.saveBlackjack();
				ItemStack playerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".PlayerTotal"));{
					ItemMeta meta = playerTotal.getItemMeta();
					meta.setDisplayName(Utils.chat("&DTotal"));
					playerTotal.setItemMeta(meta);
				}
				i.setItem(35, playerTotal);
			}

		}.runTaskLater(plugin, 10);
		player.openInventory(i);

		//Deal dealer's first card
		new BukkitRunnable() {

			@Override
			public void run() {
				//Set cards
				i.setItem(4, d1);

				//Set totals
				int dealerTotalNum = Blackjack.cardTotal(new String[]{FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() +".Cards.Card2.Data")});
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".DealerTotal", dealerTotalNum);
				FileConfig.saveBlackjack();
				ItemStack dealerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".DealerTotal"));{
					ItemMeta meta = dealerTotal.getItemMeta();
					meta.setDisplayName(Utils.chat("&DTotal"));
					dealerTotal.setItemMeta(meta);
				}
				i.setItem(8, dealerTotal);
			}

		}.runTaskLater(plugin, 20);
		player.openInventory(i);

		//Deal player's second card
		new BukkitRunnable() {

			@Override
			public void run() {
				//Set cards
				i.setItem(30, i.getItem(31));
				i.setItem(31, p2);

				//Set card totals
				int playerTotalNum = Blackjack.cardTotal(new String[]{FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() +".Cards.Card1.Data"), FileConfig.getBlackjackFile().getString(player.getUniqueId().toString() +".Cards.Card3.Data")});
				FileConfig.getBlackjackFile().set(player.getUniqueId().toString()+".PlayerTotal", playerTotalNum);
				FileConfig.saveBlackjack();
				ItemStack playerTotal = new ItemStack(Material.MINECART, FileConfig.getBlackjackFile().getInt(player.getUniqueId().toString() +".PlayerTotal"));{
					ItemMeta meta = playerTotal.getItemMeta();
					meta.setDisplayName(Utils.chat("&DTotal"));
					playerTotal.setItemMeta(meta);
				}
				i.setItem(35, playerTotal);
				player.updateInventory();

				//Set hit and stand buttons
				i.setItem(48, hit);
				i.setItem(50, stand);

			}

		}.runTaskLater(plugin, 30);
		player.openInventory(i);
	}

}
