/* Evaluator.java */

package player;
import list.*;

public class Evaluator {
	
	static Board board;
	
	public Evaluator(Board board) {
		Evaluator.board = board;
	}
	
	/**
	 * Function to evaluate if the board has a win or not regardless of black or white
	 * @return true if there is a network, false otherwise
	 */
	
	public boolean winningBoard() {
		return (Network.hasValidNetwork(board, Board.BLACK) || Network.hasValidNetwork(board, Board.WHITE));
	}
	
	/**
	 * boardEval looks at board and assigns it a score based on how good it is
	 * @param color is the player's color white or black
	 * @return a high score if desirable, low score if not
	 */
	
	public int boardEval(int color) {
		int myScore = 0;
		if(board.numberOfChips()>2) {
			if(Network.hasValidNetwork(board, color)) {
				return 100;
			} 
			if(Network.hasValidNetwork(board, MachinePlayer.opponentColor(color))) {
				return -100;
			}
			int ownConnects = 0;
			int enemyConnects = 0;
			for(int i=0; i<Board.DIMENSION; i++) {
				for(int j=0; j<Board.DIMENSION; j++) {
					int gridPiece = board.getContent(i, j);
					if(gridPiece != Board.EMPTY){
						Position currPos = new Position(i,j);
						SList connections = Network.getConnections(board, gridPiece, currPos);
						if(gridPiece == color) {
							if(Network.goalTending(color, currPos)) {
								ownConnects += connections.length()*2;
							} else {
								ownConnects += connections.length();
							}
						} else {
							if(Network.goalTending(MachinePlayer.opponentColor(color), currPos)) {
								enemyConnects += connections.length()*2;
							} else {
								enemyConnects += connections.length();
							}
						}
					}
				}
			}
			myScore = ownConnects - enemyConnects;
		}
		return myScore;
	}
	
	public static boolean isValidMove(Move move, int color, Board board) {
		Evaluator eval = new Evaluator(board);
		return isValidMove(move, color);
	}
	
	/**
	 * isValidMoves(Move move, Player player) checks if its a valid add move
	 * @param move the move to be checked
	 * @param color the player's color
     * @return a boolean of whether or not the move is valid
     */
	
	public static boolean isValidMove(Move move, int color) {
		//Can't move a piece to the same spot
		if(move.moveKind == Move.STEP) {
			if(move.x1 == move.x2 || move.y1 == move.y2) {
				return false;
			}
		} else if(move.moveKind == Move.ADD) {
			//Can't add more pieces if you already have ten
			if(board.numberOfChips(color) > 10) {
				return false;
			}
			//Checks if it's trying to add out of bounds or into the corners
			if(!Network.validPosition(new Position(move.x1, move.y1))) {
				return false;
			}
			//Checks if it is trying to put a chip on top of another chip
			if(board.getContent(move.x1, move.y1) != Board.EMPTY) {
				return false;
			}
			//Checks if trying to add a piece in opposing player's goal
			if(Network.goalTending(MachinePlayer.opponentColor(color), new Position(move.x1, move.x2))) {
				return false;
			}
			//Checks to see if there are 2+ neighbors in a box that is a radius of one away
			SList neighborList = findNeighbors(new Position(move.x1, move.y1), color);
			if(neighborList.length() > 1) {
				return false;
			} else {
				try {
					Position neighbor = (Position) neighborList.front().item();
					SList neighborhood = findNeighbors(neighbor, color);
					//If the neighbor has a neighbor, that will make a group of three in a box that is a radius of two away
					if(!neighborhood.isEmpty()) {
						return false;
					}
				} catch(InvalidNodeException e) {
					//System.out.println(e + " in isValidMove");
				}
			}
		}
		//If it gets here, it checks all the things that would make it invalid
		return true;
	}
	
	private static SList findNeighbors(Position coord, int color) {
		SList neighbors = new SList();
		
		for(int i=coord.getX()-1; i<coord.getX()+2; i++) {
			for(int j=coord.getY()-1; j<coord.getY()+2; j++) {
				if(!(i==coord.getX() && j==coord.getY()) && i>=0 && i<=7 && j>=0 && j<=7 && board.getContent(i, j) == color) {
					neighbors.insertBack(new Position(i,j));
				}
			}
		}
		
		return neighbors;
	}

}
