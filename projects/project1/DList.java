
public class DList {
	protected DListNode head;
	protected int size;
	
	public DList() {
		head = new DListNode();
		head.aniType = null;
		head.next = head;
		head.prev = head;
		size = 0;
	}
	
	public DList(int type, int length) {
		head = new DListNode();
		head.next = new DListNode(type, length);
		head.prev = head.next;
		head.next.prev = head;
		head.prev.next = head;
		size++;
	}
	
	public void insertFront(int type, int length) {
		DListNode tempNode = new DListNode(type, length);
		if(size ==0) {
			head.next = tempNode;
			head.prev = tempNode;
			tempNode.prev = head;
			tempNode.next = head;
		} else {
			tempNode.next = head.next;
			tempNode.next.prev = tempNode;
			tempNode.prev = head;
			head.next = tempNode;
		}
		size++;
	}

	public void insertBack(int type, int length) {
		DListNode tempNode = new DListNode(type, length);
		if(size==0) {
			head.next = tempNode;
			head.prev = tempNode;
			tempNode.next = head;
			tempNode.prev = head;
		} else {
			tempNode.prev=head.prev;
			tempNode.prev.next = tempNode;
			tempNode.next = head;
			head.prev = tempNode;
		}
		size++;
	}

	public void insertBack(int type, int length, int deathTime) {
		DListNode tempNode = new DListNode(type, length, deathTime);
		if(size==0) {
			head.next = tempNode;
			head.prev = tempNode;
			tempNode.next = head;
			tempNode.prev = head;
		} else {
			tempNode.prev=head.prev;
			tempNode.prev.next = tempNode;
			tempNode.next = head;
			head.prev = tempNode;
		}
		size++;
	}
	
	public int getSize() {
		return size;
	}
}	
	
	