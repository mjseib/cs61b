package list;

public class LockDList extends DList {

    public LockDList() {
	head = new DListNode(null, head, head);
	size = 0;
    }

    protected LockDListNode newNode(Object item, DListNode prev, DListNode next) {
	return new LockDListNode(item, prev, next);
    }
    
    public void lockNode(DListNode node) {
	((LockDListNode) node).isLocked = true;
   }

    public void remove(DListNode node) {
	if(((LockDListNode) node).isLocked == true) {
	} else {
	    super.remove(node);
	}
    }

    public static void main(String[] argv) {
	System.out.println("Testing Constructor");
	LockDList testList = new LockDList();
	System.out.println("Is empty? Should be true: " + testList.isEmpty());
	System.out.println("Should be zero length: " + testList.length());

	System.out.println("\nTesting insertFront");
	testList.insertFront(1);
	System.out.println("Is empty? Should be false: " + testList.isEmpty());
	System.out.println("Should be one length: " + testList.length());
	System.out.println("Should be [ 1 ]: " + testList);
	testList.insertFront(3);
	testList.insertFront(6);
	testList.insertFront(9);
	System.out.println(testList);

	LockDListNode currNode = (LockDListNode) ((DList) testList).front();
	currNode = (LockDListNode) testList.next(currNode);
	currNode = (LockDListNode) testList.next(currNode);
	//testList.lockNode(currNode);
	testList.remove(currNode);

	System.out.println(testList);
    }
}