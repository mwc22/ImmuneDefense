package tower;


import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Tower;
import projectile.MovementSlow;
import projectile.OnHitEffect;

import components.AttackComponent;
import components.TowerAnimation;

public class Antibody extends Tower {

//	private int baseHealth;
//	private int baseArmor;
//	private int attackDamage;
//	private double attacksPerSecond;
//	private int attackRange;
//	private String projectileImage;
//	private int projectileSpeed;
//	private OnHitEffect ohe;
//	private String towerImage;

	public Antibody(Point point, int id) {		
		this.x = point.x;
		this.y = point.y;
		this.id = id;
		try {
			unitImage = ImageIO.read(new File("images" + File.separator
					+ "AntibodyTowerSheet.gif"));
		} catch (IOException e) {
			//This occurs in the instantiation of a null creep

		}
		this.attackComponent = new AttackComponent(
				10, 						// attack damage
				100,			 			// attack range
				0.5, 						// atacks per second
				"AntibodyProjectile.gif", 	// projectile image
				80, 						// projectile speed 
				new MovementSlow()); 		// on hit effect (end of AttackComponent constructor)
		this.animationComponent = new TowerAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
		armor = 5;
		health = 100;
		
		
		
	}
	
	public Antibody(Point point, OnHitEffect ohe, int id) {
		this.x = point.x;
		this.y = point.y;
		this.id = id;
		try {
			unitImage = ImageIO.read(new File("images" + File.separator
					+ "AntibodyTowerSheet.gif"));
		} catch (IOException e) {
			//This occurs in the instantiation of a null creep

		}
		this.attackComponent = new AttackComponent(
				10, 						// attack damage
				100,			 			// attack range
				0.5, 						// atacks per second
				"AntibodyProjectile.gif", 	// projectile image
				80, 						// projectile speed 
				ohe); 		// on hit effect (end of AttackComponent constructor)
		this.animationComponent = new TowerAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
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
		return "Antibody Tower";
	}

	@Override
	public TowerSelection towerType() {
		return TowerSelection.Antibody;
	}

}
