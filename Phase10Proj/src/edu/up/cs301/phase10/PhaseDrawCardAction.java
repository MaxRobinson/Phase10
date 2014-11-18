package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;

/**
 * A PhaseDrawCardAction is an action representing a player drawing a card,
 * either from the deck or the discard pile.
 * 
 * @author Max
 * @version 11/17/2014
 */
public class PhaseDrawCardAction extends PhaseMoveAction {
	
	// private instance variables
	private static final long serialVersionUID = -1300466185267837604L;
	private boolean isDeck;
	
	public PhaseDrawCardAction(GamePlayer player, boolean isDeck) {
		super(player);
		this.isDeck = isDeck;
	}
	
	@Override
	public boolean isDrawCardAction(){
		return true;
	}
	
	public boolean getIsDeck(){
		return this.isDeck;
	}

}
