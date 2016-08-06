package model;
import java.awt.Point;
import java.util.Vector;

/**
 * Constructs the Path on which the Creep will travel
 * 
 * TODO: Do we want the path to be more complex? Should it have access to the image?
 * How will we make sure towers are not blocking the path? Do we want to be able to
 * build a temporary wall that blocks creep movement?
 * 
 * @author Bhavana Gorti
 * @author Patrick Martin
 * @author Michael Curtis
 * @author Travis Woodrow
 * 
 */
public class Path {
	
	Vector<Point> path;
	
	public Path(Vector<Point> path)
	{
		this.path= path;	
	}
	public Point getNextPoint(int pointsPassed)
	{
		return path.get(pointsPassed);
	}
	
	/**
	 * Used to see if at end of path
	 * @param pointsPassed
	 * @return
	 */
	public boolean hasNextPoint(int pointsPassed) {
		return pointsPassed < path.size();
	}
	
	/**
	 * Path used for debugging purposes.
	 * @return
	 */
	public static Path makeTestPath1(){
		
		Vector<Point> testPath = new Vector<Point>();
		// add points
		testPath.add(new Point(60, 1));
		testPath.add(new Point(60, 173));
		testPath.add(new Point(436, 173));
		testPath.add(new Point(436, 323));
		testPath.add(new Point(261, 323));
		
		return new Path(testPath);
	}
	
	public static Path makeTestPath2() {
		Vector<Point> testPath = new Vector<Point>();
		// add points
		testPath.add(new Point(219,0));
		testPath.add(new Point(219, 424));
		testPath.add(new Point(658, 420));
		testPath.add(new Point(661, 95));
		testPath.add(new Point(412, 95));
		testPath.add(new Point(412, 519));
		testPath.add(new Point(802,519));
		testPath.add(new Point(533,66));
		testPath.add(new Point(347, 284));
		testPath.add(new Point(888, 284));
		
		
		
		return new Path(testPath);
	}
	
	public Point getLast() {
		return path.get(path.size()-1);
	}
}
