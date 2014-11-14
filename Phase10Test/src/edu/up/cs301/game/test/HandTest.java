package edu.up.cs301.game.test;

import java.util.ArrayList;
import android.test.*;
import android.util.Log;
 

import edu.up.cs301.card.Card;
import edu.up.cs301.phase10.Deck;
import edu.up.cs301.phase10.Hand;
import edu.up.cs301.phase10.PhaseMainActivity;

public class HandTest extends android.test.ActivityInstrumentationTestCase2<PhaseMainActivity>{
	
	private Deck deck;
	
	public HandTest(){
		super(PhaseMainActivity.class);
	}
	
	public void setUp(){
		deck = new Deck();
	}
	
	/**
	 * Test getting cards from a hand and functionality of removal 
	 * of cards and what not. 
	 * 
	 */
	public void testGetHand(){
		deck.add108();
		Hand playerHand = new Hand();
		Log.i("Hand Values:", deck.toString());
		
		assertTrue("Deck is not null", deck != null);
		
		//create a hand that is not the deck. 
		for(int i= 0; i< 10; i++){
			Card card = deck.removeTopCard();
			playerHand.add(card);
		}
		
		assertTrue("Player Hand has 10 cards", playerHand.size() == 10);
		assertTrue("Deck has 98 cards", deck.size() == 98);
		
		
	}
	
}
