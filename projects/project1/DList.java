
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
	
	public void insertAfterNode(int type, int length, DListNode curr) {
		DListNode newNode = new DListNode(type, length);
		newNode.next = curr.next;
		newNode.prev = curr;
		curr.next.prev = newNode;
		curr.next = newNode;
		size++;
	}
	
	public void insertAfterNode(int type, int length, int deathTime, DListNode curr) {
		DListNode newNode = new DListNode(type, length, deathTime);
		newNode.next = curr.next;
		newNode.prev = curr;
		curr.next.prev = newNode;
		curr.next = newNode;
		size++;
	}
	
	public void removeAfterNode(DListNode curr) {
		curr.next = curr.next.next;
		curr.next.prev = curr;
		size--;
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
	
	