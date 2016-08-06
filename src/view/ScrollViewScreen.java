package view;

//imports

import interfaces.Displayable;
import interfaces.GameMap;
import interfaces.TowerDefenseModel;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.MapModel;
import model.PointConverter;
import model.Tower;
import model.Unit;
import tower.Antibody;
import tower.Macrophage;

/**
 * Constructs how the game works
 * 
 * @author Bhavana Gorti
 * @author Travis Woodrow
 * @author Michael Curtis
 * @author Patrick Martin
 * 
 */

public class ScrollViewScreen extends JPanel {

	// vars
	private static final long serialVersionUID = 1L;
	private JScrollPane scroller;
	private volatile static Icon icon = new Icony();
	private volatile static JLabel theLabel = new JLabel(icon);
	private volatile static BufferedImage backgroundImage;

	public static TexturePaint theTexture;
	private volatile Graphics backgroundGraphics;
	private TowerDefenseModel game;
	private volatile static Point upperLeft = new Point(0, 0);

	private Ellipse2D.Double shape;
	public static GameMap gameMap;

	// constructor
	/**
	 * Creates a TowerDefenseModel game
	 * @param gm
	 */
	public ScrollViewScreen(TowerDefenseModel theGame) {
		gameMap = theGame.getGameMap();
		game = theGame;
		ScrollViewScreen.backgroundImage =	gameMap.getBackground();
		makeScroller();
	}
	
	public BufferedImage getImage() {
		return getTexture().getImage();
	}
	
	public void setBasePoint(Point pt) {
		upperLeft = pt;
	}

	public void setUpTest() {
//		Path path = Path.makeTestPath2();
		//		for(Point pt : path.path)
		//		System.out.println(path);

//		Vector<Path> paths = new Vector<Path>();
//		paths.add(path);

		MapModel map = getGame().getMap();
		//		redrawEverything();
		Thread gameThread = new Thread(getGame());
		gameThread.start();
	}

	public BufferedImage getBackgroundImage() {
		return backgroundImage;
	}

	public void redrawEverything(Graphics2D gr) {

		//projectiles left as an exercise for the reader TODO

		//		this.repaint();
	}

	public void endGame() {

	}

	public void paintComponent(Graphics g) {
		//		long dur = System.nanoTime();
		super.paintComponent(g);
		//		System.out.println("PaintComponent called");
		Graphics2D gr = (Graphics2D) g;
		if (getGame() == null) {
			return;
		}
//		System.out.println();
//		System.out.println(this.backgroundImage.getHeight());
//		System.out.println(this.getHeight() + " " +  upperLeft.y);
		gr.drawImage(
				getTexture().getImage().getSubimage(upperLeft.x, 
						upperLeft.y,
						this.getWidth(),
						this.getHeight()), 0, 0, null);
		//start with the background image, add creeps, towers, projectiles but not really that last one
		//		System.out.println("resetting");
		MapModel map = game.getMap();

		Vector<Displayable> things = map.getThings();
		for (int i = 0; i < things.size(); i++) {
			drawThing(things.get(i), gr);
		}
		if (shape != null) {
			Point pt = new Point((int) shape.x, (int) shape.y);
			pt = PointConverter.MovePointOffset(pt, upperLeft);
			Ellipse2D.Double tempShape = new Ellipse2D.Double(pt.x, pt.y, shape.width, shape.height);
			gr.draw(tempShape);
		}

		//		remakeScroller();
		//		System.out.println(1000*((double)(System.nanoTime() - dur) / (double)SinglePlayer.NANOS_PER_SEC) + " milliseconds");

	}

	public void drawThing(Displayable thing, Graphics2D g2d) { 
		try {
			BufferedImage img1 = thing.animate();
			Point corner = PointConverter.MoveToCorner(thing.getPoint());
			corner = PointConverter.MovePointOffset(corner, upperLeft);
			g2d.drawImage(img1, null, corner.x, corner.y);

		} catch (Exception e) {
			//			System.out.println("Is thing null? " + (thing == null));
			//			System.out.println(thing.getClass());
			//			System.out.println("Is thing a Projectile? " + thing.isInstanceOf("Projectile"));
			//			System.out.println("Is thing a Unit? " + thing.isInstanceOf("Unit"));
			//			System.out.println("Is thing a Creep? " + thing.isInstanceOf("Creep"));
			//			System.out.println("Is thing a Tower? " + thing.isInstanceOf("Tower"));
			e.printStackTrace();
		}
	}


	public String hover(Point pt) {
//		Point adjusted = ScrollViewScreen.adjustPointToScreen(pt);
		Unit unit = game.getUnitAtPoint(pt);
		if (unit == null) {
			shape = null;
			return null;
		}
		if (unit.getAttackRange() > 0) {
			Point unitPoint = PointConverter.MovePointNW(unit.getPoint(), 0);
			shape = new Ellipse2D.Double();
			shape.x = unitPoint.x - unit.getAttackRange();
			shape.y = unitPoint.y - unit.getAttackRange();
			shape.height = 2 * unit.getAttackRange();
			shape.width = 2 * unit.getAttackRange();
		} else
			shape = null;

		return unit.getInformation();
	}
	
	public void setUpperLeft(Point pt) {
		upperLeft = pt;
	}
	
	public static Point adjustPointToScreen(Point pt) {
		return PointConverter.MovePointWithOffset(pt, upperLeft);
	}

	public void makeScroller() {

		// load in image to use for background
		theTexture = new TexturePaint(backgroundImage, new Rectangle(
				backgroundImage.getWidth(), backgroundImage.getHeight()));
		// create label
		icon = new Icony();

		theLabel = new JLabel(icon);
		this.setBounds(10, 10, 640, 480);
	}

	public JScrollPane getScroller() {
		return scroller;
	}

	public TexturePaint getTexture() {
		return theTexture;
	}

	public TowerDefenseModel getGame() {
		return game;
	}

	public void setGame(TowerDefenseModel game) {
		this.game = game;
	}	

	private static class Icony implements Icon {
		TexturePaint TEXTURE = theTexture;

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setPaint(TEXTURE);
			g2.fillRect(x, y, c.getWidth(), c.getHeight());
			g2.dispose();
		}

		@Override
		public int getIconWidth() {
			return backgroundImage.getWidth();
		}

		@Override
		public int getIconHeight() {
			return backgroundImage.getHeight();
		}
	};
}
