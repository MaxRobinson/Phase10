package edu.up.cs301.phase10;

import java.util.ArrayList;
import java.util.Locale;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.CardColor;
import edu.up.cs301.card.Rank;
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
	private Phase[] laidPhases;

	private boolean hasDrawn;
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
		initLaidPhases();

		hasDrawn = false;
		
		
		/*Put game in a state I want */
		ArrayList<Card> cards = new ArrayList<Card>();
		Rank r = Rank.valueOf("ONE");
		CardColor col = CardColor.valueOf("Orange");
		// Set
		Card c = new Card(r,col);
		/*cards.add(c);
		c = new Card(r,col);
		cards.add(c);
		c = new Card(r,col);
		cards.add(c);
		c = new Card(r,col);
		cards.add(c);*/
		
		col = CardColor.valueOf("Blue");
		r = Rank.valueOf("ONE");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("THREE");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("TWO");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("FOUR");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("FIVE");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("SIX");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("SEVEN");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("FOUR");
		c = new Card(r,col);
		cards.add(c);
		r = Rank.valueOf("NINE");
		c = new Card(r,col);
		cards.add(c);
		
		hands[0].setcards(cards);
		
		currentPhase[0] = 4;

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
		this.players = new GamePlayer[state.players.length];
		//copy the input into the local game list of players. //This is to make sure no reference carry overs.
		for(int i = 0; i< state.players.length; i++){
			this.players[i] = state.players[i];
		}
		//setNumber of players
		this.numPlayers = state.players.length;

		// init Deck
		this.deck = new Deck(state.deck);

		// init Discard pile with top card from Deck
		this.discardPile = new Deck(state.discardPile);

		this.hands = new Hand[state.numPlayers];
		for(int i = 0; i < state.players.length; i++){
			this.hands[i] = new Hand(state.hands[i]);
		}

		// init the dealer to random player
		this.dealer = state.dealer;
		this.turn = state.turn;

		currentPhase = new int[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			this.currentPhase[i] = state.currentPhase[i];
		}

		skipped = new boolean[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			this.skipped[i] = state.skipped[i];
		}

		this.score = new int[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			this.score[i] = state.score[i];
		}

		this.laidPhases = new Phase[state.numPlayers];
		for(int i = 0; i< state.players.length; i++){
			this.laidPhases[i] = state.laidPhases[i];
		}
		this.hasDrawn = state.hasDrawn;

	}

	public PhaseState(GamePlayer players[], Deck deck, Deck discard, Hand hand[], int dealer, int turn, 
			int currentPhase[], boolean skipped[], int score[],Phase laidPhase[], boolean hasDrawn){
		this.players = new GamePlayer[players.length];
		//copy the input into the local game list of players. //This is to make sure no reference carry overs.
		for(int i = 0; i< players.length; i++){
			this.players[i] = players[i];
		}
		//setNumber of players
		this.numPlayers = players.length;

		// init Deck
		this.deck = new Deck(deck);

		// init Discard pile with top card from Deck
		this.discardPile = new Deck(discard);

		this.hands = new Hand[this.numPlayers];
		for(int i = 0; i < players.length; i++){
			this.hands[i] = hands[i];
		}

		// init the dealer to random player
		this.dealer = dealer;
		this.turn = turn;

		this.currentPhase = new int[this.numPlayers];
		for(int i = 0; i< currentPhase.length; i++){
			this.currentPhase[i] = currentPhase[i];
		}

		this.skipped = new boolean[this.numPlayers];
		for(int i = 0; i< skipped.length; i++){
			this.skipped[i] = skipped[i];
		}

		this.score = new int[this.numPlayers];
		for(int i = 0; i< score.length; i++){
			this.score[i] = score[i];
		}

		this.laidPhases = new Phase[this.numPlayers];
		for(int i = 0; i< laidPhases.length; i++){
			this.laidPhases[i] = laidPhases[i];
		}

		this.hasDrawn = hasDrawn;
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

	public void setHands(Hand hand, int handNum){
		this.hands[handNum] = hand;
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
	public void nextTurn()
	{
		this.turn = (this.turn+1)%numPlayers;
	}

	public int[] getCurrentPhase(){
		return this.currentPhase;
	}

	public void setCurrentPhase(int[] currentPhase){
		this.currentPhase = currentPhase;
	}

	public void setCurrentPhase(int phase, int playerId){
		this.currentPhase[playerId] = phase;
	}

	public void setCurrentPhase(Phase phase, int playerId){
		this.laidPhases[playerId] = phase;
	}

	public boolean[] getSkipped(){
		return this.skipped;
	}

	public void setSkipped(int skipped){
		this.skipped[skipped] = true;
	}

	public int[] getScore(){
		return this.score;
	}

	public void setScore(int[] scores){
		this.score = scores;
	}

	public void setScore(int playerId, int score){
		this.score[playerId] = score;
	}

	public Phase[] getLaidPhases(){
		return this.laidPhases;
	}

	public GamePlayer[] getPlayers(){
		return this.players;
	}

	public boolean getHasDrawn(){
		return this.hasDrawn;
	}

	public void setHasDrawn(boolean hasDrawn){
		this.hasDrawn = hasDrawn;
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
	public void dealHands(Deck deck){
		for(int i = 0; i < this.INITHANDSIZE; i++){
			for(int j = 0; j < this.numPlayers; j++){
				hands[j].add(deck.removeTopCard());
			}
		}
	}
	
	/**
	 * This initializes the array of laid phases for all players in the game.
	 */
	public void initLaidPhases(){
		for(int i = 0; i< this.numPlayers; i++){
			this.laidPhases[i] = new Phase();
		}
	}
	
	/**
	 * This initiallizes the array of hands for all players in the game.
	 */
	public void initHands(){
		for(int i = 0; i<this.numPlayers; i++){
			this.hands[i] = new Hand();
		}
	}

	/**
	 * Inits which player goes first
	 * @param dealer
	 */
	public void initTurn(int dealer){
		if(dealer >= (numPlayers-1)){ // > = numPlayers - 1  this is what it should be
			turn = dealer-(numPlayers-1); // numPlayers - 1  ""
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
	//	Hand keeper = this.hands[playerId];

		// null all other hands and deck

		// Save top Card of deck
		Card tempCard = this.deck.removeTopCard();
		Card tempTopCard = null;
		if(tempCard != null){
			tempTopCard = new Card(tempCard.getRank(),tempCard.getCardColor());
		}
		// Null out Deck
		this.deck.nullifyDeck();
		this.deck.add(tempTopCard);

		// Save top card of discard pile
		tempCard = this.discardPile.removeTopCard();
		Card tempDiscardCard = null;
		if(tempCard != null){
			tempDiscardCard = new Card(tempCard.getRank(),tempCard.getCardColor());	
		}
		// Null out Deck
		this.discardPile.nullifyDeck();
		this.discardPile.add(tempDiscardCard);

		for(int i = 0; i < hands.length; i++){
			if(i != playerId){
				hands[i].nullifyDeck();
			}
		}
//		// Null all the Hands
//		for(Hand h : hands){
//			h.nullifyDeck();
//		}
//		// add back this users Hand
//		hands[playerId] = keeper;
	}
}
