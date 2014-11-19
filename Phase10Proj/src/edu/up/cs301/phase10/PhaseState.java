package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the State of the Phase 10 Game. Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 * 
 * @author Max
 * @author Dan Nelson
 * @version 11/17/14
 *
 */

public class PhaseState extends GameState {

	//CONSTANTS
	private final int INITHANDSIZE = 10;
	private final int MAXHANDSIZE = 11;

	//Private instance variables
	private static final long serialVersionUID = -189266841850951064L;

	/**
	 * Cards that are currently in the deck
	 */
	private Deck deck;

	/**
	 * Cards that are currently in the discard pile
	 */
	private Deck discardPile;

	/**
	 * Contains each players current hand
	 */
	private Hand[] hands;

	/**
	 * ID of player who is current round's dealer
	 */
	private int dealer;

	/**
	 * Current player whose turn it is
	 */
	private int turn;

	/**
	 * Contains the phase each player is currently on
	 * Indexed by player ID
	 */
	private int[] currentPhase;

	/**
	 * Which players are skipped.Indexed by ID and if ID
	 * Is true, player is skipped
	 */
	private boolean[] skipped;

	/**
	 * Each players current score indexed by ID
	 */
	private int[] score;

	/**
	 * List of players in the game
	 */
	private GamePlayer[] players;

	/**
	 * Number of Players in the game
	 */
	private int numPlayers;

	/**
	 * Indexed by ID, the laid phases that each player has 
	 */
	public Phase[] laidPhases;
	///////////////////////////////////////////////////////////

	/**
	 * Constructor
	 * NOTE This players, parameter should be a COPY of the actually list of players
	 */
	public PhaseState(GamePlayer[] players){
		this.players = new GamePlayer[players.length];
		//copy the input into the local game list of players. //This is to make sure no reference carry overs.
		synchronized(players){
			for(int i = 0; i< players.length; i++){
				this.players[i] = players[i];
			}
		}
		//setNumber of players
		numPlayers = this.players.length;

		// init Deck
		deck = new Deck();
		deck.add108();
		deck.shuffle();

		// init Discard pile with top card from Deck
		discardPile = new Deck();
		discardPile.add(deck.removeTopCard());

		hands = new Hand[numPlayers];
		initHands();
		// deal hands for players from deck
		dealHands(this.deck);

		// init the dealer to random player
		dealer = (int) Math.random()*(numPlayers);

		// init who goes first
		initTurn(dealer);

		// init starting Phases
		initCurrentPhase(this.numPlayers);

		// init Skipped array
		initSkipped();

		// init score
		score = new int[numPlayers];

		//init laid Phases
		laidPhases = new Phase[numPlayers];	
	}

	/**
	 * Constructor
	 * @param numPlayers
	 */
	/*
	public PhaseState(int numPlayers){



	}*/

	/**
	 * Copy constructor
	 * @param state
	 */
	public PhaseState(PhaseState state){
		PhaseState newState = new PhaseState(state.players);

		players = new GamePlayer[state.players.length];
		//copy the input into the local game list of players. //This is to make sure no reference carry overs.
		for(int i = 0; i< state.players.length; i++){
			players[i] = state.players[i];
		}
		//setNumber of players
		numPlayers = state.players.length;

		// init Deck
		deck = new Deck(state.deck);

		// init Discard pile with top card from Deck
		discardPile = new Deck(state.discardPile);

		hands = new Hand[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			hands[i] = state.hands[i];
		}

		// init the dealer to random player
		dealer = state.dealer;
		turn = state.turn;
		
		currentPhase = new int[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			currentPhase[i] = state.currentPhase[i];
		}
		
		skipped = new boolean[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			skipped[i] = state.skipped[i];
		}
		
		score = new int[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			score[i] = state.score[i];
		}
		
		laidPhases = new Phase[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			laidPhases[i] = state.laidPhases[i];
		}
	}


	///////////////// SETTER AND GETTERS //////////////////////////////////////

	public Deck getDeck(){
		return this.deck;
	}

	public void setDeck(Deck deck){
		this.deck = deck;
	}

	public Deck getDiscardPile(){
		return this.discardPile;
	}

	public void setDiscardPile(Deck discardPile){
		this.discardPile = discardPile;
	}

	public Hand[] getHands(){
		return this.hands;
	}

	public void setHands(Hand[] hands){
		this.hands = hands;
	}

	public int getDealer(){
		return this.dealer;
	}

	/**
	 * Set who the dealer is
	 * @param id
	 */
	public void setDealer(int id){
		this.dealer = id;
	}

	/**
	 * Tells which player's turn it is.
	 * 
	 * @return the index of the player whose turn it is.
	 */
	public int getTurn(){
		return this.turn;
	}

	/**
	 * change whose move it is
	 * 
	 * @param idx
	 * 		the index of the player whose move it now is
	 */
	public void setTurn(int idx){
		this.turn = idx;
	}

	public int[] getCurrentPhase(){
		return this.currentPhase;
	}

	public void setCurrentPhase(int[] currentPhase){
		this.currentPhase = currentPhase;
	}

	public boolean[] getSkipped(){
		return this.skipped;
	}

	public void setSkipped(boolean[] skipped){
		this.skipped = skipped;
	}

	public int[] getScore(){
		return this.score;
	}

	public void setScore(int[] scores){
		this.score = scores;
	}

	public Phase[] getLaidPhases(){
		return this.laidPhases;
	}

	public void setLaidPhases(Phase[] laidPhases){
		this.laidPhases = laidPhases;
	}	

	///////////////// HELPER METHODS ///////////////////////////////////////////

	/**
	 * Returns if a player is currently skipped or not.
	 * @param playerId
	 * @return true or false if a player is skipped.
	 */
	public boolean isSkipped(int playerId){
		return this.skipped[playerId];
	}

	/**
	 * This is to deal all of the hands for all of the players at the 
	 * start of a game or a round. 
	 * 
	 * @param deck
	 */
	private void dealHands(Deck deck){
		for(int i = 0; i < this.INITHANDSIZE; i++){
			for(int j = 0; j < this.numPlayers; j++){
				hands[j].add(deck.removeTopCard());
			}
		}
	}
	
	/**
	 * This initiallizes the array of hands for all players in the game.
	 */
	private void initHands(){
		for(int i = 0; i<this.numPlayers; i++){
			this.hands[i] = new Hand();
		}
	}
	
	/**
	 * Inits which player goes first
	 * @param dealer
	 */
	private void initTurn(int dealer){
		if(dealer >= numPlayers){
			turn = dealer-numPlayers;
		}
		else{
			turn = dealer + 1;
		}
	}

	/**
	 * Inits the starting Phase for each player
	 * @param numPlayers
	 */
	private void initCurrentPhase(int numPlayers){
		currentPhase = new int[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			currentPhase[i] = 1;
		}
	}

	/**
	 * Inits the skipped array to be all false
	 */
	private void initSkipped(){
		this.skipped = new boolean[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			this.skipped[i] = false;
		}
	}

	public void nullAllButHandOf(int playerId){
		// Save the hand of the player.
		Hand keeper = this.hands[playerId];

		// null all other hands and deck

		// Save top Card of deck
		Card tempTopCard = this.deck.removeTopCard();
		// Null out Deck
		this.deck.nullifyDeck();
		this.deck.add(tempTopCard);

		// Save top card of discard pile
		Card tempDiscardCard = this.discardPile.removeTopCard();
		// Null out Deck
		this.discardPile.nullifyDeck();
		this.discardPile.add(tempDiscardCard);

		// Null all the Hands
		synchronized(this.hands){
			for(Hand h : hands){
				h.nullifyDeck();
			}
			// add back this users Hand
			hands[playerId] = keeper;
		}
	}
}
