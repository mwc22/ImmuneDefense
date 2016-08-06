package messages;

import java.awt.Point;

import model.Tower;
import server.UpdateScheme;
import tower.TowerSelection;
import interfaces.ClientMessage;
import interfaces.TowerDefenseModel;

/**
 * Message returned by server instructing the model to add a tower.
 * @author Travis
 *
 */
public class BuildTowerMessage implements ClientMessage {

	// vars
	private static final long serialVersionUID = 9593;
	private TowerSelection m_tower;
	private Point m_center;
	private int m_towerLevel;
	private int m_teamId;
	
	public BuildTowerMessage(
			TowerSelection towerToBuild,
			Point center,
			int towerLevel,
			int teamId) {
		m_tower = towerToBuild;
		m_center = center;
		m_towerLevel = towerLevel;
		m_teamId = teamId;
	}
	
	@Override
	public void execute(TowerDefenseModel model) throws Exception {
		Tower theTower = TowerSelection.getTower(
				m_tower,
				m_center,
				m_towerLevel,
				m_teamId);
		
		model.getMap().addTower(theTower);
	}

	/**
	 * This message should update all clients.
	 */
	@Override
	public UpdateScheme getUpdateScheme() {
		return UpdateScheme.YourTeam;
	}

	@Override
	public int getSendersId() {
		return m_teamId;
	}

}
