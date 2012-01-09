/* Position.java */

package player;

public class Position {

	protected int x;
	protected int y;
	
	/**
	 * constructor
	 * @param x
	 * @param y
	 */
	
	public Position(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public Position() {
		x=0;
		y=0;
	}

	/**
	 * self explanatory
	 * @return the x position
	 */
	
	public int getX() {
		return x;
	}
	
	/**
	 * self explanatory
	 * @return the y position
	 */
	
	public int getY() {
		return y;
	}
}
