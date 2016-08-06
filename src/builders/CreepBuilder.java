package builders;

import model.MultiPlayer;
import model.MultiPlayer2;

/**
 * Spawns creeps to the TowerDefenseModel
 * @author Travis
 *
 */
public abstract class CreepBuilder {

	protected MultiPlayer2 gameRef;
	
	public CreepBuilder(MultiPlayer2 game) {
		gameRef = game;
	}
	
	public abstract void spawnCreep();
}
