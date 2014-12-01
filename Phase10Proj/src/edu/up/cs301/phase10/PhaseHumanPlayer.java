package edu.up.cs301.phase10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
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
import edu.up.cs301.phase10.PhaseState;
import edu.up.cs301.game.*;
/**
 * A GUI that allows a human to play Phase 10. Moves are made by clicking
 * regions on a surface. It is laid out for landscape orientation.
 * 
 * @author Steven R. Vegdahl 
 * @author Justice R W Nichols
 * @author Max Robinson 
 * @author Dan Nelson
 * @version July 2013
 */
public class PhaseHumanPlayer extends GameHumanPlayer implements Animator {
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
	@SuppressWarnings("unused")
	private int screenWidth;

	//the screen Height
	@SuppressWarnings("unused")
	private int screenHeight;

	//The rectangles for hit selection
	private RectF handLocation;

	private RectF drawPileLocation;

	private RectF discardPileLocation;

	private RectF opponentPhaseLocations;

	//private RectF phaseLocation;
	private RectF phaseTextLocation;

	private RectF phaseButtonLocation;

	private RectF hitButtonLocation;

	private boolean hitting;
	private boolean laying;
	private boolean[] selected;

	// Variables for dialogs
	private Spinner numberSpinner = null;
	private Spinner colorSpinner = null;
	private Spinner skipSpinner = null;
	private int skippedId = -1;
	private RadioGroup topBottomRadioGroup = null;
	private int topBottom = -1;
	private int it = 0;
	
	public PhaseHumanPlayer(String name) {
		super(name);
		laying = false;
		hitting = false;
		selected = new boolean[11];
		resetSelected();
	}

	private void resetSelected()
	{
		for(int i = 0; i < selected.length; i++)
		{
			selected[i] = false;
		}
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
		opponentPhaseLocations = new RectF(50f,0f,1200f,125f);
		phaseTextLocation = new RectF(500f,300f,650f,350f);
		phaseButtonLocation = new RectF(800f,300f,950f,350f);
		hitButtonLocation = new RectF(800f,360f,950f,410f);
		laying = false;
		hitting = false;
	}

	public int interval() {
		return 50;
	}

	public int backgroundColor() {
		return backgroundColor;
	}

	public boolean doPause() {
		return false;
	}

	public boolean doQuit() {
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
		if(state.getDiscardPile().peekAtTopCard()!=null)
		{
			drawCard(canvas,discardPileLocation,state.getDiscardPile().peekAtTopCard());
		}
		else
		{
			Paint paint = new Paint();
			paint.setARGB(150,255,255,0);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(5);
			canvas.drawRect(discardPileLocation,paint);
		}
		//draw opponents phases
		drawOpponentsPhases(canvas,opponentPhaseLocations,state.getLaidPhases());
		//draw currentPhase
		writeCurrentPhase(canvas, phaseTextLocation );
		//draw points
		writePoints(canvas, phaseTextLocation);
		//draw phaseButton
		drawLayPhaseButton(canvas, phaseButtonLocation, laying);
		//draw hitButton
		drawHitButton(canvas, hitButtonLocation, hitting);
		//highlight selected cards
		highlight(canvas,handLocation);


	}

	private void highlight(Canvas g, RectF r)
	{
		float hHeight = r.height();
		float cardWidth = 2f*hHeight/3f;
		float hL = r.left;
		float hT = r.top;
		float hB = r.bottom;
		for(int i = 0; i < selected.length; i++)
		{
			if(selected[i])
			{
				Paint paint = new Paint();
				paint.setARGB(150,255,255,0);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(5);
				RectF cardLocation = new RectF(hL+(cardWidth)*i,hT,hL+(cardWidth)*(i+1),hB);
				g.drawRect(cardLocation,paint);
			}
		}
	}

	private void writeCurrentPhase(Canvas g, RectF phaseTextLoc) 
	{
		Paint paint = new Paint();
		paint.setTextSize(25f);
		paint.setColor(Color.BLACK);
		g.drawText("Your Current Phase is: " + state.getCurrentPhase()[this.playerNum],phaseTextLoc.left,phaseTextLoc.top, paint);
		g.drawText("You Need:   " + Phase.phases[this.playerNum],phaseTextLoc.left,phaseTextLoc.top+80f, paint);


	}
	private void writePoints(Canvas g, RectF phaseTextLoc) 
	{
		Paint paint = new Paint();
		paint.setTextSize(25f);
		paint.setColor(Color.BLACK);
		g.drawText("Your Points:                    " + state.getScore()[this.playerNum],phaseTextLoc.left,phaseTextLoc.top+40f, paint);
		g.drawText("It is player " + state.getTurn()+ "'s turn",phaseTextLoc.left,phaseTextLoc.top+120f, paint);
		g.drawText("You're player " + this.playerNum ,phaseTextLoc.left,phaseTextLoc.top+160f, paint);



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


	private void drawOpponentsPhases(Canvas g, RectF opponentPhaseLocations2, Phase[] phases)
	{
		float cardheight = opponentPhaseLocations2.height()/2f;
		float phasewidth = opponentPhaseLocations2.width()/phases.length;
		float sL = opponentPhaseLocations2.left;
		float sT = opponentPhaseLocations2.top;

		//Paint paint = new Paint();
		//paint.setColor(Color.BLUE);
		//g.drawRect(opponentPhaseLocations2, paint);

		for(int i = 0; i < phases.length; i++)
		{
			if(state.getLaidPhases()[i] != null)
			{
				Hand phase1 = state.getLaidPhases()[i].getPhasePart()[0];
				if(phase1 != null)
				{
					drawPlayerHand(g,phase1,new RectF(sL+phasewidth*i,sT,sL+phasewidth*(i+1),sT+cardheight));
					Hand phase2 = state.getLaidPhases()[i].getPhasePart()[1];
					if(phase2 != null)
					{
						drawPlayerHand(g,phase2,new RectF(sL+phasewidth*i,sT+cardheight,sL+phasewidth*(i+1),sT+cardheight+cardheight));
					}
				}
			}
			drawPlayerName(g,opponentPhaseLocations2,i,phasewidth);
		}
	}

	private void drawPlayerName(Canvas g, RectF opponentPhaseLoc, int i, float width) 
	{
		Paint paint = new Paint();
		paint.setTextSize(25f);
		paint.setColor(Color.BLACK);
		float x = opponentPhaseLoc.left+width*i; 
		float y = opponentPhaseLoc.bottom+27;
		g.drawText("Phase: " + state.getCurrentPhase()[this.playerNum],x,y, paint);
	}

	private void drawLayPhaseButton(Canvas g, RectF loc, boolean pressed)
	{
		Paint paint = new Paint();
		if(!pressed)
		{	
			paint.setColor(Color.CYAN);
		}
		else
		{
			paint.setColor(Color.RED);
		}
		g.drawRect(loc, paint);
		paint.setTextSize(25f);
		paint.setColor(Color.BLACK);
		g.drawText("PHASE",loc.left+50f,loc.top+30f, paint);
	}
	private void drawHitButton(Canvas g, RectF loc, boolean pressed)
	{
		Paint paint = new Paint();
		if(!pressed)
		{	
			paint.setColor(Color.GREEN);
		}
		else
		{
			paint.setColor(Color.RED);
		}
		g.drawRect(loc, paint);
		paint.setTextSize(25f);
		paint.setColor(Color.BLACK);
		g.drawText("HIT",loc.left+50,loc.top+30f, paint);
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

		if(handLocation.contains(x,y))
		{
			int cardLoc = (int) ((x-handLocation.left)/(100f)); 
			if(cardLoc < state.getHands()[this.playerNum].size())
			{
				if(laying)
				{
					selected[cardLoc] = !selected[cardLoc];
				}
				else 
				{
					if(hitting)
					{
						selected[cardLoc] = !selected[cardLoc];
					}
					else
					{
						int anySelected = -1;
						for(int i= 0; i < selected.length; i++)
						{
							if(selected[i])
							{
								anySelected = i;
								break;
							}
						}
						if(anySelected != -1)
						{
							state.swap(this.playerNum,anySelected,cardLoc);
							resetSelected();
						}
						else
						{
							selected[cardLoc] = true;
						}
					}
				}
			}
		}
		else { if(drawPileLocation.contains(x,y))
		{
			game.sendAction(new PhaseDrawCardAction(this,true));
			resetSelected();

		}
		else{ if(discardPileLocation.contains(x, y))
		{
			if (state.getHasDrawn())
			{
				int anySelected = -1;
				for(int i= 0; i < selected.length; i++)
				{
					if(selected[i])
					{
						anySelected = i;
						break;
					}
				}
				if(anySelected != -1 && !hitting && !laying)
				{
					Card skipCard = new Card(Rank.TWO,CardColor.Orange);
					if (state.getHands()[this.playerNum].getCard(anySelected).equals(skipCard))
					{
						int toBeSkippedId = selectSkipped(this);
						//game.sendAction(new PhaseSkipAction(this, skipCard, toBeSkippedId));
					}
					else
					{
						game.sendAction(new PhaseDiscardAction(this, state.getHands()[this.playerNum].getCard(anySelected)));
					}
					resetSelected();
				}
			}
			else
			{
				game.sendAction(new PhaseDrawCardAction(this,false));
				resetSelected();

			}
		}
		else{ if (phaseButtonLocation.contains(x, y))
		{
			if(laying)
			{
				ArrayList<Card> cards = new ArrayList<Card>();
				// Count number of wildcards in players hand
				int numWildCards = 0;
				// Card to compare against for wild
				Card wildCard = new Card(Rank.ONE,CardColor.Orange);
				for(int i = 0; i < selected.length; i++)
				{
					if(selected[i])
					{
						// If the selected card is wild
						if (state.getHands()[this.playerNum].getCard(i).equals(wildCard)){
							numWildCards++;
						}
						// Not a wildcard
						else{
							cards.add(state.getHands()[this.playerNum].getCard(i));
						}
					}
				}
				// Store wildcards once they have been assigned a value

				if(numWildCards > 0){
					selectWildcard("lay",numWildCards,cards,this,-1,-1,-1);
				}
				else if(cards.size() > 0)
				{
					Phase tempPhase = new Phase(cards,null);
					game.sendAction(new PhaseLayPhaseAction(this, tempPhase));
				}
			}
			laying = !laying;
			hitting = false;
			resetSelected();
		}
		else{ if (hitButtonLocation.contains(x, y))
		{
			hitting = !hitting;
			laying = false;
			resetSelected();
		}
		else{ if(opponentPhaseLocations.contains(x,y))
		{
			if(hitting)
			{
				/*ArrayList<Card> cards = new ArrayList<Card>();
				for(int i = 0; i < selected.length; i++)
				{
					if(selected[i])
					{
						cards.add(state.getHands()[this.playerNum].getCard(i));
					}
				}*/
				// TODO need to make it so when the user is hitting, they can only select one card
				Card toLay = null;
				for(int i = 0; i < selected.length; i++)
				{
					if(selected[i])
					{
						toLay = state.getHands()[this.playerNum].getCard(i);
						break;
					}
				}

				if(toLay != null)
				{
					int idToLayOn = 0;
					int whichPart = 0;
					int topOrBottom = 0;
					float pL = opponentPhaseLocations.left;
					float pT = opponentPhaseLocations.top;
					float pW = opponentPhaseLocations.width();
					float pH = opponentPhaseLocations.height();
					if(pH / 2f + pT < y)
					{
						whichPart = 1;
					}
					idToLayOn = (int) ((x-pL)/(pW/state.getPlayers().length));
					// Card to compare against for wild
					Card wildCard = new Card(Rank.ONE,CardColor.Orange);
					// If the selected card is wild
					if (toLay.equals(wildCard)){
						ArrayList<Card> wildCards = new ArrayList<Card>();
						wildCards.add(toLay);
						selectWildcard("lay",1,null,this,idToLayOn,whichPart,topOrBottom);
					}
					else{
						Card c = toLay;
						topBottom(this,idToLayOn,whichPart,topOrBottom,c);
					}
				}
			}
		}
		}
		}
		}
		}
		}
		//			oppenentPhaseLocations 
		//			phaseTextLocation
		//			phaseButtonLocation
		//			hitButtonLocation
	}

	@Override
	public View getTopView() {
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
	
	public void selectWildcard (final String action, final int numWilds, final ArrayList<Card> cards, final GamePlayer player,
								final int idToLayOn, final int whichPart,final int topOrBottom){
		final ArrayList<Card> retCards = new ArrayList<Card>();
		// Allow users to select values for all wildcards in their hand
		for(it = 0; it < numWilds; it++){
			// Create a light themed AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(GameMainActivity.activity,AlertDialog.THEME_HOLO_LIGHT);
			// Configure layout of alert dialog
			LayoutInflater inflater = GameMainActivity.activity.getLayoutInflater();
			View view = inflater.inflate(R.layout.wildcard_view, null);
			// Set layout and titles
			builder.setTitle("Wildcard Number and Color Selection")
			.setMessage("Use dropdowns to select the number and color of the wildcard!")
			.setView(view).setCancelable(false);
			// Set up OK button to update values
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Rank r = Rank.valueOf(numberSpinner.getSelectedItem().toString().toUpperCase(Locale.ENGLISH));
					CardColor col = CardColor.valueOf(colorSpinner.getSelectedItem().toString());
					Card c = new Card(r,col);
					retCards.add(c);
					if(it == numWilds){
						if(action.equals("lay")){
							cards.addAll(retCards);
							Phase tempPhase = new Phase(cards,null);
							game.sendAction(new PhaseLayPhaseAction(player, tempPhase));
						}
						else{
							topBottom(player,idToLayOn,whichPart,topOrBottom,c);
						}
					}
				}
			});

			// Create new alert dialog from builder
			AlertDialog dialog = builder.create();

			/* Number Spinner */
			TextView numberTextView = (TextView)view.findViewById(R.id.numberTextField);
			numberTextView.setText("Select the number for the card");
			// Get spinner
			numberSpinner = (Spinner) view.findViewById(R.id.numberSpinner);
			// Create list of possible number values
			List<String> numberList= new ArrayList<String>();
			numberList.add("One");
			numberList.add("Two");
			numberList.add("Three");
			numberList.add("Four");
			numberList.add("Five");
			numberList.add("Six");
			numberList.add("Seven");
			numberList.add("Eight");
			numberList.add("Nine");
			numberList.add("Ten");
			numberList.add("Eleven");
			numberList.add("Twelve");
			// Set spinner to use number values
			ArrayAdapter<String> numberDataAdapter = new ArrayAdapter<String> (GameMainActivity.activity, android.R.layout.simple_spinner_item, numberList);
			numberDataAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
			numberDataAdapter.notifyDataSetChanged();
			numberSpinner.setAdapter(numberDataAdapter);

			/* Color Spinner */
			TextView colorTextView = (TextView)view.findViewById(R.id.colorTextField);
			colorTextView.setText("Select the color for the card");
			// Get spinner
			colorSpinner = (Spinner) view.findViewById(R.id.colorSpinner);
			// Create list of possible number values
			List<String> colorList= new ArrayList<String>();
			colorList.add("Blue");
			colorList.add("Green");
			colorList.add("Red");
			colorList.add("Yellow");
			// Set spinner to use number values
			ArrayAdapter<String> colorDataAdapter = new ArrayAdapter<String> (GameMainActivity.activity, android.R.layout.simple_spinner_item, colorList);
			colorDataAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
			colorDataAdapter.notifyDataSetChanged();
			colorSpinner.setAdapter(colorDataAdapter);

			dialog.show();
		}
	}
	
	public int selectSkipped(final GamePlayer player){
		// Create a light themed AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(GameMainActivity.activity,AlertDialog.THEME_HOLO_LIGHT);
		// Configure layout of alert dialog
		LayoutInflater inflater = GameMainActivity.activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.skip_view, null);
		// Set layout and titles
		builder.setTitle("Select player to skip")
		.setMessage("Use dropdown to select the player to be skipped!")
		.setView(view).setCancelable(false);
		// Set up OK button to update values
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				skippedId = skipSpinner.getSelectedItemPosition();
				Card skipCard = new Card(Rank.TWO,CardColor.Orange);
				game.sendAction(new PhaseSkipAction(player, skipCard, skippedId));
			}
		});

		// Create new alert dialog from builder
		AlertDialog dialog = builder.create();

		/* Skipped Spinner */
		TextView skippedTextView = (TextView)view.findViewById(R.id.skipTextField);
		skippedTextView.setText("Select the player to be skipped");
		// Get spinner
		skipSpinner = (Spinner) view.findViewById(R.id.skipSpinner);
		// Create list of possible player values
		List<String> skipList= new ArrayList<String>();
		String players[] = PhaseLocalGame.getPLayerNames();
		skipList = Arrays.asList(players);
		// Set spinner to use player values
		ArrayAdapter<String> skipDataAdapter = new ArrayAdapter<String> (GameMainActivity.activity, android.R.layout.simple_spinner_item, skipList);
		skipDataAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
		skipSpinner.setAdapter(skipDataAdapter);

		dialog.show();
		
		return skippedId;
	}
	
	public int topBottom(final GamePlayer player, final int idToLayOn,
			  			 final int whichPart,final int topOrBottom, final Card c){
		// Create a light themed AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(GameMainActivity.activity,AlertDialog.THEME_HOLO_LIGHT);
		// Configure layout of alert dialog
		LayoutInflater inflater = GameMainActivity.activity.getLayoutInflater();
		final View view = inflater.inflate(R.layout.top_bottom_view, null);
		// Set layout and titles
		builder.setTitle("Select top of bottom of phase")
		.setMessage("Use radio buttons to select if card should be on top of bottom of laid phase!")
		.setView(view).setCancelable(false);
		// Set up OK button to update values
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				int selectedId = topBottomRadioGroup.getCheckedRadioButtonId();
				RadioButton button = (RadioButton)view.findViewById(selectedId);
				int topBot = (button.getText().equals("Top") ? 1 :0);
				game.sendAction(new PhaseLayOnPhaseAction(player,c,idToLayOn,whichPart,topBot));
			}
		});

		// Create new alert dialog from builder
		AlertDialog dialog = builder.create();

		// Get Radio Group
		topBottomRadioGroup = (RadioGroup) view.findViewById(R.id.topBottomRadioGroup);

		dialog.show();
		
		return topBottom;
	}
}
