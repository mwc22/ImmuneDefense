package projectile;

import model.MapModel;
import model.Unit;

public interface OnHitEffect {
	
	public void infect(Unit target, MapModel map);

}
