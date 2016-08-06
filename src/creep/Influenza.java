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

public class Influenza extends Creep {
	private static int baseHealth = 40;
	private static int healthPerLevel = 4;
	private static int baseArmor = 4;
	private static int armorPerLevel = 2;
	private static int baseBounty = 28;
	private static int bountyPerLevel = 5;
	//	private static int attackDamage = 0;
	private static int movespeed = 30;
	private int damageToBase = 30;
	//	private static int projectileSpeed = 75;
	//	private static String projectileImage = null;
	private static String unitImageName = "HIVSheet.gif";

	public Influenza(Path path, Player enemy, int level, Point point, int id) { //all the fields are static so if something goes wrong check this first
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
		this.animationComponent = new CreepAnimation();
		this.animationComponent.setImage(unitImage);
		this.animationComponent.setUnit(this);
		armor = baseArmor + armorPerLevel * level;
		health = baseHealth + healthPerLevel * level;

	}

	public Influenza(Path path, Player enemy, int level, int id) { //all the fields are static so if something goes wrong check this first
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
		return "Influenza";
	}

	@Override
	public CreepSelection creepType() {
		return CreepSelection.Influenza;
	}

}
