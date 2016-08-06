package creep;




import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Creep;
import model.Path;
import model.Player;

import components.AttackComponent;
import components.CreepAnimation;


public class Strep extends Creep  {
	
	private static int baseHealth = 100;
	private static int healthPerLevel = 5;
	private static int baseArmor = 3;
	private static int armorPerLevel = 1;
	private static int baseBounty = 25;
	private static int bountyPerLevel = 5;
//	private static int attackDamage = 0;
	private static int movespeed = 10;
	private static int damageToBase = 1;
//	private static int projectileSpeed = 65;
//	private static String projectileImage = null;
	private static String unitImageName = "RhinovirusSheet.gif";

	public Strep(Path path, Player enemy, int level, Point point, int id) { //all the fields are static so if something goes wrong check this first
		super();
		if(point.equals(path.getNextPoint(pointsPassed)))
			pointsPassed++;
		this.path = path;
		this.enemy = enemy;
		this.money = baseBounty + bountyPerLevel * level;
		this.speed = movespeed;
		super.damageToBase = damageToBase;
		this.x = point.x;
		this.y = point.y;
		this.id = id;
		try {
			unitImage = ImageIO.read(new File("images" + File.separator
					+ unitImageName));
		} catch (IOException e) {
			System.out.println("Image not found");
		}
		this.animationComponent = new CreepAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
		this.attackComponent = new AttackComponent();
		
		armor = baseArmor + armorPerLevel * level;
		health = baseHealth + healthPerLevel * level;
		
	}
	
	public Strep (Path path, Player enemy, int level, int id) { //all the fields are static so if something goes wrong check this first
		super();
		Point point = path.getNextPoint(0);
		pointsPassed++;
		this.path = path;
		this.enemy = enemy;
		this.money = baseBounty + bountyPerLevel * level;
		this.speed = movespeed;
		super.damageToBase = damageToBase;
		this.x = point.x;
		this.y = point.y;
		this.id = id;
		try {
			unitImage = ImageIO.read(new File("images" + File.separator
					+ unitImageName));
		} catch (IOException e) {

		}
		this.attackComponent = new AttackComponent();
		this.animationComponent = new CreepAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
		armor = baseArmor + armorPerLevel * level;
		health = baseHealth + healthPerLevel * level;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Streptococcus";
	}

	@Override
	public CreepSelection creepType() {
		return CreepSelection.Strep;
	}

}