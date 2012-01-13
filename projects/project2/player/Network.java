/* Network.java */

package player;
import list.*;

public class Network {	
	private static final int UP = 2;
	private static final int DOWN = 7;
	private static final int LEFT = 4;
	private static final int RIGHT = 5;
	private static final int UPL = 1;
	private static final int UPR = 3;
	private static final int DOWNL = 6;
	private static final int DOWNR = 8;
	
	private static SList blackNetworks;
	private static SList whiteNetworks;

	/**
	 * get the connections that can be found at the coordinate
	 * @param board current game board
	 * @param color color of the player
	 * @param coord coordinate we want to get connections to
	 * @return a list of all possible connections
	 */
	
	public static SList getConnections(Board board, int color, Position coord) {
		SList connections = new SList();
		Position[] cord = new Position[8];

		if(!(goalTending(color, coord)) && color == Board.WHITE) {
			cord[0] = hasConnect(UP, board, color, coord);	
			cord[1] = hasConnect(DOWN, board, color, coord);
		}
		if(!(goalTending(color, coord)) && color == Board.BLACK) {
			cord[2] = hasConnect(LEFT, board, color, coord);
			cord[3] = hasConnect(RIGHT, board, color, coord);
		}
		cord[4] = hasConnect(UPL, board, color, coord);
		cord[5] = hasConnect(UPR, board, color, coord);
		cord[6] = hasConnect(DOWNL, board, color, coord);
		cord[7] = hasConnect(DOWNR, board, color, coord);
		
		for(int i=0; i<cord.length; i++) {
			if(cord[i]!=null) {
				connections.insertBack(cord[i]);
			}
		}
		
		return connections;
	}
	
	/**
	 * Determines if there is a winning network
	 * @param board current game board
	 * @param color we are looking for
	 * @return true if there is a winning network, false otherwise
	 */
	
	public static boolean hasValidNetwork(Board board, int color) {
		SList networkList = getNetworks(board, color);
		if(networkList.length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Finds the possible networks
	 * @param board the current game board
	 * @param color we are looking for
	 * @return returns the possible networks possibleNetwork
	 */
	
	private static SList getNetworks(Board board, int color) {
		SList validNetworks = null;
		collectPossibleNetworks(board, color);
		
		try {
			if(color == Board.BLACK) {
				SListNode currNode = (SListNode) blackNetworks.front();
				while(currNode.isValidNode()) {
					SList list = (SList) currNode.item();
					if(!checkValidNetwork(board, list)) {
						currNode.remove();
					}
					currNode = (SListNode) currNode.next();
				}
				validNetworks = blackNetworks;
			}
			if(color == Board.WHITE) {
				SListNode currNode = (SListNode) whiteNetworks.front();
				while(currNode.isValidNode()) {
					SList list = (SList) currNode.item();
					if(!checkValidNetwork(board, list)) {
						currNode.remove();
					}
					currNode = (SListNode) currNode.next();
				}
				validNetworks = whiteNetworks;
			}
		} catch(InvalidNodeException e) {
			
		}
		return validNetworks;
	}

	/**
	 * Checks the valid network for three in a row, or three in a diagonal
	 * @param networkList list of the possible networks
	 * @return true if is valid, false otherwise
	 */
	
	private static boolean checkValidNetwork(Board board, SList networkPositions) {
		try {
			if(networkPositions.length() < 6) {
				return false;
			}
			SListNode currPosition = (SListNode) networkPositions.front();
			while(currPosition.isValidNode() && currPosition.next().isValidNode() && currPosition.next().next().isValidNode()) {
				Position first = (Position) currPosition.item();
				Position second = (Position) currPosition.next().item();
				Position third = (Position) currPosition.next().next().item();
				
				if(first.getX() == second.getX() && second.getX() == third.getX()) {
					return false;
				}
				if(first.getY() == second.getY() && second.getY() == third.getY()) {
					return false;
				}
				if(slope(first, second) == slope(second, third)) {
					return false;
				}
				currPosition = (SListNode) currPosition.next();
			}
		} catch(InvalidNodeException e) {
			System.out.println(e + "In checking validnetwork");
		}
		return true;
	}
	
	/**
	 * Takes two positions and gives the slope
	 * @param a one of two positions
	 * @param b one of two positions
	 * @return the slope between the two positions
	 */
	
	private static double slope(Position a, Position b) {
		int rise = a.getY() - b.getY();
		int run = a.getX() - b.getX();
		
		return ((double) rise) / ((double) run);
	}
	
	/**
	 * Looks in the direction on the Board until it finds a connection or not, if it does returns it
	 * @param dir direction to search in
	 * @param board current game board
	 * @param color color of the player
	 * @param coord position we are looking around
	 * @return the Position if there is a connection, or null if not
	 */
	
	private static Position hasConnect(int dir, Board board, int color, Position coord) {
		Position returnPos = null;
		int enemyColor = MachinePlayer.opponentColor(color);
		for(int i=1; i<Board.DIMENSION; i++) {
			int changeX = 0;
			int changeY = 0;
			switch(dir) {
			case UP:
				changeX = coord.getX();
				changeY = coord.getY()-i;
				break;
			case DOWN:
				changeX = coord.getX();
				changeY = coord.getY()+i;
				break;
			case RIGHT:
				changeX = coord.getX()+i;
				changeY = coord.getY();
				break;
			case LEFT:
				changeX = coord.getX()-i;
				changeY = coord.getY();
				break;
			case UPL:
				changeX = coord.getX()-i;
				changeY = coord.getY()-i;
				break;
			case UPR:
				changeX = coord.getX()+i;
				changeY = coord.getY()-1;
				break;
			case DOWNL:
				changeX = coord.getX()-i;
				changeY = coord.getY()+i;
				break;
			case DOWNR:
				changeX = coord.getX()+i;
				changeY = coord.getY()+i;
				break;
			}
			if(validPosition(new Position(changeX, changeY))) {
				if(board.getContent(changeX, changeY) == enemyColor) {
					returnPos = null;
					break;
				} else if(board.getContent(changeX, changeY) == color) {
					returnPos = new Position(changeX, changeY);
					break;
				}
			} else {
				break;
			}
		}
		return returnPos;
	}

	/**
	 * Finds if there are any networks and return them
	 * @param board the current board
	 * @param color the color we are looking for networks
	 * @return a List of networks if any, otherwise null
	 */
	
	private static void collectPossibleNetworks(Board board, int color) {
		blackNetworks = new SList();
		whiteNetworks = new SList();
		
		if(board.numberOfChips(color) < 6 || !chipsAtGoals(board, color)) {
			return;
		}
		
		if(color == Board.BLACK) {
			for(int i=0; i<Board.DIMENSION; i++) {
				if(board.getContent(i, Board.DIMENSION-1) == color) {
					Position chipPair = new Position(i, Board.DIMENSION-1);
					SList network = new SList();
					network.insertBack(chipPair);
					makeConnection(board, color, network);
				}
			}
		}
		if(color == Board.WHITE) {
			for(int i=0; i<Board.DIMENSION; i++) {
				if(board.getContent(0, i) == color) {
					Position chipPair = new Position(0, i);
					SList network = new SList();
					network.insertBack(chipPair);
					makeConnection(board, color, network);
				}
			}
		}
	}

	/**
	 * Called recursively and when it finds a network across, puts it in possibleNetwork
	 * @param board the current game board
	 * @param color we are looking for
	 * @param network this is where the network is built up in
	 */
	private static void makeConnection(Board board, int color, SList network) {
		try {
			Position pos = (Position) network.back().item();
			
			// If there is more than one node, then the last node shouldn't be start goal
			if(network.length() > 1 && startGoalTending(color, pos)) {
				return;
			}
			// If the last piece is a end goal piece, put it on the possibleNetwork
			if(color == Board.BLACK && pos.getY()==0) {
				blackNetworks.insertBack(network);
			} else if(color == Board.WHITE && pos.getX()==(Board.DIMENSION-1)) {
				whiteNetworks.insertBack(network);
			} else {
				for(int i=UPL; i<=DOWNR; i++) {
					if(hasConnect(i, board, color, pos) != null) {
						Position connection = hasConnect(i,board,color,pos);
						if(network.find(connection) == null) {
							SList newList = network.copy();
							newList.insertBack(connection);
							makeConnection(board, color, newList);
						}
					}
				}
			}
		} catch (InvalidNodeException e) {
			System.out.println(e + "at checkConnection");
		}
	}

	/**
	 * Sees if the position is in the starting goal
	 * @param color color we are looking for
	 * @param coord coordinate we are checking to see if its in the color's start goal
	 * @return true if it is, false otherwise
	 */
	
	private static boolean startGoalTending(int color, Position coord) {
		if(color == Board.BLACK) {
			if(coord.getY() == 7) {
				return true;
			}
		}
		
		if(color == Board.WHITE) {
			if(coord.getX() == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Determines if there are chips in both sides of the goal
	 * @param board current game board
	 * @param color which color we're looking at
	 * @return true if there are chips in both goals, false otherwise
	 */
	
	private static boolean chipsAtGoals(Board board, int color) {
		if(color == Board.WHITE) {
			boolean leftSide = false;
			boolean rightSide = false;
			
			for(int j=0; j<Board.DIMENSION; j++) {
				if(board.getContent(0, j) == color) {
					leftSide = true;
				}
				if(board.getContent(Board.DIMENSION-1, j) == color) {
					rightSide = true;
				}
			}
			if(leftSide && rightSide) {
				return true;
			} else {
				return false;
			}
		} 
		
		if(color == Board.BLACK) {
			boolean topSide = false;
			boolean botSide = false;
			
			for(int i=0; i<Board.DIMENSION; i++) {
				if(board.getContent(i,0) == color) {
					botSide = true;
				}
				if(board.getContent(i,Board.DIMENSION-1) == color) {
					topSide = true;
				}
			}
			if(topSide && botSide) {
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Is the current position in a goal spot?
	 * @param color color of the player
	 * @param coord current coordinate of the chip
	 * @return true if is in the goal, false otherwise
	 */
	
	protected static boolean goalTending(int color, Position coord) {
		if(color == Board.BLACK) {
			if(coord.y == 0 || coord.y == 7) {
				return true;
			}
		}
		if(color == Board.WHITE) {
			if(coord.x == 0 || coord.x == 7) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the Position given is in a valid position
	 * @param coord x,y coordinate
	 * @return true if it is not in the goals or corners or outside boundaries
	 */
	
	protected static boolean validPosition(Position coord) {
		int x = coord.getX();
		int y = coord.getY();
		
		if(x<0 || x>7 || y<0 || y>7) {
			return false;
		}
		if(x==0 || x==7) {
			if(y==0 || y==7) {
				return false;
			}
		}
		
		return true;
	}
	
}