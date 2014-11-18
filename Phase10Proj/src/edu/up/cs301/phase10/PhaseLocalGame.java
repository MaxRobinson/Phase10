package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * The PhaseLocalGame class for a simple Phase10 Game.  Defines and enforces
 * the game rules; handles interactions with players.
 * 
 * @author Dan Nelson 
 * @version 11/17/14
 */
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
	 * @param players 
	 * 			The players that are currently in the game 
	 */
	@Override
	public void start(GamePlayer[] players){
		super.start(players);
		state = new PhaseState(players);
		this.players = new GamePlayer[players.length];
		// Copy the input into the local game list of players.
		for(int i = 0; i< players.length; i++){
			this.players[i] = players[i];
		}
	}
	
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		PhaseState sendState = new PhaseState(state);
		for(int i = 0; i < players.length; i++){
			if(players[i].equals(p)){
				state.nullAllButHandOf(i);
			}
		}

		p.sendInfo(sendState);
	}

	/**
	 * Tell whether the given player is allowed to make a move at the
	 * present point in the game. 
	 * 
	 * @param playerIdx
	 * 		the player's player-number (ID)
	 * @return
	 * 		true iff the player is allowed to move
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		return playerIdx == state.getTurn();
	}

	@Override
	protected String checkIfGameOver() {
		if(state.getDealer() == state.getTurn()){
			for(int i = 0; i < state.getCurrentPhase().length; i ++){
				if(state.getCurrentPhase()[i] == 10){
					if(state.getHands()[i].size() == 0){
						return this.playerNames[i];
					}
				}
			}
		}
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		PhaseMoveAction move = (PhaseMoveAction)action;
		
		
		
		return false;
	}
}
