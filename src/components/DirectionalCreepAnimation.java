package components;

import interfaces.AnimationComponent;

import java.awt.image.BufferedImage;

import model.Creep;
import model.Unit;

public class DirectionalCreepAnimation implements AnimationComponent {
	
	private BufferedImage image;
	private int spriteNum = 0, counter = 0, firstX = 0, firstY = 0, width = 50, height = 50, spacing = 50;
	private Creep creep;
	
	public DirectionalCreepAnimation(){
		
	}
	
	@Override
	public BufferedImage animate() {
		if (creep.getPath().hasNextPoint(creep.getPointsPassed())){
			int dx = creep.getPath().getNextPoint(creep.getPointsPassed()).x - creep.getPoint().x;
			int dy = creep.getPath().getNextPoint(creep.getPointsPassed()).y - creep.getPoint().y;
			double angle = Math.atan2(dy, dx);
			angle = (180 / Math.PI) * angle + 180;
			if(angle <= 45 || angle > 315)
				spriteNum = (((counter++) / 10) % 2);
			else if(angle <= 135 && angle > 45)
				spriteNum = (((counter++) / 10) % 2) + 2;
			else if(angle <= 225 && angle > 135)
				spriteNum = (((counter++) / 10) % 2) + 4;
			else if(angle <= 315 && angle > 225)
				spriteNum = (((counter++) / 10) % 2) + 6;
			
			return image.getSubimage(firstX + spriteNum * spacing,
					firstY, width, height);
		}
		else{
			return image.getSubimage(firstX + spriteNum * spacing,
					firstY, width, height);
		}
	}
	
	public void setImage(BufferedImage img){
		image = img;
	}

	@Override
	public void setUnit(Unit unit) {
		this.creep = (Creep) unit;
		
	}

	@Override
	public void attack() {
		// TODO When/If creeps attack make this work
		
	}
}
