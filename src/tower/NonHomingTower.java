package tower;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Tower;
import projectile.OnHitEffect;

import components.AttackComponent;

public class NonHomingTower extends Tower {

	
	private String projectileImage = "Pikachu.png";
	private String towerImage = "Ezreal.png";

	public NonHomingTower(Point point, int id) {
		super();
		
		this.x = point.x;
		this.y = point.y;
		this.id = id;
		try {
			unitImage = ImageIO.read(new File("images" + File.separator
					+ towerImage));
		} catch (IOException e) {
			//This occurs in the instantiation of a null creep

		}
		this.attackComponent = new AttackComponent(
				10, 						// attack damage
				100,			 			// attack range
				0.5, 						// atacks per second
				projectileImage,				 	// projectile image
				80); 		// projectile speed (end of AttackComponent constructor)
		this.animationComponent.setImage(getImage());
		armor = 5;
		health = 100;
		
	}
	
	public NonHomingTower(Point point, OnHitEffect ohe, int id) {
		super();
		
		this.x = point.x;
		this.y = point.y;
		this.id = id;
		try {
			unitImage = ImageIO.read(new File("images" + File.separator
					+ projectileImage));
		} catch (IOException e) {
			//This occurs in the instantiation of a null creep

		}
		this.attackComponent = new AttackComponent(
				10, 						// attack damage
				100,			 			// attack range
				0.5, 						// atacks per second
				towerImage,				 	// projectile image
				80,					 		// projectile speed 
				ohe);						// on hit effect (end of AttackComponent constructor)
		this.animationComponent.setImage(getImage());
		armor = 5;
		health = 100;
		
	}

	@Override
	public void upgrade(int range, double fireRate, int damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "Non-Homing Tower";
	}

	@Override
	public TowerSelection towerType() {
		return TowerSelection.NonHoming;
	}

}
