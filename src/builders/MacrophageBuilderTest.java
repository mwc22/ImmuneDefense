package builders;

import interfaces.TowerDefenseModel;
import java.awt.Point;

import server.Server;
import tower.Macrophage;

public class MacrophageBuilderTest extends TowerBuilder{

	public MacrophageBuilderTest(TowerDefenseModel game) {
		super(game);
	}

	@Override
	public boolean buildTower(Point p) {
		return gameRef.addTower(new Macrophage(p, 0));		
	}
}
