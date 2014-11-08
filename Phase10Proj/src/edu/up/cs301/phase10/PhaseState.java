package edu.up.cs301.phase10;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the state of a Slapjack game.  Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */
public class PhaseState extends GameState
{
	private static final long serialVersionUID = -8269749892027578792L;

    ///////////////////////////////////////////////////
    // ************** instance variables ************
    ///////////////////////////////////////////////////

	// the three piles of cards:
    //  - 0: pile for player 0
    //  - 1: pile for player 1
    //  - 2: the "up" pile, where the top card
	// Note that when players receive the state, all but the top card in all piles
	// are passed as null.
    private Deck[] piles;
    
    // whose turn is it to turn a card?
    private int toPlay;

    /**
     * Constructor for objects of class PhaseState. Initializes for the beginning of the
     * game, with a random player as the first to turn card
     *  
     */
    public PhaseState() {
    	// randomly pick the player who starts
    	toPlay = (int)(2*Math.random());
    	
    	// initialize the decks as follows:
    	// - each player deck (#0 and #1) gets half the cards, randomly
    	//   selected
    	// - the middle deck (#2) is empty
    	piles = new Deck[3];
    	piles[0] = new Deck(); // create empty deck
    	piles[1] = new Deck(); // create empty deck
    	piles[2] = new Deck(); // create empty deck
    	piles[toPlay].add52(); // give all cards to player whose turn it is, in order
    	piles[toPlay].shuffle(); // shuffle the cards
    	// move cards to opponent, until to piles have ~same size
    	while (piles[toPlay].size() >=
    			piles[1-toPlay].size()+1) {
    		piles[toPlay].moveTopCardTo(piles[1-toPlay]);
    	}
    }
    
    /**
     * Copy constructor for objects of class PhaseState. Makes a copy of the given state
     *  
     * @param orig  the state to be copied
     */
    public PhaseState(PhaseState orig) {
    	// set index of player whose turn it is
    	toPlay = orig.toPlay;
    	// create new deck array, making copy of each deck
    	piles = new Deck[3];
    	piles[0] = new Deck(orig.piles[0]);
    	piles[1] = new Deck(orig.piles[1]);
    	piles[2] = new Deck(orig.piles[2]);
    }
    
    /**
     * Gives the given deck.
     * 
     * @return  the deck for the given player, or the middle deck if the
     *   index is 2
     */
    public Deck getDeck(int num) {
        if (num < 0 || num > 2) return null;
        return piles[num];
    }
    
    /**
     * Tells which player's turn it is.
     * 
     * @return the index (0 or 1) of the player whose turn it is.
     */
    public int toPlay() {
        return toPlay;
    }
    
    /**
     * change whose move it is
     * 
     * @param idx
     * 		the index of the player whose move it now is
     */
    public void setToPlay(int idx) {
    	toPlay = idx;
    }
 
    /**
     * Replaces all cards with null, except for the top card of deck 2
     */
    public void nullAllButTopOf2() {
    	// see if the middle deck is empty; remove top card from middle deck
    	boolean empty2 = piles[2].size() == 0;
    	Card c = piles[2].removeTopCard();
    	
    	// set all cards in deck to null
    	for (Deck d : piles) {
    		d.nullifyDeck();
    	}
    	
    	// if middle deck had not been empty, add back the top (non-null) card
    	if (!empty2) {
    		piles[2].add(c);
    	}
    }
}
