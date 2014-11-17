package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A game-move object that a Phase player Sends to the game to make
 * a move.
 * 
 * @author Max
 * @version 11/17/2014
 */

public class PhaseMoveAction extends GameAction {

	// private instance variables
	private static final long serialVersionUID = -1535690582258328973L;

	public PhaseMoveAction(GamePlayer player) {
		// invoke superclass constructor to set source
		super(player);
	}
	
	/**
	 * @return 
	 * 		whether the move was a draw card
	 */
	public boolean isDrawCardAction(){
		return false;
	}
	
	/**
	 * @return
	 * 		whether the move was a discard
	 */
	public boolean isDiscardAction(){
		return false;
	}
	
	/**
	 * @return
	 * 		whether the move was playing a skip card
	 */
	public boolean isSkipAction(){
		return false;
	}
	
	/**
	 * @return
	 * 		whether the move was laying a phase
	 */
	public boolean isLayPhaseAction(){
		return false;
	}
	
	/**
	 * @return
	 * 		whether the move was playing on a phase, "Hitting"
	 */
	public boolean isLayOnPhaseAction(){
		return false;
	}
}
