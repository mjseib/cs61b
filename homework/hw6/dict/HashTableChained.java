/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
    protected int entryNum; //n
    protected List[] hashArray;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
      entryNum = 0;
      hashArray = new DList[findBuckets(sizeEstimate)];
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
      entryNum = 0;
      hashArray = new DList[97];
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    // Replace the following line with your solution.
      return ((600*code+99)%2147483647)%(hashArray.length);
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    return entryNum;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
	  if(entryNum == 0) {
		  return true;
	  } else {
		  return false;
	  }
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
      Entry enter = new Entry();
      int bucket = Math.abs(compFunction(key.hashCode()));
      enter.key = key;
      enter.value = value;
      
      if(hashArray[bucket]==null) {
	  hashArray[bucket] = new DList();
      }
      hashArray[bucket].insertBack(enter);
      
      entryNum++;

      return enter;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
      // Replace the following line with your solution.
      int bucket = compFunction(key.hashCode());
      try {
	  ListNode currNode = hashArray[bucket].front();
	  for(int i=0; i<hashArray[bucket].length(); i++) {
	      if(((Entry) currNode.item()).key().equals(key)) {
		  return ((Entry) currNode.item());
	      } else {
		  currNode = currNode.next();
	      }
	  }
      } catch(InvalidNodeException e) {
	  System.out.println(e);
      }
      
      return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

    public Entry remove(Object key) {
	// Replace the following line with your solution.
	int bucket = compFunction(key.hashCode());
	try {
	    ListNode currNode = hashArray[bucket].front();
	    for(int i=0; i<hashArray[bucket].length(); i++) {
		if(((Entry) currNode.item()).key().equals(key)) {
		    currNode.remove();
		    entryNum--;
		    return ((Entry) currNode.item());
		} else {
		    currNode = currNode.next();
		}
	    }
	} catch(InvalidNodeException e) {
	    System.out.println(e);
	}
	return null;
    }
    
    /**
     *  Remove all entries from the dictionary.
     */
    public void makeEmpty() {
	// Your solution here.
	entryNum = 0;
	hashArray = new DList[hashArray.length];
    }

    public int numCollisions() {
	int collision=0;
	for(int i=0; i<hashArray.length; i++) {
	    if(hashArray[i] == null) {
	    } else {
		collision += hashArray[i].length();
	    }
	}
	return collision;
    }
  
  private static int findBuckets(int estimate) {
	  double loadFactor = 0.7;
	  
	  return findPrimes((int) (estimate/loadFactor));
  }

  private static int findPrimes(int nearPrime) {
	  boolean[] prime = new boolean[nearPrime+1];
	  for(int i=0; i<=nearPrime; i++) {
		  prime[i] = true;
	  }
	  for(int divisor=2; divisor*divisor<=nearPrime; divisor++) {
		  if(prime[divisor]) {
			  for(int i=2*divisor; i<=nearPrime; i=i+divisor) {
				  prime[i] = false;
			  }
		  }
	  }
	  for(int i = nearPrime; i>=0; i--) {
		  if(prime[i]) {
			  return i;
		  }
	  }
	  return 0;
  }
  
  public static void main(String[] argv) {
	  	HashTableChained table = new HashTableChained();
	  	table.insert("WTF", "WTF");
	  	table.insert("CAT", "MEOW");
	  	table.insert("DOG", "BARK");
	  	table.insert("HUMAN", "WHYYY");
	  	table.insert(10, 10);
	  	
	  	Entry a = table.find("WTF");
	  	Entry b = table.find("CAT");
	  	table.remove("DOG");
	  	Entry c = table.find("DOG");
	  	System.out.println("Should be 4: "+table.size());
  }
}
