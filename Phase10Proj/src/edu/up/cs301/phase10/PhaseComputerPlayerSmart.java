package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.card.CardColor;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

public class PhaseComputerPlayerSmart extends PhaseComputerPlayer {

	private int phaseNum;
	private Hand hand;
	private PhaseState state;
	public PhaseComputerPlayerSmart(String name) {
		super(name);
		phaseNum = -1;
		}

    @Override
    protected void receiveInfo(GameInfo info) {
    	// if it was a "not your turn" message, just ignore it
    	if (info instanceof NotYourTurnInfo) return;
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (!(info instanceof PhaseState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
    	state = (PhaseState)info;
    	if(state == null)
    	{
    		return;
    	}
    	hand = state.getHands()[this.playerNum];
    	phaseNum = state.getCurrentPhase()[this.playerNum];
    	boolean drawPile = !dicardPile();
    	game.sendAction(new PhaseDrawCardAction(this, drawPile));
		int playerId = 0;
//		for(int i = 0; i < state.getPlayers().length; i++){
//			if(state.getPlayers()[i].equals(this)){
//				playerId = i;
//			}
//
//		}
		playerId = this.playerNum;
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		canPhase();
		hitting();
		int toDiscard = discard();
		Card tempCard = state.getHands()[playerId].getCards().get(toDiscard);
		
		if(tempCard.equals(new Card(Rank.TWO,CardColor.Orange)))
		{
	    	game.sendAction(new PhaseSkipAction(this, state.getHands()[playerId].getCards().get(toDiscard),chooseSkip()));

		}
		else
		{
			game.sendAction(new PhaseDiscardAction(this, state.getHands()[playerId].getCards().get(toDiscard)));
		}
	}
    
    private int discard()
    {
    	int index = 0;
    	int value[] = new int[this.hand.size()];
    	for(int i = 0; i < hand.size(); i++)
    	{
    		if(hand.getCard(i).equals(new Card(Rank.ONE,CardColor.Orange)))
    		{
    			value[i]=-1;
    		}
    		if(hand.getCard(i).equals(new Card(Rank.TWO,CardColor.Orange)))
    		{
    			value[i] = Integer.MAX_VALUE;
    			break;
    		}
    		Card toDiscard = hand.getCard(i);
    		Hand tempHand = new Hand (hand);
    		tempHand.removeCard(toDiscard);
        	switch (phaseNum)
        	{
        		case 1:
        			if(! addsToSet(toDiscard, tempHand, 3, false))
        			{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;
        		case 2:
        			if (! addsToSet(toDiscard, tempHand, 3, false) || addsToRun(toDiscard, tempHand, 4, false ))
                			{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 3:
        			if (! addsToSet(toDiscard, tempHand, 4, false) || addsToRun(toDiscard, tempHand, 4, false ))
        			{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 4:
        			if (! addsToRun(toDiscard, tempHand, 7, false))
        			{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 5:
        			if (! addsToRun(toDiscard, tempHand, 8, false))
                	{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 6:
        			if (! addsToRun(toDiscard, tempHand, 9, false))
        			{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 7:
        			if (! addsToSet(toDiscard, tempHand, 4, false))
               		{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 8:
        			if(! mostCardOfColor(tempHand).contains(toDiscard.getCardColor()))
                	{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 9:
        			if(! addsToRun(toDiscard, tempHand, 4, false))
        			{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        		case 10:
        			if (! addsToSet(toDiscard, tempHand, 5, false))
               		{
        				value[i] = 100 + toDiscard.getRank().ordinal();
        			}
        			break;

        	}    		
    	}
    	int temp = 0;
    	for(int i = 0; i < value.length; i++)
    	{
    		if(temp < value[i])
    		{
    			index = i;
    		}
    	}
		return index;
    	
    }
    private int chooseSkip()
    {
    	int max = 0;
    	int maxPerson = 0;
    	int tiebreakerPoints = 0;
    	for(int i = 0; i < state.getCurrentPhase().length;i++)
    	{
    		if (state.getCurrentPhase()[i]>=max && i != this.playerNum)
    		{
    			if(max == state.getCurrentPhase()[i])
    			{
    				if(state.getScore()[i] > tiebreakerPoints);
    				{
    					maxPerson = i;
    					tiebreakerPoints = state.getScore()[i];
    				}
    			}
    			else
    			{
    				max = state.getCurrentPhase()[i];
    				maxPerson = i;
    			}
    		}
    	}
    	return maxPerson;
    	
    }
    private boolean dicardPile()
    {
    	Card topDiscard = state.getDiscardPile().peekAtTopCard();
    	if(topDiscard == null)
    	{
    		return false;
    	}
    	if(topDiscard.equals(new Card(Rank.ONE,CardColor.Orange)))
    	{
    		return true;
    	}
    	switch (phaseNum)
    	{
    		case 1:
    			return addsToSet(topDiscard, hand, 3, false);
    		case 2:
    			return (addsToSet(topDiscard, hand, 3, false) || addsToRun(topDiscard, hand, 4, false ));
    		case 3:
    			return (addsToSet(topDiscard, hand, 4, false) || addsToRun(topDiscard, hand, 4, false ));
    		case 4:
    			return addsToRun(topDiscard, hand, 7, false);
    		case 5:
    			return addsToRun(topDiscard, hand, 8, false);
    		case 6:
    			return addsToRun(topDiscard, hand, 9, false);
    		case 7:
    			return addsToSet(topDiscard, hand, 4, false);
    		case 8:
    			return mostCardOfColor(hand).contains(topDiscard.getCardColor());
    		case 9:
    			return addsToRun(topDiscard, hand, 4, false);
    		case 10:
    			return addsToSet(topDiscard, hand, 5, false);
    	}
    	return false;
    }
    
	private void canPhase()
    {
    	Hand copyHand = new Hand(hand);
		ArrayList<Card> phaseHand;
		ArrayList<Card> phaseHand2;
    	switch (phaseNum)
    	{
    		
    		case 1:
    			phaseHand = phaseHasSet(copyHand, 3);
    			if (phaseHand == null)
    			{
    				break;
    			}
    			copyHand.removeCards(phaseHand);
    			phaseHand2 = phaseHasSet(copyHand, 3);
    			if (phaseHand2 == null)
    			{
    				break;
    			}
    			phaseHand.addAll(phaseHand2);
    	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 2:
    			phaseHand = phaseHasRun(copyHand, 4);
    			if (phaseHand == null)
    			{
    				break;
    			}
    			copyHand.removeCards(phaseHand);
    			phaseHand.addAll(phaseHasSet(copyHand, 3));
    			game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 3:
    			phaseHand = phaseHasSet(copyHand, 4);
    			if (phaseHand == null)
    			{
    				break;
    			}
    			copyHand.removeCards(phaseHand);
    			phaseHand.addAll(phaseHasSet(copyHand, 4));
    	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 4:
    			phaseHand = phaseHasRun(copyHand, 7);
    			if (phaseHand == null)
    			{
    				break;
    			}
       	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 5:
    			phaseHand = phaseHasRun(copyHand, 8);
    			if (phaseHand == null)
    			{
    				break;
    			}
       	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 6:
    			phaseHand = phaseHasRun(copyHand, 9);
    			if (phaseHand == null)
    			{
    				break;
    			}
       	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    			
    		case 7:
    			phaseHand = phaseHasSet(copyHand, 4);
    			if (phaseHand == null)
    			{
    				break;
    			}
    			copyHand.removeCards(phaseHand);
    			phaseHand.addAll(phaseHasSet(copyHand, 4));
    	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 8:
    			phaseHand = phaseHasColor(copyHand);
    			if (phaseHand == null)
    			{
    				break;
    			}
       	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
       	    	break;
    		case 9:
    			phaseHand = phaseHasSet(copyHand, 5);
    			if (phaseHand == null)
    			{
    				break;
    			}
    			copyHand.removeCards(phaseHand);
    			phaseHand.addAll(phaseHasSet(copyHand, 2));
    	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;
    		case 10:
    			phaseHand = phaseHasSet(copyHand, 5);
    			if (phaseHand == null)
    			{
    				break;
    			}
    			copyHand.removeCards(phaseHand);
    			phaseHand.addAll(phaseHasSet(copyHand, 3));
    	    	game.sendAction(new PhaseLayPhaseAction(this,new Phase(phaseHand,null)));
    			break;

    	}
    }
    private ArrayList<Card> phaseHasSet(Hand tempHand, int size)
    {
        int[] anArray = new int[13];
 
        for(int i = 1; i < tempHand.size(); i++)
        {
        	anArray[tempHand.getCard(i).getRank().ordinal()]++;
        }
        ArrayList<Integer> sets = new ArrayList<Integer>();
        for(int i = 1; i < anArray.length; i++)
        {
        	if(anArray[i] >= size)
        	{
        		sets.add(i);
        	}
        }
        ArrayList<Card> returnCards = new ArrayList<Card>();
        int numAdded =0;
        if (sets.size()==0)
        {
        	return null;
        }
        for(int i = 0; i < sets.size(); i++)
        {
        	if(anArray[tempHand.getCard(i).getRank().ordinal()] == sets.get(i));
        	{
        		returnCards.add(tempHand.getCard(i));
        		numAdded++;
        		if (numAdded >= size)
        		{
        			break;
        		}
        	}
        }
    	
		return returnCards;
    	
    }
    
    private ArrayList<Card> phaseHasRun(Hand tempHand1, int size)
    {
    	Hand tempHand = new Hand(tempHand1);
        int[] anArray = new int[13];
        for(int i = 1; i < tempHand.size(); i++)
        {
        	anArray[tempHand.getCard(i).getRank().ordinal()]++;
        }
    	int numWild = 0;
    	for(int i = 0; tempHand.size() > i; i++)
    	{
    		if(tempHand.getCard(i).equals(new Card(Rank.ONE,CardColor.Orange)))
    		{
    			numWild++;
    		}
    	}
        ArrayList<Integer> curRun = new ArrayList<Integer>();
        ArrayList<Integer> bigRun = null;
        
        for(int i = 1; i < anArray.length; i++)
        {
        	if(anArray[i] >= 1)
        	{
        		curRun.add(i);
        	}
        	else if (numWild > 0)
        	{
        		numWild--;
        		curRun.add(-1);
        	}
        	else
        	{
        		if(curRun.size()>=size)
        		{
        			bigRun = new ArrayList<Integer>(curRun); 
        		}
        		curRun.clear();
        	}
        }
       if (bigRun == null)
       {
    	   return null;
       }
       ArrayList<Card> returnCards = new ArrayList<Card>();
       for (int i = 0; i<tempHand.size(); i++)
       {
    	   if(bigRun.remove(tempHand.getCard(i).getRank().ordinal()) != null)
    	   {
    		   returnCards.add(tempHand.getCard(i));
    	   }
       }
		return returnCards;
    	
    }
    private ArrayList<Card> phaseHasColor(Hand tempHand)
    {
    	ArrayList<Card> blue = new ArrayList<Card>();
    	ArrayList<Card> green = new ArrayList<Card>();
    	ArrayList<Card> red = new ArrayList<Card>();
    	ArrayList<Card> yellow = new ArrayList<Card>();
    	
    	for(int i = 0; i < tempHand.size(); i++)
    	{
    		CardColor c = tempHand.getCard(i).getCardColor();
    		if (c == CardColor.Blue)
			{
				blue.add(tempHand.getCard(i));
			}
			else if (c == CardColor.Green)
			{
				green.add(tempHand.getCard(i));
			}
			else if (c == CardColor.Red)
			{
				red.add(tempHand.getCard(i));
			}
			else if (c == CardColor.Yellow)
			{
				yellow.add(tempHand.getCard(i));
			}
    	}
    	if(yellow.size()>=7)
		{
			return yellow;
		}
		if(blue.size()==7)
		{
			return blue;
		}
		if(green.size()==7)
		{
			return green;
		}
		if(red.size()==7)
		{
			return red;
		}
		return null;
    	
		    	
    }
    
    private boolean addsToRun(Card card, Hand tempHand,int setSize,boolean addTo)
    {
    	if(tempHand.containsRank(card))
    	{
    		return false;
    	}
    	Rank cardRank = card.getRank();

    	int numInRunA = 0;
    	int numInRunB = 0;
    	int numWild = 0;
    	for(int i = 0; tempHand.size() > i; i++)
    	{
    		if(tempHand.getCard(i).equals(new Card(Rank.ONE,CardColor.Orange)))
    		{
    			numWild++;
    		}
    	}
		int j = 1;
		while (tempHand.containsRank(new Card (intToRank(j+card.getRank().ordinal()), CardColor.Blue)))
		{	
			numInRunA++;
			j++;
		}
		j = -1;
		while (tempHand.containsRank(new Card (intToRank(j+card.getRank().ordinal()), CardColor.Blue)))
		{	
			numInRunB++;
			j--;
		}
    	if(!addTo && numInRunB >= setSize || numInRunA >= setSize)
    	{
    		return false;
    	}
    	if (addTo && numInRunA + numInRunB >=1)
    	{
    		return true;
    	}
    	if (numInRunA + numInRunB > 2)
    	{
    		return true;
    	}
	    return false;
    }
    private boolean addsToSet(Card card, Hand tempHand,int setSize,boolean addTo)
    {
    	Rank cardRank = card.getRank();
    	int numEqualRank = 0;
    	for(int i = 0; tempHand.size() > i; i++)
    	{
    		if (cardRank.equals(tempHand.getCard(i).getRank()))
    		{
    			numEqualRank++;
    		}
    	}
    	if (numEqualRank > 2)
    	{
	    	if(!addTo)
	    	{
	    		if (numEqualRank < setSize)
	    		{
	    			return true;
	    		}
	    		else
	    		{
	    			return false;
	    		}
	    	}
	    	return true;
	    	
    	}
	    return false;
    }
    private ArrayList<CardColor> mostCardOfColor(Hand tempHand)
    {
    	ArrayList<Card> blue = new ArrayList<Card>(); 
		ArrayList<Card> green = new ArrayList<Card>(); 
		ArrayList<Card> red = new ArrayList<Card>(); 
		ArrayList<Card> yellow = new ArrayList<Card>();
		for(int i = 0; tempHand.size() > i; i++)
		{
			Card tempCard = tempHand.getCard(i);
			CardColor tempColor = tempCard.getCardColor();
			
			if (tempColor == CardColor.Blue)
			{
				blue.add(tempHand.getCard(i));
			}
			else if (tempColor == CardColor.Green)
			{
				green.add(tempHand.getCard(i));
			}
			else if (tempColor == CardColor.Red)
			{
				red.add(tempHand.getCard(i));
			}
			else if (tempColor == CardColor.Yellow)
			{
				yellow.add(tempHand.getCard(i));
			}
		}
		int maxSize = blue.size();
		ArrayList<CardColor> returnList = new ArrayList<CardColor>();
		if(green.size()>maxSize)
		{
			maxSize = green.size();
		}
		if(red.size()>maxSize)
		{
			maxSize = red.size();
		}
		if(yellow.size()>=maxSize)
		{
			maxSize = yellow.size();
			returnList.add(CardColor.Yellow);
		}
		if(blue.size()==maxSize)
		{
			returnList.add(CardColor.Blue);
		}
		if(green.size()==maxSize)
		{
			returnList.add(CardColor.Green);
		}
		if(red.size()==maxSize)
		{
			returnList.add(CardColor.Red);
		}
		return returnList;
    }
    private Rank intToRank(int r)
    {
    	switch (r)
    	{
			case 1:
				return Rank.ONE;
			case 2:
				return Rank.TWO;
			case 3:
				return Rank.THREE;
			case 4:
				return Rank.FOUR;
			case 5:				
				return Rank.FIVE;
			case 6:
				return Rank.SIX;
			case 7:
				return Rank.SEVEN;
			case 8:
				return Rank.EIGHT;
			case 9:
				return Rank.NINE;
			case 10:
				return Rank.TEN;
			case 11:
				return Rank.ELEVEN;
			case 12:
				return Rank.TWELVE;
    	}
		return Rank.ONE;
    	
    }
    
    private void hitting(){
    	
    }
}
