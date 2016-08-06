package messages;

import interfaces.ClientMessage;
import interfaces.ServerMessage;
import server.Server;
import creep.CreepSelection;

/**
 * Notifies the server to spawn a creep for a player.
 * @author Travis
 *
 */
public class RequestCreepSpawnMessage implements ServerMessage {
	private static final long serialVersionUID = 1244324;
	private CreepSelection creep;
	private int creepLevel;
	private int playerId;
	private int creepPath;
	
	/**
	 * Notifies server to build a creep.
	 * @param datCreep
	 * @param level
	 * @param teamId
	 */
	public RequestCreepSpawnMessage(
			CreepSelection datCreep, 
			int level,
			int teamId,
			int creepPath) {
		creep = datCreep;
		playerId = teamId;
		creepLevel = level;
		this.creepPath = creepPath;
	}

	@Override
	public ClientMessage create(Server server) {
		return new SpawnCreepMessage(creep, creepLevel, playerId, creepPath);
	}
}
