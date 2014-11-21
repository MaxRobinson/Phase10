package edu.up.cs301.phase10;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Pair;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.CardColor;
import edu.up.cs301.card.Rank;
/**
 * The Phase class for a simple Phase10 Game. Defines and stored
 * the current phases.
 * 
 * @author Dan Nelson 
 * @version 11/17/14
 */
public class Phase {
	//instnace variables

	private Hand part1;
	private Hand part2;
	private Hand[] phasePart;
	private int numCards;
	private ArrayList<Card> phaseA;
	
	private int phase;
	public static String phases[] = {"set,3,set,3", "set,3,run,4", " set,4,run,4", " run,7", "run,8", "run,9",
							  "set,4,set,4", "color,7", "set,5,set,2", "set,5,set,3"};
	public static int numberPhases[] = {6,7,8,7,8,9,8,7,7,8};
	
	/**
	 * Take two lists of cards and set each part as a part of a phase
	 * @param firstPart
	 * @param secondPart
	 */
	public Phase(ArrayList<Card> firstPart, ArrayList<Card> secondPart){
		part1 = new Hand(firstPart);
		part2 = new Hand(secondPart);
		phasePart = new Hand[2];
		phaseA = new ArrayList<Card>();
		phasePart[0] = part1;
		phasePart[1] = part2;
		if(firstPart != null)
		{
			numCards = firstPart.size();
			phaseA.addAll(firstPart);

		}
		if(secondPart!=null)
		{
			numCards += secondPart.size();
			phaseA.addAll(secondPart);
		}
	}
	
	/**
	 * Creates a phase with empty phase parts
	 */
	public Phase(){
		part1 = new Hand();
		part2 = new Hand();
	}
	
	public Hand[] getPhasePart()
	{
		return phasePart;
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
	
	

	public ArrayList<Card> getPhase(){
		return phaseA;
	}
	
	public int getNumCards(){
		return numCards;
	}
	
	public Pair<Boolean,ArrayList<Card>> set(int numCards, ArrayList<Card> cards){
			ArrayList<Card> leftOver = new ArrayList<Card>();
			HashMap<Rank,Integer> sets = new HashMap<Rank,Integer>();
			
			// Add all cards to hashmap to count how many of each rank there is
			for(int i = 0; i < cards.size(); i++){
				int temp = sets.get(cards.get(i).getRank());
				temp++;
				sets.put(cards.get(i).getRank(),temp);
			}
			
			// Get all number of each rank out of hashmap
			for(Integer card : sets.values()){
				// If a rank has the proper number of cards for a set
				if(card == numCards){
					for(int i = 0; i < cards.size(); i++){
						if(!cards.get(i).equals(card)){
							leftOver.add(cards.get(i));
						}
					}
					return new Pair<Boolean,ArrayList<Card>>(true,leftOver);
				}
			}
			return new Pair<Boolean,ArrayList<Card>>(false,null);
	}
	
	public boolean run(int numCards, ArrayList<Card> cards){
		int currNum = cards.get(0).getIntRank();
		currNum++;
		for(int i = 1; i < cards.size(); i++){
			if(currNum == cards.get(i).getIntRank()){
				currNum++;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	public boolean color(int numCards, ArrayList<Card> cards){		
		CardColor color = cards.get(0).getCardColor();
		
		for(int i = 1; i < cards.size(); i++){
			if(!color.equals(cards.get(i).getCardColor())){
				return false;
			}
		}
		
		return true;
	}
	
}
