package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

public class PhaseLocalGame extends LocalGame implements PhaseGame{
	/**
	 * Current state of the game
	 */
	private PhaseState state;
	
	/**
	 * List of players in the game
	 */
	private GamePlayer[] players;
	
	/**
	 * Constructor
	 */
	public PhaseLocalGame(){
		super();
	}
	
	/**
	 * Overrides the startup method to allow the players to be passed to the PhaseState constructor
	 * 
	 * @param players - The players that are currently in the game 
	 */
	@Override
	public void start(GamePlayer[] players){
		super.start(players);
		state = new PhaseState(players);
		this.players = players;
	}
	
	protected boolean canMove(){
		
		return true;
	}
	
	
	
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Overrides the can move method and returns a booleans to inidicate if the player can move
	 * 
	 * @param playerIdx - The player that is trying to move 
	 * @return - boolean true if player can move or false if the player cannot move
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String checkIfGameOver() {
		if(state.getDealer() == state.getTurn()){
			for(int i = 0; i < state.getCurrentPhase().length; i ++){
				if(state.getCurrentPhase()[i] == 10){
					if(state.getHands()[i].size() == 0){
						//TDOD How do we get string of name?
						return players[i].toString();
					}
				}
			}
		}
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		// TODO Auto-generated method stub
		return false;
	}
}
