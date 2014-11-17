package edu.up.cs301.phase10;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

/**
 * A PhaseDiscardAction is an action that represents discarding a card
 * from a players hand.
 * 
 * @author Max
 * @version 11/17/2014
 */
public class PhaseDiscardAction extends PhaseMoveAction {
	
	// private instance variables. 
	private static final long serialVersionUID = -1087060943834140566L;
	private Card card;
	
	/**
	 * Constructor for the PhaseDiscardAction class.
	 * 
	 * @param source  the player making the move.
	 * @param c  the card the player wishes to discard.
	 */
	public PhaseDiscardAction(GamePlayer player, Card c) {
		super(player);
		this.card = c;
	}
	
	/**
	 * @return
	 * 		whether this action is a discard action.		
	 */
	@Override
	public boolean isDiscardAction(){
		return true;
	}
	
	/**
	 * @return c  The card that is being discarded.
	 */
	public Card getCard(){
		return this.card;
	}

}
