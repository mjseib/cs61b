/* Animal.java */

/** 
 * The animals class defines animal type taking into account
 * possible future implementations of more animals than just 
 * fish and sharks.
 * @author awong
 *
 */
public class Animal {
	
	//1 is shark
	//2 is fish
	protected int aniType;
	
	public Animal(int i) {
		aniType = i;
	}
	
	public int getType() {
		return aniType;
	}
	
	public static void main(String[] argv) {
		Animal a1 = new Animal(1);
		
		System.out.println("This should be a1's animal 1: " + a1.getType());
	}
}
