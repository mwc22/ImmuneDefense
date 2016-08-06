package builders;

import interfaces.TowerDefenseModel;
import java.awt.Point;

import server.Server;
import tower.Antibody;

public class AntibodyBuilder extends TowerBuilder{

	public AntibodyBuilder(TowerDefenseModel game) {
		super(game);
	}

	@Override
	public boolean buildTower(Point p) {
		return gameRef.addTower(new Antibody(p, 0));		
	}
}
