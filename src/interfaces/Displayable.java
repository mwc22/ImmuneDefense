package interfaces;

import java.awt.Point;
import java.awt.image.BufferedImage;

import model.Block;
import model.MapModel;

public interface Displayable {

	public Point getPoint();
	public boolean isInstanceOf(String displayable);
	public BufferedImage getImage();
	public void setCurrentBlock(Block block);
	public Block getCurrentBlock();
	public void update(MapModel map, double dt);
	public BufferedImage animate();
	
}
