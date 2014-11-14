package edu.up.cs301.phase10;

import java.util.ArrayList;
import java.util.Arrays;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * Contains the State of the Phase 10 Game. Sent by the game when
 * a player wants to enquire about the state of the game.  (E.g., to display
 * it, or to help figure out its next move.)
 *
 * @author Dan Nelson
 * @version 11/13/14
 * @author Max
 * @version 11/10/14
 *
 */

public class PhaseState extends GameState {
    
    //Private instance variables
    
    /**
     * Contains each players current hand
     */
    private ArrayList<Card> hands[];
    
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
     * ID of player who is current round's dealer
     */
    private int dealer;
    
    /**
     * Cards that are currently in the deck
     */
    private ArrayList<Card> deck;
    
    /**
     * Cards that are currently in the discard pile
     */
    private ArrayList<Card> discard;
    
    /**
     * Indexed by ID, then by 0 being first part
     * of phase objective or if only one objective,
     * located in 0, then all cards in that part of phase
     */
    private ArrayList<ArrayList<ArrayList<Card>>> layedPhases;
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Constructor
     */
    public PhaseState(int numPlayers){
        // Create hands
        // TODO what to put in here?
        hands = (ArrayList<Card>[])new ArrayList[numPlayers];
        
        // ID of player who is current round's dealer
        dealer = (int) Math.round(Math.random()*numPlayers);
        
        // Current player whose turn it is
        if(dealer == 0){
            turn = numPlayers;
        }
        else if(dealer == numPlayers){
            turn = 0;
        }
        else{
            turn = dealer + 1;
        }
        
        // Players current phase
        currentPhase = new int[numPlayers];
        Arrays.fill(currentPhase, 1);
        
        // Players that are skipped
        skipped = new boolean[numPlayers];
        Arrays.fill(skipped, false);
        
        // Each players current score indexed by ID
        score = new int[numPlayers];
        Arrays.fill(score, 0);
        
        // Cards that are currently in the deck
        // TODO Fill
        deck = new ArrayList<Card>();
        
        // Cards that are currently in the discard pile
        // TODO Fill
        discard = new ArrayList<Card>();
        
        // Players laid phases
        layedPhases = new ArrayList<ArrayList<ArrayList<Card>>>();
    }
    
}
