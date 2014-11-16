package edu.up.cs301.phase10;

import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.RectF;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.card.CardColor;
import edu.up.cs301.card.Rank;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
/**
 * A GUI that allows a human to play Phase 10. Moves are made by clicking
 * regions on a surface. It is laid out for landscape orientation.
 * 
 * @author Steven R. Vegdahl 
 * @author Justice R W Nichols
 * @author Max Robinson 
 * @version July 2013
 */
public class PhaseHumanPlayer extends GameHumanPlayer implements Animator {
	
	
	private final static float CARD_HEIGHT_PERCENT = 50; // height of a card
	private final static float CARD_WIDTH_PERCENT = 17; // width of a card
	private final static float LEFT_BORDER_PERCENT = 4; // width of left border
	private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
	private final static float VERTICAL_BORDER_PERCENT = 4; // width of top/bottom borders
	// instance variables
	
	// Our game state
	protected PhaseState state;
	
	// our activity
	private Activity myActivity;
	
	//the animation surface
	private AnimationSurface surface;
	
	//the background color
	private int backgroundColor;
	
	//the screen width
	private int screenWidth;
	
	//the screen Height
	private int screenHeight;
	
	
	public PhaseHumanPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Call-back method: called whenever the GUI has changed (e.g., at the beginning 
	 * of the game, or when the screen orientation changes).
	 * 
	 * @param activity
	 * 		the current activity
	 */
	public void setAsGui(GameMainActivity activity) {
		
		// remember the activity
		myActivity = activity;
		
		//Load the layout resource for the new configuration
		activity.setContentView(R.layout.phase_human_player);
		
		// link the animator (this object) to the animation surface
		surface = (AnimationSurface) myActivity.findViewById(R.id.animation_surface);
		surface.setAnimator(this);
		
		// if the state is not null, simulate having just received the state so that 
		// any state-related processing is done
		if(state != null) {
			receiveInfo(state);
		}
		screenWidth = surface.getWidth();
		screenWidth = surface.getHeight();
		Card.initImages(activity);

		
	}

	public int interval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int backgroundColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean doPause() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean doQuit() {
		// TODO Auto-generated method stub
		return false;
	}

	//Most work needs to be here but modularized 
	public void tick(Canvas canvas) {
		drawPlayerHand(canvas);
	}

	//Draw player hand on screen used in tick
	private void drawPlayerHand(Canvas g){
		//RectF cardLocation = new RectF(50.0f,4*screenHeight/5f,50.0f+50.0f,19*screenHeight/20f);
		
		//Log.w("draw", Integer.toString(screenWidth));
		for(int i = 0; i < 11; i++)
		{
			RectF cardLocation = new RectF(50f+105f*i,500f,150f+105f*i,650f);
			drawCard(g, cardLocation, new Card(Rank.ONE, CardColor.Blue));
		}
	}
	
	private static void drawCard(Canvas g, RectF boundingBox, Card card)
	{
		//just null checks
		if(g == null|| boundingBox == null)
		{
			return;
		}
		//pass null for back of card which is mapped to orange 3 
		if(card == null)
		{
			
			new Card(Rank.THREE,CardColor.Orange).drawOn(g, boundingBox);
			return;
		}
		//draw normal card
		card.drawOn(g, boundingBox);
		return;
	}
	
	
	
	
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getTopView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveInfo(GameInfo info) {
		// TODO Auto-generated method stub
		
	}

}
