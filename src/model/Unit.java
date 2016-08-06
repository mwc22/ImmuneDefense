package model;


import interfaces.AnimationComponent;
import interfaces.Displayable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Vector;

import projectile.Projectile;
import projectile.Status;

import components.AttackComponent;

/**
 * 
 * @author Patrick
 * 
 *         Units are observable objects which can attack and can be attacked.
 *         Observers are notified when the unit dies.
 * 
 *         TODO: Do we want a defense component? That would cover the strike
 *         method.
 * 
 *         TODO: Do we want units to be more than one pixel in size? This would
 *         allow projectiles to hit the unit before reaching the center point.
 *         This would look especially good in the view, as projectiles would
 *         damage the target as soon as it reaches the image.
 * 
 *         TODO: Currently the only point we can get is the upper left corner.
 *         This means that projectiles must travel all the way to that corner
 *         before doing damage. At the very least, each creep should be able to
 *         calculate its center.
 * 
 *         TODO: Get the images to have a transparent background.
 * 
 * 
 */
public abstract class Unit extends Observable implements Displayable {
	//	private int damage;
	/**
	 * Current location of the unit
	 */
	protected double x;
	protected double y;
	//	private String projectileImage;
	//	private int projectileSpeed;
	/**
	 * Current health of the unit
	 */
	protected int health;
	/**
	 * Armor of the unit
	 */
	protected int armor;
	/**
	 * Game in which the game is taking place
	 */
//		private GameModel game;
	/**
	 * Unit this unit will attack
	 */
	protected Unit target;
	//	private long lastAttack;
	/**
	 * Manner of attack for this unit
	 */
	protected AttackComponent attackComponent;
	protected BufferedImage unitImage;
	protected AnimationComponent animationComponent;
	protected boolean isAlive = true;
	protected Block currentBlock;
	protected int id;
	protected int teamId;
	protected Vector<Status> statuses = new Vector<Status>();

	/**
	 * Create a unit with the given parameters.
	 * 
	 * @param attackComponent
	 *            Manner of attack for this unit
	 * @param point
	 *            Starting location for the unit
	 * @param health
	 *            Starting health for the unit
	 * @param armor
	 *            Starting armor for the unit
	 * @param game
	 *            Game in which this unit exists
	 */
	public Unit() {
//		System.out.println("Unit constructor done for " + getName());
		
//		this.setAttackComponent(attackComponent);
//		this.health = health;
//		this.armor = armor;
		
		
		//		lastAttack = System.nanoTime();

	}

	public void setAnimationComponent(AnimationComponent ac) {
		this.animationComponent = ac;
	}
	public AnimationComponent getAnimationComponent() {
		return this.animationComponent;
	}
	
	public abstract int getMoveSpeed();

	public abstract void setMoveSpeed(int speed);

	/**
	 * Let this unit know that the locations of objects on the map has changed.
	 * This is abstract because towers and creeps respond in different ways.
	 */
	public abstract void notifyMoved(MapModel map);

	/**
	 * Deal with the death of this unit, by awarding money to the player or
	 * something else
	 */
	public abstract void die();

	public abstract void move(double dt, MapModel map);

	/**
	 * 
	 * @return The name of the tower or creep (i.e. Macrophage, Salmonella)
	 */
	public abstract String getName();

	/**
	 * 
	 * @return The type of unit (i.e. Tower, Creep)
	 */
	public abstract String getType();

	public void addStatus(Status s) {
		statuses.add(s);
	}
	
	public Integer getID() {
		return id;
	}
	
	/**
	 * ID of the team this Unit belongs to.
	 * @return
	 */
	public int getTeamId() { return teamId; }
	/**
	 * ID of the team this Unit belongs to.
	 * @param theTeamId
	 */
	public void setTeamId(int theTeamId) { 
		teamId = theTeamId;
	}

	/**
	 * Attack the target in the manner specified in the AttackComponent
	 */

	public Projectile attack(MapModel map) {
		//		System.out.println("Attacking");
		//		System.out.println("Target is not null? " + (target != null));
		//		System.out.println("I can attack?  " + getAttackComponent().canAttack());
		if (target != null && getAttackComponent().canAttack()) {
			Projectile proj = getAttackComponent().attack(target, PointConverter.MoveToCenter(getPoint()),
					map);
			//			map.addProjectile(proj);  //TODO: Fix this
			map.addProjectile(proj);
			animationComponent.attack();
			return proj;
		}
		return null;

	}

	public void movePoint(double dx, double dy) {
		x += dx;
		y += dy;
	}

	//		if(attackComponent.isInRange(point,target)) {
	//			setTarget(getGame().getTheMap().getCreepsWithinRadiusOf(point, attackComponent.getRange()).get(0));
	//		}

	//		if(attackComponent.canAttack()) {
	////			lastAttack = System.nanoTime();
	//			attack();
	//		}

	/**
	 * Change the unit this unit will attack
	 * 
	 * @param target
	 */
	public void setTarget(Unit target) {
		this.target = target;
	}

	/**
	 * Have damage dealt to this unit. If the unit runs out of health, it dies.
	 * 
	 * @param damage
	 */
	public void strike(int damage) {
		health -= Math.max(damage - armor, 1); //attacks do at least one damage
		//		System.out.println("Unit was struck, doing " + damage + " damage.\nUnit now has " + health + " health.");
		if (health <= 0) {
			health = 0;
			//			removeFromGame(game);
			isAlive = false;
			die();
		}
	}

	/**
	 * Remove this unit from the GameModel
	 * 
	 * @param game
	 */
	//	public abstract void removeFromGame(GameModel game);

	public void update(MapModel map, double dt) {
		move(dt, map);
		notifyMoved(map);
		int i=0;
		while(i < statuses.size()) {
			if(!statuses.get(i).update())
				statuses.remove(i);
			else
				i++;
		}
	}

	/**
	 * 
	 * @return Type, name, damage, movespeed, health, armor
	 */
	public String getInformation() {
		return getType() + "\n" + getName() + "\n" + getDamage() + "\n"
				+ getMoveSpeed() + "\n" + getHealth() + "\n" + getArmor();
	}

	public int getDamage() {
		return getAttackComponent().getDamage();
	}

	public Point getPoint() {
		return new Point((int) x, (int) y);
	}

	public String getProjectileImage() {
		return getAttackComponent().getProjectileImage();
	}

	public int getProjectileSpeed() {
		return getAttackComponent().getProjectileSpeed();
	}

	public int getHealth() {
		return health;
	}

	public int getArmor() {
		return armor;
	}

	public BufferedImage getImage() {
		return unitImage;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	//	public GameModel getGame() {
	//		return game;
	//	}
	//
	//	public void setGame(GameModel game2) {
	//		this.game = game2;
	//	}

	public Unit getTarget() {
		return target;
	}

	public AttackComponent getAttackComponent() {
		return attackComponent;
	}

	public void setAttackComponent(AttackComponent attackComponent) {
		this.attackComponent = attackComponent;
	}

	public int getAttackDamage() {
		return attackComponent.getDamage();
	}

	public int getAttackRange() {
		return attackComponent.getRange();
	}

	public boolean isInstanceOf(String type) {
		return type.equals("Unit");
	}

	public Block getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(Block block) {
		this.currentBlock = block;
	}
	public BufferedImage animate(){
		return animationComponent.animate();
	}
}
