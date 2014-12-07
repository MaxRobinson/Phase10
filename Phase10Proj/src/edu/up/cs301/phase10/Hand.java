package edu.up.cs301.phase10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.CardColor;
import edu.up.cs301.card.Rank;
/**
 * The Hand class represents the cards in a player's hand. Alows
 * players to order hands and hands to maintain order.
 * 
 * @author Dan Nelson 
 * @version 11/17/14
 */
public class Hand extends Deck{



	/**
	 * 
	 */
	private static final long serialVersionUID = -6654982349516137617L;

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
		for(Card c: cards)
		{
			this.cards.add(c);
		}
	}
	
	public Hand(Hand hand){
		super(hand);
	}

	public void swap(int i , int j)
	{
		if( i < 0 || j < 0)
		{
			return;
		}
		Collections.swap(this.cards, i, j);
	}
	/**

	 * {Getter for the cards
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
	 * Removes a list of cards from the users cards
	 *  
	 * @param cardsToRemove
	 * @return void
	 */
	public void removeCards(ArrayList<Card> cardsToRemove){
		// Iterate over the cards we need to remove
		for(int i = 0; i < cardsToRemove.size(); i++){
			// Iterate over the cards in the players hand
			Iterator<Card> it = cards.iterator();
			while(it.hasNext()){
				Card tempCard = it.next();
				// If the two ranks and color are the same, remove card
				if(tempCard.getRank() == cardsToRemove.get(i).getRank()
				&& tempCard.getCardColor() == cardsToRemove.get(i).getCardColor()){
					it.remove();
					break;
				}	
			}
		}
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
			for(Card c: cards){
				if(c.getRank().equals(card.getRank()) && c.getCardColor().equals(card.getCardColor())){
					cards.remove(c);
					break;
				}
			}
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
	
	/**
	 * Returns true if the hand contains card
	 * @param card
	 * @return if card is in hand
	 */
	public boolean contains(Card card)
	{
		return cards.contains(card);
	}
	
	/**
	 * Returns true if the hand contains card with same rank
	 * @param card
	 * @return if rank is in hand
	 */
	public boolean containsRank(Card card)
	{
		Rank rank = card.getRank();
		return (cards.contains(new Card(rank,CardColor.Blue)) || 
				cards.contains(new Card(rank,CardColor.Green)) ||
				cards.contains(new Card(rank,CardColor.Red)) ||
				cards.contains(new Card(rank,CardColor.Yellow)));
	}


}
