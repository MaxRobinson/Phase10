package edu.up.cs301.game.test;

import java.util.ArrayList;
import junit.framework.Assert;

import edu.up.cs301.card.Card;
import edu.up.cs301.phase10.Hand;

public class HandTest {
	
	private Hand hand;
	
	public HandTest(){
		hand = new Hand();
	}
	
	public void testGetHand(){
		ArrayList<Card> tempHand = this.hand.getHand();
		
		for(int i = 0; i< tempHand.size(); i++){
			System.out.println(tempHand.get(i).toString());
		}
		Assert.assertTrue("Hand is not null", tempHand != null);
		
	}
	
}
