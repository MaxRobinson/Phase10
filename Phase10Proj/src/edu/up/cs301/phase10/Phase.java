package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

public class Phase {
	//instnace variables
	private Hand part1;
	private Hand part2;
	private Hand[] phasePart;
	private int phase;
	
	/**
	 * Take two lists of cards and set each part as a part of a phase
	 * @param firstPart
	 * @param secondPart
	 */
	public Phase(ArrayList<Card> firstPart, ArrayList<Card> secondPart){
		part1 = new Hand(firstPart);
		part2 = new Hand(secondPart);
		phasePart = new Hand[2];
		phasePart[0] = part1;
		phasePart[1] = part2;
	}
	
	/**
	 * Creates a phase with empty phase parts
	 */
	public Phase(){
		part1 = new Hand();
		part2 = new Hand();
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
	
	public Hand[] getPhasePart()
	{
		return phasePart;
	}
	
	
}
