package model;

import interfaces.Displayable;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JOptionPane;

import projectile.Projectile;
import tower.Base;


/**
 * Constructs the Map. This keeps track of all the units and projectiles and their locations.
 * @author Bhavana Gorti
 * @author Travis Woodrow
 * @author Michael Curtis
 * @author Patrick Martin
 * 
 */
public class SingleMap implements MapModel {

	private volatile HashMap<Integer, Displayable> hashThings;
	private volatile Vector<Creep> Creeps; //List of all creeps on the map
	private volatile Vector<Tower> Towers; //list of all towers on the map
	private volatile Vector<Projectile> Projectiles; //list of all projectiles on the map
	private volatile Vector<Displayable> Things; //list of all displayables on the map
	private volatile Vector<Path> paths; //list of all paths on the map
	private volatile Block[][] blocks; //matrix of all X x Y blocks on the map
	private static final int TOWER_SIZE = 50;
	private final int MAX_NUMBER_OF_BLOCKS = 10000; //blocks will only have a maximum of 10,000 elements
	private final int BLOCK_SIZE;
	private final Base base;
	private final int BASE_HEALTH = 100;

	public SingleMap(Vector<Path> path, int widthOfImage, int heightOfImage) {
		setPaths(path);
		Towers = new Vector<Tower>(); //initialize the lists
		Creeps = new Vector<Creep>();
		Projectiles = new Vector<Projectile>();
		hashThings = new HashMap<Integer, Displayable>();
		setThings(new Vector<Displayable>());

		//now to find how big the matrix should be. I want the image to be covered by the blocks, no overlap (these are rectangular blocks), blocks to be square
		//so AREA_OF_IMAGE = widthOfImage * heightOfImage
		//and AREA_OF_BLOCK = BLOCK_SIZE * BLOCK_SIZE
		//and AREA_OF_BLOCK * NUMBER_OF_BLOCKS >= AREA_OF_IMAGE
		//so BLOCK_SIZE ^2 * NUMBER_OF_BLOCKS >= widthOfImage * heightOfImage
		//and also no more than 10,000 blocks
		//so 10000 * BLOCK_SIZE ^2 >= widthOfImage * heightOfImage
		//so BLOCK_SIZE >= Math.sqrt((widthOfImage * heightOfImage) / ((double) MAX_NUMBER_OF_BLOCKS))
		//Take the ceiling
		BLOCK_SIZE = (int) Math.ceil(Math.sqrt((widthOfImage * heightOfImage)
				/ ((double) MAX_NUMBER_OF_BLOCKS)));
		//using that, it is easy to find how many blocks are needed in each direction
		int matrixWidth = (int) Math.ceil(widthOfImage / ((double) BLOCK_SIZE));
		int matrixHeight = (int) Math.ceil(heightOfImage
				/ ((double) BLOCK_SIZE));
		setBlocks(new Block[matrixWidth][matrixHeight]); //this may be in the wrong order, although not sure if it matters
//		System.out.println("The input image is " + widthOfImage + " x "
//				+ heightOfImage);
//		System.out.println("This means each block will have side length of "
//				+ BLOCK_SIZE);
//		System.out.println("And the matrix is of size " + matrixWidth + " x "
//				+ matrixHeight);

		for (int i = 0; i < matrixWidth; i++) { //initialize everything
			for (int j = 0; j < matrixHeight; j++) {
				getBlocks()[i][j] = new Block();
			}
		}
		
		base = new Base(BASE_HEALTH, getPaths().get(0).getLast());
//		System.out.println("Base is located at: " + getPaths().get(0).getLast());
		Things.add(base);
		new FriendlyMinimap(blocks);

	}

	/* (non-Javadoc)
	 * @see model.MapModel#getBlockAtPoint(java.awt.Point)
	 */
	@Override
	public Block getBlockAtPoint(Point pt) {
		return getBlockAtPoint(pt.x, pt.y);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getBlockAtPoint(int, int)
	 */
	@Override
	public Block getBlockAtPoint(int x, int y) {
		x /= BLOCK_SIZE;
		y /= BLOCK_SIZE;
		if (x >= 0 && x < getBlocks().length && y >= 0 && y < getBlocks()[0].length)
			return getBlocks()[x][y];
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#addTower(model.Tower)
	 */
	@Override
	public void addTower(Tower tower) {
		tower.addObserver(this);
		
		getTowers().add(tower);
		getThings().add(tower);
		hashThings.put(tower.getID(), tower);		
		
		Block block = getBlockAtPoint(tower.getPoint());
		block.addThing(tower);
		tower.setCurrentBlock(block);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#addCreep(model.Creep)
	 */
	@Override
	public void addCreep(Creep creep) {
		creep.addObserver(this);
		
		getCreeps().add(0, creep);
		getThings().add(0, creep);
		hashThings.put(creep.getID(), creep);
		
		Block block = getBlockAtPoint(creep.getPoint());
		block.addThing(creep);
		creep.setCurrentBlock(block);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#addProjectile(projectile.Projectile)
	 */
	@Override
	public void addProjectile(Projectile projectile) {
		projectile.addObserver(this);
		getProjectiles().add(projectile);
		getThings().add(projectile);
		Block block = getBlockAtPoint(projectile.getPoint());
		block.addThing(projectile);
		projectile.setCurrentBlock(block);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#moveThing(interfaces.Displayable)
	 */
	@Override
	public void moveThing(Displayable thing) {
		Block block = thing.getCurrentBlock();
		block.removeThing(thing);
		Block newblock = getBlockAtPoint(thing.getPoint());
		newblock.addThing(thing);
		thing.setCurrentBlock(newblock);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getBlocksWithinRadiusOf(java.awt.Point, int)
	 */
	@Override
	public Vector<Block> getBlocksWithinRadiusOf(Point point, int radius) {
		//TODO: find the blocks that contain the radius
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
			blocks.add(this.blocks[i][centerY]);
		}
		//vertical, skipping origin
		for(int i = down;i<=up ;i++) {
			if(i != centerY&& i >= 0 && i < this.getBlocks()[0].length)
			blocks.add(this.blocks[centerX][i]);
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

	/* (non-Javadoc)
	 * @see model.MapModel#getCreepsWithinRadiusOf(java.awt.Point, int)
	 */
	@Override
	public Vector<Creep> getCreepsWithinRadiusOf(Point point, int radius) {
		Vector<Creep> nearby = new Vector<Creep>();
		Vector<Block> blocks = getBlocksWithinRadiusOf(point, radius);
		for (Block block : blocks) {
			block.getCreepsWithinRadiusOf(nearby, point, radius);
		}

		return nearby;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getTowersWithinRadiusOf(java.awt.Point, int)
	 */
	@Override
	public Vector<Tower> getTowersWithinRadiusOf(Point point, int radius) {
		Vector<Tower> nearbyTowers = new Vector<Tower>();
		//TODO: find the blocks that contain the radius
		Vector<Block> blocks = getBlocksWithinRadiusOf(point, radius);
		for (Block block : blocks) {
			block.getTowersWithinRadiusOf(nearbyTowers, point, radius);
		}
		return nearbyTowers;
	}
	
	/* (non-Javadoc)
	 * @see model.MapModel#checkValidTowerPosition(java.awt.Point)
	 */
	@Override
	public boolean checkValidTowerPosition(Point proposedPosition) {
		// iterate through all paths on the map
		for(int i=0;i<paths.size();i++) {
			Vector<Point> points = paths.get(i).path;
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
	
	/* (non-Javadoc)
	 * @see model.MapModel#getUnitsWithinRadiusOf(java.awt.Point, int)
	 */
	@Override
	public Vector<Unit> getUnitsWithinRadiusOf(Point point, int radius) {
		Vector<Unit> nearby = new Vector<Unit>();
		//TODO: find the blocks that contain the radius
		Vector<Block> blocks = getBlocksWithinRadiusOf(point, radius);
		for (Block block : blocks) {
			block.getUnitsWithinRadiusOf(nearby, point, radius);
		}
		return nearby;
	}
	
	/* (non-Javadoc)
	 * @see model.MapModel#getThingById(int)
	 */
	@Override
	public Displayable getThingById(int id) {
		Displayable thing = hashThings.get(id);
		return thing;
	}
	
	
	/* (non-Javadoc)
	 * @see model.MapModel#removeById(int)
	 */
	@Override
	public void removeById(int id) {
		Displayable thing = hashThings.remove(id);
		removeThing(thing);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#removeThing(interfaces.Displayable)
	 */
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

	/* (non-Javadoc)
	 * @see model.MapModel#removeTower(model.Tower)
	 */
	@Override
	public void removeTower(Tower tower) {
		getTowers().remove(tower);
		hashThings.remove(tower.getID());
		getBlockAtPoint(tower.getPoint()).removeThing(tower);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#removeCreep(model.Creep)
	 */
	@Override
	public void removeCreep(Creep creep) {
		getCreeps().remove(creep);
		hashThings.remove(creep.getID());
		getBlockAtPoint(creep.getPoint()).removeThing(creep);
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getPaths(int)
	 */
	@Override
	public Path getPaths(int pathIndex) {
		return paths.get(pathIndex); 
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getTowers()
	 */
	@Override
	public Vector<Tower> getTowers() {
		return Towers;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#setTowers(java.util.Vector)
	 */
	@Override
	public void setTowers(Vector<Tower> towers) {
		Towers = towers;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getCreeps()
	 */
	@Override
	public Vector<Creep> getCreeps() {
		return Creeps;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#setCreeps(java.util.Vector)
	 */
	@Override
	public void setCreeps(Vector<Creep> Creeps) {
		this.Creeps = Creeps;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getProjectiles()
	 */
	@Override
	public Vector<Projectile> getProjectiles() {
		return Projectiles;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#removeProjectile(projectile.Projectile)
	 */
	@Override
	public void removeProjectile(Projectile projectile) {
		getProjectiles().remove(projectile);

	}
	
	/* (non-Javadoc)
	 * @see model.MapModel#strikeBase(model.Creep, model.Player)
	 */
	@Override
	public void strikeBase(Creep creep, Player player) {
		player.loseLife(creep.getDamageToBase());
		base.strike(creep.getDamageToBase());
		removeCreep(creep);
	}
	
	/* (non-Javadoc)
	 * @see model.MapModel#lose()
	 */
	@Override
	public void lose() {
		//TODO what do I do
		JOptionPane.showMessageDialog(null, "You lost");
	}
	
	/* (non-Javadoc)
	 * @see model.MapModel#win()
	 */
	@Override
	public void win() {
		//TODO what do I do
		JOptionPane.showMessageDialog(null, "You won");
	}
	

	
	/* (non-Javadoc)
	 * @see model.MapModel#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
//		System.out.println("Update!");
		Displayable thing = (Displayable) arg0;
		if ((boolean) arg1) {
//			System.out.println("Unit moved!");
			moveThing(thing);
		} else {
//			System.out.println("Unit died!");
			removeThing(thing);
		}

	}

	/* (non-Javadoc)
	 * @see model.MapModel#getBlocks()
	 */
	@Override
	public Block[][] getBlocks() {
		return blocks;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#setBlocks(model.Block[][])
	 */
	@Override
	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getThings()
	 */
	@Override
	public Vector<Displayable> getThings() {
		return Things;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#setThings(java.util.Vector)
	 */
	@Override
	public void setThings(Vector<Displayable> things) {
		Things = things;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#getPaths()
	 */
	@Override
	public Vector<Path> getPaths() {
		return paths;
	}

	/* (non-Javadoc)
	 * @see model.MapModel#setPaths(java.util.Vector)
	 */
	@Override
	public void setPaths(Vector<Path> paths) {
		this.paths = paths;
	}

}
