package components;

import java.awt.Point;

import model.MapModel;
import model.Unit;
import projectile.OnHitEffect;
import projectile.Projectile;
import creep.NullCreep;

//don't yell at me I'm doing this to make passing stuff easier so I don't have to throw a ton of stuff in the constructor and it makes more sense. This isn't the attack component we just removed.

public class AttackComponent {
	private int damage;
	private int range;
	private double secondsDelayBetweenAttacks;
	private String projectileImage;
	private int projectileSpeed;
	private long lastAttack;
	private OnHitEffect ohe;
	private boolean homing;

	/**
	 * 
	 * @param damage
	 * @param range
	 * @param attacksPerSecond
	 * @param projectileImage
	 * @param projectileSpeed
	 */
	public AttackComponent(int damage, int range, double attacksPerSecond,
			String projectileImage, int projectileSpeed) {
		construct(damage, range, attacksPerSecond,	projectileImage, projectileSpeed);
	}

	//for things that have special attacks
	public AttackComponent(int damage, int range, double attacksPerSecond,
			String projectileImage, int projectileSpeed, OnHitEffect ohe) {
		construct(damage, range, attacksPerSecond,	projectileImage, projectileSpeed);
		this.ohe = ohe;
	}
	
	//for non-homing attackers
	public AttackComponent(int damage, int range, double attacksPerSecond,
			String projectileImage, int projectileSpeed, boolean homing) {
		construct(damage, range, attacksPerSecond,	projectileImage, projectileSpeed);
		this.homing = homing;
	}
	//for things that don't attack
	public AttackComponent() {
		this.setDamage(0);
		this.setRange(0);
		this.setSecondsDelayBetweenAttacks(Integer.MAX_VALUE);
		this.setProjectileImage(null);
		this.setProjectileSpeed(0);
		this.ohe = null;
	}

	private void construct(int damage, int range, double attacksPerSecond,
			String projectileImage, int projectileSpeed) {
		this.setDamage(damage);
		this.setRange(range);
		if (attacksPerSecond > 0) {
			this.setSecondsDelayBetweenAttacks((1.0 / attacksPerSecond));
		} else {
			this.setSecondsDelayBetweenAttacks(Integer.MAX_VALUE);
		}
//		System.out.println("This tower will attack " + attacksPerSecond + " times per second");
//		System.out.println("This tower will attack every " + secondsDelayBetweenAttacks + " seconds");
		this.setProjectileImage(projectileImage);
		this.setProjectileSpeed(projectileSpeed);
		this.lastAttack = System.nanoTime();
		this.ohe = null;
		this.homing = true;
	}

	public void setSecondsDelayBetweenAttacks(double attacksPerSecond) {
		this.secondsDelayBetweenAttacks = attacksPerSecond;
	}
	
	public double getSecondsDelayBetweenAttacks() {
		return this.secondsDelayBetweenAttacks;
	}

	public Projectile attack(Unit target, Point point, MapModel map) {
		lastAttack = System.nanoTime();
		Projectile proj;
		if(!homing) {
			target = new NullCreep(point, map, 10);
		}
		if (ohe != null)
			proj = new Projectile(getDamage(), target, point,
					getProjectileImage(), getProjectileSpeed(), map, ohe);
		else
			proj = new Projectile(getDamage(), target, point,
					getProjectileImage(), getProjectileSpeed(), map);
		return proj;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public boolean isInRange(Point me, Unit target) {
		return (range >= me.distance(target.getPoint()));
	}

	public boolean canAttack() {

		return ((System.nanoTime() - lastAttack) / 1000000000.0) > secondsDelayBetweenAttacks;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public String getProjectileImage() {
		return projectileImage;
	}

	public void setProjectileImage(String projectileImage) {
		this.projectileImage = projectileImage;
	}

	public int getProjectileSpeed() {
		return projectileSpeed;
	}

	public void setProjectileSpeed(int projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}
}
