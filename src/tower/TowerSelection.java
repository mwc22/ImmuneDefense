package tower;

import java.awt.Point;

import model.Tower;

public enum TowerSelection {
	Antibody, Macrophage, NonHoming;
	
	public static Tower getTower(
			TowerSelection towerType,
			Point center,
			int level,
			int id) throws Exception {
		switch(towerType) {
		case Antibody:
			return new Antibody(center, id);
		case Macrophage:
			return new Macrophage(center, id);
		default:
			throw new Exception("towerType was invalid.");
		}
	}
}
