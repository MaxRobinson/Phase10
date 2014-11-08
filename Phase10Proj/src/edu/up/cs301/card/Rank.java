package edu.up.cs301.card;

import java.util.ArrayList;

/**
 *
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public enum Rank {
	
	// ace
	ACE,
	
	// deuce
	TWO {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '2';
		}
	},

	// three
	THREE {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '3';
		}
	},

	// four
	FOUR {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '4';
		}
	},

	// five
	FIVE {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '5';
		}
	},

	// six
	SIX {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '6';
		}
	},

	// seven
	SEVEN {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '7';
		}
	},

	// eight
	EIGHT {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '8';
		}
	},
	
	// nine
	NINE {
		// override the shortName behavior: the corresponding digit
		@Override
		public char shortName() {
			return '9';
		}
	},
	
	// ten
	TEN,

	// jack
	JACK,

	// queen
	QUEEN,

	// king
	KING
	;

	/**
	 * the short (one-character) name of a rank
	 * 
	 * @return
	 * 		the rank's short name
	 */
	public char shortName() {
		// the default is the first character of the print-string;
		// TWO through NINE override this
		return this.toString().charAt(0);
	}
	
	/**
	 * the numeric value of the rank
	 * 
	 * @param aceValue
	 * 		the value to use for an Ace
	 * @return
	 * 		the numeric value of the rank: 2-13 for TWO through KING;
	 * 		the given value (typically 1 or 14) for ACE
	 */
	public int value(int aceValue) {
		return this == ACE ? aceValue : ordinal()+1;
	}
	
	/**
	 * the "long name" of the rank--fully spelled out
	 * 
	 * @return
	 * 		the rank's long name
	 */
	public String longName() {
		// use the print-string, with the first character upper case, and the
		// rest lower case
		String s = this.toString();
		return s.substring(0,1).toUpperCase()+s.substring(1).toLowerCase();
	}
	
	// an array to help us convert from character to rank; it will be initialized
	// the first time the fromChar method is called
	private static ArrayList<Character> rankChars = null;
	
	/**
	 * convert a character into a rank
	 * 
	 * @param c
	 * 		the character
	 * @return
	 * 		the rank that corresponds to that character, or null if the character
	 * 		does not denote a rank
	 */
	public static Rank fromChar(char c) {
		// if the helper-array is null, create it
		if (rankChars == null) {
			initRankChars();
		}
		// find the numeric rank of the card denoted by the character
		int idx = rankChars.indexOf(Character.toLowerCase(c));
		// return null if the character did not denote a rank
		if (idx < 0) return null;
		// return the corresponding rank
		return Rank.values()[idx];
	}
	
	/**
	 * method that initializes the helper-array for use by the 'fromChar' method
	 */
	private static void initRankChars() {
		// the list of ranks, in numeric order
		Rank[] vals = Rank.values();
		
		// create the list of characters
		rankChars = new ArrayList<Character>();
		
		// initialize the list with the characters, in rank-order
		for (Rank r : vals) {
			rankChars.add(Character.toLowerCase(r.shortName()));
		}
	}
}
