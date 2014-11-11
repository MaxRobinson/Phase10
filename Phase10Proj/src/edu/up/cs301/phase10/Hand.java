package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;

public class Hand {
	
	//private instance variables. 
	ArrayList<Card> hand;
	
	/**
	 * Constructor
	 */
	public Hand(){
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Constructor
	 * @param hand
	 */
	public Hand(ArrayList<Card> hand){
		this.hand = hand;  
	}
	
	/**
	 * Getter for the hand
	 * @return ArrayList of cards that are in the hand.
	 */
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	/**
	 * Returns a card in the hand based on the index of the card. 
	 * If the index is larger than the size of the hand, retrun null. 
	 * 
	 * @param index
	 * @return Card at a given index, or null 
	 */
	public Card getCard(int index){
		if(index < hand.size()-1){
			return hand.get(index);
		}
		else{
			return null;
		}
		
	}
	
	/**
	 * Given a card, find all occurrences of that card in the users hand, 
	 * and return a list of indices of that card.
	 * @param card
	 * @return ArrayList of indices of that card. 
	 */
	public ArrayList<Integer> getCardIndices(Card card){
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		// Find all cards equal to the card passed in, in the users hand
		for(int i = 0; i < hand.size(); i++ ){
			if(hand.get(i).equals(card)){
				indices.add(i);
			}
		}
		
		return indices; 
	}
	
	/**
	 * Add Card to hand
	 * @param newCard
	 */
	public void addCard(Card newCard){
		hand.add(newCard);
	}
	
	/**
	 * Removes a card from a users hand given the index of the
	 * card in the users hand. 
	 * @param index
	 * @return true if the card was successfully removed
	 */
	public boolean removeCard(int index){
		if(hand.size()>0){
			hand.remove(index);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Removes a card from a users hand given a card object. 
	 * @param card
	 * @return true if the card was successfully removed
	 */
	public boolean removeCard(Card card){
		if(hand.size()>0){
			hand.remove(card);
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Given an list of Cards, set the list of cards equal to a 
	 * players hand. 
	 * @param cards
	 */
	public void setHand(ArrayList<Card> cards){
		this.hand = cards;
	}
	
	/**
	 * Returns the size of a users hand.
	 * @return the number of cards in the hand
	 */
	public int size(){
		return hand.size();
	}
	
	
}
