/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes an
 *  Ocean object.  Descriptions of the methods you must implement appear below.
 *  They include constructors of the form
 *
 *      public RunLengthEncoding(int i, int j, int starveTime);
 *      public RunLengthEncoding(int i, int j, int starveTime,
 *                               int[] runTypes, int[] runLengths) {
 *      public RunLengthEncoding(Ocean ocean) {
 *
 *  that create a run-length encoding of an Ocean having width i and height j,
 *  in which sharks starve after starveTime timesteps.
 *
 *  The first constructor creates a run-length encoding of an Ocean in which
 *  every cell is empty.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts an Ocean object into a run-length encoding of that object.
 *
 *  See the README file accompanying this project for additional details.
 */

public class RunLengthEncoding {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */
	
	private DList runList;
	private DListNode currNode;
	private int starveTime;
	private int i;
	private int j;

  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with three parameters) is a constructor that creates
   *  a run-length encoding of an empty ocean having width i and height j,
   *  in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public RunLengthEncoding(int i, int j, int starveTime) {
    // Your solution here.
	  this.i = i;
	  this.j = j;
	  this.starveTime = starveTime;
	  runList = new DList(0, this.i*this.j);
	  currNode = null;
  }

  /**
   *  RunLengthEncoding() (with five parameters) is a constructor that creates
   *  a run-length encoding of an ocean having width i and height j, in which
   *  sharks starve after starveTime timesteps.  The runs of the run-length
   *  encoding are taken from two input arrays.  Run i has length runLengths[i]
   *  and species runTypes[i].
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   *  @param runTypes is an array that represents the species represented by
   *         each run.  Each element of runTypes is Ocean.EMPTY, Ocean.FISH,
   *         or Ocean.SHARK.  Any run of sharks is treated as a run of newborn
   *         sharks (which are equivalent to sharks that have just eaten).
   *  @param runLengths is an array that represents the length of each run.
   *         The sum of all elements of the runLengths array should be i * j.
   */

  public RunLengthEncoding(int i, int j, int starveTime,
                           int[] runTypes, int[] runLengths) {
	  this.i = i;
	  this.j = j;
	  currNode = null;
	  this.starveTime = starveTime;
	  runList = new DList();
	  for(int k=0; k<runTypes.length; k++) {
		  if(runTypes[k] == 1) {
			  runList.insertBack(runTypes[k], runLengths[k], starveTime);
		  } else {
			  runList.insertBack(runTypes[k], runLengths[k]);
		  }
	  }
  }

  /**
   *  restartRuns() and nextRun() are two methods that work together to return
   *  all the runs in the run-length encoding, one by one.  Each time
   *  nextRun() is invoked, it returns a different run (represented as a
   *  TypeAndSize object), until every run has been returned.  The first time
   *  nextRun() is invoked, it returns the first run in the encoding, which
   *  contains cell (0, 0).  After every run has been returned, nextRun()
   *  returns null, which lets the calling program know that there are no more
   *  runs in the encoding.
   *
   *  The restartRuns() method resets the enumeration, so that nextRun() will
   *  once again enumerate all the runs as if nextRun() were being invoked for
   *  the first time.
   *
   *  (Note:  Don't worry about what might happen if nextRun() is interleaved
   *  with addFish() or addShark(); it won't happen.)
   */

  /**
   *  restartRuns() resets the enumeration as described above, so that
   *  nextRun() will enumerate all the runs from the beginning.
   */

  public void restartRuns() {
    // Your solution here.
	  currNode = null;
  }

  /**
   *  nextRun() returns the next run in the enumeration, as described above.
   *  If the runs have been exhausted, it returns null.  The return value is
   *  a TypeAndSize object, which is nothing more than a way to return two
   *  integers at once.
   *  @return the next run in the enumeration, represented by a TypeAndSize
   *          object.
   */

  public TypeAndSize nextRun() {
	  if(currNode == null) {
		  currNode = runList.head;
	  } 
	  currNode = currNode.next;
	  // This was to reach the end of the list
	  if(currNode == runList.head) {
		  return null;
	  }
	  if(currNode.aniType == null) {
		  return new TypeAndSize(Ocean.EMPTY, currNode.aniLength);
	  } else {
		  return new TypeAndSize(currNode.aniType.getType(), currNode.aniLength);
	  }
  }

  /**
   *  toOcean() converts a run-length encoding of an ocean into an Ocean
   *  object.  You will need to implement the three-parameter addShark method
   *  in the Ocean class for this method's use.
   *  @return the Ocean represented by a run-length encoding.
   */

  public Ocean toOcean() {
	  Ocean sea = new Ocean(i, j, starveTime);
	  int indexTrack = 0;
	  int listSize = runList.getSize();
	  int feeding=0;
	  TypeAndSize currData;
	  currNode = null;
	  for(int k = 0; k < listSize; k++) {
		  currData = nextRun();
		  int aniType = currData.type;
		  int aniSize = currData.size;
		  if(aniType == 1) {
			  feeding = ((Shark) currNode.aniType).dyingTime();
		  }
		  for(int count = 0; count < aniSize; count++) {
			  int[] tuple = getTuple(indexTrack);
			  if(aniType == 1) {
				  sea.addShark(tuple[0], tuple[1], feeding);
			  } else if(aniType == 2) {
				  sea.addFish(tuple[0], tuple[1]);
			  } else {
				  //do nothing
			  }
			  indexTrack++;
		  }
	  }
	  return sea;
  }
  
  private int[] getTuple(int index) {
	  int[] tuple = {0, 0};
	  tuple[1] = index/i;
	  tuple[0] = index%i;
	  
	  return tuple;
  }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of an input Ocean.  You will need to implement
   *  the sharkFeeding method in the Ocean class for this constructor's use.
   *  @param sea is the ocean to encode.
   */

  public RunLengthEncoding(Ocean sea) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
	  currNode = null;
	  starveTime = sea.starveTime();
	  i=sea.width();
	  j=sea.height();
	  runList = new DList();
	  boolean sameAsPrev = true;
	  int size = i*j;
	  int aniCounter=1;
	  int currType;
	  int prevType;
	  int prevDeath=0;
	  // This is to get the first one (0,0) to start off
	  int[] tuple=getTuple(0);
	  prevType = sea.cellContents(tuple[0], tuple[1]);
	  if(prevType == 1) {
		  prevDeath = sea.sharkFeeding(tuple[0], tuple[1]);
	  }
	 
	  for(int i=1; i<size; i++) {
		  tuple=getTuple(i);
		  currType = sea.cellContents(tuple[0], tuple[1]);
		  if(currType == prevType) {
			  if(currType == 1) {
				  if(sea.sharkFeeding(tuple[0], tuple[1]) == prevDeath) {
					  aniCounter++;
				  } else if(sea.sharkFeeding(tuple[0], tuple[1]) != prevDeath) {
					  sameAsPrev = false;
				  }
			  } else {
				  aniCounter++;
			  }
		  } else if(currType != prevType) {
			  sameAsPrev = false;
		  }
		  if(!sameAsPrev) {
			  if(prevType == 1) {
				  runList.insertBack(prevType, aniCounter, prevDeath);
			  } else {
				  runList.insertBack(prevType, aniCounter);
			  } 
			  sameAsPrev = true;
			  prevType = currType;
			  aniCounter = 1;
			  if(currType == 1) {
				  prevDeath = sea.sharkFeeding(tuple[0], tuple[1]);
			  }
		  }
	  }
	  if(prevType == 1) {
		  runList.insertBack(prevType,aniCounter,prevDeath);
	  } else {
		  runList.insertBack(prevType, aniCounter);
	  }
	  check();
  }

  /**
   *  The following methods are required for Part IV.
   */

  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.  The final run-length
   *  encoding should be compressed as much as possible; there should not be
   *  two consecutive runs of sharks with the same degree of hunger.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
	  int index = y*i+x;
	  int listSize = runList.getSize();
	  int prevIndex = 0;
	  int currIndex = 0;
	  currNode = null;
	  for(int m=0; m<listSize; m++) {
		  TypeAndSize currData = nextRun();
		  currIndex += currData.size;
		  if(index > prevIndex && index < currIndex) {
			  if(currNode.aniType == null) {
				  runList.insertAfterNode(2, 1, currNode);
				  currNode.aniLength=index - prevIndex;
				  if(currIndex-index-1 != 0) {
					  runList.insertAfterNode(0, currIndex-index-1, currNode.next);
				  } else {
					  // If we dont add an empty run after because the length is zero, then check if there is a fish next to it
					  if(currNode.next.next.aniType.getType() == 2) {
						  currNode.next.aniLength += currNode.next.next.aniLength;
						  runList.removeAfterNode(currNode.next);
					  }
				  }
			  }
			  break;
		  } 
		  if(index > prevIndex && index <= currIndex) {
			  if(currNode.next.aniType == null) {
				  //Insert a run after the node
				  runList.insertAfterNode(2, 1, currNode);
				  // Format the empty run correctly
				  if(currNode.next.next.aniLength == 1) {
					  runList.removeAfterNode(currNode.next);
				  } else {
					  currNode.next.next.aniLength--;
				  }
				  // Checks if there is a fish to the left
				  if(currNode.aniType.getType() == 2) {
					  // Checks if there is one to the right ALSO
					  if(currNode.next.next.aniType.getType() == 2) {
						  currNode.aniLength++;
						  currNode.aniLength+=currNode.next.next.aniLength;
						  runList.removeAfterNode(currNode);
						  runList.removeAfterNode(currNode);
					  } else {
						  currNode.aniLength++;
						  runList.removeAfterNode(currNode);   
					  }
				  } else if(currNode.next.next.aniType.getType() == 2) {
					  currNode.next.next.aniLength++;
					  runList.removeAfterNode(currNode);
				  }
			  }
			  break;
		  }
		  prevIndex=currIndex;
	  }
	  check();
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  The final run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs of sharks with the same degree
   *  of hunger.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
	  int index = y*i+x;
	  int listSize = runList.getSize();
	  int prevIndex = 0;
	  int currIndex = 0;
	  currNode = null;
	  // Loop through the list
	  for(int m=0; m<listSize; m++) {
		  TypeAndSize currData = nextRun();
		  currIndex += currData.size;
		  // This clause is if the index we want is inside a run that is empty
		  if(index > prevIndex && index < currIndex) {
			  if(currNode.aniType == null) {
				  // Add in a shark
				  runList.insertAfterNode(1, 1, starveTime, currNode);
				  // Scale the current empty run back
				  currNode.aniLength=index - prevIndex;
				  // Add in an empty run that is of correct length
				  if(currIndex-index-1 != 0) {
					  runList.insertAfterNode(0, currIndex-index-1, currNode.next);
				  } else {
					  // If we dont add an empty run after because the length is zero, then check if there is a fish next to it
					  if(currNode.next.next.aniType.getType() == 1) {
						  currNode.next.aniLength += currNode.next.next.aniLength;
						  runList.removeAfterNode(currNode.next);
					  }
				  }
			  } 
			  break;
		  }
		  // This clause is if the index we want is at the border
		  if(index > prevIndex && index <= currIndex) {
			  if(currNode.next.aniType == null) {
				  // Insert a run after the node
				  runList.insertAfterNode(1, 1, starveTime, currNode);
				  // Format the empty run correctly
				  if(currNode.next.next.aniLength == 1) {
					  runList.removeAfterNode(currNode.next);
				  } else {
					  currNode.next.next.aniLength--;
				  }
				  // Checks if there is a shark to the left
				  if(currNode.aniType.getType() == 1) {
					  // Checks if there is one to the right ALSO
					/*  System.out.println(currNode.aniType.getType());
					  System.out.println(currNode.aniLength);
					  System.out.println(currNode.next.aniType.getType());
					  System.out.println(currNode.next.aniLength);
					  //System.out.println(currNode.next.next.aniType.getType());
					  System.out.println(currNode.next.next.aniLength);
					  if(currNode.next.next.aniType.getType() == 1) {
						  System.out.println("Do I get here?");
						  if(((Shark) currNode.next.next.aniType).dyingTime() == starveTime) {
							  currNode.next.aniLength += currNode.next.next.aniLength;
							  runList.removeAfterNode(currNode.next);
						  }
					  }*/
					  // Then check it's starveTime
					  if(((Shark)currNode.aniType).dyingTime() == starveTime) {
						  currNode.aniLength+=currNode.next.aniLength;
						  runList.removeAfterNode(currNode);
					  }
				  } else if(currNode.next.next.aniType.getType() == 1) {
					  // Checks if there's a shark to the right
					  // Then check it's starveTime
					  if(((Shark) currNode.next.next.aniType).dyingTime() == starveTime) {
						  currNode.next.next.aniLength++;
						  runList.removeAfterNode(currNode);
					  }
				  }
			  }
			  break;
		  }
		  prevIndex = currIndex;
	  }
	  check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same contents, or if the sum of all run
   *  lengths does not equal the number of cells in the ocean.
   */

  public void check() {
	  int totSize = i*j;
	  int sizeHolder = 0;
	  TypeAndSize currData;
	  int listSize = runList.getSize();
	  currNode = null;
	  for(int i=0; i<listSize; i++) {
		  currData = nextRun();
		  sizeHolder+=currData.size;
		  if(i!=0) {
			  //check types
			  if(currNode.aniType == currNode.prev.aniType && currNode.aniType.getType() == 1) {
				  if(((Shark) currNode.aniType).dyingTime() == ((Shark) currNode.prev.aniType).dyingTime()) {
					  System.out.println("There are sharks that are next to each other");
				  } else if(currNode.aniType == currNode.prev.aniType) {
					  System.out.println("There are fish that are next to each other");
				  }
			  }
		  }
	  }
	  if(sizeHolder!=totSize) {
		  System.out.println("Wrong SIZES WITH DLIST AND OCEAN LENGTH");
	  }
  }
  
  public static void paint(Ocean sea) {
	  if (sea != null) {
		  int width = sea.width();
		  int height = sea.height();

	      /* Draw the ocean. */
	      for (int x = 0; x < width + 2; x++) {
	    	  System.out.print("-");
	      }
	      System.out.println();
	      for (int y = 0; y < height; y++) {
	    	  System.out.print("|");
	    	  for (int x = 0; x < width; x++) {
	    		  int contents = sea.cellContents(x, y);
	    		  if (contents == Ocean.SHARK) {
	    			  System.out.print('S');
	    		  } else if (contents == Ocean.FISH) {
	    			  System.out.print('~');
	    		  } else {
	    			  System.out.print(' ');
	    		  }
	    	  }
	    	  System.out.println("|");
	      }
	      for (int x = 0; x < width + 2; x++) {
	    	  System.out.print("-");
	      }
	      System.out.println();
	  }
  }	
  
  public static void main(String[] argv) {
	  int i=3;
	  int j=3;
	  int starvetime = 3;
	  RunLengthEncoding RLE = new RunLengthEncoding(i,j,starvetime);
	  RLE.addFish(1,1);
	  RLE.addFish(2,1);
	  RLE.addShark(1,2);
  }
}
