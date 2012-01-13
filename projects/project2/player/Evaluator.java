/* Evaluator.java */

package player;
import list.*;

public class Evaluator {
	
	private Board board;
	
	public Evaluator(Board board) {
		this.board = board;
	}
	
	public void setBoard(Board board) {
		this.board = board;
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
		if(board.numberOfChips() > 2) {
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
						} else if (gridPiece == MachinePlayer.opponentColor(color)) {
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
	
	/**
	 * isValidMoves(Move move, Player player) checks if its a valid add move
	 * @param move the move to be checked
	 * @param color the player's color
     * @return a boolean of whether or not the move is valid
     */
	
	public boolean isValidMove(Move move, int color) {
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
	
	private SList findNeighbors(Position coord, int color) {
		SList neighbors = new SList();
		
		for(int i=coord.getX()-1; i<coord.getX()+2; i++) {
			for(int j=coord.getY()-1; j<coord.getY()+2; j++) {
				if(!(i==coord.getX() && j==coord.getY()) && i>=0 && i<=7 && j>=0 && j<=7)
					if(board.getContent(i, j) == color) {
					neighbors.insertBack(new Position(i,j));
				}
			}
		}
		
		return neighbors;
	}
	
	 public SList generateMoves(int color) {
		  SList validMoves = new SList();
		  try {
			  if(board.numberOfChips(color) >= 10) {

				  //Generate the step moves
				  SList emptyList = new SList();
				  SList chipList = new SList();
				  for(int i=0; i<Board.DIMENSION; i++) {
					  for(int j=0; j<Board.DIMENSION; j++) {
						  Position currPosition = new Position(i,j);
						  if(board.getContent(i,j) == Board.EMPTY) {
							  emptyList.insertBack(currPosition);
						  } else if(board.getContent(i,j) == color){
							  chipList.insertBack(currPosition);
						  }
					  }
				  }

				  SListNode chipNode = (SListNode) chipList.front();
				  for(int i=0; i<chipList.length(); i++) {
					  SListNode emptyNode = (SListNode) emptyList.front();
					  for(int j=0; j<emptyList.length(); j++) {
						  Position emptyPosition = (Position) emptyNode.item();
						  Position chipPosition = (Position) chipNode.item();
						  Move stepMove = new Move(emptyPosition.getX(), emptyPosition.getY(), chipPosition.getX(), chipPosition.getY());
						  
						  board.setContent(stepMove.x2, stepMove.y2, Board.EMPTY);
						  if(isValidMove(stepMove, color)) {
							  validMoves.insertBack(stepMove);
						  }
						  board.setContent(stepMove.x2, stepMove.y2, color);
						  emptyNode = (SListNode) emptyNode.next();
					  }
					  chipNode = (SListNode) chipNode.next();
				  }
				  
			  
			  } else  {
				  //Generates the add moves
				  for(int i=0; i<Board.DIMENSION; i++) {
					  for(int j=0; j<Board.DIMENSION; j++) {
						  if(board.getContent(i,j) == Board.EMPTY) {
							  Move addMove = new Move(i,j);
							  if(isValidMove(addMove, color)) {
								  validMoves.insertBack(addMove);
							  }
						  }
					  }
				  }
			  }
		  } catch (InvalidNodeException e) {
			  System.out.println(e + "in move generations");
		  }
		return validMoves;
	  }

}
