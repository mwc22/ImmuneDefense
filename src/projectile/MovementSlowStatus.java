package projectile;

import model.Unit;

public class MovementSlowStatus implements Status {
//	private int originalSpeed;
	private final double DURATION_IN_SECONDS;
	private final double REDUCTION_PERCENTAGE;
	private long START_TIME;
	private Unit target; //I hope this doesn't need to be here
	
	public MovementSlowStatus(double duration, double slowAmount) {
		DURATION_IN_SECONDS = duration;
		REDUCTION_PERCENTAGE = slowAmount;
	}
	
	public void induceEffect(Unit target) {
		this.target = target;
		int speed = target.getMoveSpeed();
		START_TIME = System.nanoTime();
		speed *= REDUCTION_PERCENTAGE;
		if(speed < 1)
			speed = 1;
		target.addStatus(this);
		target.setMoveSpeed(speed);
		
	}

	@Override
	public boolean update() {
		long now = System.nanoTime();
		if(now - START_TIME > DURATION_IN_SECONDS * 1000000000) {
			int speed = target.getMoveSpeed();
			target.setMoveSpeed((int) (speed / REDUCTION_PERCENTAGE));
			return false;
		} else {
			return true;
		}
		
	}
}
