package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

public class Hand extends Deck{
	
	
	
	/**
	 * Constructor
	 */
	public Hand(){
		super();
	}
	
	/**
	 * Constructor
	 * @param cards
	 */
	public Hand(ArrayList<Card> cards){
		this.cards = cards;  
	}
	
	/**
	 * Getter for the cards
	 * @return ArrayList of cards that are in the cards.
	 */
	public ArrayList<Card> getCards(){
		return this.cards;
	}
	
	/**
	 * Returns a card in the cards based on the index of the card. 
	 * If the index is larger than the size of the cards, retrun null. 
	 * 
	 * @param index
	 * @return Card at a given index, or null 
	 */
	public Card getCard(int index){
		if(index < cards.size()){
			return cards.get(index);
		}
		else{
			return null;
		}
		
	}
	
	/**
	 * Given a card, find all occurrences of that card in the users cards, 
	 * and return a list of indices of that card.
	 * @param card
	 * @return ArrayList of indices of that card. 
	 */
	public ArrayList<Integer> getCardIndices(Card card){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		// Find all cards equal to the card passed in, in the users cards
		for(int i = 0; i < cards.size(); i++ ){
			if(cards.get(i).equals(card)){
				indices.add(i);
			}
		}
		
		return indices; 
	}
	
	/**
	 * Add Card to cards
	 * @param newCard
	 */
	public void addCard(Card newCard){
		cards.add(newCard);
	}
	
	/**
	 * Removes a card from a users cards given the index of the
	 * card in the users cards. 
	 * @param index
	 * @return true if the card was successfully removed
	 */
	public boolean removeCard(int index){
		if(cards.size()>0){
			cards.remove(index);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Removes a card from a users cards given a card object. 
	 * @param card
	 * @return true if the card was successfully removed
	 */
	public boolean removeCard(Card card){
		if(cards.size()>0){
			cards.remove(card);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Given an list of Cards, set the list of cards equal to a 
	 * players cards. 
	 * @param cards
	 */
	public void setcards(ArrayList<Card> cards){
		this.cards = cards;
	}
	
	/**
	 * Returns the size of a users cards.
	 * @return the number of cards in the cards
	 */
	public int size(){
		return cards.size();
	}
	
	
}
