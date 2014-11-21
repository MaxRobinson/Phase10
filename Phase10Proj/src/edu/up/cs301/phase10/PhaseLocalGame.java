package edu.up.cs301.phase10;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.util.Pair;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.card.*;
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
				break;
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

		if(move.isDiscardAction()){
			int playerId = 0;
			for(int i = 0; i < players.length; i++){
				if(((PhaseDiscardAction)move).getPlayer().equals(players[i])){
					playerId = i;
				}
			}
			if(!state.hasDrawn || state.getTurn()!=playerId || ((PhaseDiscardAction) move).getCard() == null)
			{
				return false;
			}
			Card tempCard = ((PhaseDiscardAction) move).getCard();
			if (!state.getHands()[playerId].removeCard(((PhaseDiscardAction) move).getCard()))
			{
				return false;
			}
			state.getDiscardPile().add(tempCard);
			state.nextTurn();
			state.hasDrawn = false;
			return true;
			
			
		}
		else if(move.isDrawCardAction()){
				int playerId = 0;
				for(int i = 0; i < players.length; i++){
					if(((PhaseDrawCardAction)move).getPlayer().equals(players[i])){
						playerId = i;
					}
				}
				if(state.hasDrawn || state.getTurn()!=playerId)
				{
					return false;
				}
				if(((PhaseDrawCardAction) move).getIsDeck())
				{
					state.getHands()[playerId].add(state.getDeck().removeTopCard());
				}
				else
				{
					state.getHands()[playerId].add(state.getDiscardPile().removeTopCard());
				}
				state.hasDrawn = true;
				return true;
		}
		else if(move.isLayOnPhaseAction()){
			//TODO
		}
		else if(move.isLayPhaseAction()){
			// Get player ID
			int playerId = 0;
			for(int i = 0; i < players.length; i++){
				if(((PhaseLayPhaseAction)move).getPlayer().equals(players[i])){
					playerId = i;
				}

			}

			// Get current phase and number of cards in current phase
			int currPhase = state.getCurrentPhase()[playerId];
			int numCardsForPhase = Phase.numberPhases[state.getCurrentPhase()[playerId]];

			// Get the phase the player is trying to lay and sort it
			Phase layedPhaseP = ((PhaseLayPhaseAction)move).getPhaseToLay();
			ArrayList<Card> layedPhase = new ArrayList<Card>();
			layedPhase = layedPhaseP.getPhase();
			Collections.sort(layedPhase, new Comparator<Card>(){
				public int compare(Card c1, Card c2){
					return c1.getRank().compareTo(c2.getRank());
				}
			});

			// Check to ensure the number of cards is correct for the phase
			if(numCardsForPhase != layedPhase.size()){
				return false;
			}

			// Get the objective of the phase
			List<String> phaseObjectives = Arrays.asList(Phase.phases[currPhase].split(","));

			// If there is only one objective for the phase
			if(phaseObjectives.size() == 2){
				// Get number of cards for current stage
				int numCards = Integer.parseInt(phaseObjectives.get(1));
				try {
					// Setup reflection
					Method method = Phase.class.getMethod(phaseObjectives.get(0), Integer.class, ArrayList.class);
					Object obj = method.invoke(this, numCards, layedPhase);
					// Set method
					if(obj instanceof Pair<?,?>){
						return ((Pair<Boolean,ArrayList<Card>>)obj).first;
					}
					// Color or run
					else{
						return ((Boolean)obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				int numCards = Integer.parseInt(phaseObjectives.get(1));
				ArrayList<Card> leftOver = new ArrayList<Card>();
				try {
					// Setup reflection
					Method method = Phase.class.getMethod(phaseObjectives.get(0), Integer.class, ArrayList.class);
					Object obj = method.invoke(this,  numCards, layedPhase);
					// Set method
					if(obj instanceof Pair<?,?>){
						// If the first set failed, the seonc dset will too
						if(!((Pair<Boolean,ArrayList<Card>>)obj).first)
							return false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				numCards = Integer.parseInt(phaseObjectives.get(3));
				try {
					// Setup reflection
					Method method = Phase.class.getMethod(phaseObjectives.get(0), Integer.class, ArrayList.class);
					Object obj = method.invoke(this,  numCards, layedPhase);
					// Set method
					if(obj instanceof Pair<?,?>){
						return ((Pair<Boolean,ArrayList<Card>>)obj).first;
					}
					// Color or run
					else{
						return ((Boolean)obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else if(move.isSkipAction()){
			// The current player is not skipped, they will now be skipped
			int playerId = 0;
			for(int i = 0; i < players.length; i++){
				if(((PhaseDiscardAction)move).getPlayer().equals(players[i])){
					playerId = i;
				}
			}
			if(!state.hasDrawn || state.getTurn()!=playerId || ((PhaseDiscardAction) move).getCard() == null)
			{
				return false;
			}

			Card tempCard = ((PhaseDiscardAction) move).getCard();
			if(tempCard.equals(new Card(Rank.TWO,CardColor.Orange)))
			{
				return makeMove(new PhaseDiscardAction(((PhaseSkipAction)move).getPlayer(),((PhaseDiscardAction)move).getCard()));
			}
			if (!state.getHands()[playerId].removeCard(((PhaseDiscardAction) move).getCard()))
			{
				return false;
			}
			if(!state.getSkipped()[((PhaseSkipAction)action).getWhoSkipped()]){
				state.setSkipped(((PhaseSkipAction)action).getWhoSkipped());
			}
			else
			{
				return false;
			}
			state.getDiscardPile().add(tempCard);
			state.nextTurn();
			state.hasDrawn = false;
			return true;
		}
		return false;
	}
}
