package creep;

import java.awt.Point;
import java.util.Vector;

import model.Creep;
import model.MapModel;

public class NullCreep extends Creep {
	private Point point;
	private MapModel map;
	private int radius;

	public NullCreep (Point pt, MapModel map, int radius) { //all the fields are static so if something goes wrong check this first
		super();
		this.map = map;
		this.radius = radius;
	}
	
	public void strike(int damage) {
		Vector<Creep> nearby = map.getCreepsWithinRadiusOf(point, radius);
		if(!nearby.isEmpty())
			nearby.get(0).strike(damage);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Null Creep";
	}

	@Override
	public CreepSelection creepType() {
		return CreepSelection.NullCreep;
	}

}
