
public class DListNode {
	public DListNode next;
	public DListNode prev;
	public Animal aniType;
	public int aniLength;
	
	public DListNode() {
		next = null;
		prev = null;
		aniType = null;
		aniLength = 0;
	}
	
	public DListNode(int type, int aniLength) {
		next = null;
		prev = null;
		if(type == 1) {
			aniType = new Animal(type);
		} else if(type == 2) {
			aniType = new Animal(type);
		} else if(type == 0) {
			aniType = null;
		}
		this.aniLength = aniLength;
	}
	
	public DListNode(int type, int aniLength, int timeDeath) {
		next = null;
		prev = null;
		aniType = new Shark(timeDeath);
		this.aniLength = aniLength;
	}
}
