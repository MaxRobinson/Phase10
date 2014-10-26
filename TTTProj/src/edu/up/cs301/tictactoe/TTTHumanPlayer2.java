package edu.up.cs301.tictactoe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;


/**
 * A human (i.e., GUI) version of a tic-tac-toe player.  This is identical to
 * TTTHumanPlayer1, except that the board is upside-down, and the coloring is
 * red-on-yellow.
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013
 */
public class TTTHumanPlayer2 extends TTTHumanPlayer1
{
    /**
     * TTTHumanPlayer constructor.
     * 
     * @param name
     * 		the player's name
     */
    public TTTHumanPlayer2(String name) {
        // invoke the superclass constructor
        super(name);
    }

	/**
	 * @return
	 * 		the animation's background color
	 */
    @Override
	public int backgroundColor() {
		return Color.YELLOW;
	}

	/**
	 * @return
	 * 		the color to paint the tic-tac-toe lines, and the X's and O's
	 */
    @Override
	public int foregroundColor() {
		return Color.RED;
	}
    
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
    @Override
	protected void drawSymbol(Canvas g, char sym, int col, int row) {
    	// use the superclass method, but invert the rows and columns so
    	// that the squares are upside-down
    	super.drawSymbol(g, sym, 2-col, 2-row);
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
    @Override
	public Point mapPixelToSquare(int x, int y) {
    	// user the superclass method, but invert the result because our
    	// view is upside-down
    	Point p = super.mapPixelToSquare(x, y);
    	if (p == null) {
    		return null;
    	}
    	else {
    		return new Point(2-p.x, 2-p.y);
    	}
    }
}
