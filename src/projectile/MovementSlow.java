package projectile;

import model.MapModel;
import model.Unit;

public class MovementSlow implements OnHitEffect {

	@Override
	public void infect(Unit target, MapModel map) {
		Status s = new MovementSlowStatus(3.8, 2);
		s.induceEffect(target);
		
	}

}
