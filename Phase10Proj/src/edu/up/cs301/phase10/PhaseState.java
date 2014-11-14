package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
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
	
	//Private instance variables
	private Deck deck;
	private Deck discardPile;
	private Hand[] hands;
	private int turn;
	private int[] currentPhase;
	private boolean[] skipped;
	private int[] score;
	private int dealer;
	public ArrayList<Phase> layedPhases;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public PhaseState(){
		
	}

}
