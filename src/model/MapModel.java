package model;

import interfaces.Displayable;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import projectile.Projectile;

/**
 * MapModel extracted from the Map class.
 * @author Travis
 *
 */
public interface MapModel extends Observer{

	/**
	 * Returns the block associated with this point
	 * 
	 * @param pt
	 * @return
	 */
	public abstract Block getBlockAtPoint(Point pt);

	/**
	 * Returns the block associated with these coordinates
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract Block getBlockAtPoint(int x, int y);

	/**
	 * Adds the tower to the list of towers, the tower to the appropriate block,
	 * and tells the tower what block it is in
	 * 
	 * @param tower
	 */
	public abstract void addTower(Tower tower);

	/**
	 * Adds the creep to the list of creeps, the creep to the appropriate block,
	 * and tells the creep what block it is in
	 * 
	 * @param creep
	 */
	public abstract void addCreep(Creep creep);

	/**
	 * Adds the projectile to the list of projectiles, the projectile to the
	 * appropriate block, and tells the projectile what block it is in
	 * 
	 * @param projectile
	 */
	public abstract void addProjectile(Projectile projectile);

	/**
	 * Removes the object from the block it is in, adds it to the block that
	 * contains its point, and updates what block the thing thinks it is in
	 * 
	 * @param thing
	 */
	public abstract void moveThing(Displayable thing);

	/**
	 * 
	 * @param point
	 * @param radius
	 * @return
	 */
	public abstract Vector<Block> getBlocksWithinRadiusOf(Point point,
			int radius);

	/**
	 * Returns all creeps within the radius of the given point
	 * 
	 * @param point
	 * @param radius
	 * @return
	 */
	public abstract Vector<Creep> getCreepsWithinRadiusOf(Point point,
			int radius);

	/**
	 * Returns all towers within the radius of the given point
	 * 
	 * @param point
	 * @param radius
	 * @return
	 */
	public abstract Vector<Tower> getTowersWithinRadiusOf(Point point,
			int radius);

	public abstract boolean checkValidTowerPosition(Point proposedPosition);

	/**
	 * Returns all units within the radius of the given point
	 * 
	 * @param point
	 * @param radius
	 * @return
	 */
	public abstract Vector<Unit> getUnitsWithinRadiusOf(Point point, int radius);

	public abstract Displayable getThingById(int id);

	public abstract void removeById(int id);

	/**
	 * Removes the Displayable from the map
	 * 
	 * @param thing
	 */
	public abstract void removeThing(Displayable thing);

	/**
	 * Removes the tower from the map and the block it is in
	 * 
	 * @param tower
	 */
	public abstract void removeTower(Tower tower);

	/**
	 * Removes the creep from the map and the block it is in
	 * 
	 * @param creep
	 */
	public abstract void removeCreep(Creep creep);

	public abstract Path getPaths(int pathIndex);

	public abstract Vector<Tower> getTowers();

	public abstract void setTowers(Vector<Tower> towers);

	public abstract Vector<Creep> getCreeps();

	public abstract void setCreeps(Vector<Creep> Creeps);

	public abstract Vector<Projectile> getProjectiles();

	public abstract void removeProjectile(Projectile projectile);

	public abstract void strikeBase(Creep creep, Player player);

	public abstract void lose();

	public abstract void win();

	/**
	 * Map will be updated when things move or disappear
	 * 		
	 * @param the thing to change
	 * @param TRUE for move, FALSE for disappear
	 */
	public abstract void update(Observable arg0, Object arg1);

	public abstract Block[][] getBlocks();

	public abstract void setBlocks(Block[][] blocks);

	public abstract Vector<Displayable> getThings();

	public abstract void setThings(Vector<Displayable> things);

	public abstract Vector<Path> getPaths();

	public abstract void setPaths(Vector<Path> paths);
}