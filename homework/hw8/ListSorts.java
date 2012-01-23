/* ListSorts.java */

import list.*;

public class ListSorts {

  private final static int SORTSIZE = 10;

  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
	  LinkedQueue returnQ = new LinkedQueue();
	  try {
		  while(q.size()>0) {
			  Comparable currentItem = (Comparable) q.dequeue();
			  LinkedQueue currentQ = new LinkedQueue();
			  currentQ.enqueue(currentItem);
			  returnQ.enqueue(currentQ);
		  }
	  } catch(QueueEmptyException e) {
		  System.out.println("Empty node when dequeuing");
	  }
	  return returnQ;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
@SuppressWarnings({ "rawtypes", "unchecked" })
public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
	  	LinkedQueue returnQ = new LinkedQueue();
	  	try {
	  		while(q1.size()>0 && q2.size()>0) {
	  			Comparable item1 = (Comparable) q1.front();	
	  			Comparable item2 = (Comparable) q2.front();
	  			int comparison = item1.compareTo(item2);
	  			if(comparison >= 0) {
	  				returnQ.enqueue(item2);
	  				q2.dequeue();
	  			} else {
	  				returnQ.enqueue(item1);
	  				q1.dequeue();
	  			}
	  		}
	  		
	  		if(q1.isEmpty()) {
	  			returnQ.append(q2);
	  		} else {
	  			returnQ.append(q1);
	  		}
	  	} catch(QueueEmptyException e) {
	  		System.out.println("Empty node when merging");
	  	}
	  return returnQ;
  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  @SuppressWarnings("rawtypes")
public static void partition(LinkedQueue qIn, Comparable pivot, 
                               LinkedQueue qSmall, LinkedQueue qEquals, 
                               LinkedQueue qLarge) {
    // Your solution here.
	  try {
		  while(!qIn.isEmpty()) {
			  Comparable qItem = (Comparable) qIn.dequeue();
			  int comparison = qItem.compareTo(pivot);
			  if(comparison < 0) {
				  qSmall.enqueue(qItem);
			  } else if(comparison > 0) {
				  qLarge.enqueue(qItem);
			  } else {
				  qEquals.enqueue(qItem);
			  }
		  }
	  } catch(QueueEmptyException e) {
		  System.out.println("empty queue in partition");
	  }
  }

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
	  if(q.size()<=1) {
		  return;
	  }
	  LinkedQueue separatedQ = makeQueueOfQueues(q);
	  try {
		  while(separatedQ.size()>1) {
			  LinkedQueue q1 = (LinkedQueue) separatedQ.dequeue();
			  LinkedQueue q2 = (LinkedQueue) separatedQ.dequeue();
			  LinkedQueue mergedQ = mergeSortedQueues(q1, q2);
			  separatedQ.enqueue(mergedQ);
		  }
		  q.append((LinkedQueue) separatedQ.dequeue());
	  } catch(QueueEmptyException e) {
		  System.out.println("Empty queue doing mergeSort");
	  }
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void quickSort(LinkedQueue q) {
	  if(q.size() <= 1) {
		  return;
	  }
    // Your solution here.
	  int pivotIndex = (int) (Math.random() * q.size() +1);
	  Comparable pivot = (Comparable) q.nth(pivotIndex);
	  LinkedQueue qSmall = new LinkedQueue();
	  LinkedQueue qEqual = new LinkedQueue();
	  LinkedQueue qLarge = new LinkedQueue();
	  partition(q, pivot, qSmall, qEqual, qLarge);
	  quickSort(qSmall);
	  quickSort(qLarge);
	  q.append(qSmall);
	  q.append(qEqual);
	  q.append(qLarge);
  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }

  /**
   *  main() performs some tests on mergesort and quicksort.  Feel free to add
   *  more tests of your own to make sure your algorithms works on boundary
   *  cases.  Your test code will not be graded.
   **/
  public static void main(String [] args) {

	/*LinkedQueue tq1 = makeRandom(5);
	System.out.println(tq1);
	LinkedQueue tq2 = makeRandom(10);
	System.out.println(tq2);
	mergeSort(tq1);
	mergeSort(tq2);
	System.out.println(tq1);
	System.out.println(tq2);
	LinkedQueue tq12 = mergeSortedQueues(tq1, tq2);
	System.out.println(tq12);
	  LinkedQueue tq2 = new LinkedQueue();
	  mergeSort(tq2);*/
	
    LinkedQueue q = makeRandom(10);
    System.out.println(q.toString());
    mergeSort(q);
    System.out.println(q.toString());

    q = makeRandom(10);
    System.out.println(q.toString());
    quickSort(q);
    System.out.println(q.toString());

    Timer stopWatch = new Timer();
    q = makeRandom(SORTSIZE);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + SORTSIZE + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(SORTSIZE);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + SORTSIZE + " Integers:  " +
                       stopWatch.elapsed() + " msec.");
  
  }

}
