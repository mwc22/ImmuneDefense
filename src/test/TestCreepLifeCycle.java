package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gamemaps.TestMap1;

import java.awt.Point;
import java.util.Vector;

import model.Creep;
import model.MapModel;
import model.Path;
import model.Player;
import model.SinglePlayer;
import model.Tower;

import org.junit.Before;
import org.junit.Test;

import projectile.AoEDamageToCreeps;
import projectile.Projectile;
import tower.Antibody;
import tower.Macrophage;
import view.ScrollViewScreen;
import creep.Influenza;

public class TestCreepLifeCycle {

	private Player player;
	private Vector<Path> paths;
	private SinglePlayer gm;
	private MapModel map;
	
	
	@Before
	public void SetUpEverything() {
		player = new Player("Bob",500,100);
		paths = new Vector<Path>();
		paths.add(Path.makeTestPath1());
		gm = new SinglePlayer("Bob", paths, 1000, 1000);
		ScrollViewScreen svs = new ScrollViewScreen(gm);
		map = gm.getMap();
	}
	@Test
	public void testCreepLosesHealthWhenTowerAttacksIt() {
		Path path = Path.makeTestPath1();
		Creep creep = new Influenza(path, player, 0, new Point(10,10), 0); //create the creep to be attacked
		assertEquals(40, creep.getHealth());
		assertEquals(4, creep.getArmor());
		assertTrue(creep.isAlive());
		Tower tower = new Macrophage(new Point(10,10), 1); //create the tower to attack it
		assertEquals(30, tower.getAttackDamage());
		assertTrue(tower.isAlive());
		assertTrue(tower.getAttackRange() >= tower.getPoint().distance(creep.getPoint()));
		double wait = tower.getAttackComponent().getSecondsDelayBetweenAttacks();
		try {
			Thread.sleep((int) (wait * 1050));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tower.setTarget(creep); //have the tower target the creep
		Projectile proj = tower.attack(map);	//have the tower attack its target
		proj.strike(map); //have the projectile strike its target
		
		assertEquals(14, creep.getHealth()); //Tower does 30 damage, reduced to 26 by creep's armor, so creep has 40-26=14 health
		
		try {
			Thread.sleep((long) (wait * 1050));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		proj = tower.attack(map);	//have the tower attack its target
		proj.strike(map); //have the projectile strike its target
		
		assertEquals(0, creep.getHealth()); //Creep health cannot fall below 0
		assertFalse(creep.isAlive()); //Creep is dead
		
		tower.checkValid();
		
		assertEquals(null, tower.getTarget()); //Tower has no target now
	}
	
	@Test
	public void testTowerWillNotAttackCreepThatMovesOutOfRange() {
		Path path = Path.makeTestPath1();
		Creep creep = new Influenza(path, player, 0, new Point(10,10), 0); //create the creep to be attacked
		assertEquals(40, creep.getHealth());
		assertEquals(4, creep.getArmor());
		assertTrue(creep.isAlive());
		Tower tower = new Macrophage(new Point(10,10), 1); //create the tower to attack it
		assertEquals(30, tower.getAttackDamage());
		assertTrue(tower.isAlive());
		assertTrue(tower.getAttackRange() >= tower.getPoint().distance(creep.getPoint()));
		double wait = tower.getAttackComponent().getSecondsDelayBetweenAttacks();
		try {
			Thread.sleep((int) (wait * 1050));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tower.setTarget(creep); //have the tower target the creep
		Projectile proj = tower.attack(map);	//have the tower attack its target
		proj.strike(map); //have the projectile strike its target
		
		assertEquals(14, creep.getHealth()); //Tower does 30 damage, reduced to 26 by creep's armor, so creep has 40-26=14 health
		
		creep.move(100, map); //move creep out of tower's range
		
		tower.checkValid(); //update tower
		
		assertEquals(null, tower.getTarget());
		
		
	}
	
	@Test
	public void testMovementSlowStatusEffect() {
		Path path = Path.makeTestPath1();
		Creep creep = new Influenza(path, player, 0, new Point(10,10), 0); //create the creep to be attacked
		assertEquals(40, creep.getHealth());
		assertEquals(4, creep.getArmor());
		assertTrue(creep.isAlive());
		assertEquals(20, creep.getMoveSpeed());
		Tower tower = new Antibody(new Point(10,11), 1); //create the tower to attack it
		assertEquals(10, tower.getAttackDamage());
		assertTrue(tower.isAlive());
		assertTrue(tower.getAttackRange() >= tower.getPoint().distance(creep.getPoint()));
		double wait = tower.getAttackComponent().getSecondsDelayBetweenAttacks();
		try {
			Thread.sleep((int) (wait * 1050));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tower.setTarget(creep); //have the tower target the creep
		Projectile proj = tower.attack(map);	//have the tower attack its target
		proj.strike(map); //have the projectile strike its target
		
		assertEquals(34, creep.getHealth()); //Tower does 10 damage, reduced to 6 by creep's armor, so creep has 40-6=34 health
		assertEquals(16, creep.getMoveSpeed()); //MoveSpeedSlow reduces speed by 20%

	}
	
	@Test
	public void testAoEOnHitEffect() {
		Path path = Path.makeTestPath1();
		Creep creep = new Influenza(path, player, 0, new Point(10,10), 0); //create the creep to be attacked
		assertEquals(40, creep.getHealth());
		assertEquals(4, creep.getArmor());
		assertTrue(creep.isAlive());
		assertEquals(20, creep.getMoveSpeed());
		Creep near = new Influenza(path, player, 0, new Point(11,11), 1); //create the create to be hit by the aoe
		Tower tower = new Antibody(new Point(10,10), new AoEDamageToCreeps(10, 5), 2); //create the tower to attack it, and aoe will do 5 damage to all creeps within 10 units
		assertEquals(10, tower.getAttackDamage());
		assertTrue(tower.isAlive());
		assertTrue(tower.getAttackRange() >= tower.getPoint().distance(creep.getPoint()));
		double wait = tower.getAttackComponent().getSecondsDelayBetweenAttacks();
		gm.getMap().addCreep(creep);
		gm.getMap().addCreep(near);
		try {
			Thread.sleep((int) (wait * 1050));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tower.setTarget(creep); //have the tower target the creep
		Projectile proj = tower.attack(map);	//have the tower attack its target
		proj.strike(map); //have the projectile strike its target
		
		assertEquals(34, creep.getHealth()); //Tower does 10 damage, reduced to 6 by creep's armor, so creep has 40-6=34 health
		assertEquals(39, near.getHealth()); //AoE does 5 damage, reduced to 1 by creep's armor, so creep has 40-1=39 health
	}
	

}
