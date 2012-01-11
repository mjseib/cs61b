/* MachinePlayer.java */

package player;
import java.util.Random;

import list.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

	private final int DEPTH = 1;
	
    protected int color;
    protected int searchDepth;
    protected Board gameBoard;
    
    public final int whiteWin = 100;
    public final int blackWin = -100;

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
	  BestMove best = miniMax(color, searchDepth);
	  doMove(best.move, color);
	  return best.move;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    return doMove(m, opponentColor(color));
  }
  
  private boolean doMove(Move m, int color) {
	  if(m.moveKind == Move.ADD) {
		  if(Evaluator.isValidMove(m, color)) {
			  gameBoard.setContent(m.x1, m.y1, color);
			  return true;
		  }
	  } else if(m.moveKind == Move.STEP){
		  gameBoard.setContent(m.x2, m.y2, Board.EMPTY);
		  if(Evaluator.isValidMove(m, color)) {
			  gameBoard.setContent(m.x1, m.y1, color);
			  return true;
		  }
	  }
	  return false;
  }
  
  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    return doMove(m, color);
  }
  
  /**
   * minimax returns the best move after searching to depth
   * @return the best move
   */

  public BestMove miniMax(int color, int depth) {
	  int alpha = blackWin - 1;
	  int beta = whiteWin +1;
	  boolean side;
	  if(color == this.color) {
		  side = true;
	  } else {
		  side = false;
	  }
	  BestMove best = moveHelper(color, side, depth, alpha, beta);
	  
	  return best;
  }
  
  private BestMove moveHelper(int color, boolean side, int depth, int alpha, int beta) {
	  BestMove myBest = new BestMove();
	  BestMove reply = new BestMove();
	  Evaluator evaluation = new Evaluator(gameBoard);
				  
	  try {
		  int chips = gameBoard.numberOfChips();
		  
		  if(chips <= 2) {
			  Move firstMove = new Move();
			  Random r = new Random();
			  if(color == Board.BLACK) {
				  firstMove.y1 = 0;
				  firstMove.x1 = r.nextInt(Board.DIMENSION-2)+1;
				  myBest.move = firstMove;
				  return myBest;
			  }
			  if(color == Board.WHITE) {
				  firstMove.x1 = 0;
				  firstMove.y1 = r.nextInt(Board.DIMENSION-2)+1;
				  myBest.move = firstMove;
				  return myBest;
			  }
		  }
		  if(chips >2 && chips <= 4) {
			  Move firstMove = new Move();
			  Random r = new Random();
			  if(color == Board.BLACK) {
				  firstMove.y1 = Board.DIMENSION-1;
				  firstMove.x1 = r.nextInt(Board.DIMENSION-2)+1;
				  myBest.move = firstMove;
				  return myBest;
			  }
			  if(color == Board.WHITE) {
				  firstMove.x1 = Board.DIMENSION-1;
				  firstMove.y1 = r.nextInt(Board.DIMENSION-2)+1;
				  myBest.move = firstMove;
				  return myBest;
			  }
		  }
		  
		  if(Network.hasValidNetwork(gameBoard, color) || depth == 0) {
			  myBest.score = evaluation.boardEval(color);
			  return myBest;
		  }
	  
		  if(side) {
			  myBest.score = alpha;
		  } else {
			  myBest.score = beta;
		  }
		  SList possibleMoves = generateMoves(color);
		  SListNode currPossibleNode = (SListNode) possibleMoves.front();
		  for(int i=0; i<possibleMoves.length(); i++) {
			  Move currentMove = (Move) currPossibleNode.item();
			  Board copyBoard = gameBoard.clone();
			  if(doMove(currentMove, color)) {
				  reply = moveHelper(opponentColor(color),!side, depth-1, alpha, beta);
			  }
			  gameBoard = copyBoard;
			  if(side && (reply.score > myBest.score)) {
				  myBest.move = currentMove;
				  myBest.score = reply.score;
				  alpha = reply.score;
			  } else if(!side && (reply.score < myBest.score)) {
				  myBest.move = currentMove;
				  myBest.score = reply.score;
				  beta = reply.score;
			  } 
			  if(alpha >= beta) {
				  return myBest;
			  }
		  }
	  } catch (InvalidNodeException e) {
		  System.out.println(e + "in movehelper");
	  }
	return myBest;
  }
  
  private SList generateMoves(int color) {
	  SList validMoves = new SList();
	  try {
		  if(gameBoard.numberOfChips(color) < 10) {
			  //Generates the add moves
			  for(int i=0; i<Board.DIMENSION; i++) {
				  for(int j=0; j<Board.DIMENSION; j++) {
					  if(gameBoard.getContent(i,j) == Board.EMPTY) {
						  Move addMove = new Move(i,j);
						  if(Evaluator.isValidMove(addMove, color)) {
							  validMoves.insertBack(addMove);
						  }
					  }
				  }
			  }
		  } else {
			  //Generate the step moves
			  SList emptyList = new SList();
			  SList chipList = new SList();
			  for(int i=0; i<Board.DIMENSION; i++) {
				  for(int j=0; j<Board.DIMENSION; j++) {
					  Position currPosition = new Position(i,j);
					  if(gameBoard.getContent(i,j) == Board.EMPTY) {
						  emptyList.insertBack(currPosition);
					  } else if(gameBoard.getContent(i,j) == color){
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
					  
					  gameBoard.setContent(stepMove.x2, stepMove.y2, Board.EMPTY);
					  if(Evaluator.isValidMove(stepMove, color)) {
						  validMoves.insertBack(stepMove);
					  }
					  gameBoard.setContent(stepMove.x2, stepMove.y2, color);
					  emptyNode = (SListNode) emptyNode.next();
				  }
				  chipNode = (SListNode) chipNode.next();
			  }
			  
		  }
	  } catch (InvalidNodeException e) {
		  System.out.println(e + "in move generations");
	  }
	return validMoves;
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
