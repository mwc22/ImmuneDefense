package model;

import java.awt.Point;

/**
 * Allows for easy conversion of points from the center of the target to the to the corner, and vice versa
 * @author Patrick
 *
 */
public class PointConverter {
	
	private static int TOWER_SIZE;
	
	public static void setTowerSize(int towersize) {
		TOWER_SIZE = towersize;
	}
	
	public static Point MoveToCenter(Point pt) {
		Point center = new Point(pt.x + TOWER_SIZE/2, pt.y + TOWER_SIZE/2);
		return center;
	}
	
	public static Point MoveToCorner(Point pt) {
		Point corner = new Point(pt.x - TOWER_SIZE/2, pt.y - TOWER_SIZE/2);
		return corner;
	}
	
	public static Point MovePointSE(Point pt, int units) {
		Point move = new Point(pt.x + units, pt.y + units );
		return move;
	}
	
	public static Point MovePointNW(Point pt, int units) {
		Point move = new Point(pt.x - units, pt.y - units );
		return move;
	}
	public static Point MovePointOffset(Point pt, Point offset) {
		Point move = new Point(pt.x - offset.x, pt.y - offset.y);
		return move;
	}
	public static Point MovePointWithOffset(Point pt, Point offset) {
		Point move = new Point(pt.x + offset.x, pt.y + offset.y);
		return move;
	}	

}
