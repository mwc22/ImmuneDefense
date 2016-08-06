package builders;

import model.MultiPlayer;
import model.MultiPlayer2;
import creep.Influenza;

public class InfluenzaBuilder extends CreepBuilder {

	public InfluenzaBuilder(MultiPlayer2 game) {
		super(game);
	}

	@Override
	public void spawnCreep() {
		gameRef.addCreep(new Influenza(
				gameRef.getGameMap().getPath(0),
				gameRef.getPlayer(),
				0,
				gameRef.getEnemyId()));
	}
}