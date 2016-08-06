package model;

import interfaces.Displayable;
import interfaces.GameMap;
import interfaces.TowerDefenseModel;

import java.awt.Point;
import java.util.Vector;

import messages.RequestBuildTowerMessage;
import messages.RequestCreepSpawnMessage;
import messages.RequestGameOverMessage;
import projectile.Projectile;
import server.PlayerSocket;
import view.ImmuneResponse;
import client.Client;

public class MultiPlayer2 extends TowerDefenseModel {

        
        // vars
        public static final String SERVER_NAME = "localhost";
//        public int SERVER_PORT;
        private int m_teamId;
        private MultiMap2 m_map;
        private boolean m_gameOver;
        private Player m_player;
        private Player m_enemy;
        private Client m_client;
        private Thread m_clientRunner;
        
        public MultiPlayer2(PlayerSocket ps, GameMap gm, int teamId) {
        	m_gameOver = false;
            m_gameMap = gm;
            m_player = ImmuneResponse.player;
            m_player.prepareForGame(gm.getStartingGold(), gm.getStartingLife());
            m_enemy = new Player("enemy");
            PointConverter.setTowerSize(TOWER_SIZE);
            int height = gm.getBackground().getHeight();
            int width = gm.getBackground().getWidth();
            m_teamId = teamId;
            m_map = new MultiMap2(gm.getPaths(), m_teamId, width, height);
            m_client = new Client(ps, this);
            m_clientRunner = new Thread(m_client);
            m_clientRunner.start();
        }
        
        @Override
        public void run() {
                // not used
        }

        @Override
        public void update(double dt) {
                Vector<Displayable> things = m_map.getThings();
                
                for(int i = 0; i < things.size(); i++) {
                        Displayable thing = things.get(i);
                        thing.update(m_map, dt);
                }   
                
                if(m_player.getLife() <= 0 && !m_gameOver) {
                	m_client.sendMessageToServer(new RequestGameOverMessage(m_teamId, false));
                	m_gameOver = true;
                }
//        			lose();
        }

        @Override
        public MapModel getMap() {
                return m_map;
        }

        @Override
        public void addCreep(Creep creep) {
        	m_client.sendMessageToServer(new RequestCreepSpawnMessage(
    				creep.creepType(),
    				0,
    				creep.getTeamId(),
    				0));
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
                                    0, //TODO: hook up tower level
                                    m_teamId));                     
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
                m_map.lose();
        }

        @Override
        public void win() {
                m_map.win();
        }

        @Override
        public Player getPlayer() {
                return m_player;
        }

        @Override
        public Unit getUnitAtPoint(Point pt) {
                Vector<Unit> units = m_map.getUnitsWithinRadiusOf(pt, (int)(TOWER_SIZE * Math.sqrt(2)) / 2);
                
                if(units.isEmpty()) {
                        return null;
                }
                
                return units.get(0);
        }

        @Override
        public GameMap getGameMap() {
                return m_gameMap;
        }

		@Override
		public boolean isMultiplayer() {
			return true;
		}
		public int getEnemyId() {
			if(m_teamId == 1)
				return 0;
			
			return 1;
		}

		@Override
		public int getTeamID() {
			return m_teamId;
		}

		@Override
		public Player getEnemyPlayer() {
			return m_enemy;
		}
}