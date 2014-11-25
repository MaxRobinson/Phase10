package edu.up.cs301.game.test;

import edu.up.cs301.game.GamePlayer;

import android.util.Log;
import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.config.GamePlayerType;

import edu.up.cs301.phase10.Deck;
import edu.up.cs301.phase10.Hand;
import edu.up.cs301.phase10.Phase;
import edu.up.cs301.phase10.PhaseComputerPlayer0;
import edu.up.cs301.phase10.PhaseHumanPlayer;
import edu.up.cs301.phase10.PhaseMainActivity;
import edu.up.cs301.phase10.PhaseState;


public class PhaseStateTest extends android.test.ActivityInstrumentationTestCase2<PhaseMainActivity> {
	

	private int numPLAYA;
	private static final int INITHANDSIZE = 10;

	PhaseState state;
	public static final int NUM_PLAYERS = 2;

	public PhaseStateTest(){
		super(PhaseMainActivity.class);
	}
	
	// This is not needed for the way that we are testing
	// Leaving blank so that it shows this was done intentionaly.
	public void setUp(){

		numPLAYA = 2;
		GamePlayer[] players = new GamePlayer[2];
		GamePlayerType[] availTypes = {new GamePlayerType("human player") {
			public GamePlayer createPlayer(String name) {
				return new PhaseHumanPlayer(name);
			}}, 
			
			new GamePlayerType("Computer Player (PASSIVE BOTS)"){
			public GamePlayer createPlayer(String name)
			{
				return new PhaseComputerPlayer0(name);
			}}
			
		};
		String name = "Dan";
		GamePlayerType gpt = availTypes[0]; 
		players[0] = gpt.createPlayer(name);
		
		name =  "Bot";
		gpt = availTypes[1];
		players[1] = gpt.createPlayer(name);
		
		
		Deck deck = new Deck();
		deck.add108();
		deck.shuffle();
		
		Deck discard = new Deck();
		
		int score[] = new int[NUM_PLAYERS];
		
		Phase laidPhases[] = new Phase[NUM_PLAYERS];
		boolean hasDrawn = false;
		
		Hand hand[] = new Hand[NUM_PLAYERS];
		
		int dealer = 0;
		int turn = 1;
		
		int currentPhase[] = new int[NUM_PLAYERS];
		boolean skipped[] = new boolean[NUM_PLAYERS];
		
		state = new PhaseState(players, deck, discard, hand, dealer, turn, currentPhase, skipped, 
								score, laidPhases, hasDrawn);

	}
	
	public void testNullAllButHand()
	{
//		PhaseState(GamePlayer players[], Deck deck, Deck discard, Hand hand[], int dealer, int turn, 
//				 int currentPhase[], boolean skipped[], int score[],Phase laidPhase[], boolean hasDrawn)
		GamePlayer players[] = {new PhaseHumanPlayer("VERONICA"), new PhaseComputerPlayer0("JASMINE")};
		Deck myDeck = new Deck();
		myDeck.add108().shuffle();
		
		Deck myDiscard = new Deck(); 
		myDiscard.add(myDeck.removeTopCard());
		
		Hand[] hands = new Hand[2]; 
		initHands(hands); 
		dealHands(myDeck, hands);

		// init the dealer to random player
		int dealer = 0;

		// init who goes first
		int turn = initTurn(dealer);

		// init starting Phases
		int[] currentPhase = initCurrentPhase(this.numPLAYA);
		
		boolean[] skipped = new boolean[this.numPLAYA];
		// init Skipped array
		initSkipped(skipped);

		// init score
		int[] score = new int[this.numPLAYA];

		//init laid Phases
		Phase[] laidPhases = new Phase[this.numPLAYA];	
		
		boolean hasDrawn = false;
		
		PhaseState state = new PhaseState(players, myDeck, myDiscard, hands, dealer, turn, 
				currentPhase, skipped, score, laidPhases, hasDrawn);
		
		assertTrue("l;afksjdf;laksjdf", state.getCurrentPhase()[0] == 1 );
		
	}
	
	public void test2()
	{
//		PhaseState(GamePlayer players[], Deck deck, Deck discard, Hand hand[], int dealer, int turn, 
//				 int currentPhase[], boolean skipped[], int score[],Phase laidPhase[], boolean hasDrawn)
		GamePlayer players[] = {new PhaseHumanPlayer("VERONICA"), new PhaseComputerPlayer0("JASMINE")};
		Deck myDeck = new Deck();
		myDeck.add108().shuffle();
		
		Deck myDiscard = new Deck(); 
		myDiscard.add(myDeck.removeTopCard());
		
		Hand[] hands = new Hand[2]; 
		initHands(hands); 
		dealHands(myDeck, hands);

		// init the dealer to random player
		int dealer = 0;

		// init who goes first
		int turn = initTurn(dealer);

		// init starting Phases
		int[] currentPhase = initCurrentPhase(this.numPLAYA);
		
		boolean[] skipped = new boolean[this.numPLAYA];
		// init Skipped array
		initSkipped(skipped);

		// init score
		int[] score = new int[this.numPLAYA];

		//init laid Phases
		Phase[] laidPhases = new Phase[this.numPLAYA];	
		
		boolean hasDrawn = false;
		
		PhaseState state = new PhaseState(players, myDeck, myDiscard, hands, dealer, turn, 
				currentPhase, skipped, score, laidPhases, hasDrawn);
		
		assertTrue("l;afksjdf;laksjdf", state.isSkipped(1) == false);
		
	}
	
	public void test3()
	{
//		PhaseState(GamePlayer players[], Deck deck, Deck discard, Hand hand[], int dealer, int turn, 
//				 int currentPhase[], boolean skipped[], int score[],Phase laidPhase[], boolean hasDrawn)
		GamePlayer players[] = {new PhaseHumanPlayer("VERONICA"), new PhaseComputerPlayer0("JASMINE")};
		Deck myDeck = new Deck();
		myDeck.add108().shuffle();
		
		Deck myDiscard = new Deck(); 
		myDiscard.add(myDeck.removeTopCard());
		
		Hand[] hands = new Hand[2]; 
		initHands(hands); 
		dealHands(myDeck, hands);

		// init the dealer to random player
		int dealer = 0;

		// init who goes first
		int turn = initTurn(dealer);

		// init starting Phases
		int[] currentPhase = initCurrentPhase(this.numPLAYA);
		
		boolean[] skipped = new boolean[this.numPLAYA];
		// init Skipped array
		initSkipped(skipped);

		// init score
		int[] score = new int[this.numPLAYA];

		//init laid Phases
		Phase[] laidPhases = new Phase[this.numPLAYA];	
		
		boolean hasDrawn = false;
		
		PhaseState state = new PhaseState(players, myDeck, myDiscard, hands, dealer, turn, 
				currentPhase, skipped, score, laidPhases, hasDrawn);
		
		assertTrue("l;afksjdf;laksjdf", state.getLaidPhases()[0] == null);
		
	}
	
	
	public void test4()
	{
//		PhaseState(GamePlayer players[], Deck deck, Deck discard, Hand hand[], int dealer, int turn, 
//				 int currentPhase[], boolean skipped[], int score[],Phase laidPhase[], boolean hasDrawn)
		GamePlayer players[] = {new PhaseHumanPlayer("VERONICA"), new PhaseComputerPlayer0("JASMINE")};
		Deck myDeck = new Deck();
		myDeck.add108().shuffle();
		
		Deck myDiscard = new Deck(); 
		myDiscard.add(myDeck.removeTopCard());
		
		Hand[] hands = new Hand[2]; 
		initHands(hands); 
		dealHands(myDeck, hands);

		// init the dealer to random player
		int dealer = 0;

		// init who goes first
		int turn = initTurn(dealer);

		// init starting Phases
		int[] currentPhase = initCurrentPhase(this.numPLAYA);
		
		boolean[] skipped = new boolean[this.numPLAYA];
		// init Skipped array
		initSkipped(skipped);

		// init score
		int[] score = new int[this.numPLAYA];

		//init laid Phases
		Phase[] laidPhases = new Phase[this.numPLAYA];	
		
		boolean hasDrawn = false;
		
		PhaseState state = new PhaseState(players, myDeck, myDiscard, hands, dealer, turn, 
				currentPhase, skipped, score, laidPhases, hasDrawn);
		
		assertTrue("l;afksjdf;laksjdf", state.getScore()[0] == 0);
		
	}
	
	private void dealHands(Deck deck, Hand[] hands){
		for(int i = 0; i < this.INITHANDSIZE; i++){
			for(int j = 0; j < this.numPLAYA; j++){
				hands[j].add(deck.removeTopCard());
			}
		}
	}
	
	/**
	 * This initiallizes the array of hands for all players in the game.
	 */
	private void initHands(Hand hands[]){
		for(int i = 0; i<this.numPLAYA; i++){
			hands[i] = new Hand();
		}
	}
	
	/**
	 * Inits which player goes first
	 * @param dealer
	 */
	private int initTurn(int dealer){
		int turn;
		if(dealer >= this.numPLAYA){
			turn = dealer-this.numPLAYA;
		}
		else{
			turn = dealer + 1;
		}
		return turn;
	}

	/**
	 * Inits the starting Phase for each player
	 * @param numPlayers
	 */
	private int[] initCurrentPhase(int numPlayers){
		int[] currentPhase = new int[numPlayers];
		for(int i = 0; i < numPlayers; i++){
			currentPhase[i] = 1;
		}
		return currentPhase;
	}

	/**
	 * Inits the skipped array to be all false
	 */
	private void initSkipped(boolean [] skipped){
		for(int i = 0; i < this.numPLAYA; i++){
			skipped[i] = false;
		}
	}
	
}
