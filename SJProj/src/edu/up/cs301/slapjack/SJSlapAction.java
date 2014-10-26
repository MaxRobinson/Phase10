package edu.up.cs301.slapjack;

import edu.up.cs301.game.GamePlayer;

/**
 * A SJSlapAction is an action that represents slapping the card that is
 * on the "up" pile.
 * 
 * @author Steven R. Vegdahl
 * @version 31 July 2002
 */
public class SJSlapAction extends SJMoveAction
{
	private static final long serialVersionUID = 2134321631283669359L;

	/**
     * Constructor for the SJSlapMoveAction class.
     * 
     * @param source  the player making the move
     */
    public SJSlapAction(GamePlayer player)
    {
        // initialize the source with the superclass constructor
        super(player);
    }

    /**
	 * @return whether this action is a "slap" move
     */
    public boolean isSlap() {
        return true;
    }
    
}
