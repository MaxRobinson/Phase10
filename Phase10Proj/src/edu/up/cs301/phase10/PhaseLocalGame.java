package edu.up.cs301.phase10;

import android.util.Log;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

public class PhaseLocalGame extends LocalGame implements PhaseGame{

	// the game's state
	PhaseState state;
	
	/**
	 * Constructor for the PhaseLocalGame
	 */
	public PhaseLocalGame(){
		Log.i("PhaseLocalGame","creating game");
		// create the state for the beginning of the game 
		// giving the list of players to the state
		state = new PhaseState(players);
	}
	
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean canMove(int playerIdx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String checkIfGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		// TODO Auto-generated method stub
		return false;
	}

}
