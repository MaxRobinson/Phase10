package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
import android.graphics.Color;

/**
 * this is the primary activity for Slapjack game
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class PhaseMainActivity extends GameMainActivity {
	
	public static final int PORT_NUMBER = 4752;

	/** a slapjack game for two players. The default is human vs. computer */
	@Override
	public GameConfig createDefaultConfig() {

		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		playerTypes.add(new GamePlayerType("human player (green)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseHumanPlayer(name, Color.GREEN);
			}});
		playerTypes.add(new GamePlayerType("human player (yellow)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseHumanPlayer(name, Color.YELLOW);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (normal)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseComputerPlayer(name);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (fast)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseComputerPlayer(name, 0.3);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (slow)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseComputerPlayer(name, 1.0);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (very fast)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseComputerPlayer(name, 0.15);
			}
		});
		playerTypes.add(new GamePlayerType("computer player (very slow)") {
			public GamePlayer createPlayer(String name) {
				return new PhaseComputerPlayer(name, 3.5);
			}
		});

		// Create a game configuration class for SlapJack
		GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "SlapJack", PORT_NUMBER);

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Computer", 2);
		
		// Set the initial information for the remote player
		defaultConfig.setRemoteData("Guest", "", 1);
		
		//done!
		return defaultConfig;
	}//createDefaultConfig

	@Override
	public LocalGame createLocalGame() {
		return new PhaseLocalGame();
	}
}
