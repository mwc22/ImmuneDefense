package model;

import interfaces.Displayable;

import java.awt.Point;
import java.util.Vector;

import projectile.Projectile;

public class Block {

	private volatile Vector<Displayable> things;
//	private volatile Vector<Unit> units; //are these needed?
	private volatile Vector<Creep> creeps;
	private volatile Vector<Tower> towers;
//	private volatile Vector<Projectile> projectiles;
//	private Point upperLeft; //does the block need to know its location?
//	private int width;
//	private int height;

	public Block() {
		this.setThings(new Vector<Displayable>());
		towers = new Vector<Tower>();
		creeps = new Vector<Creep>();
	}

	public void addThing(Displayable thing) {
		getThings().add(thing);
		if(thing.isInstanceOf("Creep"))
			creeps.add((Creep) thing);
		if(thing.isInstanceOf("Tower"))
			towers.add((Tower) thing);
	}

	public void removeThing(Displayable thing) {
		getThings().remove(thing);
		if(thing.isInstanceOf("Creep"))
			creeps.remove((Creep) thing);
		if(thing.isInstanceOf("Tower"))
			towers.remove((Tower) thing);
	}

	public Vector<Displayable> getThingsAt(Point pt) {
		Vector<Displayable> thingsAtPoint = new Vector<Displayable>();
		return getThingsAt(pt, thingsAtPoint);
	}

	public Vector<Displayable> getThingsAt(Point pt,
			Vector<Displayable> thingsAtPoint) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().equals(pt))
				thingsAtPoint.add(thing);
		}
		return thingsAtPoint;
	}

	public Vector<Unit> getUnitsAt(Point pt) {
		Vector<Unit> thingsAtPoint = new Vector<Unit>();
		return getUnitsAt(pt, thingsAtPoint);
	}

	public Vector<Unit> getUnitsAt(Point pt, Vector<Unit> thingsAtPoint) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().equals(pt) && thing.isInstanceOf("Unit"))
				thingsAtPoint.add((Unit) thing);
		}
		return thingsAtPoint;
	}

	public Vector<Displayable> getThingsWithinRadiusOf(Point pt, int radius) {
		Vector<Displayable> thingsAtPoint = new Vector<Displayable>();
		return getThingsWithinRadiusOf(thingsAtPoint, pt, radius);
	}

	public Vector<Displayable> getThingsWithinRadiusOf(
			Vector<Displayable> thingsAtPoint, Point pt, int radius) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().distance(pt) <= radius)
				thingsAtPoint.add(thing);
		}
		return thingsAtPoint;
	}

	public Vector<Unit> getUnitsWithinRadiusOf(Point pt, int radius) {
		Vector<Unit> thingsAtPoint = new Vector<Unit>();
		return getUnitsWithinRadiusOf(thingsAtPoint, pt, radius);
	}

	public Vector<Unit> getUnitsWithinRadiusOf(
			Vector<Unit> thingsAtPoint, Point pt, int radius) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().distance(pt) <= radius && thing.isInstanceOf("Unit"))
				thingsAtPoint.add((Unit) thing);
		}
		return thingsAtPoint;
	}
	
	public Vector<Creep> getCreepsWithinRadiusOf(Point pt, int radius) {
		Vector<Creep> thingsAtPoint = new Vector<Creep>();
		return getCreepsWithinRadiusOf(thingsAtPoint, pt, radius);
	}

	public Vector<Creep> getCreepsWithinRadiusOf(
			Vector<Creep> thingsAtPoint, Point pt, int radius) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().distance(pt) <= radius && thing.isInstanceOf("Creep"))
				thingsAtPoint.add((Creep) thing);
		}
		return thingsAtPoint;
	}
	
	public Vector<Tower> getTowersWithinRadiusOf(Point pt, int radius) {
		Vector<Tower> thingsAtPoint = new Vector<Tower>();
		return getTowersWithinRadiusOf(thingsAtPoint, pt, radius);
	}

	public Vector<Tower> getTowersWithinRadiusOf(
			Vector<Tower> thingsAtPoint, Point pt, int radius) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().distance(pt) <= radius && thing.isInstanceOf("Tower"))
				thingsAtPoint.add((Tower) thing);
		}
		return thingsAtPoint;
	}
	
	public Vector<Projectile> getProjectilesWithinRadiusOf(Point pt, int radius) {
		Vector<Projectile> thingsAtPoint = new Vector<Projectile>();
		return getProjectilesWithinRadiusOf(thingsAtPoint, pt, radius);
	}

	public Vector<Projectile> getProjectilesWithinRadiusOf(
			Vector<Projectile> thingsAtPoint, Point pt, int radius) {
		for (Displayable thing : getThings()) {
			if (thing.getPoint().distance(pt) <= radius && thing.isInstanceOf("Projectile"))
				thingsAtPoint.add((Projectile) thing);
		}
		return thingsAtPoint;
	}

	public Vector<Displayable> getThings() {
		return things;
	}
	
	public Vector<Creep> getCreeps() {
		return creeps;
	}
	public Vector<Tower> getTowers() {
		return towers;
	}

	public void setThings(Vector<Displayable> things) {
		this.things = things;
	}
}
