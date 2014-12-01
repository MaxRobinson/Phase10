package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;

public class PhaseSwapAction extends PhaseMoveAction{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8208773778990164615L;
	private int first, second;
	/**
	 * Constructor for the PhaseDiscardAction class.
	 * 
	 * @param source  the player making the move.
	 * @param a the index of the first card to swap
	 * @param b the index of the second card to swap
	 */
	public PhaseSwapAction(GamePlayer player, int a, int b) {
		super(player);
		this.first = a;
		this.second = b;
		
	}
	
	/**
	 * @return
	 * 		whether this action is a sap action.		
	 */
	@Override
	public boolean isSwapAction(){
		return true;
	}
	
	/**
	 * @return returns the index of the first card to be swapped
	 */
	public int getFirst(){
		return this.first;
	}
	
	/**
	 * @return returns the index of the second card to be swapped
	 */
	public int getSecond(){
		return this.second;
	}

}



