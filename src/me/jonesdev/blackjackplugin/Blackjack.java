package me.jonesdev.blackjackplugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blackjack {
	//Outputs a card from the top and removes from deck.
	//Use with shuffled deck.
	public static String deal( List<String> deck) {
		String outputCard = deck.get(deck.size()-1);
		deck.remove(deck.size()-1);
		return outputCard;
	}
	
	public static List<String> deckMaker() {
		String [] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};
		//String [] out = new String [52];
		List<String> output = new ArrayList();
		//Fills Deck
		for (int k = 0; k < 4; k++) 
        	for (int j = 0; j < 13;j++) 
        		output.add(k*13+j, (j+1)+" "+suits[k]);
        return(output);
    }
	
	public static void shuffle(List<String> deck) {
		Collections.shuffle(deck);
	}

	public static int cardNum(String card) {
		String[] cardSplit = card.split(" ", 2);
		return(Integer.parseInt(cardSplit[0]));
	}

	public static String cardSuit(String card) {
		String[] cardSplit = card.split(" ", 2);
		return(cardSplit[1]);
	}

	public static String properName(String card){
		int num = cardNum(card);
		if (num==1){
			return ("Ace of " +cardSuit(card));
		}
		if (num==11){
			return ("Jack of " +cardSuit(card));
		}
		if (num==12){
			return ("Queen of " +cardSuit(card));
		}
		if (num==13){
			return ("King of "	+cardSuit(card));
		}
		return (num +" of " +cardSuit(card));
	}

	public static String material(String card){
		String suit = cardSuit(card);
		if(suit.equals("Diamonds")){
			return("DETECTOR_RAIL");
		}
		else if(suit.equals("Clubs")){
			return("RAILS");
		}
		else if(suit.equals("Hearts")){
			return("POWERED_RAIL");
		}
		else {
			return("ACTIVATOR_RAIL");
		}
	}

	public static int cardTotal(String[] cards){
		int total = 0;
		for(String card:cards){
			if (cardNum(card)==11||cardNum(card)==12||cardNum(card)==13){
				total+=10;
			}
			else{
				total+=cardNum(card);
			}
		}
		return total;
	}
	
}


