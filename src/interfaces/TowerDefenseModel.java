package interfaces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observable;
import model.Creep;
import model.MapModel;
import model.Player;
import model.Tower;
import model.Unit;
import projectile.Projectile;

public abstract class TowerDefenseModel extends Observable implements Runnable {

	// vars
	protected GameMap m_gameMap;
	protected static final int TOWER_SIZE = 50;
	protected final long NANOS_PER_SEC = 1000000000;
	
	/**
	 * moves all the creeps and projectiles, updates the towers
	 * @param dt
	 */
	public abstract void update(double dt);

	public abstract MapModel getMap();

	public abstract void addCreep(Creep creep);

	/**
	 * First checks to see if a tower can be placed in a location. A tower cannot be placed in a location if 
	 * @param tower
	 * @return
	 */
	public abstract boolean addTower(Tower tower);
	public abstract void removeCreep(Creep creep);
	public abstract void removeTower(Tower tower);
	public abstract void removeProjectile(Projectile projectile);
	public abstract void lose();
	public abstract void win();
	public abstract Player getPlayer();
	public abstract Player getEnemyPlayer() throws Exception;
	public abstract Unit getUnitAtPoint(Point pt);
	public abstract GameMap getGameMap();
	public abstract boolean isMultiplayer();
	public abstract int getTeamID();
}