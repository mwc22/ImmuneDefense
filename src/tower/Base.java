package tower;


import interfaces.AnimationComponent;
import interfaces.Displayable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;

import model.Block;
import model.MapModel;

import components.BaseAnimation;

public class Base  extends Observable implements Displayable{

	private String towerImage = "BaseCage.gif";
	private int health;
	private Point point;
	private BufferedImage image;
	private Block currentBlock;
	private AnimationComponent animationComponent;
	
	public Base(int health, Point point) {
		try {
			image = ImageIO.read(new File("images" + File.separator + towerImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.health = health;
		this.animationComponent = new BaseAnimation();
		((BaseAnimation) this.animationComponent).setImage(image);
		this.point = point;
		this.point.y -= 70;
		this.point.x -= 50;
	}
	
	public boolean strike(int damage) {
		health -= damage;
		if(health < 0)
			health = 0;
		return health > 0;
	}

	@Override
	public Point getPoint() {
		return point;
	}
 
	
	public boolean isInstanceOf(String displayable) {
		return displayable.equals("Base");
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void setCurrentBlock(Block block) {
		currentBlock = block;
	}

	@Override
	public Block getCurrentBlock() {
		return currentBlock;
	}

	@Override
	public void update(MapModel map, double dt) {
		// TODO This may actually need to be done
		
	}

	@Override
	public BufferedImage animate() {
		return animationComponent.animate();
		
	}


}
