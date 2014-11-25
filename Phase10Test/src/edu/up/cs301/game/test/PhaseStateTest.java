package edu.up.cs301.game.test;

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
	
	PhaseState state;
	public static final int NUM_PLAYERS = 2;
	public PhaseStateTest(){
		super(PhaseMainActivity.class);
	}
	
	// This is not needed for the way that we are testing
	// Leaving blank so that it shows this was done intentionaly.
	public void setUp(){
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
	
	
}
