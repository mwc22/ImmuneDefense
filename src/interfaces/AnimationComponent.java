package interfaces;

import java.awt.image.BufferedImage;

import model.Unit;

/**
 * Used by objects which will be animated
 * @author Travis
 *
 */
public interface AnimationComponent {
	
	/**
	 * Causes the object to animate.
	 
	 */
	//TODO: Does this need to take an argument? Can it be handled by the implementing class?
	public abstract BufferedImage animate(); 
	public abstract void setImage(BufferedImage img);
	void setUnit(Unit unit);
	void attack();
}
