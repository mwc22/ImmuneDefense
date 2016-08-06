package components;

import java.awt.image.BufferedImage;

import projectile.Projectile;

import model.Unit;

import interfaces.AnimationComponent;

public class ProjectileAnimation implements AnimationComponent {
	
	private Projectile projectile;
	private Unit target;
	private BufferedImage image;
	private int spriteNum = 0, firstX = 0, firstY = 0, width = 20, height = 20, spacing = 20;
	
	public ProjectileAnimation(Projectile proj){
		this.projectile = proj;
		this.target = proj.getTarget();
		
	}
	
	@Override
	public BufferedImage animate() {
		int dx = target.getPoint().x - projectile.getPoint().x;
		int dy = target.getPoint().y - projectile.getPoint().y;
		double angle = Math.atan2(dy, dx);
		angle = (180 / Math.PI) * angle + 180;
		if(angle <= 22.5 || angle > 337.5)
			spriteNum = 7;
		else if(angle <= 67.5 && angle > 22.5)
			spriteNum = 3;
		else if(angle <= 112.5 && angle > 67.5)
			spriteNum = 4;
		else if(angle <= 157.5 && angle > 112.5)
			spriteNum = 5;
		else if(angle <= 202.5 && angle > 157.5)
			spriteNum = 6;
		else if(angle <= 247.5 && angle > 202.5)
			spriteNum = 2;
		else if(angle <= 292.5 && angle > 247.5)
			spriteNum = 0;
		else if(angle <= 337.5 && angle > 292.5)
			spriteNum = 1;
		
		return image.getSubimage(firstX + spriteNum * spacing,
				firstY, width, height);
	}
	
	public void setImage(BufferedImage img){
		image = img;
	}

	public void setUnit(Unit unit) {
		this.target = unit;
		
	}

	@Override
	public void attack() {
		// TODO May or may not want to do something here.  Maybe have it trigger a splat or something on collision.
		
	}
}
