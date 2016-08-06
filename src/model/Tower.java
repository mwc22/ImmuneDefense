package model;

import interfaces.TowerDefenseModel;

import java.awt.Color;
import java.util.Vector;

import tower.TowerSelection;

import components.TowerAnimation;

/**
 * Constructs all the towers
 * 
 * @author Bhavana Gorti
 * @author Travis Woodrow
 * @author Patrick Martin
 * @author Michael Curtis
 * 
 */
public abstract class Tower extends Unit {
	
	
	//	private int range;
	//	private int damage;
	//	private Point location;
	//	private double fireRate;
	//	private int health;
	//	private String projectileFileName;
	//	private long lastAttack;
	public static int COLOR = Color.BLUE.getRGB();
	
	public Tower() {
		super();
		//		lastAttack = System.nanoTime();
//		getAnimationComponent().setImage(getImage());
	}
	
	public abstract TowerSelection towerType();

	public abstract void upgrade(int range, double fireRate, int damage);

	//	public void setLocation(Point location) {
	//		this.location = location;
	//	}

	public void move(double dt, MapModel map) {
	}
	public String getType() {
		return "Tower";
	}
	public void notifyMoved(MapModel map) {
		//		System.out.println("Notified moved");
		checkValid(); //make sure current target is a valid target
		if (getTarget() == null) { //get a new target if this tower doesn't have a target
		//			System.out.println("Finding suitable targets");
			Vector<Creep> possibleTargets = map.getCreepsWithinRadiusOf(
					getPoint(), getAttackComponent().getRange());
			//			System.out.println("Found targets " + possibleTargets.size());
			if (!possibleTargets.isEmpty())
				setTarget(possibleTargets.get(0));
			else
				setTarget(null);
		}
		//		System.out.println("Target isn't null? " + (getTarget() != null));
		if (getTarget() != null) {
			//			System.out.println("Preparing to attack");
			attack(map);
		}
	}

	public void checkValid() {
		if (getTarget() != null) {
			if (!getAttackComponent().isInRange(getPoint(), getTarget())) { //make sure target is in range
				setTarget(null);
			} else if (!getTarget().isAlive()) { //make sure target is alive
				setTarget(null);
			}
		}
	}

	public int getMoveSpeed() {
		return 0;
	}

	public void removeFromGame(TowerDefenseModel game) {
		game.removeTower(this);
	}

	public void setMoveSpeed(int speed) {

	}

	@Override
	public boolean isInstanceOf(String displayable) {
		return super.isInstanceOf(displayable) || displayable.equals("Tower");
	}

}
