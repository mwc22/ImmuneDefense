package messages;

import model.Creep;
import model.Player;
import server.UpdateScheme;
import interfaces.ClientMessage;
import interfaces.TowerDefenseModel;
import creep.CreepSelection;
import creep.Influenza;
import creep.Salmonella;
import creep.Strep;
import creep.Zoidberg;

public class SpawnCreepMessage implements ClientMessage {
	private static final long serialVersionUID = 43654356;
	private CreepSelection m_creepToBeSpawned;
	private int m_creepLevel;
	private int m_ownerId;
	private int derPath;

	public SpawnCreepMessage(CreepSelection datCreep, int level, int senderId, int derPath) {
		m_ownerId = senderId;
		m_creepToBeSpawned = datCreep;
		m_creepLevel = level;
		this.derPath = derPath;
	}

	@Override
	public void execute(TowerDefenseModel model) throws Exception {
		// make the creep		
		Creep theCreep = CreepSelection.getCreep(
				m_creepToBeSpawned,
				model.getMap().getPaths(derPath % model.getMap().getPaths().size()),
				model.getPlayer(), 
				m_creepLevel, 
				m_ownerId);

		model.getMap().addCreep(theCreep);
	}

	/**
	 * This message should update all clients.
	 */
	@Override
	public UpdateScheme getUpdateScheme() {
		return UpdateScheme.OpposingTeam;
	}

	@Override
	public int getSendersId() {
		return m_ownerId;
	}
}
