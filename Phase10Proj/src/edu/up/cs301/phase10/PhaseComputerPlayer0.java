package edu.up.cs301.phase10;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.CardColor;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * This is a really dumb computer player that always just makes a random move
 * it's so stupid that it sometimes tries to make illegal moves.
 * 
 * @author Dan Nelson
 * @auther Justice RW Nichols
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
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (!(info instanceof PhaseState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
    	PhaseState state = (PhaseState)info;
    	boolean drawPile = Math.random()<.5f;
    	game.sendAction(new PhaseDrawCardAction(this, drawPile));
		int playerId = 0;
//		for(int i = 0; i < state.getPlayers().length; i++){
//			if(state.getPlayers()[i].equals(this)){
//				playerId = i;
//			}
//
//		}
		playerId = this.playerNum;
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int toDiscard = (int) (Math.random() * state.getHands()[playerId].size());
		Card tempCard = state.getHands()[playerId].getCards().get(toDiscard);
		
		if(tempCard.equals(new Card(Rank.TWO,CardColor.Orange)))
		{
			int toSkip = (int)Math.random()*state.getCurrentPhase().length;
	    	game.sendAction(new PhaseSkipAction(this, state.getHands()[playerId].getCards().get(toDiscard),toSkip));

		}
		else
		{
			game.sendAction(new PhaseDiscardAction(this, state.getHands()[playerId].getCards().get(toDiscard)));
		}
	}

}
