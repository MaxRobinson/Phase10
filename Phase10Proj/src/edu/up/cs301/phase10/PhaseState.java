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
 * @version 11/10/14
 *
 */

public class PhaseState extends GameState {
	
	//CONSTANTS
	private final int INITHANDSIZE = 10;
	private final int MAXHANDSIZE = 11;
	
	//Private instance variables
	private Deck deck;
	private Deck discardPile;
	private Hand[] hands;
	private int dealer;
	private int turn;
	private int[] currentPhase;
	private boolean[] skipped;
	private int[] score;
	private GamePlayer[] players;
	private int numPlayers;
	public ArrayList<Phase> layedPhases;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
		
		
		
		
	}
	
	/**
	 * Constructor
	 * @param numPlayers
	 */
	public PhaseState(int numPlayers){
		
		
		
	}
	
	/**
	 * Copy constructor
	 * @param state
	 */
	public PhaseState(PhaseState state){
		
	}
	
	/**
	 * Set who the dealer is
	 * @param id
	 */
	public void setDealer(int id){
		dealer = id;
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
}
