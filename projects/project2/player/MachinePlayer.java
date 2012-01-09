/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

	private final int DEPTH = 1;
	
    protected int color;
    protected int searchDepth;
    protected Board gameBoard;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
      this.color = color;
      this.searchDepth = DEPTH;
      gameBoard = new Board();
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
      this.color = color;
      this.searchDepth = searchDepth;
      gameBoard = new Board();
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
	  Move move = miniMax(color, searchDepth);
	  doMove(move, color);
	  return move;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    return doMove(m, opponentColor(color));
  }
  
  private boolean doMove(Move m, int color) {
	  return false;
  }
  
  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    return false;
  }
  
  /**
   * minimax returns the best move after searching to depth
   * @return the best move
   */

  public Move miniMax(int color, int depth) {
	  return new Move();
  }

/**
   * Just a helper function to get opponent's color
   * @param color my color
   * @return opposing color
   */
  
  protected static int opponentColor(int color) {
	  if(color == Board.WHITE) {
		  return Board.BLACK;
	  } else {
		  return Board.WHITE;
	  }
  }
  
}
