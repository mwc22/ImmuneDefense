package creep;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Creep;
import model.Path;
import model.Player;

import components.AttackComponent;
import components.DirectionalCreepAnimation;

public class Salmonella extends Creep {

	private static int baseHealth = 70;
	private static int healthPerLevel = 8;
	private static int baseArmor = 2;
	private static int armorPerLevel = 1;
	private static int baseBounty = 30;
	private static int bountyPerLevel = 6;
	//	private static int attackDamage = 0;
	private static int movespeed = 25;
	private static int damageToBase = 25;
	//	private static int projectileSpeed = 90;
	//	private static String projectileImage = null;
	private static String unitImageName = "BacteriaSheet.gif";

	public Salmonella(Path path, Player enemy, int level, Point point, int id) { //all the fields are static so if something goes wrong check this first
		super();
		if (point.equals(path.getNextPoint(pointsPassed)))
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
		this.animationComponent = new DirectionalCreepAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
		armor = baseArmor + armorPerLevel * level;
		health = baseHealth + healthPerLevel * level;

	}

	public Salmonella(Path path, Player enemy, int level, int id) { //all the fields are static so if something goes wrong check this first
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
		this.animationComponent = new DirectionalCreepAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
		armor = baseArmor + armorPerLevel * level;
		health = baseHealth + healthPerLevel * level;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Salmonella";
	}

	@Override
	public CreepSelection creepType() {
		return CreepSelection.Salmonella;
	}

}
