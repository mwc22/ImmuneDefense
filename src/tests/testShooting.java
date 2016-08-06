//package tests;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import java.awt.Point;
//import java.util.Vector;
//
//import model.Creep;
//import model.Map;
//import model.Path;
//import model.Player;
//import model.SinglePlayer;
//import model.Tower;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import Creep.Influenza;
//import Tower.Antibody;
//
//public class testShooting {
//
//	SinglePlayer game;
//	
//	@Before
//	public void CreateMap() {
//		Vector<Point> test= new Vector<Point>();
//		Point point= new Point();
//		point.setLocation(0, 0);
//		Point point2= new Point();
//		point2.setLocation(1000, 0);
//		test.add(point);
//		test.add(point2);
//		
//		Path path1= new Path(test);
//		
//		Vector<Point> test2= new Vector<Point>();
//		Point point3= new Point();
//		point3.setLocation(0, 0);
//		Point point4= new Point();
//		point4.setLocation(0,1000);
//		test2.add(point3);
//		test2.add(point4);
//		
//		Path path2= new Path(test2);
//		Vector<Path> paths= new Vector<Path>();
//		paths.add(path1);
//		paths.add(path2);
//		game= new SinglePlayer(null,paths,null, 1000, 1000);
//	}
//	
//	@Test
//	public void addTowerTest()
//	{
//		Point point= new Point();
//		point.setLocation(0,0);
//		Antibody tower= new Antibody(point, game, 0);
//		Map map = game.getTheMap();
//		map.addTower(tower);
//		assertTrue(map.getTowers().contains(tower));
//		map.removeTower(tower);
//		assertTrue(map.getCreeps().isEmpty());
//		assertTrue(map.getTowers().isEmpty());
//		
//	}
//	
//	@Test
//	public void spawnCreepTest()
//	{
//		Influenza creep = new Influenza(game.getTheMap().getPaths(0), null, 0, game, 0);
//		Map map= game.getTheMap();
//		map.addCreep(creep);
//		assertTrue(map.getCreeps().contains(creep));
//		map.removeCreep(creep);
//		assertFalse(map.getCreeps().contains(creep));
//	}
//	
//	@Test
//	public void radiusTest()
//	{
//		Influenza creep = new Influenza(game.getTheMap().getPaths(0), null, 0, game, 0);
//		Map map= game.getTheMap();
//		map.addCreep(creep);
//		Point point= new Point();
//		point.setLocation(100,0);
//		Antibody tower= new Antibody(point, game, 0);
//		map.addTower(tower);
//		Point testBoth= new Point();
//		point.setLocation(100,100);
//		
//		Vector<Creep> creepers= map.getCreepsWithinRadiusOf(tower.getPoint(), 1000);
//		assertTrue(creepers.contains(creep));
//		Vector<Tower> towers= map.getTowersWithinRadiusOf(creep.getPoint(), 1000);
//		assertTrue(towers.contains(tower));
//		//TODO: Check edge case for Radius Methods
//		
//		creepers= map.getCreepsWithinRadiusOf(tower.getPoint(), 99);
//		assertFalse(creepers.contains(creep));
//		towers= map.getTowersWithinRadiusOf(creep.getPoint(), 99);
//		assertFalse(towers.contains(tower));
//	}
//	
//	@Test
//	public void settingTarget()
//	{
//		Player player= new Player(null, 0,100);
//		Point point= new Point();
//		point.setLocation(0,0);
//		Antibody tower= new Antibody(point, game, 0);
//		game.getTheMap().addTower(tower);
//		Influenza creep = new Influenza(game.getTheMap().getPaths(0), player, 0, game, 0);
//		game.getTheMap().addCreep(creep);
//
//		assertEquals(null, tower.getTarget());
//		tower.setTarget(creep);
//		assertEquals(creep, tower.getTarget());
//	}
//	
//	@Test
//	public void shootTarget()
//	{
//		Point point= new Point();
//		point.setLocation(0,0);
//		Antibody tower= new Antibody(point, game, 0);
//		game.getTheMap().addTower(tower);
//		Influenza creep = new Influenza(game.getTheMap().getPaths(0), null, 0, game, 0);
//		game.getTheMap().addCreep(creep);
//
//		tower.attack(null);
//		game.run();
//		
//	}
//	
//	@Test
//	public void moveTest()
//	{
//		Influenza creep = new Influenza(Path.makeTestPath2(), null, 0, new Point(0,0), game);
//		creep.setMoveSpeed(100);
//		creep.move(1);
//		System.out.print(creep.getPoint());
//	}
//}
