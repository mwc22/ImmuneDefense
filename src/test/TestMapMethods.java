package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gamemaps.TestMap1;

import java.awt.Point;
import java.util.Vector;

import model.Block;
import model.MapModel;
import model.Path;
import model.Player;
import model.SinglePlayer;
import model.Tower;

import org.junit.Before;
import org.junit.Test;

import tower.Macrophage;
import view.ScrollViewScreen;

public class TestMapMethods {

	private Player player;
	private Vector<Path> paths;
	private SinglePlayer gm;
	private MapModel map;

	@Before
	public void SetUpEverything() {
		paths = new Vector<Path>();
		paths.add(Path.makeTestPath1());
		gm = new SinglePlayer("Bob", paths, 1000, 1000);
		ScrollViewScreen svs = new ScrollViewScreen(gm);
		map = gm.getMap();
	}

	@Test
	public void testGetBlockAtPoint() {
		Block[][] blocks = map.getBlocks();
		//check the four corners
		//upper right NOT contained
		assertFalse(blocks[1][1].equals(map.getBlockAtPoint(20, 10)));
		//lower right NOT contained
		assertFalse(blocks[1][1].equals(map.getBlockAtPoint(20, 20)));
		//lower left NOT contained
		assertFalse(blocks[1][1].equals(map.getBlockAtPoint(10, 20)));
		//upper left IS contained		
		assertEquals(blocks[1][1], map.getBlockAtPoint(10, 10));

		//stuff in between IS contained
		assertEquals(blocks[1][1], map.getBlockAtPoint(15, 15));
		assertEquals(blocks[1][1], map.getBlockAtPoint(12, 18));
		assertEquals(blocks[1][1], map.getBlockAtPoint(18, 12));

		//upper right IS contained for upper right block
		assertEquals(blocks[99][0], map.getBlockAtPoint(990, 0));
	}

	@Test
	public void testAddTower() {
		Block[][] blocks = map.getBlocks();
		assertEquals(0, blocks[1][1].getThings().size());
		Tower tower = new Macrophage(new Point(200, 200), 0); //create the tower to attack it
		assertTrue(gm.addTower(tower));

		assertEquals(1, blocks[20][20].getThings().size());
		assertEquals(1, blocks[20][20].getThingsAt(new Point(200, 200)).size());
		assertEquals(1,
				blocks[20][20].getThingsWithinRadiusOf(new Point(200, 200), 1)
						.size());
		assertEquals(1,
				blocks[20][20].getTowersWithinRadiusOf(new Point(200, 200), 1)
						.size());
		assertEquals(1,
				blocks[20][20].getUnitsWithinRadiusOf(new Point(200, 200), 1)
						.size());
		assertEquals(0,
				blocks[20][20].getCreepsWithinRadiusOf(new Point(200, 200), 1)
						.size());
		assertEquals(0,
				blocks[20][20].getProjectilesWithinRadiusOf(new Point(200, 200), 1)
						.size());

	}

	@Test
	public void testGetBlocksWithinRadius() {
		//oh god this is not going to pass
		Block[][] blocks = map.getBlocks();
		//each block is 10 units wide
		assertEquals(1, map.getBlocksWithinRadiusOf(new Point(5, 5), 1).size());

	}

	@Test
	public void testCannotPlaceTowerOnPath() {
		/* These are the points in the path
		testPath.add(new Point(60, 1));
		testPath.add(new Point(60, 173));
		testPath.add(new Point(436, 173));
		testPath.add(new Point(436, 323));
		testPath.add(new Point(261, 323))
		 *
		 * A tower cannot be within 50 units of any of the lines
		 */
		Tower tower = new Macrophage(new Point(60, 173), 0); //create a tower on an endpoint
		assertFalse(gm.addTower(tower));
		tower = new Macrophage(new Point(100, 173), 0); //create a tower directly between two endpoints
		assertFalse(gm.addTower(tower));
		tower = new Macrophage(new Point(120, 160), 0); //create a tower within 50 units of two endpoints, but not directly between
		assertFalse(gm.addTower(tower));
		tower = new Macrophage(new Point(200, 200), 0); //create an acceptable tower
		assertTrue(gm.addTower(tower)); 
		
	}

}
