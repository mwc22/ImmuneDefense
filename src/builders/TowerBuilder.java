package builders;

import interfaces.TowerDefenseModel;

import java.awt.Point;

import server.Server;

/**
 * Places towers on the game model and sends tower built message to the server.
 * @author Travis
 *
 */
public abstract class TowerBuilder {
	
	// vars
	protected TowerDefenseModel gameRef;
	
	public TowerBuilder(TowerDefenseModel game) {
		gameRef = game;
	}
	
	/**
	 * Builds the implementing class's tower of choice at point p.
	 * @param p
	 * @return 
	 */
	public abstract boolean buildTower(Point p);
}
