package edu.up.cs301.phase10;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

public class PhaseLocalGame extends LocalGame implements PhaseGame{

	protected PhaseState state;
	protected int numPlayers;
	
	public PhaseLocalGame(){
		super();
	}
	
	public void start(GamePlayer[] players){
		super.start(players);
		numPlayers = players.length;
		state = new PhaseState(numPlayers);
	}
	
	protected boolean canMove(){
		return true;
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
		if(state.dealer == state.turn){
			for(int i = 0; i < currentPhase.length; i ++){
				if(currentPhase == 10){
					if(hands[i].length == 0){
						return players.name;
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
