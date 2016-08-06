package model;

// imports
import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import components.CreepAnimation;
import creep.CreepSelection;




/**
 * Contructs the creeps
 * 
 * @author Bhavana Gorti
 * @author Michael Curtis
 * @author Patrick Martin
 * @author Travis Woodrow
 * 
 */
public abstract class Creep extends Unit {

	protected Player enemy;
	//	private Point point;
	protected Path path; //used in animation
	//	private int health;
	//	private int armor;	
	//	private int damage;
	protected int money;
	protected int speed; //used in animation
	protected int damageToBase;
	//	private int projectileSpeed;
	//	private String projectileImage;
	protected int pointsPassed = 0;
	public static int COLOR = Color.GREEN.getRGB();
	

	public Creep() {
		super();
		
		//System.out.println("Creep constructor finished for " + getName());
		
		//		this.point = point;
		//		this.projectileSpeed = projectileSpeed;
		//		this.projectileImage = projectileImage;
	//TODO: Check whey animationComponent is causing tests to fail!

		
	}
	//For NullCreep, which is just something for non-homing towers
	
	public int getDamageToBase() {
		return damageToBase;
	}
	public String getType() {
		return "Creep";
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

	public void notifyMoved(MapModel map) {
		checkValid(); //make sure current target is a valid target
		if (getTarget() == null) { //get a new target if this tower doesn't have a target
		//			System.out.println("Finding suitable targets");
			Vector<Tower> possibleTargets = map.getTowersWithinRadiusOf(
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

	public int getMoveSpeed() {
		return speed;
	}

	public void setMoveSpeed(int speed) {
		this.speed = speed;
	}

//	public void removeFromGame(GameModel game) {
//		game.removeCreep(this);
//	}

	public void die() {
//		System.out.println("Unit died, notifying observers");
		enemy.addMoney(money);
		setChanged();
		notifyObservers(false);
	}

	public void move(double dt, MapModel map) {
		if(path.hasNextPoint(pointsPassed)) {
		Point currentPoint = getPoint();
		
		double distance = dt * speed;

//		System.out.println(pointsPassed);
//		System.out.println( path.getNextPoint(pointsPassed));

		Point nextPoint = path.getNextPoint(pointsPassed);
//		System.out.println("Points passed: "+pointsPassed);
//		System.out.println("Next point: " + path.getNextPoint(pointsPassed));
		
		int dx = nextPoint.x - currentPoint.x;
		int dy = nextPoint.y - currentPoint.y;
		
//		System.out.println(dx);
//		System.out.println(dy);
		double distanceTarget = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
//		System.out.println(distanceTarget);
		double angle = Math.atan2(dy, dx);
//		System.out.println(angle);
		if (distance < distanceTarget) {
			double dxCreep = Math.cos(angle) * distance;
			double dyCreep = Math.sin(angle) * distance;

			movePoint(dxCreep,dyCreep);
		} else if(path.hasNextPoint(++pointsPassed)) {
//			System.out.println("If no");
			distance = distanceTarget - distance;
			currentPoint = nextPoint;
			nextPoint = path.getNextPoint(pointsPassed);

			dx = nextPoint.x - currentPoint.x;
			dy = nextPoint.y - currentPoint.y;

			distanceTarget = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			angle = Math.atan2(dy, dx);

			double dxCreep = Math.cos(angle) * distance;
			double dyCreep = Math.sin(angle) * distance;
//			dxCreep += currentPoint.x;
//			dyCreep += currentPoint.y;

			movePoint(dxCreep, dyCreep);
		} else {
			map.strikeBase(this, enemy);
		}
		setChanged();
		notifyObservers(true);
		animate();
		}
	}
	
	public Path getPath(){
		return path;
	}

	public int getPointsPassed(){
		return pointsPassed;
	}
	
	public abstract CreepSelection creepType();
	
	@Override
	public boolean isInstanceOf(String displayable) {
		return super.isInstanceOf(displayable) || displayable.equals("Creep");
	}
}
