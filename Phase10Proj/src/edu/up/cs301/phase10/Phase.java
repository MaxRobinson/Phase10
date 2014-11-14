package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

public class Phase {
	//instnace variables
	private Deck part1;
	private Deck part2;
	private Deck[] phasePart;
	private int phase;
	
	/**
	 * Take two lists of cards and set each part as a part of a phase
	 * @param firstPart
	 * @param secondPart
	 */
	public Phase(ArrayList<Card> firstPart, ArrayList<Card> secondPart){
		part1 = new Deck(firstPart);
		part2 = new Deck(secondPart);
		phasePart = new Deck[2];
		phasePart[0] = part1;
		phasePart[1] = part2;
	}
	
	/**
	 * Creates a phase with empty phase parts
	 */
	public Phase(){
		part1 = new Deck();
		part2 = new Deck();
	}
	
	/**
	 * part is an int either 0 or 1 if not
	 * @param part
	 * @param cards
	 */
	public void setPart(int part, ArrayList<Card> cards){
		// if part passed in is not a valid Phase
		if(!(part < 2)){
			return;
		}
		synchronized(cards){
			for(Card c : cards){
				phasePart[part].add(c);
			}
		}
	}
	
	
}
