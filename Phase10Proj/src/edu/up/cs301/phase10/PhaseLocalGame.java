package edu.up.cs301.phase10;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.util.Pair;
import edu.up.cs301.game.Game;
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
				sendState.nullAllButHandOf(i);
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
		ArrayList<String> winerNames = new ArrayList<String>();
		int winerScore = 0;
		boolean first = true;
		// Check to see if anyone was out of cards, and if they had no cards, where they on phase 10
		for(int i = 0; i < players.length; i++){
			// The player has laid their phase and it was there last phase
			if(state.getLaidPhases()[i] != null && state.getCurrentPhase()[i] == 10){
				// If this is the first player to be found that has a laid phase 10
				if(first){
					// Add names to winers
					winerNames.add(this.playerNames[i]);
					winerScore = state.getScore()[i];
					first = false;
				}
				// Not the first player to be added, but is there score less other player
				else if(state.getScore()[i] < winerScore){
					winerNames.clear();
					winerNames.add(this.playerNames[i]);
					winerScore = state.getScore()[i]; 
				}
				// Two players had the same score
				else if(state.getScore()[i] == winerScore){
					winerNames.add(this.playerNames[i]);
				}
			}
		}

		// Game not over
		if(winerNames.size() == 0){
			return null;
		}
		// Game has a winer
		else if(winerNames.size() == 1){
			return winerNames.get(0);
		}
		// Game was a tie
		else{
			String ret = "";
			for(String s : winerNames){
				ret += (s + " ");
			}
			ret += "Tied!";
			return ret;
		}
	}

	@SuppressWarnings({ "unchecked" })
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
			if(!state.getHasDrawn() || state.getTurn()!=playerId || ((PhaseDiscardAction) move).getCard() == null)
			{
				return false;
			}
			Card tempCard = ((PhaseDiscardAction) move).getCard();
			if (!state.getHands()[playerId].removeCard(((PhaseDiscardAction) move).getCard()))
			{
				return false;
			}
			state.getDiscardPile().add(tempCard);
			state.setHasDrawn(false);
			state.nextTurn();	
			playerGoneOut(playerId);
			return true;
		}

		else if(move.isDrawCardAction()){
			int playerId = 0;
			for(int i = 0; i < players.length; i++){
				if(((PhaseDrawCardAction)move).getPlayer().equals(players[i])){
					playerId = i;
				}
			}
			if(state.getHasDrawn() || state.getTurn()!=playerId)
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
			state.setHasDrawn(true);
			return true;
		}

		else if(move.isLayOnPhaseAction()){
			// Get player ID
			int playerId = 0;
			for(int i = 0; i < players.length; i++){
				if(((PhaseLayOnPhaseAction)move).getPlayer().equals(players[i])){
					playerId = i;
				}

			}

			// Check to make sure it is this players turn
			if(!state.getHasDrawn() || state.getTurn()!=playerId)
			{
				return false;
			}

			int layOnId = ((PhaseLayOnPhaseAction)move).getIdToLayOn();
			int part = ((PhaseLayOnPhaseAction)move).getWhichPart();
			int numWilds = ((PhaseLayOnPhaseAction)move).getNumWilds();
			int currPhase = state.getCurrentPhase()[playerId];
			List<String> phaseObjectives = Arrays.asList(Phase.phases[currPhase-1].split(","));

			// Check to ensure it is this player's turn and has drawn
			if(!state.getHasDrawn() || state.getTurn()!=playerId || state.getLaidPhases()[layOnId] == null){
				return false;
			}

			// Trying to lay on second part of phase when phase only contains one part
			if(phaseObjectives.size() == 2 && part == 1){
				return false;
			}

			// Get all data from action
			//int numCardsToLay = ((PhaseLayOnPhaseAction)move).getToLay().size();
			int topBottom = ((PhaseLayOnPhaseAction)move).getTopOrBottom();
			int methodToCall = 0;
			Card cardToLay = ((PhaseLayOnPhaseAction)move).getToLay();

			// Call second method in phase
			if(layOnId == 1){
				methodToCall = 2;
			}

			Phase phase = new Phase();
			String methodName = phaseObjectives.get(methodToCall);
			if(methodName.equals("set")){
				methodName = "addOnSet";
			}
			ArrayList<Card> layedPhase = new ArrayList<Card>();
			layedPhase.addAll(state.getLaidPhases()[layOnId].getPhasePart()[part].getCards());
			int positionForCard = topBottom == 1 ? (layedPhase.size() - 1) : 0;
			layedPhase.add(positionForCard, cardToLay);

			// Get number of cards for current stage
			int numCards = Integer.parseInt(phaseObjectives.get(1));

			try {
				// Setup reflection
				Method method = Phase.class.getMethod(methodName, Integer.class, ArrayList.class);
				Object obj = method.invoke(phase, numCards, layedPhase);
				// Set method
				if(((Boolean)obj)){
					// Player was successfully able to lay down a card on another players phase
					// Check to see if it was their last card and update state
					updateHandAndPhaseLayOn(cardToLay, layedPhase, playerId, part, layOnId,numWilds);
				}
				return ((Boolean)obj);
			} 
			catch (Exception e) {}
		}

		else if(move.isLayPhaseAction()){

			// Get player ID
			int playerId = 0;
			for(int i = 0; i < players.length; i++){
				if(((PhaseLayPhaseAction)move).getPlayer().equals(players[i])){
					playerId = i;
				}

			}

			// Check to ensure it is this player's turn
			if(!state.getHasDrawn() || state.getTurn()!=playerId){
				return false;
			}

			// Get current phase and number of cards in current phase
			int currPhase = state.getCurrentPhase()[playerId];
			int numCardsForPhase = Phase.numberPhases[currPhase-1];
			int numWilds = ((PhaseLayPhaseAction)move).getNumWilds();
			
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
			List<String> phaseObjectives = Arrays.asList(Phase.phases[currPhase-1].split(","));
			Phase phase = new Phase();
			// If there is only one objective for the phase
			if(phaseObjectives.size() == 2){
				// Get number of cards for current stage
				int numCards = Integer.parseInt(phaseObjectives.get(1));
				try {
					// Setup reflection
					Method method = Phase.class.getMethod(phaseObjectives.get(0), Integer.class, ArrayList.class);
					Object obj = method.invoke(phase, numCards, layedPhase);
					// Set method
					if(obj instanceof Pair<?,?>){
						if(((Pair<Boolean,ArrayList<Card>>)obj).first){
							updateHandAndPhase(layedPhase, null, playerId,numWilds);
						}
						return ((Pair<Boolean,ArrayList<Card>>)obj).first;
					}
					// Color or run
					else{
						if(((Boolean)obj)){
							updateHandAndPhase(layedPhase, null, playerId,numWilds);
						}
						return ((Boolean)obj);
					}
				} 
				catch (Exception e) {}
			}
			else{
				int numCards = Integer.parseInt(phaseObjectives.get(1));
				ArrayList<Card> leftOver = new ArrayList<Card>();
				try {
					// Setup reflection
					Method method = Phase.class.getMethod(phaseObjectives.get(0), Integer.class, ArrayList.class);
					Object obj = method.invoke(phase, numCards, layedPhase);
					// Set method
					if(obj instanceof Pair<?,?>){
						// If the first set failed, the second set will too
						if(!((Pair<Boolean,ArrayList<Card>>)obj).first){
							return false;
						}
						// Cards to still be played
						else{
							for(Card c : ((Pair<Boolean,ArrayList<Card>>)obj).second){
								leftOver.add(c);
							}
						}
					}
				} 
				catch (Exception e) {}
				numCards = Integer.parseInt(phaseObjectives.get(3));
				try {
					// Setup reflection
					Method method = Phase.class.getMethod(phaseObjectives.get(2), Integer.class, ArrayList.class);
					Object obj = method.invoke(phase, numCards, leftOver);
					// Set method
					if(obj instanceof Pair<?,?>){
						if(((Pair<Boolean,ArrayList<Card>>)obj).first){
							updateHandAndPhase(layedPhase, leftOver, playerId,numWilds);
							return true;
						}
						else{
							return false;
						}
					}
					// Color or run
					else{
						if(((Boolean)obj)){
							updateHandAndPhase(layedPhase, leftOver, playerId,numWilds);
							return true;
						}
						else{
							return false;
						}
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
			if(!state.getHasDrawn() || state.getTurn()!=playerId || ((PhaseDiscardAction) move).getCard() == null)
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
			state.setHasDrawn(false);
			return true;
		}
		else if (move.isSwapAction())
		{
			int i = this.getPlayerIdx(((PhaseSwapAction)move).getPlayer());
			int size = state.getHands()[i].size();
			int first = ((PhaseSwapAction)move).getFirst();
			int second = ((PhaseSwapAction)move).getSecond();
			if(first >= size || second >= size)
			{
				return false;
			}
			state.getHands()[i].swap(first, second);
			return true;
		}
		return false;
	}

	private void updateHandAndPhase(ArrayList<Card> cards, ArrayList<Card> second, int playerId, int numWilds){
		// Remove the cards the user layed for the 
		Hand stateCards = state.getHands()[playerId];
		stateCards.removeCards(cards);
		int currNumWildsRemoved = 0;
		Card wildCard = new Card(Rank.ONE,CardColor.Orange);
		// Iterate over the cards in the players hand
		Iterator<Card> it = stateCards.cards.iterator();
		while(it.hasNext()){
			Card tempCard = it.next();
			// If the two ranks and color are the same, remove card
			// If the selected card is wild
			if (tempCard.equals(wildCard) && currNumWildsRemoved != numWilds){
				it.remove();
				currNumWildsRemoved++;
			}	
		}
		state.setHands(stateCards, playerId);

		// Multipart phase, need to find out what the first part was
		ArrayList<Card> first = new ArrayList<Card>();
		if(second != null){
			for(int i = 0; i < cards.size(); i++){
				if(!second.contains(cards.get(i))){
					first.add(cards.get(i));
				}
			}
		}

		// Update phase in the state
		Phase currPhase = null;
		if(second == null){
			currPhase = new Phase(first,null);
		}
		else{
			currPhase = new Phase(first,second);
		}
		state.setCurrentPhase(currPhase, playerId);
	}

	private void updateHandAndPhaseLayOn(Card card, ArrayList<Card> cards, int playerId, int part, int layOnId, int numWilds){
		// Remove the cards the user layed for the 
		Hand stateCards = state.getHands()[playerId];
		ArrayList<Card> cardToRemove = new ArrayList<Card>();
		cardToRemove.add(card);
		stateCards.removeCards(cardToRemove);
		int currNumWildsRemoved = 0;
		Card wildCard = new Card(Rank.ONE,CardColor.Orange);
		// Iterate over the cards in the players hand
		Iterator<Card> it = stateCards.cards.iterator();
		while(it.hasNext()){
			Card tempCard = it.next();
			// If the two ranks and color are the same, remove card
			// If the selected card is wild
			if (tempCard.equals(wildCard) && currNumWildsRemoved != numWilds){
				it.remove();
				currNumWildsRemoved++;
			}	
		}
		state.setHands(stateCards, playerId);

		// Update phase in the state
		Phase currPhase = state.getLaidPhases()[layOnId];
		if(part == 0){
			currPhase.setPart(0, cards);
		}
		else{
			currPhase.setPart(1, cards);
		}
		state.setCurrentPhase(currPhase, playerId);

		playerGoneOut(playerId);
	}

	private void playerGoneOut(int playerId){
		// Player has gone out
		if(state.getHands()[playerId].size() == 0){
			// Add points to users for cards left over in their hand
			// Loop through players
			for(int i = 0; i < players.length; i++){
				// If the player still has cards in their hand
				if(state.getHands()[playerId].size() != 0){
					// Loop through cards in each player's hand
					for(int j = 0; j < state.getHands()[playerId].size(); j++){
						int playerScore = state.getScore()[i];
						int cardScoreValue = state.getHands()[i].getCard(j).getScoreValue();
						state.setScore(i,(playerScore + cardScoreValue));
					}
				}
			}

			// Set all laid phases back to null and update player's phases
			for(int i = 0; i < players.length; i++){
				if(state.getLaidPhases()[i] != null){
					// Move user to the next phase since they completed the current one
					int newPhase = (state.getCurrentPhase()[playerId] + 1 != 11 ? (state.getCurrentPhase()[playerId] + 1) : 10);
					// Move player to next phase
					state.setCurrentPhase(newPhase, playerId);
					// Set players laid phase back to null
					state.setCurrentPhase(null, i);
				}
			}

			// Init new deck
			Deck deck = new Deck();
			deck.add108();
			deck.shuffle();

			// Init Discard pile with top card from Deck
			Deck discardPile = new Deck();
			discardPile.add(deck.removeTopCard());

			// Deal hands for players from deck
			state.initHands();
			state.dealHands(deck);

			// Set the deck and discard
			state.setDiscardPile(discardPile);
			state.setDeck(deck);

			// Init dealer to next player
			int dealer = (state.getDealer() < players.length ? (state.getDealer() + 1) : 0);

			// Set dealer
			state.setDealer(dealer);

			// Init first player
			state.initTurn(dealer);
		}	
	}
	public static String[] getPLayerNames(){
		return playerNames;
	}
}
