package model;

import interfaces.Displayable;
import interfaces.GameMap;
import interfaces.TowerDefenseModel;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Vector;

import projectile.Projectile;
import view.ScrollViewScreen;
import creep.Influenza;
import creep.Salmonella;
import creep.Strep;
import creep.Zoidberg;


/**
 * This is effectively the intermediary between the GUI and the Map. It moves
 * everything, contains the player, ends the game, and all super-game actions
 * (i.e. things that are not internal to the game).
 * 
 * Here's how the entire thing works as of now: The GUI will create a GameModel
 * and send this model the player, the gui screen, the width of the map image,
 * and the height of the map image This GameModel will then set up the game:
 * -Create the map: it sets up the map, which divides the gameboard into 10,000
 * blocks in which Displayables can be placed -Prepare the game: Create the
 * player, give the player money
 * 
 * There are two parts to this. The first is the runnable section, which will
 * need to be redone as we implement more gametypes. It currently continuously
 * spawns a certain number of a certain type of creep every certain amount of
 * time, pauses for a bit, then repeats with a different type of creep. This
 * should probably be updated with a list of strings for the different creep
 * types, or just with different runnable creepcreaters for each game mode.
 * 
 * The second part is the update. The GUI runs a timer which updates this Game
 * Model every so often. This model then notifies every creep to move, towers to
 * attack, and projectiles to move. It then repaints the GUI screen.
 * 
 * Map keeps track of where objects are on the screen. Any time something is
 * created, it must be added to the map. When it moves, it is updated in the
 * map. When it dies, it is removed from the map. For creation, all creep and
 * tower creation should be done through this GameModel after being called from
 * the GUI. Projectile creation is done by Units, and only when they are
 * notified of movement. Currently, since every projectile will have a target,
 * and every target/unit has access to this GameModel, upon projectile creation
 * it gets this GameModel from its target and adds the newly created projectile.
 * This may not be optimal, and since this GameModel is responsible for
 * movement, it could check for projectiles to add after each unit is notified,
 * and add it if one was created.
 * 
 * To work with movement and death, the Map is an observer. Each time a
 * Displayable is created, it is observed by the map. Then whenever the
 * Displayable moves, it updates the Map with a message of "true", indicating it
 * has moved and therefore its location may need to be updated in the grid. Any
 * time a Displayable dies, it updates the Map with a message of "false",
 * indicating that it needs to be removed from the grid.
 * 
 * What happens when things are notified during the update method in this class:
 * Creep: They move. (TODO?) Should probably make the notify method in Unit to
 * implement attacking capabilities for creeps (and movement capabilities for
 * towers?)
 * 
 * Tower: They get all enemies within range by accessing the map. They then
 * choose the first enemy in that list. TODO: Make Displayable a comparable by
 * spawn time or distance traveled so towers can attack the enemy that spawned
 * first or has traveled the furthest distance for smarter targeting.
 * 
 * Projectile: They move, check if they hit their target, and if they do, then
 * deal damage and possibly apply some secondary effect.
 * 
 * I believe this is general enough to deal with different game types.
 * 
 * @author Patrick
 * 
 */
@Deprecated
public class SinglePlayer extends TowerDefenseModel {

	// vars
	private MapModel map;
//	private long lastTime;im
	private boolean gameNotOver;
	private Player player;
	private final int MILLISECONDS_PAUSE_BETWEEN_CREEP_SPAWNS = 1000;
	private final int TYPES_OF_CREEP = 3;
	private final int CREEPS_PER_WAVE = 10;
	private final int TOWER_SIZE = 50;
	private Thread creeps;
	
	private boolean bosswave = false;

	public static final long NANOS_PER_SEC = 1000000000;

	/**
	 * Constructor
	 */
	//	public GameModel() {
	//		// get begin time
	//		lastTime = System.nanoTime();
	//		gameNotOver = true;
	//		//TODO: Initialize the map, player
	//	}

	public SinglePlayer(String name, Vector<Path> paths,
			int width, int height) {
//		lastTime = System.nanoTime();
		gameNotOver = true;
		setPlayer(new Player(name));
		map = new SingleMap(paths, width, height);
		PointConverter.setTowerSize(TOWER_SIZE);
	}
	
	public SinglePlayer(String name, GameMap gm) {
		this(name, gm.getPaths(), gm.getBackground().getWidth(), gm.getBackground().getHeight());
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#run()
	 */
	@Override
	public void run() {
		//		double dt = computeDt();

		CreepCreater cc = new CreepCreater(
				this.MILLISECONDS_PAUSE_BETWEEN_CREEP_SPAWNS,
				this.TYPES_OF_CREEP, this.CREEPS_PER_WAVE, this); //Creep creator takes the time between creep spawns in seconds, types of creeps to spawn, and how many creeps of each type to spawn
		creeps = new Thread(cc);
		creeps.start();

		//		CreepMover cm = new CreepMover(theMap.getTowers(),
		//				theMap.getCreepers(), dt);
		//		Thread mover = new Thread(cm);
		//		mover.start();

	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#update(double)
	 */
	@Override
	public void update(double dt) {
		//		System.out.println("GameModel update");
		Vector<Displayable> things = map.getThings();
		//		System.out.println("Moving all " + creeps.size() + "creeps on map");
		for (int i = 0; i < things.size(); i++) {
			Displayable thing = things.get(i);
			thing.update(map, dt);
		}
		if(player.getLife() <= 0)
			lose();
		//		System.out.println("GameModel repaint");

//		svs.repaint();
	}

//	public ScrollViewScreen getGUI() {
//		return svs;
//	}

	// methods
//	/**
//	 * Computes the delta t to be used in the move and animation methods.
//	 * 
//	 * @return
//	 */
//	private double computeDt() {
//		// get current time
//		long currentTime = System.nanoTime();
//		// compute the dt
//		double dt = (double) (currentTime - lastTime) / (double) NANOS_PER_SEC;
//		// update previousTime for use in the next iteration
//		lastTime = currentTime;
//
//		return dt;
//	}
	

	@Override
	public void win() {
		gameNotOver = false;
		map.win();
		endGame();
		setChanged();
		notifyObservers();
		
	}

	public void lose() {
		//game over
		System.out.println("SPG over");
		gameNotOver = false;
		map.lose();
		endGame();
		setChanged();
		notifyObservers();
	}
	
	public void endGame() {
//		creeps.interrupt();
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#getTheMap()
	 */
	@Override
	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#addCreep(model.Creep)
	 */
	@Override
	public void addCreep(Creep creep) { //this should never be called
		map.addCreep(creep);
	}
	
	public Point getCenter(Point pt) {
		Point center = new Point(pt.x + TOWER_SIZE / 2, pt.y + TOWER_SIZE / 2);
		return center;
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#addTower(model.Tower)
	 */
	@Override
	public boolean addTower(Tower tower) {
		//find the center of the tower
		Point center = tower.getPoint();
		System.out.println("Tower to place at " + center);
		System.out.println("Point was at " + tower.getPoint());
		
		//check path
		Vector<Path> paths = map.getPaths();
		for(int i=0;i<paths.size();i++) {
			Vector<Point> points = paths.get(i).path;
			for(int j=0;j<points.size() -1;j++) {
				Point one = points.get(j);
				Point two = points.get(j+1);
				//find the least acceptable area of a tower near the path
				/*
				 * A tower 
				 */
				double distance = one.distance(two);
				double minArea = distance * TOWER_SIZE / 2.0;
				if(minArea > area(one,two,center) && one.distance(center) < distance + TOWER_SIZE && two.distance(center) < distance +TOWER_SIZE ) {
					System.out.println("Center point: (" +center.x+", "+center.y+") is too close to endpoints (" +one.x+", "+one.y+") and (" +two.x+", "+two.y+")");
					System.out.println("Area was: " + area(one,two,center));
					System.out.println("Cannot be closer than " + TOWER_SIZE + " units");
					return false;
				}
			}
			 
		}
		//at this point the tower is not in conflict with any paths.
		//check other towers
		Vector<Tower> towers = map.getTowersWithinRadiusOf(center, (int) (TOWER_SIZE / 1.5));
		if(!towers.isEmpty())
			return false;
		map.addTower(tower);
		return true;
	}
	
	private double area(Point a, Point b, Point c) {
		double area = Math.abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2.0;
		return area;
	}

	public void addProjectile(Projectile projectile) { //this should never be called
		map.addProjectile(projectile);
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#removeCreep(model.Creep)
	 */
	@Override
	public void removeCreep(Creep creep) {
		map.removeCreep(creep);
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#removeTower(model.Tower)
	 */
	@Override
	public void removeTower(Tower tower) {
		map.removeTower(tower);
	}

	/* (non-Javadoc)
	 * @see model.TowerDefenseModel#removeProjectile(projectile.Projectile)
	 */
	@Override
	public void removeProjectile(Projectile projectile) {
		map.removeProjectile(projectile);
	}
	
	public Unit getUnitAtPoint(Point pt) {
		Vector<Unit> units = map.getUnitsWithinRadiusOf(pt, (int) (TOWER_SIZE * Math.sqrt(2)) / 2);
		if(units.isEmpty())
			return null;
		else
			return units.get(0);
	}
	/*
	 * If this stops working, it's because the point is the point ont he ImmuneResponse JFrame; while you need the point on the JPanel
	 */
	public String getUnitInfoAtPoint(Point pt) {
//		Point adjusted = PointConverter.MoveToCorner(pt);
		pt = ScrollViewScreen.adjustPointToScreen(pt);
		System.out.println("Searching around " + pt);
		System.out.println("Searching for units within " + (int) (TOWER_SIZE * Math.sqrt(2)) / 2 + " units");
		Vector<Unit> units = map.getUnitsWithinRadiusOf(pt, (int) (TOWER_SIZE * Math.sqrt(2)) / 2);
		System.out.println("Found " + units.size() + " units");
		if(units.isEmpty())
			return null;
		else
			return units.get(0).getInformation();
	}
	
	

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}



	private class CreepCreater implements Runnable {
		private int timeBetweenCreepSpawnsInMilliseconds;
		private int timeBetweenWavesInMilliseconds = 5000;
		private int typesOfCreep;
		private int creepsInEachWave;
		private SinglePlayer game;
		private int creepsSpawned = 0;

		public CreepCreater(int time, int types, int creeps, SinglePlayer game) {
			this.timeBetweenCreepSpawnsInMilliseconds = time;
			this.typesOfCreep = types;
			this.creepsInEachWave = creeps;
			this.game = game;
		}

		@Override
		//TODO: complete the multiple different 
		public void run() {
			int wave = 0;
			if(bosswave) {
				Creep creep = new Zoidberg(getMap().getPaths(0), player, 0, game, 0);
				map.addCreep(creep);
			}
			while (gameNotOver) {
				//				System.out.println("Creating creeps");
				Creep creep;
				for (int i = 0; i < creepsInEachWave; i++) {
					if (wave % typesOfCreep == 0) {
						creep = new Influenza(getMap().getPaths(0), getPlayer(),
								wave / typesOfCreep, creepsSpawned++);
					} else if (wave % typesOfCreep == 1) {
						creep = new Salmonella(getMap().getPaths(0), getPlayer(),
								wave / typesOfCreep, creepsSpawned++);
					} else
						creep = new Strep(getMap().getPaths(0), getPlayer(), wave
								/ typesOfCreep, creepsSpawned++);
//					System.out.println("Creep added to map");
					getMap().addCreep(creep);
					//					System.out.println(getTheMap().getCreepers().size());
					//					svs.drawThing(creep);

					try {
						Thread.sleep(timeBetweenCreepSpawnsInMilliseconds);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				wave++;
				try {
					Thread.sleep(timeBetweenWavesInMilliseconds);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public GameMap getGameMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMultiplayer() {
		return false;
	}

	@Override
	public int getTeamID() {
		return 0;
	}

	@Override
	public Player getEnemyPlayer() throws Exception {
		throw new Exception("This should not be called in SinglePlayer.");
	}


}
