package edu.up.cs301.tictactoe;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * A GUI that allows a human to play tic-tac-toe. Moves are made by clicking
 * regions on a canvas
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class TTTHumanPlayer1 extends TTTHumanPlayer implements Animator {

	// some constants, which are percentages with respect to the minimum
	// of the height and the width. All drawing will be done in the "middle
	// square".
	//
	// The divisions both horizontally and vertically within the
	// playing square are:
	// - first square starts at 5% and goes to 33%
	// - second square starts at 36% and goes to 64%
	// - third square starts at 67& and goes to 95%
	// There is therefore a 5% border around the edges; each square
	// is 28% high/wide, and the lines between squares are 3%
	private final static float BORDER_PERCENT = 5; // size of the border
	private final static float SQUARE_SIZE_PERCENT = 28; // size of each of our 9 squares
	private final static float LINE_WIDTH_PERCENT = 3; // width of a tic-tac-toe line
	private final static float SQUARE_DELTA_PERCENT = SQUARE_SIZE_PERCENT
			+ LINE_WIDTH_PERCENT; // distance from left (or top) edge of square to the next one

	/*
	 * Instance variables
	 */

	// the game's state
	protected TTTState state;

	// the current activity
	private Activity myActivity;

	// the offset from the left and top to the beginning of our "middle square"; one
	// of these will always be zero
	protected float hBase;
	protected float vBase;

	// the size of one edge of our "middle square", or -1 if we have not determined
	// size
	protected float fullSquare;

	// our animation surface. (We're not actually doing moving animation, but this
	// surface is a convenient way to draw our image.)
	private AnimationSurface surface;

	/**
	 * constructor
	 * 
	 * @param name
	 * 		the player's name
	 */
	public TTTHumanPlayer1(String name) {
		super(name);
	}

	/**
	 * Callback method, called when player gets a message
	 * 
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
			// if the move was out of turn or otherwise illegal, flash the screen
			surface.flash(Color.RED, 50);
		}
		else if (!(info instanceof TTTState))
			// if we do not have a TTTState, ignore
			return;
		else {
			// update our 'state' variable with the new state
			this.state = (TTTState) info;
			Log.i("human player", "receiving");
		}
	}

	/**
	 * sets the current player as the activity's GUI
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember our activitiy
		myActivity = activity;

		// Load the layout resource for the new configuration
		activity.setContentView(R.layout.ttt_human_player1and2);

		// set the animator (us) for the animation surface
		surface = (AnimationSurface) myActivity
				.findViewById(R.id.padding8Bot);
		surface.setAnimator(this);

		// "erase" the previous GUI's notion of what the screen size is
		fullSquare = -1;

		// if we have state, "simulate" that it just came from the game, and
		// hence draw it on the GUI
		if (state != null) {
			receiveInfo(state);
		}
	}

	/**
	 * returns the GUI's top view
	 * 
	 * @return
	 * 		the GUI's top view
	 */
	@Override
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	/** 
	 * @return
	 * 		the time interval for the animation
	 */
	public int interval() {
		// 50 milliseconds, or 20 times per second
		return 50;
	}

	/**
	 * @return
	 * 		the animation's background color
	 */
	public int backgroundColor() {
		return Color.BLUE;
	}

	/**
	 * @return
	 * 		whether the animation should be pause
	 */
	public boolean doPause() {
		// never tell the animation to pause
		return false;
	}

	/**
	 * @return
	 * 		whether the animation should quit
	 */
	public boolean doQuit() {
		// never tell the animation to quit
		return false;
	}

	/**
	 * perform any initialization that needs to be done after the player
	 * knows what their game-position and opponents' names are.
	 */
	protected void initAfterReady() {
		myActivity.setTitle("Tic-Tac-Toe: "+allPlayerNames[0]+" vs. "+allPlayerNames[1]);
	}

	/**
	 * callback method, called whenever it's time to draw the next animatio
	 * frame
	 * 
	 * @param g
	 * 		the canvas to draw on
	 */
	public void tick(Canvas g) {

		// if the full square size is outdated our variables that relate
		// to the dimensions of the animation surface
		if (fullSquare < 0) {
			updateDimensions(g);
		}

		// paint the TTT-board's horizontal and vertical lines
		Paint p = new Paint();
		p.setColor(foregroundColor());
		for (int i = 0; i <= 1; i++) {
			float variable1 = BORDER_PERCENT + SQUARE_SIZE_PERCENT
					+ (i * SQUARE_DELTA_PERCENT);
			float variable2 = variable1 + LINE_WIDTH_PERCENT;
			float fixed1 = BORDER_PERCENT;
			float fixed2 = 100 - BORDER_PERCENT;
			g.drawRect(h(variable1), v(fixed1), h(variable2), v(fixed2), p);
			g.drawRect(h(fixed1), v(variable1), h(fixed2), v(variable2), p);
		}

		// if we don't have any state, there's nothing more to draw, so return
		if (state == null) {
			return;
		}

		// for each square that has an X or O, draw it on the appropriate
		// place on the canvas
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				char result = state.getPiece(row, col); // get piece
				drawSymbol(g, result, col, row);
			}
		}
	}

	/**
	 * helper-method to convert from a percentage to a horizontal pixel location
	 * 
	 * @param percent
	 * 		the percentage across the drawing square
	 * @return
	 * 		the pixel location that corresponds to that percentage
	 */
	protected float h(float percent) {
		return hBase + percent * fullSquare / 100;
	}

	/**
	 * helper-method to convert from a percentage to a vertical pixel location
	 * 
	 * @param percent
	 * 		the percentage down the drawing square
	 * @return
	 * 		the pixel location that corresponds to that percentage
	 */
	protected float v(float percent) {
		return vBase + percent * fullSquare / 100;
	}

	/**
	 * update the instance variables that relate to the drawing surface
	 * 
	 * @param g
	 * 		an object that references the drawing surface
	 */
	private void updateDimensions(Canvas g) {

		// initially, set the height and width to be that of the
		// drawing surface
		int width = g.getWidth();
		int height = g.getHeight();
		
		// Set the "full square" size to be the minimum of the height and
		// the width. Depending on which is greater, set either the
		// horizontal or vertical base to be partway across the screen,
		// so that the "playing square" is in the middle of the screen on
		// its long dimension
		if (width > height) {
			fullSquare = height;
			vBase = 0;
			hBase = (width - height) / (float) 2.0;
		} else {
			fullSquare = width;
			hBase = 0;
			vBase = (height - width) / (float) 2.0;
		}

	}

	/**
	 * @return
	 * 		the color to paint the tic-tac-toe lines, and the X's and O's
	 */
	public int foregroundColor() {
		return Color.YELLOW;
	}

	/**
	 * callback method when the screen it touched. We're
	 * looking for a screen touch (which we'll detect on
	 * the "up" movement" onto a tic-tac-tie square
	 * 
	 * @param event
	 * 		the motion event that was detected
	 */
	public void onTouch(MotionEvent event) {
		
		// ignore if not an "up" event
		if (event.getAction() != MotionEvent.ACTION_UP) return;

		// get the x and y coordinates of the touch-location;
		// convert them to square coordinates (where both
		// values are in the range 0..2)
		int x = (int) event.getX();
		int y = (int) event.getY();
		Point p = mapPixelToSquare(x, y);
		
		// if the location did not map to a legal square, flash
		// the screen; otherwise, create and send an action to
		// the game
		if (p == null) {
			surface.flash(Color.RED, 50);
		} else {
			TTTMoveAction action = new TTTMoveAction(this, p.y, p.x);
			Log.i("onTouch", "Human player sending TTTMA ...");
			game.sendAction(action);
		}

	}

	// x- and y-percentage-coordinates for a polygon that displays the X's
	// first slash
	private static float[] xPoints1 = { 6.25f, 12.5f, 87.5f, 93.75f };
	private static float[] yPoints1 = { 12.5f, 6.25f, 93.75f, 87.5f };

	// x- and y-percentage-coordinates for a polygon that displays the X's
	// second slash
	private static float[] xPoints2 = { 87.5f, 6.25f, 93.75f, 12.5f };
	private static float[] yPoints2 = { 6.25f, 87.5f, 12.5f, 93.75f };

	/**
	 * Draw a symbol (X or O) on the canvas in a particular location
	 * 
	 * @param g
	 *            the graphics object on which to draw
	 * @param sym
	 *            the symbol to draw (X or O)
	 * @param col
	 *            the column number of the square on which to draw (0, 1 or 2)
	 * @param col
	 *            the row number of the square on which to draw (0, 1 or 2)
	 */
	protected void drawSymbol(Canvas g, char sym, int col, int row) {

		// compute the pixel-location
		float xLoc = BORDER_PERCENT + col * SQUARE_DELTA_PERCENT; // compute ...
		float yLoc = BORDER_PERCENT + row * SQUARE_DELTA_PERCENT; // ... location

		// set the paint color to be the foreground color
		Paint p = new Paint();
		p.setColor(foregroundColor());

		// draw either an X or O, depending on the symbol
		switch (sym) {
		case 'O':
			// 'O' found: draw it by drawing two circles: an outer one with the
			// foreground color, and an inner one with the background color
			RectF rect = new RectF(h(xLoc + 5), v(yLoc + 1), h(xLoc
					+ SQUARE_SIZE_PERCENT - 5), v(yLoc + SQUARE_SIZE_PERCENT
					- 1));
			g.drawOval(rect, p); // outside of the 'O'
			p.setColor(backgroundColor());
			rect = new RectF(h(xLoc + 6), v(yLoc + 2), h(xLoc
					+ SQUARE_SIZE_PERCENT - 8), v(yLoc + SQUARE_SIZE_PERCENT
					- 3));
			g.drawOval(rect, p); // carve out "hole"
			break;
		case 'X': // 'X' found: draw it
			
			// create a translation matrix to move Path to the given square on the
			// surface
			Matrix translateMatrix = new Matrix();
			translateMatrix.setTranslate(h(xLoc), v(yLoc));

			// create the Path object for the X's first slash; move and draw it
			Path pth = createPoly(xPoints1, yPoints1, fullSquare
					* SQUARE_SIZE_PERCENT / 100);
			pth.transform(translateMatrix);
			g.drawPath(pth, p);
			
			// create the Path object for the X's second slash; move and draw it
			pth = createPoly(xPoints2, yPoints2, fullSquare
					* SQUARE_SIZE_PERCENT / 100);
			pth.transform(translateMatrix);
			g.drawPath(pth, p);
			break;
		default:
			// if not X or O, draw nothing
			break;
		}
	}

	/**
	 * helper-method to create a scaled polygon (Path) object from a list of points
	 * 
	 * @param xPoints
	 * 		list of x-coordinates, taken as percentages
	 * @param yPoints
	 * 		corresponding list of y-coordinates--should have the same length as xPoints
	 * @param scale
	 * 		factor by which to scale them
	 * @return
	 */
	private Path createPoly(float[] xPoints, float[] yPoints, float scale) {
		
		// in case array-lengths are different, take the minimim length, to avoid
		// array-out-of-bounds errors
		int count = Math.min(xPoints.length, yPoints.length);
		
		// run through the points, adding each to the Path object, scaling as we go
		Path rtnVal = new Path();
		rtnVal.moveTo(xPoints[0] * scale / 100, yPoints[0] * scale / 100);
		for (int i = 1; i < count; i++) {
			float xPoint = xPoints[i] * scale / 100;
			float yPoint = yPoints[i] * scale / 100;
			rtnVal.lineTo(xPoint, yPoint);
		}
		
		// close the Path into a polygon; return the object
		rtnVal.close();
		return rtnVal;
	}

	/**
	 * maps a point from the canvas' pixel coordinates to "square" coordinates
	 * 
	 * @param x
	 * 		the x pixel-coordinate
	 * @param y
	 * 		the y pixel-coordinate
	 * @return
	 *		a Point whose components are in the range 0-2, indicating the
	 *		column and row of the corresponding square on the tic-tac-toe
	 * 		board, or null if the point does not correspond to a square
	 */
	public Point mapPixelToSquare(int x, int y) {

		// loop through each square and see if we get a "hit"; if so, return
		// the corresponding Point in "square" coordinates
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				float left = h(BORDER_PERCENT + (i * SQUARE_DELTA_PERCENT));
				float right = h(BORDER_PERCENT + SQUARE_SIZE_PERCENT
						+ (i * SQUARE_DELTA_PERCENT));
				float top = v(BORDER_PERCENT + (j * SQUARE_DELTA_PERCENT));
				float bottom = v(BORDER_PERCENT + SQUARE_SIZE_PERCENT
						+ (j * SQUARE_DELTA_PERCENT));
				if ((x > left) != (x > right) && (y > top) != (y > bottom)) {
					return new Point(i, j);
				}
			}
		}

		// no match: return null
		return null;
	}

}
