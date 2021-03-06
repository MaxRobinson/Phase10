package edu.up.cs301.card;

import java.io.Serializable;

import edu.up.cs301.game.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * A playing card in the standard 52-card deck. The images, which have been
 * placed in the res/drawable-hdpi folder in the project, are from
 * http://www.pdclipart.org.
 * 
 * In order to display the card-images on the android you need to call the
 *   Card.initImages(currentActivity)
 * method during initialization; the 52 image files need to be placed in the
 * res/drawable-hdpi project area.
 * 
 * @author Dan Nelson
 * @author Steven R. Vegdahl
 * @version 11/20/14
 */
public class Card implements Serializable {

	// to satisfy the Serializable interface
	private static final long serialVersionUID = 893542931190030342L;
	
	// instance variables: the card's rank and the cardColor
    private Rank rank;
    private CardColor cardColor;

    /**
     * Constructor for class card
     *
     * @param r the Rank of the card
     * @param s the CardColor of the card
     */
    public Card(Rank r, CardColor s) {
        rank = r;
        cardColor = s;
    }

    /**
     * Creates a Card from a String.  (Can be used instead of the
     * constructor.)
     *
     * @param str
     * 		a two-character string representing the card, which is
     *		of the form "4C", with the first character representing the rank,
     *		and the second character representing the cardColor.  Each cardColor is
     *		denoted by its first letter.  Each single-digit rank is represented
     *		by its digit.  The letters 'T', 'J', 'Q', 'K' and 'A', represent
     *		the ranks Ten, Jack, Queen, King and Ace, respectively.
     * @return
     * 		A Card object that corresponds to the 'str' string. Returns
     *		null if 'str' has improper format.
     */
    public static Card fromString(String str) {
    	// check the string for being null
        if (str == null) return null;
        
        // trim the string; return null if length is not 2
        str = str.trim();
        if (str.length() !=2) return null;
        
        // get the rank and cardColor corresponding to the two characters
        // in the string
        Rank r = Rank.fromChar(str.charAt(0));
        CardColor s = CardColor.fromChar(str.charAt(1));
        
        // if both rank and cardColor are non-null, create the corresponding
        // card; if either is null, return null
        return r==null || s == null ? null : new Card(r, s);
    }

    /**
     * Produces a textual description of a Card.
     *
     * @return
	 *		A string such as "Blue 2", which describes the card.
     */
    public String toString() {
    	if(cardColor.longName().equals("Orange")) //if the card is 
    	{
    		return rank.longName() + " Card";
    	}
        return cardColor.longName() + " " + rank.longName();
    }

    /**
     * Tells whether two Card objects represent the same card.
     *
     * @return
	 *		true if the two card objects represent the same card, false
     *		otherwise.
     */
    public boolean equals(Card other) {
        return this.rank == other.rank && this.cardColor == other.cardColor;
    }

    public int getScoreValue(){
    	if(cardColor.longName().equals("Orange")){
    		// Wild
    		if(rank.equals("1")){
    			return 25;
    		}
    		// Skip
    		else if(rank.equals("2")){
    			return 15;
    		}
    	}
    	else if(rank.shortName() == '1'
    		|| rank.shortName() == '2'
    		|| rank.shortName() == '3'
    		|| rank.shortName() == '4'
    		|| rank.shortName() == '5'
    		|| rank.shortName() == '6'
    		|| rank.shortName() == '7'
    		|| rank.shortName() == '8'
    		|| rank.shortName() == '9'){
    			return 5;
    	}
    	else if(rank.shortName() == 'a'
        	|| rank.shortName() == 'b'
        	|| rank.shortName() == 'c'){
    		return 10;
    	}
    	return -1;
    }
    
    /**
     * Draws the card on a Graphics object.  The card is drawn as a
     * white card with a black border.  If the card's rank is numeric, the
     * appropriate number of spots is drawn.  Otherwise the appropriate
     * picture (e.g., of a queen) is included in the card's drawing.
     *
     * @param g  the graphics object on which to draw
     * @param where  a rectangle that tells where the card should be drawn
     */
    public void drawOn(Canvas g, RectF where) {
    	// create the paint object
    	Paint p = new Paint();
    	p.setColor(Color.BLACK);
    	
    	// get the bitmap for the card
    	Bitmap bitmap = cardImages[this.getCardColor().ordinal()][this.getRank().ordinal()];
    	
    	// create the source rectangle
    	Rect r = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
    	
    	// draw the bitmap into the target rectangle
    	g.drawBitmap(bitmap, r, where, p);
    }

    
    /**
     * Gives a two-character version of the card (e.g., "TS" for ten of
     * spades).
     */
    public String shortName() {
        return "" + getRank().shortName() + getCardColor().shortName();
    }

    /**
     * Tells the card's rank.
     *
     * @return
	 *		a Rank object (actually of a subclass) that tells the card's
     *		rank (e.g., Jack, three).
     */
    public Rank getRank() {
    	return rank;
    }
    
    /**
     * Tells the card's rank.
     *
     * @return
	 *		a Rank object (actually of a subclass) that tells the card's
     *		rank (e.g., Jack, three).
     */
    public int getIntRank() {
    	return getRank().value(1);
    }

    /**
     * Tells the card's cardColor.
     *
     * @return
	 *		a CardColor object (actually of a subclass) that tells the card's
     *		rank (e.g., heart, club).
     */
    public CardColor getCardColor() {
    	return cardColor;
    }
 
    // array that contains the android resource indices for the 52 card
    // images
    private static int[][] resIdx = {
    	{
    		R.drawable.card_blue_one, R.drawable.card_blue_two, R.drawable.card_blue_three,
    		R.drawable.card_blue_four, R.drawable.card_blue_five, R.drawable.card_blue_six,
    		R.drawable.card_blue_seven, R.drawable.card_blue_eight, R.drawable.card_blue_nine,
    		R.drawable.card_blue_ten, R.drawable.card_blue_eleven, R.drawable.card_blue_twelve, 
    	},
    	{
    		R.drawable.card_green_one, R.drawable.card_green_two, R.drawable.card_green_three,
    		R.drawable.card_green_four, R.drawable.card_green_five, R.drawable.card_green_six,
    		R.drawable.card_green_seven, R.drawable.card_green_eight, R.drawable.card_green_nine,
    		R.drawable.card_green_ten, R.drawable.card_green_eleven, R.drawable.card_green_twelve,
    	},
    	{
    		R.drawable.card_red_one, R.drawable.card_red_two, R.drawable.card_red_three,
    		R.drawable.card_red_four, R.drawable.card_red_five, R.drawable.card_red_six,
    		R.drawable.card_red_seven, R.drawable.card_red_eight, R.drawable.card_red_nine,
    		R.drawable.card_red_ten, R.drawable.card_red_eleven, R.drawable.card_red_twelve,
    	},
    	{
    		R.drawable.card_yellow_one, R.drawable.card_yellow_two, R.drawable.card_yellow_three,
    		R.drawable.card_yellow_four, R.drawable.card_yellow_five, R.drawable.card_yellow_six,
    		R.drawable.card_yellow_seven, R.drawable.card_yellow_eight, R.drawable.card_yellow_nine,
    		R.drawable.card_yellow_ten, R.drawable.card_yellow_eleven, R.drawable.card_yellow_twelve,
    	},
    	{
    		R.drawable.card_wild, R.drawable.card_skip, R.drawable.card_back,
    	},
    };
    
    // the array of card images
    private static Bitmap[][] cardImages = null;
    
    /**
     * initializes the card images
     * 
     * @param activity
     * 		the current activity
     */
    public static void initImages(Activity activity) {
    	// if it's already initialized, then ignore
    	if (cardImages != null) return;
    	
    	// create the outer array
    	cardImages = new Bitmap[resIdx.length][];
    	
    	// loop through the resource-index array, creating a
    	// "parallel" array with the images themselves
    	for (int i = 0; i < resIdx.length; i++) {
    		// create an inner array
    		cardImages[i] = new Bitmap[resIdx[i].length];
    		for (int j = 0; j < resIdx[i].length; j++) {
    			// create the bitmap from the corresponding image
    			// resource, and set the corresponding array element
    			cardImages[i][j] =
    					BitmapFactory.decodeResource(
    							activity.getResources(),
    							resIdx[i][j]);
    		}
    	}
    }

}
