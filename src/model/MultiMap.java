package model;

import interfaces.Displayable;
import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;

import javax.swing.JOptionPane;

import projectile.Projectile;
import tower.Base;

public class MultiMap implements MapModel {
	
	// vars
	private int m_teamId; // this will be 0 or 1
	private volatile Vector<Path> m_paths;
	private volatile HashMap<Integer, Displayable> m_hashThings;
	private volatile Vector<Creep>[] m_creeps; //List of all creeps on the map
	private volatile Vector<Tower>[] m_towers; //list of all towers on the map
	private volatile Vector<Projectile> m_projectiles; //list of all projectiles on the map
	private volatile Vector<Displayable> m_things; //list of all displayables on the map
	private volatile Block[][] m_blocks; //matrix of all X x Y blocks on the map
	private static final int TOWER_SIZE = 50;
	private final int MAX_NUMBER_OF_BLOCKS = 10000; //blocks will only have a maximum of 10,000 elements
	private final int BLOCK_SIZE;
	private Base[] m_bases;
	private final int BASE_HEALTH = 100;
	
	
	public MultiMap(Vector<Path> paths, int teamId, int widthOfImage, int heightOfImage) {
		setPaths(paths);
		m_teamId = teamId;
		m_hashThings = new HashMap<Integer, Displayable>();
		m_creeps = new Vector[2];
		m_creeps[0] = new Vector<Creep>();
		m_creeps[1] = new Vector<Creep>();
		m_towers = new Vector[2];
		m_towers[0] = new Vector<Tower>();
		m_towers[1] = new Vector<Tower>();
		m_things = new Vector<Displayable>();
		m_projectiles = new Vector<Projectile>();
		
		BLOCK_SIZE = (int) Math.ceil(Math.sqrt((widthOfImage * heightOfImage)
				/ ((double) MAX_NUMBER_OF_BLOCKS)));
		int matrixWidth = (int) Math.ceil(widthOfImage / ((double) BLOCK_SIZE));
		int matrixHeight = (int) Math.ceil(heightOfImage
				/ ((double) BLOCK_SIZE));
		m_blocks = new Block[matrixWidth][matrixHeight];
		for (int i = 0; i < matrixWidth; i++) { //initialize everything
			for (int j = 0; j < matrixHeight; j++) {
				getBlocks()[i][j] = new Block();
			}
		}
		
		m_bases = new Base[2];
		m_bases[0] = new Base(BASE_HEALTH, getPaths().get(0).getLast());
		m_bases[1] = new Base(BASE_HEALTH, getPaths().get(1).getLast());
		m_things.add(m_bases[0]);
		m_things.add(m_bases[1]);
		new FriendlyMinimap(m_blocks);
	}

	@Override
	public Block getBlockAtPoint(Point pt) {
		return getBlockAtPoint(pt.x, pt.y);
	}

	@Override
	public Block getBlockAtPoint(int x, int y) {
		x /= BLOCK_SIZE;
		y /= BLOCK_SIZE;
		if (x >= 0 && x < getBlocks().length && y >= 0 && y < getBlocks()[0].length)
			return getBlocks()[x][y];
		else
			return null;
	}

	@Override
	public void addTower(Tower tower) {
		tower.addObserver(this);
		
		m_towers[tower.getTeamId()].add(tower);
		m_things.add(tower);
		m_hashThings.put(tower.getID(), tower);		
		
		Block block = getBlockAtPoint(tower.getPoint());
		block.addThing(tower);
		tower.setCurrentBlock(block);
	}

	@Override
	public void addCreep(Creep creep) {
		creep.addObserver(this);
		
		m_creeps[creep.getTeamId()].add(0, creep);
		m_things.add(0, creep);
		m_hashThings.put(creep.getID(), creep);
		
		Block block = getBlockAtPoint(creep.getPoint());
		block.addThing(creep);
		creep.setCurrentBlock(block);
	}

	@Override
	public void addProjectile(Projectile projectile) {
		projectile.addObserver(this);
		m_projectiles.add(projectile);
		m_things.add(projectile);
		Block block = getBlockAtPoint(projectile.getPoint());
		block.addThing(projectile);
		projectile.setCurrentBlock(block);
	}

	@Override
	public void moveThing(Displayable thing) {
		Block block = thing.getCurrentBlock();
		block.removeThing(thing);
		Block newblock = getBlockAtPoint(thing.getPoint());
		newblock.addThing(thing);
		thing.setCurrentBlock(newblock);
	}

	@Override
	public Vector<Block> getBlocksWithinRadiusOf(Point point, int radius) {
		/*
		 * c=center block (point) t=blocks containing the radius (target) block
		 * radius = 3 u = block directly above b = block directly below l =
		 * block directly to the left r = block directly to the right d = blocks
		 * contained in the diagonal 2 = blocks with two containing blocks on
		 * its border
		 * 
				 * - - - - - - - - - - - - - 
				 * - - - - 2 u 2 - - - - - - 
				 * - - - 2 d d d 2 - - - - - 
				 * - - 2 d d d d d 2 - - - - 
				 * - - l d d c d d r - - - - 
				 * - - 2 d d d d d 2 - - - - 
				 * - - - 2 d d d 2 - - - - - 
				 * - - - - 2 b 2 - - - - - - 
				 * - - - - - - - - - - - - - 
				 * 
				 * - - - - - - - - - - - - - - - - -
				 * - - - - - - - - u 2 - - - - - - -
				 * - - - - - - - 2 d d 2 - - - - - -
				 * - - - - - - 2 d d d d 2 - - - - -
				 * - - - - - - l d c d d r - - - - -
				 * - - - - - - 2 d d d d 2 - - - - -
				 * - - - - - - - 2 d d 2 - - - - - -
				 * - - - - - - - - b 2 - - - - - - -
				 * - - - - - - - - - - - - - - - - -
		 * 
		 * So I know one block, and that is the block that contains the point. I
		 * know four other blocks, and those are the points of the circle
		 * directly above, below, and to the sides of the point. Also, all points between the left and right sides
		 * and the upper and lower sides are in the radius as well.
		 * Then I can divide the board into four quadrants. In general, every box has a point which is closest to
		 * the center of the circle. For the boxes in the quadrants, that point is on the corner.
		 * I can go along the horizontal diameter of the circle and for each column, iterate up or down, adding blocks,
		 * until you reach a block that does not contain the circle.
		 * 
		 * 
		 */
		Vector<Block> blocks = new Vector<Block>();

		Point upright = new Point(point.x + radius, point.y + radius);
		Point downleft = new Point(point.x - radius, point.y - radius);
		
		int up = upright.y / BLOCK_SIZE;
		int right = upright.x / BLOCK_SIZE;
		int down = downleft.y / BLOCK_SIZE;
		int left = downleft.x / BLOCK_SIZE;
		int centerX = point.x / BLOCK_SIZE;
		int centerY = point.y / BLOCK_SIZE;
		
		//add all the blocks between the extrema
		//horizontal
		for(int i = left;i<=right;i++) {
			if(i >= 0 && i < this.getBlocks().length)
			blocks.add(m_blocks[i][centerY]);
		}
		//vertical, skipping origin
		for(int i = down;i<=up ;i++) {
			if(i != centerY&& i >= 0 && i < this.getBlocks()[0].length)
			blocks.add(m_blocks[centerX][i]);
		}
		
		/*
		 *	boxes in quadrant I have their closest point in the lower left corner
		 */

		for (int i = centerX + 1;i <= right && i >= 0 && i < this.getBlocks().length; i++) {
			for (int j = centerY+1; j <= up && j >= 0 && j < this.getBlocks()[0].length; j++) {
				Point pt = new Point(i*BLOCK_SIZE,j*BLOCK_SIZE);
				if(pt.distance(point) > radius)
					break;
				blocks.add(this.getBlocks()[i][j]);
			}
		}
		
		/*
		 *	boxes in quadrant II have their closest point in the lower right corner
		 */

		for (int i = centerX - 1;i >= left && i >= 0 && i < this.getBlocks().length; i--) {
			for (int j = centerY + 1; j <= up && j >= 0 && j < this.getBlocks()[0].length; j++) {
				Point pt = new Point((i+1)*BLOCK_SIZE,j*BLOCK_SIZE);
				if(pt.distance(point) > radius)
					break;
				blocks.add(this.getBlocks()[i][j]);
			}
		}
		
		/*
		 *	boxes in quadrant III have their closest point in the upper right corner
		 */

		for (int i = centerX - 1;i >= left && i >= 0 && i < this.getBlocks().length; i--) {
			for (int j = centerY - 1; j >= down && j >= 0 && j < this.getBlocks()[0].length; j--) {
				Point pt = new Point((i+1)*BLOCK_SIZE,(j+1)*BLOCK_SIZE);
				if(pt.distance(point) > radius)
					break;
				blocks.add(this.getBlocks()[i][j]);
			}
		}
		
		/*
		 *	boxes in quadrant IV have their closest point in the upper left corner
		 */

		for (int i = centerX + 1;i <= right && i >= 0 && i < this.getBlocks().length; i++) {
			for (int j = centerY - 1; j >= down && j >= 0 && j < this.getBlocks()[0].length; j--) {
				Point pt = new Point(i*BLOCK_SIZE,(j+1)*BLOCK_SIZE);
				if(pt.distance(point) > radius)
					break;
				blocks.add(this.getBlocks()[i][j]);
			}
		}

		return blocks;
	}

	@Override
	public Vector<Creep> getCreepsWithinRadiusOf(Point point, int radius) {
		Vector<Creep> nearby = new Vector<Creep>();
		Vector<Block> blocks = getBlocksWithinRadiusOf(point, radius);
		for (Block block : blocks) {
			block.getCreepsWithinRadiusOf(nearby, point, radius);
		}

		return nearby;
	}

	@Override
	public Vector<Tower> getTowersWithinRadiusOf(Point point, int radius) {
		Vector<Tower> nearbyTowers = new Vector<Tower>();
		Vector<Block> blocks = getBlocksWithinRadiusOf(point, radius);
		for (Block block : blocks) {
			block.getTowersWithinRadiusOf(nearbyTowers, point, radius);
		}
		return nearbyTowers;
	}

	@Override
	public boolean checkValidTowerPosition(Point proposedPosition) {
		// iterate through all paths on the map
		for(int i=0;i<m_paths.size();i++) {
			Vector<Point> points = m_paths.get(i).path;
			// iterate through all points in a path
			for(int j=0; j<points.size() - 1; j++) {
				Point one = points.get(j);
				Point two = points.get(j+1);
				//find the least acceptable area of a tower near the path
				/*
				 * A tower 
				 */
				double distance = one.distance(two);
				double minArea = distance * TOWER_SIZE / 2.0;
				boolean tooCloseToPath = ( minArea > area(one, two, proposedPosition) );
				boolean tooCloseToEndpoint = ( one.distance(proposedPosition) < distance + TOWER_SIZE );
				tooCloseToEndpoint &= ( two.distance(proposedPosition) < distance + TOWER_SIZE );
				if(tooCloseToPath && tooCloseToEndpoint) {
					return false;
				}
			}			 
		}
		//at this point the tower is not in conflict with any paths.
		//check other towers
		Vector<Tower> towers = getTowersWithinRadiusOf(proposedPosition, (int) (TOWER_SIZE / 1.5));
		boolean anyTowersWithinRadius = !towers.isEmpty();
		if(anyTowersWithinRadius) {
			return false;
		}
		
		// proposed tower position is valid
		return true;
	}

	private double area(Point a, Point b, Point c) {
		double area = Math.abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2.0;
		return area;
	}
	
	@Override
	public Vector<Unit> getUnitsWithinRadiusOf(Point point, int radius) {
		Vector<Unit> nearby = new Vector<Unit>();
		// find the blocks that contain the radius
		Vector<Block> blocks = getBlocksWithinRadiusOf(point, radius);
		for (Block block : blocks) {
			block.getUnitsWithinRadiusOf(nearby, point, radius);
		}
		return nearby;
	}

	@Override
	public Displayable getThingById(int id) {
		Displayable thing = m_hashThings.get(id);
		return thing;		
	}

	@Override
	public void removeById(int id) {
		Displayable thing = m_hashThings.remove(id);
		removeThing(thing);
	}

	@Override
	public void removeThing(Displayable thing) {
		getThings().remove(thing);
		if (thing.isInstanceOf("Tower")) {
			removeTower((Tower) thing);
		} else if (thing.isInstanceOf("Creep")) {
			removeCreep((Creep) thing);
		} else if (thing.isInstanceOf("Projectile")) {
			removeProjectile((Projectile) thing);
		}
	}

	@Override
	public void removeTower(Tower tower) {
		getTowers().remove(tower);
		m_hashThings.remove(tower.getID());
		getBlockAtPoint(tower.getPoint()).removeThing(tower);
	}

	@Override
	public void removeCreep(Creep creep) {
		getCreeps().remove(creep);
		m_hashThings.remove(creep.getID());
		getBlockAtPoint(creep.getPoint()).removeThing(creep);
	}

	@Override
	public Path getPaths(int pathIndex) {
		return m_paths.get(pathIndex);
	}

	@Override
	public Vector<Tower> getTowers() {
		return m_towers[m_teamId];
	}

	@Override
	public void setTowers(Vector<Tower> towers) {
		m_towers[m_teamId] = towers;
		
	}

	@Override
	public Vector<Creep> getCreeps() {
		return m_creeps[m_teamId];
	}

	@Override
	public void setCreeps(Vector<Creep> creeps) {
		m_creeps[m_teamId] = creeps;
	}

	@Override
	public Vector<Projectile> getProjectiles() {
		return m_projectiles;
	}

	@Override
	public void removeProjectile(Projectile projectile) {
		m_projectiles.remove(projectile);		
	}

	@Override
	public void strikeBase(Creep creep, Player player) {
		player.loseLife(creep.getDamageToBase());
		int teamToAttack = 1;
		if(creep.getTeamId() == 1) { teamToAttack = 0; }
		m_bases[teamToAttack].strike(creep.getDamageToBase());
		removeCreep(creep);
	}

	@Override
	public void lose() {
		JOptionPane.showMessageDialog(null, "You lost");
	}

	@Override
	public void win() {
		JOptionPane.showMessageDialog(null, "You won");		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Displayable thing = (Displayable) arg0;
		if ((boolean) arg1) {
			moveThing(thing);
		} else {
			removeThing(thing);
		}		
	}

	@Override
	public Block[][] getBlocks() {
		return m_blocks;
	}

	@Override
	public void setBlocks(Block[][] blocks) {
		m_blocks = blocks;		
	}

	@Override
	public Vector<Displayable> getThings() {
		return m_things;
	}

	@Override
	public void setThings(Vector<Displayable> things) {
		m_things = things;
	}

	@Override
	public Vector<Path> getPaths() {
		return m_paths;
	}

	@Override
	public void setPaths(Vector<Path> paths) {
		m_paths = paths;
	}
	
	/**
	 * Gets the other teams teamId
	 * @return
	 */
	private int getEnemyId() {
		if(m_teamId == 0)
			return 1;
		return 0;
	}
}
