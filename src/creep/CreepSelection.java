package creep;

import model.Creep;
import model.Path;
import model.Player;

/**
 * Used by the CreepSpawningMessages to denote what creep is being spawned.
 * @author Travis
 *
 */
public enum CreepSelection {
	NullCreep, Influenza, Salmonella, Strep, Zoidberg;


	/**
	 * Static method to create creeps. When creeps are added, add their constructor here.
	 * @param type
	 * @param thePath
	 * @param enemy
	 * @param level
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public static Creep getCreep(
			CreepSelection creepType,	// a creep type in CreepSelection's enum
			Path thePath,				// path to travel on
			Player enemy,				// the enemy player
			int level,					// creep's level
			int id) throws Exception {					// creep's team
		switch(creepType) {
		case NullCreep:
			return null;
		case Influenza:
			return new Influenza(thePath, enemy, level, id);
		case Salmonella:
			return new Salmonella(thePath, enemy, level, id);
		case Strep:
			return new Strep(thePath, enemy, level, id);
		case Zoidberg:
			return new Zoidberg(thePath, enemy, level, null, id);
		default:
			throw new Exception("creepType was invalid");
		}
	}
}

