package gamemaps;

import interfaces.GameMap;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import model.Path;

public class Map1 implements GameMap {
	private BufferedImage background;
	private Vector<Path> paths = new Vector<Path>();
	
	public Map1() {
		paths.add(makePath1());
		try {
			background = ImageIO.read(new File("images" + File.separator
					+ "Map1.gif"));
			//backgroundGraphics = backgroundImage.createGraphics().create();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	private Path makePath1(){
		Vector<Point> path1 = new Vector<Point>();
		// add points
		path1.add(new Point(53, 875));
		path1.add(new Point(53, 642));
		path1.add(new Point(555, 642));
		path1.add(new Point(555, 404));
		path1.add(new Point(136, 404));
		path1.add(new Point(136, 144));
		path1.add(new Point(758, 144));
		path1.add(new Point(758, 412));
		path1.add(new Point(1218, 412));
		path1.add(new Point(1218, 194));
		path1.add(new Point(990, 194));
		
		
		return new Path(path1);
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
	@Override
	public int getStartingGold() {
		return 500;
	}
	@Override
	public int getStartingLife() {
		return 100;
	}

}
