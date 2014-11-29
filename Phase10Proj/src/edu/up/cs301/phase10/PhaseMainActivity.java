package edu.up.cs301.phase10;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;
import edu.up.cs301.phase10.PhaseHumanPlayer;

/**
 * this is the primary activity for Phase10 game
 * 
 * @author Max Robinson
 * @version 11/11/2014
 */
public class PhaseMainActivity extends GameMainActivity{
	
	public static final int PORT_NUMBER = 4752;
	
	/** a Phase 10 game for multiple players. The default is human vs. computer */
	@Override
	public GameConfig createDefaultConfig() {
		
		// Define the allowed player types
		ArrayList<GamePlayerType>  playerTypes = new ArrayList<GamePlayerType>();
		
		playerTypes.add(new GamePlayerType("human player") {
			public GamePlayer createPlayer(String name) {
				return new PhaseHumanPlayer(name);
			}});
//		playerTypes.add(new GamePlayerType("computer player (normal)") {
//			public GamePlayer createPlayer(String name) {
//				return new PhaseComputerPlayer(name);
//			}
//		});
		playerTypes.add(new GamePlayerType("Computer Player (PASSIVE BOTS)")
		{
			public GamePlayer createPlayer(String name)
			{
				return new PhaseComputerPlayer0(name);
			}
		});
		
		// Create a game configuration class for Phase10
		GameConfig defaultConfig = new GameConfig(playerTypes, 2, 6, "Phase10", PORT_NUMBER);
		
		// Add the defualt players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Computer", 1);
		
		// Set the initial informatino for the remote player
		defaultConfig.setRemoteData("Guest", "",0);
		
		// Done!
		return defaultConfig;
	}//createDefaultConfig

	@Override
	public LocalGame createLocalGame() {
		// TODO Auto-generated method stub
		return new PhaseLocalGame();
	}

}
