package me.jonesdev.blackjackplugin;

import java.util.Arrays;

public class Blackjack {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] deck = deckMaker();
		System.out.println(Arrays.toString(deck));
	}
	
	//Outputs a card and removes from deck
	public static String deal( String[] deck, int size ) {
		int x = (int) (Math.random()*(size-1));
		String out = deck[x];
		deck[x] = deck[size-1];
		deck[size-1] = "";
		return out;
	}
	
	public static String[] deckMaker() {
		String [] suits = {"diamonds", "clubs", "hearts", "spades"};
		String [] out = new String [52];
		//Fills Deck
		for (int k = 0; k < 4; k++) 
        	for (int j = 0; j < 13;j++) 
        		out[k*13+j] = ""+(j+1)+" "+suits[k];
        return(out);
    }
	
	public static void shuffle( String[] deck) {
		String [] temp = new String [52];
		for (int k = 0; k < 52; k++) {
			int x = (int) (Math.random()*(51-k));
			temp[k] = deck[x];
			deck[x] = deck[51-k];
		}
		for (int k = 0; k < 52; k++) {
			deck[k]=temp[k];
		}
	}
	
}


