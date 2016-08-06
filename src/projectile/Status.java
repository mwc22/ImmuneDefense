package projectile;

import model.Unit;

public interface Status {

	public void induceEffect(Unit unit);
	public boolean update();
	
	
}
