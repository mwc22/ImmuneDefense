package gamemaps;

import java.awt.Point;
import interfaces.GameMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import model.Path;

public class Map2 implements GameMap {

	private BufferedImage background;
	private Vector<Path> paths = new Vector<Path>();
	
	public Map2() {
		paths.add(makePath1());
		paths.add(makePath2());
		try {
			background = ImageIO.read(new File("images" + File.separator
					+ "Map2.gif"));
			//backgroundGraphics = backgroundImage.createGraphics().create();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Override
	public BufferedImage getBackground() {
		// TODO Auto-generated method stub
		return background;
	}

	@Override
	public Vector<Path> getPaths() {
		// TODO Auto-generated method stub
		return paths;
	}

	@Override
	public Path getPath(int index) {
		// TODO Auto-generated method stub
		return paths.get(index);
	}
	
	private Path makePath1(){
		Vector<Point> path1 = new Vector<Point>();
		// add points
		path1.add(new Point(30, 160));
		path1.add(new Point(464, 160));
		path1.add(new Point(464, 302));
		path1.add(new Point(245, 302));
		path1.add(new Point(245, 490));
		path1.add(new Point(690, 490));
		path1.add(new Point(690, 291));
		path1.add(new Point(820, 291));
		
		return new Path(path1);
	}
	
	private Path makePath2(){
		Vector<Point> path2 = new Vector<Point>();
		// add points
		path2.add(new Point(236, 690));
		path2.add(new Point(532, 690));
		path2.add(new Point(532, 490));
		path2.add(new Point(690, 490));
		path2.add(new Point(690, 291));
		path2.add(new Point(820, 291));
		
		return new Path(path2);
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
