/* Board.java */

package player;

import list.*;

public class Board {
    protected final static int DIMENSION = 8;
    public final static int EMPTY = 0;
    public final static int BLACK = 1;
    public final static int WHITE = 2;

    private int blackChips;
    private int whiteChips;
    private int[][] grid = new int[DIMENSION][DIMENSION];

    /**
     * Board() constructs an empty game board
     */

    public Board() {
    	for(int i=0; i<DIMENSION; i++) {
    		for(int j=0; j<DIMENSION; j++) {
    			grid[i][j] = EMPTY;
    		}
    	}
    }
	

    /**
     * numberOfChips() returns the number of total chips
     * @return the total number of chips
     */

    public int numberOfChips() {
    	return blackChips + whiteChips;
    }

    /**
     * numberOfChips(int color) returns the number of chips for that clor
     * @return the number of chips for the color
     */
    
    public int numberOfChips(int color) {
    	if(color == WHITE) {
    		return whiteChips;
    	} else {
    		return blackChips;
    	}
    }

    /**
     * currentBoard() returns the current board.
     * @return the grid.
     */

    public int[][] currentBoard() {
	return grid;
    }
    
    /**
     * clone() clones the current game board into a new object
     * @return the clone to be able to manipulate
     */
    
    public Board clone() {
    	Board clones = new Board();
    	for(int i=0; i<DIMENSION; i++) {
    		for(int j=0; j<DIMENSION; j++) {
    			clones.grid[i][j] = this.getContent(i, j);
    		}
    	}
    	return clones;
    }
    
    /**
     * getContent(int x, int y) to get what is in that square
     * @return color or empty
     */

    public int getContent(int x, int y) {
    	return grid[x][y];
    }
}