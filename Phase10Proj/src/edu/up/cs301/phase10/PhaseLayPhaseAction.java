package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;

/**
 * A PhaseLayPhaseAction is an action that represents a player 
 * laying down a phase from their hand. (AKA "Phasing").
 * 
 * @author Max
 * @version 11/17/2014
 */
public class PhaseLayPhaseAction extends PhaseMoveAction {
	
	// private instance variables
	private static final long serialVersionUID = 3494826443766524896L;
	private Phase toLay;
	private int numWildCards;
	
	public PhaseLayPhaseAction(GamePlayer player, Phase toLay) {
		super(player);
		this.toLay = toLay;
		this.numWildCards = 0;
	}
	
	public PhaseLayPhaseAction(GamePlayer player, Phase toLay, int numWildCards) {
		super(player);
		this.toLay = toLay;
		this.numWildCards = numWildCards;
	}
	
	@Override
	public boolean isLayPhaseAction(){
		return true;
	}
	
	public Phase getPhaseToLay(){
		return this.toLay;
	}
	
	public int getNumWilds(){
		return numWildCards;
	}

}
