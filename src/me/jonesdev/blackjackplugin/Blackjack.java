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
		String [] suits = {"diamonds", "clubs", "hearts", "spades"};
		//String [] out = new String [52];
		List<String> output = new ArrayList();
		//Fills Deck
		for (int k = 0; k < 4; k++) 
        	for (int j = 0; j < 13;j++) 
        		output.add(k*13+j, (j+1)+" "+suits[k]);
        return(output);
    }
	
	public static void shuffle( List<String> deck) {
		Collections.shuffle(deck);
	}
	
}


