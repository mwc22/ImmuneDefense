package builders;

import gamemaps.Map1;
import gamemaps.Map2;
import gamemaps.TestMapMultiplayer1;
import interfaces.GameMap;


public class GameMapBuilder {

public enum GameMapSelector {
	TestMapMultiplayer1, Map1, Map2;
}
	
	public static GameMap getMap(GameMapSelector gameType) throws Exception {
		switch(gameType) {
		case TestMapMultiplayer1:
			return new TestMapMultiplayer1();
		case Map1:
			return new Map1();
		case Map2:
			return new Map2();
		default:
			throw new Exception("GameType was invalid.");
		}
	}
}
