package tower;



import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Tower;
import projectile.AoEDamageToCreeps;
import projectile.OnHitEffect;

import components.AttackComponent;
import components.TowerAnimation;

public class Macrophage extends Tower {
	
	private String projectileImage = "MacrophageProjectile.gif";
	private String towerImage = "MacrophageTowerSheet.gif";
	private OnHitEffect ohe = new AoEDamageToCreeps(30, 40);

	/**
	 * Constructs new Macrophage.
	 * @param point the tower's center point
	 * @param id the tower's ID
	 */
	public Macrophage(Point point, int id) {
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
		return "Macrophage Tower";
	}

	@Override
	public TowerSelection towerType() {
		return TowerSelection.Macrophage;
	}
}
