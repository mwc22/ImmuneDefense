package projectile;

import model.MapModel;
import model.Unit;

public class ArmorShred implements OnHitEffect {

	
	public void infect(Unit target, MapModel map) {
		int armor = target.getArmor();
		if(armor > 0) 
			armor--;
		target.setArmor(armor);
		
	}

}
