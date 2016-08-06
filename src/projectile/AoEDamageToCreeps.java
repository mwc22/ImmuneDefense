package projectile;

import java.util.Vector;

import model.Creep;
import model.MapModel;
import model.Unit;





public class AoEDamageToCreeps implements OnHitEffect{

	private int radius;
	private int damage;
	
	public AoEDamageToCreeps(int radius, int damage) {
		this.radius = radius;
		this.damage = damage;
	}
	
	@Override
	public void infect(Unit target, MapModel map) {
		Vector<Creep> targets = map.getCreepsWithinRadiusOf(target.getPoint(), radius);
		targets.remove(target);
		for(Creep creep : targets) {
			creep.strike(damage);
		}
		
	}

	
}
