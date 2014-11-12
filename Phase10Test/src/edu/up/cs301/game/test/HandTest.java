package edu.up.cs301.game.test;

import java.util.ArrayList;
import android.test.*;
import android.util.Log;
 

import edu.up.cs301.card.Card;
import edu.up.cs301.phase10.Hand;
import edu.up.cs301.phase10.PhaseMainActivity;

public class HandTest extends android.test.ActivityInstrumentationTestCase2<PhaseMainActivity>{
	
	private Hand hand;
	
	public HandTest(){
		super(PhaseMainActivity.class);
	}
	
	public void setUp(){
		hand = new Hand();
	}
	
	public void testGetHand(){
		hand.add108();
		Log.i("Hand Values:", hand.toString());
		/*ArrayList<Card> tempHand = this.hand.getCards();
		
		for(int i = 0; i< tempHand.size(); i++){
			System.out.println(tempHand.get(i).toString());
		}*/
		assertTrue("Hand is not null", hand != null);
		
	}
	
}
