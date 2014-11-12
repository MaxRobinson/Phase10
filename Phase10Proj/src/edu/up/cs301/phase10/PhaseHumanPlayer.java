package edu.up.cs301.phase10;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;

public class PhaseHumanPlayer extends GameHumanPlayer implements Animator {
	
	// instance variables
	
	// Our game state
	protected PhaseState state;
	
	// our activity
	private Activity myActivity;
	
	//the animation surface
	private AnimationSurface surface;
	
	//the background color
	private int backgroundColor;
	
	
	
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

	public void tick(Canvas canvas) {
		// TODO Auto-generated method stub
		
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
