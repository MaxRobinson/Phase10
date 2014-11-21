package edu.up.cs301.phase10;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
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
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.phase10.Deck;
import edu.up.cs301.phase10.PhaseState;
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
	private int backgroundColor = Color.WHITE;
	
	//the screen width
	private int screenWidth;
	
	//the screen Height
	private int screenHeight;
	
	//The rectangles for hit selection
	private RectF handLocation;
	
	private RectF drawPileLocation;
	
	private RectF discardPileLocation;
	
	private RectF oppenentPhaseLocations;
	
	private RectF phaseLocation;
	

	
	public PhaseHumanPlayer(String name) {
		super(name);
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
		
		handLocation = new RectF(50f,500f,150f+105f*11,650f);
		drawPileLocation = new RectF(50f,300f,150f,450f);
		discardPileLocation = new RectF(150f,300f,250f,450f);
		oppenentPhaseLocations = new RectF(50f,50f,150f+105f*11,125f);
		
	}

	public int interval() {
		// TODO Auto-generated method stub
		return 50;
	}

	public int backgroundColor() {
		// TODO Auto-generated method stub
		return backgroundColor;
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
		if(state == null)
		{
			return;
		}
		Hand tempHand = state.getHands()[this.playerNum];
		//draw hand
		drawPlayerHand(canvas,tempHand,handLocation);
		//draw deck
		drawCard(canvas,drawPileLocation,null); 
		//draw discard
		drawCard(canvas,discardPileLocation,state.getDiscardPile().peekAtTopCard());
		//draw opponents phases
		drawOpponentsPhases(canvas,oppenentPhaseLocations,state.getLaidPhases());
		
	}

	//Draw player hand on screen used in tick
	private void drawPlayerHand(Canvas g, Hand tempHand, RectF location){
		//RectF cardLocation = new RectF(50.0f,4*screenHeight/5f,50.0f+50.0f,19*screenHeight/20f);
		
		//Log.w("draw", Integer.toString(screenWidth));


		float hHeight = location.height();
		float cardWidth = 2f*hHeight/3f;
		float hL = location.left;
		float hT = location.top;
		float hB = location.bottom;
		for(int i = 0; i < tempHand.size(); i++)
		{
			
			RectF cardLocation = new RectF(hL+(cardWidth)*i,hT,hL+(cardWidth)*(i+1),hB);
			drawCard(g, cardLocation, tempHand.getCard(i));
		}
		
	}
	
	
	private void drawOpponentsPhases(Canvas g, RectF oppenentPhaseLocations2, Phase[] phases)
	{
		for(int i = 0; i<phases.length; i++)
		{
			
		}
	}
	
	private void drawNumberCards(Canvas g, RectF totalBox, int numCards)
	{
		float width =  totalBox.width();
		 
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
	// ignore everything except down-touch events
			if (event.getAction() != MotionEvent.ACTION_DOWN) return;

			// get the location of the touch on the surface
			int x = (int) event.getX();
			int y = (int) event.getY();		
			
	}

	@Override
	public View getTopView() {
		// TODO Auto-generated method stub
		return myActivity.findViewById(R.id.top_gui_layout);	}

	@Override
	public void receiveInfo(GameInfo info) {
		Log.i("SJComputerPlayer", "receiving updated state ("+info.getClass()+")");
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if we had an out-of-turn or illegal move, flash the screen
			surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof PhaseState)) {
			// otherwise, if it's not a game-state message, ignore
			return;
		}
		else {
			// it's a game-state object: update the state. Since we have an animation
			// going, there is no need to explicitly display anything. That will happen
			// at the next animation-tick, which should occur within 1/20 of a second
			this.state = (PhaseState)info;
			Log.i("human player", "receiving");
		}
	}
}
