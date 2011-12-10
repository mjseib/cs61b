
public class Shark extends Animal{

	protected int timeUntilDeath;
	
	public Shark(int starveTime) {
		super(1);
		timeUntilDeath = starveTime;
	}
	
	public void dyingShark() {
		timeUntilDeath--;
	}
	
	public int dyingTime() {
		return timeUntilDeath;
	}
	
	public static void main(String[] argv) {
		Shark s1 = new Shark(3);
		
		System.out.println("This is s1s animal 2: "+s1.getType()+" and should be the time: "+ s1.dyingTime());
		s1.dyingShark();
		System.out.println("This is s1s time after starving one turn: "+s1.dyingTime());		
	}
}
