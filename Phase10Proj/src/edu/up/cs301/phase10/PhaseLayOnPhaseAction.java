package edu.up.cs301.phase10;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;

public class PhaseLayOnPhaseAction extends PhaseMoveAction {

	// private instance variables
	private static final long serialVersionUID = 9122955413755544288L;
	private Card toLay;
	private int idToLayOn;
	private int whichPart;
	private int topOrBottom;
	private int numWildCards; 
	
	
	/**
	 * Constructor for the PhaseLayOnPhaseAction.
	 * 
	 * @param player
	 * @param toLay
	 * @param idToLayOn
	 * @param whichPart
	 * @param topOrBottom
	 */
	public PhaseLayOnPhaseAction(GamePlayer player, Card toLay, int idToLayOn, int whichPart, int topOrBottom) {
		super(player);
		this.toLay = toLay;
		this.idToLayOn = idToLayOn;
		this.whichPart = whichPart;
		this.topOrBottom = topOrBottom;
		this.numWildCards = 0;
	}
	
	public PhaseLayOnPhaseAction(GamePlayer player, Card toLay, int idToLayOn, int whichPart, int topOrBottom, int numWildCards) {
		super(player);
		this.toLay = toLay;
		this.idToLayOn = idToLayOn;
		this.whichPart = whichPart;
		this.topOrBottom = topOrBottom;
		this.numWildCards = numWildCards;
	}
	
	@Override
	public boolean isLayOnPhaseAction(){
		return true;
	}
	
	public Card getToLay(){
		return this.toLay;
	}
	
	public int getIdToLayOn(){
		return this.idToLayOn;
	}
	
	public int getWhichPart(){
		return this.whichPart;
	}
	
	public int getTopOrBottom(){
		return this.topOrBottom;
	}
	
	public int getNumWilds(){
		return this.numWildCards;
	}

}
