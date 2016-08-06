package projectile;

import interfaces.AnimationComponent;
import interfaces.Displayable;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;

import model.Block;
import model.MapModel;
import model.PointConverter;
import model.Unit;

import components.ProjectileAnimation;

/**
 * Controls the "bullets" that towers will use to attack Creeps
 * 
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Travis Woodrow
 * @author Michael Curtis
 * 
 */
public class Projectile extends Observable implements Displayable {


	private int damage;
//	private String imageFileName; //used in animation
	private Unit target;
	private double x;
	private double y;
	private int projectileSpeed; //used in animation
	private BufferedImage projectileImage;
	private OnHitEffect ohe;
	private AnimationComponent animationComponent;
	private MapModel map;
	private Block currentBlock;

	/**
	 * Projectile Constructor
	 * 
	 * @param damage
	 *            : The amount of damage each bullet has
	 * @param image
	 *            : The image of that tower type
	 * @param target
	 *            : the Creep object
	 * @param point
	 *            : the initial point of the projectile
	 */
	public Projectile(int damage, Unit target, Point point,
			String imageFileName, int projectileSpeed, MapModel map) {
		this.constructor(damage, target, point, imageFileName, projectileSpeed, map);
	}

	public Projectile(int damage, Unit target, Point point,
			String imageFileName, int projectileSpeed, MapModel map, OnHitEffect ohe) {
		this.constructor(damage, target, point, imageFileName, projectileSpeed, map); //I don't know if this is allowed, so if something goes wrong check here
		this.ohe = ohe;
	}
	
	private void constructor(int damage, Unit target, Point point,
			String imageFileName, int projectileSpeed, MapModel map) {
//		System.out.println("Projectile created");
		this.damage = damage;
		this.target = target;
		this.x = point.x;
		this.y = point.y;
		//		this.imageFileName = imageFileName;
		this.projectileSpeed = projectileSpeed;
		
		try { 
			projectileImage = ImageIO.read(new File("images" + File.separator
					+ imageFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.animationComponent = new ProjectileAnimation(this);
		((ProjectileAnimation) animationComponent).setImage(projectileImage);
	}
	
	

	public void strike(MapModel map) {
		//TODO: This needs to be updated to reference an attack component
//		System.out.println("Striking target");
		if(target.isAlive()) {
		target.strike(damage);
		if (ohe != null)
			ohe.infect(target, map);
		}
		setChanged();
		notifyObservers(false);
		
	}

	public Point getPoint() {
		return new Point((int) x, (int) y);
	}

	public void movePoint(double dx, double dy) {
		x += dx;
		y += dy;
	}

	public BufferedImage getImage() {
		
		return projectileImage;
	}

	public void update(MapModel map, double dt) {
		Point currentPoint = PointConverter.MovePointSE(getPoint(), 10);

		double distance = dt * projectileSpeed;

		
		Point nextPoint = PointConverter.MoveToCenter(target.getPoint());
		//		System.out.println("Points passed: "+pointsPassed);
		//		System.out.println("Next point: " + path.getNextPoint(pointsPassed));

		int dx = nextPoint.x - currentPoint.x;
		int dy = nextPoint.y - currentPoint.y;

		//		System.out.println("DX: " + dx);
		//		System.out.println("DY: " + dy);

		double distanceTarget = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		double angle = Math.atan2(dy, dx);
		//		System.out.println("Angle: " + angle);
		//		System.out.println("Distance to Target: " + distanceTarget);
		//		System.out.println("Distance travel: " + distance);
		if (distance < distanceTarget) {
			//			System.out.println("If yes");
			double dxCreep = Math.cos(angle) * distance;
			//			System.out.println("dxCreep: " + dxCreep);
			double dyCreep = Math.sin(angle) * distance;
			//			System.out.println("dyCreep: " + dyCreep);
			//			dxCreep += currentPoint.x;
			//			dyCreep += currentPoint.y;

			movePoint(dxCreep, dyCreep);
//			System.out.println("Projectile moved, " + distanceTarget + " left to go");
		} else {
			//			System.out.println("If no");
			strike(map);
		}
		
//		animate();
		setChanged();
		notifyObservers(true);
		
		
	}

	@Override
	public boolean isInstanceOf(String displayable) {
		return displayable.equals("Projectile");
	}

	@Override
	public void setCurrentBlock(Block block) {
		this.currentBlock = block;
	}

	@Override
	public Block getCurrentBlock() {
		return currentBlock;
	}

	@Override
	public BufferedImage animate() {
		try{
		return animationComponent.animate();
		} catch (Exception e) {
			return projectileImage;
		}
	}

	public Unit getTarget() {
		return target;
	}
}
