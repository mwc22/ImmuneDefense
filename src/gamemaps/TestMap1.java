package gamemaps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import model.Path;

import interfaces.GameMap;

public class TestMap1 implements GameMap {

	private BufferedImage background;
	private Vector<Path> paths = new Vector<Path>();
	
	public TestMap1() {
		paths.add(Path.makeTestPath1());
		try {
			background = ImageIO.read(new File("images" + File.separator
					+ "testPathImageBig.png"));
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
	
	@Override
	public int getStartingGold() {
		return 500;
	}
	@Override
	public int getStartingLife() {
		return 100;
	}

}
