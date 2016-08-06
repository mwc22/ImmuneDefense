package components;

import interfaces.AnimationComponent;

import java.awt.image.BufferedImage;

import model.Unit;

public class CreepAnimation implements AnimationComponent {
	
	private BufferedImage image;
	private int spriteNum = 0, counter = 0, firstX = 0, firstY = 0, width = 50, height = 50, spacing = 50;
	private Unit creep;
	
	public CreepAnimation(){
		
	}
	
	@Override
	public BufferedImage animate() {
		spriteNum = (((counter++)) / 10) % 8;
		return image.getSubimage(firstX + spriteNum * spacing,
				firstY, width, height);
	}
	
	public void setImage(BufferedImage img){
		//System.out.println("Image set");
		image = img;
	}

	@Override
	public void setUnit(Unit unit) {
		this.creep = unit;
		
	}

	@Override
	public void attack() {
		// TODO When/If creeps attack make this work
		
	}
}
