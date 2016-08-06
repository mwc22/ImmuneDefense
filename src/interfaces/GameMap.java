package interfaces;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Vector;

import model.Path;

public interface GameMap extends Serializable {

	public BufferedImage getBackground();
	public Vector<Path> getPaths();
	public Path getPath(int index);
	public int getStartingGold();
	public int getStartingLife();
}
