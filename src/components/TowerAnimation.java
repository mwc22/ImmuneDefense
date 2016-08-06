package components;

import interfaces.AnimationComponent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Tower;
import model.Unit;

public class TowerAnimation implements AnimationComponent {
	
	private BufferedImage image;
	private int spriteNum = 0, counter = 0, firstX = 0, firstY = 0, width = 50, height = 50, spacing = 50;
	private Unit tower;
	private boolean shot = false;
	
	public TowerAnimation() {
		//uhh nothing to do here just pretty please set my image before you do anything
	}
	
	public BufferedImage animate() {
		if (tower.getTarget() == null) {
			counter = 0;
			return image.getSubimage(firstX + 7*spacing, firstY, width, height);
		}
		else if (shot){
			spriteNum = (int) ((counter++)/2) % 7;
			if (spriteNum == 6){
				shot = false;
				counter = 0;
			}
			return image.getSubimage(firstX + spriteNum * spacing,
				firstY, width, height);
			
		}
		else{
			spriteNum = ((counter++) / 15)%2;
			return image.getSubimage(firstX + spriteNum * spacing,
					firstY, width, height);
		}
	}
	
	public void setImage(BufferedImage img){
		image = img;
		
	}
	
	public void setUnit(Unit unit){
		this.tower = unit;
	}

	@Override
	public void attack() {
		shot = true;
		counter = 0;
	}
}
