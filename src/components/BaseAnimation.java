package components;

import interfaces.AnimationComponent;

import java.awt.image.BufferedImage;

import model.Unit;

public class BaseAnimation implements AnimationComponent {
	
	private BufferedImage image;
	private int counter = 0, spriteNum = 0, firstX = 0, firstY = 0, width = 144, height = 192, spacing = 144;
	private Unit base;
	
	
	public BaseAnimation(){
		
	}
	
	@Override
	public BufferedImage animate() {
		spriteNum = (((counter++)) / 2) % 28;
		return image.getSubimage(firstX + spriteNum * spacing,
				firstY, width, height);
	}
	
	public void setImage(BufferedImage img){
		image = img;
	}

	@Override
	public void setUnit(Unit unit) {
		this.base = unit;
		
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}
}
