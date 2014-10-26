package edu.up.cs301.card;

import java.util.ArrayList;

/**
 * Suit the suit of a playing card (e.g., Club).
 * 
 * @author Steven R. Vegdahl 
 * @version July 2013 
 */
public enum Suit {
	
	// club
	Club {
//		// the "long name
//		@Override
//		public String longName() {
//			return "Club";
//		}
	},
	
	// diamond
	Diamond {
//		@Override
//		public String longName() {
//			return "Diamond";
//		}
	},
	
	// heart
	Heart {
//		@Override
//		public String longName() {
//			return "Heart";
//		}
	},
	
	// spade
	Spade {
//		@Override
//		public String longName() {
//			return "Spade";
//		}
	};

	/**
	 * the "short" (one-character) name that represents the suit
	 * 
	 * @return
	 * 		the suit's short name
	 */
	public char shortName() { return longName().charAt(0); }
	
	/**
	 * the "long" (full-word) name that represents the suit
	 * 
	 * @return
	 * 		the suit's long name
	 */
	public String longName() {
		return toString();
	}

	// an array to help us convert from character to suit; it will be initialized
	// the first time the fromChar method is called
	private static ArrayList<Character> suitChars = null;
	
	/**
	 * convert a character into a suit
	 * 
	 * @param c
	 * 		the character
	 * @return
	 * 		the suit that corresponds to that character, or null if the character
	 * 		does not denote a suit
	 */
	public static Suit fromChar(char c) {
		// if the helper-array is null, create it
		if (suitChars == null) {
			initSuitChars();
		}
		// find the numeric rank of the card denoted by the character
		int idx = suitChars.indexOf(Character.toLowerCase(c));
		// return null if the character did not denote a rank
		if (idx < 0) return null;
		// return the corresponding rank
		return Suit.values()[idx];
	}
	
	/**
	 * method that initializes the helper-array for use by the 'fromChar' method
	 */	
	private static void initSuitChars() {
		
		// the list of suits, in numeric order
		Suit[] vals = Suit.values();
		
		// create the list of characters
		suitChars = new ArrayList<Character>();
		
		// initialize the list with the characters, in suit-order
		for (Suit s : vals) {
			suitChars.add(Character.toLowerCase(s.shortName()));
		}
	}
}
