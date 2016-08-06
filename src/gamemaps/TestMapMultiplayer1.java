package gamemaps;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import model.Path;

import interfaces.GameMap;

public class TestMapMultiplayer1 implements GameMap {

	private BufferedImage m_background;
	private Vector<Path> m_paths;
	
	public TestMapMultiplayer1() {
		// paths
		m_paths = makeDefault();		
		
		// image
		try {			
			m_background = ImageIO.read(new File("images" + File.separator
					+ "MultiplayerTest.png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Override
	public BufferedImage getBackground() {
		return m_background;
	}

	@Override
	public Vector<Path> getPaths() {
		return m_paths;
	}

	@Override
	public Path getPath(int index) {
		return m_paths.get(index);
	}
	
	/**
	 * Makes paths for this map
	 * @return
	 */
	private Vector<Path> makeDefault() {
		Vector<Point> pointsOne = new Vector<Point>();
		pointsOne.add(new Point(960, 250));
		pointsOne.add(new Point(25, 250));
		Path one = new Path(pointsOne);
		
		Vector<Point> pointsTwo = new Vector<Point>();
		pointsTwo.add(new Point(1040, 250));
		pointsTwo.add(new Point(1975, 250));
		Path two = new Path(pointsTwo);
		
		Vector<Path> paths = new Vector<Path>();
		paths.add(one);
		paths.add(two);
		
		return paths;
	}
	
	@Override
	public int getStartingGold() {
		return 500;
	}
	@Override
	public int getStartingLife() {
		return 100;
	}

}
