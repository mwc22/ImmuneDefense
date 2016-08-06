package model;

// from us
import interfaces.*;
import projectile.*;
import view.*;
import server.*;
import client.*;
// from java
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Vector;

import messages.RequestBuildTowerMessage;
import messages.RequestCreepSpawnMessage;

import creep.*;

/**
 * The single player game using the server to spawn creeps and such.
 * 
 * @author Travis
 * 
 */
public class SinglePlayerUsingServer extends TowerDefenseModel {

	// vars
	private MapModel m_map;
	private boolean m_gameOver;
	private Player m_player;
	private Thread m_creeps;
	private boolean bosswave;

	// server/client
	Server m_server;
	public static Client m_client;
	Thread m_serverThread;
	Thread m_clientThread;
	private static final int PORT_NUMBER = 4001;

	// game specific constants
	private final int MILLISECONDS_PAUSE_BETWEEN_CREEP_SPAWNS = 1000;
	private final int MILLISECONDS_PAUSE_BETWEEN_WAVE_STARTS = 5000;
	private final int TYPES_OF_CREEP = 3;
	private final int CREEPS_PER_WAVE = 10;

	// constructor
	public SinglePlayerUsingServer(
			String playerName, 	// name of the player
			GameMap map) { 		// GameMap object to use
		m_gameMap = map;
		m_gameOver = false;
		m_player = new Player(playerName);
		m_map = new SingleMap(map.getPaths(), map.getBackground().getWidth(), map
				.getBackground().getHeight());
		PointConverter.setTowerSize(TOWER_SIZE);
		m_server = new Server(PORT_NUMBER, 1);
		m_serverThread = new Thread(m_server);
		m_serverThread.start();
		m_client = new Client(PORT_NUMBER, playerName, 0, this);
		m_clientThread = new Thread(m_client);
		m_clientThread.start();
	}


	@Override
	public void update(double dt) {
		// ref to objects which we want to update
		Vector<Displayable> things = m_map.getThings();

		// UPDATE ALL THE THINGS!
		for (int i = 0; i < things.size(); i++) {
			Displayable thing = things.get(i);
			thing.update(m_map, dt);
		}
		if(m_player.getLife() <= 0)
			lose();
		// repaint the screen

	}

	@Override
	public MapModel getMap() {
		return m_map;
	}

	@Override
	public void addCreep(Creep creep) {
		m_map.addCreep(creep);

	}

	@Override
	public boolean addTower(Tower tower) {
		Point towerCenter = tower.getPoint();
		boolean towerPlacementValid = m_map.checkValidTowerPosition(towerCenter);

		if (towerPlacementValid) {
			//m_map.addTower(tower);
			m_client.sendMessageToServer(new RequestBuildTowerMessage(
					tower.towerType(),
					towerCenter,
					0,
					0));			
		}

		return towerPlacementValid;
	}
	

	@Override
	public void removeCreep(Creep creep) {
		m_map.removeCreep(creep);

	}

	@Override
	public void removeTower(Tower tower) {
		m_map.removeTower(tower);

	}

	@Override
	public void removeProjectile(Projectile projectile) {
		m_map.removeProjectile(projectile);
		
	}

	@Override
	public void lose() {
		m_gameOver = true;
		m_map.lose();
		setChanged();
		notifyObservers();
	}

	@Override
	public void win() {
		m_gameOver = true;
		m_map.win();
		setChanged();
		notifyObservers();
	}

	private class CreepCreater implements Runnable {
		private int timeBetweenCreepSpawnsInMilliseconds;
		private int timeBetweenWavesInMilliseconds = 5000;
		private int typesOfCreep;
		private int creepsInEachWave;
		private TowerDefenseModel game;
		private int creepsSpawned = 0;

		public CreepCreater(int time, int types, int creeps,
				TowerDefenseModel game) {
			this.timeBetweenCreepSpawnsInMilliseconds = time;
			this.typesOfCreep = types;
			this.creepsInEachWave = creeps;
			this.game = game;
		}

		@Override
		public void run() {
			int wave = 0;
			int creepPath = 0;
			if (bosswave) {
				m_client.sendMessageToServer(new RequestCreepSpawnMessage(
						CreepSelection.Zoidberg, wave, wave, 0));
			}
			while (!m_gameOver) {
				// vars
				CreepSelection theCreep = null;
				RequestCreepSpawnMessage rcs = null;

				for (int i = 0; i < creepsInEachWave; i++) {
					if (wave % typesOfCreep == 0)
						theCreep = CreepSelection.Influenza;
					else if (wave % typesOfCreep == 1)
						theCreep = CreepSelection.Salmonella;
					else
						theCreep = CreepSelection.Strep;

					// create message and send to server
					rcs = new RequestCreepSpawnMessage(theCreep, wave / typesOfCreep,
							0, creepPath);
					m_client.sendMessageToServer(rcs);
					creepPath++;

					try {
						Thread.sleep(timeBetweenCreepSpawnsInMilliseconds);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				wave++;
				
				try {
					Thread.sleep(timeBetweenWavesInMilliseconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public Player getPlayer() {
		return m_player;
	}

	@Override
	public void run() {
		CreepCreater cc = new CreepCreater(
				this.MILLISECONDS_PAUSE_BETWEEN_CREEP_SPAWNS,
				this.TYPES_OF_CREEP, this.CREEPS_PER_WAVE, this);

		Thread creeps = new Thread(cc);
		creeps.start();
	}

	@Override
	public Unit getUnitAtPoint(Point pt) {
		Vector<Unit> units = m_map.getUnitsWithinRadiusOf(pt,
				(int) (TOWER_SIZE * Math.sqrt(2)) / 2);
		if (units.isEmpty())
			return null;
		else
			return units.get(0);
	}

	@Override
	public GameMap getGameMap() {
		return m_gameMap;
	}


	@Override
	public boolean isMultiplayer() {
		return false;
	}


	@Override
	public int getTeamID() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Player getEnemyPlayer() throws Exception {
		throw new Exception("This should not be called in SinglePlayer.");
	}
}
