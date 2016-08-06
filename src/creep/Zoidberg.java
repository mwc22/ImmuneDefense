package creep;

import interfaces.TowerDefenseModel;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Creep;
import model.Path;
import model.Player;

import components.AttackComponent;
import components.CreepAnimation;

public class Zoidberg extends Creep {
	private static int baseHealth = 400;
	private static int baseArmor = 4;
	private static int baseBounty = 28;
	private static int movespeed = 20;
	private static String unitImageName = "RhinovirusSheet.gif";
	private TowerDefenseModel game;

	public Zoidberg (Path path, Player enemy, int level, Point point, TowerDefenseModel game, int id) { //all the fields are static so if something goes wrong check this first
		super();
		if(point.equals(path.getNextPoint(pointsPassed)))
			pointsPassed++;
		this.path = path;
		this.enemy = enemy;
		this.money = baseBounty;
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
		armor = baseArmor ;
		health = baseHealth;
		this.game = game;
		
	}
	public Zoidberg (Path path, Player enemy, int level, TowerDefenseModel game, int id) { //all the fields are static so if something goes wrong check this first
		super();
		Point point = path.getNextPoint(0);
		pointsPassed++;
		this.path = path;
		this.enemy = enemy;
		this.money = baseBounty;
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
		armor = baseArmor ;
		health = baseHealth ;
		this.game = game;
	}
	
	@Override
	public void die() {
		System.out.println("Unit died, notifying observers");
		//game.win(); When this is uncommented, make sure you change the null in CreepSelection
		setChanged();
		notifyObservers(false);
	}
	@Override
	public String getName() {
		return "Zoidberg";
	}
	@Override
	public CreepSelection creepType() {
		return CreepSelection.Zoidberg;
	}
}
