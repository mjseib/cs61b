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

		cord[0] = hasConnect(UP, board, color, coord);
		cord[1] = hasConnect(DOWN, board, color, coord);
		cord[2] = hasConnect(LEFT, board, color, coord);
		cord[3] = hasConnect(RIGHT, board, color, coord);
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
	 * Finds if there are any networks and return them
	 * @param board the current board
	 * @param color the color we are looking for networks
	 * @return a List of networks if any, otherwise null
	 */
	
	private static SList getNetworks(Board board, int color) {
		SList possibleNetworks;
		SList goodNetworks = new SList();
		// If there are less than 6 chips, or there aren't any chips in the goal then there won't even be a network
		if(board.numberOfChips(color) < 6 || !chipsAtGoals(board, color)) {
			return null;
		}
		
		possibleNetworks = collectPossibleNetworks(board, color);
		
		try {
			SListNode currPossibleNetwork = (SListNode) possibleNetworks.front();
			while(currPossibleNetwork.isValidNode()) {
				SList currNetworkCoords = (SList) currPossibleNetwork.item();
				if(checkValidNetwork(currNetworkCoords)) {
					goodNetworks.insertBack(currNetworkCoords);
				}
				currPossibleNetwork = (SListNode) currPossibleNetwork.next();
			}
		} catch(InvalidNodeException e) {
			System.out.println(e);
		}
		
		return goodNetworks;
	}
	
	/**
	 * Checks the valid network for three in a row, or three in a diagonal
	 * @param networkList list of the possible networks
	 * @return true if is valid, false otherwise
	 */
	
	private static boolean checkValidNetwork(SList networkPositions) {
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
	 * Finds the possible networks
	 * @param board the current game board
	 * @param color we are looking for
	 * @return returns the possible networks possibleNetwork
	 */
	
	private static SList collectPossibleNetworks(Board board, int color) {
		SList possibleNetwork = new SList();
		SList network = new SList();
		if(color == Board.BLACK) {
			for(int i=0; i<Board.DIMENSION; i++) {
				if(board.getContent(i, 0) == color) {
					Position chip = new Position(i, 0);
					network.insertBack(chip);
					makeConnection(board, color, network, possibleNetwork);
				}
			}
		}
		if(color == Board.WHITE) {
			for(int j=0; j<Board.DIMENSION; j++) {
				if(board.getContent(0, j) == color) {
					Position chip = new Position(0, j);
					network.insertBack(chip);
					makeConnection(board, color, network, possibleNetwork);
				}
			}
		}
		return possibleNetwork;
	}
	
	/**
	 * Called recursively and when it finds a network across, puts it in possibleNetwork
	 * @param board the current game board
	 * @param color we are looking for
	 * @param network this is where the network is built up in
	 * @param possibleNetwork where the found network will be put into
	 */
	private static void makeConnection(Board board, int color, SList network, SList possibleNetwork) {
		try {
			Position pos = (Position) network.back().item();
			
			// If there is more than one node, then the last node shouldn't be start goal
			if(network.length() > 1 && startGoalTending(color, pos)) {
				return;
			}
			// If the last piece is a end goal piece, put it on the possibleNetwork
			if(endGoalTending(color, pos)) {
				possibleNetwork.insertBack(network);
			} else {
				for(int i=UPL; i<=DOWNR; i++) {
					if(hasConnect(i, board, color, pos) != null) {
						Position connection = hasConnect(i, board, color, pos);
						if(network.find(connection) == null) {
							network.insertBack(connection);
							makeConnection(board, color, network, possibleNetwork);
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
			if(coord.getY() == 0) {
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
	 * Sees if the position is in the ending goal
	 * @param color color we are looking for
	 * @param coord coordinate we are checking to see if it is in the color's end goal
	 * @return true if it is, false otherwise
	 */
	
	private static boolean endGoalTending(int color, Position coord) {
		if(color == Board.BLACK) {
			if(coord.getY() == Board.DIMENSION-1) {
				return true;
			}
		}
		
		if(color == Board.WHITE) {
			if(coord.getX() == Board.DIMENSION-1) {
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
	 * Looks in the direction on the Board until it finds a connection or not, if it does returns it
	 * @param dir direction to search in
	 * @param board current game board
	 * @param color color of the player
	 * @param coord position we are looking around
	 * @return the Position if there is a connection, or null if not
	 */
	
	private static Position hasConnect(int dir, Board board, int color, Position coord) {
		// If it's WHITE, in the goal, and searching UP/DOWN, then don't care
		if(goalTending(color, coord) && color == Board.WHITE && (dir == UP || dir == DOWN)) {
			return null;
		}
		// If it's BLACK, in the goal, and searching LEFT/RIGHT, then don't care
		if(goalTending(color, coord) && color == Board.BLACK && (dir == LEFT || dir == RIGHT)) {
			return null;
		}
		int changeX = 0;
		int changeY = 0;
		switch(dir) {
		case UP:
			changeY = -1;
			break;
		case DOWN:
			changeY = 1;
			break;
		case RIGHT:
			changeX = 1;
			break;
		case LEFT:
			changeX = -1;
			break;
		case UPL:
			changeY = -1;
			changeX = -1;
			break;
		case UPR:
			changeY = -1;
			changeX = 1;
			break;
		case DOWNL:
			changeY = 1;
			changeX = -1;
			break;
		case DOWNR:
			changeY = 1;
			changeX = 1;
			break;
		}
		
		Position newCord = new Position(coord.getX() + changeX, coord.getY() + changeY);
		while(validPosition(newCord)) {
			int thisContent = board.getContent(newCord.getX(), newCord.getY());
			if(thisContent == color) {
				return newCord;
			} else if(thisContent == Board.EMPTY) {
				newCord = new Position(newCord.getX()+changeX, newCord.getY()+changeY);
			} else {
				return null;
			}
		}

		return null;
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