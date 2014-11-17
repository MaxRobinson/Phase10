package edu.up.cs301.phase10;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

/**
 * A PhaseSkipAction is an action that represents playing a 
 * skip card. 
 * 
 * @author Max
 * @version 11/17/2014
 */
public class PhaseSkipAction extends PhaseDiscardAction {

	// private instance variables
	private static final long serialVersionUID = 5861500451228463242L;
	private int whoSkipped;
	
	/**
	 * Constructor for the PhaseSkipAction class
	 * 
	 * @param source 	The player making the move
	 * @param c			The card being discarded
	 * @param whoSkipped  The id of the player being skipped
	 */
	public PhaseSkipAction(GamePlayer player, Card c, int whoSkipped) {
		super(player, c);
		this.whoSkipped = whoSkipped; 
	}
	
	/**
	 * @return 
	 * 		whether the move is discarding a skip card
	 */
	@Override
	public boolean isSkipAction(){
		return true;
	}
	
	/**
	 * 
	 * @return the id of the player being skipped
	 */
	public int getWhoSkipped(){
		return this.whoSkipped;
	}
}
