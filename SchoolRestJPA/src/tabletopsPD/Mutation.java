package tabletopsPD;

public class Mutation {

	public Mutation(Guest m, Guest d){
		moving = m;
		displaced = d;
	}
	
	private Guest moving;
	private Guest displaced;
	
	public Guest getMoving() {
		return moving;
	}

	public Guest getDisplaced() {
		return displaced;
	}
}
