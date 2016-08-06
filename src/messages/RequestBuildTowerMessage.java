package messages;

import interfaces.ClientMessage;
import interfaces.ServerMessage;

import java.awt.Point;

import server.Server;
import tower.TowerSelection;

/**
 * Instructs the server to build a tower for a user.
 * @author Travis
 *
 */
public class RequestBuildTowerMessage implements ServerMessage{
	
	// vars
	private static final long serialVersionUID = 23423;
	private TowerSelection m_tower;
	private Point m_center;
	private int m_towerLevel;
	private int m_playerId;
	
	public RequestBuildTowerMessage(
			TowerSelection towerToBuild,
			Point center,
			int level,
			int serversIdForPlayer) {
		m_tower = towerToBuild;
		m_center = center;
		m_towerLevel = level;
		m_playerId = serversIdForPlayer;
	}

	@Override
	public ClientMessage create(Server server) {
		return new BuildTowerMessage(m_tower, m_center, m_towerLevel, m_playerId);
	}
}
