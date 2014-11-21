package edu.up.cs301.phase10;

import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * This is a really dumb computer player that always just makes a random move
 * it's so stupid that it sometimes tries to make illegal moves.
 * 
 * @author Dan Nelson
 * @versio2 11/21/14
 */
public class PhaseComputerPlayer0 extends PhaseComputerPlayer {

	public PhaseComputerPlayer0(String name) {
		super(name);
	}
	
    /**
     * Called when the player receives a game-state (or other info) from the
     * game.
     * 
     * @param gameInfo
     * 		the message from the game
     */
    @Override
    protected void receiveInfo(GameInfo info) {
    	// if it was a "not your turn" message, just ignore it
    	if (info instanceof NotYourTurnInfo) return;
    	
    	PhaseState state = (PhaseState)info;
    	game.sendAction(new PhaseDrawCardAction(this, true));
		int playerId = 0;
		for(int i = 0; i < state.getPlayers().length; i++){
			if(state.getPlayers()[i].equals(this)){
				playerId = i;
			}

		}
    	game.sendAction(new PhaseDiscardAction(this, state.getHands()[playerId].getCards().get(0)));
	}

}
