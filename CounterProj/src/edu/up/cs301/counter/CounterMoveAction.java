package edu.up.cs301.counter;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A CounterMoveAction is an action that is a "move" the game: either increasing
 * or decreasing the counter value.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version September 2012
 */
public class CounterMoveAction extends GameAction {
	
	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	//whether this move is a plus (true) or minus (false)
	private boolean isPlus;
	
	/**
	 * Constructor for the CounterMoveAction class.
	 * 
	 * @param source
	 *            the player making the move
	 * @param isPlus
	 *            value to initialize this.isPlus
	 */
	public CounterMoveAction(GamePlayer player, boolean isPlus) {
		super(player);
		this.isPlus = isPlus;
	}
	
	/**
	 * getter method, to tell whether the move is a "plus"
	 * 
	 * @return
	 * 		a boolean that tells whether this move is a "plus"
	 */
	public boolean isPlus() {
		return isPlus;
		
	}
}//class CounterMoveAction
