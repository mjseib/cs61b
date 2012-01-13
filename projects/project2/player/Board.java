/* Board.java */

package player;

public class Board {
    public final static int DIMENSION = 8;
    public final static int BLACK = 0;
    public final static int WHITE = 1;
    public final static int EMPTY = 2;

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
     * numberOfChips(int color) returns the number of chips for that color
     * @return the number of chips for the color
     */
    
    public int numberOfChips(int color) {
    	if(color == WHITE) {
    		return whiteChips;
    	} else if(color == BLACK){
    		return blackChips;
    	} else {
    		return -1;
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
    	clones.whiteChips = whiteChips;
    	clones.blackChips = blackChips;
    	return clones;
    }
    
    /**
     * getContent(int x, int y) to get what is in that square
     * @return color or empty
     */

    public int getContent(int x, int y) {
    	return grid[x][y];
    }
    
    /**
     * sets the content at (x,y) to the color
     * @param x
     * @param y
     * @param color
     */
    
    public void setContent(int x, int y, int color) {
    	int currItem = grid[x][y];
    	if(color != EMPTY && currItem == EMPTY) {
    		if(color == BLACK) {
    			blackChips++;
    		} else if(color == WHITE) {
    			whiteChips++;
    		}
    	} else if(color == EMPTY && currItem != EMPTY) {
    		if(currItem == BLACK) {
    			blackChips--;
    		} else if(currItem == WHITE) {
    			whiteChips--;
    		}
    	}
    	grid[x][y] = color;
    }
    
    public String toString() {
    	System.out.println("hiii");
    	String output = new String();
    	String linestring = "--------------------------\n";
    	for(int i=0; i<DIMENSION; i++) {
    		String startString = "|";
    		for(int j=0; j<DIMENSION; j++) {
    			if(grid[i][j] == BLACK) {
    				startString = startString + "B";
    			} else if(grid[i][j] == WHITE) {
    				startString = startString + "W";
    			} else if(grid[i][j] == EMPTY) {
    				startString = startString + " ";
    			}
    		}
    		startString = startString + "|\n";
    		output = output + startString;
    	}
    	output = linestring + output + linestring;
    	return output;
    }
    
}